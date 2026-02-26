/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dominio.PedidoExpress;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <h1>PantallaPedidoExpressRealizado</h1>
 *
 * <p>
 * Pantalla de confirmación final para el flujo <b>Express</b>, mostrada después
 * de registrar correctamente un {@link PedidoExpress}. Presenta al cliente los
 * datos clave del pedido (folio, PIN, total, fecha y método de pago) y un aviso
 * de tiempo límite para recoger.
 * </p>
 *
 * <p>
 * La UI presenta:
 * </p>
 * <ul>
 * <li>Encabezado con título <b>Panadería</b> y etiqueta <b>EXPRESS</b>.</li>
 * <li>Mensaje de éxito: "Pedido realizado correctamente".</li>
 * <li>Aviso con ícono 🔔 indicando 20 minutos para recoger el pedido.</li>
 * <li>Cajas informativas centradas con: Folio, PIN, Total, Fecha y Método de
 * pago.</li>
 * <li>Botón <b>Listo</b> que regresa a {@link Menu}.</li>
 * <li>Footer informativo.</li>
 * </ul>
 *
 * <h2>Obtención de datos</h2>
 * <p>
 * Para poblar los campos, se leen propiedades del pedido usando reflexión
 * mediante {@link #getProp(java.lang.Object, java.lang.String...)} y
 * {@link #getStringProp(java.lang.Object, java.lang.String...)}. Esto permite
 * tolerancia si el modelo cambia nombres de getters (por ejemplo,
 * {@code getFolio}, {@code getFolioPedido}, etc.).
 * </p>
 *
 * <h2>Formato</h2>
 * <ul>
 * <li>El total se formatea con {@link #formatearDinero(java.lang.Object)}.</li>
 * <li>La fecha se formatea con {@link #formatearFecha(java.lang.Object)}.</li>
 * </ul>
 *
 * @author
 */
public class PantallaPedidoExpressRealizado extends JFrame {

    /**
     * Pedido express que fue registrado y cuyos datos se mostrarán en pantalla.
     */
    private final PedidoExpress pedido;

    /**
     * <p>
     * Constructor de la pantalla final de "pedido express realizado".
     * </p>
     *
     * <p>
     * Construye la UI con:
     * </p>
     * <ul>
     * <li>Fondo beige y tarjeta blanca con borde.</li>
     * <li>Encabezado con etiqueta EXPRESS y mensajes de confirmación.</li>
     * <li>Cajas de datos (Folio, PIN, Total, Fecha, Método de pago).</li>
     * <li>Botón "Listo" para volver a {@link Menu}.</li>
     * </ul>
     *
     * <p>
     * Para obtener valores del {@link PedidoExpress}, utiliza reflexión para
     * buscar getters comunes (folio, total, fecha y método de pago) y aplica
     * formato donde corresponde.
     * </p>
     *
     * @param pedido pedido express a mostrar
     * @param ctx contexto global para navegar a {@link Menu}
     * @param pin pin generado/capturado para mostrar al cliente
     */
    public PantallaPedidoExpressRealizado(PedidoExpress pedido, AppContext ctx, String pin) {
        this.pedido = pedido;

        setTitle("Panadería - Pedido Express realizado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 700);
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
        card.setPreferredSize(new Dimension(860, 620));
        root.add(card);

        // ----- parte de arriba -----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelIzq.setOpaque(false);

        topBar.add(panelIzq, BorderLayout.WEST);
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        // ----- titulo -----
        JLabel lblTitle = new JLabel("Panadería");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 56));

        JLabel lblExpress = new JLabel("EXPRESS");
        lblExpress.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblExpress.setForeground(new Color(200, 60, 60));
        lblExpress.setBorder(new EmptyBorder(0, 6, 0, 0));

        JPanel titleRow = new JPanel();
        titleRow.setOpaque(false);
        titleRow.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        titleRow.add(lblTitle);
        titleRow.add(lblExpress);

        JLabel lblSub1 = new JLabel("Pedido realizado correctamente");
        lblSub1.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        lblSub1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel avisoRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        avisoRow.setOpaque(false);
        JLabel bell = new JLabel("🔔");
        bell.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        JLabel aviso = new JLabel("Cuenta con 20 minutos para recoger su pedido");
        aviso.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        avisoRow.add(bell);
        avisoRow.add(aviso);

        header.add(Box.createVerticalStrut(6));
        header.add(titleRow);
        header.add(Box.createVerticalStrut(10));
        header.add(lblSub1);
        header.add(Box.createVerticalStrut(10));
        header.add(avisoRow);
        header.add(Box.createVerticalStrut(18));

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(topBar, BorderLayout.NORTH);
        north.add(header, BorderLayout.CENTER);

        card.add(north, BorderLayout.NORTH);

        // ----- parte central -----
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        String folio = getStringProp(pedido, "getFolio", "getFolioPedido", "getCodigoFolio", "getNumeroFolio");
        Object totalObj = getProp(pedido, "getTotal", "getTotalPedido", "getMontoTotal", "getImporteTotal");
        String total = formatearDinero(totalObj);
        Object fechaObj = getProp(pedido, "getFecha", "getFechaCreacion", "getFechaRegistro", "getFechaHoraCreacion", "getFechaHora");
        String fecha = formatearFecha(fechaObj);

        Object mpObj = getProp(pedido, "getMetodoPago", "getMetodoDePago", "getMetodo");
        String metodoPago = (mpObj == null) ? "N/A" : String.valueOf(mpObj);

        center.add(crearCaja("Folio: " + (folio == null ? "N/A" : folio)));
        center.add(Box.createVerticalStrut(10));
        center.add(crearCaja("PIN: " + (pin == null ? "N/A" : pin)));
        center.add(Box.createVerticalStrut(10));
        center.add(crearCaja("Total: " + total));
        center.add(Box.createVerticalStrut(10));
        center.add(crearCaja("Fecha: " + fecha));
        center.add(Box.createVerticalStrut(10));
        center.add(crearCaja("Método de pago: " + metodoPago));

        JPanel wrapCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapCenter.setOpaque(false);
        wrapCenter.add(center);

        card.add(wrapCenter, BorderLayout.CENTER);

        // ----- parte de anajo -----
        /**
         * Botón final para cerrar el flujo; abre {@link Menu} y cierra la
         * pantalla actual.
         */
        JButton btnListo = new JButton("Listo");
        btnListo.setPreferredSize(new Dimension(160, 34));
        btnListo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnListo.setFocusPainted(false);
        btnListo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnListo.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        btnListo.setBackground(new Color(245, 245, 245));

        btnListo.addActionListener(e -> {
            new Menu(ctx).setVisible(true);
            dispose();

        });

        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
        panelBtn.setOpaque(false);
        panelBtn.add(btnListo);

        JLabel footer = new JLabel("© 2026 Panadería. Todos los derechos reservados.");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(80, 80, 80));

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.add(footer, BorderLayout.WEST);

        JPanel south = new JPanel();
        south.setOpaque(false);
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.add(panelBtn);
        south.add(footerPanel);

        card.add(south, BorderLayout.SOUTH);
    }

    /**
     * <p>
     * Crea una caja visual (panel con borde) para mostrar una línea de
     * información centrada.
     * </p>
     *
     * @param texto texto a mostrar dentro de la caja
     * @return panel con estilo de "caja" para mostrar información
     */
    private JPanel crearCaja(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel caja = new JPanel(new BorderLayout());
        caja.setBackground(new Color(245, 245, 245));
        caja.setBorder(new LineBorder(new Color(60, 60, 60), 2));
        caja.setPreferredSize(new Dimension(340, 42));
        caja.setMaximumSize(new Dimension(340, 42));
        caja.add(lbl, BorderLayout.CENTER);

        return caja;
    }

    /**
     * <p>
     * Obtiene el valor de una propiedad invocando por reflexión el primer
     * getter existente dentro de la lista {@code methodNames}.
     * </p>
     *
     * <p>
     * Si ningún método existe o hay error en la invocación, regresa
     * {@code null}.
     * </p>
     *
     * @param obj objeto sobre el cual se intentan invocar métodos
     * @param methodNames posibles nombres de método (getters) a probar en orden
     * @return valor retornado por el getter encontrado o {@code null}
     */
    // para darle valor a las cajas
    private static Object getProp(Object obj, String... methodNames) {
        if (obj == null) {
            return null;
        }
        for (String mName : methodNames) {
            try {
                Method m = obj.getClass().getMethod(mName);
                return m.invoke(obj);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * <p>
     * Variante de {@link #getProp(java.lang.Object, java.lang.String...)} que
     * devuelve el resultado como {@link String}.
     * </p>
     *
     * @param obj objeto objetivo
     * @param methodNames posibles getters a intentar
     * @return valor como string o {@code null} si no se pudo obtener
     */
    private static String getStringProp(Object obj, String... methodNames) {
        Object v = getProp(obj, methodNames);
        return (v == null) ? null : String.valueOf(v);
    }

    /**
     * <p>
     * Formatea un total monetario proveniente de diferentes tipos:
     * </p>
     * <ul>
     * <li>{@link BigDecimal}: redondea a 0 decimales.</li>
     * <li>{@link Number}: redondea con {@link Math#round(double)}.</li>
     * <li>{@link String}: normaliza agregando '$' si no existe.</li>
     * </ul>
     *
     * @param totalObj valor del total a formatear
     * @return total con prefijo '$' y formato simple
     */
    private static String formatearDinero(Object totalObj) {
        if (totalObj == null) {
            return "$0";
        }
        if (totalObj instanceof BigDecimal bd) {
            return "$" + bd.setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
        }
        if (totalObj instanceof Number n) {
            return "$" + Math.round(n.doubleValue());
        }
        // si viene como String "54" o "$54"
        String s = String.valueOf(totalObj).trim();
        if (s.startsWith("$")) {
            return s;
        }
        return "$" + s.replaceAll("[^0-9.]", "");
    }

    /**
     * <p>
     * Formatea una fecha proveniente de diferentes tipos:
     * </p>
     * <ul>
     * <li>{@link LocalDate}: formato {@code dd/MM/yyyy}.</li>
     * <li>{@link LocalDateTime}: formato {@code dd/MM/yyyy}.</li>
     * <li>{@link Date}: formato {@code dd/MM/yyyy}.</li>
     * </ul>
     *
     * <p>
     * Si no se puede formatear por tipo o excepción, devuelve
     * {@code String.valueOf(fechaObj)}.
     * </p>
     *
     * @param fechaObj objeto fecha a formatear
     * @return fecha formateada o "N/A" si {@code fechaObj} es {@code null}
     */
    private static String formatearFecha(Object fechaObj) {
        if (fechaObj == null) {
            return "N/A";
        }

        try {
            if (fechaObj instanceof LocalDate ld) {
                return ld.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
            if (fechaObj instanceof LocalDateTime ldt) {
                return ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
            if (fechaObj instanceof Date d) {
                return new SimpleDateFormat("dd/MM/yyyy").format(d);
            }
        } catch (Exception ignored) {
        }

        // fallback
        return String.valueOf(fechaObj);
    }
}
