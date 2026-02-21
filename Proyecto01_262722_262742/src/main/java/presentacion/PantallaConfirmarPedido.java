package presentacion;

import dominio.Cliente;
import dominio.Cupon;
import dominio.EstadoPedido;
import dominio.ItemCarrito;
import dominio.MetodoPago;
import dominio.PedidoProgramado;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import negocio.BOs.iCuponBO;
import negocio.BOs.iPedidoBO;
import negocio.BOs.iProductoBO;
import negocio.BOs.iUsuarioBO;
import negocio.Excepciones.NegocioException;

public class PantallaConfirmarPedido extends JFrame {

    private final JFrame pantallaAnterior;
    private final List<ItemCarrito> carrito;

    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtCupon;
    private JLabel lblCuponValido;
    private JLabel lblCuponInvalido;

    private JLabel lblSubtotal;
    private JLabel lblDescuento;
    private JLabel lblTotal;
    
    private final iProductoBO productoBO;
    private final iPedidoBO pedidoBO;
    private final iCuponBO cuponBO;
    private final Cliente cliente;

    private float descuentoActual = 0;
    private float subtotal = 0;
    private MetodoPago metodoPago = MetodoPago.Efectivo; //efectivo como default

    public PantallaConfirmarPedido(JFrame pantallaAnterior, List<ItemCarrito> carrito, iProductoBO productoBO, iCuponBO cuponBO, iPedidoBO pedidoBO, Cliente cliente) {
        this.pantallaAnterior = pantallaAnterior;
        this.carrito = carrito;
        
        this.productoBO = productoBO;
        this.pedidoBO = pedidoBO;
        this.cuponBO = cuponBO;
        this.cliente = cliente;
        

        setTitle("Panadería - Confirmar pedido");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 650);
        setLocationRelativeTo(null);

        // Fondo beige
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(new Color(214, 186, 150));
        setContentPane(root);

        // Tarjeta blanca con borde
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

        JButton btnFlecha = new JButton("↩");
        btnFlecha.setFocusPainted(false);
        btnFlecha.setBorderPainted(false);
        btnFlecha.setContentAreaFilled(false);
        btnFlecha.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        btnFlecha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFlecha.addActionListener(e -> {
            if (pantallaAnterior != null) pantallaAnterior.setVisible(true);
            dispose();
        });

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelIzq.setOpaque(false);
        panelIzq.add(btnFlecha);

        topBar.add(panelIzq, BorderLayout.WEST);

        // ----- titulo -----
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Panadería");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 56));

