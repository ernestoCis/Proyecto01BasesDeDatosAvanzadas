/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.DetallePedido;
import dominio.EstadoPedido;
import dominio.Pedido;
import dominio.PedidoExpress;
import dominio.PedidoProgramado;
import java.time.LocalDate;
import java.util.List;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author
 */
public interface iPedidoDAO {

    /**
     * Metodo para insertar un pedido programado en la BD.
     *
     * @param pedido pedido programado a insertar
     * @return pedido programado insertado con IDs generados
     * @throws PersistenciaException si ocurre un error de persistencia
     */
    PedidoProgramado insertarPedidoProgramado(PedidoProgramado pedido, List<DetallePedido> detalles) throws PersistenciaException;

    /**
     * Metodo para insertar un pedido express en la BD.
     *
     * @param pedido pedido express a insertar
     * @return pedido express insertado con IDs generados
     * @throws PersistenciaException si ocurre un error de persistencia
     */
    PedidoExpress insertarPedidoExpress(PedidoExpress pedido, List<DetallePedido> detalles) throws PersistenciaException;

    /**
     * Metodo para consultar un pedido por su ID.
     *
     * @param idPedido id del pedido a consultar
     * @return pedido encontrado
     * @throws PersistenciaException si no existe o ocurre error de persistencia
     */
    Pedido consultarPedidoPorId(int idPedido) throws PersistenciaException;

    /**
     * metodo que valida que el atributo numero medido no este dentro de la BD
     *
     * @param numeroPedido numero a consultar
     * @return true = si el numero ya existe, false = si el numero no existe
     * @throws PersistenciaException excepcion por si el sql falla
     */
    public boolean existeNumeroDePedido(int numeroPedido) throws PersistenciaException;

    /**
     * metodo para obtener todos los pedidos
     * @return lista de pedidos
     * @throws PersistenciaException excepcion por si falla el sql
     */
    public List<Pedido> listarPedidos() throws PersistenciaException;

    /**
     * metodo que cambia el estado de un pedido
     * @param idPedido id del pedido a cambiar su estado
     * @param nuevoEstado nuevo estado para el pedido
     * @throws PersistenciaException excepcion por si falla el sql
     */
    public void actualizarEstadoPedido(int idPedido, EstadoPedido nuevoEstado) throws PersistenciaException;
    
    /**
     * metodo que lista los pedidos de un cliente
     * @param idCliente id del cliente ora buscar sus pedidos
     * @return regresa la lista de pedidos de un cliente
     * @throws PersistenciaException excepcion por si el sql falla
     */
    public List<Pedido> listarPedidosPorCliente(int idCliente) throws PersistenciaException;
    
    /**
     * metodo que filtra los pedidos por cliente y filtros adicionales
     * @param idCliente id del cliente del que buscaremos sus pedidos
     * @param folio folio del pedido a buscar
     * @param fechaInicio fecha de inicio para el rengo de fechas a buscar
     * @param fechaFin fecha de fim para el rengo de fechas a buscar
     * @return lista de pedidos filrados
     * @throws PersistenciaException excepcion por si falla el sql
     */
    public List<Pedido> listarPedidosPorClienteFiltro(int idCliente, String folio, LocalDate fechaInicio, LocalDate fechaFin) throws PersistenciaException;

     /**
     * metodo que valida que el atributo numero medido no este dentro de la BD
     *
     * @param numeroPedido numero a consultar
     * @return true = si el numero ya existe, false = si el numero no existe
     * @throws PersistenciaException excepcion por si el sql falla
     */
    public boolean existeFolio(String folio) throws PersistenciaException;
    
    /**
     * metodo que mos dice cual sera el siguiente numero de pedido disponible
     * @return regresa el sihuiente numero disponible
     * @throws PersistenciaException execpecion por si el sql falla
     */
    public int obtenerSiguienteNumeroDePedido() throws PersistenciaException;
    
    /**
     * metodo que cuenta los pedidos activos por cliente
     * @param idCliente cliente el cual se contaran sus pedidos activos
     * @return numero de pedidos activos
     * @throws PersistenciaException excepcion por si el sql falla
     */
    public int contarPedidosActivosPorCliente(int idCliente) throws PersistenciaException;
}
