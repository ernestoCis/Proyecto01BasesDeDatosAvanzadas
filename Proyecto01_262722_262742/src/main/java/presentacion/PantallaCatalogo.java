package presentacion;

import dominio.Cliente;
import dominio.ItemCarrito;
import dominio.Producto;
import imagenes.ImagenProductoUtil;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;
import negocio.BOs.iCuponBO;
import negocio.BOs.iPedidoBO;
import negocio.BOs.iProductoBO;
import negocio.BOs.iUsuarioBO;
import negocio.Excepciones.NegocioException;

/**
 * <h1>PantallaCatalogo</h1>
 *
 * <p>
 * Pantalla que muestra el <b>catálogo de productos</b> para el cliente (flujo
 * no express).
 * </p>
 *
 * <p>
 * La UI presenta:
 * </p>
 * <ul>
 * <li>Un botón de regreso hacia {@link Menu}.</li>
 * <li>Un ícono de carrito con un <b>badge</b> que indica el total de piezas
 * agregadas.</li>
 * <li>Un grid (3 columnas) con tarjetas de productos cargadas desde la BD.</li>
 * <li>Un footer informativo.</li>
 * </ul>
 *
 * <h2>Comportamiento del carrito</h2>
 * <ul>
 * <li>El carrito se mantiene en memoria como una lista de
 * {@link ItemCarrito}.</li>
 * <li>Cada tarjeta tiene un stepper (+ / −) para ajustar la cantidad.</li>
 * <li>Al cambiar cantidades, se actualiza el badge del carrito.</li>
 * <li>Al dar click en el ícono 🛒 se abre {@link PantallaCarrito} con el
 * carrito actual.</li>
 * </ul>
 *
 * <h2>Carga de datos</h2>
 * <p>
 * Los productos se consultan desde la capa de negocio usando
 * {@code ctx.getProductoBO().listarProductos()}. Si no hay productos
 * disponibles, se muestra un mensaje "No hay productos para mostrar.".
 * </p>
 *
 * @author 262722, 2627242
 */
public class PantallaCatalogo extends JFrame {

    /**
     * Contexto global de la aplicación; permite acceder a BOs y estado de
     * sesión.
     */
    private final AppContext ctx;

    /**
     * Lista en memoria que representa el carrito del cliente en el catálogo.
     */
    private final java.util.List<ItemCarrito> carrito = new java.util.ArrayList<>();

    /**
     * Panel contenedor del grid donde se insertan las tarjetas de productos.
     */
    private JPanel grid;

    /**
     * Badge que muestra el total de piezas agregadas al carrito.
     */
    private JLabel badgeCarrito;

