/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.Cliente;
import dominio.Direccion;
import dominio.EstadoCliente;
import dominio.Telefono;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
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

        String comandoSQLUsuario = """
                SELECT 
                    u.id,
                    u.usuario,
                    u.contrasenia,
                    c.nombres,
                    c.apellido_paterno,
                    c.apellido_materno,
                    c.fecha_nacimiento,
                    c.estado
                FROM usuarios u
                INNER JOIN clientes c ON c.id_usuario = u.id
                WHERE u.usuario = ?
                """;

        String comandoSQLDireccion = """
                SELECT id, calle, colonia, cp, numero
                FROM Direcciones
                WHERE id_cliente = ?
                """;

        String comandoSQLTelefonos = """
                SELECT id, telefono, etiqueta
                FROM Telefonos
                WHERE id_cliente = ?
                """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQLUsuario)) {

            ps.setString(1, usuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setUsuario(rs.getString("usuario"));          
                cliente.setContrasenia(rs.getString("contrasenia"));
                cliente.setNombres(rs.getString("nombres"));
                cliente.setApellidoPaterno(rs.getString("apellido_paterno"));
                cliente.setEstado(EstadoCliente.valueOf(rs.getString("estado")));

                String apMat = rs.getString("apellido_materno");
                if (apMat != null) {
                    cliente.setApellidoMaterno(apMat);
                }

                if (rs.getDate("fecha_nacimiento") != null) {
                    cliente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                }

                // ----- direccion -----
                try (PreparedStatement psDir = conn.prepareStatement(comandoSQLDireccion)) {
                    psDir.setInt(1, cliente.getId());

                    try (ResultSet rsDir = psDir.executeQuery()) {
                        if (rsDir.next()) {
                            Direccion dir = new Direccion();
                            dir.setId(rsDir.getInt("id"));
                            dir.setCalle(rsDir.getString("calle"));
                            dir.setColonia(rsDir.getString("colonia"));
                            dir.setCp(rsDir.getInt("cp"));
                            dir.setNumero(rsDir.getInt("numero"));
                            dir.setCliente(cliente);

                            cliente.setDireccion(dir);
                        } else {
                            cliente.setDireccion(null); // si no tiene registro de dirección
                        }
                    }
                }

                // ----- telefonos -----
                List<Telefono> telefonos = new ArrayList<>();

                try (PreparedStatement psTel = conn.prepareStatement(comandoSQLTelefonos)) {
                    psTel.setInt(1, cliente.getId());

                    try (ResultSet rsTel = psTel.executeQuery()) {
                        while (rsTel.next()) {
                            Telefono t = new Telefono();
                            t.setId(rsTel.getInt("id"));
                            t.setTelefono(rsTel.getString("telefono"));
                            t.setEtiqueta(rsTel.getString("etiqueta"));
                            t.setCliente(cliente);

                            telefonos.add(t);
                        }
                    }
                }

                cliente.setTelefonos(telefonos);

                return cliente;
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar cliente por usuario", e);
        }
    }

    @Override
    public Cliente insertarCliente(Cliente cliente, Telefono telefono) throws PersistenciaException {

        List<Telefono> telefonos = cliente.getTelefonos();

        if (telefonos == null) {
            telefonos = new ArrayList<>();
        }

        // Agregar telefono recibido (si viene)
        telefonos.add(telefono);
        
        // Re-settear la lista al cliente
        cliente.setTelefonos(telefonos);
        
        String comandoSQLUsuario = """
                                   INSERT INTO usuarios(usuario, contrasenia, rol)
                                   VALUES(?, ?, ?)
                                   """;
        
        String comandoSQLCliente = """
                                   INSERT INTO clientes(id_usuario, nombres, apellido_paterno, apellido_materno, fecha_nacimiento)
                                   VALUES(?, ?, ?, ?, ?)
                                   """;
        
        String comandoSQLDireccion = """
                                     INSERT INTO direcciones(id_cliente, calle, colonia, cp, numero)
                                     VALUES(?, ?, ?, ?, ?)
                                     """;
        
        String comandoSQLTelefono = """
                                    INSERT INTO telefonos(id_cliente, telefono, etiqueta)
                                    VALUES(?, ?, ?)
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
                ps.setString(3, cliente.getApellidoPaterno().trim());
                if(cliente.getApellidoMaterno() == null || cliente.getApellidoMaterno().trim().isEmpty()){
                    ps.setNull(4, Types.VARCHAR);
                }else{
                    ps.setString(4, cliente.getApellidoMaterno().trim());
                }
                ps.setDate(5, Date.valueOf(cliente.getFechaNacimiento()));

                if (ps.executeUpdate() == 0) {
                    throw new PersistenciaException("No se pudo insertar cliente");
                }
            }
            
            //insertar en direcciones
            if (cliente.getDireccion() != null) {
                try (PreparedStatement ps = conn.prepareStatement(comandoSQLDireccion)) {
                    ps.setInt(1, cliente.getId());
                    ps.setString(2, cliente.getDireccion().getCalle());
                    ps.setString(3, cliente.getDireccion().getColonia());
                    ps.setInt(4, cliente.getDireccion().getCp());
                    ps.setInt(5, cliente.getDireccion().getNumero());

                    if (ps.executeUpdate() == 0) {
                        throw new PersistenciaException("No se pudo insertar la dirección del cliente");
                    }
                }
            }
            
            //insertar telefono
            
            if (cliente.getTelefonos() != null && !cliente.getTelefonos().isEmpty()) {

                try (PreparedStatement ps = conn.prepareStatement(comandoSQLTelefono)) {

                    for (Telefono t : cliente.getTelefonos()) {
                        if (t == null) {
                            continue;
                        }
                        ps.setInt(1, cliente.getId()); // FK id_cliente / id_usuario
                        ps.setString(2, t.getTelefono());
                        ps.setString(3, t.getEtiqueta());

                        ps.addBatch();
                    }

                    ps.executeBatch();
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

    @Override
    public Cliente actualizarCliente(Cliente cliente) throws PersistenciaException {
        String comandoSQLUsuario = """
                            UPDATE usuarios
                            SET usuario = ?, contrasenia = ?
                            WHERE id = ?
                            """;

        String comandoSQLCliente = """
                            UPDATE clientes
                            SET nombres = ?, apellido_paterno = ?, apellido_materno = ?, fecha_nacimiento = ?, estado = ?
                            WHERE id_usuario = ?
                            """;


        String sqlUpdateDireccion = """
                                    UPDATE direcciones
                                    SET calle = ?, colonia = ?, cp = ?, numero = ?
                                    WHERE id_cliente = ?
                                    """;

        Connection conn = null;

        try {
            conn = conexionBD.crearConexion();
            conn.setAutoCommit(false);

            // ----- actualizar usuario -----
            try (PreparedStatement ps = conn.prepareStatement(comandoSQLUsuario)) {

                ps.setString(1, cliente.getUsuario().trim());
                ps.setString(2, cliente.getContrasenia().trim());
                ps.setInt(3, cliente.getId());

                if (ps.executeUpdate() == 0) {
                    throw new PersistenciaException("No se pudo actualizar el usuario (id no existe).");
                }
            }

            // ----- actualizar cliente -----
            try (PreparedStatement ps = conn.prepareStatement(comandoSQLCliente)) {
                ps.setString(1, cliente.getNombres().trim());
                ps.setString(2, cliente.getApellidoPaterno().trim());

                if (cliente.getApellidoMaterno() == null || cliente.getApellidoMaterno().trim().isEmpty()) {
                    ps.setNull(3, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(3, cliente.getApellidoMaterno().trim());
                }

                ps.setDate(4, Date.valueOf(cliente.getFechaNacimiento()));
                ps.setString(5, String.valueOf(cliente.getEstado()));
                ps.setInt(6, cliente.getId());

                if (ps.executeUpdate() == 0) {
                    throw new PersistenciaException("No se pudo actualizar el cliente (id_usuario no existe).");
                }
            }

            // ----- actualizar direccion -----
            if (cliente.getDireccion() != null) {

                try (PreparedStatement ps = conn.prepareStatement(sqlUpdateDireccion)) {

                    ps.setString(1, cliente.getDireccion().getCalle().trim());
                    ps.setString(2, cliente.getDireccion().getColonia().trim());
                    ps.setInt(3, cliente.getDireccion().getCp());
                    ps.setInt(4, cliente.getDireccion().getNumero());
                    ps.setInt(5, cliente.getId()); // id_cliente

                    if (ps.executeUpdate() == 0) {
                        throw new PersistenciaException("No se pudo actualizar la dirección.");
                    }
                }
            }

            conn.commit();
            return cliente;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            LOG.log(Level.INFO, "Error al actualizar cliente/usuario", ex);
            throw new PersistenciaException("Error al actualizar cliente/usuario", ex);

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
    
    @Override
    public List<Telefono> insertarTelefonos(int idCliente, List<Telefono> telefonos) throws PersistenciaException {
        String sqlDelete = """
                           DELETE FROM Telefonos WHERE id_cliente = ? 
                           """;

        String sqlInsert = """
                            INSERT INTO Telefonos(id_cliente, telefono, etiqueta)
                            VALUES(?, ?, ?)
                            """;

        Connection conn = null;

        try {
            conn = conexionBD.crearConexion();
            conn.setAutoCommit(false);

            // 1) Borrar todos los teléfonos del cliente
            try (PreparedStatement psDel = conn.prepareStatement(sqlDelete)) {
                psDel.setInt(1, idCliente);
                psDel.executeUpdate();
            }

            // 2) Insertar los nuevos teléfonos (si hay)
            if (telefonos != null && !telefonos.isEmpty()) {

                try (PreparedStatement psIns = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {

                    for (Telefono t : telefonos) {
                        if (t == null) {
                            continue;
                        }

                        String num = (t.getTelefono() == null) ? "" : t.getTelefono().trim();
                        if (num.isEmpty()) {
                            continue;
                        }

                        psIns.setInt(1, idCliente);
                        psIns.setString(2, num);

                        String etq = (t.getEtiqueta() == null) ? null : t.getEtiqueta().trim();
                        if (etq == null || etq.isEmpty()) {
                            psIns.setNull(3, Types.VARCHAR);
                        } else {
                            psIns.setString(3, etq);
                        }

                        psIns.addBatch();
                    }

                    psIns.executeBatch();

                    // 3) Asignar IDs generados a los objetos (si el driver los devuelve)
                    try (ResultSet rs = psIns.getGeneratedKeys()) {
                        for (Telefono t : telefonos) {
                            if (t == null) {
                                continue;
                            }

                            String num = (t.getTelefono() == null) ? "" : t.getTelefono().trim();
                            if (num.isEmpty()) {
                                continue;
                            }

                            if (rs.next()) {
                                t.setId(rs.getInt(1));
                            }
                        }
                    }
                }
            }

            conn.commit();

            // Regresar lista limpia (sin nulls ni vacíos)
            List<Telefono> limpia = new ArrayList<>();
            if (telefonos != null) {
                for (Telefono t : telefonos) {
                    if (t == null) {
                        continue;
                    }
                    String num = (t.getTelefono() == null) ? "" : t.getTelefono().trim();
                    if (num.isEmpty()) {
                        continue;
                    }
                    limpia.add(t);
                }
            }

            return limpia;

        } catch (SQLException e) {

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }

            throw new PersistenciaException("Error al reemplazar teléfonos del cliente", e);

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
