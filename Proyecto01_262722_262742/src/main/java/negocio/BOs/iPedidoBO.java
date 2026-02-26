/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import dominio.DetallePedido;
import dominio.EstadoPedido;
import dominio.Pedido;
import dominio.PedidoExpress;
import dominio.PedidoProgramado;
import java.time.LocalDate;
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

    /**
     * metodo para obtener todos los pedidos de un cliente
     *
     * @param idCliente id del cliente que se buscaran sus pedidos
     * @return regresa la lista de pedidos
     * @throws NegocioException excepcion por reglas de negocio
     */
    public List<Pedido> listarPedidosPorCliente(int idCliente) throws NegocioException;

    /**
     * metodo que lista los pedidos del cliente mas un filtro
     *
     * @param idCliente id de del cliente del que se buscaran sus pedidos
     * @param folio folio para filtrar
     * @param fechaInicio fecha para el rango del filtro
     * @param fechaFin fecha para el rango del filtro
     * @return regresa la lista con los pedidos
     * @throws NegocioException excepcion por reglas de negocio
     */
    public List<Pedido> listarPedidosPorClienteFiltro(int idCliente, String folio, LocalDate fechaInicio, LocalDate fechaFin) throws NegocioException;

    /**
     * metodo que genera in folio valido para los pedidos express
     *
     * @return regresa el folio valido
     * @throws NegocioException excepcion por regla de negocio
     */
    public String generarFolio() throws NegocioException;

    /**
     * metodo que genera un PIN de 8 digitos hasheado
     *
     * @return pin hasheado
     * @throws NegocioException excepcion por reglas de negocio
     */
    public String generarPIN() throws NegocioException;

    /**
     * metodo para insertar un pedido express y lo que lleva
     *
     * @param pedidoExpress pedido a insertar
     * @param detalles detalles pedido del pedido express
     * @return pedido express insertado
     * @throws NegocioException excepcion por reglas de negocio
     */
    public PedidoExpress agregarPedidoExpress(PedidoExpress pedidoExpress, List<DetallePedido> detalles) throws NegocioException;

    /**
     * Metodo que actualiza el estado de un pedido express validando primero el
     * PIN ingresado contra el hash almacenado en la base de datos.
     *
     * @param idPedido id del pedido express
     * @param pinIngresado pin ingresado por el empleado
     * @param nuevoEstado nuevo estado que se desea asignar
     * @throws NegocioException excepcion si el pin es incorrecto o falla la operacion
     */
    public void actualizarEstadoPedidoExpressConPIN(int idPedido,
            String pinIngresado,
            EstadoPedido nuevoEstado) throws NegocioException;

    /**
     * Metodo que lista los pedidos aplicando filtros opcionales. 
     * @param folio folio del pedido (puede ser null)
     * @param telefono telefono del cliente (puede ser null)
     * @param fechaInicio fecha inicial del rango (puede ser null)
     * @param fechaFin fecha final del rango (puede ser null)
     * @return lista de pedidos filtrados
     * @throws NegocioException excepcion por reglas de negocio o error de
     * persistencia
     */
    public List<Pedido> listarPedidosFiltro(String folio,
            String telefono,
            LocalDate fechaInicio,
            LocalDate fechaFin) throws NegocioException;

    /**
     * Metodo que valida el PIN de un pedido express antes de marcarlo como entregado. 
     * Si el PIN es correcto, actualiza el estado a Entregado.
     *
     * @param idPedido id del pedido express
     * @param pinPlano pin ingresado en texto plano
     * @throws NegocioException si el pin es incorrecto o ocurre algun error
     */
    public void entregarPedidoExpressConPin(int idPedido, String pinPlano) throws NegocioException;
}
