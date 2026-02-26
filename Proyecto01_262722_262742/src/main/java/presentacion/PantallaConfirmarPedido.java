package presentacion;

import dominio.Cliente;
import dominio.DetallePedido;
import dominio.EstadoPedido;
import dominio.ItemCarrito;
import dominio.MetodoPago;
import dominio.PedidoProgramado;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import negocio.Excepciones.NegocioException;

/**
 * <p>
 * Pantalla final del flujo de <b>pedido programado</b> donde el cliente revisa
 * su carrito, puede capturar <b>notas por producto</b>, validar un <b>cupón
 * (opcional)</b>, seleccionar el {@link MetodoPago} y confirmar la creación del
 * pedido.
 * </p>
 *
 * <p>
 * La UI presenta:
 * </p>
 * <ul>
 * <li>Botón de regreso a la {@link JFrame} anterior.</li>
 * <li>Tabla de productos con columna de <b>Notas</b> editable.</li>
 * <li>Campo para cupón + botón <b>Validar</b> + indicadores de
 * válido/inválido.</li>
 * <li>Selector de {@link MetodoPago}.</li>
 * <li>Resumen: <b>Subtotal</b>, <b>Descuento</b> y <b>Total a pagar</b>.</li>
 * <li>Botón <b>Realizar pedido</b> que persiste el pedido mediante la capa de
 * negocio.</li>
 * </ul>
 *
 * <h2>Datos y reglas</h2>
 * <ul>
 * <li>El carrito se recibe como {@link java.util.List} de
 * {@link ItemCarrito}.</li>
 * <li>El subtotal se calcula sumando precio*cantidad de cada item del
 * carrito.</li>
 * <li>El cupón es opcional; si es válido, aplica un descuento calculado por la
 * capa de negocio.</li>
 * <li>El método de pago inicia en {@link MetodoPago#Efectivo} como valor
 * default.</li>
 * <li>Antes de confirmar, si la tabla está editándose, se fuerza
 * {@code stopCellEditing()} para guardar la nota.</li>
 * </ul>
 *
 * <h2>Persistencia del pedido</h2>
 * <p>
 * Al confirmar, se construye un {@link PedidoProgramado} y una lista de
 * {@link DetallePedido} con los datos capturados en la tabla. Posteriormente se
 * invoca
 * {@code ctx.getPedidoBO().agregarPedidoProgramado(pedidoProgramado, detalles)}
 * y, si se registra, se navega a {@link PantallaPedidoProgramadoRealizado}.
 * </p>
 *
 * @author
 */
public class PantallaConfirmarPedido extends JFrame {

    /**
     * Referencia a la pantalla anterior para poder regresar al flujo previo.
     */
    private final JFrame pantallaAnterior;

    /**
     * Lista de items que representan el carrito actual a confirmar.
     */
    private final List<ItemCarrito> carrito;

    /**
     * Tabla que muestra el contenido del carrito y permite capturar notas por
     * renglón.
     */
    private JTable tabla;

    /**
     * Modelo de la tabla; contiene las filas renderizadas desde
     * {@link #carrito}.
     */
    private DefaultTableModel modelo;

    /**
     * Campo de texto donde el usuario escribe el código del cupón (opcional).
     */
    private JTextField txtCupon;

    /**
     * Indicador visual de cupón válido.
     */
    private JLabel lblCuponValido;

    /**
     * Indicador visual de cupón inválido.
     */
    private JLabel lblCuponInvalido;

    /**
     * Etiqueta del subtotal calculado.
     */
    private JLabel lblSubtotal;

    /**
     * Etiqueta del descuento aplicado por cupón.
     */
    private JLabel lblDescuento;

    /**
     * Etiqueta del total final a pagar (subtotal - descuento).
     */
    private JLabel lblTotal;

    /**
     * Contexto global de la aplicación; permite acceder a BOs y estado de
     * sesión.
     */
    private final AppContext ctx;

    /**
     * Cliente asociado al pedido programado a registrar.
     */
    private final Cliente cliente;

    /**
     * Descuento actual aplicado al pedido (si el cupón es válido).
     */
    private float descuentoActual = 0;

    /**
     * Subtotal calculado a partir del carrito.
     */
    private float subtotal = 0;

