/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.Telefono;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class TelefonoDAO implements iTelefonoDAO{
    
    private final iConexionBD conexionBD;
    
    /**
     * Logger para registrar información relevante durante operaciones de
     * persistencia.
     */
    private static final Logger LOG = Logger.getLogger(ProductoDAO.class.getName());
    
    public TelefonoDAO(iConexionBD conexionBD){
        this.conexionBD = conexionBD;
    }

    @Override
    public Telefono insertarTelefono(Telefono telefono) throws PersistenciaException {
        String comandoSQL = """
                    INSERT INTO telefonos(id_cliente, telefono, etiqueta)
                    VALUES (?, ?, ?)
                    """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, telefono.getCliente().getId());
            ps.setString(2, telefono.getTelefono());
            ps.setString(3, telefono.getEtiqueta());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se pudo insertar el teléfono.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    telefono.setId(rs.getInt(1));
                }
            }

            return telefono;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE,"Error al insertar teléfono", ex);
            throw new PersistenciaException("Error al insertar teléfono", ex);
        }
    }

    @Override
    public List<Telefono> consultarTelefonosPorCliente(int idCliente) throws PersistenciaException {
        String comandoSQL = """
                        SELECT id, telefono, etiqueta
                        FROM Telefonos
                        WHERE id_cliente = ?
                        """;

        List<Telefono> telefonos = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Telefono telefono = new Telefono();
                    telefono.setId(rs.getInt("id"));
                    telefono.setTelefono(rs.getString("telefono"));
                    telefono.setEtiqueta(rs.getString("etiqueta"));

                    telefonos.add(telefono);
                }
            }

            return telefonos;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al consultar teléfonos", ex);
            throw new PersistenciaException("Error al consultar teléfonos", ex);
        }
    }
    
}
