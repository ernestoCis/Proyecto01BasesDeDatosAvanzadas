/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dominio.Cliente;
import dominio.Direccion;
import dominio.RolUsuario;
import dominio.Telefono;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import java.awt.event.MouseEvent;
import java.time.LocalDate;
import negocio.Excepciones.NegocioException;

/**
 * <h1>PantallaCrearCuenta</h1>
 *
 * <p>
 * Pantalla de <b>registro</b> para que un usuario cree una cuenta de cliente en
 * el sistema.
 * </p>
 *
 * <p>
 * La UI se organiza en una tarjeta central con:
 * </p>
 * <ul>
 * <li>Botón de regreso hacia {@link PantallaInicioSesionCliente}.</li>
 * <li>Sección de <b>datos de acceso</b> (usuario y contraseña con icono de
 * visibilidad).</li>
 * <li>Sección de <b>datos personales</b> (nombres, apellidos y fecha de
 * nacimiento).</li>
 * <li>Sección de <b>dirección</b> (calle, número, colonia y código
 * postal).</li>
 * <li>Sección de <b>teléfono</b> (teléfono y etiqueta).</li>
 * <li>Botón para crear la cuenta y footer informativo.</li>
 * </ul>
 *
 * <h2>Placeholders</h2>
 * <p>
 * Los campos usan placeholders visuales (texto gris). En el caso de la
 * contraseña, el placeholder se muestra <b>sin enmascarar</b> y al enfocar el
 * campo se restaura el caracter de eco original.
 * </p>
 *
 * <h2>Registro</h2>
 * <p>
 * Al presionar "Crear", se construyen instancias de
 * {@link Cliente}, {@link Telefono} y {@link Direccion} a partir de la
 * información del formulario, se registra el cliente en la capa de negocio y se
 * guarda como cliente actual en el contexto ({@link AppContext}).
 * </p>
 *
 * @author 262722, 2627242
 */
public class PantallaCrearCuenta extends JFrame {

    /**
     * Campo para capturar el usuario del cliente.
     */
    private JTextField txtUsuario;

    /**
     * Campo para capturar la contraseña del cliente.
     */
    private JPasswordField txtContrasena;

    /**
     * Caracter de eco por defecto del campo contraseña. Se conserva para
     * restaurarlo cuando se oculta/mostrar contraseña o se quita el
     * placeholder.
     */
    private char echoDefault;

    /**
     * Campo para capturar los nombres del cliente.
     */
    private JTextField txtNombres;

    /**
     * Campo para capturar el apellido paterno del cliente.
     */
    private JTextField txtApellidoP;

    /**
     * Campo para capturar el apellido materno del cliente.
     */
    private JTextField txtApellidoM;

    /**
     * Campo para capturar el día de la fecha de nacimiento.
     */
    private JTextField txtDia;

    /**
     * Campo para capturar el mes de la fecha de nacimiento.
     */
    private JTextField txtMes;

    /**
     * Campo para capturar el año de la fecha de nacimiento.
     */
    private JTextField txtAnio;

    /**
     * Campo para capturar la calle de la dirección del cliente.
     */
    private JTextField txtCalle;

    /**
     * Campo para capturar el número de la dirección del cliente.
     */
    private JTextField txtNumero;

    /**
     * Campo para capturar la colonia de la dirección del cliente.
     */
    private JTextField txtColonia;

    /**
     * Campo para capturar el código postal de la dirección del cliente.
     */
    private JTextField txtCodigoPostal;

    /**
     * Campo para capturar el número de teléfono del cliente.
     */
    private JTextField txtTelefono;

    /**
     * Campo para capturar la etiqueta del teléfono (por ejemplo: Casa,
     * Trabajo).
     */
    private JTextField txtEtiqueta;

    /**
     * Botón principal para ejecutar el registro del cliente.
     */
    private JButton btnCrear;

    /**
     * Contexto global de la aplicación; permite acceder a BOs y estado de
     * sesión.
     */
    private final AppContext ctx;

