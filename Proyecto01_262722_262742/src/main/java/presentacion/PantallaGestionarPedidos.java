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
 * Pantalla para que el empleado gestione pedidos. - Muestra todos los pedidos
 * (programados y express). - Permite cambiar estado (Listo / Entregado /
 * Cancelado) según el estado actual. - Cuando se intenta marcar "Entregado" en
 * un pedido express, se pide el PIN y se valida.
 *
 * @author Isaac
 */
public class PantallaGestionarPedidos extends JFrame {

    private final AppContext ctx;

    private JPanel listaCards;
    private JScrollPane scroll;
    private JComboBox<String> cbFiltro;
    private JTextField txtFolio;
    private JTextField txtTelefono;
    private JTextField txtDesde;
    private JTextField txtHasta;

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

    private JLabel lblInfo(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

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

    private String nombreCliente(Cliente c) {
        String nom = safe(c.getNombres());
        String ap = safe(c.getApellidoPaterno());
        String am = safe(c.getApellidoMaterno());
        String full = (nom + " " + ap + " " + am).trim();
        return full.isEmpty() ? "N/A" : full;
    }

    private String safe(String s) {
        return (s == null) ? "" : s;
    }

    private String mostrarEnumBonito(Enum<?> e) {
        if (e == null) {
            return "N/A";
        }
        return e.name().replace("_", " ");
    }
}
