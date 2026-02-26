package presentacion;

import dominio.Cliente;
import dominio.Telefono;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import negocio.Excepciones.NegocioException;

/**
 * <p>
 * Pantalla que permite al cliente <b>administrar (visualizar, agregar, editar y
 * eliminar)</b>
 * sus teléfonos asociados dentro del sistema.
 * </p>
 *
 * <p>
 * La administración se realiza mediante una tabla ({@link JTable}) que muestra:
 * </p>
 *
 * <ul>
 * <li><b>Número</b> del teléfono.</li>
 * <li><b>Etiqueta</b> (opcional) para identificar el teléfono (ej. "Casa",
 * "Trabajo").</li>
 * <li><b>Acción</b> con botones "Editar" y "Eliminar" integrados en la
 * tabla.</li>
 * </ul>
 *
 * <h2>Acciones disponibles</h2>
 * <ul>
 * <li><b>Añadir teléfono</b>: muestra un diálogo para capturar número y
 * etiqueta.</li>
 * <li><b>Editar</b>: permite modificar número/etiqueta del registro
 * seleccionado.</li>
 * <li><b>Eliminar</b>: elimina el registro seleccionado previa
 * confirmación.</li>
 * <li><b>Guardar cambios</b>: envía la lista final a la capa de negocio para
 * persistir.</li>
 * <li><b>Regresar</b>: vuelve a la pantalla anterior si existe.</li>
 * </ul>
 *
 *
 * @author 262722, 2627242
 */
public class PantallaAdministrarTelefonosCliente extends JFrame {

    /**
     * Pantalla anterior utilizada para regresar al flujo previo. Puede ser null
     * si fue abierta sin una ventana padre.
     */
    private final JFrame pantallaAnterior;

    /**
     * Contexto global de la aplicación: contiene BOs y estado de sesión.
     */
    private final AppContext ctx;

    /**
     * Cliente actualmente autenticado al construir la pantalla.
     */
    private final Cliente clienteActual;

    /**
     * Tabla principal que muestra la lista de teléfonos del cliente.
     */
    private JTable tabla;

    /**
     * Modelo de la tabla; permite insertar y refrescar filas.
     */
    private DefaultTableModel modelo;

    /**
     * Lista de teléfonos en memoria que se muestra y edita en la UI.
     */
    private List<Telefono> telefonos;

