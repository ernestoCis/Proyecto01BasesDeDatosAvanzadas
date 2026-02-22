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
     * metodo que registra los detalles de cada pedido
     * @param idPedido pedido del cual pertenecen los detalles
     * @param detalles lista de todos los detalles del pedido
     * @throws PersistenciaException excepcion por si sql falla
     */
    public void insertarDetalles(int idPedido, List<DetallePedido> detalles) throws PersistenciaException;
}
