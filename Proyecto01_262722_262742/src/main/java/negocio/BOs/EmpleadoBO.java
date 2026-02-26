/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Empleado;
import dominio.RolUsuario;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import negocio.util.PasswordUtil;
import persistencia.DAOs.iEmpleadoDAO;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Objeto de Negocio (BO) para la gestión de Empleados.</b>
 * <p>Esta clase provee la lógica necesaria para el control de acceso y registro 
 * del personal de la panadería. Se encarga de validar que los nombres de usuario 
 * sean únicos y de gestionar el ciclo de vida de las credenciales de seguridad 
 * utilizando algoritmos de hashing.</p>
 * * @author 262722
 * @author 262742
 */
public class EmpleadoBO implements iEmpleadoBO {

    /**
     * DAO para la persistencia de datos de empleados.
     */
    private iEmpleadoDAO empleadoDAO;
    
    /**
     * Logger para el registro de auditoría y errores técnicos.
     */
    private static final Logger LOG = Logger.getLogger(EmpleadoBO.class.getName());

    /**
     * Constructor que inyecta el DAO de empleado.
     * @param empleadoDAO Interfaz del DAO de persistencia.
     */
    public EmpleadoBO(iEmpleadoDAO empleadoDAO) {
        this.empleadoDAO = empleadoDAO;
    }

    /**
     * Registra un nuevo empleado en el sistema.
     * <p>El método realiza las siguientes acciones:</p>
     * <ul>
     * <li>Valida que los campos obligatorios no estén vacíos.</li>
     * <li>Verifica que el nombre de usuario no esté registrado previamente.</li>
     * <li>Asigna por defecto el rol de <code>RolUsuario.Empleado</code>.</li>
     * <li>Cifra la contraseña del empleado antes de enviarla a la base de datos.</li>
     * </ul>
     * @param empleado Objeto con la información del empleado a registrar.
     * @return El empleado guardado con su identificador generado.
     * @throws NegocioException Si el usuario ya existe o los datos son insuficientes.
     */
    @Override
    public Empleado registrarEmpleado(Empleado empleado) throws NegocioException {
        try {
            if (empleado == null) {
                throw new NegocioException("El empleado es obligatorio.");
            }

            if (empleado.getUsuario() == null || empleado.getUsuario().trim().isEmpty()) {
                throw new NegocioException("El usuario es obligatorio.");
            }

            if (empleado.getContrasenia() == null || empleado.getContrasenia().trim().isEmpty()) {
                throw new NegocioException("La contraseña es obligatoria.");
            }

            // Evitar duplicados 
            Empleado existente = empleadoDAO.consultarEmpleado(empleado.getUsuario().trim());
            if (existente != null) {
                throw new NegocioException("Ya existe un empleado con ese usuario.");
            }

            empleado.setRol(RolUsuario.Empleado);

            String hash = PasswordUtil.hash(empleado.getContrasenia());
            empleado.setContrasenia(hash);

            return empleadoDAO.insertarEmpleado(empleado);

        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo registrar al empleado. " + ex);
            throw new NegocioException("Error al registrar al empleado: " + ex.getMessage(), ex);
        }
    }

    /**
     * Valida las credenciales de un empleado para el inicio de sesión.
     * <p>Busca al empleado por su usuario y compara la contraseña proporcionada 
     * con el hash almacenado mediante el método <code>PasswordUtil.verificar</code>.</p>
     * @param usuario Nombre de usuario del empleado.
     * @param contrasenia Contraseña en texto plano ingresada en el login.
     * @return El objeto <code>Empleado</code> si la autenticación es exitosa.
     * @throws NegocioException Si el usuario no existe o las credenciales no coinciden.
     */
    @Override
    public Empleado consultarEmpleado(String usuario, String contrasenia) throws NegocioException {
        try {
            if (usuario == null || usuario.trim().isEmpty()) {
                throw new NegocioException("El usuario es obligatorio.");
            }

            if (contrasenia == null || contrasenia.trim().isEmpty()) {
                throw new NegocioException("La contraseña es obligatoria.");
            }

            Empleado empleado = empleadoDAO.consultarEmpleado(usuario.trim());

            if (empleado == null) {
                throw new NegocioException("Usuario o contraseña incorrectos.");
            }

            if (!PasswordUtil.verificar(contrasenia, empleado.getContrasenia())) {
                throw new NegocioException("Usuario o contraseña incorrectos.");
            }

            return empleado;

        } catch (PersistenciaException ex) {
            LOG.warning("Error al consultar al empleado. " + ex);
            throw new NegocioException("Error al consultar empleado. " + ex.getMessage(), ex);
        }
    }
}