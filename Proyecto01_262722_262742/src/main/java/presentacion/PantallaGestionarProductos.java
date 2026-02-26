/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import dominio.EstadoProducto;
import dominio.Producto;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import negocio.Excepciones.NegocioException;

/**
 * <h1>PantallaGestionarProductos</h1>
 *
 * <p>
 * Pantalla administrativa para <b>gestionar productos</b> del sistema.
 * </p>
 *
 * <p>
 * La UI presenta:
 * </p>
 * <ul>
 * <li>Botón de regreso hacia {@link MenuEmpleado}.</li>
 * <li>Tabla con información de productos (ID, nombre, tipo, precio y
 * estado).</li>
 * <li>Columna de acción <b>Editar</b> para abrir
 * {@link PantallaEditarProducto}.</li>
 * <li>Columna de acción <b>Activar/Desactivar</b> según el estado actual del
 * producto.</li>
 * <li>Botón para abrir {@link PantallaAgregarProducto}.</li>
 * <li>Footer informativo.</li>
 * </ul>
 *
 * <h2>Carga de datos</h2>
 * <p>
 * Los productos se consultan desde la capa de negocio mediante
 * {@code ctx.getProductoBO().listarProductos()}. La tabla se reconstruye en
 * {@link #cargarProductos()}.
 * </p>
 *
 * <h2>Acciones en la tabla</h2>
 * <ul>
 * <li><b>Editar:</b> abre {@link PantallaEditarProducto} con el id del producto
 * seleccionado.</li>
 * <li><b>Activar/Desactivar:</b> solicita confirmación con
 * {@link DialogConfirmacion} y actualiza el estado del producto en la capa de
 * negocio.</li>
 * </ul>
 *
 * @author
 */
public class PantallaGestionarProductos extends JFrame {

    /**
     * Contexto global de la aplicación; permite acceder a BOs y estado de
     * sesión.
     */
    private final AppContext ctx;

    /**
     * Tabla principal donde se listan los productos.
     */
    private JTable tabla;

    /**
     * Modelo de la tabla; contiene filas con los datos renderizados y columnas
     * de acciones.
     */
    private DefaultTableModel modelo;

    /**
     * <p>
     * Constructor de la pantalla de gestión de productos.
     * </p>
     *
     * <p>
     * Construye la interfaz con:
     * </p>
     * <ul>
     * <li>Fondo beige y tarjeta blanca central</li>
     * <li>Top con flecha de regreso</li>
     * <li>Títulos</li>
     * <li>Tabla con acciones (Editar / Activar-Desactivar)</li>
     * <li>Botón "Agregar producto" y footer</li>
     * </ul>
     *
     * <p>
     * Finalmente invoca {@link #cargarProductos()} para mostrar el listado.
     * </p>
     *
     * @param ctx contexto global de la aplicación
     */
    public PantallaGestionarProductos(AppContext ctx) {
        this.ctx = ctx;

        setTitle("Panadería - Gestionar productos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 620);
        setLocationRelativeTo(null);

        // Fondo beige
        JPanel base = new JPanel(new GridBagLayout());
        base.setBackground(new Color(214, 186, 150));
        setContentPane(base);

        // Tarjeta central blanca
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(new CompoundBorder(
                new LineBorder(new Color(30, 30, 30), 2, false),
                new EmptyBorder(18, 22, 18, 22)
        ));
        panelPrincipal.setPreferredSize(new Dimension(760, 540));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        base.add(panelPrincipal, gbc);

        // ---- TOP (flecha) ----
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        /**
         * Botón de regreso hacia {@link MenuEmpleado}.
         */
        JButton btnBack = new JButton("←");
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panelSuperior.add(btnBack, BorderLayout.WEST);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // ---- CENTER ----
        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Panadería");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 58));

        JLabel subtitulo = new JLabel("Gestionar productos");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(titulo);
        panelCentro.add(Box.createVerticalStrut(8));
        panelCentro.add(subtitulo);
        panelCentro.add(Box.createVerticalStrut(18));

        // ✅ Tabla con 2 acciones (Editar / Activar-Desactivar)
        /**
         * Columnas de la tabla: datos del producto y columnas de acciones.
         */
        String[] columnas = {"ID", "Nombre", "Tipo", "Precio", "Estado", "Editar", "Acción"};

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 5 || col == 6; // Editar y Acción
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setReorderingAllowed(false);

        // Centrados para tipo/precio/estado (opcional)
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(2).setCellRenderer(center);
        tabla.getColumnModel().getColumn(3).setCellRenderer(center);
        tabla.getColumnModel().getColumn(4).setCellRenderer(center);

        // Botones en columnas
        tabla.getColumnModel().getColumn(5).setCellRenderer(new EditarRenderer());
        tabla.getColumnModel().getColumn(5).setCellEditor(new EditarEditor(new JCheckBox()));

        tabla.getColumnModel().getColumn(6).setCellRenderer(new AccionRenderer());
        tabla.getColumnModel().getColumn(6).setCellEditor(new AccionEditor(new JCheckBox()));

        // Anchos aproximados
        tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(260);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(110);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(680, 250));
        scrollPane.setMaximumSize(new Dimension(680, 250));
        scrollPane.setBorder(new LineBorder(new Color(60, 60, 60), 2));

        panelCentro.add(scrollPane);
        panelCentro.add(Box.createVerticalStrut(20));

        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // ---- SOUTH (botón + footer) ----
        /**
         * Botón para abrir la pantalla de agregar producto.
         */
        JButton btnAgregar = crearBotonPequeno("Agregar producto");

        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(footer, BorderLayout.WEST);
        panelInferior.add(btnAgregar, BorderLayout.EAST);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // ---- Acciones ----
        /**
         * Regresa al menú del empleado.
         */
        btnBack.addActionListener(e -> {
            new MenuEmpleado(ctx).setVisible(true);
            this.dispose();
        });

        /**
         * Abre {@link PantallaAgregarProducto} y oculta esta pantalla.
         */
        btnAgregar.addActionListener(e -> {
            new PantallaAgregarProducto(ctx, this).setVisible(true);
            this.setVisible(false);
        });

        cargarProductos();
    }

