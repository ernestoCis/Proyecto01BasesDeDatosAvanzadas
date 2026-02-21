/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.Conexion.iConexionBD;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author 
 */
public class ClienteDAO implements iClienteDAO{
    
    private final iConexionBD conexionBD;
    
    
    /**
     * Logger para registrar información relevante durante operaciones de
     * persistencia.
     */
    private static final Logger LOG = Logger.getLogger(PedidoDAO.class.getName());
    
    public ClienteDAO(iConexionBD conexionBD){
        this.conexionBD = conexionBD;
    }

    @Override
    public Cliente consultarCliente(String usuario) throws PersistenciaException {
        String sql = """
                    SELECT 
                        u.id,
                        u.usuario,
                        c.nombres,
                        c.apellido_paterno,
                        c.apellido_materno,
                        c.fecha_nacimiento
                    FROM usuarios u
                    INNER JOIN clientes c ON c.id_usuario = u.id
                    WHERE u.usuario = ?
                    """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null; // no existe cliente con ese correo
                }

                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_usuario"));
                cliente.setNombres(rs.getString("nombres"));
                cliente.setApellidoPaterno(rs.getString("apellido_paterno"));
                if(rs.getString("apellido_materno") != null){
                    cliente.setApellidoMaterno("apellido_materno");
                }
                cliente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                
                return cliente;
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar cliente por correo", e);
        }
    }

    @Override
    public Cliente insertarCliente(Cliente cliente) throws PersistenciaException {

        String comandoSQLUsuario = """
                                   INSERT INTO usuarios(usuario, contrasenia, rol)
                                   VALUES(?, ?, ?)
                                   """;
        
        String comandoSQLCliente = """
                                   INSERT INTO clientes(id_usuario, nombres, apellido_paterno, apellido_materno, fecha_nacimiento)
                                   VALUES(?, ?)
                                   """;

        Connection conn = null;

        try {
            conn = conexionBD.crearConexion();
            conn.setAutoCommit(false);

            // insertar en usuarios y obtener id
            try (PreparedStatement ps = conn.prepareStatement(comandoSQLUsuario, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, cliente.getUsuario());
                ps.setString(2, cliente.getContrasenia());
                ps.setString(3, cliente.getRol().name());

                if (ps.executeUpdate() == 0) {
                    throw new PersistenciaException("No se pudo insertar usuario");
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        cliente.setId(rs.getInt(1));
                    } else {
                        throw new PersistenciaException("No se pudo obtener el ID generado del usuario");
                    }
                }
            }

            // insertar en clientes usando el id_usuario
            try (PreparedStatement ps = conn.prepareStatement(comandoSQLCliente)) {
                ps.setInt(1, cliente.getId());
                ps.setString(2, cliente.getNombres());
                ps.setString(3, cliente.getApellidoPaterno());
                if(!cliente.getApellidoMaterno().isEmpty() || cliente.getApellidoMaterno() != null){
                    ps.setString(4, cliente.getApellidoMaterno());
                }
                ps.setDate(5, Date.valueOf(cliente.getFechaNacimiento()));

                if (ps.executeUpdate() == 0) {
                    throw new PersistenciaException("No se pudo insertar cliente");
                }
            }

            conn.commit();
            return cliente;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {}
            }
            LOG.log(Level.WARNING, "Error al insertar al usuario/cliente. " + ex);
            throw new PersistenciaException("Error al insertar cliente", ex);

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }
    
}
