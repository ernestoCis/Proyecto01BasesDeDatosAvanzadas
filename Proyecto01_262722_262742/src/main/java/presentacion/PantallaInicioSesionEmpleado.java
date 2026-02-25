/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;


import dominio.Empleado;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import negocio.Excepciones.NegocioException;

public class PantallaInicioSesionEmpleado extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private char echoDefault;

    private final AppContext ctx;

    public PantallaInicioSesionEmpleado(AppContext ctx) {
        this.ctx = ctx;

        setTitle("Panadería - Iniciar Sesión Empleado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 620);
        setLocationRelativeTo(null);

        JPanel base = new JPanel(new GridBagLayout());
        base.setBackground(new Color(214, 186, 150));
        setContentPane(base);

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

        // ---- Arriba ----
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));

        JLabel lblCliente = new JLabel("Cliente");
        lblCliente.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCliente.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnBack = new JButton("←");
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelIzquierdo.add(lblCliente);
        panelIzquierdo.add(Box.createVerticalStrut(4));
        panelIzquierdo.add(btnBack);

        panelSuperior.add(panelIzquierdo, BorderLayout.WEST);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // ---- Centro ----
        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Panadería");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 58));

        JLabel subtitulo = new JLabel("Iniciar Sesión – Empleado");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        panelCentro.add(Box.createVerticalStrut(40));
        panelCentro.add(titulo);
        panelCentro.add(Box.createVerticalStrut(22));
        panelCentro.add(subtitulo);
        panelCentro.add(Box.createVerticalStrut(18));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUsuario = new JTextField();
        configurarCampo(txtUsuario, "Usuario");

        JPanel passRow = new JPanel(new BorderLayout());
        passRow.setOpaque(false);

        txtContrasena = new JPasswordField();
        configurarCampo(txtContrasena, "Contraseña");
        echoDefault = txtContrasena.getEchoChar();

        JLabel ojo = new JLabel("👁");
        ojo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        ojo.setBorder(new EmptyBorder(0, 8, 0, 8));
        ojo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ojo.addMouseListener(new java.awt.event.MouseAdapter() {
            private boolean visible = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                visible = !visible;
                txtContrasena.setEchoChar(visible ? (char) 0 : echoDefault);
            }
        });

        passRow.add(txtContrasena, BorderLayout.CENTER);
        passRow.add(ojo, BorderLayout.EAST);

        JButton btnIniciar = crearBotonMediano("Iniciar Sesión");
        btnIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCrearCuenta = crearBotonMediano("Crear cuenta");
        btnCrearCuenta.setAlignmentX(Component.CENTER_ALIGNMENT);

        form.add(txtUsuario);
        form.add(Box.createVerticalStrut(10));
        form.add(passRow);
        form.add(Box.createVerticalStrut(14));
        form.add(btnIniciar);
        form.add(Box.createVerticalStrut(8));
        form.add(btnCrearCuenta);

        panelCentro.add(form);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // ---- Footer ----
        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));
        footer.setBorder(new EmptyBorder(0, 4, 0, 0));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(footer, BorderLayout.WEST);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // ---- Acciones ----
        btnBack.addActionListener(e -> {
            new Menu(ctx).setVisible(true);
            dispose();
        });

        btnIniciar.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String contrasenia = new String(txtContrasena.getPassword());

            if (usuario.isEmpty() || contrasenia.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa usuario y contraseña.");
                return;
            }

            try {
                Empleado emp = ctx.getUsuarioBO().iniciarSesionEmpleado(usuario, contrasenia);

                // guardar sesión
                ctx.setEmpleadoActual(emp);

                new MenuEmpleado(ctx).setVisible(true);
                dispose();

            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCrearCuenta.addActionListener(e -> {
            new PantallaCrearCuentaEmpleado(ctx).setVisible(true);
            dispose();
        });
    }

    private void configurarCampo(JTextField t, String tooltip) {
        t.setPreferredSize(new Dimension(280, 42));
        t.setMaximumSize(new Dimension(280, 42));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        t.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(8, 10, 8, 10)
        ));
        t.setBackground(Color.WHITE);
        t.setToolTipText(tooltip);
    }

    private JButton crearBotonMediano(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(150, 28));
        b.setMaximumSize(new Dimension(150, 28));
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