    /**
     * <p>
     * Carga los productos desde la capa de negocio y los inserta como filas en
     * {@link #modelo}.
     * </p>
     *
     * <p>
     * La tabla muestra:
     * </p>
     * <ul>
     * <li>ID</li>
     * <li>Nombre</li>
     * <li>Tipo</li>
     * <li>Precio formateado</li>
     * <li>Estado (con "_" reemplazado por espacios)</li>
     * <li>Texto "Editar" (columna renderizada con botón)</li>
     * <li>Columna de acción (render/editor define "Activar" o
     * "Desactivar")</li>
     * </ul>
     *
     * <p>
     * En caso de error, se notifica con un {@link JOptionPane}.
     * </p>
     */
    private void cargarProductos() {
        try {
            modelo.setRowCount(0);

            List<Producto> productos = ctx.getProductoBO().listarProductos();

            for (Producto p : productos) {

                String precio = String.format("$%.2f", p.getPrecio());
                String estado = p.getEstado().name().replace("_", " ");

                modelo.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getTipo().name(),
                    precio,
                    estado,
                    "Editar",
                    "" // la columna acción se pinta con renderer/editor
                });
            }

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los productos.\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ====== BOTÓN EDITAR ======
    /**
     * <p>
     * Renderer para la columna "Editar".
     * </p>
     *
     * <p>
     * Renderiza un {@link JButton} con texto "Editar" en cada fila.
     * </p>
     */
    private class EditarRenderer extends JButton implements TableCellRenderer {

