/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.DetallePedido;
import java.util.List;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Interfaz para el Data Access Object (DAO) de Detalles de Pedido.</b>
 * <p>Define el contrato para las operaciones de persistencia relacionadas con los 
 * detalles (productos, cantidades y precios) que componen un pedido específico.</p>
 *
 * @author 262722
 * @author 262742
 */
public interface iDetallePedidoDAO {

    /**
     * Inserta una lista de detalles asociados a un pedido específico en la base de datos.
     * <p>Se encarga de registrar qué productos y en qué cantidad fueron ordenados
     * dentro de una misma transacción o pedido.</p>
     * * @param idPedido El ID único del pedido al que pertenecen los detalles.
     * @param detalles Lista de objetos {@link DetallePedido} que contienen la información de los productos.
     * @throws PersistenciaException Si ocurre un error de SQL durante la inserción masiva.
     */
    public void insertarDetalles(int idPedido, List<DetallePedido> detalles) throws PersistenciaException;

    /**
     * Consulta y recupera todos los detalles (productos) asociados a un pedido específico.
     * * @param idPedido El ID único del pedido a consultar.
     * @return Una lista de objetos {@link DetallePedido} correspondientes al pedido.
     * @throws PersistenciaException Si ocurre un error al ejecutar la consulta SQL.
     */
    public List<DetallePedido> listarDetallesPorPedido(int idPedido) throws PersistenciaException;
    
    /**
     * Calcula y obtiene el subtotal de un pedido sumando los importes de sus detalles.
     * <p>Normalmente, esto implica multiplicar la cantidad por el precio unitario de 
     * cada detalle y sumar los resultados de todos los artículos en el pedido.</p>
     * * @param idPedido El ID único del pedido del cual se calculará el subtotal.
     * @return El subtotal calculado en formato de punto flotante.
     * @throws PersistenciaException Si ocurre un error de SQL durante el cálculo o la consulta.
     */
    public float obtenerSubTotalPorPedido(int idPedido) throws PersistenciaException;
}