    /**
     * Método de pago seleccionado para el pedido. Inicia en efectivo como
     * default.
     */
    private MetodoPago metodoPago = MetodoPago.Efectivo; //efectivo como default

    /**
     * <p>
     * Constructor de la pantalla de confirmación.
     * </p>
     *
     * <p>
     * Construye la interfaz con:
     * </p>
     * <ul>
     * <li>Fondo beige y tarjeta blanca con borde.</li>
     * <li>Barra superior con botón de regreso.</li>
     * <li>Tabla del carrito (columna Notas editable).</li>
     * <li>Sección de cupón con validación.</li>
     * <li>Selector de {@link MetodoPago}.</li>
     * <li>Resumen de importes y botón para realizar el pedido.</li>
     * </ul>
     *
     * <p>
     * Al iniciar, carga la tabla usando {@link #cargarTablaDesdeCarrito()} y
     * calcula el resumen con {@link #calcularSubtotal()}.
     * </p>
     *
     * @param pantallaAnterior frame anterior al que se regresa al presionar la
     * flecha
     * @param carrito lista de items del carrito a confirmar
     * @param ctx contexto global de la aplicación
     * @param cliente cliente asociado al pedido programado
     */
    public PantallaConfirmarPedido(JFrame pantallaAnterior, List<ItemCarrito> carrito, AppContext ctx, Cliente cliente) {
        this.pantallaAnterior = pantallaAnterior;
        this.carrito = carrito;

        this.ctx = ctx;
        this.cliente = cliente;

        setTitle("Panadería - Confirmar pedido");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 650);
        setLocationRelativeTo(null);

        // Fondo beige
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(new Color(214, 186, 150));
        setContentPane(root);

