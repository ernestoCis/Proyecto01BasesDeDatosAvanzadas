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
 * <b>Clase DAO concreta para la gestión de Cupones de descuento.</b>
 * <p>Esta clase implementa la interfaz <code>iCuponDAO</code> y centraliza las 
 * operaciones de lectura y actualización de cupones en la base de datos. Permite 
 * validar la existencia de promociones y llevar el control del conteo de usos 
 * para respetar los límites establecidos por el negocio.</p>
 *
 * @author 262722
 * @author 262742
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
     * Constructor que inicializa la conexión inyectando la dependencia de la BD.
     * @param conexionBD Interfaz que provee el acceso a la conexión física.
     */
    public CuponDAO(iConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Consulta los datos detallados de un cupón mediante su nombre identificador.
     * <p>Recupera información crítica como el porcentaje de descuento, las fechas 
     * de vigencia y el estado actual de los usos registrados.</p>
     * * @param nombreCupon El nombre o código del cupón a buscar (ej. "PROMO2026").
     * @return Un objeto <code>Cupon</code> con la información recuperada, o <code>null</code> 
     * si no existe un registro con ese nombre.
     * @throws PersistenciaException Si ocurre una falla en la ejecución del SQL o en la conexión.
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

    /**
     * Incrementa en una unidad el contador de usos de un cupón específico.
     * <p>Este método es fundamental para el control de inventario de promociones, 
     * asegurando que cada vez que se finaliza un pedido con cupón, este se registre 
     * para no exceder el <code>tope_usos</code>.</p>
     * * @param idCupon El identificador único (ID) del cupón a actualizar.
     * @throws PersistenciaException Si el ID no existe o si hay un error de comunicación con la BD.
     */
    @Override
    public void incrementarUsoCupon(int idCupon) throws PersistenciaException {
        String comandoSQL = "UPDATE cupones SET numero_usos = numero_usos + 1 WHERE id = ?";

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