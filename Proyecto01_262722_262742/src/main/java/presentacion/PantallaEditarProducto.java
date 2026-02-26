/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dominio.EstadoProducto;
import dominio.Producto;
import dominio.TipoProducto;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import negocio.Excepciones.NegocioException;

/**
 * <h1>PantallaEditarProducto</h1>
 *
 * <p>
 * Pantalla administrativa para <b>editar</b> la información de un
 * {@link Producto} existente.
 * </p>
 *
 * <p>
 * La UI presenta:
 * </p>
 * <ul>
 * <li>Botón de regreso hacia la pantalla padre
 * {@link PantallaGestionarProductos}.</li>
 * <li>Formulario con campos del producto: nombre, precio, descripción, tipo y
 * estado.</li>
 * <li>Botones de acción: <b>Cancelar</b> y <b>Guardar</b>.</li>
 * <li>Footer informativo.</li>
 * </ul>
 *
 * <h2>Carga del producto</h2>
 * <p>
 * Al inicializar la pantalla se invoca {@link #cargarProducto()} usando el
 * {@code idProducto} recibido por parámetro. Si ocurre un error al consultar,
 * se notifica con un {@link JOptionPane} y se regresa a la pantalla padre.
 * </p>
 *
 * <h2>Guardado</h2>
 * <p>
 * Al presionar "Guardar", se actualiza {@link #productoActual} con los valores
 * del formulario mediante {@link #actualizarProductoDesdeFormulario()}, se
 * valida la información y finalmente se actualiza el producto en la capa de
 * negocio con {@code ctx.getProductoBO().actualizarProducto(...)}.
 * </p>
 *
 * @author 262722, 2627242
 */
public class PantallaEditarProducto extends JFrame {

    /**
     * Contexto global de la aplicación; permite acceder a BOs y estado de
     * sesión.
     */
    private final AppContext ctx;

    /**
     * Referencia a la pantalla que abrió esta ventana; se utiliza para regresar
     * al cancelar o volver.
     */
    private final PantallaGestionarProductos pantallaPadre;

    /**
     * Identificador del producto a editar.
     */
    private final int idProducto;

    /**
     * Campo para editar el nombre del producto.
     */
    private JTextField txtNombre;

    /**
     * Campo para editar el precio del producto.
     */
    private JTextField txtPrecio;

    /**
     * Campo para editar la descripción del producto.
     */
    private JTextField txtDescripcion;

    /**
     * ComboBox para seleccionar el tipo del producto.
     */
    private JComboBox<TipoProducto> cbTipo;

    /**
     * ComboBox para seleccionar el estado del producto.
     */
    private JComboBox<EstadoProducto> cbEstado;

    /**
     * Producto actualmente cargado desde la capa de negocio y que será
     * modificado/actualizado.
     */
    private Producto productoActual;

