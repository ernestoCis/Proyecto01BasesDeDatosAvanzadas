/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dominio.Empleado;
import dominio.RolUsuario;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import negocio.Excepciones.NegocioException;

/**
 * <h1>PantallaCrearCuentaEmpleado</h1>
 *
 * <p>
 * Pantalla de <b>registro de empleados</b> del sistema.
 * </p>
 *
 * <p>
 * La UI se muestra en una tarjeta central e incluye:
 * </p>
 * <ul>
 * <li>Botón de regreso hacia {@link PantallaInicioSesionEmpleado}.</li>
 * <li>Sección de <b>datos de acceso</b> (usuario y contraseña).</li>
 * <li>Campo de contraseña con ícono de visibilidad (👁).</li>
 * <li>Botón para crear la cuenta del empleado.</li>
 * <li>Footer informativo.</li>
 * </ul>
 *
 * <h2>Placeholders</h2>
 * <p>
 * Se usan placeholders visuales (texto gris). En contraseña, el placeholder se
 * muestra sin enmascarar y al enfocar se restaura el caracter de eco original.
 * </p>
 *
 * <h2>Registro</h2>
 * <p>
 * Al presionar "Crear", se valida que usuario y contraseña no estén vacíos, se
 * construye un {@link Empleado}, se registra con
 * {@code ctx.getEmpleadoBO().registrarEmpleado(emp)} y se regresa al login de
 * empleado.
 * </p>
 *
 * @author 262722, 2627242
 */
public class PantallaCrearCuentaEmpleado extends JFrame {

    /**
     * Contexto global de la aplicación; permite acceder a BOs y estado de
     * sesión.
     */
    private final AppContext ctx;

    /**
     * Campo para capturar el usuario del empleado.
     */
    private JTextField txtUsuario;

    /**
     * Campo para capturar la contraseña del empleado.
     */
    private JPasswordField txtContrasena;

    /**
     * Caracter de eco por defecto del campo contraseña. Se conserva para
     * restaurarlo cuando se oculta/mostrar contraseña o se quita el
     * placeholder.
     */
    private char echoDefault;

    /**
     * <p>
     * Constructor de la pantalla de creación de cuenta para empleado.
     * </p>
     *
     * <p>
     * Construye toda la interfaz:
     * </p>
     * <ul>
     * <li>Fondo beige y tarjeta blanca central</li>
     * <li>Top con flecha de regreso</li>
     * <li>Títulos y sección de datos de acceso</li>
     * <li>Campos de usuario y contraseña con "ojo" para visibilidad</li>
     * <li>Botón para registrar empleado</li>
     * <li>Footer informativo</li>
     * </ul>
     *
     * <p>
     * También inicializa placeholders y define las acciones de navegación y
     * registro.
     * </p>
     *
     * @param ctx contexto global de la aplicación
     */
    public PantallaCrearCuentaEmpleado(AppContext ctx) {
        this.ctx = ctx;

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

        /**
         * Botón de regreso hacia {@link PantallaInicioSesionEmpleado}.
         */
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

        /**
         * Ícono "ojo" para alternar la visibilidad de la contraseña.
         */
        JLabel ojo = new JLabel("👁");
        ojo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        ojo.setBorder(new EmptyBorder(0, 8, 0, 8));
        ojo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        /**
         * Alterna el carácter de eco del {@link #txtContrasena} para mostrar u
         * ocultar el texto. Si el placeholder está activo, no realiza cambios.
         */
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

        filaCampos.add(txtUsuario);
        filaCampos.add(Box.createHorizontalStrut(18));
        filaCampos.add(passRow);

        /**
         * Botón principal para registrar un nuevo empleado.
         */
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
        /**
         * Acción de regresar al inicio de sesión de empleado.
         */
        btnBack.addActionListener(e -> {
            new PantallaInicioSesionEmpleado(ctx).setVisible(true);
            this.dispose();
        });

        /**
         * Acción principal: valida campos, registra el empleado y regresa al
         * login.
         */
        btnCrear.addActionListener(e -> {
            try {
                String usuario = txtUsuario.getText() == null ? "" : txtUsuario.getText().trim();
                String pass = new String(txtContrasena.getPassword());

                if (usuario.equalsIgnoreCase("Usuario")) {
                    usuario = "";
                }
                if (pass.equals("Contraseña")) {
                    pass = "";
                }

                if (usuario.isEmpty()) {
                    throw new NegocioException("El usuario es obligatorio.");
                }
                if (pass.isEmpty()) {
                    throw new NegocioException("La contraseña es obligatoria.");
                }

                // Crear objeto empleado
                Empleado emp = new Empleado();
                emp.setUsuario(usuario);
                emp.setContrasenia(pass);
                emp.setRol(RolUsuario.Empleado);

                ctx.getEmpleadoBO().registrarEmpleado(emp);

                JOptionPane.showMessageDialog(this,
                        "Empleado creado correctamente.");

                // Regresar al login empleado
                new PantallaInicioSesionEmpleado(ctx).setVisible(true);
                this.dispose();

            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * <p>
     * Configura el estilo visual estándar de un {@link JTextField} del
     * formulario.
     * </p>
     *
     * <p>
     * Aplica:
     * </p>
     * <ul>
     * <li>Dimensión preferida y máxima</li>
     * <li>Fuente</li>
     * <li>Borde compuesto (línea + padding)</li>
     * <li>Fondo blanco</li>
     * </ul>
     *
     * @param t campo a configurar
     */
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

    /**
     * <p>
     * Crea un botón con tamaño mediano para acciones principales en la
     * pantalla.
     * </p>
     *
     * @param text texto del botón
     * @return botón configurado con estilo
     */
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

    /**
     * <p>
     * Aplica un placeholder visual a un {@link JTextField}.
     * </p>
     *
     * <p>
     * Cuando el campo gana foco y el texto coincide con el placeholder, se
     * limpia y cambia a color oscuro. Cuando pierde foco y queda vacío, se
     * restaura el placeholder con color gris.
     * </p>
     *
     * @param campo componente a configurar
     * @param texto texto placeholder a mostrar
     */
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

    /**
     * <p>
     * Aplica un placeholder visual a un {@link JPasswordField}.
     * </p>
     *
     * <p>
     * Mientras el placeholder está activo:
     * </p>
     * <ul>
     * <li>El texto se muestra en color gris</li>
     * <li>El campo no enmascara (echoChar = 0)</li>
     * </ul>
     *
     * <p>
     * Al enfocar el campo, si el texto coincide con el placeholder, se limpia,
     * se cambia a color oscuro y se restaura el {@link #echoDefault}.
     * </p>
     *
     * @param campo componente a configurar
     * @param texto texto placeholder a mostrar
     */
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

    /**
     * <p>
     * Indica si el placeholder de contraseña está activo en el campo
     * {@link #txtContrasena}.
     * </p>
     *
     * <p>
     * Se considera activo cuando:
     * </p>
     * <ul>
     * <li>El texto del campo es "Contraseña"</li>
     * <li>El {@code echoChar} es 0 (no enmascarado)</li>
     * </ul>
     *
     * @return {@code true} si el placeholder de contraseña está activo;
     * {@code false} en caso contrario
     */
    private boolean isPasswordPlaceholderActivo() {
        return new String(txtContrasena.getPassword()).equals("Contraseña")
                && txtContrasena.getEchoChar() == 0;
    }
}
