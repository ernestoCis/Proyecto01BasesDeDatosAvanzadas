package presentacion.DTOs;

/**
 * <b>Data Transfer Object (DTO) que encapsula el resultado de validar un cupón.</b>
 * <p>Esta clase se utiliza para transportar la información desde la capa de Negocio 
 * hacia la capa de Presentación (la interfaz gráfica) sin exponer las entidades 
 * originales del sistema. Al tener sus atributos definidos como <code>final</code>, 
 * es un objeto inmutable, lo que garantiza que los datos de respuesta no sean 
 * alterados accidentalmente durante el trayecto.</p>
 *
 * @author 262722
 * @author 262742
 */
public class ResultadoCuponDTO {
    
    private final boolean valido;
    private final String mensaje;
    private final float descuento;
    
    /**
     * Constructor que inicializa el DTO con el resultado de la validación del cupón.
     * <p>Una vez creado, los valores de este objeto no pueden modificarse.</p>
     *
     * @param valido <code>true</code> si el cupón es válido y aplicable; 
     * <code>false</code> si expiró, no existe o no cumple los requisitos.
     * @param mensaje Un texto descriptivo con la retroalimentación para el usuario.
     * @param descuento El porcentaje o cantidad económica a descontar del total del pedido. 
     * Si el cupón no es válido, este valor suele ser 0.
     */
    public ResultadoCuponDTO(boolean valido, String mensaje, float descuento){
        this.valido = valido;
        this.mensaje = mensaje;
        this.descuento = descuento;
    }

    /**
     * Indica si el cupón ingresado superó las validaciones del negocio.
     * @return <code>true</code> si es válido; <code>false</code> en caso contrario.
     */
    public boolean esValido() {
        return valido;
    }

    /**
     * Obtiene el mensaje de respuesta de la validación.
     * @return Un <code>String</code> con la retroalimentación para mostrar en pantalla.
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Obtiene el valor del descuento que otorga este cupón.
     * @return Un número <code>float</code> que representa la rebaja a aplicar.
     */
    public float getDescuento() {
        return descuento;
    }
    
}