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
import java.util.ArrayList;
import java.util.List;
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
    public PedidoProgramado insertarPedidoProgramado(PedidoProgramado pedido, List<DetallePedido> detalles) throws PersistenciaException {

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
                if (pedido.getFechaEntrega() != null) {
                    ps.setDate(3, java.sql.Date.valueOf(pedido.getFechaEntrega().toLocalDate()));
                } else {
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

                if (ps2.executeUpdate() == 0) {
                    throw new PersistenciaException("No se pudo insertar el pedido programado (tabla hija).");
                }
            }

            //insertar detalles pedidos
            if (detalles == null || detalles.isEmpty()) {
                throw new PersistenciaException("No se puede insertar un pedido sin detalles.");
            }

            try (PreparedStatement ps3 = conn.prepareStatement(comandoDetalleSQL)) {

                for (DetallePedido d : detalles) {
                    if (d == null) {
                        continue;
                    }

                    // nota puede ser null
                    String nota = d.getNota();
                    if (nota == null || nota.trim().isEmpty()) {
                        ps3.setNull(1, java.sql.Types.VARCHAR);
                    } else {
                        ps3.setString(1, nota.trim());
                    }

                    ps3.setInt(2, d.getCantidad());
                    ps3.setFloat(3, d.getPrecio());
                    ps3.setFloat(4, d.getSubtotal());

                    ps3.setInt(5, pedido.getId());

                    // producto obligatorio
                    if (d.getProducto() == null) {
                        throw new PersistenciaException("Un detalle no tiene producto asignado.");
                    }
                    ps3.setInt(6, d.getProducto().getId());

                    ps3.addBatch();
                }

                ps3.executeBatch();
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
        SELECT 
            p.id, p.estado, p.fecha_creacion, p.fecha_entrega, p.metodo_pago, p.numero_pedido, p.id_cliente,
            c.nombres, c.apellido_paterno, c.apellido_materno, c.fecha_nacimiento,
            pe.pin, pe.folio,
            pp.id_cupon
        FROM Pedidos p
        LEFT JOIN Clientes c ON p.id_cliente = c.id_usuario
        LEFT JOIN PedidosExpress pe ON pe.id_pedido = p.id
        LEFT JOIN PedidosProgramados pp ON pp.id_pedido = p.id
        WHERE p.id = ?
        """;

        String totalSQL = """
        SELECT SUM(dp.total) AS total_pedido
        FROM DetallesPedidos dp
        WHERE dp.id_pedido = ?
        """;

        try (Connection conn = this.conexionBD.crearConexion()) {

            Pedido pedido;

            try (PreparedStatement ps = conn.prepareStatement(comandoSQL)) {
                ps.setInt(1, idPedido);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        LOG.log(Level.WARNING, "No se encontro el pedido con id {0}", idPedido);
                        throw new PersistenciaException("No existe el pedido con el ID proporcionado.");
                    }

                    // ===== Cliente (puede ser null) =====
                    Cliente cliente = null;
                    int idCliente = rs.getInt("id_cliente");
                    if (!rs.wasNull()) {
                        cliente = new Cliente();
                        cliente.setId(idCliente);
                        cliente.setNombres(rs.getString("nombres"));
                        cliente.setApellidoPaterno(rs.getString("apellido_paterno"));
                        cliente.setApellidoMaterno(rs.getString("apellido_materno"));

                        java.sql.Date fn = rs.getDate("fecha_nacimiento");
                        if (fn != null) {
                            cliente.setFechaNacimiento(fn.toLocalDate());
                        }
                    }

                    // ===== Datos comunes del pedido =====
                    int id = rs.getInt("id");

                    EstadoPedido estado = EstadoPedido.valueOf(rs.getString("estado").replace(" ", "_")
                    );

                    LocalDateTime fechaCreacion = rs.getDate("fecha_creacion").toLocalDate().atStartOfDay();

                    java.sql.Date fechaEntregaSQL = rs.getDate("fecha_entrega");
                    LocalDateTime fechaEntrega = null;
                    if (fechaEntregaSQL != null) {
                        fechaEntrega = fechaEntregaSQL.toLocalDate().atStartOfDay();
                    }

                    MetodoPago metodoPago = MetodoPago.valueOf(rs.getString("metodo_pago"));
                    int numeroPedido = rs.getInt("numero_pedido");

                    // ===== pin/folio (express) =====
                    int pinValor = rs.getInt("pin");
                    Integer pin = rs.wasNull() ? null : pinValor;
                    String folio = rs.getString("folio");

                    // ===== cupon (programado) =====
                    int idCuponValor = rs.getInt("id_cupon");
                    Integer idCupon = rs.wasNull() ? null : idCuponValor;

                    // ===== total =====
                    float total = 0;
                    try (PreparedStatement psTotal = conn.prepareStatement(totalSQL)) {
                        psTotal.setInt(1, idPedido);
                        try (ResultSet rsTotal = psTotal.executeQuery()) {
                            if (rsTotal.next()) {
                                total = rsTotal.getFloat("total_pedido");
                                if (rsTotal.wasNull()) {
                                    total = 0;
                                }
                            }
                        }
                    }

                    // Crear cupon si existe 
                    Cupon cupon = null;
                    if (idCupon != null) {
                        cupon = new Cupon();
                        cupon.setId(idCupon);
                    }

                    // ===== Determinar tipo =====
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
            throw new PersistenciaException(ex.getMessage(), ex);
        }
    }

    /**
     * metodo que consulta en la BD si el numeroPedido ya esta registrado
     *
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

    @Override
    public List<Pedido> listarPedidos() throws PersistenciaException {

        String comandoSQL = """
                            SELECT 
                                p.id, p.estado, p.fecha_creacion, p.fecha_entrega, p.metodo_pago, p.numero_pedido, p.id_cliente,
                                c.nombres, c.apellido_paterno, c.apellido_materno, c.fecha_nacimiento,
                                pe.pin, pe.folio,
                                pp.id_cupon
                            FROM Pedidos p
                            LEFT JOIN Clientes c ON p.id_cliente = c.id_usuario
                            LEFT JOIN PedidosExpress pe ON pe.id_pedido = p.id
                            LEFT JOIN PedidosProgramados pp ON pp.id_pedido = p.id
                            ORDER BY 
                                CASE WHEN p.estado = 'Entregado' THEN 1 ELSE 0 END,
                                p.fecha_creacion DESC
                            """;

        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                // Cliente (puede ser null)
                Cliente cliente = null;
                int idCliente = rs.getInt("id_cliente");
                if (!rs.wasNull()) {
                    cliente = new Cliente();
                    cliente.setId(idCliente);
                    cliente.setNombres(rs.getString("nombres"));
                    cliente.setApellidoPaterno(rs.getString("apellido_paterno"));
                    cliente.setApellidoMaterno(rs.getString("apellido_materno"));

                    java.sql.Date fn = rs.getDate("fecha_nacimiento");
                    if (fn != null) {
                        cliente.setFechaNacimiento(fn.toLocalDate());
                    }
                }

                int id = rs.getInt("id");
                EstadoPedido estado = EstadoPedido.valueOf(rs.getString("estado").replace(" ", "_"));

                LocalDateTime fechaCreacion = rs.getDate("fecha_creacion").toLocalDate().atStartOfDay();

                java.sql.Date fechaEntregaSQL = rs.getDate("fecha_entrega");
                LocalDateTime fechaEntrega = null;
                if (fechaEntregaSQL != null) {
                    fechaEntrega = fechaEntregaSQL.toLocalDate().atStartOfDay();
                }

                MetodoPago metodoPago = MetodoPago.valueOf(rs.getString("metodo_pago"));
                int numeroPedido = rs.getInt("numero_pedido");

                int pinValor = rs.getInt("pin");
                Integer pin = rs.wasNull() ? null : pinValor;
                String folio = rs.getString("folio");

                int idCuponValor = rs.getInt("id_cupon");
                Integer idCupon = rs.wasNull() ? null : idCuponValor;

                float total = 0;

                Cupon cupon = null;
                if (idCupon != null) {
                    cupon = new Cupon();
                    cupon.setId(idCupon);
                }

                Pedido pedido;
                if (pin != null) {
                    pedido = new PedidoExpress(id, estado, fechaCreacion, fechaEntrega, metodoPago, total, numeroPedido, cliente, pin, folio);
                } else {
                    pedido = new PedidoProgramado(id, estado, fechaCreacion, fechaEntrega, metodoPago, total, numeroPedido, cliente, cupon);
                }

                pedidos.add(pedido);
            }

            return pedidos;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error SQL al listar pedidos", ex);
            throw new PersistenciaException("Error al listar pedidos", ex);
        }
    }

    @Override
    public void actualizarEstadoPedido(int idPedido, EstadoPedido nuevoEstado) throws PersistenciaException {

        String comandoSQL = """
                            UPDATE Pedidos
                            SET estado = ?
                            WHERE id = ?
                            """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            String estadoBD = nuevoEstado.name().replace("_", " ");

            ps.setString(1, estadoBD);
            ps.setInt(2, idPedido);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se pudo actualizar: el pedido no existe.");
            }

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error SQL al actualizar estado del pedido", ex);
            throw new PersistenciaException("Error al actualizar estado del pedido", ex);
        }
    }

}
