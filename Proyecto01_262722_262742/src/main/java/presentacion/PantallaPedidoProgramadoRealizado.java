package presentacion;

import dominio.PedidoProgramado;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * <h1>PantallaPedidoProgramadoRealizado</h1>
 *
 * <p>
 * Pantalla de confirmación que muestra el <b>resumen</b> de un
 * {@link PedidoProgramado} después de ser registrado correctamente.
 * </p>
 *
 * <p>
 * La UI presenta:
 * </p>
 * <ul>
 * <li>Título principal "Panadería" y subtítulo de confirmación.</li>
 * <li>Cajas informativas con:
 * <ul>
 * <li>Número de pedido</li>
 * <li>Estado</li>
 * <li>Total</li>
 * <li>Método de pago</li>
 * <li>Fecha de creación</li>
 * </ul>
 * </li>
 * <li>Botón "Listo" para regresar al {@link Menu}.</li>
 * <li>Footer informativo.</li>
 * </ul>
 *
 * <h2>Origen de la información</h2>
 * <p>
 * Los datos mostrados se obtienen directamente del objeto
 * {@link PedidoProgramado} recibido como parámetro (por ejemplo {@code getNumeroPedido()}, {@code getEstado()}, {@code getTotal()},
 * {@code getMetodoPago()} y {@code getFechaCreacion()}).
 * </p>
 *
 * <h2>Navegación</h2>
 * <p>
 * Al presionar "Listo" se abre {@link Menu} utilizando el {@link AppContext} y
 * se cierra esta ventana con {@link #dispose()}.
 * </p>
 *
 * @author
 */
public class PantallaPedidoProgramadoRealizado extends JFrame {

    /**
     * Label que muestra el número del pedido (Num. de pedido).
     */
    private JLabel lblNumPedido;

    /**
     * Label que muestra el estado actual del pedido.
     */
    private JLabel lblEstado;

    /**
     * Label que muestra el total del pedido.
     */
    private JLabel lblTotal;

    /**
     * Label que muestra la fecha del pedido (formateada).
     */
    private JLabel lblFecha;

    /**
     * Label que muestra el método de pago del pedido.
     */
    private JLabel lblMetodoPago;

    /**
     * Contexto global de la aplicación; permite navegación y acceso a estado
     * compartido.
     */
    private final AppContext ctx;

    /**
     * Pantalla que muestra el resumen del pedido programado realizado.
     *
     * @param ctx contexto de aplicación (contiene BOs compartidos)
     * @param pedidoProgramado pedido que se acaba de registrar
     */
    public PantallaPedidoProgramadoRealizado(AppContext ctx, PedidoProgramado pedidoProgramado) {

        this.ctx = ctx;

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
        lblTotal = crearCajaTexto("Total: $" + String.format("%.2f", pedidoProgramado.getTotal()));
        lblMetodoPago = crearCajaTexto("Método de pago: " + pedidoProgramado.getMetodoPago());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblFecha = crearCajaTexto("Fecha: " + (pedidoProgramado.getFechaCreacion() == null
                ? ""
                : pedidoProgramado.getFechaCreacion().format(fmt)));

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

        /**
         * Regresa al inicio de sesión del cliente (flujo simple por ahora). Si
         * después quieres regresar al Menú con sesión iniciada, aquí lo
         * cambiamos.
         */
        btnListo.addActionListener(e -> {
            new Menu(ctx).setVisible(true);
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

    /**
     * <p>
     * Crea un {@link JLabel} centrado con estilo estándar para el texto dentro
     * de las cajas.
     * </p>
     *
     * @param text texto a mostrar
     * @return label configurado
     */
    private JLabel crearCajaTexto(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        l.setForeground(new Color(40, 40, 40));
        return l;
    }

    /**
     * <p>
     * Envuelve un {@link JLabel} dentro de un panel con fondo gris, borde y
     * tamaño fijo, simulando una "caja" de información.
     * </p>
     *
     * @param label label a insertar en la caja
     * @return panel con estilo de caja
     */
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
