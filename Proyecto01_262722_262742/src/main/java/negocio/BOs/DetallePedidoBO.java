/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.DetallePedido;
import java.util.List;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import persistencia.DAOs.iDetallePedidoDAO;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Objeto de Negocio (BO) para la gestión de los Detalles de Pedido.</b>
 * <p>Esta clase se encarga de la lógica relacionada con las partidas individuales 
 * de un pedido, permitiendo recuperar la lista de productos adquiridos y el 
 * cálculo del subtotal acumulado de una orden específica.</p>
 * * @author 262722
 * @author 262742
 */
public class DetallePedidoBO implements iDetallePedidoBO {

    /**
     * DAO para la persistencia de los detalles del pedido.
     */
    private final iDetallePedidoDAO detallePedidoDAO;

    /**
     * Logger para el registro de eventos y errores en la capa de negocio.
     */
    private static final Logger LOG = Logger.getLogger(DetallePedidoBO.class.getName());

    /**
     * Constructor que inyecta la dependencia del DAO de detalles.
     * @param detallePedidoDAO Interfaz del DAO correspondiente.
     */
    public DetallePedidoBO(iDetallePedidoDAO detallePedidoDAO) {
        this.detallePedidoDAO = detallePedidoDAO;
    }

    /**
     * Recupera todos los productos y cantidades asociados a un pedido.
     * @param idPedido Identificador único del pedido.
     * @return Lista de objetos <code>DetallePedido</code>.
     * @throws NegocioException Si el ID es inválido o ocurre un error en la base de datos.
     */
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

    /**
     * Calcula la suma total de los precios por cantidades de todos los detalles de un pedido.
     * <p>Este monto representa el subtotal base indispensable para cálculos posteriores 
     * de descuentos y total final.</p>
     * @param idPedido Identificador único del pedido.
     * @return El subtotal del pedido como valor flotante.
     * @throws NegocioException Si el ID es inválido o no se puede realizar el cálculo en persistencia.
     */
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