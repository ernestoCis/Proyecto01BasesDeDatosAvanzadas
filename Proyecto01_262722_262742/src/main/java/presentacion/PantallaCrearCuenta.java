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

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import negocio.BOs.iCuponBO;
import negocio.BOs.iPedidoBO;
import negocio.BOs.iProductoBO;

public class PantallaCrearCuenta extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private char echoDefault;

    private JTextField txtNombres;
    private JTextField txtApellidoP;
    private JTextField txtApellidoM;

    private JTextField txtDia;
    private JTextField txtMes;
    private JTextField txtAnio;

    private JTextField txtCalle;
    private JTextField txtNumero;
    private JTextField txtColonia;
    private JTextField txtCodigoPostal;

    private JTextField txtTelefono;
    private JTextField txtEtiqueta;

    private JButton btnCrear;
    
    private final iProductoBO productoBO;
    private final iCuponBO cuponBO;
    private final iPedidoBO pedidoBO;

    public PantallaCrearCuenta(iProductoBO productoBO, iCuponBO cuponBO, iPedidoBO pedidoBO) {
        
        this.productoBO = productoBO;
        this.cuponBO = cuponBO;
        this.pedidoBO = pedidoBO;
        
        setTitle("Panadería - Crear cuenta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 620);
        setLocationRelativeTo(null);

        // Fondo general
        JPanel base = new JPanel(new GridBagLayout());
        base.setBackground(new Color(214, 186, 150)); // beige suave
        setContentPane(base);

        // Tarjeta central
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

        // ====== TOP (flecha) ======
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        JButton btnBack = new JButton("←"); // antes "↩" (a veces sale cuadrito)
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panelSuperior.add(btnBack, BorderLayout.WEST);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // ====== CENTRO (título + form) ======
        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Panadería");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 58));

        JLabel subtitulo = new JLabel("Crear cuenta");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        // un poco más “arriba” y compacto para dejar espacio al SOUTH
        centro.add(Box.createVerticalStrut(4));
        centro.add(titulo);
        centro.add(Box.createVerticalStrut(6));
        centro.add(subtitulo);
        centro.add(Box.createVerticalStrut(6));
        
        // Formulario (2 columnas)
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(6, 10, 6, 10);
        f.anchor = GridBagConstraints.NORTHWEST;
        f.fill = GridBagConstraints.HORIZONTAL;
        f.weightx = 1;

        JLabel lblAcceso = new JLabel("Datos de acceso");
        lblAcceso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblAcceso.setForeground(new Color(60, 60, 60));

        JLabel lblPersonales = new JLabel("Datos personales");
        lblPersonales.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPersonales.setForeground(new Color(60, 60, 60));

        // Fila 0: acceso
        f.gridx = 0;
        f.gridy = 0;
        f.gridwidth = 1;
        form.add(lblAcceso, f);

        f.gridx = 1;
        f.gridy = 0;
        form.add(Box.createHorizontalStrut(1), f);

        // Fila 1: Usuario | Contraseña (con ojo)
        txtUsuario = new JTextField();
        configurarCampo(txtUsuario);

        JPanel passRow = new JPanel(new BorderLayout());
        passRow.setOpaque(false);

        txtContrasena = new JPasswordField();
        configurarCampo(txtContrasena);
        echoDefault = txtContrasena.getEchoChar();

        JLabel ojo = new JLabel("👁");
        ojo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        ojo.setBorder(new EmptyBorder(0, 8, 0, 8));
        ojo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ojo.addMouseListener(new java.awt.event.MouseAdapter() {
            private boolean visible = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (isPasswordPlaceholderActivo()) {
                    return;
                }
                visible = !visible;
                txtContrasena.setEchoChar(visible ? (char) 0 : echoDefault);
            }
        });

        passRow.add(txtContrasena, BorderLayout.CENTER);
        passRow.add(ojo, BorderLayout.EAST);

        f.gridx = 0;
        f.gridy = 1;
        form.add(txtUsuario, f);

        f.gridx = 1;
        f.gridy = 1;
        form.add(passRow, f);

        // Fila 2: personales
        f.gridx = 0;
        f.gridy = 2;
        form.add(lblPersonales, f);

        f.gridx = 1;
        f.gridy = 2;
        form.add(Box.createHorizontalStrut(1), f);

        // Fila 3: Nombres | Apellido paterno
        txtNombres = new JTextField();
        configurarCampo(txtNombres);
        txtApellidoP = new JTextField();
        configurarCampo(txtApellidoP);

        f.gridx = 0;
        f.gridy = 3;
        form.add(txtNombres, f);

        f.gridx = 1;
        f.gridy = 3;
        form.add(txtApellidoP, f);

        // Fila 4: Fecha (D/M/A) | Apellido materno
        JPanel filaFecha = new JPanel(new GridBagLayout());
        filaFecha.setOpaque(false);

        GridBagConstraints gFecha = new GridBagConstraints();
        gFecha.insets = new Insets(0, 0, 0, 8);
        gFecha.fill = GridBagConstraints.HORIZONTAL;
        gFecha.weightx = 1;

        txtDia = new JTextField();
        configurarCampoPequeno(txtDia);
        txtMes = new JTextField();
        configurarCampoPequeno(txtMes);
        txtAnio = new JTextField();
        configurarCampoPequeno(txtAnio);

        gFecha.gridx = 0;
        filaFecha.add(txtDia, gFecha);
        gFecha.gridx = 1;
        filaFecha.add(txtMes, gFecha);
        gFecha.gridx = 2;
        gFecha.insets = new Insets(0, 0, 0, 0);
        filaFecha.add(txtAnio, gFecha);

        txtApellidoM = new JTextField();
        configurarCampo(txtApellidoM);

        f.gridx = 0;
        f.gridy = 4;
        form.add(filaFecha, f);

        f.gridx = 1;
        f.gridy = 4;
        form.add(txtApellidoM, f);

        // Fila 5: Calle | Número
        txtCalle = new JTextField();
        configurarCampo(txtCalle);
        txtNumero = new JTextField();
        configurarCampo(txtNumero);

        f.gridx = 0;
        f.gridy = 5;
        form.add(txtCalle, f);

        f.gridx = 1;
        f.gridy = 5;
        form.add(txtNumero, f);

        // Fila 6: Colonia | Código Postal
        txtColonia = new JTextField();
        configurarCampo(txtColonia);
        txtCodigoPostal = new JTextField();
        configurarCampo(txtCodigoPostal);

        f.gridx = 0;
        f.gridy = 6;
        form.add(txtColonia, f);

        f.gridx = 1;
        f.gridy = 6;
        form.add(txtCodigoPostal, f);

        // Fila 7: Teléfono | Etiqueta
        txtTelefono = new JTextField();
        configurarCampo(txtTelefono);
        txtEtiqueta = new JTextField();
        configurarCampo(txtEtiqueta);

        f.gridx = 0;
        f.gridy = 7;
        form.add(txtTelefono, f);

        f.gridx = 1;
        f.gridy = 7;
        form.add(txtEtiqueta, f);
        centro.add(form);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);

        // Contenido arriba
        centerWrapper.add(centro, BorderLayout.NORTH);

        // Espacio que empuja el SOUTH hacia abajo
        centerWrapper.add(Box.createVerticalGlue(), BorderLayout.CENTER);

        panelPrincipal.add(centerWrapper, BorderLayout.CENTER);
        // ====== BOTÓN + FOOTER en SOUTH (INFALIBLE) ======
        btnCrear = crearBotonCrear("Crear");

        JPanel southContainer = new JPanel();
        southContainer.setOpaque(false);
        southContainer.setLayout(new BoxLayout(southContainer, BoxLayout.Y_AXIS));

        // Botón centrado
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelBoton.setOpaque(false);
        panelBoton.add(btnCrear);

        // Footer
        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));
        footer.setBorder(new EmptyBorder(0, 4, 0, 0));

        JPanel panelFooter = new JPanel(new BorderLayout());
        panelFooter.setOpaque(false);
        panelFooter.add(footer, BorderLayout.WEST);

        southContainer.add(panelBoton);
        southContainer.add(Box.createVerticalStrut(8));
        southContainer.add(panelFooter);

        panelPrincipal.add(southContainer, BorderLayout.SOUTH);

        // ====== Placeholders (visual) ======
        aplicarPlaceholder(txtUsuario, "Usuario");
        aplicarPlaceholderPassword(txtContrasena, "Contraseña");

        aplicarPlaceholder(txtNombres, "Nombres");
        aplicarPlaceholder(txtApellidoP, "Apellido paterno");
        aplicarPlaceholder(txtApellidoM, "Apellido materno");

        aplicarPlaceholder(txtDia, "Día");
        aplicarPlaceholder(txtMes, "Mes");
        aplicarPlaceholder(txtAnio, "Año");

        aplicarPlaceholder(txtCalle, "Calle");
        aplicarPlaceholder(txtNumero, "Número");
        aplicarPlaceholder(txtColonia, "Colonia");
        aplicarPlaceholder(txtCodigoPostal, "Código Postal");

        aplicarPlaceholder(txtTelefono, "Teléfono");
        aplicarPlaceholder(txtEtiqueta, "Etiqueta ej. (Casa, Trabajo)");

        // ====== Acciones ======
        btnBack.addActionListener(e -> {
            new PantallaInicioSesionCliente(productoBO, cuponBO, pedidoBO).setVisible(true);
            this.dispose();
        });

        btnCrear.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Crear cuenta (pendiente de lógica/BD)");
        });
    }

    // ========== Estilos ==========
    private void configurarCampo(JTextField t) {
        t.setPreferredSize(new Dimension(280, 40));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(7, 10, 7, 10)
        ));
        t.setBackground(Color.WHITE);
    }

    private void configurarCampoPequeno(JTextField t) {
        t.setPreferredSize(new Dimension(86, 40));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(7, 10, 7, 10)
        ));
        t.setBackground(Color.WHITE);
    }

    private JButton crearBotonCrear(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(160, 28));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBackground(new Color(245, 245, 245));
        b.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(2, 12, 2, 12)
        ));
        return b;
    }

    // ========== Placeholders (Visual) ==========
    private void aplicarPlaceholder(JTextField campo, String texto) {
        Color gris = new Color(150, 150, 150);
        Color negro = new Color(30, 30, 30);

        campo.setForeground(gris);
        campo.setText(texto);

        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (campo.getText().equals(texto)) {
                    campo.setText("");
                    campo.setForeground(negro);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
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
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (new String(campo.getPassword()).equals(texto)) {
                    campo.setText("");
                    campo.setForeground(negro);
                    campo.setEchoChar(echoDefault);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
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