        // Tarjeta blanca con borde
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(30, 30, 30), 2, false),
                new EmptyBorder(18, 22, 18, 22)
        ));
        card.setPreferredSize(new Dimension(860, 560));
        root.add(card);

        // ----- parte de arriba -----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        /**
         * Botón de regreso; muestra la pantalla anterior (si existe) y cierra
         * la actual.
         */
        JButton btnFlecha = new JButton("←");
        btnFlecha.setFocusPainted(false);
        btnFlecha.setBorderPainted(false);
        btnFlecha.setContentAreaFilled(false);
        btnFlecha.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        btnFlecha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFlecha.addActionListener(e -> {
            if (pantallaAnterior != null) {
                pantallaAnterior.setVisible(true);
            }
            dispose();
        });

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelIzq.setOpaque(false);
        panelIzq.add(btnFlecha);

        topBar.add(panelIzq, BorderLayout.WEST);

        // ----- titulo -----
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Panadería");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 56));

        JLabel subtitle = new JLabel("Confirmar pedido");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 22));

        header.add(Box.createVerticalStrut(2));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(12));

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(topBar, BorderLayout.NORTH);
        north.add(header, BorderLayout.CENTER);

        card.add(north, BorderLayout.NORTH);

        // ----- tabla -----
        String[] cols = {"ID", "Producto", "Precio pz.", "Cantidad", "Subtotal", "Notas"};

        /**
         * Modelo de tabla con edición habilitada únicamente para la columna
         * "Notas".
         */
        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                // solo la columna Notas editable
                return col == 5;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(34);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setReorderingAllowed(false);

        // centrado de columnas numéricas
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(2).setCellRenderer(center);
        tabla.getColumnModel().getColumn(3).setCellRenderer(center);
        tabla.getColumnModel().getColumn(4).setCellRenderer(center);

        // notas a la izquierda
        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(SwingConstants.LEFT);
        tabla.getColumnModel().getColumn(5).setCellRenderer(left);

        // para editar con 1 click
        DefaultCellEditor notasEditor = new DefaultCellEditor(new JTextField());
        notasEditor.setClickCountToStart(1);
        tabla.getColumnModel().getColumn(5).setCellEditor(notasEditor);

        tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(240); // Producto
        tabla.getColumnModel().getColumn(2).setPreferredWidth(90);  // Precio
        tabla.getColumnModel().getColumn(3).setPreferredWidth(80);  // Cantidad
        tabla.getColumnModel().getColumn(4).setPreferredWidth(100);  // Subtotal
        tabla.getColumnModel().getColumn(5).setPreferredWidth(320); // Notas

        //ocultar columna de id
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(0);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        scroll.getViewport().setBackground(Color.WHITE);

        // cargar filas desde carrito
        cargarTablaDesdeCarrito();

        // ----- cupon -----
        JPanel panelCupon = new JPanel(new GridBagLayout());
        panelCupon.setOpaque(false);
        panelCupon.setBorder(new EmptyBorder(10, 0, 0, 0));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.gridy = 0;

        JLabel lblCupon = new JLabel("Cupon (opcional):");
        lblCupon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        c.gridx = 0;
        c.weightx = 0;
        c.anchor = GridBagConstraints.WEST;
        panelCupon.add(lblCupon, c);

        txtCupon = new JTextField(14);
        txtCupon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCupon.setBorder(new CompoundBorder(
                new LineBorder(new Color(120, 120, 120), 1),
                new EmptyBorder(4, 8, 4, 8)
        ));
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panelCupon.add(txtCupon, c);

        /**
         * Botón que dispara la validación del cupón mediante
         * {@link #validarCupon()}.
         */
        JButton btnValidar = new JButton("Validar");
        btnValidar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnValidar.setFocusPainted(false);
        btnValidar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnValidar.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        btnValidar.setBackground(new Color(245, 245, 245));
        c.gridx = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        panelCupon.add(btnValidar, c);

        lblCuponValido = new JLabel("[Cupon valido]");
        lblCuponValido.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCuponValido.setForeground(new Color(40, 130, 40));
        lblCuponValido.setVisible(false);

        lblCuponInvalido = new JLabel("[Cupon invalido]");
        lblCuponInvalido.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCuponInvalido.setForeground(new Color(160, 40, 40));
        lblCuponInvalido.setVisible(false);

        c.gridx = 3;
        panelCupon.add(lblCuponValido, c);
        c.gridx = 4;
        panelCupon.add(lblCuponInvalido, c);

        btnValidar.addActionListener(e -> validarCupon());

        // ----- panel de resumen -----
        JPanel panelResumen = new JPanel(new GridLayout(1, 3, 0, 0));
        panelResumen.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        panelResumen.setBackground(Color.WHITE);

        lblSubtotal = crearCeldaResumen("Subtotal: $" + String.format("%.2f", calcularSubtotal()));
        lblDescuento = crearCeldaResumen("Descuento: $0.00");
        lblTotal = crearCeldaResumen("Total a pagar: $" + String.format("%.2f", calcularSubtotal()));

        panelResumen.add(wrapResumen(lblSubtotal));
        panelResumen.add(wrapResumen(lblDescuento));
        panelResumen.add(wrapResumen(lblTotal));

        // ----- scroll -----
        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBorder(new EmptyBorder(0, 40, 0, 40));

        // ----- metodo de pago -----
        JPanel panelMetodoPago = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelMetodoPago.setOpaque(false);
        panelMetodoPago.setBorder(new EmptyBorder(8, 0, 8, 0));

        JLabel lblMetodo = new JLabel("Metodo de pago:");
        lblMetodo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        /**
         * ComboBox para seleccionar el método de pago; actualiza
         * {@link #metodoPago}.
         */
        JComboBox<MetodoPago> comboMetodoPago
                = new JComboBox<>(MetodoPago.values());

        comboMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboMetodoPago.setSelectedItem(MetodoPago.Efectivo); // default

        comboMetodoPago.addActionListener(e -> {
            metodoPago = (MetodoPago) comboMetodoPago.getSelectedItem();
        });

        panelMetodoPago.add(lblMetodo);
        panelMetodoPago.add(Box.createHorizontalStrut(10));
        panelMetodoPago.add(comboMetodoPago);

        centro.add(scroll);
        centro.add(panelCupon);
        centro.add(panelMetodoPago);
        centro.add(Box.createVerticalStrut(6));
        centro.add(panelResumen);

        card.add(centro, BorderLayout.CENTER);

        // ----- footer y boton de confirmacion -----
        /**
         * Botón que construye el {@link PedidoProgramado}, genera los
         * {@link DetallePedido} desde la tabla y registra el pedido en la capa
         * de negocio.
         */
        JButton btnRealizar = new JButton("Realizar pedido");
        btnRealizar.setPreferredSize(new Dimension(200, 38));
        btnRealizar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRealizar.setFocusPainted(false);
        btnRealizar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRealizar.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        btnRealizar.setBackground(new Color(245, 245, 245));

        btnRealizar.addActionListener(e -> {

            try {
                //crear el pedido para mandarlo a la ultima pantalla
                PedidoProgramado pedidoProgramado = new PedidoProgramado();
                pedidoProgramado.setEstado(EstadoPedido.Pendiente);
                pedidoProgramado.setFechaCreacion(LocalDateTime.now());
                // fecha de entrega vacia por ahora (se cambia hasta que el estado pasa a Entregado)
                pedidoProgramado.setMetodoPago(metodoPago);
                pedidoProgramado.setTotal(subtotal - descuentoActual);
                pedidoProgramado.setNumeroPedido(ctx.getPedidoBO().generarNumeroDePedido());
                pedidoProgramado.setCliente(cliente);

                if (txtCupon.getText() != null && !txtCupon.getText().trim().isEmpty()) {
                    pedidoProgramado.setCupon(ctx.getCuponBO().consultarCupon(txtCupon.getText()));
                }

                //lista con los detalles del pedido
                if (tabla.isEditing()) {
                    tabla.getCellEditor().stopCellEditing();
                }
                List<DetallePedido> detalles = crearDetallesDesdeTabla();

                if (ctx.getPedidoBO().agregarPedidoProgramado(pedidoProgramado, detalles) != null) {

                    PantallaPedidoProgramadoRealizado pantallaPedidoRealizado = new PantallaPedidoProgramadoRealizado(ctx, pedidoProgramado);
                    pantallaPedidoRealizado.setVisible(true);
                    dispose();

                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar el pedido.");
                }

            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }

        });

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        panelBtn.setOpaque(false);
        panelBtn.setBorder(new EmptyBorder(0, 40, 0, 40));
        panelBtn.add(btnRealizar);

        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(0, 40, 0, 40));
        footerPanel.add(footer, BorderLayout.WEST);

        JPanel south = new JPanel();
        south.setOpaque(false);
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.add(panelBtn);
        south.add(footerPanel);

        card.add(south, BorderLayout.SOUTH);
    }

    /**
     * <p>
     * Carga las filas del {@link #modelo} a partir de {@link #carrito}.
     * </p>
     *
     * <p>
     * Inserta por cada {@link ItemCarrito}:
     * </p>
     * <ul>
     * <li>ID del producto (columna oculta)</li>
     * <li>Nombre</li>
     * <li>Precio por pieza</li>
     * <li>Cantidad</li>
     * <li>Subtotal por renglón</li>
     * <li>Nota inicial vacía</li>
     * </ul>
     */
    private void cargarTablaDesdeCarrito() {
        modelo.setRowCount(0);

        for (ItemCarrito it : carrito) {
            String nombre = it.getProducto().getNombre();
            float precio = it.getProducto().getPrecio();
            int cantidad = it.getCantidad();
            float subtotal = precio * cantidad;

            String nota = "";

            modelo.addRow(new Object[]{
                it.getProducto().getId(), // id oculto
                nombre,
                "$" + precio,
                cantidad,
                "$" + subtotal,
                nota
            });
        }
    }

    /**
     * <p>
     * Calcula el subtotal sumando {@code precio*cantidad} de cada
     * {@link ItemCarrito}.
     * </p>
     *
     * <p>
     * Además actualiza el atributo {@link #subtotal} con el total obtenido.
     * </p>
     *
     * @return subtotal calculado
     */
    private float calcularSubtotal() {
        float total = 0;
        for (ItemCarrito it : carrito) {
            total += it.getProducto().getPrecio() * it.getCantidad();
        }
        subtotal = total;

        return subtotal;
    }

    /**
     * <p>
     * Recalcula y actualiza el panel de resumen (subtotal, descuento y total a
     * pagar).
     * </p>
     *
     * <p>
     * El total final se calcula como:
     * </p>
     * <ul>
     * <li>{@code total = max(0, subtotal - descuentoActual)}</li>
     * </ul>
     */
    private void recalcularResumen() {
        float total = Math.max(0, subtotal - descuentoActual);

        lblSubtotal.setText("Subtotal: $" + String.format("%.2f", subtotal));
        lblDescuento.setText("Descuento: $" + String.format("%.2f", descuentoActual));
        lblTotal.setText("Total a pagar: $" + String.format("%.2f", total));
    }

    /**
     * <p>
     * Valida el cupón capturado en {@link #txtCupon} usando la capa de negocio.
     * </p>
     *
     * <p>
     * Flujo:
     * </p>
     * <ul>
     * <li>Resetea indicadores (visto/oculto) y {@link #descuentoActual}.</li>
     * <li>Si el campo está vacío, marca cupón inválido y recalcula el
     * resumen.</li>
     * <li>Invoca {@code ctx.getCuponBO().validarCupon(codigo, subtotal)}.</li>
     * <li>Si es válido, muestra indicador de válido y guarda el descuento.</li>
     * <li>Si no es válido, muestra indicador de inválido.</li>
     * <li>En error, muestra {@link JOptionPane}.</li>
     * </ul>
     */
    private void validarCupon() {
        String codigo = txtCupon.getText().trim();

        // reset
        lblCuponValido.setVisible(false);
        lblCuponInvalido.setVisible(false);
        descuentoActual = 0;

        if (codigo.isEmpty()) {
            lblCuponInvalido.setVisible(true);
            recalcularResumen();
            return;
        }

        try {
            var r = ctx.getCuponBO().validarCupon(codigo, subtotal);

            if (r.esValido()) {
                lblCuponValido.setVisible(true);
                descuentoActual = r.getDescuento();
            } else {
                lblCuponInvalido.setVisible(true);
            }

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        recalcularResumen();
    }

    /**
     * Crea una etiqueta de resumen centrada con el estilo base (fuente Segoe UI
     * 13).
     *
     * @param text texto a mostrar
     * @return {@link JLabel} configurado para el resumen
     */
    private JLabel crearCeldaResumen(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    /**
     * Envuelve una etiqueta de resumen en un panel con borde para simular una
     * celda.
     *
     * @param l etiqueta a envolver
     * @return panel con fondo blanco, borde y la etiqueta centrada
     */
    private JPanel wrapResumen(JLabel l) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        p.add(l, BorderLayout.CENTER);
        return p;
    }

    /**
     * <p>
     * Construye la lista de {@link DetallePedido} a partir del contenido actual
     * de la tabla.
     * </p>
     *
     * <p>
     * Por cada renglón:
     * </p>
     * <ul>
     * <li>Lee el id del producto, cantidad y nota.</li>
     * <li>Busca el {@link ItemCarrito} original con
     * {@link #buscarItemCarritoPorId(int)}.</li>
     * <li>Calcula subtotal del renglón (precio*cantidad).</li>
     * <li>Genera un {@link DetallePedido} con producto, cantidad, precio,
     * subtotal y nota (o null).</li>
     * </ul>
     *
     * @return lista de detalles construida desde la tabla
     * @throws NegocioException si no se encuentra el producto en el carrito
     */
    private List<DetallePedido> crearDetallesDesdeTabla() throws NegocioException {

        List<DetallePedido> detalles = new ArrayList<>();

        for (int i = 0; i < modelo.getRowCount(); i++) {

            int idProducto = Integer.parseInt(String.valueOf(modelo.getValueAt(i, 0)));
            int cantidad = Integer.parseInt(String.valueOf(modelo.getValueAt(i, 3)));
            String nota = String.valueOf(modelo.getValueAt(i, 5));

            ItemCarrito item = buscarItemCarritoPorId(idProducto);

            if (item == null) {
                throw new NegocioException("Producto no encontrado en carrito.");
            }

            float precio = item.getProducto().getPrecio();
            float subtotal = precio * cantidad;

            DetallePedido d = new DetallePedido();
            d.setProducto(item.getProducto());
            d.setCantidad(cantidad);
            d.setPrecio(precio);
            d.setSubtotal(subtotal);

            if (nota != null && !nota.trim().isEmpty()) {
                d.setNota(nota.trim());
            } else {
                d.setNota(null);
            }

            detalles.add(d);
        }

        return detalles;
    }

    /**
     * Busca un {@link ItemCarrito} dentro de {@link #carrito} por el id del
     * producto.
     *
     * @param idProducto id del producto a buscar
     * @return item encontrado o {@code null} si no existe
     */
    private ItemCarrito buscarItemCarritoPorId(int idProducto) {
        for (ItemCarrito it : carrito) {
            if (it.getProducto().getId() == idProducto) {
                return it;
            }
        }
        return null;
    }
}
