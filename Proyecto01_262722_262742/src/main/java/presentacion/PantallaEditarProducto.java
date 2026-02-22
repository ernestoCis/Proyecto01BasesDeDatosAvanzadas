/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author Isaac
 */
import dominio.EstadoProducto;
import dominio.Producto;
import dominio.TipoProducto;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import negocio.Excepciones.NegocioException;

public class PantallaEditarProducto extends JFrame {

    private final AppContext ctx;
    private final PantallaGestionarProductos pantallaPadre;
    private final int idProducto;

    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtDescripcion;
    private JComboBox<TipoProducto> cbTipo;
    private JComboBox<EstadoProducto> cbEstado;

    private Producto productoActual;

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
        g.gridx = 0; g.gridy = 0; g.weightx = 1;
        form.add(txtNombre, g);
        g.gridx = 1; g.gridy = 0;
        form.add(cbTipo, g);

        // Fila 2
        g.gridx = 0; g.gridy = 1;
        form.add(txtPrecio, g);
        g.gridx = 1; g.gridy = 1;
        form.add(cbEstado, g);

        // Fila 3
        g.gridx = 0; g.gridy = 2; g.gridwidth = 2;
        form.add(txtDescripcion, g);

        panelCentro.add(form);
        panelCentro.add(Box.createVerticalStrut(18));

        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelBtns.setOpaque(false);

        JButton btnCancelar = crearBotonMediano("Cancelar");
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

        btnBack.addActionListener(e -> volver());
        btnCancelar.addActionListener(e -> volver());

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

    private void actualizarProductoDesdeFormulario() throws NegocioException {
        String nombre = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
        String precioTxt = txtPrecio.getText() == null ? "" : txtPrecio.getText().trim();
        String desc = txtDescripcion.getText() == null ? "" : txtDescripcion.getText().trim();

        if (nombre.isEmpty()) throw new NegocioException("El nombre es obligatorio.");
        if (precioTxt.isEmpty()) throw new NegocioException("El precio es obligatorio.");
        if (desc.isEmpty()) throw new NegocioException("La descripción es obligatoria.");

        float precio;
        try {
            precio = Float.parseFloat(precioTxt);
        } catch (NumberFormatException e) {
            throw new NegocioException("El precio debe ser numérico.");
        }
        if (precio <= 0) throw new NegocioException("El precio debe ser mayor a 0.");

        productoActual.setNombre(nombre);
        productoActual.setPrecio(precio);
        productoActual.setDescripcion(desc);
        productoActual.setTipo((TipoProducto) cbTipo.getSelectedItem());
        productoActual.setEstado((EstadoProducto) cbEstado.getSelectedItem());
    }

    private void volver() {
        pantallaPadre.setVisible(true);
        dispose();
    }

    private void configurarCampo(JTextField t) {
        t.setPreferredSize(new Dimension(280, 40));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(7, 10, 7, 10)
        ));
        t.setBackground(Color.WHITE);
    }

    private void configurarCombo(JComboBox<?> cb) {
        cb.setPreferredSize(new Dimension(280, 40));
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(Color.WHITE);
        cb.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(2, 10, 2, 10)
        ));
    }

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
  