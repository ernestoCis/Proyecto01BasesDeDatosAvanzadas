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

    public PantallaActualizarCliente(JFrame pantallaAnterior, AppContext ctx) {
        this.pantallaAnterior = pantallaAnterior;
        this.ctx = ctx;
        this.clienteActual = ctx.getClienteActual();

        setTitle("Panadería - Mi cuenta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 900);
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
        card.setPreferredSize(new Dimension(900, 800));
        root.add(card);

        // ========= TOP =========
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JButton btnFlecha = new JButton("↩");
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

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.add(btnFlecha);

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

        top.add(panelIzquierdo, BorderLayout.WEST);
        top.add(titles, BorderLayout.CENTER);

        card.add(top, BorderLayout.NORTH);

        // ========= CENTER =========
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(18, 70, 10, 70));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.gridy = 0;

        JLabel lblAcceso = new JLabel("Datos de acceso");
        lblAcceso.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblPersonales = new JLabel("Datos personales");
        lblPersonales.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // ----- Sección acceso -----
        addSection(center, g, lblAcceso);

        txtUsuario = crearTextField("Usuario");
        txtUsuario.setText(nz(clienteActual.getUsuario()));
        chkUsuario = crearCheckPara(txtUsuario);

        txtContrasenia = new JPasswordField();
        estiloCampo(txtContrasenia, "Contraseña");
        passEcho = txtContrasenia.getEchoChar();
        txtContrasenia.setText("");
        txtContrasenia.setEnabled(false);

        chkContrasenia = crearCheckPara(txtContrasenia);

        JButton btnOjo = new JButton("👁");
        btnOjo.setFocusPainted(false);
        btnOjo.setBorderPainted(false);
        btnOjo.setContentAreaFilled(false);
        btnOjo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnOjo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        btnOjo.addActionListener(e -> togglePassword());

        JPanel passWrap = campoConLabel("Contraseña", wrapPassword(txtContrasenia, btnOjo));

        addRow4(center, g,
                campoConLabel("Usuario", txtUsuario), chkUsuario,
                passWrap, chkContrasenia
        );

        // ----- Sección personales -----
        addSection(center, g, lblPersonales);

        txtNombres = crearTextField("Nombres");
        txtNombres.setText(nz(clienteActual.getNombres()));
        chkNombres = crearCheckPara(txtNombres);

        txtApellidoP = crearTextField("Apellido paterno");
        txtApellidoP.setText(nz(clienteActual.getApellidoPaterno()));
        chkApellidoP = crearCheckPara(txtApellidoP);

        addRow4(center, g,
                campoConLabel("Nombres", txtNombres), chkNombres,
                campoConLabel("Apellido paterno", txtApellidoP), chkApellidoP
        );

        // Fecha + Apellido materno
        LocalDate fn = clienteActual.getFechaNacimiento();

        JPanel panelFecha = new JPanel(new GridLayout(1, 3, 10, 0));
        panelFecha.setOpaque(false);

        txtDia = crearMini("Día", fn != null ? String.valueOf(fn.getDayOfMonth()) : "");
        txtMes = crearMini("Mes", fn != null ? String.valueOf(fn.getMonthValue()) : "");
        txtAnio = crearMini("Año", fn != null ? String.valueOf(fn.getYear()) : "");

        panelFecha.add(txtDia);
        panelFecha.add(txtMes);
        panelFecha.add(txtAnio);

        chkFecha = new JCheckBox();
        chkFecha.setOpaque(false);
        chkFecha.setPreferredSize(new Dimension(18, 18));
        chkFecha.addActionListener(e -> {
            boolean en = chkFecha.isSelected();
            txtDia.setEnabled(en);
            txtMes.setEnabled(en);
            txtAnio.setEnabled(en);
        });

        txtDia.setEnabled(false);
        txtMes.setEnabled(false);
        txtAnio.setEnabled(false);

        txtApellidoM = crearTextField("Apellido materno");
        txtApellidoM.setText(nz(clienteActual.getApellidoMaterno()));
        chkApellidoM = crearCheckPara(txtApellidoM);

        addRow4(center, g,
                campoConLabel("Fecha de nacimiento", panelFecha), chkFecha,
                campoConLabel("Apellido materno", txtApellidoM), chkApellidoM
        );

        // Dirección
        Direccion dir = clienteActual.getDireccion();
        String calle = dir != null ? nz(dir.getCalle()) : "";
        String colonia = dir != null ? nz(dir.getColonia()) : "";
        String numero = dir != null ? String.valueOf(dir.getNumero()) : "";
        String cp = dir != null ? String.valueOf(dir.getCp()) : "";

        txtCalle = crearTextField("Calle");
        txtCalle.setText(calle);
        chkCalle = crearCheckPara(txtCalle);

        txtNumero = crearTextField("Número");
        txtNumero.setText(numero);
        chkNumero = crearCheckPara(txtNumero);

        addRow4(center, g,
                campoConLabel("Calle", txtCalle), chkCalle,
                campoConLabel("Número", txtNumero), chkNumero
        );

        txtColonia = crearTextField("Colonia");
        txtColonia.setText(colonia);
        chkColonia = crearCheckPara(txtColonia);

        txtCP = crearTextField("Código Postal");
        txtCP.setText(cp);
        chkCP = crearCheckPara(txtCP);

        addRow4(center, g,
                campoConLabel("Colonia", txtColonia), chkColonia,
                campoConLabel("Código Postal", txtCP), chkCP
        );

        card.add(center, BorderLayout.CENTER);

        // ========= SOUTH =========
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
        btnTelefonos.addActionListener(e -> {
            new PantallaAdministrarTelefonosCliente(this, ctx).setVisible(true);
            dispose();
        });
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
            Cliente actualizado = copiarCliente(clienteActual);

            if (chkUsuario.isSelected()) {
                actualizado.setUsuario(txtUsuario.getText().trim());
            }

            if (chkContrasenia.isSelected()) {
                String nueva = new String(txtContrasenia.getPassword()).trim();
                if (nueva.isEmpty()) {
                    throw new NegocioException("La contraseña no puede estar vacía.");
                }
                actualizado.setContrasenia(nueva);
            }else{
                actualizado.setContrasenia(clienteActual.getContrasenia()); // conservar la contraseña
            }

            if (chkNombres.isSelected()) {
                actualizado.setNombres(txtNombres.getText().trim());
            }
            if (chkApellidoP.isSelected()) {
                actualizado.setApellidoPaterno(txtApellidoP.getText().trim());
            }
            if (chkApellidoM.isSelected()) {
                actualizado.setApellidoMaterno(txtApellidoM.getText().trim());
            }

            if (chkFecha.isSelected()) {
                int d = parseInt(txtDia.getText(), "Día");
                int m = parseInt(txtMes.getText(), "Mes");
                int a = parseInt(txtAnio.getText(), "Año");
                actualizado.setFechaNacimiento(LocalDate.of(a, m, d));
            }

            boolean tocaDireccion = chkCalle.isSelected() || chkNumero.isSelected() || chkColonia.isSelected() || chkCP.isSelected();
            if (tocaDireccion) {
                Direccion dir = actualizado.getDireccion();
                if (dir == null) {
                    dir = new Direccion();
                }

                if (chkCalle.isSelected()) {
                    dir.setCalle(txtCalle.getText().trim());
                }
                if (chkColonia.isSelected()) {
                    dir.setColonia(txtColonia.getText().trim());
                }
                if (chkNumero.isSelected()) {
                    dir.setNumero(parseInt(txtNumero.getText(), "Número"));
                }
                if (chkCP.isSelected()) {
                    dir.setCp(parseInt(txtCP.getText(), "Código Postal"));
                }

                dir.setCliente(actualizado);
                actualizado.setDireccion(dir);
            }

            ctx.getClienteBO().actualizarCliente(actualizado);
            JOptionPane.showMessageDialog(this, "Cambios guardados correctamente.");
            resetChecksYCampos();

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =================== Layout helpers ===================
    private void addSection(JPanel parent, GridBagConstraints g, JComponent sectionLabel) {
        g.gridx = 0;
        g.gridwidth = 4;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        parent.add(sectionLabel, g);
        g.gridy++;
        g.gridwidth = 1;
    }

    private void addRow4(JPanel parent, GridBagConstraints g, Component leftField, JCheckBox leftChk,
            Component rightField, JCheckBox rightChk) {

        g.gridx = 0;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.CENTER;
        parent.add(leftField, g);

        g.gridx = 1;
        g.weightx = 0;
        g.fill = GridBagConstraints.NONE;
        g.anchor = GridBagConstraints.WEST;
        parent.add(wrapChk((JCheckBox) leftChk), g);

        g.gridx = 2;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.CENTER;
        parent.add(rightField, g);

        g.gridx = 3;
        g.weightx = 0;
        g.fill = GridBagConstraints.NONE;
        g.anchor = GridBagConstraints.WEST;
        parent.add(wrapChk((JCheckBox) rightChk), g);

        
        g.gridy++;
    }

    // =================== UI helpers ===================
    private JPanel campoConLabel(String label, Component campo) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(new Color(35, 35, 35));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Alineación del campo
        if (campo instanceof JComponent jc) {
            jc.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        p.add(l);
        p.add(Box.createVerticalStrut(4));
        p.add(campo);
        return p;
    }

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

    private JCheckBox crearCheckPara(JComponent campo) {
        JCheckBox chk = new JCheckBox();
        chk.setOpaque(false);
        chk.setFocusPainted(false);
        chk.setBorderPaintedFlat(true);     // se ve menos “cuadrote”
        chk.setMargin(new Insets(0, 0, 0, 0));
        chk.setPreferredSize(new Dimension(18, 18));
        chk.setMinimumSize(new Dimension(18, 18));
        chk.setMaximumSize(new Dimension(18, 18));

        chk.addActionListener(e -> campo.setEnabled(chk.isSelected()));
        return chk;
    }

    private JPanel wrapPassword(JPasswordField pass, JButton ojo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        p.add(pass, BorderLayout.CENTER);

        JPanel east = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        east.setOpaque(false);
        east.add(ojo);

        p.add(east, BorderLayout.EAST);
        return p;
    }

    private void togglePassword() {
        passVisible = !passVisible;
        txtContrasenia.setEchoChar(passVisible ? (char) 0 : passEcho);
    }

    private void resetChecksYCampos() {
        JCheckBox[] checks = {chkUsuario, chkContrasenia, chkNombres, chkApellidoP, chkApellidoM, chkFecha,
            chkCalle, chkNumero, chkColonia, chkCP};
        for (JCheckBox c : checks) {
            c.setSelected(false);
        }

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
    
    private JPanel wrapChk(JCheckBox chk) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(26, 40));  // ancho para que no baile, alto similar al campo
        p.add(chk, BorderLayout.NORTH);             // lo pega arriba
        return p;
    }
}
