package dominio;

/**
 * <b>Enumeración que define las categorías o tipos de un Producto.</b>
 * <p>Permite clasificar los artículos de la panadería según su naturaleza, 
 * facilitando la organización del catálogo, las búsquedas de los clientes 
 * y la estructuración del menú (por ejemplo, separar el pan dulce del salado).</p>
 *
 * @author 262722
 * @author 262742
 */
public enum TipoProducto {
    
    /**
     * <b>Dulce:</b> Clasifica productos azucarados o de repostería, 
     * como conchas, donas, pasteles, galletas y pan de muerto.
     */
    Dulce,
    
    /**
     * <b>Salado:</b> Clasifica productos de panadería tradicional salada, 
     * como bolillos, teleras, baguettes, cuernitos o empanadas saladas.
     */
    Salado,
    
    /**
     * <b>Integral:</b> Clasifica productos elaborados con harinas no refinadas, 
     * avena, semillas o granos enteros, enfocados en una línea más dietética.
     */
    Integral
    
}