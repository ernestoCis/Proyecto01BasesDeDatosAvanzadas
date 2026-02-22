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

        String sqlUsuario = """
                            INSERT INTO usuarios(usuario, contrasenia, rol)
                            VALUES(?, ?, ?)
                            """;

        String sqlEmpleado = """
                             INSERT INTO empleados(id_usuario)
                             VALUES(?)
                             """;

        Connection conn = null;

        try {
            conn = conexionBD.crearConexion();
            conn.setAutoCommit(false); // iniciamos transacción

            // Se inserta en tabla Usuarios
            try (PreparedStatement ps = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, empleado.getUsuario());
                ps.setString(2, empleado.getContrasenia());
                ps.setString(3, RolUsuario.Empleado.name());

                if (ps.executeUpdate() == 0) {
                    throw new PersistenciaException("No se pudo insertar usuario (empleado).");
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        empleado.setId(rs.getInt(1));
                    } else {
                        throw new PersistenciaException("No se pudo obtener el ID generado.");
                    }
                }
            }

            // Se inserta en tabla Empleados
            try (PreparedStatement ps = conn.prepareStatement(sqlEmpleado)) {

                ps.setInt(1, empleado.getId());

                if (ps.executeUpdate() == 0) {
                    throw new PersistenciaException("No se pudo insertar empleado.");
                }
            }

            conn.commit(); // confirmamos transacción
            empleado.setRol(RolUsuario.Empleado);

            return empleado;

        } catch (SQLException ex) {

            if (conn != null) {
                try {
                    conn.rollback(); // revertimos si falla algo
                } catch (SQLException ignored) {
                }
            }

            LOG.log(Level.WARNING, "Error al insertar empleado: " + ex.getMessage());
            throw new PersistenciaException("Error al insertar empleado", ex);

        } finally {

            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
