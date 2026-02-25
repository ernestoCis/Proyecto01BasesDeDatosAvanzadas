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

public class PantallaHistorialPedidosCliente extends JFrame {

    private final JFrame pantallaAnterior;
    private final AppContext ctx;
    private final Cliente clienteActual;
    private JLabel lblFiltrar;
    private JButton btnFiltrar;
    private JButton btnBack;

    private JPanel contenedorCards;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy");

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

        JButton btnBack = new JButton("←");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            if (pantallaAnterior != null) pantallaAnterior.setVisible(true);
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

        btnFiltrar = new JButton("⚙");
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.setBorderPainted(false);
        btnFiltrar.setContentAreaFilled(false);
        btnFiltrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFiltrar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        btnFiltrar.addActionListener(e -> abrirDialogoFiltro());

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
            izquierda.add(labelInfo("Subtotal: $" + ctx.getDetallePedidoBO().obtenerSubtotalPedido(pp.getId()) ));

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
            izquierda.add(labelInfo("PIN: " + pe.getPin()));
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

    private JLabel labelInfo(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    // Formato: "2 Concha............. $32"
    private JLabel labelDetalle(DetallePedido d) {
        String nombre = (d.getProducto() != null) ? d.getProducto().getNombre() : "Producto";
        String izquierda = d.getCantidad() + " " + nombre;
        String derecha = "$" + redondear(d.getSubtotal());

        String linea = rellenarPuntos(izquierda, derecha, 60);

        JLabel l = new JLabel(linea);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return l;
    }

    private String rellenarPuntos(String left, String right, int ancho) {
        String base = left;
        if (base.length() > ancho) base = base.substring(0, ancho);

        StringBuilder sb = new StringBuilder(base);
        while (sb.length() < ancho) sb.append('.');
        sb.append(" ").append(right);
        return sb.toString();
    }

    private void cancelarPedido(Pedido pedido) {
        int r = JOptionPane.showConfirmDialog(
                this,
                "¿Cancelar este pedido?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (r != JOptionPane.YES_OPTION) return;

        try {
            ctx.getPedidoBO().actualizarEstadoPedido(pedido.getId(), EstadoPedido.Cancelado);
            JOptionPane.showMessageDialog(this, "Pedido cancelado.");
            cargarPedidos();
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String redondear(float v) {
        return String.valueOf(Math.round(v));
    }
    
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

    private LocalDate parseFecha(String s) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(s, fmt);
    }
}