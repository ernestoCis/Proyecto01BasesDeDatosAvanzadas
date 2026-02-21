/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.PedidoProgramado;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import persistencia.DAOs.iPedidoDAO;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author jesus y isaac
 */
public class PedidoBO implements iPedidoBO{
    
    //DAO comun
    private iPedidoDAO pedidoDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());
    
    public PedidoBO(iPedidoDAO pedido){
        this.pedidoDAO = pedido; // asignamos valor al DAO
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
    public PedidoProgramado agregarPedidoProgramado(PedidoProgramado pedidoProgramado) throws NegocioException {
        try{
           return pedidoDAO.insertarPedidoProgramado(pedidoProgramado);
        }catch(PersistenciaException ex){
            LOG.warning("No se pudo agregar el pedido programado " + ex);
            throw new NegocioException("No se pudo agregar el pedido programado. ", ex);
        }
    }
    
}
