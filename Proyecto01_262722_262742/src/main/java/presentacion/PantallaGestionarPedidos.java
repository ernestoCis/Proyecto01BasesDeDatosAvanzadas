/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import presentacion.AppContext;
import javax.swing.*;
import java.awt.*;
import negocio.Excepciones.NegocioException;

import dominio.Cliente;
import dominio.EstadoPedido;
import dominio.Pedido;
import dominio.PedidoExpress;
import dominio.PedidoProgramado;
import dominio.MetodoPago;
import java.time.LocalDate;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.time.format.DateTimeFormatter;
import java.util.List;
import negocio.util.PasswordUtil;

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
        setSize(920, 620);
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
        card.setPreferredSize(new Dimension(860, 540));

        base.add(card, new GridBagConstraints());

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

        topBar.add(crearPanelFiltros(), BorderLayout.EAST);

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

        listaCards = new JPanel();
        listaCards.setOpaque(false);
        listaCards.setLayout(new BoxLayout(listaCards, BoxLayout.Y_AXIS));

        scroll = new JScrollPane(listaCards);
        scroll.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        card.add(scroll, BorderLayout.CENTER);

        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.add(footer, BorderLayout.WEST);

        card.add(south, BorderLayout.SOUTH);

        // Cargar pedidos al abrir
        refrescar();
    }

    /**
     * Consulta pedidos en BD y reconstruye la lista de cards. Lo hacemos así
     * para que después de un cambio de estado, se vea el cambio al instante.
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
     * Construye una card visual por pedido.
     */
    private JPanel construirCardPedido(Pedido pedido, int numeroVisual) {

        JPanel cont = new JPanel(new BorderLayout(10, 0));
        cont.setOpaque(true);
        cont.setBackground(new Color(245, 245, 245));
        cont.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        cont.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        cont.setPreferredSize(new Dimension(760, 170));

        // info
        JPanel izq = new JPanel();
        izq.setOpaque(false);
        izq.setBorder(new EmptyBorder(10, 10, 10, 10));
        izq.setLayout(new BoxLayout(izq, BoxLayout.Y_AXIS));
        izq.setPreferredSize(new Dimension(300, 170));

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

        // Tipo
        String tipo = (pedido instanceof PedidoExpress) ? "Express" : "Programado";
        izq.add(lblInfo("Tipo: " + tipo));

        // Cliente
        Cliente c = pedido.getCliente();
        if (c != null) {
            izq.add(lblInfo("Cliente: " + nombreCliente(c)));
        } else {
            izq.add(lblInfo("Cliente: N/A"));
        }

        // Express
        if (pedido instanceof PedidoExpress pe) {
            izq.add(lblInfo("Folio: " + safe(pe.getFolio())));
        }

        izq.add(lblInfo("No. pedido: " + pedido.getNumeroPedido()));
        izq.add(lblInfo("Estado: " + mostrarEnumBonito(pedido.getEstado())));

        // Cupón 
        if (pedido instanceof PedidoProgramado pp) {
            izq.add(lblInfo("Cupón: " + (pp.getCupon() == null ? "N/A" : ("#" + pp.getCupon().getId()))));
        }

        // datos 
        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setBorder(new EmptyBorder(10, 10, 10, 10));
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy  HH:mm");

        String fechaCreacion = (pedido.getFechaCreacion() != null) ? pedido.getFechaCreacion().format(fmt) : "N/A";
        String fechaEntrega = (pedido.getFechaEntrega() != null) ? pedido.getFechaEntrega().format(fmt) : "N/A";

        MetodoPago mp = pedido.getMetodoPago();
        String metodoPago = (mp != null) ? mp.toString() : "N/A";

        centro.add(lblInfo("Creación: " + fechaCreacion));
        centro.add(Box.createVerticalStrut(6));
        centro.add(lblInfo("Entrega: " + fechaEntrega));
        centro.add(Box.createVerticalStrut(6));
        centro.add(lblInfo("Método de pago: " + metodoPago));
        centro.add(Box.createVerticalStrut(10));

        // Total 
        JLabel lblTotal = new JLabel("Total: $" + Math.round(pedido.getTotal()));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 15));
        centro.add(lblTotal);

        // acciones
        JPanel der = new JPanel();
        der.setOpaque(false);
        der.setBorder(new EmptyBorder(10, 10, 10, 10));
        der.setPreferredSize(new Dimension(160, 170));
        der.setLayout(new BoxLayout(der, BoxLayout.Y_AXIS));

        // Botones para según que estado 
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

        cont.add(izq, BorderLayout.WEST);
        cont.add(centro, BorderLayout.CENTER);
        cont.add(der, BorderLayout.EAST);

        return cont;
    }

    /**
     * Panel superior de filtros (como storyboard): el combo define qué campo se
     * usa y solo se habilita lo que toca para evitar confusión.
     */
    private JPanel crearPanelFiltros() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        p.setOpaque(false);

        cbFiltro = new JComboBox<>(new String[]{"Todos", "Folio", "Teléfono", "Rango de fechas"});
        cbFiltro.setPreferredSize(new Dimension(140, 28));

        cbFiltro.setSelectedIndex(0); // Inicia en "Todos" 

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

        // Estado inicial
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
            refrescar(); // vuelve a listar todo
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

    /**
     * Aplica el filtro seleccionado y vuelve a pintar las cards.
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
            // Si no escribió nada, entonces muestra todos
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

    /**
     * Cambia el estado de un pedido. Caso especial: - Si el pedido es EXPRESS y
     * se quiere marcar como ENTREGADO: pedimos el PIN al usuario y lo validamos
     * contra el hash guardado en BD.
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

        // Express Entregado 
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

        // Ya validado, actualizamos
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
        // En tu BD tienes cosas como "No reclamado", en Java lo manejas como No_reclamado,
        // entonces aquí lo “boniteamos” para UI.
        return e.name().replace("_", " ");
    }
}
