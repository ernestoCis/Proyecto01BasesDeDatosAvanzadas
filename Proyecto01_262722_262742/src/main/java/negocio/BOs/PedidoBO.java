/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.DetallePedido;
import dominio.PedidoProgramado;
import java.util.List;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import persistencia.DAOs.iDetallePedidoDAO;
import persistencia.DAOs.iPedidoDAO;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author jesus y isaac
 */
public class PedidoBO implements iPedidoBO{
    
    //DAO comun
    private iPedidoDAO pedidoDAO;
    private iDetallePedidoDAO detallePedidoDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());
    
    public PedidoBO(iPedidoDAO pedido, iDetallePedidoDAO detallePedidoDAO){
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
            if (detalles == null || detalles.isEmpty()) {
                throw new NegocioException("El pedido no tiene productos.");
            }

            // 1) Insertar pedido (debe regresar con ID seteado)
            PedidoProgramado pedidoGuardado = pedidoDAO.insertarPedidoProgramado(pedidoProgramado, detalles);

            return pedidoGuardado;
        }catch(PersistenciaException ex){
            LOG.warning("No se pudo agregar el pedido programado " + ex);
            throw new NegocioException("No se pudo agregar el pedido programado. ", ex);
        }
    }
    
}
