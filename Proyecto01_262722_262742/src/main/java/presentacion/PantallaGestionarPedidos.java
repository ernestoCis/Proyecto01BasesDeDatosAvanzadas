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
import java.time.format.DateTimeFormatter;
import java.util.List;
import negocio.Excepciones.NegocioException;

/**
 *
 * @author
 */
public class PantallaGestionarPedidos extends JFrame {

    private final AppContext ctx;

    private JPanel listaCards;       // Contenedor vertical
    private JScrollPane scroll;

    public PantallaGestionarPedidos(AppContext ctx) {
        this.ctx = ctx;

        setTitle("Panadería - Pedidos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(920, 620);
        setLocationRelativeTo(null);

        // Fondo beige
        JPanel base = new JPanel(new GridBagLayout());
        base.setBackground(new Color(214, 186, 150));
        setContentPane(base);

        // Tarjeta blanca
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(30, 30, 30), 2, false),
                new EmptyBorder(18, 22, 18, 22)
        ));
        card.setPreferredSize(new Dimension(860, 540));

        base.add(card, new GridBagConstraints());

        // ===== NORTH (flecha + titulo) =====
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

        // (placeholder del filtro)
        JLabel lblFiltrar = new JLabel("Filtrar");
        lblFiltrar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFiltrar.setForeground(new Color(40, 40, 40));

        topBar.add(btnBack, BorderLayout.WEST);
        topBar.add(lblFiltrar, BorderLayout.EAST);

        JLabel titulo = new JLabel("Panadería");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 58));

        JLabel pedidos = new JLabel("Pedidos");
        pedidos.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pedidos.setAlignmentX(Component.LEFT_ALIGNMENT);

        north.add(topBar);
        north.add(Box.createVerticalStrut(10));
        north.add(titulo);
        north.add(Box.createVerticalStrut(6));
        north.add(pedidos);
        north.add(Box.createVerticalStrut(12));

        card.add(north, BorderLayout.NORTH);

        // ===== CENTER (scroll cards) =====
        listaCards = new JPanel();
        listaCards.setOpaque(false);
        listaCards.setLayout(new BoxLayout(listaCards, BoxLayout.Y_AXIS));

        scroll = new JScrollPane(listaCards);
        scroll.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        card.add(scroll, BorderLayout.CENTER);

        // ===== SOUTH (footer) =====
        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.add(footer, BorderLayout.WEST);

        card.add(south, BorderLayout.SOUTH);

        // Cargar pedidos
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
                    JPanel cardPedido = construirCardPedido(p, n++);
                    listaCards.add(cardPedido);
                    listaCards.add(Box.createVerticalStrut(14));
                }
            }

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        listaCards.revalidate();
        listaCards.repaint();
    }

    private JPanel construirCardPedido(Pedido pedido, int numeroVisual) {

        JPanel cont = new JPanel(new BorderLayout());
        cont.setOpaque(true);
        cont.setBackground(new Color(245, 245, 245));
        cont.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        cont.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        cont.setPreferredSize(new Dimension(760, 170));

        // ===== Columna izquierda: info =====
        JPanel izq = new JPanel();
        izq.setOpaque(false);
        izq.setBorder(new EmptyBorder(10, 10, 10, 10));
        izq.setLayout(new BoxLayout(izq, BoxLayout.Y_AXIS));
        izq.setPreferredSize(new Dimension(260, 170));

        // Título pedido + (EXPRESS)
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
            izq.add(lblInfo("PIN: " + pe.getPin()));
        }

        izq.add(lblInfo("Número de pedido: " + pedido.getNumeroPedido()));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy");
        String fecha = (pedido.getFechaCreacion() != null)
                ? pedido.getFechaCreacion().format(fmt)
                : "N/A";
        izq.add(lblInfo("Fecha: " + fecha));

        izq.add(lblInfo("Estado: " + pedido.getEstado().name().replace("_", " ")));

        // Cupón
        if (pedido instanceof PedidoProgramado pp) {
            izq.add(lblInfo("Cupón: " + (pp.getCupon() == null ? "N/A" : ("#" + pp.getCupon().getId()))));
        }

        // ===== Columna centro: detalles =====
        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setBorder(new EmptyBorder(10, 10, 10, 10));
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        float total = 0;

        try {
            List<DetallePedido> detalles = ctx.getDetallePedidoBO().listarDetallesPorPedido(pedido.getId());

            if (detalles == null || detalles.isEmpty()) {
                centro.add(lblInfo("Sin detalles."));
            } else {
                for (DetallePedido d : detalles) {
                    String nombreProd = (d.getProducto() != null) ? d.getProducto().getNombre() : "Producto";
                    String linea = d.getCantidad() + " " + nombreProd;

                    JLabel l = new JLabel(linea + "  ..........  $" + Math.round(d.getSubtotal()));
                    l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    centro.add(l);

                    if (d.getNota() != null && !d.getNota().trim().isEmpty()) {
                        JLabel nota = new JLabel("Nota: " + d.getNota().trim());
                        nota.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                        nota.setForeground(new Color(70, 70, 70));
                        nota.setBorder(new EmptyBorder(0, 20, 4, 0));
                        centro.add(nota);
                    }

                    total += d.getSubtotal();
                }
            }

        } catch (NegocioException ex) {
            centro.add(lblInfo("Error cargando detalles."));
        }

        // ===== Columna derecha: acciones =====
        JPanel der = new JPanel();
        der.setOpaque(false);
        der.setBorder(new EmptyBorder(10, 10, 10, 10));
        der.setPreferredSize(new Dimension(160, 170));
        der.setLayout(new BoxLayout(der, BoxLayout.Y_AXIS));

        // Botones según estado (funcional)
        EstadoPedido estado = pedido.getEstado();

        if (estado == EstadoPedido.Pendiente || estado == EstadoPedido.Pendiente /* por si tu enum es Pendiente */) {
            // Nota: esta línea extra no cambia nada, solo evita confusión si tu IDE autocompleta raro.
        }

        if (estado == EstadoPedido.Pendiente) {
            der.add(crearBotonAccion("Listo", () -> cambiarEstado(pedido.getId(), EstadoPedido.Listo)));
            der.add(Box.createVerticalStrut(10));
            der.add(crearBotonAccion("Entregado", () -> cambiarEstado(pedido.getId(), EstadoPedido.Entregado)));
            der.add(Box.createVerticalStrut(10));
            der.add(crearBotonAccion("Cancelar", () -> cambiarEstado(pedido.getId(), EstadoPedido.Cancelado)));
        } else if (estado == EstadoPedido.Listo) {
            der.add(crearBotonAccion("Entregado", () -> cambiarEstado(pedido.getId(), EstadoPedido.Entregado)));
            der.add(Box.createVerticalStrut(10));
            der.add(crearBotonAccion("Cancelar", () -> cambiarEstado(pedido.getId(), EstadoPedido.Cancelado)));
        } else {
            // Entregado/Cancelado/No_reclamado -> sin acciones
            JLabel sin = new JLabel(" ");
            der.add(sin);
        }

        // Total abajo
        JLabel lblTotal = new JLabel("Total: $" + Math.round(total));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel wrapTotal = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapTotal.setOpaque(false);
        wrapTotal.add(lblTotal);

        // Armado final (3 columnas)
        cont.add(izq, BorderLayout.WEST);
        cont.add(new JSeparator(SwingConstants.VERTICAL), BorderLayout.CENTER); // separador (visual simple)
        cont.add(centro, BorderLayout.CENTER);

        // Para que se vea 3 columnas reales sin pelearse con BorderLayout:
        JPanel centroDer = new JPanel(new BorderLayout());
        centroDer.setOpaque(false);
        centroDer.add(centro, BorderLayout.CENTER);
        centroDer.add(der, BorderLayout.EAST);

        cont.add(izq, BorderLayout.WEST);
        cont.add(centroDer, BorderLayout.CENTER);

        // Línea inferior con total (en la izquierda como storyboard)
        izq.add(Box.createVerticalStrut(6));
        JLabel totalLabel = new JLabel("Total: $" + Math.round(total));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        izq.add(totalLabel);

        return cont;
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

    private void cambiarEstado(int idPedido, EstadoPedido nuevoEstado) {
        int r = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas cambiar el estado a \"" + nuevoEstado.name().replace("_", " ") + "\"?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (r != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            ctx.getPedidoBO().actualizarEstadoPedido(idPedido, nuevoEstado);
            refrescar();
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
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
}