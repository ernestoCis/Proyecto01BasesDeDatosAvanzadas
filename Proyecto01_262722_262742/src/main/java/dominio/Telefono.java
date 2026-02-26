package dominio;

/**
 * <b>Clase que representa un número de Teléfono de contacto de un Cliente.</b>
 * <p>Esta clase permite gestionar la información de contacto de los usuarios 
 * de la panadería. Al ser una entidad separada, facilita que un mismo cliente 
 * pueda registrar múltiples números telefónicos para recibir notificaciones 
 * sobre sus pedidos.</p>
 * <ul>
 * <li>Almacena el número telefónico como una cadena de texto.</li>
 * <li>Utiliza una <b>etiqueta</b> para identificar el tipo de teléfono (ej. "Celular", "Casa", "Oficina").</li>
 * <li>Mantiene una relación directa con el objeto <code>Cliente</code> al que le pertenece.</li>
 * </ul>
 *
 * @author 262722
 * @author 262742
 */
public class Telefono {
    
    private int id;
    private String telefono;
    private String etiqueta;
    private Cliente cliente;

    /**
     * Constructor por defecto de la clase <b>Telefono</b>.
     * <p>Crea una instancia vacía sin inicializar sus atributos de contacto.</p>
     */
    public Telefono() {
    }

    /**
     * Constructor que inicializa todos los atributos de un número de teléfono.
     *
     * @param id El identificador único del teléfono en la base de datos.
     * @param telefono El número de contacto propiamente dicho (ej. "555-1234").
     * @param etiqueta Una palabra descriptiva para identificar el número (ej. "Trabajo", "Personal").
     * @param cliente El objeto <code>Cliente</code> dueño de este número telefónico.
     */
    public Telefono(int id, String telefono, String etiqueta, Cliente cliente) {
        this.id = id;
        this.telefono = telefono;
        this.etiqueta = etiqueta;
        this.cliente = cliente;
    }

    /**
     * Obtiene el identificador único del registro telefónico.
     * @return Un número entero con el <code>id</code>.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el número de teléfono.
     * @return Un <code>String</code> con el número de contacto.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Obtiene la etiqueta o descripción del número telefónico.
     * @return Un <code>String</code> indicando el tipo de teléfono.
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Obtiene el cliente asociado a este número telefónico.
     * @return Un objeto <code>Cliente</code> dueño del contacto.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Establece el identificador único del teléfono.
     * @param id El <code>id</code> a asignar.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Establece el número de teléfono de contacto.
     * @param telefono El texto con el número a asignar.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Establece la etiqueta para identificar el tipo de teléfono.
     * @param etiqueta El texto descriptivo (ej. "Casa", "Móvil").
     */
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Vincula este número telefónico con un cliente en específico.
     * @param cliente El objeto <code>Cliente</code> correspondiente.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
}