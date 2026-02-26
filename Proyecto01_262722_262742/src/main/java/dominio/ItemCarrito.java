package dominio;

/**
 * <b>Clase que representa un artículo dentro del carrito de compras.</b>
 * <p>Esta clase se utiliza de forma temporal mientras el cliente está navegando 
 * y seleccionando productos en la panadería, antes de formalizar la compra.</p>
 * <ul>
 * <li>Vincula un objeto <code>Producto</code> específico.</li>
 * <li>Almacena la <b>cantidad</b> que el cliente desea llevar de dicho producto.</li>
 * </ul>
 * <p>Una vez que el cliente confirma la compra, estos ítems suelen convertirse 
 * en objetos de tipo <code>DetallePedido</code>.</p>
 *
 * @author 262722
 * @author 262742
 */
public class ItemCarrito {
    
    private Producto producto;
    private int cantidad;

    /**
     * Constructor por defecto de la clase <b>ItemCarrito</b>.
     * <p>Crea un ítem vacío para el carrito sin inicializar el producto ni la cantidad.</p>
     */
    public ItemCarrito() {
    }

    /**
     * Constructor que inicializa un artículo en el carrito con su producto y cantidad.
     *
     * @param producto El objeto <code>Producto</code> que el cliente desea comprar.
     * @param cantidad El número de unidades (entero) que se agregan al carrito.
     */
    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el producto asociado a este ítem del carrito.
     * @return Un objeto <code>Producto</code>.
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Establece o cambia el producto de este ítem en el carrito.
     * @param producto El nuevo objeto <code>Producto</code> a asignar.
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * Obtiene la cantidad seleccionada de este producto.
     * @return Un número entero con la cantidad.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece o actualiza la cantidad a comprar de este producto.
     * @param cantidad La nueva cantidad en unidades.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
}