/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 * <p>
 * Ventana principal para el flujo de trabajo del <b>empleado</b> dentro del
 * sistema.
 * </p>
 *
 * <p>
 * Desde este menú, el empleado puede acceder a dos funciones principales:
 * </p>
 *
 * <ul>
 * <li><b>Gestionar pedidos</b> → Accede a la pantalla de administración de
 * pedidos.</li>
 * <li><b>Gestionar productos</b> → Accede a la pantalla de administración de
 * productos.</li>
 * </ul>
 *
 * <p>
 * Además, incluye una opción para <b>cerrar sesión</b>, lo cual limpia el
 * estado del usuario autenticado dentro del {@link AppContext} y regresa al
 * menú principal.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 * <li>Presentar opciones de navegación para el empleado.</li>
 * <li>Redirigir a pantallas de gestión (pedidos / productos).</li>
 * <li>Cerrar sesión del empleado mediante
 * {@link AppContext#cerrarSesion()}.</li>
 * </ul>
 *
 * @author 262722, 2627242
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MenuEmpleado extends JFrame {

    /**
     * Contexto global de la aplicación. Permite acceder a BOs y controlar el
     * estado de sesión.
     */
    private final AppContext ctx;

    /**
     * <p>
     * Constructor del menú de empleado.
     * </p>
     *
     * <p>
     * Configura la interfaz visual, agrega los botones de navegación y define
     * la lógica de cambio de pantallas.
     * </p>
     *
     * @param ctx contexto global de la aplicación
     */
    public MenuEmpleado(AppContext ctx) {
        this.ctx = ctx;

        setTitle("Panadería - Menú Empleado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 620);
        setLocationRelativeTo(null);

        // ======================
        // Panel base (fondo)
        // ======================
        JPanel base = new JPanel(new GridBagLayout());
        base.setBackground(new Color(214, 186, 150));
        setContentPane(base);

        // ======================
        // Panel principal (tarjeta)
        // ======================
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
        // Panel centro (contenido)
        // ======================
        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Panadería");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 58));

        JLabel subtitulo = new JLabel("Selecciona el tipo Acción:");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        panelCentro.add(Box.createVerticalStrut(40));
        panelCentro.add(titulo);
        panelCentro.add(Box.createVerticalStrut(12));
        panelCentro.add(subtitulo);
        panelCentro.add(Box.createVerticalStrut(28));

        /**
         * Botón que redirige a la gestión de pedidos.
         */
        JButton btnGestionarPedidos = crearBotonGrande("Gestionar pedidos");
        btnGestionarPedidos.setAlignmentX(Component.CENTER_ALIGNMENT);

        /**
         * Botón que redirige a la gestión de productos.
         */
        JButton btnGestionarProductos = crearBotonGrande("Gestionar productos");
        btnGestionarProductos.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCentro.add(btnGestionarPedidos);
        panelCentro.add(Box.createVerticalStrut(18));
        panelCentro.add(btnGestionarProductos);

        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // ======================
        // Footer (derechos + cerrar sesión)
        // ======================
        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));
        footer.setBorder(new EmptyBorder(0, 4, 0, 0));

        /**
         * Botón para cerrar sesión del empleado.
         */
        JButton btnCerrarSesion = crearBotonPequeno("Cerrar Sesión");

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(footer, BorderLayout.WEST);
        panelInferior.add(btnCerrarSesion, BorderLayout.EAST);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // ======================
        // Navegación
        // ======================
        /**
         * Abre la pantalla de gestión de pedidos.
         */
        btnGestionarPedidos.addActionListener(e -> {
            new PantallaGestionarPedidos(ctx).setVisible(true);
            this.dispose();
        });

        /**
         * Abre la pantalla de gestión de productos.
         */
        btnGestionarProductos.addActionListener(e -> {
            new PantallaGestionarProductos(ctx).setVisible(true);
            this.dispose();
        });

        /**
         * Cierra la sesión actual, regresa al menú principal y cierra esta
         * ventana.
         */
        btnCerrarSesion.addActionListener(e -> {
            ctx.cerrarSesion();
            new Menu(ctx).setVisible(true);
            this.dispose();
        });
    }

    /**
     * <p>
     * Crea un botón grande con estilo uniforme para las acciones principales.
     * </p>
     *
     * @param text texto visible del botón
     * @return botón configurado con tamaño y estilo
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
     * <p>
     * Crea un botón pequeño con estilo uniforme para acciones secundarias (como
     * cerrar sesión).
     * </p>
     *
     * @param text texto visible del botón
     * @return botón configurado con tamaño y estilo
     */
    private JButton crearBotonPequeno(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(140, 28));
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
