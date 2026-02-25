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

public class PantallaAdministrarTelefonosCliente extends JFrame {

    private final JFrame pantallaAnterior;
    private final AppContext ctx;
    private final Cliente clienteActual;

    private JTable tabla;
    private DefaultTableModel modelo;

    private List<Telefono> telefonos; 

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

        // Fondo beige
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(new Color(214, 186, 150));
        setContentPane(root);

        // Tarjeta blanca
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(30, 30, 30), 2, false),
                new EmptyBorder(18, 22, 18, 22)
        ));
        card.setPreferredSize(new Dimension(860, 560));
        root.add(card);

        // =================== TOP ===================
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JButton btnFlecha = new JButton("←");
        btnFlecha.setFocusPainted(false);
        btnFlecha.setBorderPainted(false);
        btnFlecha.setContentAreaFilled(false);
        btnFlecha.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        btnFlecha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFlecha.addActionListener(e -> {
            if (pantallaAnterior != null) pantallaAnterior.setVisible(true);
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

        // =================== TABLA ===================
        String[] cols = {"Número", "Etiqueta", "Acción"};

        modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) {
                // solo "Acción" editable para el editor de botones
                return col == 2;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(34);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setReorderingAllowed(false);

        // Centrar columnas
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(0).setCellRenderer(center);
        tabla.getColumnModel().getColumn(1).setCellRenderer(center);

        // Renderer/Editor para botones Editar/Eliminar
        tabla.getColumnModel().getColumn(2).setCellRenderer(new AccionesRenderer());
        tabla.getColumnModel().getColumn(2).setCellEditor(new AccionesEditor(new JCheckBox()));

        // Anchos
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

        cargarTabla();

        // =================== BOTONES ABAJO ===================
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

        // =================== FOOTER ===================
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

    // =================== LÓGICA UI ===================

    private void cargarTabla() {
        modelo.setRowCount(0);

        if (telefonos == null) return;

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

        if (r != JOptionPane.OK_OPTION) return;

        String numero = txtTel.getText().trim();
        String etiqueta = txtEtq.getText().trim();

        if (numero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El número no puede estar vacío.");
            return;
        }

        Telefono nuevo = new Telefono();
        // id lo asignará la BD si luego lo persistimos
        nuevo.setTelefono(numero);
        nuevo.setEtiqueta(etiqueta.isEmpty() ? null : etiqueta);
        nuevo.setCliente(clienteActual);

        telefonos.add(nuevo);
        cargarTabla();
    }

    private void guardarCambios() {
        // IMPORTANTE: asegurarnos que se guarde cualquier edición activa
        if (tabla.isEditing()) {
            tabla.getCellEditor().stopCellEditing();
        }

        // Actualizar el cliente actual en memoria
        clienteActual.setTelefonos(telefonos);
        ctx.setClienteActual(clienteActual); // si tu ctx no tiene set, quítalo
        
        try{
            List<Telefono> listaTelefonos = obtenerTelefonosDesdeTabla();
            ctx.getClienteBO().agregarTelefonos(clienteActual, listaTelefonos);
            JOptionPane.showMessageDialog(this, "Cambios guardados.");
        }catch(NegocioException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
        

        
    }

    // =================== COMPONENTES ===================

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

    // =================== Renderer/Editor Acciones ===================

    private class AccionesRenderer extends JPanel implements TableCellRenderer {
        private final JButton btnEditar = new JButton("Editar");
        private final JButton btnEliminar = new JButton("Eliminar");

        public AccionesRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
            setOpaque(true);
            setBackground(Color.WHITE);

            estiloBtnAccion(btnEditar);
            estiloBtnAccion(btnEliminar);

            add(btnEditar);
            add(btnEliminar);

            // Renderer no maneja clicks
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

    private class AccionesEditor extends DefaultCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        private final JButton btnEditar = new JButton("Editar");
        private final JButton btnEliminar = new JButton("Eliminar");
        private int row;

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

    private void estiloBtnAccion(JButton b) {
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new LineBorder(new Color(120, 120, 120), 1));
        b.setBackground(new Color(245, 245, 245));
        b.setPreferredSize(new Dimension(78, 26));
    }
    
    private List<Telefono> obtenerTelefonosDesdeTabla() {

        List<Telefono> lista = new ArrayList<>();

        // IMPORTANTE: detener edición activa si la hay
        if (tabla.isEditing()) {
            tabla.getCellEditor().stopCellEditing();
        }

        for (int i = 0; i < tabla.getRowCount(); i++) {

            Object numObj = tabla.getValueAt(i, 0);
            Object etqObj = tabla.getValueAt(i, 1);

            String numero = numObj == null ? "" : numObj.toString().trim();
            String etiqueta = etqObj == null ? null : etqObj.toString().trim();

            if (numero.isEmpty()) {
                continue; // ignorar filas vacías
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