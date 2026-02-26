package persistencia.Excepciones;

/**
 * <b>Clase que representa una Excepción personalizada para la capa de Persistencia.</b>
 * <p>Esta clase hereda de <code>Exception</code> y su propósito principal es encapsular 
 * cualquier error que ocurra al interactuar con la base de datos (por ejemplo, errores 
 * de sintaxis SQL, fallas de conexión o violaciones de llaves foráneas).</p>
 * <p>Al utilizar esta excepción, se aísla a las capas superiores (como la de Negocio) 
 * de los detalles técnicos de la base de datos, manteniendo una arquitectura limpia 
 * y fácil de mantener.</p>
 *
 * @author 262722
 * @author 262742
 */
public class PersistenciaException extends Exception{
    
    /**
     * Constructor por defecto de la clase <b>PersistenciaException</b>.
     * <p>Crea una excepción vacía sin un mensaje detallado del error.</p>
     */
    public PersistenciaException() {
    }

    /**
     * Constructor que inicializa la excepción con un mensaje descriptivo.
     * <p>Útil para lanzar errores específicos de persistencia generados manualmente, 
     * como "No se pudo encontrar el registro en la base de datos".</p>
     *
     * @param message El mensaje detallado que explica la causa del error.
     */
    public PersistenciaException(String message) {
        super(message);
    }

    /**
     * Constructor que inicializa la excepción con un mensaje y la causa original.
     * <p>Este es el constructor más utilizado en los DAOs. Sirve para atrapar una 
     * excepción técnica (como <code>SQLException</code>) y "envolverla" en esta 
     * excepción de persistencia, conservando el rastro original del error (stack trace) 
     * para facilitar la depuración.</p>
     *
     * @param message El mensaje descriptivo del error al intentar acceder a los datos.
     * @param cause La excepción técnica original (<code>Throwable</code>) que provocó el fallo.
     */
    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}