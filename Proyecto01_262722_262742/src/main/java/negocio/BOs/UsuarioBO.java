/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Cliente;
import dominio.Empleado;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import persistencia.DAOs.iUsuarioDAO;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Objeto de Negocio (BO) para la gestión de Autenticación de Usuarios.</b>
 * <p>Esta clase centraliza la lógica de inicio de sesión tanto para clientes como 
 * para empleados. Utiliza algoritmos de hashing seguro (BCrypt) para verificar 
 * las credenciales sin exponer las contraseñas en texto plano.</p>
 * * @author 262722
 * @author 262742
 */
public class UsuarioBO implements iUsuarioBO {

    private iUsuarioDAO usuarioDAO;
    private static final Logger LOG = Logger.getLogger(UsuarioBO.class.getName());

    /**
     * Constructor que inyecta la dependencia del DAO de usuarios.
     * @param usuarioDAO Implementación de la interfaz iUsuarioDAO.
     */
    public UsuarioBO(iUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Método para iniciar sesión de un empleado. 
     * <p>Valida que el usuario y la contraseña no estén vacíos, consulta el empleado en la base de datos y
     * verifica la contraseña usando BCrypt.</p>
     *
     * @param usuario nombre de usuario ingresado
     * @param contrasenia contraseña ingresada en texto plano
     * @return Empleado autenticado si las credenciales son correctas
     * @throws NegocioException si el usuario no existe, la contraseña es incorrecta o ocurre un error de persistencia
     */
    @Override
    public Empleado iniciarSesionEmpleado(String usuario, String contrasenia) throws NegocioException {
        try {

            if (usuario == null || usuario.trim().isEmpty()) {
                throw new NegocioException("El usuario es obligatorio.");
            }

            if (contrasenia == null || contrasenia.isEmpty()) {
                throw new NegocioException("La contraseña es obligatoria.");
            }

            Empleado empleado = usuarioDAO.consultarEmpleadoPorUsuario(usuario.trim());

            if (empleado == null || empleado.getContrasenia() == null
                    || empleado.getContrasenia().isEmpty()) {
                throw new NegocioException("Usuario o contraseña incorrectos.");
            }

            String hashBD = empleado.getContrasenia();

            boolean ok = org.mindrot.jbcrypt.BCrypt.checkpw(contrasenia, hashBD);

            if (!ok) {
                throw new NegocioException("Usuario o contraseña incorrectos.");
            }

            return empleado;

        } catch (PersistenciaException ex) {
            LOG.warning("Error al iniciar sesión empleado. " + ex);
            throw new NegocioException("Error al iniciar sesión empleado: " + ex.getMessage(), ex);
        }
    }

    /**
     * Método para iniciar sesión de un cliente. 
     * <p>Valida que el usuario y la contraseña no estén vacíos, consulta el cliente en la base de datos y
     * verifica la contraseña con BCrypt (no se desencripta, se compara contra el hash).</p>
     *
     * @param usuario nombre de usuario ingresado
     * @param contrasenia contraseña ingresada en texto plano
     * @return Cliente autenticado si las credenciales son correctas
     * @throws NegocioException si el usuario no existe, la contraseña es incorrecta o ocurre un error de persistencia
     */
    @Override
    public Cliente iniciarSesionCliente(String usuario, String contrasenia) throws NegocioException {
        try {

            if (usuario == null || usuario.trim().isEmpty()) {
                throw new NegocioException("El usuario es obligatorio.");
            }

            if (contrasenia == null || contrasenia.isEmpty()) {
                throw new NegocioException("La contraseña es obligatoria.");
            }

            Cliente cliente = usuarioDAO.consultarClientePorUsuario(usuario.trim());

            // Si no existe o no tiene contraseña guardada
            if (cliente == null || cliente.getContrasenia() == null || cliente.getContrasenia().isEmpty()) {
                throw new NegocioException("Usuario o contraseña incorrectos.");
            }

            String hashBD = cliente.getContrasenia();

            boolean ok = org.mindrot.jbcrypt.BCrypt.checkpw(contrasenia, hashBD);

            if (!ok) {
                throw new NegocioException("Usuario o contraseña incorrectos.");
            }

            return cliente;

        } catch (PersistenciaException ex) {
            LOG.warning("Error al iniciar sesión cliente. " + ex);
            throw new NegocioException("Error al iniciar sesión cliente: " + ex.getMessage(), ex);
        }
    }
}