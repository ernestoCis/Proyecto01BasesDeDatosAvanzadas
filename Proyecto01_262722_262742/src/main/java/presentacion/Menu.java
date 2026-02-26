/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import javax.swing.JFrame;

/**
 * <p>
 * Ventana principal del sistema de la Panadería.
 * </p>
 *
 * <p>
 * Esta clase representa el punto de entrada visual de la aplicación. Permite al
 * usuario seleccionar el tipo de pedido que desea realizar:
 * </p>
 *
 * <ul>
 * <li><b>Pedido Programado</b> → Requiere inicio de sesión del cliente.</li>
 * <li><b>Pedido Express</b> → No requiere registro previo.</li>
 * </ul>
 *
 * <p>
 * También permite el acceso al inicio de sesión tanto para clientes como para
 * empleados.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 * <li>Mostrar la pantalla inicial del sistema.</li>
 * <li>Redirigir a pantallas de autenticación.</li>
 * <li>Redirigir al catálogo Express.</li>
 * <li>Mantener referencia al contexto global de la aplicación.</li>
 * </ul>
 *
 * <p>
 * La clase utiliza el objeto {@link AppContext} para mantener la consistencia
 * del estado de la aplicación.
 * </p>
 *
 * @author 262722, 2627242
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class Menu extends JFrame {

    /**
     * Contexto global de la aplicación. Permite acceder a los BO y al estado de
     * sesión.
     */
    private final AppContext ctx;

    /**
     * <p>
     * Constructor del menú principal.
     * </p>
     *
     * <p>
     * Configura toda la interfaz gráfica, estilos visuales, distribución de
     * componentes y eventos de navegación.
     * </p>
     *
     * @param ctx contexto global de la aplicación
     */
    public Menu(AppContext ctx) {
        this.ctx = ctx;

        /**
         * Se limpia cualquier cliente en sesión al entrar al menú principal.
         */
        ctx.setClienteActual(null);

        setTitle("Panadería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 620);
        setLocationRelativeTo(null);

        // ======================
        // Fondo principal
        // ======================
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

        // ======================
        // Panel Superior
        // ======================
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        /**
         * Panel izquierdo: acceso a login de empleado.
         */
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));

        JLabel lblEmpleado = new JLabel("Empleado");
        lblEmpleado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEmpleado.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnFlecha = new JButton("←");
        btnFlecha.setFocusPainted(false);
        btnFlecha.setBorderPainted(false);
        btnFlecha.setContentAreaFilled(false);
        btnFlecha.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        btnFlecha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFlecha.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelIzquierdo.add(lblEmpleado);
        panelIzquierdo.add(Box.createVerticalStrut(4));
        panelIzquierdo.add(btnFlecha);

        /**
         * Panel derecho: acceso a login cliente.
         */
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

        // ======================
        // Panel Centro
        // ======================
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

        /**
         * Botón para pedidos programados.
         */
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

        /**
         * Botón para pedidos express.
         */
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

        // ======================
        // Footer
        // ======================
        JLabel panelFooter = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        panelFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panelFooter.setForeground(new Color(80, 80, 80));
        panelFooter.setBorder(new EmptyBorder(0, 4, 0, 0));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(panelFooter, BorderLayout.WEST);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // ======================
        // Eventos
        // ======================
        /**
         * Redirige a inicio de sesión de empleado.
         */
        btnFlecha.addActionListener(e -> {
            new PantallaInicioSesionEmpleado(ctx).setVisible(true);
            this.dispose();
        });

        /**
         * Redirige a inicio de sesión de cliente.
         */
        btnProgramado.addActionListener(e -> {
            try {
                new PantallaInicioSesionCliente(ctx).setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al abrir inicio de sesión: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        /**
         * Redirige al catálogo express.
         */
        btnExpress.addActionListener(e -> {
            new PantallaCatalogoExpress(ctx).setVisible(true);
            dispose();
        });

        lblLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        userIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        /**
         * Evento tipo enlace para login cliente.
         */
        MouseAdapter clickLogin = new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new PantallaInicioSesionCliente(ctx).setVisible(true);
                Menu.this.dispose();
            }
        };
        lblLogin.addMouseListener(clickLogin);
        userIcon.addMouseListener(clickLogin);
    }

    /**
     * <p>
     * Método auxiliar para crear botones grandes con estilo uniforme.
     * </p>
     *
     * @param text texto que mostrará el botón
     * @return botón configurado
     */
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

    /**
     * Clase interna utilizada para manejar eventos tipo enlace.
     */
    private static abstract class MouseAdapter extends java.awt.event.MouseAdapter {
    }
}
