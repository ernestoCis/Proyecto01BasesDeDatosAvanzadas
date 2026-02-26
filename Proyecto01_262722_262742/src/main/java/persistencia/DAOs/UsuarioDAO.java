/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.Cliente;
import dominio.Empleado;
import dominio.RolUsuario;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.Conexion.iConexionBD;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Data Access Object (DAO) para la gestión de Usuarios.</b>
 * <p>Esta clase maneja la persistencia y consulta de los usuarios del sistema. 
 * Dado que el sistema maneja distintos tipos de usuarios (Empleados y Clientes), 
 * este DAO realiza las consultas uniendo la tabla base (usuarios) con sus 
 * respectivas tablas hijas (empleados o clientes).</p>
 *
 * @author 262722
 * @author 262742
 */
public class UsuarioDAO implements iUsuarioDAO {

    /**
     * Componente encargado de crear conexiones con la base de datos.
     */
    private final iConexionBD conexionBD;
    
    /**
     * Logger para registrar información relevante y errores durante operaciones de persistencia.
     */
    private static final Logger LOG = Logger.getLogger(UsuarioDAO.class.getName());

    /**
     * Constructor que inicializa la dependencia de conexión.
     * * @param conexionBD Objeto que gestiona la creación de conexiones a la base de datos.
     */
    public UsuarioDAO(iConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    

    /**
     * Consulta y recupera un Empleado específico utilizando su nombre de usuario.
     * <p>Realiza un INNER JOIN entre las tablas 'usuarios' y 'empleados' para
     * obtener tanto las credenciales como los datos específicos del empleado.</p>
     * * @param usuario Nombre de usuario (credencial) del empleado a buscar.
     * @return Objeto {@link Empleado} poblado con los datos de la BD, o <code>null</code> si no se encuentra.
     * @throws PersistenciaException Si ocurre un error de SQL durante la consulta.
     */
    @Override
    public Empleado consultarEmpleadoPorUsuario(String usuario) throws PersistenciaException {

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
                    return null;
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
            throw new PersistenciaException("Error al consultar empleado por usuario", e);
        }
    }

    /**
     * Consulta y recupera un Cliente específico utilizando su nombre de usuario.
     * <p>Realiza un INNER JOIN entre las tablas 'usuarios' y 'clientes' para
     * obtener las credenciales, rol y datos personales del cliente.</p>
     * * @param usuario Nombre de usuario (credencial) del cliente a buscar.
     * @return Objeto {@link Cliente} poblado con los datos de la BD, o <code>null</code> si no se encuentra.
     * @throws PersistenciaException Si ocurre un error de SQL durante la consulta.
     */
    @Override
    public Cliente consultarClientePorUsuario(String usuario) throws PersistenciaException {

        String sql = """
            SELECT u.id, u.usuario, u.contrasenia, u.rol, c.nombres, c.apellido_paterno, c.apellido_materno, c.fecha_nacimiento
            FROM usuarios u
            INNER JOIN clientes c ON c.id_usuario = u.id
            WHERE u.usuario = ? AND u.rol = 'Cliente'
            """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Cliente cli = new Cliente();
                cli.setId(rs.getInt("id"));
                cli.setUsuario(rs.getString("usuario"));
                cli.setContrasenia(rs.getString("contrasenia"));
                cli.setRol(RolUsuario.valueOf(rs.getString("rol")));

                cli.setNombres(rs.getString("nombres"));
                cli.setApellidoPaterno(rs.getString("apellido_paterno"));
                cli.setApellidoMaterno(rs.getString("apellido_materno"));
                cli.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());

                return cli;
            }

        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Error consultando cliente: " + e.getMessage());
            throw new PersistenciaException("Error al consultar cliente por usuario", e);
        }
    }
}