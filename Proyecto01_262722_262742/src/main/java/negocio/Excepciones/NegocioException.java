package negocio.Excepciones;

/**
 * <b>Clase que representa una Excepción personalizada para la capa de Negocio.</b>
 * <p>Esta clase hereda de <code>Exception</code> y se utiliza para encapsular y 
 * manejar de forma controlada todos los errores que violen las reglas operativas 
 * de la panadería. Permite separar las fallas lógicas (ej. intentar comprar un 
 * producto agotado, o un cliente no encontrado) de los errores técnicos del sistema.</p>
 *
 * @author 262722
 * @author 262742
 */
public class NegocioException extends Exception{
    
    /**
     * Constructor por defecto de la clase <b>NegocioException</b>.
     * <p>Crea una excepción vacía sin un mensaje detallado del error.</p>
     */
     public NegocioException() {
    }

    /**
     * Constructor que inicializa la excepción con un mensaje descriptivo.
     * <p>Ideal para mostrar retroalimentación clara al usuario o al desarrollador 
     * sobre qué regla de negocio falló.</p>
     *
     * @param message El mensaje detallado que explica la causa del error.
     */
    public NegocioException(String message) {
        super(message);
    }

    /**
     * Constructor que inicializa la excepción con un mensaje y la causa original.
     * <p>Se utiliza comúnmente para "atrapar" una excepción de una capa inferior 
     * (como un error de base de datos) y "envolverla" en un error de negocio, 
     * sin perder el rastro técnico (stack trace) original.</p>
     *
     * @param message El mensaje detallado que explica el problema en el negocio.
     * @param cause La excepción original (<code>Throwable</code>) que provocó este error.
     */
    public NegocioException(String message, Throwable cause) {
        super(message, cause);
    }
}