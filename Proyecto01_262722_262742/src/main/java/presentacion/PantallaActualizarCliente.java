package presentacion;

import dominio.Cliente;
import dominio.Direccion;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import negocio.Excepciones.NegocioException;

public class PantallaActualizarCliente extends JFrame {

    private final JFrame pantallaAnterior;
    private final AppContext ctx;
    private final Cliente clienteActual;

    // Campos
    private JTextField txtUsuario;
    private JPasswordField txtContrasenia;

    private JTextField txtNombres;
    private JTextField txtApellidoP;
    private JTextField txtApellidoM;

    private JTextField txtDia;
    private JTextField txtMes;
    private JTextField txtAnio;

    private JTextField txtCalle;
    private JTextField txtNumero;
    private JTextField txtColonia;
    private JTextField txtCP;

    // Checks
    private JCheckBox chkUsuario, chkContrasenia, chkNombres, chkApellidoP, chkApellidoM,
                      chkFecha, chkCalle, chkNumero, chkColonia, chkCP;

    // Mostrar contraseña
    private boolean passVisible = false;
    private char passEcho;

    public PantallaActualizarCliente(JFrame pantallaAnterior, AppContext ctx, Cliente clienteActual) {
        this.pantallaAnterior = pantallaAnterior;
        this.ctx = ctx;
        this.clienteActual = clienteActual;

        setTitle("Panadería - Mi cuenta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 700);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(new Color(214, 186, 150));
        setContentPane(root);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(30, 30, 30), 2, false),
                new EmptyBorder(18, 22, 18, 22)
        ));
        card.setPreferredSize(new Dimension(900, 600));
        root.add(card);

        // ========= TOP: flecha + títulos =========
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JButton btnFlecha = new JButton("↩");
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

        JLabel lblSub = new JLabel("Mi cuenta");
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 22));

        titles.add(Box.createVerticalStrut(10));
        titles.add(lblPan);
        titles.add(Box.createVerticalStrut(4));
        titles.add(lblSub);

        top.add(leftTop, BorderLayout.WEST);
        top.add(titles, BorderLayout.CENTER);

        card.add(top, BorderLayout.NORTH);

        // ========= CENTER: formulario =========
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(18, 70, 10, 70));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        // Secciones
        JLabel lblAcceso = new JLabel("Datos de acceso");
        lblAcceso.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblPersonales = new JLabel("Datos personales");
        lblPersonales.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // --- fila sección acceso ---
        g.gridx = 0; g.gridy = 0; g.gridwidth = 6;
        center.add(lblAcceso, g);

        // Usuario (col izq)
        txtUsuario = crearTextField("Usuario");
        txtUsuario.setText(nz(clienteActual.getUsuario()));
        chkUsuario = new JCheckBox();
        prepararCheck(chkUsuario, txtUsuario);

        // Contraseña (col der) + ojo
        txtContrasenia = new JPasswordField();
        estiloCampo(txtContrasenia, "Contraseña");
        passEcho = txtContrasenia.getEchoChar();

        // No precargues la contraseña real por seguridad (opcional):
        txtContrasenia.setText("");

        chkContrasenia = new JCheckBox();
        prepararCheck(chkContrasenia, txtContrasenia);

        JButton btnOjo = new JButton("👁");
        btnOjo.setFocusPainted(false);
        btnOjo.setBorderPainted(false);
        btnOjo.setContentAreaFilled(false);
        btnOjo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnOjo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        btnOjo.addActionListener(e -> togglePassword());

        // fila usuario/contraseña
        g.gridwidth = 2; g.gridy = 1;

        g.gridx = 0; center.add(txtUsuario, g);
        g.gridx = 2; center.add(chkUsuario, g);

        g.gridx = 3; center.add(wrapPassword(txtContrasenia, btnOjo), g);
        g.gridx = 5; center.add(chkContrasenia, g);

        // --- sección personales ---
        g.gridx = 0; g.gridy = 2; g.gridwidth = 6;
        center.add(lblPersonales, g);

        txtNombres = crearTextField("Nombres");
        txtNombres.setText(nz(clienteActual.getNombres()));
        chkNombres = new JCheckBox();
        prepararCheck(chkNombres, txtNombres);

        txtApellidoP = crearTextField("Apellido paterno");
        txtApellidoP.setText(nz(clienteActual.getApellidoPaterno()));
        chkApellidoP = new JCheckBox();
        prepararCheck(chkApellidoP, txtApellidoP);

        txtApellidoM = crearTextField("Apellido materno");
        txtApellidoM.setText(nz(clienteActual.getApellidoMaterno()));
        chkApellidoM = new JCheckBox();
        prepararCheck(chkApellidoM, txtApellidoM);

        // fila nombres / apellido paterno
        g.gridwidth = 2; g.gridy = 3;
        g.gridx = 0; center.add(txtNombres, g);
        g.gridx = 2; center.add(chkNombres, g);
        g.gridx = 3; center.add(txtApellidoP, g);
        g.gridx = 5; center.add(chkApellidoP, g);

        // fecha (día/mes/año) + apellido materno
        JPanel panelFecha = new JPanel(new GridLayout(1, 3, 10, 0));
        panelFecha.setOpaque(false);

        LocalDate fn = clienteActual.getFechaNacimiento();
        txtDia = crearMini("Día", fn != null ? String.valueOf(fn.getDayOfMonth()) : "");
        txtMes = crearMini("Mes", fn != null ? String.valueOf(fn.getMonthValue()) : "");
        txtAnio = crearMini("Año", fn != null ? String.valueOf(fn.getYear()) : "");

        panelFecha.add(txtDia);
        panelFecha.add(txtMes);
        panelFecha.add(txtAnio);

        // habilitar/inhabilitar los 3 con un solo checkbox
        chkFecha = new JCheckBox();
        prepararCheck(chkFecha, txtDia);
        // para mes y año también
        chkFecha.addActionListener(e -> {
            boolean en = chkFecha.isSelected();
            txtDia.setEnabled(en);
            txtMes.setEnabled(en);
            txtAnio.setEnabled(en);
        });
        txtMes.setEnabled(false);
        txtAnio.setEnabled(false);

        g.gridy = 4;
        g.gridx = 0; center.add(panelFecha, g);
        g.gridx = 2; center.add(chkFecha, g);
        g.gridx = 3; center.add(txtApellidoM, g);
        g.gridx = 5; center.add(chkApellidoM, g);

        // Dirección
        Direccion dir = clienteActual.getDireccion();
        String calle = dir != null ? nz(dir.getCalle()) : "";
        String colonia = dir != null ? nz(dir.getColonia()) : "";
        String numero = dir != null ? String.valueOf(dir.getNumero()) : "";
        String cp = dir != null ? String.valueOf(dir.getCp()) : "";

        txtCalle = crearTextField("Calle");
        txtCalle.setText(calle);
        chkCalle = new JCheckBox();
        prepararCheck(chkCalle, txtCalle);

        txtNumero = crearTextField("Número");
        txtNumero.setText(numero);
        chkNumero = new JCheckBox();
        prepararCheck(chkNumero, txtNumero);

        txtColonia = crearTextField("Colonia");
        txtColonia.setText(colonia);
        chkColonia = new JCheckBox();
        prepararCheck(chkColonia, txtColonia);

        txtCP = crearTextField("Código Postal");
        txtCP.setText(cp);
        chkCP = new JCheckBox();
        prepararCheck(chkCP, txtCP);

        // fila calle / número
        g.gridy = 5;
        g.gridx = 0; center.add(txtCalle, g);
        g.gridx = 2; center.add(chkCalle, g);
        g.gridx = 3; center.add(txtNumero, g);
        g.gridx = 5; center.add(chkNumero, g);

        // fila colonia / cp
        g.gridy = 6;
        g.gridx = 0; center.add(txtColonia, g);
        g.gridx = 2; center.add(chkColonia, g);
        g.gridx = 3; center.add(txtCP, g);
        g.gridx = 5; center.add(chkCP, g);

        card.add(center, BorderLayout.CENTER);

        // ========= SOUTH: botones =========
        JButton btnGuardar = crearBoton("Guardar cambios");
        JButton btnHistorial = crearBoton("Consultar historial");
        JButton btnTelefonos = crearBoton("Consultar teléfonos");

        JButton btnDesactivar = new JButton("Desactivar cuenta");
        btnDesactivar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnDesactivar.setFocusPainted(false);
        btnDesactivar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDesactivar.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        btnDesactivar.setBackground(new Color(245, 245, 245));
        btnDesactivar.setPreferredSize(new Dimension(200, 38));

        btnGuardar.addActionListener(e -> guardarCambios());
        btnHistorial.addActionListener(e -> JOptionPane.showMessageDialog(this, "Historial (pendiente)"));
        btnTelefonos.addActionListener(e -> JOptionPane.showMessageDialog(this, "Teléfonos (pendiente)"));
        btnDesactivar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Desactivar cuenta (pendiente)"));

        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        filaBotones.setOpaque(false);
        filaBotones.setBorder(new EmptyBorder(0, 70, 0, 70));
        filaBotones.add(btnGuardar);
        filaBotones.add(btnHistorial);
        filaBotones.add(btnTelefonos);

        JPanel filaDesactivar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        filaDesactivar.setOpaque(false);
        filaDesactivar.setBorder(new EmptyBorder(0, 70, 0, 70));
        filaDesactivar.add(btnDesactivar);

        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(0, 70, 0, 70));
        footerPanel.add(footer, BorderLayout.WEST);

        JPanel south = new JPanel();
        south.setOpaque(false);
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.add(filaBotones);
        south.add(filaDesactivar);
        south.add(footerPanel);

        card.add(south, BorderLayout.SOUTH);
    }

    // =================== Guardar cambios ===================

    private void guardarCambios() {
        try {
            // Copia segura del cliente actual
            Cliente actualizado = copiarCliente(clienteActual);

            // Solo aplica campos si su checkbox está seleccionado
            if (chkUsuario.isSelected()) {
                actualizado.setUsuario(txtUsuario.getText().trim());
            }

            if (chkContrasenia.isSelected()) {
                String nueva = new String(txtContrasenia.getPassword()).trim();
                if (nueva.isEmpty()) throw new NegocioException("La contraseña no puede estar vacía.");
                actualizado.setContrasenia(nueva); // en BO lo hasheas
            }

            if (chkNombres.isSelected()) actualizado.setNombres(txtNombres.getText().trim());
            if (chkApellidoP.isSelected()) actualizado.setApellidoPaterno(txtApellidoP.getText().trim());
            if (chkApellidoM.isSelected()) actualizado.setApellidoMaterno(txtApellidoM.getText().trim());

            if (chkFecha.isSelected()) {
                int d = parseInt(txtDia.getText(), "Día");
                int m = parseInt(txtMes.getText(), "Mes");
                int a = parseInt(txtAnio.getText(), "Año");
                actualizado.setFechaNacimiento(LocalDate.of(a, m, d));
            }

            // Dirección (solo si alguno de estos checks está seleccionado)
            boolean tocaDireccion = chkCalle.isSelected() || chkNumero.isSelected() || chkColonia.isSelected() || chkCP.isSelected();
            if (tocaDireccion) {
                Direccion dir = actualizado.getDireccion();
                if (dir == null) dir = new Direccion();

                if (chkCalle.isSelected()) dir.setCalle(txtCalle.getText().trim());
                if (chkColonia.isSelected()) dir.setColonia(txtColonia.getText().trim());
                if (chkNumero.isSelected()) dir.setNumero(parseInt(txtNumero.getText(), "Número"));
                if (chkCP.isSelected()) dir.setCp(parseInt(txtCP.getText(), "Código Postal"));

                dir.setCliente(actualizado);
                actualizado.setDireccion(dir);
            }

            // ====== LLAMADA A NEGOCIO ======
            // Ajusta el nombre del método a tu BO real:
            ctx.getClienteBO().actualizarCliente(actualizado);

            JOptionPane.showMessageDialog(this, "Cambios guardados correctamente.");

            // (Opcional) desmarcar checks y bloquear campos otra vez
            resetChecksYCampos();

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =================== Helpers UI ===================

    private JTextField crearTextField(String placeholder) {
        JTextField t = new JTextField();
        estiloCampo(t, placeholder);
        t.setEnabled(false);
        return t;
    }

    private JTextField crearMini(String placeholder, String value) {
        JTextField t = new JTextField(value);
        estiloCampo(t, placeholder);
        t.setEnabled(false);
        t.setHorizontalAlignment(SwingConstants.CENTER);
        return t;
    }

    private void estiloCampo(JComponent c, String placeholder) {
        c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        c.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2),
                new EmptyBorder(8, 10, 8, 10)
        ));
        c.setForeground(new Color(70, 70, 70));

        // placeholder “visual”: tooltip
        c.setToolTipText(placeholder);
    }

    private JButton crearBoton(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(190, 38));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        b.setBackground(new Color(245, 245, 245));
        return b;
    }

    private void prepararCheck(JCheckBox chk, JComponent campo) {
        chk.setOpaque(false);
        chk.addActionListener(e -> campo.setEnabled(chk.isSelected()));
    }

    private JPanel wrapPassword(JPasswordField pass, JButton ojo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(pass, BorderLayout.CENTER);

        JPanel east = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        east.setOpaque(false);
        east.add(ojo);

        p.add(east, BorderLayout.EAST);
        pass.setEnabled(false);
        return p;
    }

    private void togglePassword() {
        passVisible = !passVisible;
        txtContrasenia.setEchoChar(passVisible ? (char) 0 : passEcho);
    }

    private void resetChecksYCampos() {
        JCheckBox[] checks = {chkUsuario, chkContrasenia, chkNombres, chkApellidoP, chkApellidoM, chkFecha,
                chkCalle, chkNumero, chkColonia, chkCP};

        for (JCheckBox c : checks) c.setSelected(false);

        txtUsuario.setEnabled(false);
        txtContrasenia.setEnabled(false);
        txtNombres.setEnabled(false);
        txtApellidoP.setEnabled(false);
        txtApellidoM.setEnabled(false);

        txtDia.setEnabled(false);
        txtMes.setEnabled(false);
        txtAnio.setEnabled(false);

        txtCalle.setEnabled(false);
        txtNumero.setEnabled(false);
        txtColonia.setEnabled(false);
        txtCP.setEnabled(false);
    }

    private Cliente copiarCliente(Cliente base) {
        Cliente c = new Cliente();
        c.setId(base.getId());
        c.setUsuario(base.getUsuario());
        c.setContrasenia(base.getContrasenia());
        c.setRol(base.getRol());

        c.setNombres(base.getNombres());
        c.setApellidoPaterno(base.getApellidoPaterno());
        c.setApellidoMaterno(base.getApellidoMaterno());
        c.setFechaNacimiento(base.getFechaNacimiento());

        if (base.getDireccion() != null) {
            Direccion d = new Direccion();
            d.setId(base.getDireccion().getId());
            d.setCalle(base.getDireccion().getCalle());
            d.setColonia(base.getDireccion().getColonia());
            d.setNumero(base.getDireccion().getNumero());
            d.setCp(base.getDireccion().getCp());
            d.setCliente(c);
            c.setDireccion(d);
        }

        // teléfonos no se tocan aquí (van en otra pantalla)
        return c;
    }

    private int parseInt(String s, String campo) throws NegocioException {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            throw new NegocioException("Valor inválido en " + campo);
        }
    }

    private String nz(String s) {
        return s == null ? "" : s;
    }
}