/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.DetallePedido;
import dominio.Pedido;
import dominio.PedidoExpress;
import dominio.PedidoProgramado;
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
 * @author Isaac
 */
public class PedidoDAO implements iPedidoDAO {

    /**
     * Componente encargado de crear conexiones con la base de datos. Se inyecta
     * por constructor para reducir acoplamiento y facilitar pruebas.
     */
    private final iConexionBD conexionBD;

    /**
     * Logger para registrar información relevante durante operaciones de
     * persistencia.
     */
    private static final Logger LOG = Logger.getLogger(PedidoDAO.class.getName());

    /**
     * Constructor que inicializa la dependencia de conexión.
     *
     * @param conexionBD objeto que gestiona la creación de conexiones a la base
     * de datos
     */
    public PedidoDAO(iConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public PedidoProgramado insertarPedidoProgramado(PedidoProgramado pedido) throws PersistenciaException {

        String comandoPedidoSQL = """
                                  INSERT INTO Pedidos(estado, fecha_creacion, fecha_entrega, metodo_pago, numero_pedido, id_cliente)
                                  VALUES(?,?,?,?,?,?)
                                  """;

        String comandoProgramadoSQL = """
                                   INSERT INTO PedidosProgramados(id_pedido, id_cupon)
                                   VALUES(?,?)
                                   """;

        String comandoDetalleSQL = """
                                   INSERT INTO DetallesPedidos(nota, cantidad, precio, total, id_pedido, id_producto)
                                   VALUES(?,?,?,?,?,?)
                                   """;

        // Se declara fuera del try porque la conexión se utilizara tambien en el catch y en el finally osea el rollback y cierre        
        Connection conn = null;

        try {
            conn = conexionBD.crearConexion();
            conn.setAutoCommit(false); // Se inicia la transaccion, el autocommit false evita que se guarde automaticamente cada accion

            // Se inserta en la tabla de pedidos
            try (PreparedStatement ps = conn.prepareStatement(comandoPedidoSQL, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, String.valueOf(pedido.getEstado()));
                ps.setDate(2, java.sql.Date.valueOf(pedido.getFechaCreacion().toLocalDate()));
                ps.setDate(3, java.sql.Date.valueOf(pedido.getFechaEntrega().toLocalDate()));
                ps.setString(4, String.valueOf(pedido.getMetodoPago()));
                ps.setInt(5, pedido.getNumeroPedido());

                // Se asigna el id del cliente si existe
                if (pedido.getCliente() != null) {
                    ps.setInt(6, pedido.getCliente().getId());
                } else {
                    ps.setNull(6, java.sql.Types.INTEGER);
                }

                if (ps.executeUpdate() == 0) {
                    LOG.log(Level.WARNING, "No se pudo insertar el pedido: {0}", pedido);
                    throw new PersistenciaException("No se pudo insertar el pedido");
                }

                // Obtener el id generado automaticamente
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setId(rs.getInt(1));
                    } else {
                        throw new PersistenciaException("No se pudo obtener el id del pedido");
                    }
                }
            }

            // Insertar en la tabla de programados
            try (PreparedStatement ps2 = conn.prepareStatement(comandoProgramadoSQL)) {

                // Se relaciona el registro hijo con el id generado del pedido base
                ps2.setInt(1, pedido.getId());

                // Se asigna el cupon si existe
                if (pedido.getCupon() != null) {
                    ps2.setInt(2, pedido.getCupon().getId());
                } else {
                    ps2.setNull(2, java.sql.Types.INTEGER);
                }

                ps2.executeUpdate();

            }

            // Insertar los detalles si existen
            if (!pedido.getDetalles().isEmpty()) {

                try (PreparedStatement ps3 = conn.prepareStatement(comandoDetalleSQL)) {

                    // Se recorren los detalles asociados al pedido
                    for (DetallePedido d : pedido.getDetalles()) {

                        ps3.setString(1, d.getNota());
                        ps3.setInt(2, d.getCantidad());
                        ps3.setFloat(3, d.getPrecio());
                        ps3.setFloat(4, d.getSubtotal());
                        ps3.setInt(5, pedido.getId());
                        ps3.setInt(6, d.getProducto().getId());

                        ps3.executeUpdate();
                    }

                }
            }

            conn.commit(); // Se confirma la transaccinn
            LOG.log(Level.INFO, "Pedido programado insertado correctamente. ID: {0}", pedido.getId());
            return pedido;

        } catch (SQLException ex) {

            // Si algo falla se revierte todo
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }

            LOG.log(Level.SEVERE, "Error al insertar pedido programado", ex);
            throw new PersistenciaException(ex.getMessage());

        } finally {

            // Se cierra la conexión
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public PedidoExpress insertarPedidoExpress(PedidoExpress pedido) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Pedido consultarPedidoPorId(int idPedido) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
