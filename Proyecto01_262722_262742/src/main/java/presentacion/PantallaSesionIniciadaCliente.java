package presentacion;

import dominio.Cliente;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class PantallaSesionIniciadaCliente extends JFrame {

    private final AppContext ctx;
    private final Cliente cliente;

    public PantallaSesionIniciadaCliente(AppContext ctx) {
        this.ctx = ctx;
        this.cliente = ctx.getClienteActual();

        setTitle("Panadería - Sesión iniciada");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 650);
        setLocationRelativeTo(null);

        // Fondo general
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(new Color(214, 186, 150));
        setContentPane(root);

        // Tarjeta blanca con borde negro
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(30, 30, 30), 2, false),
                new EmptyBorder(18, 22, 18, 22)
        ));
        card.setPreferredSize(new Dimension(860, 560));
        root.add(card);

        // ----- parte de arriba -----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        // boton de actualizar perfil y historial
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelDerecho.setOpaque(false);

        String nombreMostrar = (cliente != null && cliente.getUsuario() != null)
                ? cliente.getUsuario()
                : "Usuario";

        JLabel lblBienvenida = new JLabel("Bienvenid@, " + nombreMostrar);
        lblBienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnUsuario = crearBotonIconoUsuario();
        btnUsuario.addActionListener(e
                -> JOptionPane.showMessageDialog(this, "Abrir perfil / cuenta (pendiente)")
        );

        panelDerecho.add(lblBienvenida);
        panelDerecho.add(btnUsuario);

        topBar.add(panelDerecho, BorderLayout.EAST);
        card.add(topBar, BorderLayout.NORTH);

        // ----- centro -----
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Panadería");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 70));

        JLabel subtitle = new JLabel("Selecciona el tipo de pedido:");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JButton btnProgramado = crearBotonGrande("Programado");
        btnProgramado.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel notaProgramado = new JLabel("(Requiere inicio sesión)");
        notaProgramado.setAlignmentX(Component.CENTER_ALIGNMENT);
        notaProgramado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notaProgramado.setForeground(new Color(90, 90, 90));

        JButton btnExpress = crearBotonGrande("Express");
        btnExpress.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel notaExpress = new JLabel("(No requiere registro)");
        notaExpress.setAlignmentX(Component.CENTER_ALIGNMENT);
        notaExpress.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notaExpress.setForeground(new Color(90, 90, 90));

        center.add(Box.createVerticalStrut(70));
        center.add(title);
        center.add(Box.createVerticalStrut(45));
        center.add(subtitle);
        center.add(Box.createVerticalStrut(22));
        center.add(btnProgramado);
        center.add(Box.createVerticalStrut(6));
        center.add(notaProgramado);
        center.add(Box.createVerticalStrut(22));
        center.add(btnExpress);
        center.add(Box.createVerticalStrut(6));
        center.add(notaExpress);

        card.add(center, BorderLayout.CENTER);

        // ----- parte de abajo -----
        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);

        // Botón Cerrar sesión
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        btnCerrarSesion.setBackground(new Color(245, 245, 245));
        btnCerrarSesion.setPreferredSize(new Dimension(160, 36));

        JPanel panelCerrar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelCerrar.setOpaque(false);
        panelCerrar.add(btnCerrarSesion);

        // Footer
        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.add(footer, BorderLayout.WEST);

        JPanel wrapSouth = new JPanel();
        wrapSouth.setOpaque(false);
        wrapSouth.setLayout(new BoxLayout(wrapSouth, BoxLayout.Y_AXIS));
        wrapSouth.add(panelCerrar);
        wrapSouth.add(Box.createVerticalStrut(10));
        wrapSouth.add(footerPanel);

        south.add(wrapSouth, BorderLayout.CENTER);
        card.add(south, BorderLayout.SOUTH);

        // ----- acciones -----
        btnProgramado.addActionListener(e -> {
            new PantallaCatalogo(ctx).setVisible(true);
            dispose();
        });

        btnExpress.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Ir a Pedido Express (pendiente)");
        });

        btnCerrarSesion.addActionListener(e -> {
            new Menu(ctx).setVisible(true);
            dispose();
        });
    }

    private JButton crearBotonGrande(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(360, 70));
        b.setMaximumSize(new Dimension(360, 70));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBackground(new Color(245, 245, 245));
        b.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(10, 18, 10, 18)
        ));
        return b;
    }

    private JButton crearBotonIconoUsuario() {
        JButton b = new JButton("👤");
        b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMargin(new Insets(0, 0, 0, 0));
        return b;
    }
}
