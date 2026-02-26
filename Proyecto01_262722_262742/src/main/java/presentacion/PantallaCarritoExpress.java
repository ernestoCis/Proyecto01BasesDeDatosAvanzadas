/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dominio.Cliente;
import dominio.ItemCarrito;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * <p>
 * Pantalla que muestra el <b>carrito de compras</b> para el flujo de <b>pedido
 * EXPRESS</b>.
 * </p>
 *
 * <p>
 * Visualiza una tabla con los productos agregados al carrito, mostrando:
 * </p>
 *
 * <ul>
 * <li><b>Producto</b></li>
 * <li><b>Precio por pieza</b></li>
 * <li><b>Cantidad</b></li>
 * <li><b>Subtotal</b> (precio * cantidad)</li>
 * <li><b>Acción</b> para eliminar el elemento</li>
 * </ul>
 *
 * <p>
 * También calcula y muestra el <b>total</b> del carrito y permite continuar al
 * proceso de confirmación del pedido express abriendo
 * {@code PantallaConfirmarPedidoExpress}.
 * </p>
 *
 * <h2>Notas del flujo express</h2>
 * <ul>
 * <li>Se regresa al catálogo express con la flecha.</li>
 * <li>Si el carrito está vacío, no permite continuar.</li>
 * <li>La acción de eliminar se ejecuta desde un botón incrustado en la
 * tabla.</li>
 * </ul>
 *
 * @author 262722, 2627242
 */
public class PantallaCarritoExpress extends JFrame {

    /**
     * Tabla que muestra los items del carrito.
     */
    private JTable tabla;

    /**
     * Modelo de la tabla para administrar filas.
     */
    private DefaultTableModel modelo;

    /**
     * Etiqueta donde se muestra el total calculado del carrito.
     */
    private JLabel lblTotal;

    /**
     * Referencia a la pantalla de catálogo express para regresar.
     */
    private PantallaCatalogoExpress pantallaCatalogo;

    /**
     * Lista de elementos del carrito (producto + cantidad).
     */
    private final List<ItemCarrito> carrito;

    /**
     * Contexto global de la aplicación; provee acceso a BOs y sesión.
     */
    private final AppContext ctx;

    /**
     * Cliente actual tomado desde el contexto (puede ser null en flujo
     * express).
     */
    private final Cliente cliente;

    /**
     * <p>
     * Constructor del carrito express.
     * </p>
     *
     * <p>
     * Inicializa la interfaz completa:
     * </p>
     * <ul>
     * <li>Marco beige y tarjeta blanca</li>
     * <li>Encabezado con título y etiqueta "EXPRESS"</li>
     * <li>Tabla con botón "Eliminar"</li>
     * <li>Total y botón "Continuar"</li>
     * </ul>
     *
     * <p>
     * Al presionar "Continuar" se valida que el carrito no esté vacío y se abre
     * {@code PantallaConfirmarPedidoExpress}.
     * </p>
     *
     * @param pantallaCatalogo pantalla de catálogo express para regresar
     * @param carrito lista de items actualmente agregados
     * @param ctx contexto global de la aplicación
     */
    public PantallaCarritoExpress(PantallaCatalogoExpress pantallaCatalogo, List<ItemCarrito> carrito, AppContext ctx) {
        this.pantallaCatalogo = pantallaCatalogo;
        this.carrito = carrito;
        this.cliente = ctx.getClienteActual();

        this.ctx = ctx;

        setTitle("Panadería - Carrito Express");
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
        // Parte superior (back + título)
        // ======================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        /**
         * Botón flecha para regresar a {@link PantallaCatalogoExpress}.
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
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 22));

        JLabel lblExpress = new JLabel("EXPRESS");
        lblExpress.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblExpress.setForeground(new Color(200, 60, 60));

        JPanel subtitleRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        subtitleRow.setOpaque(false);
        subtitleRow.add(subtitle);
        subtitleRow.add(lblExpress);

        header.add(Box.createVerticalStrut(2));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitleRow);
        header.add(Box.createVerticalStrut(10));

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(topBar, BorderLayout.NORTH);
        north.add(header, BorderLayout.CENTER);

        card.add(north, BorderLayout.NORTH);

        // ======================
        // Tabla
        // ======================
        String[] cols = {"Producto", "Precio pz.", "Cantidad", "Subtotal", ""};

        /**
         * Modelo donde solo la columna de acción (última) es editable.
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
         * Centrado para columnas numéricas.
         */
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(1).setCellRenderer(center);
        tabla.getColumnModel().getColumn(2).setCellRenderer(center);
        tabla.getColumnModel().getColumn(3).setCellRenderer(center);

        /**
         * Renderer/Editor para botón eliminar dentro de la tabla.
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

        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setPreferredSize(new Dimension(180, 34));
        btnContinuar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnContinuar.setFocusPainted(false);
        btnContinuar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnContinuar.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        btnContinuar.setBackground(new Color(245, 245, 245));

        /**
         * Continúa al flujo de confirmación del pedido express. Si el carrito
         * está vacío, muestra aviso y no avanza.
         */
        btnContinuar.addActionListener(e -> {
            if (carrito == null || carrito.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tu carrito está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            new PantallaConfirmarPedidoExpress(this, carrito, ctx).setVisible(true);
            dispose();
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
     * Calcula el total del carrito sumando precio * cantidad de cada item y lo
     * muestra en {@link #lblTotal}.
     */
    private void actualizarTotal() {
        float total = 0;
        for (ItemCarrito it : carrito) {
            total += it.getProducto().getPrecio() * it.getCantidad();
        }
        lblTotal.setText("Total: $" + Math.round(total));
    }

    // ======================
    // Estilos para el botón eliminar
    // ======================
    /**
     * Renderer de la celda que muestra el botón "Eliminar". Solo representa
     * visualmente el componente (no maneja eventos).
     */
    private class ButtonRenderer extends JButton implements TableCellRenderer {

        /**
         * Configura el botón visual con estilo uniforme.
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
     * Editor de la columna de acción que ejecuta el eliminado del item.
     * Actualiza la tabla y el total inmediatamente después del borrado.
     */
    private class ButtonEditor extends DefaultCellEditor {

        /**
         * Botón mostrado durante la edición de la celda.
         */
        private final JButton button;

        /**
         * Fila actualmente seleccionada/editada.
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
     * Carga la tabla con el contenido actual del carrito. Por cada item calcula
     * el subtotal y agrega una fila.
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
