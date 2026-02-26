package dominio;

/**
 * <b>Enumeración que define el ciclo de vida o estado de un Pedido.</b>
 * <p>Permite rastrear en qué fase se encuentra una orden dentro de la panadería,
 * facilitando el control tanto para los empleados como 
 * para el cliente que espera sus productos.</p>
 *
 * @author 262722
 * @author 262742
 */
public enum EstadoPedido {
    
    /**
     * <b>Pendiente:</b> El pedido ha sido registrado en el sistema
     */
    Pendiente,
    
    /**
     * <b>Listo:</b> Los productos del pedido ya fueron horneados o empacados. 
     * La orden está terminada y a la espera de ser entregada al cliente.
     */
    Listo,
    
    /**
     * <b>Entregado:</b> El proceso finalizó con éxito. El cliente recibió 
     * sus productos y la transacción está completamente cerrada.
     */
    Entregado,
    
    /**
     * <b>Cancelado:</b> El pedido fue anulado antes de ser entregado, ya 
     * sea por petición del cliente o del empleado.
     */
    Cancelado,
    
    /**
     * <b>No_reclamado:</b> El pedido fue preparado y cambió a estado "Listo", 
     * pero el cliente no se presentó a recogerlo en el tiempo establecido.
     */
    No_reclamado
    
}