/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.Pedido;
import dominio.PedidoExpress;
import dominio.PedidoProgramado;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author Isaac
 */
public interface iPedidoDAO {

    /**
     * Metodo para insertar un pedido programado en la BD.
     * @param pedido pedido programado a insertar
     * @return pedido programado insertado con IDs generados
     * @throws PersistenciaException si ocurre un error de persistencia
     */
    PedidoProgramado insertarPedidoProgramado(PedidoProgramado pedido) throws PersistenciaException;

    /**
     * Metodo para insertar un pedido express en la BD.
     * @param pedido pedido express a insertar
     * @return pedido express insertado con IDs generados
     * @throws PersistenciaException si ocurre un error de persistencia
     */
    PedidoExpress insertarPedidoExpress(PedidoExpress pedido) throws PersistenciaException;

    /**
     * Metodo para consultar un pedido por su ID.
     * @param idPedido id del pedido a consultar
     * @return pedido encontrado
     * @throws PersistenciaException si no existe o ocurre error de persistencia
     */
    Pedido consultarPedidoPorId(int idPedido) throws PersistenciaException;
}