package dominio;

/**
 * <b>Enumeración que define los roles de acceso de un Usuario en el sistema.</b>
 * <p>Esta enumeración es la base del control de acceso (seguridad) de la panadería. 
 * Permite al sistema identificar qué tipo de persona está iniciando sesión y, en 
 * consecuencia, qué permisos, menús o pantallas se le deben mostrar.</p>
 *
 * @author 262722
 * @author 262742
 */
public enum RolUsuario {
    
    /**
     * <b>Cliente:</b> Representa a un usuario externo. 
     * Tiene permisos básicos como explorar el catálogo de productos, 
     * agregar artículos al carrito, realizar pedidos y consultar su historial.
     */
    Cliente,
    
    /**
     * <b>Empleado:</b> Representa al personal interno de la panadería. 
     * Tiene permisos administrativos u operativos, como cambiar el estado 
     * de los pedidos, actualizar la disponibilidad de los productos o 
     * gestionar el inventario.
     */
    Empleado
    
}