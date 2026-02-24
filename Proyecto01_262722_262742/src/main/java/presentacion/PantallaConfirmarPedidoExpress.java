/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dominio.DetallePedido;
import dominio.EstadoPedido;
import dominio.ItemCarrito;
import dominio.MetodoPago;
import dominio.PedidoExpress;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import negocio.Excepciones.NegocioException;

public class PantallaConfirmarPedidoExpress extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblTotal;

    private JComboBox<MetodoPago> cbMetodoPago;
    private MetodoPago metodoPagoSeleccionado; 

    private final List<ItemCarrito> carrito;
    private final AppContext ctx;

    private final JFrame pantallaAnterior;
    private float total;

    public PantallaConfirmarPedidoExpress(JFrame pantallaAnterior, List<ItemCarrito> carrito, AppContext ctx) {
        this.pantallaAnterior = pantallaAnterior;
        this.carrito = carrito;
        this.ctx = ctx;

        setTitle("Panadería - Confirmar pedido Express");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 600);
        setLocationRelativeTo(null);

        // Fondo beige (marco)
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
        card.setPreferredSize(new Dimension(860, 520));
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
            if (pantallaAnterior != null) {
                pantallaAnterior.setVisible(true);
            }
            dispose();
        });

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelIzq.setOpaque(false);
        panelIzq.add(btnFlecha);
        topBar.add(panelIzq, BorderLayout.WEST);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Panadería");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 56));

        JLabel lblExpress = new JLabel("EXPRESS");
        lblExpress.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblExpress.setForeground(new Color(200, 60, 60));
        lblExpress.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Confirmar pedido");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 22));

        header.add(Box.createVerticalStrut(2));
        header.add(title);
        header.add(lblExpress);
        header.add(Box.createVerticalStrut(2));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(10));

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(topBar, BorderLayout.NORTH);
        north.add(header, BorderLayout.CENTER);

        card.add(north, BorderLayout.NORTH);

        // ----- parte central -----
        String[] cols = {"Producto", "Precio pz.", "Cantidad", "Subtotal"};

        modelo = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(32);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(1).setCellRenderer(center);
        tabla.getColumnModel().getColumn(2).setCellRenderer(center);
        tabla.getColumnModel().getColumn(3).setCellRenderer(center);

        tabla.getColumnModel().getColumn(0).setPreferredWidth(300);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.setBorder(new EmptyBorder(6, 40, 0, 40));
        centro.add(scroll, BorderLayout.NORTH);

        JPanel cajaVacia = new JPanel();
        cajaVacia.setBackground(Color.WHITE);
        cajaVacia.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        cajaVacia.setPreferredSize(new Dimension(1, 250));

        JPanel wrapCaja = new JPanel(new BorderLayout());
        wrapCaja.setOpaque(false);
        wrapCaja.add(cajaVacia, BorderLayout.CENTER);

        centro.add(wrapCaja, BorderLayout.CENTER);

        card.add(centro, BorderLayout.CENTER);

        // ----- parte de abajo -----
        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);

        lblTotal = new JLabel();
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel cajaTotal = new JPanel(new BorderLayout());
        cajaTotal.setBackground(new Color(245, 245, 245));
        cajaTotal.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        cajaTotal.setPreferredSize(new Dimension(170, 32));
        cajaTotal.add(lblTotal, BorderLayout.CENTER);
        lblTotal.setBorder(new EmptyBorder(0, 12, 0, 12));

        JLabel lblMetodoPago = new JLabel("Método de pago:");
        lblMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        cbMetodoPago = new JComboBox<>(MetodoPago.values());
        cbMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbMetodoPago.setPreferredSize(new Dimension(120, 26));
        cbMetodoPago.setFocusable(false);

        JPanel contenidoMetodo = new JPanel(new BorderLayout(10, 0));
        contenidoMetodo.setOpaque(false);
        contenidoMetodo.setBorder(new EmptyBorder(0, 12, 0, 8));
        contenidoMetodo.add(lblMetodoPago, BorderLayout.WEST);
        contenidoMetodo.add(cbMetodoPago, BorderLayout.EAST);

        JPanel cajaMetodoPago = new JPanel(new BorderLayout());
        cajaMetodoPago.setBackground(new Color(245, 245, 245));
        cajaMetodoPago.setBorder(new LineBorder(new Color(60, 60, 60), 1));
        cajaMetodoPago.setPreferredSize(new Dimension(260, 32));
        cajaMetodoPago.add(contenidoMetodo, BorderLayout.CENTER);

        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelResumen.setOpaque(false);
        panelResumen.setBorder(new EmptyBorder(0, 40, 0, 40));
        panelResumen.add(cajaMetodoPago);
        panelResumen.add(cajaTotal);

        JButton btnRealizar = new JButton("Realizar pedido");
        btnRealizar.setPreferredSize(new Dimension(180, 34));
        btnRealizar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRealizar.setFocusPainted(false);
        btnRealizar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRealizar.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        btnRealizar.setBackground(new Color(245, 245, 245));

        btnRealizar.addActionListener(e -> {
            if (carrito == null || carrito.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tu carrito está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try{
                PedidoExpress pedidoExpress = new PedidoExpress();
                pedidoExpress.setEstado(EstadoPedido.Pendiente);
                pedidoExpress.setFechaCreacion(LocalDateTime.now());
                pedidoExpress.setMetodoPago((MetodoPago) cbMetodoPago.getSelectedItem());
                pedidoExpress.setTotal(total);
                pedidoExpress.setNumeroPedido(ctx.getPedidoBO().generarNumeroDePedido());
                pedidoExpress.setFolio(ctx.getPedidoBO().generarFolio());
                pedidoExpress.setPin(ctx.getPedidoBO().generarPIN());
                
                String pin = pedidoExpress.getPin();
                
                List<DetallePedido> detalles = crearDetallesDesdeCarrito();
                
                if (ctx.getPedidoBO().agregarPedidoExpress(pedidoExpress, detalles) != null) {

                    new PantallaPedidoExpressRealizado(pedidoExpress, ctx, pin).setVisible(true);
                    dispose();

                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar el pedido.");
                }
                
                
            }catch(NegocioException ex){
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
            
        });

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        panelBtn.setOpaque(false);
        panelBtn.setBorder(new EmptyBorder(0, 40, 0, 40));
        panelBtn.add(btnRealizar);

        // Footer
        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.add(footer, BorderLayout.WEST);

        JPanel stackSouth = new JPanel();
        stackSouth.setOpaque(false);
        stackSouth.setLayout(new BoxLayout(stackSouth, BoxLayout.Y_AXIS));
        stackSouth.add(panelResumen);  
        stackSouth.add(panelBtn);
        stackSouth.add(footerPanel);

        south.add(stackSouth, BorderLayout.CENTER);
        card.add(south, BorderLayout.SOUTH);

        cargarTabla();
        actualizarTotal();
    }

    private void cargarTabla() {
        modelo.setRowCount(0);

        if (carrito == null) {
            return;
        }

        for (ItemCarrito it : carrito) {
            String nombre = it.getProducto().getNombre();
            float precio = it.getProducto().getPrecio();
            int cantidad = it.getCantidad();
            float subtotal = precio * cantidad;

            modelo.addRow(new Object[]{
                nombre,
                "$" + Math.round(precio),
                cantidad,
                "$" + Math.round(subtotal)
            });
        }
    }

    private void actualizarTotal() {
        float total = 0;
        if (carrito != null) {
            for (ItemCarrito it : carrito) {
                total += it.getProducto().getPrecio() * it.getCantidad();
            }
        }
        this.total = total;
        lblTotal.setText("Total a pagar: $" + Math.round(total));
    }
    
    private List<DetallePedido> crearDetallesDesdeCarrito() throws NegocioException {

        if (carrito == null || carrito.isEmpty()) {
            throw new NegocioException("El carrito está vacío.");
        }

        List<DetallePedido> detalles = new ArrayList<>();

        for (ItemCarrito item : carrito) {

            int cantidad = item.getCantidad();
            float precio = item.getProducto().getPrecio();
            float subtotal = precio * cantidad;

            DetallePedido d = new DetallePedido();
            d.setProducto(item.getProducto());
            d.setCantidad(cantidad);
            d.setPrecio(precio);
            d.setSubtotal(subtotal);

            // En esta pantalla no hay notas, entonces:
            d.setNota(null);

            detalles.add(d);
        }

        return detalles;
    }
}
