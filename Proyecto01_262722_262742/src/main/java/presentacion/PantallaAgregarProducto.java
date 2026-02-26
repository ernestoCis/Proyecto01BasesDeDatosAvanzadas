/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dominio.EstadoProducto;
import dominio.Producto;
import dominio.TipoProducto;
import imagenes.ImagenProductoUtil;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;
import negocio.Excepciones.NegocioException;

/**
 * <p>
 * Pantalla gráfica utilizada por el empleado para <b>registrar un nuevo
 * producto</b> en el sistema.
 * </p>
 *
 * <p>
 * Permite capturar la información base del producto:
 * </p>
 *
 * <ul>
 * <li><b>Nombre</b></li>
 * <li><b>Precio</b></li>
 * <li><b>Descripción</b></li>
 * <li><b>Tipo</b> ({@link TipoProducto})</li>
 * <li><b>Estado</b> ({@link EstadoProducto})</li>
 * </ul>
 *
 * <p>
 * Adicionalmente, permite seleccionar una imagen en formato <b>.png</b> y
 * guardarla mediante {@link ImagenProductoUtil} una vez que el producto fue
 * insertado correctamente.
 * </p>
 *
 * <h2>Flujo general</h2>
 * <ol>
 * <li>El usuario llena el formulario.</li>
 * <li>Se valida la información.</li>
 * <li>Se construye un objeto {@link Producto}.</li>
 * <li>Si se seleccionó imagen, se intenta guardar y se notifica si falla.</li>
 * <li>Se regresa a la pantalla padre y se refresca la gestión de
 * productos.</li>
 * </ol>
 *
 * @author 262722, 2627242
 */
public class PantallaAgregarProducto extends JFrame {

    /**
     * Contexto global de la aplicación, utilizado para acceder a BOs.
     */
    private final AppContext ctx;

    /**
     * Pantalla padre desde la cual se abrió esta ventana. Se utiliza para
     * volver al cancelar o finalizar el guardado.
     */
    private final PantallaGestionarProductos pantallaPadre;

    /**
     * Archivo de imagen seleccionado por el usuario (si aplica).
     */
    private File imagenSeleccionada;

    /**
     * Campos del formulario.
     */
    private JTextField txtNombre;
    /**
     * texto
     */
    private JTextField txtPrecio;
    /**
     * texto
     */
    private JTextField txtDescripcion;

    /**
     * Combobox para seleccionar tipo de producto.
     */
    private JComboBox<TipoProducto> cbTipo;

    /**
     * Combobox para seleccionar estado del producto.
     */
    private JComboBox<EstadoProducto> cbEstado;

    /**
     * <p>
     * Constructor de la pantalla de alta de producto.
     * </p>
     *
     * <p>
     * Construye la UI (título, formulario, botones), configura estilos, y
     * asigna eventos para:
     * </p>
     *
     * <ul>
     * <li>Volver (flecha/cancelar)</li>
     * <li>Seleccionar imagen (JFileChooser)</li>
     * <li>Guardar producto y, opcionalmente, su imagen</li>
     * </ul>
     *
     * @param ctx contexto global de la aplicación
     * @param pantallaPadre pantalla de gestión que abrió esta ventana
     */
    public PantallaAgregarProducto(AppContext ctx, PantallaGestionarProductos pantallaPadre) {
        this.ctx = ctx;
        this.pantallaPadre = pantallaPadre;

        setTitle("Panadería - Agregar producto");
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

        // ======================
        // TOP (back)
        // ======================
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        /**
         * Botón de regreso (flecha) para volver a la pantalla padre.
         */
        JButton btnBack = new JButton("←");
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelSuperior.add(btnBack, BorderLayout.WEST);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // ======================
        // CENTRO (títulos + formulario)
        // ======================
        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Panadería");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 58));

        JLabel subtitulo = new JLabel("Agregar producto");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(titulo);
        panelCentro.add(Box.createVerticalStrut(8));
        panelCentro.add(subtitulo);
        panelCentro.add(Box.createVerticalStrut(18));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new GridBagLayout());
        form.setMaximumSize(new Dimension(680, 280));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField();
        configurarCampo(txtNombre);
        aplicarPlaceholder(txtNombre, "Nombre");

        txtPrecio = new JTextField();
        configurarCampo(txtPrecio);
        aplicarPlaceholder(txtPrecio, "Precio");

        txtDescripcion = new JTextField();
        configurarCampo(txtDescripcion);
        aplicarPlaceholder(txtDescripcion, "Descripcion");

        cbTipo = new JComboBox<>(TipoProducto.values());
        configurarCombo(cbTipo);

        cbEstado = new JComboBox<>(EstadoProducto.values());
        configurarCombo(cbEstado);
        cbEstado.setSelectedItem(EstadoProducto.Disponible);

        // Fila 1: Nombre | Tipo
        g.gridx = 0;
        g.gridy = 0;
        g.weightx = 1;
        form.add(txtNombre, g);
        g.gridx = 1;
        g.gridy = 0;
        g.weightx = 1;
        form.add(cbTipo, g);

        // Fila 2: Precio | Estado
        g.gridx = 0;
        g.gridy = 1;
        form.add(txtPrecio, g);
        g.gridx = 1;
        g.gridy = 1;
        form.add(cbEstado, g);

        // Fila 3: Descripcion (2 columnas)
        g.gridx = 0;
        g.gridy = 2;
        g.gridwidth = 2;
        form.add(txtDescripcion, g);

        panelCentro.add(form);
        panelCentro.add(Box.createVerticalStrut(18));

        // ======================
        // Botones
        // ======================
        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelBtns.setOpaque(false);

