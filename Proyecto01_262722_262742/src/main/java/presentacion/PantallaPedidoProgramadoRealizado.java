package presentacion;

import dominio.PedidoProgramado;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import negocio.BOs.iCuponBO;
import negocio.BOs.iPedidoBO;
import negocio.BOs.iProductoBO;

public class PantallaPedidoProgramadoRealizado extends JFrame {

    private JLabel lblNumPedido;
    private JLabel lblEstado;
    private JLabel lblTotal;
    private JLabel lblFecha;
    private JLabel lblMetodoPago;
    
    private final iProductoBO productoBO;
    private final iCuponBO cuponBO;
    private final iPedidoBO pedidoBO;


    public PantallaPedidoProgramadoRealizado(PedidoProgramado pedidoProgramado, iProductoBO productoBO, iCuponBO cuponBO, iPedidoBO pedidoBO) {

        this.productoBO = productoBO;
        this.cuponBO = cuponBO;
        this.pedidoBO = pedidoBO;
        
        setTitle("Panadería - Pedido realizado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 600);
        setLocationRelativeTo(null);

        // Fondo beige
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(new Color(214, 186, 150));
        setContentPane(root);

        // Tarjeta blanca con borde negro
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(30, 30, 30), 2, false),
                new EmptyBorder(18, 22, 18, 22)
        ));
        card.setPreferredSize(new Dimension(860, 520));
        root.add(card);

        // ----- parte central -----
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Panadería");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 70));

        JLabel subtitle = new JLabel("Pedido realizado correctamente");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 22));

        center.add(Box.createVerticalStrut(60));
        center.add(title);
        center.add(Box.createVerticalStrut(10));
        center.add(subtitle);
        center.add(Box.createVerticalStrut(28));

        // Cajas
        lblNumPedido = crearCajaTexto("Num. de pedido: " + pedidoProgramado.getNumeroPedido());
        lblEstado = crearCajaTexto("Estado: " + pedidoProgramado.getEstado());
        lblTotal = crearCajaTexto("Total: $" +String.format("%.2f", pedidoProgramado.getTotal()));
        lblMetodoPago = crearCajaTexto("Método de pago: " + pedidoProgramado.getMetodoPago());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblFecha = crearCajaTexto("Fecha: " + (pedidoProgramado.getFechaCreacion() == null ? "" : pedidoProgramado.getFechaCreacion().format(fmt)));

        center.add(wrapCaja(lblNumPedido));
        center.add(Box.createVerticalStrut(12));
        center.add(wrapCaja(lblEstado));
        center.add(Box.createVerticalStrut(12));
        center.add(wrapCaja(lblTotal));
        center.add(Box.createVerticalStrut(12));
        center.add(wrapCaja(lblMetodoPago));
        center.add(Box.createVerticalStrut(12));
        center.add(wrapCaja(lblFecha));
        center.add(Box.createVerticalStrut(18));

        // Botón listo
        JButton btnListo = new JButton("Listo");
        btnListo.setPreferredSize(new Dimension(180, 38));
        btnListo.setMaximumSize(new Dimension(180, 38));
        btnListo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnListo.setFocusPainted(false);
        btnListo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnListo.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        btnListo.setBackground(new Color(245, 245, 245));
        btnListo.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnListo.addActionListener(e -> {
//            PantallaInicioSesionCliente pantallaInicioSesionCliente = new PantallaInicioSesionCliente(productoBO, cuponBO, pedidoBO);
//            pantallaInicioSesionCliente.setVisible(true);
            dispose();
        });

        center.add(btnListo);

        card.add(center, BorderLayout.CENTER);

        // =================== FOOTER ===================
        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.add(footer, BorderLayout.WEST);

        card.add(footerPanel, BorderLayout.SOUTH);
    }

    private JLabel crearCajaTexto(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        l.setForeground(new Color(40, 40, 40));
        return l;
    }

    private JPanel wrapCaja(JLabel label) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(new Color(245, 245, 245));
        box.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        box.setMaximumSize(new Dimension(380, 48));
        box.setPreferredSize(new Dimension(380, 48));
        box.add(label, BorderLayout.CENTER);
        return box;
    }
}