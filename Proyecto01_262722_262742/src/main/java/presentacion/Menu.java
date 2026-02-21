/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import javax.swing.JFrame;

/**
 *
 * @author Isaac
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import negocio.BOs.iCuponBO;
import negocio.BOs.iPedidoBO;
import negocio.BOs.iProductoBO;


public class Menu extends JFrame {
    
    private final iProductoBO productoBO;
    private final iCuponBO cuponBO;
    private final iPedidoBO pedidoBO;

    public Menu(iProductoBO productoBO, iCuponBO cuponBO, iPedidoBO pedidoBO) {
        
        this.productoBO = productoBO;
        this.cuponBO = cuponBO;
        this.pedidoBO = pedidoBO;

        setTitle("Panadería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 620);
        setLocationRelativeTo(null);

        // Fondo general
        JPanel base = new JPanel(new GridBagLayout());
        base.setBackground(new Color(214, 186, 150)); // beige suave
        setContentPane(base);

        // Tarjeta central (blanca con borde negro)
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

        // ---- Parte de arriba ----
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        // Izquierda: Empleado + flecha
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));

        JLabel lblEmpleado = new JLabel("Empleado");
        lblEmpleado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEmpleado.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnFlecha = new JButton("↩");
        btnFlecha.setFocusPainted(false);
        btnFlecha.setBorderPainted(false);
        btnFlecha.setContentAreaFilled(false);
        btnFlecha.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        btnFlecha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFlecha.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelIzquierdo.add(lblEmpleado);
        panelIzquierdo.add(Box.createVerticalStrut(4));
        panelIzquierdo.add(btnFlecha);

        // Derecha: Iniciar Sesión + icono
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelDerecho.setOpaque(false);

        JLabel lblLogin = new JLabel("Iniciar Sesión");
        lblLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));

        panelDerecho.add(lblLogin);
        panelDerecho.add(userIcon);

        panelSuperior.add(panelIzquierdo, BorderLayout.WEST);
        panelSuperior.add(panelDerecho, BorderLayout.EAST);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // ---- Centro ----
        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Panadería");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 58));

        panelCentro.add(Box.createVerticalStrut(28));
        panelCentro.add(titulo);
        panelCentro.add(Box.createVerticalStrut(30));

        JLabel subtitulo = new JLabel("Selecciona el tipo de pedido:");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panelCentro.add(subtitulo);

        panelCentro.add(Box.createVerticalStrut(22));

        // Botón Programado
        JButton btnProgramado = crearBotonGrande("Programado");
        btnProgramado.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel txtProgramado = new JLabel("(Requiere inicio sesión)");
        txtProgramado.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtProgramado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtProgramado.setForeground(new Color(90, 90, 90));

        panelCentro.add(btnProgramado);
        panelCentro.add(Box.createVerticalStrut(6));
        panelCentro.add(txtProgramado);

        panelCentro.add(Box.createVerticalStrut(18));

        // Botón Express
        JButton btnExpress = crearBotonGrande("Express");
        btnExpress.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel notaExpress = new JLabel("(No requiere registro)");
        notaExpress.setAlignmentX(Component.CENTER_ALIGNMENT);
        notaExpress.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notaExpress.setForeground(new Color(90, 90, 90));

        panelCentro.add(btnExpress);
        panelCentro.add(Box.createVerticalStrut(6));
        panelCentro.add(notaExpress);

        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // ---- Footer ----
        JLabel panelFooter = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        panelFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panelFooter.setForeground(new Color(80, 80, 80));
        panelFooter.setBorder(new EmptyBorder(0, 4, 0, 0));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(panelFooter, BorderLayout.WEST);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // ---- Acciones de botones ----
        btnFlecha.addActionListener(e -> {
            new PantallaInicioSesionEmpleado(productoBO, cuponBO, pedidoBO).setVisible(true);
            this.dispose();
        });
        btnProgramado.addActionListener(e -> {
            try {

                PantallaCatalogo pantalla = new PantallaCatalogo(productoBO, cuponBO, pedidoBO);
                pantalla.setVisible(true);

                this.dispose(); // cierra el menú actual

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al abrir catálogo: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        btnExpress.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ir a Pedido Express"));
        lblLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        userIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Click login
        MouseAdapter clickLogin = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new PantallaInicioSesionCliente(productoBO, cuponBO, pedidoBO).setVisible(true);
                Menu.this.dispose();
            }
        };
        lblLogin.addMouseListener(clickLogin);
        userIcon.addMouseListener(clickLogin);
    }

    private JButton crearBotonGrande(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(320, 60));
        b.setMaximumSize(new Dimension(320, 60));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBackground(new Color(245, 245, 245));
        b.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(10, 18, 10, 18)
        ));
        return b;
    }

    // Para clicks "tipo link"
    private static abstract class MouseAdapter extends java.awt.event.MouseAdapter {
    }

}
