package presentacion;

import dominio.Cliente;
import dominio.ItemCarrito;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import negocio.BOs.iCuponBO;
import negocio.BOs.iPedidoBO;
import negocio.BOs.iProductoBO;
import negocio.BOs.iUsuarioBO;

public class PantallaCarrito extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblTotal;
    private PantallaCatalogo pantallaCatalogo;
    private final java.util.List<ItemCarrito> carrito;
    
    private final iProductoBO productoBO;
    private final iCuponBO cuponBO;
    private final iPedidoBO pedidoBO;
    private final Cliente cliente;

    public PantallaCarrito(PantallaCatalogo pantallaCatalogo, java.util.List<ItemCarrito> carrito, iProductoBO productoBO, iCuponBO cuponBO, iPedidoBO pedidoBO, Cliente cliente) {
        this.pantallaCatalogo = pantallaCatalogo;
        this.carrito = carrito;
        
        this.productoBO = productoBO;
        this.cuponBO = cuponBO;
        this.pedidoBO = pedidoBO;
        this.cliente = cliente;
        
        setTitle("Panadería - Carrito");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 600);
        setLocationRelativeTo(null);

        // Fondo beige (marco)
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

        // ----- parte de arriba -----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JButton btnFlecha = new JButton("↩");
        btnFlecha.setFocusPainted(false);
        btnFlecha.setBorderPainted(false);
        btnFlecha.setContentAreaFilled(false);
        btnFlecha.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        btnFlecha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFlecha.addActionListener(e -> {
            pantallaCatalogo.setVisible(true);
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

        JLabel subtitle = new JLabel("Carrito de compras");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 22));

        header.add(Box.createVerticalStrut(2));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(10));

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(topBar, BorderLayout.NORTH);
        north.add(header, BorderLayout.CENTER);

        card.add(north, BorderLayout.NORTH);

        //----- tabla -----
        String[] cols = {"Producto", "Precio pz.", "Cantidad", "Subtotal", ""};

        modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                return col == 4;
            }
        };

        

        tabla = new JTable(modelo);
        tabla.setRowHeight(32);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setReorderingAllowed(false);

        // Centrados para precio/subtotal
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(1).setCellRenderer(center);
        tabla.getColumnModel().getColumn(2).setCellRenderer(center);
        tabla.getColumnModel().getColumn(3).setCellRenderer(center);


        // Columna botón Eliminar
        tabla.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        tabla.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Anchos parecidos
        tabla.getColumnModel().getColumn(0).setPreferredWidth(260);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(90);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.setBorder(new EmptyBorder(6, 40, 0, 40));
        centro.add(scroll, BorderLayout.NORTH);

        JPanel cajaVacia = new JPanel();
        cajaVacia.setBackground(Color.WHITE);
        cajaVacia.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        cajaVacia.setPreferredSize(new Dimension(1, 220));

        JPanel wrapCaja = new JPanel(new BorderLayout());
        wrapCaja.setOpaque(false);
        wrapCaja.setBorder(new EmptyBorder(0, 0, 0, 0));
        wrapCaja.add(cajaVacia, BorderLayout.CENTER);

        centro.add(wrapCaja, BorderLayout.CENTER);

        card.add(centro, BorderLayout.CENTER);

        //----- total y continuar -----
        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);

        // Total en “cajita” alineada a la derecha
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelTotal.setOpaque(false);
        lblTotal = new JLabel();
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        actualizarTotal();

        JPanel cajaTotal = new JPanel(new BorderLayout());
        cajaTotal.setBackground(new Color(245, 245, 245));
        cajaTotal.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        cajaTotal.setPreferredSize(new Dimension(140, 32));
        cajaTotal.add(lblTotal, BorderLayout.CENTER);
        lblTotal.setBorder(new EmptyBorder(0, 12, 0, 12));

        panelTotal.add(cajaTotal);

        // Botón continuar centrado
        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setPreferredSize(new Dimension(180, 34));
        btnContinuar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnContinuar.setFocusPainted(false);
        btnContinuar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnContinuar.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        btnContinuar.setBackground(new Color(245, 245, 245));

        btnContinuar.addActionListener(e -> {
            if (carrito == null || carrito.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tu carrito está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            PantallaConfirmarPedido confirmar = new PantallaConfirmarPedido(this, carrito, productoBO, cuponBO, pedidoBO, cliente);
            confirmar.setVisible(true);
            this.setVisible(false); 
        });

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        panelBtn.setOpaque(false);
        panelBtn.add(btnContinuar);

        // Footer
        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.add(footer, BorderLayout.WEST);

        JPanel stackSouth = new JPanel();
        stackSouth.setOpaque(false);
        stackSouth.setLayout(new BoxLayout(stackSouth, BoxLayout.Y_AXIS));
        stackSouth.add(panelTotal);
        stackSouth.add(panelBtn);
        stackSouth.add(footerPanel);

        south.add(stackSouth, BorderLayout.CENTER);
        card.add(south, BorderLayout.SOUTH);
        
        cargarTabla();
        actualizarTotal();
    }

    private void actualizarTotal() {
        float total = 0;
        for (ItemCarrito it : carrito) {
            total += it.getProducto().getPrecio() * it.getCantidad();
        }
        lblTotal.setText("Total: $" + Math.round(total));
    }

    // ----- estilos para el boton eliminar -----
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("Eliminar");
            setFont(new Font("Segoe UI", Font.PLAIN, 12));
            setFocusPainted(false);
            setBackground(new Color(245, 245, 245));
            setBorder(new LineBorder(new Color(120, 120, 120), 1));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Eliminar");
            button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            button.setFocusPainted(false);
            button.setBackground(new Color(245, 245, 245));
            button.setBorder(new LineBorder(new Color(120, 120, 120), 1));

            button.addActionListener(e -> {
                if (row >= 0 && row < modelo.getRowCount()) {

                    // 1) Eliminar también de la lista
                    if (row >= 0 && row < carrito.size()) {
                        carrito.remove(row);
                    }

                    // 2) Recargar tabla (ya con lista actual)
                    cargarTabla();

                    // 3) Actualizar total
                    actualizarTotal();
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Eliminar";
        }
    }
    
    private void cargarTabla() {
        modelo.setRowCount(0);

        for (ItemCarrito it : carrito) {
            String nombre = it.getProducto().getNombre();
            float precio = it.getProducto().getPrecio();
            int cantidad = it.getCantidad();
            float subtotal = precio * cantidad;

            modelo.addRow(new Object[]{
                nombre,
                "$" + Math.round(precio),
                cantidad,
                "$" + Math.round(subtotal),
                "Eliminar"
            });
        }
    }
}