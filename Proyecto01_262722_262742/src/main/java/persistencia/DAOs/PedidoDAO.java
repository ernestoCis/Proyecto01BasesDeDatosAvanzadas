/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.Cliente;
import dominio.Cupon;
import dominio.DetallePedido;
import dominio.EstadoPedido;
import dominio.MetodoPago;
import dominio.Pedido;
import dominio.PedidoExpress;
import dominio.PedidoProgramado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
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
                if(pedido.getFechaEntrega() != null){
                    ps.setDate(3, java.sql.Date.valueOf(pedido.getFechaEntrega().toLocalDate()));
                }else{
                    ps.setDate(3, null);
                }
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

        String comandoSQL = """
                        SELECT Pedidos.id, Pedidos.estado, Pedidos.fecha_creacion, Pedidos.fecha_entrega, Pedidos.metodo_pago, Pedidos.numero_pedido, Pedidos.id_cliente,
                        Clientes.nombres, Clientes.apellido_paterno, Clientes.apellido_materno, Clientes.fecha_nacimiento,
                        PedidosExpress.pin, PedidosExpress.folio,
                        PedidosProgramados.id_cupon
                        FROM Pedidos
                        INNER JOIN Clientes ON Pedidos.id_cliente = Clientes.id_usuario
                        LEFT JOIN PedidosExpress ON PedidosExpress.id_pedido = Pedidos.id
                        LEFT JOIN PedidosProgramados ON PedidosProgramados.id_pedido = Pedidos.id
                        WHERE Pedidos.id = ?
                        """;

        String totalSQL = """
                          SELECT SUM(DetallesPedidos.total) AS total_pedido
                          FROM DetallesPedidos
                          WHERE DetallesPedidos.id_pedido = ?
                          """;

        try (Connection conn = this.conexionBD.crearConexion()) {

            Pedido pedido;

            // Se prepara la consulta principal
            try (PreparedStatement ps = conn.prepareStatement(comandoSQL)) {
                ps.setInt(1, idPedido);

                // Se ejecuta la consulta
                try (ResultSet rs = ps.executeQuery()) {

                    // Se valida que exista el pedido
                    if (!rs.next()) {
                        LOG.log(Level.WARNING, "No se encontro el pedido con id {0}", idPedido);
                        throw new PersistenciaException("No existe el pedido con el ID proporcionado.");
                    }

                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("id_cliente"));
                    cliente.setNombres(rs.getString("nombres"));
                    cliente.setApellidoPaterno(rs.getString("apellido_paterno"));
                    cliente.setApellidoMaterno(rs.getString("apellido_materno"));
                    cliente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());

                    // Datos comunes del pedido
                    int id = rs.getInt("id");
                    EstadoPedido estado = EstadoPedido.valueOf(rs.getString("estado").toUpperCase());
                    LocalDateTime fechaCreacion = rs.getDate("fecha_creacion").toLocalDate().atStartOfDay();
                    LocalDateTime fechaEntrega = rs.getDate("fecha_entrega").toLocalDate().atStartOfDay();
                    MetodoPago metodoPago = MetodoPago.valueOf(rs.getString("metodo_pago").toUpperCase());
                    int numeroPedido = rs.getInt("numero_pedido");

                    // Obtener pin en caso de ser pedido express
                    int pinValor = rs.getInt("pin");
                    Integer pin;
                    if (rs.wasNull()) {
                        pin = null;
                    } else {
                        pin = pinValor;
                    }

                    // Obtener folio (puede ser null si no es express)
                    String folio = rs.getString("folio");

                    // Obtener id del cupón en caso de ser pedido programado
                    int idCuponValor = rs.getInt("id_cupon");
                    Integer idCupon;
                    if (rs.wasNull()) {
                        idCupon = null;
                    } else {
                        idCupon = idCuponValor;
                    }

                    // Calcular el total del pedido
                    float total;
                    try (PreparedStatement psTotal = conn.prepareStatement(totalSQL)) {
                        psTotal.setInt(1, idPedido);

                        try (ResultSet rsTotal = psTotal.executeQuery()) {
                            rsTotal.next();
                            total = rsTotal.getFloat("total_pedido");
                        }
                    }

                    // Crear cupon si existe
                    Cupon cupon = null;
                    if (idCupon != null) {
                        cupon = new Cupon();
                        cupon.setId(idCupon);
                    }

                    // Determinar el tipo de pedido 
                    if (pin != null) {
                        pedido = new PedidoExpress(
                                id, estado, fechaCreacion, fechaEntrega,
                                metodoPago, total, numeroPedido,
                                cliente, pin, folio
                        );
                    } else {
                        pedido = new PedidoProgramado(
                                id, estado, fechaCreacion, fechaEntrega,
                                metodoPago, total, numeroPedido,
                                cliente, cupon
                        );
                    }
                }
            }

            return pedido;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error de SQL al consultar el pedido", ex);
            throw new PersistenciaException(ex.getMessage());
        }

    }

    /**
     * metodo que consulta en la BD si el numeroPedido ya esta registrado
     * @param numeroPedido numero de pedido a validar
     * @return true= si ya esta registrado, false = si no esta registrado
     * @throws PersistenciaException excepcion por si el sql falla
     */
    @Override
    public boolean existeNumeroDePedido(int numeroPedido) throws PersistenciaException {
        String comandoSQL = """
                            SELECT 1 FROM pedidos WHERE numero_pedido = ? LIMIT 1
                            """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, numeroPedido);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al verificar número de pedido", ex);
            throw new PersistenciaException("Error al verificar número de pedido", ex);
        }
    }

}
