package dominio;

import java.time.LocalDate;
import java.util.List;

/**
 * <b>Clase que representa a un Cliente dentro del sistema de la panadería.</b>
 * <p>Esta clase hereda de la clase <code>Usuario</code>, extendiendo su funcionalidad
 * para almacenar los datos personales y de contacto específicos de los clientes que 
 * realizan pedidos.</p>
 * * <b>Información almacenada:</b>
 * <ul>
 * <li>Datos personales (nombres, apellidos, edad, fecha de nacimiento).</li>
 * <li>Datos de contacto (dirección y una lista de teléfonos).</li>
 * <li>El estado actual del cliente (activo, inactivo).</li>
 * </ul>
 *
 * @author 262722
 * @author 262742
 */
public class Cliente extends Usuario {

    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private LocalDate fechaNacimiento;
    private int edad;
    private Direccion direccion;
    private List<Telefono> telefonos;
    private EstadoCliente estado;

    /**
     * Constructor por defecto de la clase <b>Cliente</b>.
     * <p>Crea una instancia de Cliente vacía, sin inicializar sus atributos.</p>
     */
    public Cliente() {
    }

    /**
     * Constructor que inicializa los datos personales del cliente y 
     * su información de acceso al sistema (heredada de Usuario).
     *
     * @param nombres Los nombres del cliente.
     * @param apellidoPaterno El apellido paterno del cliente.
     * @param apellidoMaterno El apellido materno del cliente.
     * @param fechaNacimiento La fecha de nacimiento del cliente.
     * @param edad La edad del cliente.
     * @param id El identificador único del usuario en la base de datos (heredado).
     * @param usario El nombre de usuario para iniciar sesión (heredado).
     * @param contrasenia La contraseña de acceso (heredada).
     * @param rol El rol asignado a este usuario en el sistema (heredado).
     */
    public Cliente(String nombres, String apellidoPaterno, String apellidoMaterno, LocalDate fechaNacimiento, int edad, int id, String usario, String contrasenia, RolUsuario rol) {
        super(id, usario, contrasenia, rol);
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
    }

    /**
     * Obtiene los nombres del cliente.
     * @return Un <code>String</code> con los nombres.
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Obtiene el apellido paterno del cliente.
     * @return Un <code>String</code> con el apellido paterno.
     */
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    /**
     * Obtiene el apellido materno del cliente.
     * @return Un <code>String</code> con el apellido materno.
     */
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    /**
     * Obtiene la fecha de nacimiento del cliente.
     * @return Un objeto <code>LocalDate</code> con la fecha de nacimiento.
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Obtiene la edad del cliente.
     * @return Un número entero con la edad actual.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Establece los nombres del cliente.
     * @param nombres El nombre o nombres a asignar.
     */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /**
     * Establece el apellido paterno del cliente.
     * @param apellidoPaterno El apellido paterno a asignar.
     */
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    /**
     * Establece el apellido materno del cliente.
     * @param apellidoMaterno El apellido materno a asignar.
     */
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    /**
     * Establece la fecha de nacimiento del cliente.
     * @param fechaNacimiento Objeto <code>LocalDate</code> con la fecha de nacimiento.
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Establece la edad del cliente.
     * @param edad La edad calculada a asignar.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Obtiene la dirección registrada del cliente.
     * @return Un objeto <code>Direccion</code> con la información del domicilio.
     */
    public Direccion getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del cliente.
     * @param direccion Objeto <code>Direccion</code> a asignar.
     */
    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene la lista de teléfonos asociados al cliente.
     * @return Una <code>List</code> de objetos <code>Telefono</code>.
     */
    public List<Telefono> getTelefonos() {
        return telefonos;
    }

    /**
     * Establece la lista de teléfonos del cliente.
     * @param telefonos La lista de objetos <code>Telefono</code> a asignar.
     */
    public void setTelefonos(List<Telefono> telefonos) {
        this.telefonos = telefonos;
    }

    /**
     * Obtiene el estado actual del cliente en el sistema.
     * @return Un objeto <code>EstadoCliente</code> representando su situación actual.
     */
    public EstadoCliente getEstado() {
        return estado;
    }

    /**
     * Establece el estado del cliente.
     * @param estado El <code>EstadoCliente</code> a asignar.
     */
    public void setEstado(EstadoCliente estado) {
        this.estado = estado;
    }
    
}