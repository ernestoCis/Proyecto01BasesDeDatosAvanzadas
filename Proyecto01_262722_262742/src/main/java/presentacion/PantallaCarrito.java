package presentacion;

import dominio.Cliente;
import dominio.ItemCarrito;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * <h1>PantallaCarrito</h1>
 *
 * <p>
 * Pantalla que muestra el <b>carrito de compras</b> del cliente.
 * </p>
 *
 * <p>
 * Presenta una tabla con los productos agregados al carrito, incluyendo:
 * </p>
 *
 * <ul>
 * <li><b>Producto</b></li>
 * <li><b>Precio por pieza</b></li>
 * <li><b>Cantidad</b></li>
 * <li><b>Subtotal</b> (precio * cantidad)</li>
 * <li><b>Acción</b> para eliminar el producto</li>
 * </ul>
 *
 * <p>
 * Además, muestra el <b>total</b> del carrito y permite continuar al proceso de
 * confirmación del pedido, abriendo la pantalla
 * {@code PantallaConfirmarPedido}.
 * </p>
 *
 * <h2>Flujo general</h2>
 * <ol>
 * <li>Se carga la lista {@code carrito} en la tabla.</li>
 * <li>Se calcula y muestra el total.</li>
 * <li>El usuario puede eliminar elementos desde la tabla.</li>
 * <li>Al presionar "Continuar", se valida que el carrito no esté vacío y se
 * pasa a confirmar.</li>
 * </ol>
 *
 * <p>
 * Esta clase trabaja con {@link AppContext} (acceso a negocio) y el
 * {@link Cliente} (usuario actual que realizará el pedido).
 * </p>
 *
 * @author 262722, 2627242
 */
public class PantallaCarrito extends JFrame {

    /**
     * Tabla que muestra el contenido del carrito.
     */
    private JTable tabla;

    /**
     * Modelo asociado a la tabla; permite agregar y limpiar filas.
     */
    private DefaultTableModel modelo;

    /**
     * Etiqueta donde se muestra el total calculado del carrito.
     */
    private JLabel lblTotal;

    /**
     * Referencia a la pantalla del catálogo para regresar con la flecha.
     */
    private PantallaCatalogo pantallaCatalogo;

    /**
     * Lista de elementos del carrito (producto + cantidad). Se utiliza como
     * fuente de verdad para cargar tabla y calcular el total.
     */
    private final List<ItemCarrito> carrito;

    /**
     * Contexto global de la aplicación.
     */
    private final AppContext ctx;

    /**
     * Cliente asociado al flujo de compra/pedido.
     */
    private final Cliente cliente;

    /**
     * <p>
     * Constructor de la pantalla del carrito.
     * </p>
     *
     * <p>
     * Inicializa la UI completa (títulos, tabla, total, botones y footer),
     * asigna eventos de navegación y carga el contenido inicial.
     * </p>
     *
     * @param pantallaCatalogo pantalla desde la cual se abrió el carrito
     * @param carrito lista de items actualmente agregados
     * @param ctx contexto global de la aplicación
     * @param cliente cliente que realizará el pedido
     */
    public PantallaCarrito(PantallaCatalogo pantallaCatalogo, List<ItemCarrito> carrito, AppContext ctx, Cliente cliente) {
        this.pantallaCatalogo = pantallaCatalogo;
        this.carrito = carrito;

        this.ctx = ctx;
        this.cliente = cliente;

        setTitle("Panadería - Carrito");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 600);
        setLocationRelativeTo(null);

        // ======================
        // Fondo beige (marco)
        // ======================
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(new Color(214, 186, 150));
        setContentPane(root);

        // ======================
        // Tarjeta principal (blanca con borde)
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
        // Parte superior (back + título)
        // ======================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        /**
         * Botón de regreso hacia el catálogo.
         */
        JButton btnFlecha = new JButton("←");
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

        // ======================
        // Tabla
        // ======================
        /**
         * Columnas: producto, precio, cantidad, subtotal y acción de
         * eliminación.
         */
        String[] cols = {"Producto", "Precio pz.", "Cantidad", "Subtotal", ""};

        /**
         * Modelo donde solamente la última columna (acción) es editable para
         * habilitar el editor con botón.
         */
        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 4;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(32);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setReorderingAllowed(false);

