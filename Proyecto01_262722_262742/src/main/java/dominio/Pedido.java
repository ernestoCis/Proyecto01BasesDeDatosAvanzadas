package dominio;

import java.time.LocalDateTime;

/**
 * <b>Clase abstracta que representa un Pedido general en la panadería.</b>
 * <p>Esta clase centraliza toda la información de una orden de compra. Al ser abstracta,
 * sirve como base o "plantilla" para otros tipos de pedidos más específicos.</p>
 * <ul>
 * <li>Registra las fechas clave (creación y entrega).</li>
 * <li>Controla el estado actual del pedido y cómo se va a pagar.</li>
 * <li>Calcula o almacena el costo total y el cliente asociado.</li>
 * </ul>
 *
 * @author 262722
 * @author 262742
 */
public abstract class Pedido {

    private int id;
    private EstadoPedido estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEntrega;
    private MetodoPago metodoPago;
    private float total;
    private int numeroPedido;
    private Cliente cliente;

    /**
     * Constructor por defecto de la clase <b>Pedido</b>.
     * <p>Crea una instancia vacía sin inicializar sus atributos.</p>
     */
    public Pedido() {
    }

    /**
     * Constructor que inicializa todos los atributos principales de un pedido.
     *
     * @param id El identificador único del pedido en la base de datos.
     * @param estado El estado actual de la orden (ej. Pendiente, Listo, Entregado).
     * @param fechaCreacion La fecha y hora exacta en la que el cliente realizó el pedido.
     * @param fechaEntrega La fecha y hora programada o en la que se entregó el pedido.
     * @param metodoPago La forma en la que el cliente va a liquidar el total (Efectivo, Tarjeta, etc.).
     * @param total El importe total a cobrar por todos los productos del pedido.
     * @param numeroPedido Un número consecutivo o folio visible para el cliente y el ticket.
     * @param cliente El objeto <code>Cliente</code> que realizó la compra.
     */
    public Pedido(int id, EstadoPedido estado, LocalDateTime fechaCreacion, LocalDateTime fechaEntrega, MetodoPago metodoPago, float total, int numeroPedido, Cliente cliente) {
        this.id = id;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrega = fechaEntrega;
        this.metodoPago = metodoPago;
        this.total = total;
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
    }

    /**
     * Obtiene el identificador único del pedido.
     * @return Un número entero con el <code>id</code>.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el estado actual del pedido.
     * @return Un objeto <code>EstadoPedido</code> representando la fase de la orden.
     */
    public EstadoPedido getEstado() {
        return estado;
    }

    /**
     * Obtiene la fecha y hora en la que se creó el pedido.
     * @return Un objeto <code>LocalDateTime</code> de la creación.
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Obtiene la fecha y hora de entrega del pedido.
     * @return Un objeto <code>LocalDateTime</code> de la entrega.
     */
    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    /**
     * Obtiene el método de pago seleccionado para esta orden.
     * @return Un objeto <code>MetodoPago</code>.
     */
    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    /**
     * Obtiene el costo total del pedido.
     * @return Un valor <code>float</code> con el importe total.
     */
    public float getTotal() {
        return total;
    }

    /**
     * Obtiene el número o folio visible del pedido.
     * @return Un número entero con el folio del ticket.
     */
    public int getNumeroPedido() {
        return numeroPedido;
    }

    /**
     * Obtiene el cliente que realizó la compra.
     * @return Un objeto <code>Cliente</code>.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Establece el identificador único del pedido.
     * @param id El <code>id</code> a asignar.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Establece el estado actual de la orden.
     * @param estado El nuevo <code>EstadoPedido</code> a asignar.
     */
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    /**
     * Establece la fecha y hora de creación del pedido.
     * @param fechaCreacion Objeto <code>LocalDateTime</code> con la fecha de origen.
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Establece la fecha y hora de entrega del pedido.
     * @param fechaEntrega Objeto <code>LocalDateTime</code> con la fecha de entrega.
     */
    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    /**
     * Establece el método de pago de la orden.
     * @param metodoPago El <code>MetodoPago</code> utilizado por el cliente.
     */
    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    /**
     * Establece el costo total a cobrar por el pedido.
     * @param total El importe a calcular o asignar.
     */
    public void setTotal(float total) {
        this.total = total;
    }

    /**
     * Establece el folio o número consecutivo del ticket.
     * @param numeroPedido El número visible del pedido.
     */
    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    /**
     * Asocia la orden con un cliente específico.
     * @param cliente El <code>Cliente</code> que realiza la compra.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}