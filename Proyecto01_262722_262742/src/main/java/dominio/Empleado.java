package dominio;

/**
 * <b>Clase que representa a un Empleado dentro del sistema de la panadería.</b>
 * <p>Esta clase hereda de la clase <code>Usuario</code>. Su propósito es distinguir
 * a los trabajadores de la panadería de los clientes normales, permitiéndoles tener
 * acceso al sistema mediante sus credenciales y un rol específico.</p>
 *
 * @author 262722
 * @author 262742
 */
public class Empleado extends Usuario {

    /**
     * Constructor por defecto de la clase <b>Empleado</b>.
     * <p>Crea una instancia vacía sin inicializar sus atributos.</p>
     */
    public Empleado() {
    }

    /**
     * Constructor que inicializa los datos de acceso del empleado.
     * <p>Este constructor llama al constructor de su clase padre (<code>Usuario</code>)
     * mediante la palabra reservada <code>super</code>, delegando la asignación de 
     * las credenciales y el rol.</p>
     *
     * @param id El identificador único del empleado en la base de datos.
     * @param usuario El nombre de usuario utilizado para iniciar sesión.
     * @param contrasenia La contraseña de acceso al sistema.
     * @param rol El nivel de permisos (<code>RolUsuario</code>) asignado a este usuario.
     */
    public Empleado(int id, String usuario, String contrasenia, RolUsuario rol) {
        super(id, usuario, contrasenia, rol);
    }

}