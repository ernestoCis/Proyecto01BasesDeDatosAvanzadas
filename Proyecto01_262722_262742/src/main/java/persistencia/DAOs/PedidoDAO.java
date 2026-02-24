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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import persistencia.Conexion.iConexionBD;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author 
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
                                  INSERT INTO Pedidos(estado, fecha_creacion, fecha_entrega, metodo_pago, total, numero_pedido, id_cliente)
                                  VALUES(?,?,?,?,?,?,?)
                                  """;

        String comandoProgramadoSQL = """
                                   INSERT INTO PedidosProgramados(id_pedido, id_cupon)
                                   VALUES(?,?)
                                   """;

        String comandoDetalleSQL = """
                                   INSERT INTO DetallesPedidos(nota, cantidad, precio, total, id_pedido, id_producto)
                                   VALUES(?,?,?,?,?,?)
                                   """;

        // se declara fuera del try porque la conexión se utilizara tambien en el catch y en el finally osea el rollback y cierre        
        Connection conn = null;

        try {
            conn = conexionBD.crearConexion();
            conn.setAutoCommit(false); // se inicia la transaccion, el autocommit false evita que se guarde automaticamente cada accion

            try (PreparedStatement ps = conn.prepareStatement(comandoPedidoSQL, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, String.valueOf(pedido.getEstado()));
                ps.setDate(2, java.sql.Date.valueOf(pedido.getFechaCreacion().toLocalDate()));
                if (pedido.getFechaEntrega() != null) {
                    ps.setDate(3, java.sql.Date.valueOf(pedido.getFechaEntrega().toLocalDate()));
                } else {
                    ps.setDate(3, null);
                }
                ps.setString(4, String.valueOf(pedido.getMetodoPago()));
                ps.setFloat(5, pedido.getTotal());
                ps.setInt(6, pedido.getNumeroPedido());

                if (pedido.getCliente() != null) {
                    ps.setInt(7, pedido.getCliente().getId());
                } else {
                    ps.setNull(7, java.sql.Types.INTEGER);
                }

                if (ps.executeUpdate() == 0) {
                    LOG.log(Level.WARNING, "No se pudo insertar el pedido: {0}", pedido);
                    throw new PersistenciaException("No se pudo insertar el pedido");
                }

                // obtener el id generado automaticamente
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setId(rs.getInt(1));
                    } else {
                        throw new PersistenciaException("No se pudo obtener el id del pedido");
                    }
                }
            }

            // insertar en la tabla de programados
            try (PreparedStatement ps2 = conn.prepareStatement(comandoProgramadoSQL)) {

                ps2.setInt(1, pedido.getId());

                // se asigna el cupon si existe
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

                    // cliente puede ser null
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

                    // pin/folio
                    int pinValor = rs.getInt("pin");
                    Integer pin = rs.wasNull() ? null : pinValor;
                    String folio = rs.getString("folio");

                    // cupon
                    int idCuponValor = rs.getInt("id_cupon");
                    Integer idCupon = rs.wasNull() ? null : idCuponValor;

                    // total
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

                    // tipo de pedido
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

    @Override
    public List<Pedido> listarPedidosPorCliente(int idCliente) throws PersistenciaException {
        String sql = """
                    SELECT
                        p.id,
                        p.estado,
                        p.fecha_creacion,
                        p.fecha_entrega,
                        p.metodo_pago,
                        p.total,
                        p.numero_pedido,
                        p.id_cliente,

                        pp.id_cupon,
                        c.nombre AS cupon_nombre,

                        pe.pin,
                        pe.folio

                    FROM Pedidos p
                    LEFT JOIN PedidosProgramados pp ON pp.id_pedido = p.id
                    LEFT JOIN Cupones c ON c.id = pp.id_cupon
                    LEFT JOIN PedidosExpress pe ON pe.id_pedido = p.id

                    WHERE p.id_cliente = ?
                    ORDER BY p.fecha_creacion DESC
                    """;

        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    
                    String folio = rs.getString("folio");
                    Integer pin = (Integer) rs.getObject("pin"); // getObject para permitir null
                    Integer idCupon = (Integer) rs.getObject("id_cupon");

                    Pedido pedido;

                    if (folio != null || pin != null) {
                        // ----- pedido express -----
                        PedidoExpress pe = new PedidoExpress();
                        pe.setFolio(folio);
                        if (pin != null) {
                            pe.setPin(pin);
                        }
                        pedido = pe;

                    } else {
                        // ----- pedido programado -----
                        PedidoProgramado pp = new PedidoProgramado();

                        // ----- cupon -----
                        if (idCupon != null) {
                            Cupon cupon = new Cupon();
                            cupon.setId(idCupon);

                            String cuponNombre = rs.getString("cupon_nombre");
                            cupon.setNombre(cuponNombre); // puede venir null si no existe

                            pp.setCupon(cupon);
                        } else {
                            pp.setCupon(null);
                        }

                        pedido = pp;
                    }

                    // ----- datos de pedido -----
                    pedido.setId(rs.getInt("id"));

                    pedido.setEstado(EstadoPedido.valueOf(rs.getString("estado")));
                    pedido.setMetodoPago(MetodoPago.valueOf(rs.getString("metodo_pago")));

                    
                    Timestamp tsCreacion = rs.getTimestamp("fecha_creacion");
                    if (tsCreacion != null) {
                        pedido.setFechaCreacion(tsCreacion.toLocalDateTime());
                    }

                    Timestamp tsEntrega = rs.getTimestamp("fecha_entrega");
                    if (tsEntrega != null) {
                        pedido.setFechaEntrega(tsEntrega.toLocalDateTime());
                    } else {
                        pedido.setFechaEntrega(null);
                    }

                    pedido.setTotal(rs.getFloat("total"));
                    pedido.setNumeroPedido(rs.getInt("numero_pedido"));

                    // cliente
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("id_cliente"));
                    pedido.setCliente(cliente);

                    pedidos.add(pedido);
                }
            }

            return pedidos;

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al listar pedidos por cliente", ex);
        }
    }
    
    @Override
    public List<Pedido> listarPedidosPorClienteFiltro(int idCliente, String folio,
            LocalDate fechaInicio,
            LocalDate fechaFin)
            throws PersistenciaException {

        boolean filtraFolio = (folio != null && !folio.trim().isEmpty());
        boolean filtraFechas = (!filtraFolio && fechaInicio != null && fechaFin != null);

        StringBuilder comandoSQL = new StringBuilder("""
        SELECT
            p.id,
            p.estado,
            p.fecha_creacion,
            p.fecha_entrega,
            p.metodo_pago,
            p.total,
            p.numero_pedido,
            p.id_cliente,

            pp.id_cupon,
            c.nombre AS cupon_nombre,

            pe.pin,
            pe.folio

        FROM Pedidos p
        LEFT JOIN PedidosProgramados pp ON pp.id_pedido = p.id
        LEFT JOIN Cupones c ON c.id = pp.id_cupon
        LEFT JOIN PedidosExpress pe ON pe.id_pedido = p.id
        WHERE p.id_cliente = ?
        """);

        if (filtraFolio) {
            // solo express con folio exacto
            comandoSQL.append(" AND pe.folio = ? ");
        } else if (filtraFechas) {
            comandoSQL.append(" AND p.fecha_creacion >= ? AND p.fecha_creacion <= ? ");
        }

        comandoSQL.append(" ORDER BY p.fecha_creacion DESC ");

        List<Pedido> pedidos = new java.util.ArrayList<>();

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL.toString())) {

            int idx = 1;
            ps.setInt(idx++, idCliente);

            if (filtraFolio) {
                ps.setString(idx++, folio.trim());
            } else if (filtraFechas) {
                java.time.LocalDateTime ini = fechaInicio.atStartOfDay();
                java.time.LocalDateTime fin = fechaFin.atTime(23, 59, 59);

                ps.setTimestamp(idx++, java.sql.Timestamp.valueOf(ini));
                ps.setTimestamp(idx++, java.sql.Timestamp.valueOf(fin));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    String folioDb = rs.getString("folio");
                    Integer pin = (Integer) rs.getObject("pin");
                    Integer idCupon = (Integer) rs.getObject("id_cupon");

                    Pedido pedido;

                    if (folioDb != null || pin != null) {
                        PedidoExpress pe = new PedidoExpress();
                        pe.setFolio(folioDb);
                        if (pin != null) {
                            pe.setPin(pin);
                        }
                        pedido = pe;
                    } else {
                        PedidoProgramado pp = new PedidoProgramado();
                        if (idCupon != null) {
                            Cupon cupon = new Cupon();
                            cupon.setId(idCupon);
                            cupon.setNombre(rs.getString("cupon_nombre"));
                            pp.setCupon(cupon);
                        }
                        pedido = pp;
                    }

                    pedido.setId(rs.getInt("id"));
                    pedido.setEstado(EstadoPedido.valueOf(rs.getString("estado")));
                    pedido.setMetodoPago(MetodoPago.valueOf(rs.getString("metodo_pago")));

                    java.sql.Timestamp tsC = rs.getTimestamp("fecha_creacion");
                    if (tsC != null) {
                        pedido.setFechaCreacion(tsC.toLocalDateTime());
                    }

                    java.sql.Timestamp tsE = rs.getTimestamp("fecha_entrega");
                    pedido.setFechaEntrega(tsE != null ? tsE.toLocalDateTime() : null);

                    pedido.setTotal(rs.getFloat("total"));
                    pedido.setNumeroPedido(rs.getInt("numero_pedido"));

                    Cliente cli = new Cliente();
                    cli.setId(rs.getInt("id_cliente"));
                    pedido.setCliente(cli);

                    pedidos.add(pedido);
                }
            }

            return pedidos;

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al listar pedidos filtrados", ex);
        }
    }

    @Override
    public boolean existeFolio(String folio) throws PersistenciaException {
        String comandoSQL = """
                            SELECT 1 FROM PedidosExpress WHERE folio = ? LIMIT 1
                            """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setString(1, folio);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al verificar el folio", ex);
            throw new PersistenciaException("Error al verificar eñ folio", ex);
        }
    }

}
