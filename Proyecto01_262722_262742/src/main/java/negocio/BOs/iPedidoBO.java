/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import dominio.DetallePedido;
import dominio.EstadoPedido;
import dominio.Pedido;
import dominio.PedidoProgramado;
import java.util.List;
import negocio.Excepciones.NegocioException;

/**
 *
 * @author jesus y isaac
 */
public interface iPedidoBO {

    /**
     * metodo que genera un numero de pedido que no este registrado en la BD
     *
     * @return regresa el numero de pedido
     * @throws NegocioException excepcion por reglas de nogocio
     */
    public int generarNumeroDePedido() throws NegocioException;

    /**
     * metodo que mediante el DAO agrega un pedidoProgramado
     *
     * @param pedidoProgramado pedido a agregar
     * @throws NegocioException excepcion por regla de negocio
     */
    public PedidoProgramado agregarPedidoProgramado(PedidoProgramado pedidoProgramado, List<DetallePedido> detalles) throws NegocioException;

    /**
     * Metodo que lista los pedidos registrados en la BD.
     *
     * @return lista de pedidos
     * @throws NegocioException excepcion por reglas de negocio
     */
    public List<Pedido> listarPedidos() throws NegocioException;

    /**
     * Metodo que actualiza el estado de un pedido.
     *
     * @param idPedido id del pedido a actualizar
     * @param estado nuevo estado
     * @throws NegocioException excepcion por reglas de negocio
     */
    public void actualizarEstadoPedido(int idPedido, EstadoPedido estado) throws NegocioException;
}