        /**
         * Configura el estilo del botón renderizado para la columna "Editar".
         */
        public EditarRenderer() {
            setOpaque(true);
            setText("Editar");
            setFont(new Font("Segoe UI", Font.PLAIN, 12));
            setFocusPainted(false);
            setBackground(new Color(245, 245, 245));
            setBorder(new LineBorder(new Color(120, 120, 120), 1));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    /**
     * <p>
     * Editor para la columna "Editar".
     * </p>
     *
     * <p>
     * Permite capturar el click en el botón para abrir
     * {@link PantallaEditarProducto} con el id del producto de la fila
     * seleccionada.
     * </p>
     */
    private class EditarEditor extends DefaultCellEditor {

        /**
         * Botón que se muestra dentro de la celda al editar.
         */
        private final JButton button;

        /**
         * Índice de la fila actualmente editada.
         */
        private int row;

        /**
         * Constructor del editor de "Editar".
         *
         * @param checkBox checkbox requerido por {@link DefaultCellEditor}
         */
        public EditarEditor(JCheckBox checkBox) {
            super(checkBox);

            button = new JButton("Editar");
            button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            button.setFocusPainted(false);
            button.setBackground(new Color(245, 245, 245));
            button.setBorder(new LineBorder(new Color(120, 120, 120), 1));

            button.addActionListener(e -> {
                try {
                    int idProducto = Integer.parseInt(String.valueOf(modelo.getValueAt(row, 0)));

                    new PantallaEditarProducto(ctx, PantallaGestionarProductos.this, idProducto).setVisible(true);
                    PantallaGestionarProductos.this.setVisible(false);
                } finally {
                    fireEditingStopped();
                }
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
            return "Editar";
        }
    }

    // ====== BOTÓN ACTIVAR/DESACTIVAR ======
    /**
     * <p>
     * Renderer para la columna "Acción".
     * </p>
     *
     * <p>
     * El texto del botón depende del estado visible en la tabla:
     * </p>
     * <ul>
     * <li>Si el estado es "Disponible" → muestra "Desactivar"</li>
     * <li>En caso contrario → muestra "Activar"</li>
     * </ul>
     */
    private class AccionRenderer extends JButton implements TableCellRenderer {

        /**
         * Configura el estilo del botón renderizado para la columna de acción.
         */
        public AccionRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.PLAIN, 12));
            setFocusPainted(false);
            setBackground(new Color(245, 245, 245));
            setBorder(new LineBorder(new Color(120, 120, 120), 1));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            String estado = String.valueOf(modelo.getValueAt(row, 4));
            setText("Disponible".equalsIgnoreCase(estado) ? "Desactivar" : "Activar");
            return this;
        }
    }

    /**
     * <p>
     * Editor para la columna "Acción" (Activar/Desactivar).
     * </p>
     *
     * <p>
     * Al hacer click:
     * </p>
     * <ul>
     * <li>Obtiene el id del producto desde la fila</li>
     * <li>Determina si está disponible (según el texto mostrado en la
     * tabla)</li>
     * <li>Pide confirmación con {@link DialogConfirmacion}</li>
     * <li>Consulta el producto real en negocio</li>
     * <li>Actualiza el estado del producto y persiste</li>
     * <li>Refresca la tabla llamando {@link #cargarProductos()}</li>
     * </ul>
     */
    private class AccionEditor extends DefaultCellEditor {

        /**
         * Botón que se muestra dentro de la celda al editar.
         */
        private final JButton button;

        /**
         * Índice de la fila actualmente editada.
         */
        private int row;

        /**
         * Constructor del editor de acción.
         *
         * @param checkBox checkbox requerido por {@link DefaultCellEditor}
         */
        public AccionEditor(JCheckBox checkBox) {
            super(checkBox);

            button = new JButton();
            button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            button.setFocusPainted(false);
            button.setBackground(new Color(245, 245, 245));
            button.setBorder(new LineBorder(new Color(120, 120, 120), 1));

            button.addActionListener(e -> {
                try {
                    int idProducto = Integer.parseInt(String.valueOf(modelo.getValueAt(row, 0)));
                    String estadoStr = String.valueOf(modelo.getValueAt(row, 4));
                    boolean estaDisponible = "Disponible".equalsIgnoreCase(estadoStr);

                    boolean ok = DialogConfirmacion.confirmar(
                            PantallaGestionarProductos.this,
                            estaDisponible
                                    ? "¿Esta seguro que desea Desactivar\nel producto?"
                                    : "¿Esta seguro que desea Activar\nel producto?"
                    );

                    if (!ok) {
                        return;
                    }

                    // 1) Consultar producto real por ID
                    Producto temp = new Producto();
                    temp.setId(idProducto);
                    Producto producto = ctx.getProductoBO().consultarProducto(temp);

                    // 2) Cambiar estado
                    producto.setEstado(estaDisponible ? EstadoProducto.No_disponible : EstadoProducto.Disponible);

                    // 3) Guardar cambios
                    ctx.getProductoBO().actualizarProducto(producto);

                    // 4) Refrescar tabla
                    cargarProductos();

                } catch (NegocioException ex) {
                    JOptionPane.showMessageDialog(PantallaGestionarProductos.this,
                            ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.row = row;

            String estado = String.valueOf(modelo.getValueAt(row, 4));
            button.setText("Disponible".equalsIgnoreCase(estado) ? "Desactivar" : "Activar");

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }

    // ====== UI ======
    /**
     * <p>
     * Crea un botón pequeño con el estilo visual utilizado en esta pantalla.
     * </p>
     *
     * @param text texto del botón
     * @return botón configurado con estilo
     */
    private JButton crearBotonPequeno(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(170, 28));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBackground(new Color(245, 245, 245));
        b.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(2, 10, 2, 10)
        ));
        return b;
    }
}
