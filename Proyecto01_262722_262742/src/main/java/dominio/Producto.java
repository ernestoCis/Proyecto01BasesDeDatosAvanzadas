package dominio;

/**
 * <b>Clase que representa un Producto dentro del catálogo de la panadería.</b>
 * <p>Esta clase centraliza toda la información de los artículos que se venden 
 * en el establecimiento. Es fundamental para 
 * mostrar el menú a los clientes y calcular los totales de los pedidos.</p>
 * <ul>
 * <li>Define la información básica: nombre, descripción y precio.</li>
 * <li>Clasifica el artículo mediante un <code>TipoProducto</code>.</li>
 * <li>Controla su disponibilidad en el inventario mediante un <code>EstadoProducto</code>.</li>
 * </ul>
 *
 * @author 262722
 * @author 262742
 */
public class Producto {
    
    private int id;
    private String nombre;
    private TipoProducto tipo;
    private float precio;
    private EstadoProducto estado;
    private String descripcion;

    /**
     * Constructor por defecto de la clase <b>Producto</b>.
     * <p>Crea una instancia de producto vacía sin inicializar sus atributos.</p>
     */
    public Producto() {
    }

    /**
     * Constructor que inicializa todos los atributos de un producto del catálogo.
     *
     * @param id El identificador único del producto en la base de datos.
     * @param nombre El nombre comercial del producto (ej. "Concha de Vainilla").
     * @param tipo La categoría o clasificación a la que pertenece.
     * @param precio El costo de venta al público de este producto.
     * @param estado La disponibilidad actual del producto (Disponible o No disponible).
     * @param descripcion Una breve reseña o lista de ingredientes para mostrar al cliente.
     */
    public Producto(int id, String nombre, TipoProducto tipo, float precio, EstadoProducto estado, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el identificador único del producto.
     * @return Un número entero con el <code>id</code>.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del producto.
     * @return Un <code>String</code> con el nombre comercial.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la categoría o tipo de este producto.
     * @return Un objeto <code>TipoProducto</code>.
     */
    public TipoProducto getTipo() {
        return tipo;
    }

    /**
     * Obtiene el precio de venta del producto.
     * @return Un valor <code>float</code> con el precio.
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * Obtiene el estado de disponibilidad del producto.
     * @return Un objeto <code>EstadoProducto</code> indicando si se puede vender o no.
     */
    public EstadoProducto getEstado() {
        return estado;
    }

    /**
     * Obtiene la descripción detallada del producto.
     * @return Un <code>String</code> con la descripción o ingredientes.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece el identificador único del producto.
     * @param id El <code>id</code> a asignar.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Establece el nombre del producto.
     * @param nombre El texto a asignar como nombre comercial.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece el tipo o categoría del producto.
     * @param tipo El <code>TipoProducto</code> correspondiente.
     */
    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }

    /**
     * Establece el precio de venta del producto.
     * @param precio El importe a asignar.
     */
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    /**
     * Establece el estado de disponibilidad del producto en el catálogo.
     * @param estado El nuevo <code>EstadoProducto</code> a asignar.
     */
    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }

    /**
     * Establece la descripción del producto.
     * @param descripcion El texto descriptivo a mostrar a los clientes.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}