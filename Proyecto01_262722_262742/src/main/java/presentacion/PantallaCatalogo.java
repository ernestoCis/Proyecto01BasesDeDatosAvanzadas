package presentacion;

import dominio.ItemCarrito;
import dominio.Producto;          
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;
import negocio.BOs.ProductoBO;
import negocio.Excepciones.NegocioException;

public class PantallaCatalogo extends JFrame {

    private final ProductoBO productoBO;
    private final java.util.List<ItemCarrito> carrito = new java.util.ArrayList<>();

    private JPanel grid;
    private JLabel badgeCarrito;

    public PantallaCatalogo(ProductoBO productoBO) {
        this.productoBO = productoBO;

        setTitle("Panadería - Catálogo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 600);
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
        card.setPreferredSize(new Dimension(860, 520));
        root.add(card);

        //----- parte de arriba -----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        // parte izquierda
        JButton btnFlecha = new JButton("⬅️");
        btnFlecha.setFocusPainted(false);
        btnFlecha.setBorderPainted(false);
        btnFlecha.setContentAreaFilled(false);
        btnFlecha.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        btnFlecha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Acción: regresar
        btnFlecha.addActionListener(e -> {
            new Menu().setVisible(true);
            dispose();
        });

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.add(btnFlecha);

        // ----- parte izquierda -----
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

        lblCarrito.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                PantallaCarrito pantallaCarrito = new PantallaCarrito(PantallaCatalogo.this, carrito);
                pantallaCarrito.setVisible(true);
                setVisible(false);
            }
        });

        topBar.add(panelIzquierdo, BorderLayout.WEST);
        topBar.add(panelCarrito, BorderLayout.EAST);

        // ----- titulos -----
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

        // ----- grid scroll -----
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

        // ----- parte de abajo -----
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

    private void cargarProductosDesdeBD() {
        try {
            List<Producto> productos = productoBO.listarProductos();

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

    private JPanel crearTarjetaProducto(Producto p) {
        JPanel item = new JPanel();
        item.setOpaque(false);
        item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));

        // Nombre
        JLabel lblNombre = new JLabel(p.getNombre(), SwingConstants.CENTER);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // Placeholder de imagen
        JPanel img = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(245, 245, 245));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g.setColor(new Color(200, 200, 200));
                g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
                g.setColor(new Color(120, 120, 120));
                g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                FontMetrics fm = g.getFontMetrics();
                String t = "Imagen";
                g.drawString(t, (getWidth()-fm.stringWidth(t))/2, getHeight()/2);
            }
        };
        img.setPreferredSize(new Dimension(180, 100));
        img.setMaximumSize(new Dimension(180, 100));
        img.setAlignmentX(Component.CENTER_ALIGNMENT);
        img.setOpaque(false);

        // Tipo y precio
        String tipo = (p.getTipo() != null) ? p.getTipo().toString() : "N/A";
        JLabel lblTipoPrecio = new JLabel("Tipo: " + tipo + "   Precio: $" + Math.round(p.getPrecio()));
        lblTipoPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTipoPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Fila inferior: info + stepper
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setOpaque(false);

        JButton btnInfo = new JButton("i");
        btnInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnInfo.setFocusPainted(false);
        btnInfo.setPreferredSize(new Dimension(26, 24));
        btnInfo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnInfo.setBorder(new LineBorder(new Color(60, 60, 60), 1, true));

        btnInfo.addActionListener(e -> {
            String estado = (p.getEstado() != null) ? p.getEstado().toString() : "N/A";
            String desc = (p.getDescripcion() != null) ? p.getDescripcion() : "(Sin descripción)";

            JOptionPane.showMessageDialog(
                    this,
                    "Nombre: " + p.getNombre() +
                            "\nTipo: " + tipo +
                            "\nPrecio: $" + p.getPrecio() +
                            "\nEstado: " + estado +
                            "\nDescripción: " + desc,
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

    private void actualizarBadgeCarrito(int delta) {
        int actual = Integer.parseInt(badgeCarrito.getText());
        int nuevo = Math.max(0, actual + delta);
        badgeCarrito.setText(String.valueOf(nuevo));
    }
    
    private ItemCarrito buscarItem(int idProducto) {
        for (ItemCarrito it : carrito) {
            if (it.getProducto().getId() == idProducto) {
                return it;
            }
        }
        return null;
    }

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

    private int totalPiezas() {
        int s = 0;
        for (ItemCarrito it : carrito) {
            s += it.getCantidad();
        }
        return s;
    }
}
