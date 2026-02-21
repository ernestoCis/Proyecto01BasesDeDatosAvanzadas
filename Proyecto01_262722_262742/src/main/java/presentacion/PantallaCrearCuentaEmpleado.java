/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author Isaac
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import negocio.BOs.iCuponBO;
import negocio.BOs.iPedidoBO;
import negocio.BOs.iProductoBO;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class PantallaCrearCuentaEmpleado extends JFrame {

    private final JFrame pantallaLoginEmpleado;

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private char echoDefault;

    public PantallaCrearCuentaEmpleado(JFrame pantallaLoginEmpleado) {

        this.pantallaLoginEmpleado = pantallaLoginEmpleado;

        setTitle("Panadería - Crear cuenta Empleado");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        JButton btnBack = new JButton("←");
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panelSuperior.add(btnBack, BorderLayout.WEST);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // ---- Centro ----
        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Panadería");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 58));

        JLabel subtitulo = new JLabel("Crear cuenta – Empleado");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        panelCentro.add(Box.createVerticalStrut(30));
        panelCentro.add(titulo);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(subtitulo);
        panelCentro.add(Box.createVerticalStrut(24));

        JLabel lblAcceso = new JLabel("Datos de acceso");
        lblAcceso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblAcceso.setForeground(new Color(60, 60, 60));
        lblAcceso.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cont = new JPanel();
        cont.setOpaque(false);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        cont.setAlignmentX(Component.CENTER_ALIGNMENT);
        cont.setMaximumSize(new Dimension(620, 200));

        JPanel filaCampos = new JPanel();
        filaCampos.setOpaque(false);
        filaCampos.setLayout(new BoxLayout(filaCampos, BoxLayout.X_AXIS));

        txtUsuario = new JTextField();
        configurarCampo(txtUsuario);
        aplicarPlaceholder(txtUsuario, "Usuario");

        JPanel passRow = new JPanel(new BorderLayout());
        passRow.setOpaque(false);

        txtContrasena = new JPasswordField();
        configurarCampo(txtContrasena);
        echoDefault = txtContrasena.getEchoChar();
        aplicarPlaceholderPassword(txtContrasena, "Contraseña");

        JLabel ojo = new JLabel("👁");
        ojo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        ojo.setBorder(new EmptyBorder(0, 8, 0, 8));
        ojo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ojo.addMouseListener(new java.awt.event.MouseAdapter() {
            private boolean visible = false;
            @Override public void mouseClicked(MouseEvent e) {
                if (isPasswordPlaceholderActivo()) return;
                visible = !visible;
                txtContrasena.setEchoChar(visible ? (char) 0 : echoDefault);
            }
        });

        passRow.add(txtContrasena, BorderLayout.CENTER);
        passRow.add(ojo, BorderLayout.EAST);

        filaCampos.add(txtUsuario);
        filaCampos.add(Box.createHorizontalStrut(18));
        filaCampos.add(passRow);

        JButton btnCrear = crearBotonMediano("Crear");
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);

        cont.add(lblAcceso);
        cont.add(Box.createVerticalStrut(8));
        cont.add(filaCampos);
        cont.add(Box.createVerticalStrut(40));
        cont.add(btnCrear);

        panelCentro.add(cont);
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
            pantallaLoginEmpleado.setVisible(true);
            this.dispose();
        });

        btnCrear.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Crear empleado (pendiente de lógica/BD)");
        });
    }

    private void configurarCampo(JTextField t) {
        t.setPreferredSize(new Dimension(280, 40));
        t.setMaximumSize(new Dimension(280, 40));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(7, 10, 7, 10)
        ));
        t.setBackground(Color.WHITE);
    }

    private JButton crearBotonMediano(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(120, 30));
        b.setMaximumSize(new Dimension(120, 30));
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

    private void aplicarPlaceholder(JTextField campo, String texto) {
        Color gris = new Color(150, 150, 150);
        Color negro = new Color(30, 30, 30);
        campo.setForeground(gris);
        campo.setText(texto);

        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if (campo.getText().equals(texto)) {
                    campo.setText("");
                    campo.setForeground(negro);
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (campo.getText().trim().isEmpty()) {
                    campo.setForeground(gris);
                    campo.setText(texto);
                }
            }
        });
    }

    private void aplicarPlaceholderPassword(JPasswordField campo, String texto) {
        Color gris = new Color(150, 150, 150);
        Color negro = new Color(30, 30, 30);

        campo.setForeground(gris);
        campo.setEchoChar((char) 0);
        campo.setText(texto);

        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if (new String(campo.getPassword()).equals(texto)) {
                    campo.setText("");
                    campo.setForeground(negro);
                    campo.setEchoChar(echoDefault);
                }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (new String(campo.getPassword()).trim().isEmpty()) {
                    campo.setForeground(gris);
                    campo.setEchoChar((char) 0);
                    campo.setText(texto);
                }
            }
        });
    }

    private boolean isPasswordPlaceholderActivo() {
        return new String(txtContrasena.getPassword()).equals("Contraseña")
                && txtContrasena.getEchoChar() == 0;
    }
}