    /**
     * <p>
     * Constructor de la pantalla de creación de cuenta.
     * </p>
     *
     * <p>
     * Construye toda la interfaz:
     * </p>
     * <ul>
     * <li>Fondo beige y tarjeta blanca central</li>
     * <li>Top con flecha de regreso</li>
     * <li>Títulos</li>
     * <li>Formulario en dos columnas con campos de acceso, personales,
     * dirección y teléfono</li>
     * <li>Botón "Crear" y footer en la parte inferior</li>
     * <li>Inicialización de placeholders</li>
     * <li>Asociación de acciones a los botones</li>
     * </ul>
     *
     * @param ctx contexto global de la aplicación
     */
    public PantallaCrearCuenta(AppContext ctx) {
        this.ctx = ctx;

        setTitle("Panadería - Crear cuenta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 720);
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
        panelPrincipal.setPreferredSize(new Dimension(760, 640));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        base.add(panelPrincipal, gbc);

        // ====== TOP (flecha) ======
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        /**
         * Botón de regreso hacia {@link PantallaInicioSesionCliente}.
         */
        JButton btnBack = new JButton("←");
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
        /**
         * Botón principal para iniciar el flujo de registro del cliente.
         */
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
        /**
         * Acción de regresar a {@link PantallaInicioSesionCliente}.
         */
        btnBack.addActionListener(e -> {
            new PantallaInicioSesionCliente(ctx).setVisible(true);
            dispose();
        });

        /**
         * Acción principal: construye entidades a partir del formulario y
         * registra el cliente.
         */
        btnCrear.addActionListener(e -> {
            Cliente cliente = new Cliente();
            cliente.setUsuario(txtUsuario.getText());

            char[] passArray = txtContrasena.getPassword();
            String password = new String(passArray);
            cliente.setContrasenia(password);

            cliente.setNombres(txtNombres.getText());
            cliente.setApellidoPaterno(txtApellidoP.getText());

            String apellidoM = txtApellidoM.getText().trim();

            if (!apellidoM.isEmpty() && !apellidoM.equals("Apellido materno")) {
                cliente.setApellidoMaterno(apellidoM);
            } else {
                cliente.setApellidoMaterno(null);
            }

            Telefono telefono = new Telefono();
            telefono.setTelefono(txtTelefono.getText());
            telefono.setEtiqueta(txtEtiqueta.getText());

            cliente.setRol(RolUsuario.Cliente);

            LocalDate fecha = LocalDate.of(Integer.parseInt(txtAnio.getText()), Integer.parseInt(txtMes.getText()), Integer.parseInt(txtDia.getText()));
            cliente.setFechaNacimiento(fecha);

            String calle = txtCalle.getText();
            String colonia = txtColonia.getText();
            int cp = Integer.parseInt(txtCodigoPostal.getText());
            int numero = Integer.parseInt(txtNumero.getText());

            Direccion direccion = new Direccion();
            direccion.setCalle(calle);
            direccion.setColonia(colonia);
            direccion.setCp(cp);
            direccion.setNumero(numero);
            direccion.setCliente(cliente);

            cliente.setDireccion(direccion);

            try {

                Cliente clienteActual = ctx.getClienteBO().registrarCliente(cliente, telefono);
                ctx.setClienteActual(clienteActual);

                JOptionPane.showMessageDialog(this, "Cuenta creada");

                new PantallaSesionIniciadaCliente(ctx).setVisible(true);
                dispose();

            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
    }

    // ========== Estilos ==========
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
     * <li>Dimensión preferida</li>
     * <li>Fuente</li>
     * <li>Borde compuesto (línea + padding)</li>
     * <li>Fondo blanco</li>
     * </ul>
     *
     * @param t campo a configurar
     */
    private void configurarCampo(JTextField t) {
        t.setPreferredSize(new Dimension(280, 40));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(7, 10, 7, 10)
        ));
        t.setBackground(Color.WHITE);
    }

    /**
     * <p>
     * Configura el estilo visual para campos pequeños (por ejemplo, fecha
     * D/M/A).
     * </p>
     *
     * <p>
     * Mantiene el mismo estilo que {@link #configurarCampo(JTextField)} pero
     * con una dimensión menor.
     * </p>
     *
     * @param t campo a configurar
     */
    private void configurarCampoPequeno(JTextField t) {
        t.setPreferredSize(new Dimension(86, 40));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(7, 10, 7, 10)
        ));
        t.setBackground(Color.WHITE);
    }

    /**
     * <p>
     * Crea un botón con el estilo visual utilizado para la acción principal de
     * registro.
     * </p>
     *
     * @param text texto del botón
     * @return botón configurado con estilo
     */
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
