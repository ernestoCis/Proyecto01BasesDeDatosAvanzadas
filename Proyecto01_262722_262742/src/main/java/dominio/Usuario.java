package dominio;

/**
 * <b>Clase abstracta que representa a un Usuario del sistema.</b>
 * <p>Esta clase centraliza la información de autenticación y control de acceso 
 * para la plataforma de la panadería. Al ser abstracta, no puede instanciarse 
 * directamente, sino que sirve como base para entidades más específicas como 
 * <code>Cliente</code> y <code>Empleado</code>.</p>
 * <ul>
 * <li>Almacena las credenciales de acceso (nombre de usuario y contraseña).</li>
 * <li>Define el nivel de permisos a través de la enumeración <code>RolUsuario</code>.</li>
 * </ul>
 *
 * @author 262722
 * @author 262742
 */
public abstract class Usuario {

    private int id;
    private String usuario;
    private String contrasenia;
    private RolUsuario rol;

    /**
     * Constructor por defecto de la clase <b>Usuario</b>.
     * <p>Crea una instancia vacía sin inicializar sus atributos de acceso.</p>
     */
    public Usuario() {
    }

    /**
     * Constructor que inicializa las credenciales y el rol del usuario.
     *
     * @param id El identificador único del usuario en la base de datos.
     * @param usario El nombre o alias utilizado para iniciar sesión.
     * @param contrasenia La clave de seguridad (password) del usuario.
     * @param rol El <code>RolUsuario</code> que define sus permisos en el sistema.
     */
    public Usuario(int id, String usario, String contrasenia, RolUsuario rol) {
        this.id = id;
        this.usuario = usario;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    /**
     * Obtiene el identificador único del usuario.
     * @return Un número entero con el <code>id</code>.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre de usuario utilizado para el inicio de sesión.
     * @return Un <code>String</code> con el nombre de usuario.
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Obtiene la contraseña de acceso al sistema.
     * @return Un <code>String</code> con la contraseña.
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * Obtiene el rol o nivel de privilegios de este usuario.
     * @return Un objeto <code>RolUsuario</code>.
     */
    public RolUsuario getRol() {
        return rol;
    }

    /**
     * Establece el identificador único del usuario.
     * @param id El <code>id</code> a asignar.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Establece el nombre de usuario para el inicio de sesión.
     * @param usuario El alias o nombre de usuario a asignar.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Establece la contraseña de seguridad del usuario.
     * @param contrasenia La nueva contraseña a asignar.
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    /**
     * Establece el rol de acceso y permisos del usuario.
     * @param rol El <code>RolUsuario</code> correspondiente.
     */
    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

}