        JButton btnCancelar = crearBotonMediano("Cancelar");
        JButton btnImagen = crearBotonMediano("Seleccionar imagen");
        JButton btnGuardar = crearBotonMediano("Guardar");

        panelBtns.add(btnCancelar);
        panelBtns.add(btnImagen);
        panelBtns.add(btnGuardar);

        panelCentro.add(panelBtns);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // ======================
        // Footer
        // ======================
        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(footer, BorderLayout.WEST);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // ======================
        // Acciones
        // ======================
        btnBack.addActionListener(e -> volver());
        btnCancelar.addActionListener(e -> volver());

        /**
         * Guarda el producto:
         * <ul>
         * <li>Valida y construye el objeto Producto</li>
         * <li>Inserta en BD por medio de BO</li>
         * <li>Si hay imagen seleccionada, intenta guardarla</li>
         * <li>Refresca la pantalla de gestión</li>
         * </ul>
         */
        btnGuardar.addActionListener(e -> {
            try {
                Producto p = construirProductoDesdeFormulario();
                Producto guardado = ctx.getProductoBO().insertarProducto(p);

                if (imagenSeleccionada != null) {
                    try {
                        ImagenProductoUtil.guardarImagenProducto(guardado.getId(), imagenSeleccionada);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this,
                                "El producto se guardó, pero no se pudo guardar la imagen.",
                                "Aviso",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
                JOptionPane.showMessageDialog(this, "Producto guardado correctamente.");

                pantallaPadre.setVisible(true);
                pantallaPadre.dispose();
                new PantallaGestionarProductos(ctx).setVisible(true);
                dispose();

            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        /**
         * Permite seleccionar una imagen desde el sistema de archivos. Se
         * valida que sea un archivo con extensión .png.
         */
        btnImagen.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Selecciona una imagen PNG");

            int r = chooser.showOpenDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();

                if (!f.getName().toLowerCase().endsWith(".png")) {
                    JOptionPane.showMessageDialog(this,
                            "Solo se permiten imágenes .png",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                imagenSeleccionada = f;
                JOptionPane.showMessageDialog(this, "Imagen seleccionada: " + f.getName());
            }
        });

    }

    /**
     * Regresa a la pantalla padre y cierra esta ventana.
     */
    private void volver() {
        pantallaPadre.setVisible(true);
        dispose();
    }

    /**
     * <p>
     * Construye un objeto {@link Producto} con la información capturada en el
     * formulario.
     * </p>
     *
     * <p>
     * Realiza validaciones:
     * </p>
     * <ul>
     * <li>Nombre obligatorio</li>
     * <li>Precio obligatorio y numérico</li>
     * <li>Precio mayor a 0</li>
     * <li>Descripción obligatoria</li>
     * </ul>
     *
     * @return producto construido con nombre, precio, descripción, tipo y
     * estado
     * @throws NegocioException si falla alguna validación de negocio
     */
    private Producto construirProductoDesdeFormulario() throws NegocioException {
        String nombre = normalizarPlaceholder(txtNombre, "Nombre");
        String precioTxt = normalizarPlaceholder(txtPrecio, "Precio");
        String desc = normalizarPlaceholder(txtDescripcion, "Descripcion");

        if (nombre.isEmpty()) {
            throw new NegocioException("El nombre es obligatorio.");
        }
        if (precioTxt.isEmpty()) {
            throw new NegocioException("El precio es obligatorio.");
        }
        if (desc.isEmpty()) {
            throw new NegocioException("La descripción es obligatoria.");
        }

        float precio;
        try {
            precio = Float.parseFloat(precioTxt);
        } catch (NumberFormatException e) {
            throw new NegocioException("El precio debe ser numérico.");
        }
        if (precio <= 0) {
            throw new NegocioException("El precio debe ser mayor a 0.");
        }

        TipoProducto tipo = (TipoProducto) cbTipo.getSelectedItem();
        EstadoProducto estado = (EstadoProducto) cbEstado.getSelectedItem();

        Producto p = new Producto();
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setDescripcion(desc);
        p.setTipo(tipo);
        p.setEstado(estado);

        return p;
    }

    /**
     * Normaliza el texto de un campo que utiliza placeholder.
     * <p>
     * Si el texto actual del campo coincide con el placeholder, se considera
     * vacío.
     * </p>
     *
     * @param campo campo de texto
     * @param ph placeholder a comparar
     * @return texto normalizado (vacío si coincide con placeholder)
     */
    private String normalizarPlaceholder(JTextField campo, String ph) {
        String t = campo.getText() == null ? "" : campo.getText().trim();
        if (t.equalsIgnoreCase(ph)) {
            return "";
        }
        return t;
    }

    /**
     * Configura el estilo visual estándar de un campo de texto.
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
     * Configura el estilo visual estándar de un {@link JComboBox}.
     *
     * @param cb combo a configurar
     */
    private void configurarCombo(JComboBox<?> cb) {
        cb.setPreferredSize(new Dimension(280, 40));
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        cb.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(2, 10, 2, 10)
        ));
    }

    /**
     * Crea un botón mediano con el estilo visual estándar de la aplicación.
     *
     * @param text texto del botón
     * @return botón configurado
     */
    private JButton crearBotonMediano(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(140, 34));
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
     * Aplica comportamiento de placeholder (texto guía) a un
     * {@link JTextField}.
     * </p>
     *
     * <ul>
     * <li>Al ganar foco: si el texto es igual al placeholder, se limpia.</li>
     * <li>Al perder foco: si quedó vacío, se restaura el placeholder.</li>
     * </ul>
     *
     * @param campo campo a configurar
     * @param texto texto placeholder
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
}
