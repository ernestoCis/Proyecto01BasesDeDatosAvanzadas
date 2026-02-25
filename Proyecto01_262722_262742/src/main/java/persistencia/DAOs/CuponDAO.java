/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.Cupon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.Conexion.iConexionBD;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author
 */
public class CuponDAO implements iCuponDAO{
    
    /**
     * Componente encargado de crear conexiones con la base de datos. Se inyecta
     * por constructor para reducir acoplamiento y facilitar pruebas.
     */
    private final iConexionBD conexionBD;

    /**
     * Logger para registrar información relevante durante operaciones de
     * persistencia.
     */
    private static final Logger LOG = Logger.getLogger(CuponDAO.class.getName());
    
    /**
     * metodo para inicializar la conexion
     * @param conexionBD objeto de conexión
     */
    public CuponDAO(iConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * metodo para consultar los datos de un cupon
     * @param nombreCupon nombre del cupon a buscar
     * @return cupon consultado
     * @throws PersistenciaException excepcion por si falla el sql
     */
    @Override
    public Cupon consultarCupon(String nombreCupon) throws PersistenciaException {
        String comandoSQL = """
                            SELECT id, descuento, fecha_vencimiento, fecha_inicio, nombre, numero_usos, tope_usos FROM Cupones
                            WHERE nombre = ?
                            """;
        
        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setString(1, nombreCupon);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return new Cupon(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("descuento"),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getDate("fecha_vencimiento").toLocalDate(),
                        rs.getInt("numero_usos"),
                        rs.getInt("tope_usos")
                        
                );
            }

        }catch(SQLException ex){
            LOG.log(Level.SEVERE, "Error de SQL al consultar el cupon", ex);
            throw new PersistenciaException(ex.getMessage());
        }
        
    }

    @Override
    public void incrementarUsoCupon(int idCupon) throws PersistenciaException {
        String comandoSQL = "UPDATE cupones SET num_usos = num_usos + 1 WHERE id = ?";

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idCupon);

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new PersistenciaException("No se pudo incrementar el uso del cupón (no se encontró).");
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al incrementar uso del cupón", ex);
        }
    }
}
