package dominio;

/**
 * <b>Clase que representa el Detalle de un Pedido en la panadería.</b>
 * <p>Esta clase modela cada "renglón" o línea de artículo dentro de un ticket o pedido general. 
 * Se encarga de vincular un producto específico con la cantidad solicitada y el pedido al que pertenece.</p>
 * <ul>
 * <li>Guarda la información de cantidades y precios unitarios.</li>
 * <li>Almacena el subtotal por esa línea de producto.</li>
 * <li>Permite agregar notas específicas (por ejemplo: "sin glaseado" o "extra chocolate").</li>
 * </ul>
 *
 * @author 262722
 * @author 262742
 */
public class DetallePedido {

    private int id;
    private String nota;      
    private int cantidad;
    private float precio;
    private float subtotal;
    private Pedido pedido;
    private Producto producto;

    /**
     * Constructor por defecto de la clase <b>DetallePedido</b>.
     * <p>Crea una instancia vacía sin inicializar sus atributos.</p>
     */
    public DetallePedido() {
    }

    /**
     * Constructor que inicializa todos los atributos del detalle de un pedido.
     *
     * @param id El identificador único del detalle en la base de datos.
     * @param nota Instrucciones especiales o comentarios sobre este producto en particular.
     * @param cantidad El número de unidades solicitadas de este producto.
     * @param precio El precio unitario del producto al momento de realizar el pedido.
     * @param subtotal El costo total de esta línea (cantidad multiplicada por el precio).
     * @param pedido El objeto <code>Pedido</code> general al que pertenece este detalle.
     * @param producto El objeto <code>Producto</code> que se está ordenando.
     */
    public DetallePedido(int id, String nota, int cantidad, float precio, float subtotal, Pedido pedido, Producto producto) {
        this.id = id;
        this.nota = nota;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = subtotal;
        this.pedido = pedido;
        this.producto = producto;
    }

    /**
     * Obtiene el identificador del detalle del pedido.
     * @return Un número entero con el <code>id</code>.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene la nota o instrucción especial asociada a este detalle.
     * @return Un <code>String</code> con la nota del pedido.
     */
    public String getNota() {
        return nota;
    }

    /**
     * Obtiene la cantidad de productos solicitados.
     * @return Un número entero con la cantidad.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Obtiene el precio unitario del producto.
     * @return Un valor <code>float</code> que representa el precio.
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * Obtiene el subtotal calculado para esta línea del pedido.
     * @return Un valor <code>float</code> con el subtotal.
     */
    public float getSubtotal() {
        return subtotal;
    }

    /**
     * Obtiene el pedido general al que está vinculado este detalle.
     * @return Un objeto <code>Pedido</code>.
     */
    public Pedido getPedido() {
        return pedido;
    }

    /**
     * Obtiene el producto que se está ordenando en este detalle.
     * @return Un objeto <code>Producto</code>.
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Establece el identificador del detalle del pedido.
     * @param id El <code>id</code> a asignar.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Establece una nota o instrucción especial para este producto.
     * @param nota El texto de la nota a asignar.
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * Establece la cantidad de productos a ordenar.
     * @param cantidad El número de unidades.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Establece el precio unitario del producto en este detalle.
     * @param precio El costo unitario.
     */
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    /**
     * Establece el subtotal de esta línea del pedido.
     * @param subtotal El importe total calculado para esta línea.
     */
    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Vincula este detalle con un pedido general.
     * @param pedido El objeto <code>Pedido</code> correspondiente.
     */
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    /**
     * Asigna el producto que se está ordenando en este detalle.
     * @param producto El objeto <code>Producto</code> a vincular.
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}