        JLabel subtitle = new JLabel("Confirmar pedido");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 22));

        header.add(Box.createVerticalStrut(2));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(12));

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(topBar, BorderLayout.NORTH);
        north.add(header, BorderLayout.CENTER);

        card.add(north, BorderLayout.NORTH);

        // ----- tabla -----
        String[] cols = {"Producto", "Precio pz.", "Cantidad", "Subtotal", "Notas"};

        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                // solo la columna Notas editable
                return col == 4;
            }
        };
        
        tabla = new JTable(modelo);
        tabla.setRowHeight(34);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setReorderingAllowed(false);

        // centrado de columnas numéricas
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(1).setCellRenderer(center);
        tabla.getColumnModel().getColumn(2).setCellRenderer(center);
        tabla.getColumnModel().getColumn(3).setCellRenderer(center);

        // notas a la izquierda
        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(SwingConstants.LEFT);
        tabla.getColumnModel().getColumn(4).setCellRenderer(left);

        // para editar con 1 click
        DefaultCellEditor notasEditor = new DefaultCellEditor(new JTextField());
        notasEditor.setClickCountToStart(1);
        tabla.getColumnModel().getColumn(4).setCellEditor(notasEditor);

        tabla.getColumnModel().getColumn(0).setPreferredWidth(220); // Producto
        tabla.getColumnModel().getColumn(1).setPreferredWidth(90);  // Precio
        tabla.getColumnModel().getColumn(2).setPreferredWidth(70);  // Cantidad
        tabla.getColumnModel().getColumn(3).setPreferredWidth(90);  // Subtotal
        tabla.getColumnModel().getColumn(4).setPreferredWidth(320); // Notas
        
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        scroll.getViewport().setBackground(Color.WHITE);

        // cargar filas desde carrito
        cargarTablaDesdeCarrito();

        // ----- cupon -----
        JPanel panelCupon = new JPanel(new GridBagLayout());
        panelCupon.setOpaque(false);
        panelCupon.setBorder(new EmptyBorder(10, 0, 0, 0));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.gridy = 0;

        JLabel lblCupon = new JLabel("Cupon (opcional):");
        lblCupon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        c.gridx = 0;
        c.weightx = 0;
        c.anchor = GridBagConstraints.WEST;
        panelCupon.add(lblCupon, c);

        txtCupon = new JTextField(14);
        txtCupon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCupon.setBorder(new CompoundBorder(
                new LineBorder(new Color(120, 120, 120), 1),
                new EmptyBorder(4, 8, 4, 8)
        ));
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panelCupon.add(txtCupon, c);

        JButton btnValidar = new JButton("Validar");
        btnValidar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnValidar.setFocusPainted(false);
        btnValidar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnValidar.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        btnValidar.setBackground(new Color(245, 245, 245));
        c.gridx = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        panelCupon.add(btnValidar, c);

        lblCuponValido = new JLabel("[Cupon valido]");
        lblCuponValido.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCuponValido.setForeground(new Color(40, 130, 40));
        lblCuponValido.setVisible(false);

        lblCuponInvalido = new JLabel("[Cupon invalido]");
        lblCuponInvalido.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCuponInvalido.setForeground(new Color(160, 40, 40));
        lblCuponInvalido.setVisible(false);

        c.gridx = 3;
        panelCupon.add(lblCuponValido, c);
        c.gridx = 4;
        panelCupon.add(lblCuponInvalido, c);

        btnValidar.addActionListener(e -> validarCupon());

        // ----- panel de resumen -----
        JPanel panelResumen = new JPanel(new GridLayout(1, 3, 0, 0));
        panelResumen.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        panelResumen.setBackground(Color.WHITE);

        lblSubtotal = crearCeldaResumen("Subtotal: $" + String.format("%.2f", calcularSubtotal()));
        lblDescuento = crearCeldaResumen("Descuento: $0.00");
        lblTotal = crearCeldaResumen("Total a pagar: $" + String.format("%.2f", calcularSubtotal()));

        panelResumen.add(wrapResumen(lblSubtotal));
        panelResumen.add(wrapResumen(lblDescuento));
        panelResumen.add(wrapResumen(lblTotal));

        // ----- scroll -----
        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBorder(new EmptyBorder(0, 40, 0, 40));

        // ----- metodo de pago -----
        JPanel panelMetodoPago = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelMetodoPago.setOpaque(false);
        panelMetodoPago.setBorder(new EmptyBorder(8, 0, 8, 0));

        JLabel lblMetodo = new JLabel("Metodo de pago:");
        lblMetodo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JComboBox<MetodoPago> comboMetodoPago
                = new JComboBox<>(MetodoPago.values());

        comboMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboMetodoPago.setSelectedItem(MetodoPago.Efectivo); // default

        comboMetodoPago.addActionListener(e -> {
            metodoPago = (MetodoPago) comboMetodoPago.getSelectedItem();
        });

        panelMetodoPago.add(lblMetodo);
        panelMetodoPago.add(Box.createHorizontalStrut(10));
        panelMetodoPago.add(comboMetodoPago);
        
        centro.add(scroll);
        centro.add(panelCupon);
        centro.add(panelMetodoPago);
        centro.add(Box.createVerticalStrut(6));
        centro.add(panelResumen);

        card.add(centro, BorderLayout.CENTER);

        // ----- footer y boton de confirmacion -----
        JButton btnRealizar = new JButton("Realizar pedido");
        btnRealizar.setPreferredSize(new Dimension(200, 38));
        btnRealizar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRealizar.setFocusPainted(false);
        btnRealizar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRealizar.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        btnRealizar.setBackground(new Color(245, 245, 245));

        btnRealizar.addActionListener(e -> {
            
            try{
                //crear el pedido para mandarlo a la ultima pantalla
                PedidoProgramado pedidoProgramado = new PedidoProgramado();
                pedidoProgramado.setEstado(EstadoPedido.Pendiente);
                pedidoProgramado.setFechaCreacion(LocalDateTime.now());
                // fecha de entrega vacia por ahora (se cambia hasta que el estado pasa a Entregado)
                pedidoProgramado.setMetodoPago(metodoPago);
                pedidoProgramado.setTotal(subtotal - descuentoActual);
                pedidoProgramado.setNumeroPedido(pedidoBO.generarNumeroDePedido());
                pedidoProgramado.setCliente(cliente);
                
                if(!txtCupon.getText().trim().isEmpty()){
                    pedidoProgramado.setCupon(cuponBO.consultarCupon(txtCupon.getText()));
                }

                if(pedidoBO.agregarPedidoProgramado(pedidoProgramado) != null){
                    PantallaPedidoProgramadoRealizado pantallaPedidoRealizado = new PantallaPedidoProgramadoRealizado(usuarioBO, pedidoProgramado, productoBO, cuponBO, pedidoBO);
                    pantallaPedidoRealizado.setVisible(true);
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(this, "Error al agregar el pedido.");
                }
                
                
            }catch(NegocioException ex){
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
            
        });

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        panelBtn.setOpaque(false);
        panelBtn.setBorder(new EmptyBorder(0, 40, 0, 40));
        panelBtn.add(btnRealizar);

        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(0, 40, 0, 40));
        footerPanel.add(footer, BorderLayout.WEST);

        JPanel south = new JPanel();
        south.setOpaque(false);
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.add(panelBtn);
        south.add(footerPanel);

        card.add(south, BorderLayout.SOUTH);
    }

    private void cargarTablaDesdeCarrito() {
        modelo.setRowCount(0);

        for (ItemCarrito it : carrito) {
            String nombre = it.getProducto().getNombre();
            float precio = it.getProducto().getPrecio();
            int cantidad = it.getCantidad();
            float subtotal = precio * cantidad;

            String nota = "";

            modelo.addRow(new Object[]{
                nombre,
                "$" + precio,
                cantidad,
                "$" + subtotal,
                nota
            });
        }
    }

    private float calcularSubtotal() {
        float total = 0;
        for (ItemCarrito it : carrito) {
            total += it.getProducto().getPrecio() * it.getCantidad();
        }
        subtotal = total;
        
        return subtotal;
    }

    private void recalcularResumen() {
        float total = Math.max(0, subtotal - descuentoActual);

        lblSubtotal.setText("Subtotal: $" + String.format("%.2f", subtotal));
        lblDescuento.setText("Descuento: $" + String.format("%.2f", descuentoActual));
        lblTotal.setText("Total a pagar: $" + String.format("%.2f", total));
    }

    private void validarCupon() {
        String codigo = txtCupon.getText().trim();

        // reset
        lblCuponValido.setVisible(false);
        lblCuponInvalido.setVisible(false);
        descuentoActual = 0;

        if (codigo.isEmpty()) {
            lblCuponInvalido.setVisible(true);
            recalcularResumen();
            return;
        }
        
        try {
            if(cuponBO.validarCupon(codigo, subtotal).esValido()){
                lblCuponValido.setVisible(true);
                descuentoActual = cuponBO.validarCupon(codigo, subtotal).getDescuento();
            }else{
                lblCuponInvalido.setVisible(true);
            }
        } catch (NegocioException ex) {
            System.getLogger(PantallaConfirmarPedido.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        recalcularResumen();
    }

    private JLabel crearCeldaResumen(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    private JPanel wrapResumen(JLabel l) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        p.add(l, BorderLayout.CENTER);
        return p;
    }
}