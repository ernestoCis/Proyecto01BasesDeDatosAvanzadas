/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import dominio.PedidoProgramado;
import negocio.Excepciones.NegocioException;

/**
 *
 * @author jesus y isaac
 */
public interface iPedidoBO {
    /**
     * metodo que genera un numero de pedido que no este registrado en la BD
     * @return regresa el numero de pedido
     * @throws NegocioException excepcion por reglas de nogocio
     */
    public int generarNumeroDePedido() throws NegocioException;
    
    /**
     * metodo que mediante el DAO agrega un pedidoProgramado
     * @param pedidoProgramado pedido a agregar
     * @throws NegocioException excepcion por regla de negocio
     */
    public PedidoProgramado agregarPedidoProgramado(PedidoProgramado pedidoProgramado) throws NegocioException;
}
