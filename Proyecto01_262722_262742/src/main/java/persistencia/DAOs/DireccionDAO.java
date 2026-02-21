/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.Direccion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.Conexion.iConexionBD;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author
 */
public class DireccionDAO implements iDireccionDAO{
    
    private final iConexionBD conexionBD;
    
    /**
     * Logger para registrar información relevante durante operaciones de
     * persistencia.
     */
    private static final Logger LOG = Logger.getLogger(ProductoDAO.class.getName());
    
    
    public DireccionDAO(iConexionBD conexionBD){
        this.conexionBD = conexionBD;
    }

    @Override
    public Direccion insertarDireccion(Direccion direccion) throws PersistenciaException {
        String comandoSQL = """
                            INSERT INTO Direcciones(id_cliente, calle, colonia, cp, numero)
                            VALUES (?, ?, ?, ?, ?)
                            """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, direccion.getCliente().getId());
            ps.setString(2, direccion.getCalle());
            ps.setString(3, direccion.getColonia());
            ps.setInt(4, direccion.getCp());
            ps.setInt(5, direccion.getNumero());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se pudo insertar la dirección.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    direccion.setId(rs.getInt(1));
                }
            }

            return direccion;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al insertar dirección", ex);
            throw new PersistenciaException("Error al insertar dirección", ex);
        }
    }

    @Override
    public Direccion consultarDireccionPorCliente(int idCliente) throws PersistenciaException {
        String comandoSQL = """
                            SELECT id, calle, colonia, cp, numero
                            FROM direcciones
                            WHERE id_cliente = ?
                            LIMIT 1
                            """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                Direccion direccion = new Direccion();
                direccion.setId(rs.getInt("id"));
                direccion.setCalle(rs.getString("calle"));
                direccion.setColonia(rs.getString("colonia"));
                direccion.setCp(rs.getInt("cp"));
                direccion.setNumero(rs.getInt("numero"));

                return direccion;
            }

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al consultar dirección", ex);
            throw new PersistenciaException("Error al consultar dirección", ex);
        }
    }
    
}
