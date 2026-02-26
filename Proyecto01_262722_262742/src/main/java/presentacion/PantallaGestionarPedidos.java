/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dominio.*;
import presentacion.AppContext;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import negocio.Excepciones.NegocioException;

/**
 * <h1>PantallaGestionarPedidos</h1>
 *
 * <p>
 * Pantalla para que el <b>empleado</b> gestione pedidos (programados y
 * express).
 * </p>
 *
 * <p>
 * Funcionalidades principales:
 * </p>
 * <ul>
 * <li>Muestra una lista de pedidos en formato <b>cards</b> dentro de un
 * {@link JScrollPane}.</li>
 * <li>Permite filtrar por <b>Folio</b>, <b>Teléfono</b> o <b>Rango de
 * fechas</b>, además de la vista <b>Todos</b>.</li>
 * <li>Permite cambiar el {@link EstadoPedido} (Listo / Entregado / Cancelado)
 * según el estado actual.</li>
 * <li>Cuando se intenta marcar <b>Entregado</b> un {@link PedidoExpress},
 * solicita el <b>PIN</b> y lo valida con la capa de negocio.</li>
 * </ul>
 *
 * <h2>Carga de datos</h2>
 * <p>
 * La pantalla obtiene los pedidos mediante
 * {@code ctx.getPedidoBO().listarPedidos()} y, al filtrar, utiliza
 * {@code ctx.getPedidoBO().listarPedidosFiltro(...)}.
 * </p>
 *
 * <h2>Validación de entrega Express</h2>
 * <p>
 * Si el pedido es {@link PedidoExpress} y el nuevo estado es
 * {@link EstadoPedido#Entregado}, se solicita un PIN con
 * {@link JOptionPane#showInputDialog(java.awt.Component, java.lang.Object, java.lang.String, int)}
 * y se valida mediante
 * {@code ctx.getPedidoBO().entregarPedidoExpressConPin(pedido.getId(), pinCapturado)}.
 * </p>
 *
 * @author Isaac
 */
public class PantallaGestionarPedidos extends JFrame {

    /**
     * Contexto global de la aplicación; permite acceder a BOs y estado de
     * sesión.
     */
    private final AppContext ctx;

    /**
     * Panel contenedor donde se renderizan las cards de pedidos.
     */
    private JPanel listaCards;

    /**
     * Scroll que envuelve {@link #listaCards} para permitir desplazamiento
     * vertical.
     */
    private JScrollPane scroll;

    /**
     * ComboBox para seleccionar el modo de filtrado (Todos, Folio, Teléfono,
     * Rango de fechas).
     */
    private JComboBox<String> cbFiltro;

    /**
     * Campo de texto para capturar el folio al filtrar.
     */
    private JTextField txtFolio;

    /**
     * Campo de texto para capturar el teléfono del cliente al filtrar.
     */
    private JTextField txtTelefono;

    /**
     * Campo de texto para capturar la fecha "Desde" al filtrar por rango.
     */
    private JTextField txtDesde;

    /**
     * Campo de texto para capturar la fecha "Hasta" al filtrar por rango.
     */
    private JTextField txtHasta;

