/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.DetallePedido;
import dominio.EstadoProducto;
import dominio.Producto;
import dominio.TipoProducto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class DetallePedidoDAO implements iDetallePedidoDAO {

    private final iConexionBD conexionBD;

    public DetallePedidoDAO(iConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Logger para registrar información relevante durante operaciones de
     * persistencia.
     */
    private static final Logger LOG = Logger.getLogger(DetallePedidoDAO.class.getName());

    @Override
    public void insertarDetalles(int idPedido, List<DetallePedido> detalles) throws PersistenciaException {
        if (detalles == null || detalles.isEmpty()) {
            return;
        }

        String comandoSQL = """
                    INSERT INTO DetallesPedidos(nota, cantidad, precio, total, id_pedido, id_producto)
                    VALUES(?, ?, ?, ?, ?, ?)
                    """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            for (DetallePedido d : detalles) {
                if (d == null) {
                    continue;
                }

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

    /**
     * Metodo que lista los detalles (productos) asociados a un pedido.
     *
     * @param idPedido id del pedido
     * @return lista de detalles encontrados
     * @throws PersistenciaException si ocurre un error de SQL
     */
    @Override
    public List<DetallePedido> listarDetallesPorPedido(int idPedido) throws PersistenciaException {

        String comandoSQL = """
                SELECT dp.id, dp.nota, dp.cantidad, dp.precio, dp.total, dp.id_producto,  p.nombre,  p.tipo, p.estado, p.descripcion
                FROM DetallesPedidos dp
                INNER JOIN Productos p ON p.id = dp.id_producto
                WHERE dp.id_pedido = ?
                ORDER BY dp.id ASC
                """;

        List<DetallePedido> detalles = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Producto producto = new Producto();
                    producto.setId(rs.getInt("id_producto"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setTipo(TipoProducto.valueOf(rs.getString("tipo")));
                    producto.setEstado(EstadoProducto.valueOf(rs.getString("estado").replace(" ", "_")));
                    producto.setDescripcion(rs.getString("descripcion"));

                    // ===== Detalle =====
                    DetallePedido d = new DetallePedido();
                    d.setId(rs.getInt("id"));
                    d.setNota(rs.getString("nota")); // puede ser null
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecio(rs.getFloat("precio"));

                    d.setSubtotal(rs.getFloat("total"));

                    d.setProducto(producto);

                    d.setPedido(null);

                    detalles.add(d);
                }
            }

            return detalles;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al listar detalles del pedido", ex);
            throw new PersistenciaException("Error al listar detalles del pedido", ex);
        }
    }
}
