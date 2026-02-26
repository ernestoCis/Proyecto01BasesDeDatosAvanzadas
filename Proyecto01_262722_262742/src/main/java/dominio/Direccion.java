package dominio;

/**
 * <b>Clase que representa la Dirección física de un Cliente.</b>
 * <p>Esta clase se encarga de almacenar de forma estructurada los datos del domicilio 
 * de los clientes de la panadería. Es fundamental para gestionar las entregas o 
 * mantener un registro detallado de la ubicación de los usuarios.</p>
 * <ul>
 * <li>Contiene los datos básicos de una dirección domiciliaria (calle, colonia, número y código postal).</li>
 * <li>Mantiene una relación directa con el objeto <code>Cliente</code> al que pertenece.</li>
 * </ul>
 *
 * @author 262722
 * @author 262742
 */
public class Direccion {

    private int id;
    private String calle;
    private String colonia;
    private int cp;
    private int numero;
    private Cliente cliente;

    /**
     * Constructor por defecto de la clase <b>Direccion</b>.
     * <p>Crea una instancia vacía sin inicializar los atributos del domicilio.</p>
     */
    public Direccion() {
    }

    /**
     * Constructor que inicializa todos los atributos de una dirección.
     *
     * @param id El identificador único de la dirección en la base de datos.
     * @param calle El nombre de la calle donde reside el cliente.
     * @param colonia El nombre de la colonia, barrio o fraccionamiento.
     * @param cp El Código Postal (CP) de la ubicación.
     * @param numero El número exterior (e interior, si aplica y se maneja numéricamente) de la vivienda.
     * @param cliente El objeto <code>Cliente</code> al que pertenece esta dirección.
     */
    public Direccion(int id, String calle, String colonia, int cp, int numero, Cliente cliente) {
        this.id = id;
        this.calle = calle;
        this.colonia = colonia;
        this.cp = cp;
        this.numero = numero;
        this.cliente = cliente;
    }

    /**
     * Obtiene el identificador único de la dirección.
     * @return Un número entero con el <code>id</code>.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre de la calle.
     * @return Un <code>String</code> con la calle registrada.
     */
    public String getCalle() {
        return calle;
    }

    /**
     * Obtiene el nombre de la colonia o barrio.
     * @return Un <code>String</code> con la colonia registrada.
     */
    public String getColonia() {
        return colonia;
    }

    /**
     * Obtiene el código postal de la dirección.
     * @return Un número entero que representa el código postal.
     */
    public int getCp() {
        return cp;
    }

    /**
     * Obtiene el número de la vivienda o local.
     * @return Un número entero con el número exterior/interior.
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Obtiene el cliente asociado a esta dirección.
     * @return Un objeto <code>Cliente</code> dueño de este domicilio.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Establece el identificador único de la dirección.
     * @param id El <code>id</code> a asignar.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Establece el nombre de la calle.
     * @param calle El nombre de la calle a asignar.
     */
    public void setCalle(String calle) {
        this.calle = calle;
    }

    /**
     * Establece el nombre de la colonia, barrio o fraccionamiento.
     * @param colonia El nombre de la colonia a asignar.
     */
    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    /**
     * Establece el código postal de la ubicación.
     * @param cp El código postal a asignar.
     */
    public void setCp(int cp) {
        this.cp = cp;
    }

    /**
     * Establece el número de la vivienda correspondiente a la dirección.
     * @param numero El número exterior o interior a asignar.
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * Vincula esta dirección con un cliente en específico.
     * @param cliente El objeto <code>Cliente</code> propietario de la dirección.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}