    /**
     * <p>
     * Constructor de la pantalla de gestión de pedidos.
     * </p>
     *
     * <p>
     * Construye la interfaz con:
     * </p>
     * <ul>
     * <li>Fondo beige y tarjeta blanca con borde.</li>
     * <li>Barra superior con botón de regreso a {@link MenuEmpleado}.</li>
     * <li>Encabezado con títulos.</li>
     * <li>Panel de filtros construido por {@link #crearPanelFiltros()}.</li>
     * <li>Listado de pedidos en cards dentro de {@link #scroll}.</li>
     * <li>Footer informativo.</li>
     * </ul>
     *
     * <p>
     * Finalmente invoca {@link #refrescar()} para pintar los pedidos.
     * </p>
     *
     * @param ctx contexto global de la aplicación
     */
    public PantallaGestionarPedidos(AppContext ctx) {
        this.ctx = ctx;

        setTitle("Panadería - Pedidos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setSize(1120, 650);
        setMinimumSize(new Dimension(1120, 650));
        setLocationRelativeTo(null);

        JPanel base = new JPanel(new GridBagLayout());
        base.setBackground(new Color(214, 186, 150));
        setContentPane(base);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(30, 30, 30), 2, false),
                new EmptyBorder(18, 22, 18, 22)
        ));
        card.setPreferredSize(new Dimension(1060, 580));

        GridBagConstraints gbcCard = new GridBagConstraints();
        gbcCard.gridx = 0;
        gbcCard.gridy = 0;
        gbcCard.anchor = GridBagConstraints.CENTER;
        base.add(card, gbcCard);

        JPanel north = new JPanel();
        north.setOpaque(false);
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        /**
         * Botón de regreso; abre {@link MenuEmpleado} y cierra la pantalla
         * actual.
         */
        JButton btnBack = new JButton("←");
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> {
            new MenuEmpleado(ctx).setVisible(true);
            dispose();
        });

        JLabel titulo = new JLabel("Panadería");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 58));

        JLabel pedidos = new JLabel("Pedidos");
        pedidos.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pedidos.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel filtrosWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filtrosWrap.setOpaque(false);
        filtrosWrap.add(crearPanelFiltros());

        topBar.add(btnBack, BorderLayout.WEST);

        north.add(topBar);
        north.add(Box.createVerticalStrut(10));
        north.add(titulo);
        north.add(Box.createVerticalStrut(6));
        north.add(pedidos);
        north.add(Box.createVerticalStrut(12));
        north.add(filtrosWrap);
        north.add(Box.createVerticalStrut(12));

        card.add(north, BorderLayout.NORTH);

        listaCards = new JPanel();
        listaCards.setOpaque(false);
        listaCards.setLayout(new BoxLayout(listaCards, BoxLayout.Y_AXIS));

        scroll = new JScrollPane(listaCards);
        scroll.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        card.add(scroll, BorderLayout.CENTER);

        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.add(footer, BorderLayout.WEST);

        card.add(south, BorderLayout.SOUTH);

        refrescar();
    }

    /**
     * <p>
     * Recarga la lista completa de pedidos (sin filtros) y repinta las cards.
     * </p>
     *
     * <p>
     * Obtiene la información mediante
     * {@code ctx.getPedidoBO().listarPedidos()}. Si no hay pedidos, muestra un
     * mensaje informativo.
     * </p>
     *
     * <p>
     * En caso de error de negocio, muestra un {@link JOptionPane}.
     * </p>
     */
    private void refrescar() {
        listaCards.removeAll();

        try {
            List<Pedido> pedidos = ctx.getPedidoBO().listarPedidos();

            if (pedidos == null || pedidos.isEmpty()) {
                JLabel vacio = new JLabel("No hay pedidos para mostrar.");
                vacio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                vacio.setForeground(new Color(60, 60, 60));
                vacio.setBorder(new EmptyBorder(10, 10, 10, 10));
                listaCards.add(vacio);
            } else {
                int n = 1;
                for (Pedido p : pedidos) {
                    listaCards.add(construirCardPedido(p, n++));
                    listaCards.add(Box.createVerticalStrut(14));
                }
            }

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        listaCards.revalidate();
        listaCards.repaint();
    }

    /**
     * <p>
     * Construye una card visual para un pedido específico.
     * </p>
     *
     * <p>
     * La card incluye:
     * </p>
     * <ul>
     * <li>Título "Pedido #n" y etiqueta <b>EXPRESS</b> si aplica.</li>
     * <li>Información de tipo, cliente, folio (solo express), número, estado y
     * cupón (solo programado).</li>
     * <li>Fechas de creación/entrega y método de pago.</li>
     * <li>Total del pedido.</li>
     * <li>Botones de acción según el {@link EstadoPedido} actual.</li>
     * </ul>
     *
     * @param pedido pedido a renderizar
     * @param numeroVisual número consecutivo mostrado en la card (no
     * necesariamente el número de pedido real)
     * @return panel completo de la card
     */
    private JPanel construirCardPedido(Pedido pedido, int numeroVisual) {

        JPanel cont = new JPanel(new BorderLayout(10, 0));
        cont.setOpaque(true);
        cont.setBackground(new Color(245, 245, 245));
        cont.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        cont.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        cont.setPreferredSize(new Dimension(1020, 170));

        JPanel izq = new JPanel();
        izq.setOpaque(false);
        izq.setBorder(new EmptyBorder(10, 10, 10, 10));
        izq.setLayout(new BoxLayout(izq, BoxLayout.Y_AXIS));

        JLabel lblPedido = new JLabel("Pedido #" + numeroVisual);
        lblPedido.setFont(new Font("Segoe UI", Font.BOLD, 15));

        if (pedido instanceof PedidoExpress) {
            JLabel lblExpress = new JLabel("EXPRESS");
            lblExpress.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblExpress.setForeground(new Color(200, 0, 0));

            JPanel filaTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
            filaTitulo.setOpaque(false);
            filaTitulo.add(lblPedido);
            filaTitulo.add(lblExpress);
            izq.add(filaTitulo);
        } else {
            izq.add(lblPedido);
        }

        izq.add(Box.createVerticalStrut(6));

        String tipo = (pedido instanceof PedidoExpress) ? "Express" : "Programado";
        izq.add(lblInfo("Tipo: " + tipo));

        Cliente c = pedido.getCliente();
        if (c != null) {
            izq.add(lblInfo("Cliente: " + nombreCliente(c)));
        } else {
            izq.add(lblInfo("Cliente: N/A"));
        }

        if (pedido instanceof PedidoExpress pe) {
            izq.add(lblInfo("Folio: " + safe(pe.getFolio())));
        }

        izq.add(lblInfo("No. pedido: " + pedido.getNumeroPedido()));
        izq.add(lblInfo("Estado: " + mostrarEnumBonito(pedido.getEstado())));

        if (pedido instanceof PedidoProgramado pp) {
            izq.add(lblInfo("Cupón: " + (pp.getCupon() == null ? "N/A" : ("#" + pp.getCupon().getId()))));
        }

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.setBorder(new EmptyBorder(18, 0, 10, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy  HH:mm");

        String fechaCreacion = (pedido.getFechaCreacion() != null) ? pedido.getFechaCreacion().format(fmt) : "N/A";
        String fechaEntrega = (pedido.getFechaEntrega() != null) ? pedido.getFechaEntrega().format(fmt) : "N/A";

        MetodoPago mp = pedido.getMetodoPago();
        String metodoPago = (mp != null) ? mp.toString() : "N/A";

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fila1.setOpaque(false);
        fila1.add(lblInfo("Creación: " + fechaCreacion));
        centro.add(fila1, gbc);
        gbc.gridy++;

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fila2.setOpaque(false);
        fila2.add(lblInfo("Entrega: " + fechaEntrega));
        centro.add(fila2, gbc);
        gbc.gridy++;

        JPanel fila3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fila3.setOpaque(false);
        fila3.add(lblInfo("Método de pago: " + metodoPago));
        centro.add(fila3, gbc);
        gbc.gridy++;

        JPanel fila4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fila4.setOpaque(false);
        JLabel lblTotal = new JLabel("Total: $" + Math.round(pedido.getTotal()));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        fila4.add(lblTotal);
        centro.add(fila4, gbc);

        JPanel der = new JPanel();
        der.setOpaque(false);
        der.setBorder(new EmptyBorder(10, 10, 10, 10));
        der.setPreferredSize(new Dimension(160, 170));
        der.setLayout(new BoxLayout(der, BoxLayout.Y_AXIS));

        EstadoPedido estado = pedido.getEstado();

        if (estado == EstadoPedido.Pendiente) {

            der.add(crearBotonAccion("Listo", () -> cambiarEstado(pedido, EstadoPedido.Listo)));
            der.add(Box.createVerticalStrut(10));
            der.add(crearBotonAccion("Entregado", () -> cambiarEstado(pedido, EstadoPedido.Entregado)));
            der.add(Box.createVerticalStrut(10));
            der.add(crearBotonAccion("Cancelar", () -> cambiarEstado(pedido, EstadoPedido.Cancelado)));

        } else if (estado == EstadoPedido.Listo) {

            der.add(crearBotonAccion("Entregado", () -> cambiarEstado(pedido, EstadoPedido.Entregado)));
            der.add(Box.createVerticalStrut(10));
            der.add(crearBotonAccion("Cancelar", () -> cambiarEstado(pedido, EstadoPedido.Cancelado)));

        } else {
            JLabel sin = new JLabel(" ");
            der.add(sin);
        }

        JPanel wrapIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 35, 0));
        wrapIzq.setOpaque(false);
        wrapIzq.add(izq);

        cont.add(wrapIzq, BorderLayout.WEST);
        cont.add(centro, BorderLayout.CENTER);
        cont.add(der, BorderLayout.EAST);

        return cont;
    }

    /**
     * <p>
     * Crea el panel superior de filtros (modo + campos + acciones).
     * </p>
     *
     * <p>
     * Incluye:
     * </p>
     * <ul>
     * <li>{@link #cbFiltro} con modos: Todos, Folio, Teléfono, Rango de
     * fechas.</li>
     * <li>Campos de entrada para folio, teléfono y fechas.</li>
     * <li>Botón Buscar que invoca {@link #refrescarConFiltros()}.</li>
     * <li>Botón Limpiar que reinicia entradas y llama
     * {@link #refrescar()}.</li>
     * </ul>
     *
     * @return panel de filtros listo para insertarse en la UI
     */
    private JPanel crearPanelFiltros() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        p.setOpaque(false);

        cbFiltro = new JComboBox<>(new String[]{"Todos", "Folio", "Teléfono", "Rango de fechas"});
        cbFiltro.setPreferredSize(new Dimension(140, 28));

        cbFiltro.setSelectedIndex(0);

        txtFolio = new JTextField();
        txtFolio.setPreferredSize(new Dimension(120, 28));

        txtTelefono = new JTextField();
        txtTelefono.setPreferredSize(new Dimension(120, 28));

        txtDesde = new JTextField();
        txtDesde.setPreferredSize(new Dimension(90, 28));
        txtDesde.setToolTipText("Formato: yyyy-MM-dd");

        txtHasta = new JTextField();
        txtHasta.setPreferredSize(new Dimension(90, 28));
        txtHasta.setToolTipText("Formato: yyyy-MM-dd");

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setPreferredSize(new Dimension(90, 28));

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setPreferredSize(new Dimension(90, 28));

        aplicarModoFiltro();

        cbFiltro.addActionListener(e -> aplicarModoFiltro());

        btnBuscar.addActionListener(e -> refrescarConFiltros());

        btnLimpiar.addActionListener(e -> {
            txtFolio.setText("");
            txtTelefono.setText("");
            txtDesde.setText("");
            txtHasta.setText("");
            cbFiltro.setSelectedIndex(0);
            aplicarModoFiltro();
            refrescar();
        });

        p.add(cbFiltro);
        p.add(new JLabel("Folio:"));
        p.add(txtFolio);
        p.add(new JLabel("Tel:"));
        p.add(txtTelefono);
        p.add(new JLabel("Desde:"));
        p.add(txtDesde);
        p.add(new JLabel("Hasta:"));
        p.add(txtHasta);
        p.add(btnBuscar);
        p.add(btnLimpiar);

        return p;
    }

    /**
     * <p>
     * Activa y limpia los campos de filtro de acuerdo con el modo seleccionado
     * en {@link #cbFiltro}.
     * </p>
     *
     * <ul>
     * <li><b>Todos</b>: habilita todos los campos.</li>
     * <li><b>Folio</b>: habilita folio y deshabilita/limpia los demás.</li>
     * <li><b>Teléfono</b>: habilita teléfono y deshabilita/limpia los
     * demás.</li>
     * <li><b>Rango de fechas</b>: habilita desde/hasta y deshabilita/limpia los
     * demás.</li>
     * </ul>
     */
    private void aplicarModoFiltro() {
        String modo = (String) cbFiltro.getSelectedItem();
        if (modo == null) {
            modo = "Todos";
        }

        boolean todos = "Todos".equals(modo);
        boolean folio = "Folio".equals(modo);
        boolean tel = "Teléfono".equals(modo);
        boolean fechas = "Rango de fechas".equals(modo);

        txtFolio.setEnabled(todos || folio);
        txtTelefono.setEnabled(todos || tel);
        txtDesde.setEnabled(todos || fechas);
        txtHasta.setEnabled(todos || fechas);

        if (!todos && !folio) {
            txtFolio.setText("");
        }
        if (!todos && !tel) {
            txtTelefono.setText("");
        }
        if (!todos && !fechas) {
            txtDesde.setText("");
            txtHasta.setText("");
        }
    }

    /**
     * <p>
     * Ejecuta la búsqueda de pedidos aplicando los filtros capturados en la UI.
     * </p>
     *
     * <p>
     * Reglas:
     * </p>
     * <ul>
     * <li>Si folio está vacío, se considera {@code null}.</li>
     * <li>Si teléfono está vacío, se considera {@code null}.</li>
     * <li>Para fechas, si se usa un valor, se requiere que ambos (Desde/Hasta)
     * estén presentes.</li>
     * <li>Si no se proporciona ningún filtro, invoca {@link #refrescar()}.</li>
     * </ul>
     *
     * <p>
     * Si hay filtros, consulta con
     * {@code ctx.getPedidoBO().listarPedidosFiltro(folio, telefono, desde, hasta)}
     * y repinta con {@link #pintarCards(java.util.List)}.
     * </p>
     */
    private void refrescarConFiltros() {
        try {
            String folio = txtFolio.getText().trim();
            if (folio.isEmpty()) {
                folio = null;
            }

            String telefono = txtTelefono.getText().trim();
            if (telefono.isEmpty()) {
                telefono = null;
            }

            LocalDate desde = null;
            LocalDate hasta = null;

            String d = txtDesde.getText().trim();
            String h = txtHasta.getText().trim();

            if (!d.isEmpty() || !h.isEmpty()) {
                if (d.isEmpty() || h.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Si usas rango de fechas, llena Desde y Hasta.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                desde = LocalDate.parse(d, f);
                hasta = LocalDate.parse(h, f);
            }

            if (folio == null && telefono == null && desde == null && hasta == null) {
                refrescar();
                return;
            }

            List<Pedido> pedidos = ctx.getPedidoBO().listarPedidosFiltro(folio, telefono, desde, hasta);
            pintarCards(pedidos);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al filtrar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * <p>
     * Pinta la lista de cards a partir de una lista de pedidos ya consultada
     * (filtrada o completa).
     * </p>
     *
     * <p>
     * Si la lista está vacía, muestra un mensaje informativo.
     * </p>
     *
     * @param pedidos lista de pedidos a renderizar
     */
    private void pintarCards(List<Pedido> pedidos) {
        listaCards.removeAll();

        if (pedidos == null || pedidos.isEmpty()) {
            JLabel vacio = new JLabel("No hay pedidos para mostrar.");
            vacio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            vacio.setForeground(new Color(60, 60, 60));
            vacio.setBorder(new EmptyBorder(10, 10, 10, 10));
            listaCards.add(vacio);
        } else {
            int n = 1;
            for (Pedido p : pedidos) {
                listaCards.add(construirCardPedido(p, n++));
                listaCards.add(Box.createVerticalStrut(14));
            }
        }

        listaCards.revalidate();
        listaCards.repaint();
    }

    /**
     * Crea un {@link JLabel} estándar para mostrar información dentro de una
     * card.
     *
     * @param txt texto a mostrar
     * @return label con estilo base (Segoe UI 13)
     */
    private JLabel lblInfo(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    /**
     * <p>
     * Crea un botón de acción para una card de pedido.
     * </p>
     *
     * <p>
     * El botón ejecuta {@link Runnable#run()} al hacer click.
     * </p>
     *
     * @param texto texto mostrado en el botón
     * @param onClick acción a ejecutar al presionar el botón
     * @return botón configurado para la UI de la pantalla
     */
    private JButton crearBotonAccion(String texto, Runnable onClick) {
        JButton b = new JButton(texto);
        b.setPreferredSize(new Dimension(120, 32));
        b.setMaximumSize(new Dimension(120, 32));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBackground(new Color(245, 245, 245));
        b.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 2, false),
                new EmptyBorder(2, 10, 2, 10)
        ));

        b.addActionListener(e -> onClick.run());
        return b;
    }

    /**
     * <p>
     * Cambia el estado de un pedido mostrando confirmación al usuario.
     * </p>
     *
     * <p>
     * Flujo general:
     * </p>
     * <ul>
     * <li>Solicita confirmación con
     * {@link JOptionPane#showConfirmDialog(java.awt.Component, java.lang.Object, java.lang.String, int)}.</li>
     * <li>Si se cancela, no realiza cambios.</li>
     * <li>Si el pedido es {@link PedidoExpress} y se intenta marcar
     * {@link EstadoPedido#Entregado}:</li>
     * <li style="list-style-type: none;">
     * <ul>
     * <li>Solicita PIN con
     * {@link JOptionPane#showInputDialog(java.awt.Component, java.lang.Object, java.lang.String, int)}.</li>
     * <li>Valida el PIN mediante
     * {@code ctx.getPedidoBO().entregarPedidoExpressConPin(pedido.getId(), pinCapturado)}.</li>
     * <li>En éxito, invoca {@link #refrescar()}.</li>
     * </ul>
     * </li>
     * <li>Para otros casos, actualiza con
     * {@code ctx.getPedidoBO().actualizarEstadoPedido(pedido.getId(), nuevoEstado)}
     * y refresca.</li>
     * </ul>
     *
     * @param pedido pedido a modificar
     * @param nuevoEstado nuevo estado a asignar
     */
    private void cambiarEstado(Pedido pedido, EstadoPedido nuevoEstado) {

        int r = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas cambiar el estado a \"" + mostrarEnumBonito(nuevoEstado) + "\"?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (r != JOptionPane.YES_OPTION) {
            return;
        }

        if (pedido instanceof PedidoExpress && nuevoEstado == EstadoPedido.Entregado) {

            String pinCapturado = JOptionPane.showInputDialog(
                    this,
                    "Ingresa el PIN del pedido para marcarlo como entregado:",
                    "Confirmar entrega (Express)",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (pinCapturado == null) {
                return;
            }

            pinCapturado = pinCapturado.trim();
            if (pinCapturado.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El PIN es obligatorio.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                ctx.getPedidoBO().entregarPedidoExpressConPin(pedido.getId(), pinCapturado);
                refrescar();
            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        try {
            ctx.getPedidoBO().actualizarEstadoPedido(pedido.getId(), nuevoEstado);
            refrescar();
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Construye el nombre completo de un cliente usando nombres y apellidos,
     * con manejo de nulos.
     *
     * @param c cliente del cual se obtiene el nombre
     * @return nombre completo o "N/A" si no hay texto resultante
     */
    private String nombreCliente(Cliente c) {
        String nom = safe(c.getNombres());
        String ap = safe(c.getApellidoPaterno());
        String am = safe(c.getApellidoMaterno());
        String full = (nom + " " + ap + " " + am).trim();
        return full.isEmpty() ? "N/A" : full;
    }

    /**
     * Devuelve una cadena segura (no nula). Si {@code s} es {@code null},
     * regresa cadena vacía.
     *
     * @param s texto de entrada
     * @return texto no nulo
     */
    private String safe(String s) {
        return (s == null) ? "" : s;
    }

    /**
     * Formatea un {@link Enum} para mostrarlo de forma legible en UI.
     *
     * <p>
     * Convierte {@code NAME_WITH_UNDERSCORES} a {@code NAME WITH UNDERSCORES}.
     * Si el valor es {@code null}, regresa "N/A".
     * </p>
     *
     * @param e enum a mostrar
     * @return texto formateado para UI
     */
    private String mostrarEnumBonito(Enum<?> e) {
        if (e == null) {
            return "N/A";
        }
        return e.name().replace("_", " ");
    }
}
