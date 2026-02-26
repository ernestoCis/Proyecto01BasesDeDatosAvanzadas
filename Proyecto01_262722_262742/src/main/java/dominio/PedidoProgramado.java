package dominio;

import java.time.LocalDateTime;

/**
 * <b>Clase que representa un Pedido Programado en la panadería.</b>
 * <p>Esta clase hereda de <code>Pedido</code> y se utiliza para gestionar aquellas 
 * órdenes que los clientes realizan con anticipación.</p>
 * <ul>
 * <li>Hereda toda la información base de un pedido (cliente, total, fechas, etc.).</li>
 * <li>Permite la aplicación de un <b>Cupón de descuento</b> específico para esta orden.</li>
 * </ul>
 *
 * @author 262722
 * @author 262742
 */
public class PedidoProgramado extends Pedido {

    private Cupon cupon;

    /**
     * Constructor por defecto de la clase <b>PedidoProgramado</b>.
     * <p>Crea una instancia vacía sin inicializar sus atributos ni los de su clase padre.</p>
     */
    public PedidoProgramado() {
    }

    /**
     * Constructor que inicializa todos los atributos de un pedido programado.
     * <p>Utiliza <code>super()</code> para asignar los valores generales definidos en 
     * la clase <code>Pedido</code> y adicionalmente asigna un cupón promocional.</p>
     *
     * @param id El identificador único del pedido en la base de datos (heredado).
     * @param estado El estado actual de la orden (heredado).
     * @param fechaCreacion La fecha y hora en la que se registró el pedido (heredada).
     * @param fechaEntrega La fecha y hora en la que se programó la entrega (heredada).
     * @param metodoPago La forma de pago elegida por el cliente (heredada).
     * @param total El costo total de la orden, que puede verse afectado por el cupón (heredado).
     * @param numeroPedido El número visible o folio del ticket (heredado).
     * @param cliente El objeto <code>Cliente</code> que realiza la compra (heredado).
     * @param cupon El objeto <code>Cupon</code> aplicado para obtener un descuento en este pedido.
     */
    public PedidoProgramado(int id, EstadoPedido estado, LocalDateTime fechaCreacion, LocalDateTime fechaEntrega, MetodoPago metodoPago, float total, int numeroPedido, Cliente cliente, Cupon cupon) {
        super(id, estado, fechaCreacion, fechaEntrega, metodoPago, total, numeroPedido, cliente);
        this.cupon = cupon;
    }

    /**
     * Obtiene el cupón de descuento aplicado a este pedido programado.
     * @return Un objeto <code>Cupon</code>. Si no se aplicó ninguno, puede ser nulo.
     */
    public Cupon getCupon() {
        return cupon;
    }

    /**
     * Establece o aplica un cupón de descuento a la orden programada.
     * @param cupon El objeto <code>Cupon</code> a vincular.
     */
    public void setCupon(Cupon cupon) {
        this.cupon = cupon;
    }
}