/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.Empleado;
import dominio.RolUsuario;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.Conexion.iConexionBD;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Data Access Object (DAO) para los Empleados.</b>
 * <p>Gestiona las operaciones de persistencia del personal interno. 
 * Esta clase se apoya en procedimientos almacenados para garantizar la integridad 
 * referencial al insertar registros en múltiples tablas (ej. Usuarios y Empleados).</p>
 *
 * @author Isaac
 * @author 262722
 * @author 262742
 */
public class EmpleadoDAO implements iEmpleadoDAO {

    private final iConexionBD conexionBD;

    /**
     * Logger para registrar información relevante y errores durante operaciones de persistencia.
     */
    private static final Logger LOG = Logger.getLogger(EmpleadoDAO.class.getName());

    /**
     * Constructor que inyecta la dependencia de la conexión a la base de datos.
     * @param conexionBD Implementación de la interfaz de conexión.
     */
    public EmpleadoDAO(iConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Consulta la información de un empleado en la base de datos a partir de su nombre de usuario.
     * <p>Realiza un <code>INNER JOIN</code> entre las tablas <code>usuarios</code> y 
     * <code>empleados</code>, filtrando explícitamente por el rol de 'Empleado'.</p>
     *
     * @param usuario Nombre de usuario exacto a buscar.
     * @return Objeto {@link Empleado} poblado con los datos de la base de datos, o <code>null</code> si no existe.
     * @throws PersistenciaException Si ocurre un error de SQL durante la consulta.
     */
    @Override
    public Empleado consultarEmpleado(String usuario) throws PersistenciaException {

        String sql = """
                     SELECT u.id, u.usuario, u.contrasenia, u.rol
                     FROM usuarios u
                     INNER JOIN empleados e ON e.id_usuario = u.id
                     WHERE u.usuario = ? AND u.rol = 'Empleado'
                     """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null; // no existe empleado
                }

                Empleado emp = new Empleado();
                emp.setId(rs.getInt("id"));
                emp.setUsuario(rs.getString("usuario"));
                emp.setContrasenia(rs.getString("contrasenia"));
                emp.setRol(RolUsuario.valueOf(rs.getString("rol")));

                return emp;
            }

        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error consultando empleado: " + e.getMessage());
            throw new PersistenciaException("Error al consultar empleado", e);
        }
    }

    /**
     * Registra un nuevo empleado en el sistema invocando un procedimiento almacenado.
     * <p>Delega la lógica de inserción relacional al SP <code>sp_insertar_empleado</code> 
     * y posteriormente recupera el identificador autogenerado utilizando <code>LAST_INSERT_ID()</code> 
     * sobre la misma conexión.</p>
     *
     * @param empleado Objeto con los datos del empleado (usuario y contraseña ya hasheada).
     * @return El objeto <code>Empleado</code> con su ID y rol asignados tras la inserción.
     * @throws PersistenciaException Si la llamada al procedimiento almacenado falla.
     */
    @Override
    public Empleado insertarEmpleado(Empleado empleado) throws PersistenciaException {

        String call = "{CALL sp_insertar_empleado(?, ?)}";

        try (Connection conn = conexionBD.crearConexion(); CallableStatement cs = conn.prepareCall(call)) {

            cs.setString(1, empleado.getUsuario());
            cs.setString(2, empleado.getContrasenia());

            cs.execute();

            // Obtenemos el último ID generado asegurando usar la misma conexión activa
            try (PreparedStatement ps = conn.prepareStatement("SELECT LAST_INSERT_ID()"); 
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    empleado.setId(rs.getInt(1));
                }
            }

            empleado.setRol(RolUsuario.Empleado);
            return empleado;

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al insertar empleado", ex);
        }
    }
}