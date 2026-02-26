package dominio;

/**
 * <b>Enumeración que define los métodos de pago aceptados en la panadería.</b>
 * <p>Esta enumeración permite estandarizar la forma en la que los clientes 
 * pueden liquidar el total de sus pedidos. Facilita el registro contable, 
 * el corte de caja y el procesamiento de cobros.</p>
 *
 * @author 262722
 * @author 262742
 */
public enum MetodoPago {
    
    /**
     * <b>Efectivo:</b> Pago realizado con billetes y monedas físicas, 
     * normalmente entregado directamente en el mostrador de la panadería 
     * o al repartidor al momento de la entrega.
     */
    Efectivo,
    
    /**
     * <b>Credito:</b> Pago procesado a través de una terminal bancaria 
     * o pasarela digital utilizando una tarjeta de crédito.
     */
    Credito,
    
    /**
     * <b>Debito:</b> Pago procesado a través de una terminal bancaria 
     * o pasarela digital utilizando una tarjeta de débito o cuenta de ahorros.
     */
    Debito
    
}