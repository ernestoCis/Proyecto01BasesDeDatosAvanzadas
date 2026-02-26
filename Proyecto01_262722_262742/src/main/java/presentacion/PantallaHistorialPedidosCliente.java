package presentacion;

import dominio.*;
import negocio.Excepciones.NegocioException;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <h1>PantallaHistorialPedidosCliente</h1>
 *
 * <p>
 * Pantalla para que el cliente visualice el <b>historial de pedidos</b>
 * registrados en el sistema.
 * </p>
 *
 * <p>
 * La UI presenta:
 * </p>
 * <ul>
 * <li>Botón de regreso hacia {@link #pantallaAnterior} (si existe).</li>
 * <li>Títulos centrados "Panadería" y "Historial de pedidos".</li>
 * <li>Acceso a filtros mediante un botón (⚙) y un label clickeable
 * ("Filtrar").</li>
 * <li>Scroll vertical con tarjetas (cards) de pedidos construidas
 * dinámicamente.</li>
 * <li>Footer informativo.</li>
 * </ul>
 *
 * <h2>Carga de pedidos</h2>
 * <p>
 * Al inicializar la pantalla se invoca {@link #cargarPedidos()}, el cual
 * consulta los pedidos del cliente actual mediante
 * {@code ctx.getPedidoBO().listarPedidosPorCliente(clienteActual.getId())}. Si
 * no existen pedidos, se muestra el mensaje "No tienes pedidos aún.".
 * </p>
 *
 * <h2>Tarjetas de pedido</h2>
 * <p>
 * Cada pedido se renderiza con {@link #crearCardPedido(int, Pedido)}:
 * </p>
 * <ul>
 * <li>Sección izquierda con información general (tipo, folio/número, fecha,
 * estado, subtotal/cupón si aplica, total).</li>
 * <li>Sección derecha con la lista de {@link DetallePedido} del pedido.</li>
 * <li>Botón "Cancelar" únicamente si el estado del pedido es
 * {@link EstadoPedido#Pendiente}.</li>
 * </ul>
 *
 * <h2>Filtros</h2>
 * <p>
 * El diálogo de filtros ({@link #abrirDialogoFiltro()}) permite:
 * </p>
 * <ul>
 * <li>Filtrar por <b>folio</b> (solo pedidos Express).</li>
 * <li>Filtrar por <b>rango de fechas</b> (inicio/fin) usando formato
 * dd/MM/yyyy.</li>
 * </ul>
 *
 * <h2>Cancelación</h2>
 * <p>
 * Si el pedido está en estado pendiente, la pantalla permite cancelarlo
 * mediante {@link #cancelarPedido(Pedido)} que confirma la acción y llama
 * {@code ctx.getPedidoBO().actualizarEstadoPedido(...)}.
 * </p>
 *
 * @author
 */
public class PantallaHistorialPedidosCliente extends JFrame {

    /**
     * Pantalla anterior a la que se regresa al presionar el botón "←".
     */
    private final JFrame pantallaAnterior;

    /**
     * Contexto global de la aplicación; permite acceder a BOs y estado de
     * sesión.
     */
    private final AppContext ctx;

    /**
     * Cliente actualmente autenticado obtenido desde el {@link AppContext}.
     */
    private final Cliente clienteActual;

    /**
     * Label que funciona como acceso alterno para abrir el filtro (clickeable).
     */
    private JLabel lblFiltrar;

    /**
     * Botón con icono (⚙) que abre el diálogo de filtros.
     */
    private JButton btnFiltrar;

    /**
     * Botón de regreso.
     * <p>
     * Nota: en el constructor se declara también un {@code JButton btnBack}
     * local, por lo que este atributo no se utiliza en esa sección, pero se
     * mantiene tal como está.
     * </p>
     */
    private JButton btnBack;

    /**
     * Contenedor vertical donde se agregan dinámicamente las tarjetas de
     * pedidos.
     */
    private JPanel contenedorCards;

    /**
     * Formateador de fecha para mostrar "dd/MM/yy" en las tarjetas.
     */
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy");

    /**
     * <p>
     * Constructor de la pantalla de historial de pedidos del cliente.
     * </p>
     *
     * <p>
     * Construye la interfaz con:
     * </p>
     * <ul>
     * <li>Fondo beige y tarjeta blanca central</li>
     * <li>Top con botón de regreso, títulos y controles de filtro</li>
     * <li>Centro con scroll de tarjetas</li>
     * <li>Footer informativo</li>
     * </ul>
     *
     * <p>
     * Finalmente invoca {@link #cargarPedidos()} para consultar y renderizar el
     * listado inicial.
     * </p>
     *
     * @param pantallaAnterior ventana anterior a mostrar al regresar (puede ser
     * {@code null})
     * @param ctx contexto global de la aplicación
     */
    public PantallaHistorialPedidosCliente(JFrame pantallaAnterior, AppContext ctx) {
        this.pantallaAnterior = pantallaAnterior;
        this.ctx = ctx;
        this.clienteActual = ctx.getClienteActual();

        setTitle("Panadería - Historial de pedidos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 920);
        setLocationRelativeTo(null);

        // Fondo beige
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(new Color(214, 186, 150));
        setContentPane(root);

        // Tarjeta blanca
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(40, 40, 40), 2),
                new EmptyBorder(18, 22, 18, 22)
        ));
        card.setPreferredSize(new Dimension(920, 840));
        root.add(card);

        // ----- parte de arriba -----
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        /**
         * Botón de regreso: muestra {@link #pantallaAnterior} si existe y
         * cierra esta ventana.
         */
        JButton btnBack = new JButton("←");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            if (pantallaAnterior != null) {
                pantallaAnterior.setVisible(true);
            }
            dispose();
        });

        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftTop.setOpaque(false);
        leftTop.add(btnBack);

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel lblPan = new JLabel("Panadería");
        lblPan.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPan.setFont(new Font("Segoe UI", Font.BOLD, 60));

        JLabel lblSub = new JLabel("Historial de pedidos");
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 22));

        titles.add(lblPan);
        titles.add(lblSub);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightTop.setOpaque(false);

        /**
         * Botón (⚙) que abre el diálogo de filtros.
         */
        btnFiltrar = new JButton("⚙");
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.setBorderPainted(false);
        btnFiltrar.setContentAreaFilled(false);
        btnFiltrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFiltrar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        btnFiltrar.addActionListener(e -> abrirDialogoFiltro());

        /**
         * Label "Filtrar" que también abre el diálogo de filtros al hacer
         * click.
         */
        lblFiltrar = new JLabel("Filtrar");
        lblFiltrar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFiltrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblFiltrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                abrirDialogoFiltro();
            }
        });

        rightTop.add(btnFiltrar);
        rightTop.add(lblFiltrar);

        top.add(titles, BorderLayout.CENTER);

        top.add(leftTop, BorderLayout.WEST);

        top.add(rightTop, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);

        // ----- parte central -----
        /**
         * Contenedor vertical donde se agregan las cards de pedidos.
         */
        contenedorCards = new JPanel();
        contenedorCards.setOpaque(false);
        contenedorCards.setLayout(new BoxLayout(contenedorCards, BoxLayout.Y_AXIS));
        contenedorCards.setBorder(new EmptyBorder(10, 0, 0, 0));

        JScrollPane scroll = new JScrollPane(contenedorCards);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        card.add(scroll, BorderLayout.CENTER);

        // ----- fotter -----
        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.add(footer, BorderLayout.WEST);

        card.add(footerPanel, BorderLayout.SOUTH);

        cargarPedidos();
    }

    /**
     * <p>
     * Carga y renderiza todos los pedidos del cliente actual en
     * {@link #contenedorCards}.
     * </p>
     *
     * <p>
     * Consulta la lista usando
     * {@code ctx.getPedidoBO().listarPedidosPorCliente(clienteActual.getId())}.
     * Si la lista está vacía, muestra un mensaje "No tienes pedidos aún.".
     * </p>
     *
     * <p>
     * Si ocurre un error, regresa a {@link #pantallaAnterior} (si existe),
     * cierra esta ventana y muestra el error con {@link JOptionPane}.
     * </p>
     */
    private void cargarPedidos() {
        contenedorCards.removeAll();

        try {
            List<Pedido> pedidos = ctx.getPedidoBO().listarPedidosPorCliente(clienteActual.getId());

            if (pedidos == null || pedidos.isEmpty()) {
                JLabel vacio = new JLabel("No tienes pedidos aún.");
                vacio.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                vacio.setBorder(new EmptyBorder(20, 10, 0, 0));
                contenedorCards.add(vacio);
            } else {
                int idx = 1;
                for (Pedido p : pedidos) {
                    contenedorCards.add(crearCardPedido(idx++, p));
                    contenedorCards.add(Box.createVerticalStrut(14));
                }
            }

        } catch (NegocioException ex) {
            this.pantallaAnterior.setVisible(true);
            this.dispose();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        contenedorCards.revalidate();
        contenedorCards.repaint();
    }

    /**
     * <p>
     * Construye una tarjeta (card) para visualizar un pedido y sus detalles.
     * </p>
     *
     * <p>
     * La card contiene:
     * </p>
     * <ul>
     * <li>Panel izquierdo con información general del pedido.</li>
     * <li>Panel derecho con la lista de {@link DetallePedido}.</li>
     * <li>Botón "Cancelar" únicamente si el pedido está en estado
     * {@link EstadoPedido#Pendiente}.</li>
     * </ul>
     *
     * <p>
     * Para pedidos programados, se consulta el subtotal con
     * {@code ctx.getDetallePedidoBO().obtenerSubtotalPedido(pp.getId())}.
     * </p>
     *
     * @param numeroVisual número consecutivo mostrado en la UI (Pedido #1,
     * Pedido #2, ...)
     * @param pedido pedido a renderizar
     * @return panel construido con la card del pedido
     * @throws NegocioException si ocurre un error al consultar datos
     * relacionados al pedido
     */
    private JPanel crearCardPedido(int numeroVisual, Pedido pedido) throws NegocioException {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));

        // ----- parte izquierda -----
        JPanel izquierda = new JPanel();
        izquierda.setOpaque(false);
        izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));
        izquierda.setBorder(new EmptyBorder(10, 10, 10, 10));
        izquierda.setPreferredSize(new Dimension(260, 190));

        JPanel tituloRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        tituloRow.setOpaque(false);

        JLabel lblPedido = new JLabel("Pedido #" + numeroVisual);
        lblPedido.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tituloRow.add(lblPedido);

        boolean esExpress = pedido instanceof PedidoExpress;
        if (esExpress) {
            JLabel tag = new JLabel("EXPRESS");
            tag.setFont(new Font("Segoe UI", Font.BOLD, 12));
            tag.setForeground(new Color(170, 20, 20));
            tituloRow.add(tag);
        }

        izquierda.add(tituloRow);
        izquierda.add(Box.createVerticalStrut(4));

        if (pedido instanceof PedidoProgramado pp) {
            izquierda.add(labelInfo("Tipo: Programado"));
            izquierda.add(labelInfo("Número de pedido: " + pp.getNumeroPedido()));
            izquierda.add(labelInfo("Fecha: " + (pp.getFechaCreacion() != null ? pp.getFechaCreacion().format(fmt) : "")));
            izquierda.add(labelInfo("Estado: " + pp.getEstado()));
            izquierda.add(labelInfo("Subtotal: $" + ctx.getDetallePedidoBO().obtenerSubtotalPedido(pp.getId())));

            String cuponTxt = "N/A";
            if (pp.getCupon() != null && pp.getCupon().getNombre() != null && !pp.getCupon().getNombre().isBlank()) {
                cuponTxt = pp.getCupon().getNombre();
            }
            izquierda.add(labelInfo("Cupón: " + cuponTxt));
            izquierda.add(Box.createVerticalStrut(6));

            JLabel total = new JLabel("Total: $" + redondear(pp.getTotal()));
            total.setFont(new Font("Segoe UI", Font.BOLD, 14));
            izquierda.add(total);

        } else if (pedido instanceof PedidoExpress pe) {
            izquierda.add(labelInfo("Tipo: EXPRESS"));
            izquierda.add(labelInfo("Folio: " + pe.getFolio()));
//            izquierda.add(labelInfo("PIN: " + pe.getPin()));
            izquierda.add(labelInfo("Fecha: " + (pe.getFechaCreacion() != null ? pe.getFechaCreacion().format(fmt) : "")));
            izquierda.add(labelInfo("Estado: " + pe.getEstado()));
            izquierda.add(Box.createVerticalStrut(6));

            JLabel total = new JLabel("Total: $" + redondear(pe.getTotal()));
            total.setFont(new Font("Segoe UI", Font.BOLD, 14));
            izquierda.add(total);

        }

        card.add(izquierda, BorderLayout.WEST);

        // ----- parte derecha -----
        JPanel derecha = new JPanel(new BorderLayout());
        derecha.setOpaque(false);
        derecha.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));

        List<DetallePedido> detalles = ctx.getDetallePedidoBO().listarDetallesPorPedido(pedido.getId());

        if (detalles != null) {
            for (DetallePedido d : detalles) {
                lista.add(labelDetalle(d));
            }
        }

        derecha.add(lista, BorderLayout.CENTER);

        // Botón cancelar SOLO si estado = pendiente
        if (pedido.getEstado() == EstadoPedido.Pendiente) {
            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.setPreferredSize(new Dimension(160, 34));
            btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btnCancelar.setFocusPainted(false);
            btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnCancelar.setBorder(new LineBorder(new Color(60, 60, 60), 2));
            btnCancelar.setBackground(new Color(245, 245, 245));

            btnCancelar.addActionListener(e -> cancelarPedido(pedido));

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
            bottom.setOpaque(false);
            bottom.add(btnCancelar);

            derecha.add(bottom, BorderLayout.SOUTH);
        }

        card.add(derecha, BorderLayout.CENTER);

        return card;
    }

    /**
     * Crea un {@link JLabel} con estilo estándar para mostrar información del
     * pedido en la sección izquierda.
     *
     * @param txt texto a mostrar
     * @return label configurado
     */
    private JLabel labelInfo(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    /**
     * <p>
     * Construye un {@link JLabel} para mostrar un detalle del pedido con
     * formato de puntos.
     * </p>
     *
     * <p>
     * Formato esperado (ejemplo):
     * </p>
     * <ul>
     * <li>{@code "2 Concha............. $32"}</li>
     * </ul>
     *
     * @param d detalle del pedido a renderizar
     * @return label formateado con cantidad, nombre y subtotal
     */
    private JLabel labelDetalle(DetallePedido d) {
        String nombre = (d.getProducto() != null) ? d.getProducto().getNombre() : "Producto";
        String izquierda = d.getCantidad() + " " + nombre;
        String derecha = "$" + redondear(d.getSubtotal());

        String linea = rellenarPuntos(izquierda, derecha, 60);

        JLabel l = new JLabel(linea);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return l;
    }

    /**
     * <p>
     * Rellena con puntos una cadena para alinear visualmente la parte izquierda
     * y derecha.
     * </p>
     *
     * <p>
     * Si el texto izquierdo excede el ancho, se recorta.
     * </p>
     *
     * @param left texto izquierdo (cantidad + nombre)
     * @param right texto derecho (precio/subtotal)
     * @param ancho ancho base para rellenar con puntos
     * @return cadena final con puntos y el texto derecho al final
     */
    private String rellenarPuntos(String left, String right, int ancho) {
        String base = left;
        if (base.length() > ancho) {
            base = base.substring(0, ancho);
        }

        StringBuilder sb = new StringBuilder(base);
        while (sb.length() < ancho) {
            sb.append('.');
        }
        sb.append(" ").append(right);
        return sb.toString();
    }

    /**
     * <p>
     * Intenta cancelar un pedido (solo aplica cuando la UI muestra el botón y
     * el estado es pendiente).
     * </p>
     *
     * <p>
     * Muestra confirmación con {@link JOptionPane} y, si acepta, actualiza el
     * estado a {@link EstadoPedido#Cancelado} mediante
     * {@code ctx.getPedidoBO().actualizarEstadoPedido(...)}. Finalmente recarga
     * la lista con {@link #cargarPedidos()}.
     * </p>
     *
     * @param pedido pedido a cancelar
     */
    private void cancelarPedido(Pedido pedido) {
        int r = JOptionPane.showConfirmDialog(
                this,
                "¿Cancelar este pedido?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (r != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            ctx.getPedidoBO().actualizarEstadoPedido(pedido.getId(), EstadoPedido.Cancelado);
            JOptionPane.showMessageDialog(this, "Pedido cancelado.");
            cargarPedidos();
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Redondea un valor flotante usando {@link Math#round(float)} y lo regresa
     * como {@link String}.
     *
     * @param v valor a redondear
     * @return representación en string del valor redondeado
     */
    private String redondear(float v) {
        return String.valueOf(Math.round(v));
    }

    /**
     * <p>
     * Refresca visualmente el contenedor de cards usando una lista de pedidos
     * ya filtrada.
     * </p>
     *
     * <p>
     * Si la lista es vacía o nula, muestra el mensaje "No hay pedidos con ese
     * filtro.". Si ocurre un error al construir alguna card, se agrega un label
     * indicando el id del pedido.
     * </p>
     *
     * @param pedidos lista filtrada a renderizar
     */
    private void refrescarListaPedidos(List<Pedido> pedidos) {

        contenedorCards.removeAll();

        if (pedidos == null || pedidos.isEmpty()) {
            JLabel vacio = new JLabel("No hay pedidos con ese filtro.");
            vacio.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            vacio.setBorder(new EmptyBorder(20, 10, 0, 0));
            contenedorCards.add(vacio);
        } else {
            int idx = 1;
            for (Pedido p : pedidos) {
                try {
                    contenedorCards.add(crearCardPedido(idx++, p));
                    contenedorCards.add(Box.createVerticalStrut(14));
                } catch (Exception ex) {
                    JLabel err = new JLabel("Error al mostrar pedido ID: " + p.getId());
                    err.setForeground(new Color(160, 40, 40));
                    contenedorCards.add(err);
                }
            }
        }

        contenedorCards.revalidate();
        contenedorCards.repaint();
    }

    /**
     * <p>
     * Abre un diálogo para filtrar pedidos del cliente actual por:
     * </p>
     * <ul>
     * <li>Folio (modo Express)</li>
     * <li>Rango de fechas (inicio/fin)</li>
     * </ul>
     *
     * <p>
     * Según la opción seleccionada, habilita/deshabilita campos y al confirmar
     * realiza la consulta con
     * {@code ctx.getPedidoBO().listarPedidosPorClienteFiltro(...)}. Finalmente
     * actualiza la UI con {@link #refrescarListaPedidos(List)}.
     * </p>
     */
    private void abrirDialogoFiltro() {

        JRadioButton rbFolio = new JRadioButton("Por folio (Express)");
        JRadioButton rbFechas = new JRadioButton("Por rango de fechas");

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbFolio);
        bg.add(rbFechas);
        rbFolio.setSelected(true);

        JTextField txtFolio = new JTextField(12);

        JTextField txtInicio = new JTextField(10); // dd/MM/yyyy
        JTextField txtFin = new JTextField(10);    // dd/MM/yyyy

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0;
        g.gridy = 0;
        panel.add(rbFolio, g);
        g.gridx = 1;
        panel.add(new JLabel("Folio:"), g);
        g.gridx = 2;
        panel.add(txtFolio, g);

        g.gridx = 0;
        g.gridy = 1;
        panel.add(rbFechas, g);
        g.gridx = 1;
        panel.add(new JLabel("Inicio (dd/MM/yyyy):"), g);
        g.gridx = 2;
        panel.add(txtInicio, g);

        g.gridx = 1;
        g.gridy = 2;
        panel.add(new JLabel("Fin (dd/MM/yyyy):"), g);
        g.gridx = 2;
        panel.add(txtFin, g);

        // Habilitar/deshabilitar campos según modo
        ActionListener toggle = ev -> {
            boolean porFolio = rbFolio.isSelected();
            txtFolio.setEnabled(porFolio);
            txtInicio.setEnabled(!porFolio);
            txtFin.setEnabled(!porFolio);
        };
        rbFolio.addActionListener(toggle);
        rbFechas.addActionListener(toggle);
        toggle.actionPerformed(null);

        int op = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Filtrar pedidos",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (op != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String folio = null;
            LocalDate fIni = null;
            LocalDate fFin = null;

            if (rbFolio.isSelected()) {
                folio = txtFolio.getText().trim();
                if (folio.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Escribe un folio.", "Filtro", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                fIni = parseFecha(txtInicio.getText().trim());
                fFin = parseFecha(txtFin.getText().trim());
                if (fIni.isAfter(fFin)) {
                    JOptionPane.showMessageDialog(this, "La fecha inicio no puede ser mayor que la fecha fin.", "Filtro", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            List<Pedido> pedidos = ctx.getPedidoBO().listarPedidosPorClienteFiltro(
                    clienteActual.getId(),
                    folio,
                    fIni,
                    fFin
            );

            refrescarListaPedidos(pedidos);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Filtro inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Convierte una cadena con formato {@code dd/MM/yyyy} a {@link LocalDate}.
     *
     * @param s texto de fecha a parsear
     * @return fecha parseada como {@link LocalDate}
     */
    private LocalDate parseFecha(String s) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(s, fmt);
    }
}
