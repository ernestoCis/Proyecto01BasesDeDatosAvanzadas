/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dominio.Cliente;
import dominio.EstadoCliente;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.text.JTextComponent;
import negocio.Excepciones.NegocioException;

/**
 * <h1>PantallaInicioSesionCliente</h1>
 *
 * <p>
 * Pantalla de <b>inicio de sesión</b> para clientes. Permite capturar usuario y
 * contraseña, alternar la visibilidad de la contraseña y autenticar al cliente
 * mediante la capa de negocio.
 * </p>
 *
 * <p>
 * La UI presenta:
 * </p>
 * <ul>
 * <li>Botón de regreso a {@link Menu}.</li>
 * <li>Título y subtítulo.</li>
 * <li>Campo de usuario.</li>
 * <li>Campo de contraseña con ícono 👁 para mostrar/ocultar.</li>
 * <li>Botón <b>Iniciar Sesión</b>.</li>
 * <li>Botón <b>Crear cuenta</b> para abrir {@link PantallaCrearCuenta}.</li>
 * <li>Footer informativo.</li>
 * </ul>
 *
 * <h2>Autenticación</h2>
 * <p>
 * Al presionar <b>Iniciar Sesión</b> se consulta al cliente con
 * {@code ctx.getClienteBO().consultarCliente(usuario, password)}. Si el cliente
 * está {@link EstadoCliente#Inactivo}, se muestra un aviso. En caso contrario,
 * se guarda la sesión con {@code ctx.setClienteActual(cliente)} y se navega a
 * {@link PantallaSesionIniciadaCliente}.
 * </p>
 *
 * <h2>Mostrar/Ocultar contraseña</h2>
 * <p>
 * El ícono 👁 alterna el eco del {@link JPasswordField} usando
 * {@link #echoDefault} para restaurar el comportamiento original.
 * </p>
 *
 * @author
 */
public class PantallaInicioSesionCliente extends JFrame {

    /**
     * Campo de texto donde se captura el usuario.
     */
    private JTextField txtUsuario;

    /**
     * Campo de contraseña donde se captura la contraseña del cliente.
     */
    private JPasswordField txtContrasena;

    /**
     * Carácter de eco original del {@link #txtContrasena}, usado para restaurar
     * el modo oculto.
     */
    private char echoDefault;

    /**
     * Contexto global de la aplicación; permite acceder a BOs y estado de
     * sesión.
     */
    private final AppContext ctx;

    /**
     * <p>
     * Constructor de la pantalla de inicio de sesión para cliente.
     * </p>
     *
     * <p>
     * Construye la interfaz con:
     * </p>
     * <ul>
     * <li>Fondo beige y tarjeta blanca central.</li>
     * <li>Botón de regreso.</li>
     * <li>Campos de usuario y contraseña.</li>
     * <li>Toggle 👁 para mostrar/ocultar contraseña.</li>
     * <li>Botones de iniciar sesión y crear cuenta.</li>
     * <li>Footer informativo.</li>
     * </ul>
     *
     * <p>
     * Registra los listeners para:
     * </p>
     * <ul>
     * <li>Navegación a {@link Menu} (back).</li>
     * <li>Autenticación y apertura de {@link PantallaSesionIniciadaCliente}
     * (iniciar).</li>
     * <li>Apertura de {@link PantallaCrearCuenta} (crear cuenta).</li>
     * </ul>
     *
     * @param ctx contexto global de la aplicación
     */
    public PantallaInicioSesionCliente(AppContext ctx) {
        this.ctx = ctx;

        setTitle("Panadería - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 620);
        setLocationRelativeTo(null);

        // Fondo general
        JPanel base = new JPanel(new GridBagLayout());
        base.setBackground(new Color(214, 186, 150));
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

        // ---- Parte de arriba ----
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));

        /**
         * Botón de regreso; abre {@link Menu} y cierra la pantalla actual.
         */
        JButton btnBack = new JButton("←");
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.setAlignmentX(Component.LEFT_ALIGNMENT);

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

