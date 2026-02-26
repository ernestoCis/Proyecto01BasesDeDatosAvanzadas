package dominio;

/**
 * <b>Enumeración que define el estado de disponibilidad de un Producto.</b>
 * <p>Sirve para controlar el catálogo de la panadería, permitiendo mostrar
 * a los clientes únicamente aquellos panes que actualmente 
 * se encuentran en existencia o que pueden ser preparados.</p>
 *
 * @author 262722
 * @author 262742
 */
public enum EstadoProducto {
    
    /**
     * <b>Disponible:</b> El producto cuenta con inventario suficiente o 
     * los ingredientes necesarios para su preparación están en almacén. 
     * Se puede agregar a nuevos pedidos.
     */
    Disponible,
    
    /**
     * <b>No_disponible:</b> El producto se ha agotado, está fuera de temporada 
     * o temporalmente retirado del menú. No se permite su venta.
     */
    No_disponible
    
}