        /**
         * Alineación centrada en columnas numéricas.
         */
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(1).setCellRenderer(center);
        tabla.getColumnModel().getColumn(2).setCellRenderer(center);
        tabla.getColumnModel().getColumn(3).setCellRenderer(center);

        /**
         * Renderer/Editor para el botón "Eliminar" dentro de la tabla.
         */
        tabla.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        tabla.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

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

        // ======================
        // Total + Continuar
        // ======================
        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);

        /**
         * Panel del total (alineado a la derecha).
         */
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelTotal.setOpaque(false);

        lblTotal = new JLabel();
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        /**
         * Caja visual donde se muestra el total.
         */
        JPanel cajaTotal = new JPanel(new BorderLayout());
        cajaTotal.setBackground(new Color(245, 245, 245));
        cajaTotal.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        cajaTotal.setPreferredSize(new Dimension(140, 32));
        cajaTotal.add(lblTotal, BorderLayout.CENTER);
        lblTotal.setBorder(new EmptyBorder(0, 12, 0, 12));

        panelTotal.add(cajaTotal);

        /**
         * Botón para continuar a la confirmación del pedido.
         */
        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setPreferredSize(new Dimension(180, 34));
        btnContinuar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnContinuar.setFocusPainted(false);
        btnContinuar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnContinuar.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        btnContinuar.setBackground(new Color(245, 245, 245));

        /**
         * Valida carrito no vacío y abre {@code PantallaConfirmarPedido}.
         */
        btnContinuar.addActionListener(e -> {
            if (carrito == null || carrito.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tu carrito está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            PantallaConfirmarPedido confirmar = new PantallaConfirmarPedido(this, carrito, ctx, cliente);
            confirmar.setVisible(true);
            this.setVisible(false);
        });

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        panelBtn.setOpaque(false);
        panelBtn.add(btnContinuar);

        // ======================
        // Footer
        // ======================
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

        // ======================
        // Carga inicial
        // ======================
        cargarTabla();
        actualizarTotal();
    }

    /**
     * <p>
     * Calcula el total del carrito sumando (precio * cantidad) por cada item, y
     * lo muestra en {@link #lblTotal}.
     * </p>
     */
    private void actualizarTotal() {
        float total = 0;
        for (ItemCarrito it : carrito) {
            total += it.getProducto().getPrecio() * it.getCantidad();
        }
        lblTotal.setText("Total: $" + Math.round(total));
    }

    // ======================
    // Estilos para botón eliminar
    // ======================
    /**
     * <p>
     * Renderer de la columna de acción "Eliminar".
     * </p>
     *
     * <p>
     * Solo dibuja el botón en la celda, no maneja eventos.
     * </p>
     */
    private class ButtonRenderer extends JButton implements TableCellRenderer {

        /**
         * Construye el botón visual con estilo uniforme.
         */
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

    /**
     * <p>
     * Editor de celda que permite ejecutar la acción de eliminar el item
     * seleccionado.
     * </p>
     *
     * <p>
     * Al eliminar:
     * </p>
     * <ol>
     * <li>Se elimina el elemento correspondiente de la lista
     * {@link #carrito}.</li>
     * <li>Se recarga la tabla.</li>
     * <li>Se recalcula el total.</li>
     * </ol>
     */
    private class ButtonEditor extends DefaultCellEditor {

        /**
         * Botón que se muestra mientras la celda está en modo edición.
         */
        private final JButton button;

        /**
         * Fila actualmente asociada a la celda editada.
         */
        private int row;

        /**
         * Constructor del editor.
         *
         * @param checkBox checkbox requerido por {@link DefaultCellEditor}
         */
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Eliminar");
            button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            button.setFocusPainted(false);
            button.setBackground(new Color(245, 245, 245));
            button.setBorder(new LineBorder(new Color(120, 120, 120), 1));

            button.addActionListener(e -> {
                if (row >= 0 && row < modelo.getRowCount()) {

                    if (row >= 0 && row < carrito.size()) {
                        carrito.remove(row);
                    }

                    cargarTabla();
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

    /**
     * <p>
     * Carga la tabla con los elementos actuales del carrito.
     * </p>
     *
     * <p>
     * Por cada {@link ItemCarrito} se calcula el subtotal y se agrega como
     * fila.
     * </p>
     */
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