    /**
     * <p>
     * Constructor de la pantalla para editar un producto.
     * </p>
     *
     * <p>
     * Construye la interfaz con:
     * </p>
     * <ul>
     * <li>Fondo beige y tarjeta blanca central</li>
     * <li>Top con flecha para volver</li>
     * <li>Títulos y formulario en dos columnas</li>
     * <li>Botones "Cancelar" y "Guardar"</li>
     * <li>Footer informativo</li>
     * </ul>
     *
     * <p>
     * Finalmente invoca {@link #cargarProducto()} para consultar y mostrar el
     * producto real.
     * </p>
     *
     * @param ctx contexto global de la aplicación
     * @param pantallaPadre pantalla anterior desde la que se invocó esta
     * ventana
     * @param idProducto id del producto a editar
     */
    public PantallaEditarProducto(AppContext ctx, PantallaGestionarProductos pantallaPadre, int idProducto) {
        this.ctx = ctx;
        this.pantallaPadre = pantallaPadre;
        this.idProducto = idProducto;

        setTitle("Panadería - Editar producto");
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

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        /**
         * Botón para volver a la pantalla padre.
         */
        JButton btnBack = new JButton("←");
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelSuperior.add(btnBack, BorderLayout.WEST);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Panadería");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 58));

        JLabel subtitulo = new JLabel("Editar producto");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(titulo);
        panelCentro.add(Box.createVerticalStrut(8));
        panelCentro.add(subtitulo);
        panelCentro.add(Box.createVerticalStrut(18));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setLayout(new GridBagLayout());
        form.setMaximumSize(new Dimension(680, 280));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField();
        configurarCampo(txtNombre);

        txtPrecio = new JTextField();
        configurarCampo(txtPrecio);

        txtDescripcion = new JTextField();
        configurarCampo(txtDescripcion);

        cbTipo = new JComboBox<>(TipoProducto.values());
        configurarCombo(cbTipo);

        cbEstado = new JComboBox<>(EstadoProducto.values());
        configurarCombo(cbEstado);

        // Fila 1
        g.gridx = 0;
        g.gridy = 0;
        g.weightx = 1;
        form.add(txtNombre, g);
        g.gridx = 1;
        g.gridy = 0;
        form.add(cbTipo, g);

        // Fila 2
        g.gridx = 0;
        g.gridy = 1;
        form.add(txtPrecio, g);
        g.gridx = 1;
        g.gridy = 1;
        form.add(cbEstado, g);

        // Fila 3
        g.gridx = 0;
        g.gridy = 2;
        g.gridwidth = 2;
        form.add(txtDescripcion, g);

        panelCentro.add(form);
        panelCentro.add(Box.createVerticalStrut(18));

        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelBtns.setOpaque(false);

        /**
         * Botón para cancelar la edición y volver.
         */
        JButton btnCancelar = crearBotonMediano("Cancelar");

        /**
         * Botón para guardar cambios del producto.
         */
        JButton btnGuardar = crearBotonMediano("Guardar");

        panelBtns.add(btnCancelar);
        panelBtns.add(btnGuardar);

        panelCentro.add(panelBtns);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(footer, BorderLayout.WEST);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        /**
         * Vuelve a la pantalla padre al presionar la flecha.
         */
        btnBack.addActionListener(e -> volver());

        /**
         * Cancela la operación y vuelve a la pantalla padre.
         */
        btnCancelar.addActionListener(e -> volver());

        /**
         * Guarda cambios del producto. Primero valida y actualiza el objeto en
         * memoria y después persiste los cambios mediante la capa de negocio.
         */
        btnGuardar.addActionListener(e -> {
            try {
                actualizarProductoDesdeFormulario();
                ctx.getProductoBO().actualizarProducto(productoActual);

                JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.");

                pantallaPadre.setVisible(true);
                pantallaPadre.dispose();
                new PantallaGestionarProductos(ctx).setVisible(true);
                dispose();

            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ✅ cargar producto real
        cargarProducto();
    }

    /**
     * <p>
     * Consulta el producto real desde la capa de negocio y carga sus valores en
     * el formulario.
     * </p>
     *
     * <p>
     * Si la consulta falla, se muestra un mensaje de error y se regresa a la
     * pantalla padre mediante {@link #volver()}.
     * </p>
     */
    private void cargarProducto() {
        try {
            Producto temp = new Producto();
            temp.setId(idProducto);
            productoActual = ctx.getProductoBO().consultarProducto(temp);

            txtNombre.setText(productoActual.getNombre());
            txtPrecio.setText(String.valueOf(productoActual.getPrecio()));
            txtDescripcion.setText(productoActual.getDescripcion());

            cbTipo.setSelectedItem(productoActual.getTipo());
            cbEstado.setSelectedItem(productoActual.getEstado());

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar el producto.\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            volver();
        }
    }

    /**
     * <p>
     * Valida y aplica los valores del formulario a {@link #productoActual}.
     * </p>
     *
     * <p>
     * Reglas de validación:
     * </p>
     * <ul>
     * <li>Nombre, precio y descripción no pueden estar vacíos</li>
     * <li>Precio debe ser numérico y mayor a 0</li>
     * </ul>
     *
     * @throws NegocioException si alguna validación falla
     */
    private void actualizarProductoDesdeFormulario() throws NegocioException {
        String nombre = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
        String precioTxt = txtPrecio.getText() == null ? "" : txtPrecio.getText().trim();
        String desc = txtDescripcion.getText() == null ? "" : txtDescripcion.getText().trim();

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

        productoActual.setNombre(nombre);
        productoActual.setPrecio(precio);
        productoActual.setDescripcion(desc);
        productoActual.setTipo((TipoProducto) cbTipo.getSelectedItem());
        productoActual.setEstado((EstadoProducto) cbEstado.getSelectedItem());
    }

    /**
     * <p>
     * Regresa a la pantalla padre y cierra la ventana actual.
     * </p>
     */
    private void volver() {
        pantallaPadre.setVisible(true);
        dispose();
    }

    /**
     * <p>
     * Configura el estilo visual estándar de un {@link JTextField} del
     * formulario.
     * </p>
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
     * Configura el estilo visual estándar de un {@link JComboBox} del
     * formulario.
     * </p>
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
     * <p>
     * Crea un botón mediano con el estilo visual utilizado en pantallas
     * administrativas.
     * </p>
     *
     * @param text texto del botón
     * @return botón configurado con estilo
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
}
