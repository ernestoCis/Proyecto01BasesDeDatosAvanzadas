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

public class Menu extends JFrame {

    public Menu() {
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
        btnFlecha.addActionListener(e -> JOptionPane.showMessageDialog(this, "Regresar (Empleado)"));
        btnProgramado.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ir a Pedido Programado (Login requerido)"));
        btnExpress.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ir a Pedido Express"));
        lblLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        userIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Click simulando login
        MouseAdapter clickLogin = new MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(Menu.this, "Abrir pantalla de Iniciar Sesión");
            }
        };
        lblLogin.addMouseListener(clickLogin);
        userIcon.addMouseListener(clickLogin);
    }
    
//    public PantallaCatalogo() {
//        setTitle("Panadería - Catálogo");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(920, 600);
//        setLocationRelativeTo(null);
//
//        // Fondo beige (marco)
//        JPanel root = new JPanel(new GridBagLayout());
//        root.setBackground(new Color(214, 186, 150));
//        setContentPane(root);
//
//        // Tarjeta blanca con borde
//        JPanel card = new JPanel(new BorderLayout());
//        card.setBackground(Color.WHITE);
//        card.setBorder(new CompoundBorder(
//                new LineBorder(new Color(30, 30, 30), 2, false),
//                new EmptyBorder(18, 22, 18, 22)
//        ));
//        card.setPreferredSize(new Dimension(860, 520));
//
//        root.add(card);
//
//        // =================== TOP BAR ===================
//        JPanel topBar = new JPanel(new BorderLayout());
//        topBar.setOpaque(false);
//
//        // Izquierda: flecha grande
//        JButton btnFlecha = new JButton("↩");
//        btnFlecha.setFocusPainted(false);
//        btnFlecha.setBorderPainted(false);
//        btnFlecha.setContentAreaFilled(false);
//        btnFlecha.setFont(new Font("Segoe UI", Font.PLAIN, 40));
//        btnFlecha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        btnFlecha.addActionListener(e -> JOptionPane.showMessageDialog(this, "Regresar"));
//
//        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
//        panelIzquierdo.setOpaque(false);
//        panelIzquierdo.add(btnFlecha);
//
//        // Derecha: carrito con badge "4"
//        JPanel panelCarrito = new JPanel(null);
//        panelCarrito.setOpaque(false);
//        panelCarrito.setPreferredSize(new Dimension(90, 50));
//
//        JLabel lblCarrito = new JLabel("🛒");
//        lblCarrito.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
//        lblCarrito.setBounds(35, 2, 50, 45);
//        lblCarrito.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//        JLabel badge = new JLabel("4", SwingConstants.CENTER);
//        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
//        badge.setOpaque(true);
//        badge.setBackground(Color.WHITE);
//        badge.setBorder(new LineBorder(new Color(30, 30, 30), 1));
//        badge.setBounds(18, 0, 22, 18);
//
//        panelCarrito.add(badge);
//        panelCarrito.add(lblCarrito);
//
//        lblCarrito.addMouseListener(new java.awt.event.MouseAdapter() {
//            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
//                JOptionPane.showMessageDialog(PantallaCatalogo.this, "Abrir carrito");
//            }
//        });
//
//        topBar.add(panelIzquierdo, BorderLayout.WEST);
//        topBar.add(panelCarrito, BorderLayout.EAST);
//
//        // =================== HEADER (TÍTULOS) ===================
//        JPanel header = new JPanel();
//        header.setOpaque(false);
//        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
//
//        JLabel title = new JLabel("Panadería");
//        title.setAlignmentX(Component.CENTER_ALIGNMENT);
//        title.setFont(new Font("Segoe UI", Font.BOLD, 56));
//
//        JLabel subtitle = new JLabel("Catálogo de productos");
//        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
//        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 22));
//
//        header.add(Box.createVerticalStrut(2));
//        header.add(title);
//        header.add(Box.createVerticalStrut(4));
//        header.add(subtitle);
//        header.add(Box.createVerticalStrut(10));
//
//        // =================== GRID SCROLL ===================
//        List<Producto> productos = List.of(
//                new Producto("Concha Tradicional", "Dulce", 16),
//                new Producto("Pan de Mantequilla", "Dulce", 20),
//                new Producto("Croissant Clásico", "Dulce", 23),
//                new Producto("Pan de Caja Artesanal", "Integral", 55),
//                new Producto("Baguette Tradicional", "Salado", 30),
//                new Producto("Cuernito Dulce", "Dulce", 18)
//        );
//
//        JPanel grid = new JPanel(new GridLayout(0, 3, 28, 18));
//        grid.setBackground(Color.WHITE);
//        grid.setBorder(new EmptyBorder(6, 10, 12, 10));
//
//        for (Producto p : productos) {
//            grid.add(crearTarjetaProducto(p));
//        }
//
//        JScrollPane scroll = new JScrollPane(grid);
//        scroll.setBorder(null);
//        scroll.getViewport().setBackground(Color.WHITE);
//        scroll.getVerticalScrollBar().setUnitIncrement(18);
//
//        // =================== CENTRO COMPLETO ===================
//        JPanel center = new JPanel(new BorderLayout());
//        center.setOpaque(false);
//        center.add(header, BorderLayout.NORTH);
//        center.add(scroll, BorderLayout.CENTER);
//
//        // =================== FOOTER ===================
//        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
//        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
//        footer.setForeground(new Color(80, 80, 80));
//
//        JPanel bottom = new JPanel(new BorderLayout());
//        bottom.setOpaque(false);
//        bottom.add(footer, BorderLayout.WEST);
//
//        // Ensamble final
//        card.add(topBar, BorderLayout.NORTH);
//        card.add(center, BorderLayout.CENTER);
//        card.add(bottom, BorderLayout.SOUTH);
//    }

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
    private static abstract class MouseAdapter extends java.awt.event.MouseAdapter {}

}
