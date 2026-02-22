/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.DetallePedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.Conexion.iConexionBD;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author
 */
public class DetallePedidoDAO implements iDetallePedidoDAO{
    
    private final iConexionBD conexionBD;
    
    public DetallePedidoDAO(iConexionBD conexionBD){
        this.conexionBD = conexionBD;
    }
    
    /**
     * Logger para registrar información relevante durante operaciones de
     * persistencia.
     */
    private static final Logger LOG = Logger.getLogger(CuponDAO.class.getName());

    @Override
    public void insertarDetalles(int idPedido, List<DetallePedido> detalles) throws PersistenciaException {
        if (detalles == null || detalles.isEmpty()) {
            return;
        }

        String comandoSQL = """
                    INSERT INTO DetallesPedidos(nota, cantidad, precio, subtotal, id_pedido, id_producto)
                    VALUES(?, ?, ?, ?, ?, ?)
                    """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            for (DetallePedido d : detalles) {
                if (d == null) {
                    continue;
                }

                // nota (nullable)
                if (d.getNota() == null || d.getNota().trim().isEmpty()) {
                    ps.setNull(1, Types.VARCHAR);
                } else {
                    ps.setString(1, d.getNota().trim());
                }

                ps.setInt(2, d.getCantidad());
                ps.setFloat(3, d.getPrecio());
                ps.setFloat(4, d.getSubtotal());
                ps.setInt(5, idPedido);

                if (d.getProducto() == null) {
                    throw new PersistenciaException("Un detalle no tiene producto asignado.");
                }
                ps.setInt(6, d.getProducto().getId());

                ps.addBatch();
            }

            ps.executeBatch();

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al insertar detalles del pedido", ex);
            throw new PersistenciaException("Error al insertar detalles del pedido", ex);
        }
    }
    
}
