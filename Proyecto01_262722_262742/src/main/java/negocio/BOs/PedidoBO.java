/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Cupon;
import dominio.DetallePedido;
import dominio.EstadoPedido;
import dominio.Pedido;
import dominio.PedidoExpress;
import dominio.PedidoProgramado;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import negocio.util.PasswordUtil;
import persistencia.DAOs.iCuponDAO;
import persistencia.DAOs.iDetallePedidoDAO;
import persistencia.DAOs.iPedidoDAO;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Objeto de Negocio (BO) para la gestión integral de Pedidos.</b>
 * <p>Esta clase es el núcleo operativo del sistema, encargándose de:</p>
 * <ul>
 * <li>Gestionar pedidos Programados y Express.</li>
 * <li>Validar reglas de negocio (máximo 3 pedidos activos por cliente).</li>
 * <li>Controlar la vigencia y el uso de cupones de descuento.</li>
 * <li>Generar folios únicos y PINes de seguridad para entregas express.</li>
 * <li>Verificar la integridad de los detalles (productos y cantidades).</li>
 * </ul>
 * * @author 262722
 * @author 262742
 */
public class PedidoBO implements iPedidoBO {

    private iPedidoDAO pedidoDAO;
    private iDetallePedidoDAO detallePedidoDAO;
    private iCuponDAO cuponDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());

    /**
     * Constructor que inyecta los DAOs necesarios para la operación de pedidos.
     * @param pedido DAO para pedidos.
     * @param detallePedidoDAO DAO para los detalles/partidas del pedido.
     * @param cuponDAO DAO para la validación y actualización de cupones.
     */
    public PedidoBO(iPedidoDAO pedido, iDetallePedidoDAO detallePedidoDAO, iCuponDAO cuponDAO) {
        this.pedidoDAO = pedido;
        this.detallePedidoDAO = detallePedidoDAO;
        this.cuponDAO = cuponDAO;
    }

    /**
     * Obtiene el siguiente número de secuencia para un pedido desde la base de datos.
     * @return Número de pedido incremental.
     * @throws NegocioException Si hay un error al consultar la secuencia.
     */
    @Override
    public int generarNumeroDePedido() throws NegocioException {
        try {
            return pedidoDAO.obtenerSiguienteNumeroDePedido();
        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo generar el numero de pedido. " + ex);
            throw new NegocioException("Error generando número de pedido", ex);
        }
    }

    /**
     * Registra un nuevo pedido programado con sus validaciones correspondientes.
     * <p>Verifica que el cliente exista, que no exceda el límite de pedidos activos 
     * y que, en caso de usar cupón, este sea válido y no haya expirado.</p>
     * @param pedidoProgramado Datos del pedido.
     * @param detalles Lista de productos y cantidades.
     * @return El pedido registrado con ID y folio generado.
     * @throws NegocioException Si se exceden los 3 pedidos activos o el cupón es inválido.
     */
    @Override
    public PedidoProgramado agregarPedidoProgramado(PedidoProgramado pedidoProgramado, List<DetallePedido> detalles) throws NegocioException {
        try {
            if (pedidoProgramado == null) {
                throw new NegocioException("El pedido es obligatorio.");
            }

            if (pedidoProgramado.getCliente() == null) {
                throw new NegocioException("No se puede realizar un pedido programado sin cliente");
            }

            if (pedidoProgramado.getNumeroPedido() < 0) {
                throw new NegocioException("Numero de pedido invalido");
            }

            LocalDateTime hoy = LocalDateTime.now();
            if (pedidoProgramado.getFechaCreacion().isAfter(hoy)) {
                throw new NegocioException("Fecha de creacion invalida");
            }

            if (detalles == null || detalles.isEmpty()) {
                throw new NegocioException("El pedido no tiene productos.");
            }

            for (DetallePedido d : detalles) {
                if (d.getCantidad() <= 0) {
                    throw new NegocioException("No puede agregar menos de un producto");
                }
                if (d.getProducto() == null) {
                    throw new NegocioException("No puede haver un detalle sin producto");
                }
            }

            int pedidosActivos = pedidoDAO.contarPedidosActivosPorCliente(pedidoProgramado.getCliente().getId());

            if (pedidosActivos >= 3) {
                throw new NegocioException("Limite alcanzado: Ya tienes 3 pedidos activos.");
            }

            // Insertar pedido (debe regresar con ID seteado)
            PedidoProgramado pedidoGuardado = pedidoDAO.insertarPedidoProgramado(pedidoProgramado, detalles);

            //aumentar un uso del cupon si es que se usó
            if (pedidoProgramado.getCupon() != null) {
                try {
                    Cupon cupon = cuponDAO.consultarCupon(pedidoProgramado.getCupon().getNombre());

                    cuponDAO.incrementarUsoCupon(cupon.getId());

                    if (cupon.getFechaVencimiento().isBefore(LocalDate.now()) || cupon.getFechaVencimiento().equals(LocalDate.now())) {
                        throw new NegocioException("La fecha de vencimiento del cupon expiró");
                    }

                    if (cupon.getNumUsos() >= cupon.getTopeUsos()) {
                        throw new NegocioException("El cupon llegó a su limite de usos");
                    }

                } catch (PersistenciaException e) {
                    throw new NegocioException("El pedido se guardó, pero no se pudo actualizar el uso del cupón: " + e.getMessage(), e);
                }
            }

            return pedidoGuardado;
        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo agregar el pedido programado " + ex);
            throw new NegocioException("No se pudo agregar el pedido programado. ", ex);
        }
    }

    /**
     * Lista la totalidad de pedidos registrados en el sistema.
     * @return Lista de pedidos.
     * @throws NegocioException Si ocurre un error en la consulta.
     */
    @Override
    public List<Pedido> listarPedidos() throws NegocioException {
        try {
            return pedidoDAO.listarPedidos();
        } catch (PersistenciaException ex) {
            LOG.warning("No se pudieron listar los pedidos. " + ex);
            throw new NegocioException("No se pudieron listar los pedidos. " + ex.getMessage(), ex);
        }
    }

    /**
     * Cambia el estado actual de un pedido (ej. de Pendiente a En Proceso).
     * @param idPedido ID del pedido a modificar.
     * @param estado Nuevo estado (Enum <code>EstadoPedido</code>).
     * @throws NegocioException Si el ID es inválido o el estado es nulo.
     */
    @Override
    public void actualizarEstadoPedido(int idPedido, EstadoPedido estado) throws NegocioException {
        try {
            if (idPedido <= 0) {
                throw new NegocioException("El id del pedido no es válido.");
            }
            if (estado == null) {
                throw new NegocioException("El estado es obligatorio.");
            }
            pedidoDAO.actualizarEstadoPedido(idPedido, estado);
        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo actualizar el estado del pedido. " + ex);
            throw new NegocioException("No se pudo actualizar el estado del pedido. " + ex.getMessage(), ex);
        }
    }

    /**
     * Recupera el historial de pedidos de un cliente específico.
     * @param idCliente ID del cliente.
     * @return Lista de pedidos asociados al cliente.
     * @throws NegocioException Si el ID del cliente es inválido.
     */
    @Override
    public List<Pedido> listarPedidosPorCliente(int idCliente) throws NegocioException {
        try {
            if (idCliente <= 0) {
                throw new NegocioException("ID de cliente inválido");
            }
            return pedidoDAO.listarPedidosPorCliente(idCliente);
        } catch (PersistenciaException ex) {
            LOG.warning("No se pudieron listar pedidos del cliente: " + ex.getMessage());
            throw new NegocioException("No se pudieron consultar los pedidos del cliente: " + ex.getMessage(), ex);
        }
    }

    /**
     * Realiza una búsqueda filtrada de pedidos para un cliente.
     * @param idCliente ID del cliente.
     * @param folio Folio parcial o completo.
     * @param fechaInicio Fecha inicial del rango.
     * @param fechaFin Fecha final del rango.
     * @return Lista de pedidos que coinciden con los criterios.
     * @throws NegocioException Si los parámetros de búsqueda son inconsistentes.
     */
    @Override
    public List<Pedido> listarPedidosPorClienteFiltro(int idCliente, String folio, LocalDate fechaInicio, LocalDate fechaFin) throws NegocioException {
        if (idCliente <= 0) {
            throw new NegocioException("ID de cliente inválido");
        }
        try {
            return pedidoDAO.listarPedidosPorClienteFiltro(idCliente, folio, fechaInicio, fechaFin);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al filtrar: " + ex.getMessage());
        }
    }

    /**
     * Genera un folio único con formato "PE####" verificando que no exista en la BD.
     * @return Folio de pedido único.
     * @throws NegocioException Si falla la verificación de existencia en persistencia.
     */
    @Override
    public String generarFolio() throws NegocioException {
        int contador = 1;
        try {
            String folio;
            boolean existe;
            do {
                folio = String.format("PE%04d", contador);
                existe = pedidoDAO.existeFolio(folio);
                if (existe) {
                    contador++;
                }
            } while (existe);
            return folio;
        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo generar el folio de pedido. " + ex);
            throw new NegocioException("Error generando número de folio", ex);
        }
    }

    /**
     * Genera un PIN aleatorio de 8 dígitos para seguridad en entregas express.
     * @return PIN en formato de cadena de 8 dígitos.
     */
    @Override
    public String generarPIN() throws NegocioException {
        SecureRandom random = new SecureRandom();
        int numeroAleatorio = random.nextInt(100000000);
        return String.format("%08d", numeroAleatorio);
    }

    /**
     * Registra un pedido express, hasheando el PIN para almacenamiento seguro.
     * <p>Aplica la misma restricción de máximo 3 pedidos activos si el pedido 
     * está ligado a un cliente registrado.</p>
     * @param pedidoExpress Datos del pedido express (incluye PIN y Folio).
     * @param detalles Partidas del pedido.
     * @return El pedido express registrado.
     * @throws NegocioException Si faltan datos críticos como PIN o Folio.
     */
    @Override
    public PedidoExpress agregarPedidoExpress(PedidoExpress pedidoExpress, List<DetallePedido> detalles) throws NegocioException {
        try {
            if (pedidoExpress == null) {
                throw new NegocioException("El pedido es obligatorio.");
            }
            if (pedidoExpress.getFolio() == null || pedidoExpress.getFolio().trim().isEmpty()) {
                throw new NegocioException("No se puede agregar un pedido express sin un numero de folio");
            }
            if (pedidoExpress.getPin() == null || pedidoExpress.getPin().trim().isEmpty()) {
                throw new NegocioException("No se puede agregar un pedido express sin un pin");
            }
            if (pedidoExpress.getNumeroPedido() < 0) {
                throw new NegocioException("Numero de pedido invalido");
            }
            if (detalles == null || detalles.isEmpty()) {
                throw new NegocioException("El pedido no tiene productos.");
            }

            for (DetallePedido d : detalles) {
                if (d.getCantidad() <= 0) {
                    throw new NegocioException("No puede agregar menos de un producto");
                }
                if (d.getProducto() == null) {
                    throw new NegocioException("No puede haber un detalle sin producto");
                }
            }

            //hashear pin
            String hash = PasswordUtil.hash(pedidoExpress.getPin());
            pedidoExpress.setPin(hash);

            if (pedidoExpress.getCliente() != null) {
                int pedidosActivos = pedidoDAO.contarPedidosActivosPorCliente(pedidoExpress.getCliente().getId());
                if (pedidosActivos >= 3) {
                    throw new NegocioException("Limite alcanzado: Ya tienes 3 pedidos activos.");
                }
            }

            return pedidoDAO.insertarPedidoExpress(pedidoExpress, detalles);
        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo agregar el pedido express " + ex);
            throw new NegocioException("No se pudo agregar el pedido express. " + ex.getMessage(), ex);
        }
    }

    /**
     * Actualiza el estado de un pedido express validando el PIN si el estado es 'Entregado'.
     * @param idPedido ID del pedido.
     * @param pinIngresado PIN proporcionado por el cliente al momento de la entrega.
     * @param nuevoEstado Estado al que se desea transicionar.
     * @throws NegocioException Si el PIN es incorrecto o el pedido no es de tipo Express.
     */
    @Override
    public void actualizarEstadoPedidoExpressConPIN(int idPedido, String pinIngresado, EstadoPedido nuevoEstado) throws NegocioException {
        try {
            if (idPedido <= 0) {
                throw new NegocioException("El id del pedido no es válido.");
            }
            if (nuevoEstado == null) {
                throw new NegocioException("El estado es obligatorio.");
            }

            if (nuevoEstado != EstadoPedido.Entregado) {
                pedidoDAO.actualizarEstadoPedido(idPedido, nuevoEstado);
                return;
            }

            if (pinIngresado == null || pinIngresado.trim().isEmpty()) {
                throw new NegocioException("Debes ingresar el PIN para entregar el pedido express.");
            }

            Pedido pedido = pedidoDAO.consultarPedidoPorId(idPedido);

            if (!(pedido instanceof PedidoExpress)) {
                throw new NegocioException("Este pedido no es express, no requiere PIN para entregarse.");
            }

            PedidoExpress pe = (PedidoExpress) pedido;
            boolean ok = PasswordUtil.verificar(pinIngresado.trim(), pe.getPin());
            if (!ok) {
                throw new NegocioException("PIN incorrecto. No se pudo marcar como entregado.");
            }

            pedidoDAO.actualizarEstadoPedido(idPedido, nuevoEstado);

        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo actualizar estado con PIN. " + ex);
            throw new NegocioException("No se pudo actualizar el estado del pedido. " + ex.getMessage(), ex);
        }
    }

    /**
     * Filtra pedidos basándose en folio, teléfono del cliente o rango de fechas.
     * @return Lista de pedidos filtrados.
     */
    @Override
    public List<Pedido> listarPedidosFiltro(String folio, String telefono, LocalDate fechaInicio, LocalDate fechaFin) throws NegocioException {
        try {
            return pedidoDAO.listarPedidosFiltro(folio, telefono, fechaInicio, fechaFin);
        } catch (PersistenciaException ex) {
            throw new NegocioException("No se pudieron filtrar los pedidos. " + ex.getMessage(), ex);
        }
    }

    /**
     * Método especializado para la entrega física de pedidos express mediante PIN.
     * @param idPedido ID del pedido express.
     * @param pinPlano PIN sin cifrar capturado en el punto de entrega.
     * @throws NegocioException Si el PIN no coincide con el hash almacenado.
     */
    @Override
    public void entregarPedidoExpressConPin(int idPedido, String pinPlano) throws NegocioException {
        try {
            if (idPedido <= 0) {
                throw new NegocioException("ID de pedido inválido.");
            }
            if (pinPlano == null || pinPlano.trim().isEmpty()) {
                throw new NegocioException("El PIN es obligatorio.");
            }

            String hashBD = pedidoDAO.obtenerPinHashPorPedido(idPedido);
            boolean ok = PasswordUtil.verificar(pinPlano.trim(), hashBD);
            if (!ok) {
                throw new NegocioException("PIN incorrecto. No se pudo entregar el pedido.");
            }

            pedidoDAO.actualizarEstadoPedido(idPedido, EstadoPedido.Entregado);

        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo entregar pedido express con PIN: " + ex.getMessage());
            throw new NegocioException("No se pudo entregar el pedido: " + ex.getMessage(), ex);
        }
    }
}