    /**
     * <p>
     * Constructor de la pantalla de administración de teléfonos.
     * </p>
     *
     * <p>
     * Inicializa:
     * </p>
     * <ul>
     * <li>El cliente en sesión desde el {@link AppContext}.</li>
     * <li>Una lista local de teléfonos (copiando los existentes del
     * cliente).</li>
     * <li>La tabla con renderer/editor para botones de acciones.</li>
     * <li>Los botones inferiores para guardar y añadir.</li>
     * </ul>
     *
     * @param pantallaAnterior ventana anterior para regresar al presionar la
     * flecha
     * @param ctx contexto global de la aplicación
     */
    public PantallaAdministrarTelefonosCliente(JFrame pantallaAnterior, AppContext ctx) {
        this.pantallaAnterior = pantallaAnterior;
        this.ctx = ctx;
        this.clienteActual = ctx.getClienteActual();

        this.telefonos = new ArrayList<>();
        if (clienteActual != null && clienteActual.getTelefonos() != null) {
            this.telefonos.addAll(clienteActual.getTelefonos());
        }

        setTitle("Panadería - Administrar teléfonos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 650);
        setLocationRelativeTo(null);

        // ===================
        // Fondo y tarjeta
        // ===================
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(new Color(214, 186, 150));
        setContentPane(root);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(30, 30, 30), 2, false),
                new EmptyBorder(18, 22, 18, 22)
        ));
        card.setPreferredSize(new Dimension(860, 560));
        root.add(card);

        // ===================
        // TOP (títulos + regreso)
        // ===================
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        /**
         * Botón flecha para regresar a la pantalla anterior. Si existe, se
         * vuelve visible; esta ventana se cierra.
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

        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftTop.setOpaque(false);
        leftTop.add(btnFlecha);

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel lblPan = new JLabel("Panadería");
        lblPan.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPan.setFont(new Font("Segoe UI", Font.BOLD, 64));

        JLabel lblSub = new JLabel("Administrar teléfonos");
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 22));

        titles.add(Box.createVerticalStrut(10));
        titles.add(lblPan);
        titles.add(Box.createVerticalStrut(4));
        titles.add(lblSub);

        top.add(leftTop, BorderLayout.WEST);
        top.add(titles, BorderLayout.CENTER);

        card.add(top, BorderLayout.NORTH);

        // ===================
        // TABLA
        // ===================
        /**
         * Columnas de la tabla: número, etiqueta y acciones.
         */
        String[] cols = {"Número", "Etiqueta", "Acción"};

        /**
         * Modelo de tabla donde únicamente la columna "Acción" es editable,
         * para permitir el editor con botones.
         */
        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 2;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(34);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setReorderingAllowed(false);

        /**
         * Renderer para centrar el contenido de las columnas de texto.
         */
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(0).setCellRenderer(center);
        tabla.getColumnModel().getColumn(1).setCellRenderer(center);

        /**
         * Renderer/Editor para botones "Editar" y "Eliminar" dentro de la
         * columna de acciones.
         */
        tabla.getColumnModel().getColumn(2).setCellRenderer(new AccionesRenderer());
        tabla.getColumnModel().getColumn(2).setCellEditor(new AccionesEditor(new JCheckBox()));

        tabla.getColumnModel().getColumn(0).setPreferredWidth(260);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(220);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(180);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(16, 80, 0, 80));
        centerPanel.add(scroll, BorderLayout.CENTER);

        card.add(centerPanel, BorderLayout.CENTER);

        /**
         * Carga inicial de la tabla con los teléfonos en memoria.
         */
        cargarTabla();

        // ===================
        // BOTONES (abajo)
        // ===================
        JButton btnGuardar = crearBotonGrande("Guardar cambios");
        JButton btnAnadir = crearBotonGrande("Añadir teléfono");

        btnGuardar.addActionListener(e -> guardarCambios());
        btnAnadir.addActionListener(e -> anadirTelefono());

        JPanel accionesBottom = new JPanel(new BorderLayout());
        accionesBottom.setOpaque(false);
        accionesBottom.setBorder(new EmptyBorder(10, 80, 0, 80));

        JPanel leftBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftBtns.setOpaque(false);
        leftBtns.add(btnGuardar);

        JPanel rightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightBtns.setOpaque(false);
        rightBtns.add(btnAnadir);

        accionesBottom.add(leftBtns, BorderLayout.WEST);
        accionesBottom.add(rightBtns, BorderLayout.EAST);

        // ===================
        // FOOTER
        // ===================
        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 80, 0, 80));
        footerPanel.add(footer, BorderLayout.WEST);

        JPanel south = new JPanel();
        south.setOpaque(false);
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.add(accionesBottom);
        south.add(footerPanel);

        card.add(south, BorderLayout.SOUTH);
    }

    // ===================
    // LÓGICA UI
    // ===================
    /**
     * <p>
     * Carga/recarga la tabla con la lista de teléfonos en memoria.
     * </p>
     *
     * <p>
     * Limpia el modelo y agrega una fila por cada {@link Telefono}.
     * </p>
     */
    private void cargarTabla() {
        modelo.setRowCount(0);

        if (telefonos == null) {
            return;
        }

        for (Telefono t : telefonos) {
            String tel = (t.getTelefono() == null) ? "" : t.getTelefono();
            String etq = (t.getEtiqueta() == null) ? "" : t.getEtiqueta();

            modelo.addRow(new Object[]{
                tel,
                etq,
                "Editar | Eliminar"
            });
        }
    }

    /**
     * <p>
     * Muestra un diálogo para capturar un nuevo teléfono y lo agrega a la lista
     * en memoria.
     * </p>
     *
     * <p>
     * Validación principal:
     * </p>
     * <ul>
     * <li>El número no puede ser vacío.</li>
     * </ul>
     *
     * <p>
     * Al finalizar, se recarga la tabla.
     * </p>
     */
    private void anadirTelefono() {
        JTextField txtTel = new JTextField();
        JTextField txtEtq = new JTextField();

        Object[] msg = {
            "Número:", txtTel,
            "Etiqueta (opcional):", txtEtq
        };

        int r = JOptionPane.showConfirmDialog(
                this,
                msg,
                "Añadir teléfono",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (r != JOptionPane.OK_OPTION) {
            return;
        }

        String numero = txtTel.getText().trim();
        String etiqueta = txtEtq.getText().trim();

        if (numero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El número no puede estar vacío.");
            return;
        }

        Telefono nuevo = new Telefono();
        nuevo.setTelefono(numero);
        nuevo.setEtiqueta(etiqueta.isEmpty() ? null : etiqueta);
        nuevo.setCliente(clienteActual);

        telefonos.add(nuevo);
        cargarTabla();
    }

    /**
     * <p>
     * Guarda los cambios realizados en la administración de teléfonos.
     * </p>
     *
     * <p>
     * Pasos clave:
     * </p>
     * <ol>
     * <li>Si hay una celda en edición, se detiene la edición para capturar el
     * último valor.</li>
     * <li>Se sincroniza la lista local de teléfonos con el {@link Cliente} en
     * memoria.</li>
     * <li>Se construye una lista desde la tabla para enviarla a la capa de
     * negocio.</li>
     * <li>Se invoca a {@link iClienteBO} para persistir la relación.</li>
     * </ol>
     *
     * <p>
     * Muestra mensajes de éxito o error según corresponda.
     * </p>
     */
    private void guardarCambios() {
        if (tabla.isEditing()) {
            tabla.getCellEditor().stopCellEditing();
        }

        clienteActual.setTelefonos(telefonos);
        ctx.setClienteActual(clienteActual);

        try {
            List<Telefono> listaTelefonos = obtenerTelefonosDesdeTabla();
            ctx.getClienteBO().agregarTelefonos(clienteActual, listaTelefonos);
            JOptionPane.showMessageDialog(this, "Cambios guardados.");
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // ===================
    // COMPONENTES
    // ===================
    /**
     * Crea un botón grande con estilo uniforme para acciones principales.
     *
     * @param text texto visible del botón
     * @return botón configurado
     */
    private JButton crearBotonGrande(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(260, 44));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        b.setBackground(new Color(245, 245, 245));
        return b;
    }

    // ===================
    // Renderer/Editor Acciones
    // ===================
    /**
     * <p>
     * Renderer de la columna "Acción".
     * </p>
     *
     * <p>
     * Muestra botones "Editar" y "Eliminar" dentro de la celda, pero sin
     * manejar clicks, ya que esa responsabilidad corresponde al editor.
     * </p>
     */
    private class AccionesRenderer extends JPanel implements TableCellRenderer {

        /**
         * Botón visual de edición (solo para mostrar en renderer).
         */
        private final JButton btnEditar = new JButton("Editar");

        /**
         * Botón visual de eliminación (solo para mostrar en renderer).
         */
        private final JButton btnEliminar = new JButton("Eliminar");

        /**
         * Construye el renderer de acciones y aplica estilo visual a los
         * botones.
         */
        public AccionesRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
            setOpaque(true);
            setBackground(Color.WHITE);

            estiloBtnAccion(btnEditar);
            estiloBtnAccion(btnEliminar);

            add(btnEditar);
            add(btnEliminar);

            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return this;
        }
    }

    /**
     * <p>
     * Editor de la columna "Acción".
     * </p>
     *
     * <p>
     * A diferencia del renderer, este editor sí maneja eventos de click para:
     * </p>
     *
     * <ul>
     * <li><b>Editar</b> una fila → muestra un diálogo con los valores
     * actuales.</li>
     * <li><b>Eliminar</b> una fila → solicita confirmación y elimina de la
     * lista.</li>
     * </ul>
     */
    private class AccionesEditor extends DefaultCellEditor {

        /**
         * Panel contenedor que se muestra como componente editor.
         */
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));

        /**
         * Botón para editar el teléfono de la fila actual.
         */
        private final JButton btnEditar = new JButton("Editar");

        /**
         * Botón para eliminar el teléfono de la fila actual.
         */
        private final JButton btnEliminar = new JButton("Eliminar");

        /**
         * Índice de fila actualmente editada.
         */
        private int row;

        /**
         * Constructor del editor de acciones.
         *
         * @param checkBox checkbox requerido por {@link DefaultCellEditor}
         */
        public AccionesEditor(JCheckBox checkBox) {
            super(checkBox);

            panel.setOpaque(true);
            panel.setBackground(Color.WHITE);

            estiloBtnAccion(btnEditar);
            estiloBtnAccion(btnEliminar);

            panel.add(btnEditar);
            panel.add(btnEliminar);

            btnEditar.addActionListener(e -> editarFila(row));
            btnEliminar.addActionListener(e -> eliminarFila(row));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.row = row;
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Editar | Eliminar";
        }

        /**
         * <p>
         * Edita la fila indicada mostrando un diálogo con los valores actuales.
         * </p>
         *
         * <p>
         * Validación:
         * </p>
         * <ul>
         * <li>El número no puede ser vacío.</li>
         * </ul>
         *
         * @param row índice de fila a editar
         */
        private void editarFila(int row) {
            if (row < 0 || row >= telefonos.size()) {
                fireEditingStopped();
                return;
            }

            Telefono t = telefonos.get(row);

            JTextField txtTel = new JTextField(t.getTelefono() == null ? "" : t.getTelefono());
            JTextField txtEtq = new JTextField(t.getEtiqueta() == null ? "" : t.getEtiqueta());

            Object[] msg = {
                "Número:", txtTel,
                "Etiqueta (opcional):", txtEtq
            };

            int r = JOptionPane.showConfirmDialog(
                    PantallaAdministrarTelefonosCliente.this,
                    msg,
                    "Editar teléfono",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (r == JOptionPane.OK_OPTION) {
                String numero = txtTel.getText().trim();
                String etiqueta = txtEtq.getText().trim();

                if (numero.isEmpty()) {
                    JOptionPane.showMessageDialog(PantallaAdministrarTelefonosCliente.this, "El número no puede estar vacío.");
                } else {
                    t.setTelefono(numero);
                    t.setEtiqueta(etiqueta.isEmpty() ? null : etiqueta);
                    cargarTabla();
                }
            }

            fireEditingStopped();
        }

        /**
         * <p>
         * Elimina la fila indicada solicitando confirmación al usuario.
         * </p>
         *
         * @param row índice de fila a eliminar
         */
        private void eliminarFila(int row) {
            if (row >= 0 && row < telefonos.size()) {
                int r = JOptionPane.showConfirmDialog(
                        PantallaAdministrarTelefonosCliente.this,
                        "¿Eliminar este teléfono?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );

                if (r == JOptionPane.YES_OPTION) {
                    telefonos.remove(row);
                    cargarTabla();
                }
            }
            fireEditingStopped();
        }
    }

    /**
     * Aplica estilo uniforme a los botones de acción dentro de la tabla.
     *
     * @param b botón a estilizar
     */
    private void estiloBtnAccion(JButton b) {
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new LineBorder(new Color(120, 120, 120), 1));
        b.setBackground(new Color(245, 245, 245));
        b.setPreferredSize(new Dimension(78, 26));
    }

    /**
     * <p>
     * Construye una lista de {@link Telefono} leyendo directamente los valores
     * actuales de la tabla.
     * </p>
     *
     * <p>
     * Consideraciones:
     * </p>
     * <ul>
     * <li>Si existe una edición activa, se detiene para capturar el valor
     * final.</li>
     * <li>Filas con número vacío se ignoran.</li>
     * <li>Etiqueta vacía se normaliza como null.</li>
     * </ul>
     *
     * @return lista de teléfonos obtenida desde la tabla
     */
    private List<Telefono> obtenerTelefonosDesdeTabla() {

        List<Telefono> lista = new ArrayList<>();

        if (tabla.isEditing()) {
            tabla.getCellEditor().stopCellEditing();
        }

        for (int i = 0; i < tabla.getRowCount(); i++) {

            Object numObj = tabla.getValueAt(i, 0);
            Object etqObj = tabla.getValueAt(i, 1);

            String numero = numObj == null ? "" : numObj.toString().trim();
            String etiqueta = etqObj == null ? null : etqObj.toString().trim();

            if (numero.isEmpty()) {
                continue;
            }

            Telefono t = new Telefono();
            t.setTelefono(numero);
            t.setEtiqueta(etiqueta == null || etiqueta.isEmpty() ? null : etiqueta);
            t.setCliente(ctx.getClienteActual());

            lista.add(t);
        }

        return lista;
    }
}
