package dominio;

/**
 * <b>Enumeración que define los estados posibles de un Cliente.</b>
 * <p>Se utiliza para gestionar de forma segura si un cliente tiene acceso 
 * actual al sistema de la panadería. Esto permite suspender o dar de baja 
 * cuentas sin necesidad de borrar los datos personales o el historial de 
 * pedidos de la base de datos.</p>
 *
 * @author 262722
 * @author 262742
 */
public enum EstadoCliente {
    
    /**
     * <b>Activo:</b> Indica que el cliente está operando normalmente. 
     * Puede iniciar sesión en el sistema, explorar productos y realizar 
     * pedidos sin ningún tipo de restricción.
     */
    Activo,
    
    /**
     * <b>Inactivo:</b> Indica que la cuenta del cliente está suspendida, 
     * bloqueada o dada de baja. El cliente no podrá realizar nuevas 
     * operaciones o pedidos en la panadería hasta que su estado cambie.
     */
    Inactivo
    
}