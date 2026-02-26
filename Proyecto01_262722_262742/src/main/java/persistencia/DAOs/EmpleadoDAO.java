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
 *
 * @author Isaac
 */
public class EmpleadoDAO implements iEmpleadoDAO {

    private final iConexionBD conexionBD;

    /**
     * Logger para registrar información relevante
     */
    private static final Logger LOG = Logger.getLogger(EmpleadoDAO.class.getName());

    public EmpleadoDAO(iConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

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

    @Override
    public Empleado insertarEmpleado(Empleado empleado) throws PersistenciaException {

        String call = "{CALL sp_insertar_empleado(?, ?)}";

        try (Connection conn = conexionBD.crearConexion(); CallableStatement cs = conn.prepareCall(call)) {

            cs.setString(1, empleado.getUsuario());
            cs.setString(2, empleado.getContrasenia());

            cs.execute();

            // Obtenemos el último ID generado
            try (PreparedStatement ps = conn.prepareStatement("SELECT LAST_INSERT_ID()"); ResultSet rs = ps.executeQuery()) {

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
