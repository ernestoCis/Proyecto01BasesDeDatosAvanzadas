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
 *
 * @author
 */
public class PedidoBO implements iPedidoBO {

    //DAO comun
    private iPedidoDAO pedidoDAO;
    private iDetallePedidoDAO detallePedidoDAO;
    private iCuponDAO cuponDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());

    public PedidoBO(iPedidoDAO pedido, iDetallePedidoDAO detallePedidoDAO, iCuponDAO cuponDAO) {
        this.pedidoDAO = pedido; // asignamos valor al DAO
        this.detallePedidoDAO = detallePedidoDAO;
        this.cuponDAO = cuponDAO;
    }

    @Override
    public int generarNumeroDePedido() throws NegocioException {
        try {
            return pedidoDAO.obtenerSiguienteNumeroDePedido();
        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo generar el numero de pedido. " + ex);
            throw new NegocioException("Error generando número de pedido", ex);
        }
    }

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

    @Override
    public List<Pedido> listarPedidos() throws NegocioException {
        try {
            return pedidoDAO.listarPedidos();

        } catch (PersistenciaException ex) {
            LOG.warning("No se pudieron listar los pedidos. " + ex);
            throw new NegocioException("No se pudieron listar los pedidos. " + ex.getMessage(), ex);
        }
    }

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

            contador++;

            return folio;

        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo generar el folio de pedido. " + ex);
            throw new NegocioException("Error generando número de folio", ex);
        }
    }

    @Override
    public String generarPIN() throws NegocioException {
        SecureRandom random = new SecureRandom();

        // Generamos un número aleatorio entre 0 y 99,999,999
        int numeroAleatorio = random.nextInt(100000000);

        // Lo formateamos para que SIEMPRE tenga 8 dígitos (ej. si sale 45, será "00000045")
        String pinPlano = String.format("%08d", numeroAleatorio);

        return pinPlano;

    }

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

            // Insertar pedido (debe regresar con ID seteado)
            PedidoExpress pedidoGuardado = pedidoDAO.insertarPedidoExpress(pedidoExpress, detalles);

            return pedidoGuardado;
        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo agregar el pedido express " + ex);
            throw new NegocioException("No se pudo agregar el pedido express. " + ex.getMessage(), ex);
        }
    }

    @Override
    public void actualizarEstadoPedidoExpressConPIN(int idPedido, String pinIngresado, EstadoPedido nuevoEstado) throws NegocioException {
        try {
            if (idPedido <= 0) {
                throw new NegocioException("El id del pedido no es válido.");
            }
            if (nuevoEstado == null) {
                throw new NegocioException("El estado es obligatorio.");
            }

            // Pedir PIN cuando se quiera marcar el estado como entregadoo
            if (nuevoEstado != EstadoPedido.Entregado) {
                pedidoDAO.actualizarEstadoPedido(idPedido, nuevoEstado);
                return;
            }

            if (pinIngresado == null || pinIngresado.trim().isEmpty()) {
                throw new NegocioException("Debes ingresar el PIN para entregar el pedido express.");
            }

            // 1) Consultar pedido
            Pedido pedido = pedidoDAO.consultarPedidoPorId(idPedido);

            // 2) Validar que sea express
            if (!(pedido instanceof PedidoExpress)) {
                throw new NegocioException("Este pedido no es express, no requiere PIN para entregarse.");
            }

            PedidoExpress pe = (PedidoExpress) pedido;

            // 3) Validar PIN
            boolean ok = PasswordUtil.verificar(pinIngresado.trim(), pe.getPin());
            if (!ok) {
                throw new NegocioException("PIN incorrecto. No se pudo marcar como entregado.");
            }

            // 4) Actualizar estado
            pedidoDAO.actualizarEstadoPedido(idPedido, nuevoEstado);

        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo actualizar estado con PIN. " + ex);
            throw new NegocioException("No se pudo actualizar el estado del pedido. " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Pedido> listarPedidosFiltro(String folio, String telefono, LocalDate fechaInicio, LocalDate fechaFin)
            throws NegocioException {
        try {
            return pedidoDAO.listarPedidosFiltro(folio, telefono, fechaInicio, fechaFin);
        } catch (PersistenciaException ex) {
            throw new NegocioException("No se pudieron filtrar los pedidos. " + ex.getMessage(), ex);
        }
    }

    @Override
    public void entregarPedidoExpressConPin(int idPedido, String pinPlano) throws NegocioException {
        try {
            if (idPedido <= 0) {
                throw new NegocioException("ID de pedido inválido.");
            }
            if (pinPlano == null || pinPlano.trim().isEmpty()) {
                throw new NegocioException("El PIN es obligatorio.");
            }

            // 1) Traer hash real desde la BD 
            String hashBD = pedidoDAO.obtenerPinHashPorPedido(idPedido);

            // 2) Verificar pin capturado 
            boolean ok = PasswordUtil.verificar(pinPlano.trim(), hashBD);
            if (!ok) {
                throw new NegocioException("PIN incorrecto. No se pudo entregar el pedido.");
            }

            // 3) Una vez validado, cambiar estado
            pedidoDAO.actualizarEstadoPedido(idPedido, EstadoPedido.Entregado);

        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo entregar pedido express con PIN: " + ex.getMessage());
            throw new NegocioException("No se pudo entregar el pedido: " + ex.getMessage(), ex);
        }
    }
}