        JLabel subtitulo = new JLabel("Iniciar Sesión");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        panelCentro.add(Box.createVerticalStrut(40));
        panelCentro.add(titulo);
        panelCentro.add(Box.createVerticalStrut(22));
        panelCentro.add(subtitulo);
        panelCentro.add(Box.createVerticalStrut(18));

        // Form panel
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUsuario = new JTextField();
        configurarCampo(txtUsuario, "Usuario");

        JPanel passRow = new JPanel(new BorderLayout());
        passRow.setOpaque(false);

        passRow.setMinimumSize(new Dimension(280, 42));
        passRow.setPreferredSize(new Dimension(280, 42));
        passRow.setMaximumSize(new Dimension(280, 42));
        passRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtContrasena = new JPasswordField();
        configurarCampo(txtContrasena, "Contraseña");
        echoDefault = txtContrasena.getEchoChar();

        /**
         * Ícono de ojo que alterna la visibilidad de la contraseña.
         */
        JLabel ojo = new JLabel("👁");
        ojo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        ojo.setBorder(new EmptyBorder(0, 8, 0, 8));
        ojo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Toggle mostrar/ocultar contraseña
        ojo.addMouseListener(new java.awt.event.MouseAdapter() {
            /**
             * Bandera local para controlar el estado de visibilidad.
             */
            private boolean visible = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                visible = !visible;
                txtContrasena.setEchoChar(visible ? (char) 0 : echoDefault);
            }
        });

        passRow.add(txtContrasena, BorderLayout.CENTER);
        passRow.add(ojo, BorderLayout.EAST);

        /**
         * Botón que intenta autenticar al cliente y abrir
         * {@link PantallaSesionIniciadaCliente}.
         */
        JButton btnIniciar = crearBotonMediano("Iniciar Sesión");

        /**
         * Botón que navega a {@link PantallaCrearCuenta}.
         */
        JButton btnCrear = crearBotonMediano("Crear cuenta");

        btnIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);

        form.add(txtUsuario);
        form.add(Box.createVerticalStrut(10));
        form.add(passRow);
        form.add(Box.createVerticalStrut(14));
        form.add(btnIniciar);
        form.add(Box.createVerticalStrut(8));
        form.add(btnCrear);

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
            this.dispose();
        });

        btnIniciar.addActionListener(e -> {
            try {
                String usuario = txtUsuario.getText().trim();
                String password = new String(txtContrasena.getPassword());

                Cliente cliente = ctx.getClienteBO().consultarCliente(usuario, password);

                if (cliente.getEstado() == EstadoCliente.Inactivo) {
                    JOptionPane.showMessageDialog(this, "Cliente inactivo");
                } else {
                    // guardar sesión
                    ctx.setClienteActual(cliente);

                    new PantallaSesionIniciadaCliente(ctx).setVisible(true);
                    dispose();
                }
            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCrear.addActionListener(e -> {
            new PantallaCrearCuenta(ctx).setVisible(true);
            this.dispose();
        });
    }

    /**
     * <p>
     * Configura un campo de entrada con dimensiones, fuente, borde, fondo y
     * tooltip.
     * </p>
     *
     * <p>
     * Se utiliza para unificar el estilo entre {@link JTextField} y
     * {@link JPasswordField}, recibiendo un {@link JTextComponent} como
     * parámetro.
     * </p>
     *
     * @param t componente de texto a configurar
     * @param placeholderVisual texto utilizado como tooltip (guía visual)
     */
    private void configurarCampo(JTextComponent t, String placeholderVisual) {
        t.setMinimumSize(new Dimension(280, 42));
        t.setPreferredSize(new Dimension(280, 42));
        t.setMaximumSize(new Dimension(280, 42));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        t.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(8, 10, 8, 10)
        ));
        t.setBackground(Color.WHITE);
        t.setToolTipText(placeholderVisual);
    }

    /**
     * <p>
     * Crea un botón con estilo mediano utilizado en la pantalla (misma fuente,
     * bordes y colores).
     * </p>
     *
     * @param text texto a mostrar en el botón
     * @return {@link JButton} configurado con el estilo estándar de la UI
     */
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
