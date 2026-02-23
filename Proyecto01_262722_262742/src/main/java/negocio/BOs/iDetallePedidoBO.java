/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.DetallePedido;
import java.util.List;
import negocio.Excepciones.NegocioException;

/**
 *
 * @author Isaac
 */
public interface iDetallePedidoBO {

    /**
     * Metodo que lista los detalles (productos) de un pedido.
     *
     * @param idPedido id del pedido
     * @return lista de detalles del pedido
     * @throws NegocioException excepcion por reglas de negocio
     */
    public List<DetallePedido> listarDetallesPorPedido(int idPedido) throws NegocioException;
}
