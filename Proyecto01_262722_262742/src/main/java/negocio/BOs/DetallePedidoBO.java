/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

/**
 *
 * @author Isaac
 */
import dominio.DetallePedido;
import java.util.List;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import persistencia.DAOs.iDetallePedidoDAO;
import persistencia.Excepciones.PersistenciaException;

public class DetallePedidoBO implements iDetallePedidoBO {

    private final iDetallePedidoDAO detallePedidoDAO;

    private static final Logger LOG = Logger.getLogger(DetallePedidoBO.class.getName());

    public DetallePedidoBO(iDetallePedidoDAO detallePedidoDAO) {
        this.detallePedidoDAO = detallePedidoDAO;
    }

    @Override
    public List<DetallePedido> listarDetallesPorPedido(int idPedido) throws NegocioException {
        try {
            if (idPedido <= 0) {
                throw new NegocioException("El id del pedido no es válido.");
            }

            return detallePedidoDAO.listarDetallesPorPedido(idPedido);

        } catch (PersistenciaException ex) {
            LOG.warning("No se pudieron listar los detalles del pedido. " + ex);
            throw new NegocioException("No se pudieron listar los detalles del pedido. " + ex.getMessage(), ex);
        }
    }

    @Override
    public float obtenerSubtotalPedido(int idPedido) throws NegocioException {
        if(idPedido <= 0){
            throw new NegocioException("Id de pedido invalido");
        }
        
        try{
            float subtotal = detallePedidoDAO.obtenerSubTotalPorPedido(idPedido);
            return subtotal;
        }catch(PersistenciaException ex){
            throw new NegocioException("Error al obtener el subtotal: " + ex.getMessage(), ex);
        }
        
    }
}
