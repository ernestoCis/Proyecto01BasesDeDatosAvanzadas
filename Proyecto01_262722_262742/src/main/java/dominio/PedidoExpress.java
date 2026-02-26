package dominio;

import java.time.LocalDateTime;

/**
 * <b>Clase que representa un Pedido Express en la panadería.</b>
 * <p>Esta clase hereda de <code>Pedido</code> y extiende su funcionalidad para 
 * manejar órdenes de vía rápida.</p>
 * <ul>
 * <li>Utiliza los atributos generales de cualquier pedido (fechas, total, estado).</li>
 * <li>Agrega mecanismos de seguridad y control rápido mediante un <b>PIN</b> y un <b>Folio</b>.</li>
 * </ul>
 *
 * @author 262722
 * @author 262742
 */
public class PedidoExpress extends Pedido {

    private String pin;
    private String folio;

    /**
     * Constructor por defecto de la clase <b>PedidoExpress</b>.
     * <p>Crea una instancia vacía sin inicializar sus atributos ni los de su clase padre.</p>
     */
    public PedidoExpress() {
    }

    /**
     * Constructor que inicializa todos los atributos de un pedido exprés.
     * <p>Llama al constructor de su clase padre (<code>Pedido</code>) usando <code>super()</code>
     * para establecer la información general, y luego inicializa sus propios atributos de seguridad.</p>
     *
     * @param id El identificador único del pedido en la base de datos (heredado).
     * @param estado El estado actual de la orden (heredado).
     * @param fechaCreacion La fecha y hora exacta en la que se realizó el pedido (heredada).
     * @param fechaEntrega La fecha y hora programada o real de entrega (heredada).
     * @param metodoPago La forma de pago utilizada por el cliente (heredada).
     * @param total El importe total a cobrar (heredado).
     * @param numeroPedido El número consecutivo o visible del ticket (heredado).
     * @param cliente El objeto <code>Cliente</code> que realizó la compra (heredado).
     * @param pin Un código de seguridad corto para autorizar la entrega rápida del pedido.
     * @param folio Un código alfanumérico o identificador único para el seguimiento del pedido exprés.
     */
    public PedidoExpress(int id, EstadoPedido estado, LocalDateTime fechaCreacion, LocalDateTime fechaEntrega, MetodoPago metodoPago, float total, int numeroPedido, Cliente cliente, String pin, String folio) {
        super(id, estado, fechaCreacion, fechaEntrega, metodoPago, total, numeroPedido, cliente);
        this.pin = pin;
        this.folio = folio;
    }

    /**
     * Obtiene el código PIN de seguridad del pedido exprés.
     * @return Un <code>String</code> con el PIN asignado.
     */
    public String getPin() {
        return pin;
    }

    /**
     * Establece el código PIN de seguridad para autorizar la entrega.
     * @param pin El código PIN a asignar.
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * Obtiene el folio de seguimiento del pedido exprés.
     * @return Un <code>String</code> con el folio correspondiente.
     */
    public String getFolio() {
        return folio;
    }

    /**
     * Establece el folio de seguimiento para este pedido.
     * @param folio El código alfanumérico a asignar como folio.
     */
    public void setFolio(String folio) {
        this.folio = folio;
    }
}