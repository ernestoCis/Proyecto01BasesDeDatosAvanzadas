/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.DetallePedido;
import java.util.List;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author
 */
public interface iDetallePedidoDAO {

    /**
     * Metodo que registra los detalles de cada pedido
     * @param idPedido pedido del cual pertenecen los detalles
     * @param detalles lista de todos los detalles del pedido
     * @throws PersistenciaException excepcion por si sql falla
     */
    public void insertarDetalles(int idPedido, List<DetallePedido> detalles) throws PersistenciaException;

    /**
     * Metodo que consulta los detalles (productos) de un pedido
     * @param idPedido id del pedido
     * @return lista de detalles del pedido
     * @throws PersistenciaException excepcion por si sql falla
     */
    public List<DetallePedido> listarDetallesPorPedido(int idPedido) throws PersistenciaException;
    
    /**
     * metodo que calcula el subtotal de un pedido
     * @param idPedido id del pedido que tiene detalles de pedidp
     * @return subtotal calculado
     * @throws PersistenciaException excepcion por si el sql falla
     */
    public float obtenerSubTotalPorPedido(int idPedido) throws PersistenciaException;
}