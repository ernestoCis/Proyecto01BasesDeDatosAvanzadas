/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.DetallePedido;
import java.util.List;
import negocio.Excepciones.NegocioException;

/**
 * <b>Interfaz para el Objeto de Negocio (BO) de Detalles de Pedido.</b>
 * <p>Define el contrato para las operaciones relacionadas con las partidas 
 * individuales de una orden, permitiendo recuperar los productos asociados 
 * y calcular el monto base (subtotal) del pedido.</p>
 *
 * @author Isaac
 * @author 262722
 * @author 262742
 */
public interface iDetallePedidoBO {

    /**
     * Recupera la lista de detalles (productos y cantidades) que conforman un pedido específico.
     *
     * @param idPedido Identificador único del pedido a consultar.
     * @return Una lista de objetos {@link DetallePedido} asociados a la orden.
     * @throws NegocioException Si el ID del pedido es inválido o ocurre un error en la capa de persistencia.
     */
    public List<DetallePedido> listarDetallesPorPedido(int idPedido) throws NegocioException;
    
    /**
     * Calcula y obtiene el subtotal de un pedido sumando los importes de sus detalles.
     *
     * @param idPedido Identificador único del pedido a obtener su subtotal.
     * @return El monto subtotal del pedido como valor flotante (antes de aplicar descuentos).
     * @throws NegocioException Si el ID es inválido o ocurre un error durante la consulta o el cálculo.
     */
    public float obtenerSubtotalPedido(int idPedido) throws NegocioException;
}