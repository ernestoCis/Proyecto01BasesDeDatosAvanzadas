/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

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
import persistencia.DAOs.iDetallePedidoDAO;
import persistencia.DAOs.iPedidoDAO;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author jesus y isaac
 */
public class PedidoBO implements iPedidoBO {

    //DAO comun
    private iPedidoDAO pedidoDAO;
    private iDetallePedidoDAO detallePedidoDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());

    public PedidoBO(iPedidoDAO pedido, iDetallePedidoDAO detallePedidoDAO) {
        this.pedidoDAO = pedido; // asignamos valor al DAO
        this.detallePedidoDAO = detallePedidoDAO;
    }

    @Override
    public int generarNumeroDePedido() throws NegocioException {
        try {
            int numero;
            boolean existe;

            do {
                numero = (int) (System.currentTimeMillis() % 1000000);
                existe = pedidoDAO.existeNumeroDePedido(numero);
            } while (existe);

            return numero;

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
            
            if(pedidoProgramado.getCliente() == null){
                throw new NegocioException("No se puede realizar un pedido programado sin cliente");
            }
            
            if(pedidoProgramado.getNumeroPedido() < 0){
                throw new NegocioException("Numero de pedido invalido");
            }
            
            LocalDateTime hoy = LocalDateTime.now();
            if(pedidoProgramado.getFechaCreacion().isAfter(hoy)){
                throw new NegocioException("Fecha de creacion invalida");
            }
            
            if (detalles == null || detalles.isEmpty()) {
                throw new NegocioException("El pedido no tiene productos.");
            }
            
            for(DetallePedido d : detalles){
                if(d.getCantidad() <= 0){
                    throw new NegocioException("No puede agregar menos de un producto");
                }
                if(d.getProducto() == null){
                    throw new NegocioException("No puede haver un detalle sin producto");
                }
            }

            // 1) Insertar pedido (debe regresar con ID seteado)
            PedidoProgramado pedidoGuardado = pedidoDAO.insertarPedidoProgramado(pedidoProgramado, detalles);

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
        
        try{
            return pedidoDAO.listarPedidosPorClienteFiltro(idCliente, folio, fechaInicio, fechaFin);
        }catch(PersistenciaException ex){
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
            
            if(pedidoExpress.getFolio() == null || pedidoExpress.getFolio().trim().isEmpty()){
                throw new NegocioException("No se puede agregar un pedido express sin un numero de folio");
            }
            
            if(pedidoExpress.getPin() == null || pedidoExpress.getPin().trim().isEmpty()){
                throw new NegocioException("No se puede agregar un pedido express sin un pin");
            }
            
            if(pedidoExpress.getNumeroPedido() < 0){
                throw new NegocioException("Numero de pedido invalido");
            }
            
            if (detalles == null || detalles.isEmpty()) {
                throw new NegocioException("El pedido no tiene productos.");
            }
            
            for(DetallePedido d : detalles){
                if(d.getCantidad() <= 0){
                    throw new NegocioException("No puede agregar menos de un producto");
                }
                if(d.getProducto() == null){
                    throw new NegocioException("No puede haver un detalle sin producto");
                }
            }
            
            //hashear pin
            String hash = PasswordUtil.hash(pedidoExpress.getPin());
            pedidoExpress.setPin(hash);

            // 1) Insertar pedido (debe regresar con ID seteado)
            PedidoExpress pedidoGuardado = pedidoDAO.insertarPedidoExpress(pedidoExpress, detalles);

            return pedidoGuardado;
        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo agregar el pedido express " + ex);
            throw new NegocioException("No se pudo agregar el pedido express. ", ex);
        }
    }

}
