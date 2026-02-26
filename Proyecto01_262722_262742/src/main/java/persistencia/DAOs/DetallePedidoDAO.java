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
 * <b>Data Access Object (DAO) para los Detalles de Pedido.</b>
 * <p>Esta clase se encarga de gestionar todas las operaciones de persistencia 
 * (inserción y lectura) en la tabla <code>DetallesPedidos</code> de la base de datos. 
 * Implementa un enfoque de procesamiento por lotes (batching) para optimizar 
 * la inserción múltiple de partidas.</p>
 *
 * @author 262722
 * @author 262742
 */
public class DetallePedidoDAO implements iDetallePedidoDAO {

    private final iConexionBD conexionBD;

    /**
     * Logger para registrar información relevante y errores durante las operaciones de persistencia.
     */
    private static final Logger LOG = Logger.getLogger(DetallePedidoDAO.class.getName());

    /**
     * Constructor que inyecta la dependencia de la conexión a la base de datos.
     * @param conexionBD Implementación de la interfaz de conexión.
     */
    public DetallePedidoDAO(iConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Inserta una lista de detalles asociados a un pedido utilizando procesamiento por lotes.
     * <p>Prepara una única sentencia SQL y agrega cada detalle al lote (batch) 
     * para luego ejecutarlos todos juntos, mejorando significativamente el rendimiento.</p>
     *
     * @param idPedido Identificador del pedido al que pertenecen los detalles.
     * @param detalles Lista de objetos {@link DetallePedido} a insertar.
     * @throws PersistenciaException Si la lista contiene detalles sin producto o si ocurre un error en SQL.
     */
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

                // Se agrega la instrucción al lote en lugar de ejecutarla inmediatamente
                ps.addBatch();
            }

            // Ejecuta todas las inserciones preparadas en un solo viaje a la BD
            ps.executeBatch();

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al insertar detalles del pedido", ex);
            throw new PersistenciaException("Error al insertar detalles del pedido", ex);
        }
    }

    /**
     * Recupera todos los detalles (productos, cantidades, subtotales) asociados a un pedido específico.
     * <p>Realiza un <code>INNER JOIN</code> con la tabla <code>Productos</code> para 
     * mapear la información completa de cada artículo en un solo viaje.</p>
     *
     * @param idPedido Identificador del pedido a consultar.
     * @return Una lista de objetos {@link DetallePedido} poblados con la información de la base de datos.
     * @throws PersistenciaException Si ocurre un error de SQL durante la consulta.
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
                    
                    // El replace garantiza que los espacios en la BD coincidan con el Enum en Java
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

    /**
     * Calcula y obtiene el subtotal de un pedido sumando directamente los totales en la base de datos.
     * <p>Utiliza la función <code>COALESCE</code> para devolver 0 de forma segura en caso 
     * de que el pedido aún no tenga detalles registrados, evitando valores nulos.</p>
     *
     * @param idPedido Identificador del pedido a consultar.
     * @return El subtotal del pedido calculado por la base de datos.
     * @throws PersistenciaException Si ocurre un error al ejecutar la consulta sumatoria.
     */
    @Override
    public float obtenerSubTotalPorPedido(int idPedido) throws PersistenciaException {
        String comandoSQL = """
                    SELECT COALESCE(SUM(total), 0) AS subtotal
                    FROM DetallesPedidos
                    WHERE id_pedido = ?
                    """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat("subtotal");
                }
                return 0f;
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al obtener subtotal del pedido", ex);
        }
    }
}