    /**
     * <p>
     * Constructor del catálogo de productos.
     * </p>
     *
     * <p>
     * Construye toda la interfaz:
     * </p>
     * <ul>
     * <li>Marco beige y tarjeta blanca</li>
     * <li>Barra superior con botón de regreso y carrito</li>
     * <li>Encabezado con títulos</li>
     * <li>Scroll con grid de productos (3 columnas)</li>
     * </ul>
     *
     * <p>
     * Finalmente invoca {@link #cargarProductosDesdeBD()} para mostrar el
     * contenido real.
     * </p>
     *
     * @param ctx contexto global de la aplicación
     */
    public PantallaCatalogo(AppContext ctx) {
        this.ctx = ctx;

        setTitle("Panadería - Catálogo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 600);
        setLocationRelativeTo(null);

        // ======================
        // Fondo beige
        // ======================
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(new Color(214, 186, 150));
        setContentPane(root);

        // ======================
        // Tarjeta blanca con borde
        // ======================
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(30, 30, 30), 2, false),
                new EmptyBorder(18, 22, 18, 22)
        ));
        card.setPreferredSize(new Dimension(860, 520));
        root.add(card);

        // ======================
        // Parte superior (back + carrito)
        // ======================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        /**
         * Botón de regreso a {@link Menu}.
         */
        JButton btnFlecha = new JButton("←️");
        btnFlecha.setFocusPainted(false);
        btnFlecha.setBorderPainted(false);
        btnFlecha.setContentAreaFilled(false);
        btnFlecha.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        btnFlecha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnFlecha.addActionListener(e -> {
            new Menu(ctx).setVisible(true);
            dispose();
        });

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.add(btnFlecha);

        /**
         * Panel contenedor del icono de carrito y su badge. Usa layout null
         * para posicionar manualmente el badge.
         */
        JPanel panelCarrito = new JPanel(null);
        panelCarrito.setOpaque(false);
        panelCarrito.setPreferredSize(new Dimension(90, 50));

        JLabel lblCarrito = new JLabel("🛒");
        lblCarrito.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
        lblCarrito.setBounds(35, 2, 50, 45);
        lblCarrito.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        badgeCarrito = new JLabel("0", SwingConstants.CENTER);
        badgeCarrito.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badgeCarrito.setOpaque(true);
        badgeCarrito.setBackground(Color.WHITE);
        badgeCarrito.setBorder(new LineBorder(new Color(30, 30, 30), 1));
        badgeCarrito.setBounds(18, 0, 22, 18);

        panelCarrito.add(badgeCarrito);
        panelCarrito.add(lblCarrito);

        /**
         * Abre la pantalla del carrito al hacer click en el icono.
         */
        lblCarrito.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                PantallaCarrito pantallaCarrito = new PantallaCarrito(
                        PantallaCatalogo.this,
                        carrito,
                        ctx,
                        ctx.getClienteActual()
                );
                pantallaCarrito.setVisible(true);
                setVisible(false);
            }
        });

        topBar.add(panelIzquierdo, BorderLayout.WEST);
        topBar.add(panelCarrito, BorderLayout.EAST);

        // ======================
        // Títulos
        // ======================
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Panadería");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 56));

        JLabel subtitle = new JLabel("Catálogo de productos");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 22));

        header.add(Box.createVerticalStrut(2));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(10));

        // ======================
        // Grid + Scroll
        // ======================
        grid = new JPanel(new GridLayout(0, 3, 28, 18));
        grid.setBackground(Color.WHITE);
        grid.setBorder(new EmptyBorder(6, 10, 12, 10));

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(header, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);

        // ======================
        // Parte inferior (footer)
        // ======================
        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(footer, BorderLayout.WEST);

        // Ensamble final
        card.add(topBar, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        // Cargar desde BD
        cargarProductosDesdeBD();
    }

    /**
     * <p>
     * Consulta los productos desde la capa de negocio y los renderiza en el
     * grid.
     * </p>
     *
     * <p>
     * Si no hay productos, cambia el layout del grid y muestra un mensaje
     * centrado.
     * </p>
     *
     * <p>
     * En caso de error, muestra un {@link JOptionPane} con el detalle.
     * </p>
     */
    private void cargarProductosDesdeBD() {
        try {
            List<Producto> productos = ctx.getProductoBO().listarProductos();

            grid.removeAll();

            if (productos == null || productos.isEmpty()) {
                grid.setLayout(new BorderLayout());
                JLabel vacio = new JLabel("No hay productos para mostrar.", SwingConstants.CENTER);
                vacio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                grid.add(vacio, BorderLayout.CENTER);
            } else {
                grid.setLayout(new GridLayout(0, 3, 28, 18));
                for (Producto p : productos) {
                    grid.add(crearTarjetaProducto(p));
                }
            }

            grid.revalidate();
            grid.repaint();

        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar productos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * <p>
     * Crea una tarjeta de producto para mostrar en el grid.
     * </p>
     *
     * <p>
     * La tarjeta incluye:
     * </p>
     * <ul>
     * <li>Nombre del producto</li>
     * <li>Imagen (si existe en {@code imagenes/productos/{id}.png})</li>
     * <li>Tipo y precio</li>
     * <li>Botón de información</li>
     * <li>Stepper (+ / −) para ajustar cantidad en carrito</li>
     * </ul>
     *
     * @param p producto a renderizar
     * @return panel con la tarjeta construida
     */
    private JPanel crearTarjetaProducto(Producto p) {
        JPanel item = new JPanel();
        item.setOpaque(false);
        item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));

        JLabel lblNombre = new JLabel(p.getNombre(), SwingConstants.CENTER);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel img = new JLabel("Sin imagen", SwingConstants.CENTER);
        img.setPreferredSize(new Dimension(180, 100));
        img.setMaximumSize(new Dimension(180, 100));
        img.setAlignmentX(Component.CENTER_ALIGNMENT);
        img.setOpaque(true);
        img.setBackground(new Color(245, 245, 245));
        img.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));

        ImageIcon icon = ImagenProductoUtil.cargarIconoProducto(p.getId(), 180, 100);
        if (icon != null) {
            img.setText("");
            img.setIcon(icon);
        }

        String tipo = (p.getTipo() != null) ? p.getTipo().toString() : "N/A";
        JLabel lblTipoPrecio = new JLabel("Tipo: " + tipo + "   Precio: $" + Math.round(p.getPrecio()));
        lblTipoPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTipoPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setOpaque(false);

        JButton btnInfo = new JButton("i");
        btnInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnInfo.setFocusPainted(false);
        btnInfo.setPreferredSize(new Dimension(26, 24));
        btnInfo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnInfo.setBorder(new LineBorder(new Color(60, 60, 60), 1, true));

        /**
         * Muestra un diálogo con información detallada del producto.
         */
        btnInfo.addActionListener(e -> {
            String estado = (p.getEstado() != null) ? p.getEstado().toString() : "N/A";
            String desc = (p.getDescripcion() != null) ? p.getDescripcion() : "(Sin descripción)";

            JOptionPane.showMessageDialog(
                    this,
                    "Nombre: " + p.getNombre()
                    + "\nTipo: " + tipo
                    + "\nPrecio: $" + p.getPrecio()
                    + "\nEstado: " + estado
                    + "\nDescripción: " + desc,
                    "Detalle del producto",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        JPanel stepper = crearStepperCantidad(p);

        row.add(btnInfo);
        row.add(stepper);

        item.add(lblNombre);
        item.add(Box.createVerticalStrut(6));
        item.add(img);
        item.add(Box.createVerticalStrut(10));
        item.add(lblTipoPrecio);
        item.add(Box.createVerticalStrut(6));
        item.add(row);

        return item;
    }

    /**
     * <p>
     * Crea el componente stepper (+ / −) que permite ajustar la cantidad de un
     * producto dentro del carrito.
     * </p>
     *
     * <p>
     * El valor inicial se calcula buscando si ya existe un {@link ItemCarrito}
     * asociado a ese producto.
     * </p>
     *
     * <p>
     * Cada cambio:
     * </p>
     * <ul>
     * <li>Actualiza el campo de cantidad</li>
     * <li>Actualiza la lista del carrito mediante
     * {@link #setCantidad(dominio.Producto, int)}</li>
     * <li>Refresca el badge del carrito con {@link #totalPiezas()}</li>
     * </ul>
     *
     * @param producto producto al que pertenece el stepper
     * @return panel con el stepper construido
     */
    private JPanel crearStepperCantidad(dominio.Producto producto) {
        JPanel stepper = new JPanel(new BorderLayout());
        stepper.setOpaque(false);
        stepper.setPreferredSize(new Dimension(130, 26));

        JButton btnMenos = new JButton("−");
        JButton btnMas = new JButton("+");

        int inicial = 0;
        ItemCarrito it = buscarItem(producto.getId());
        if (it != null) {
            inicial = it.getCantidad();
        }

        JTextField txtCantidad = new JTextField(String.valueOf(inicial));
        txtCantidad.setHorizontalAlignment(SwingConstants.CENTER);
        txtCantidad.setEditable(false);

        Dimension btnSize = new Dimension(46, 26);
        btnMenos.setPreferredSize(btnSize);
        btnMas.setPreferredSize(btnSize);
        btnMenos.setFocusPainted(false);
        btnMas.setFocusPainted(false);
        txtCantidad.setBorder(new LineBorder(new Color(120, 120, 120), 1));

        stepper.add(btnMenos, BorderLayout.WEST);
        stepper.add(txtCantidad, BorderLayout.CENTER);
        stepper.add(btnMas, BorderLayout.EAST);

        btnMenos.addActionListener(e -> {
            int v = Integer.parseInt(txtCantidad.getText());
            if (v > 0) {
                v--;
            }
            txtCantidad.setText(String.valueOf(v));
            setCantidad(producto, v);
            badgeCarrito.setText(String.valueOf(totalPiezas()));
        });

        btnMas.addActionListener(e -> {
            int v = Integer.parseInt(txtCantidad.getText());
            v++;
            txtCantidad.setText(String.valueOf(v));
            setCantidad(producto, v);
            badgeCarrito.setText(String.valueOf(totalPiezas()));
        });

        return stepper;
    }

    /**
     * Busca un {@link ItemCarrito} por id de producto dentro de la lista
     * {@link #carrito}.
     *
     * @param idProducto id del producto
     * @return item encontrado o null si no existe
     */
    private ItemCarrito buscarItem(int idProducto) {
        for (ItemCarrito it : carrito) {
            if (it.getProducto().getId() == idProducto) {
                return it;
            }
        }
        return null;
    }

    /**
     * <p>
     * Ajusta la cantidad de un producto en el carrito.
     * </p>
     *
     * <ul>
     * <li>Si {@code cantidad <= 0}, elimina el item si existe.</li>
     * <li>Si no existía y {@code cantidad > 0}, crea un nuevo item.</li>
     * <li>Si existía, actualiza la cantidad.</li>
     * </ul>
     *
     * @param producto producto a modificar
     * @param cantidad nueva cantidad
     */
    private void setCantidad(dominio.Producto producto, int cantidad) {
        ItemCarrito it = buscarItem(producto.getId());

        if (cantidad <= 0) {
            if (it != null) {
                carrito.remove(it);
            }
            return;
        }

        if (it == null) {
            carrito.add(new ItemCarrito(producto, cantidad));
        } else {
            it.setCantidad(cantidad);
        }
    }

    /**
     * Calcula el total de piezas sumando las cantidades de todos los items del
     * carrito.
     *
     * @return suma total de piezas
     */
    private int totalPiezas() {
        int s = 0;
        for (ItemCarrito it : carrito) {
            s += it.getCantidad();
        }
        return s;
    }
}
