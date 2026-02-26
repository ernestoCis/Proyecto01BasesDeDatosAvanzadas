package negocio.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * <b>Clase utilitaria para el encriptado y verificación de contraseñas.</b>
 * <p>Esta clase proporciona métodos estáticos para asegurar las credenciales 
 * de los usuarios (clientes y empleados) utilizando el algoritmo <b>BCrypt</b>. 
 * Garantiza que las contraseñas no se almacenen en texto plano en la base de datos, 
 * protegiendo la integridad del sistema contra posibles vulnerabilidades.</p>
 *
 * @author 262722
 * @author 262742
 */
public class PasswordUtil {
    
    /**
     * Constructor privado de la clase <b>PasswordUtil</b>.
     * <p>Al ser una clase estrictamente utilitaria (con puros métodos estáticos), 
     * se oculta el constructor para evitar que sea instanciada por error en otras 
     * partes del código.</p>
     */
    private PasswordUtil() {}

    /**
     * Encripta (aplica un hash) a una contraseña en texto plano.
     * <p>Utiliza el algoritmo BCrypt para generar un "salt" aleatorio con una 
     * carga de trabajo (log_rounds) de 12, lo cual ofrece un excelente equilibrio 
     * entre seguridad y rendimiento.</p>
     *
     * @param plainPassword La contraseña original ingresada por el usuario.
     * @return Un <code>String</code> que contiene el hash seguro (y su salt) 
     * listo para ser guardado en la base de datos.
     */
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /**
     * Verifica si una contraseña ingresada coincide con un hash almacenado.
     * <p>Este método es el que se debe utilizar durante el proceso de <b>Login</b>. 
     * Compara de forma segura el texto ingresado contra el hash de la base de datos, 
     * extrayendo el "salt" automáticamente. También maneja casos donde los valores 
     * sean nulos para evitar errores del sistema.</p>
     *
     * @param plainPassword La contraseña en texto plano que el usuario intenta usar para entrar.
     * @param hashedPassword El hash seguro que fue recuperado de la base de datos.
     * @return <code>true</code> si la contraseña es correcta y coincide con el hash; 
     * <code>false</code> si es incorrecta o si alguno de los parámetros es nulo.
     */
    public static boolean verificar(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) return false;
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}