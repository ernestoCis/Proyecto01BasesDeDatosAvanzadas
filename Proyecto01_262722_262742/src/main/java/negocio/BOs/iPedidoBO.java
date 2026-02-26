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
 * <b>Interfaz para el Objeto de Negocio (BO) de Pedidos.</b>
 * <p>Define el contrato principal para la gestión del ciclo de vida de las órdenes.
 * Abarca la creación, consulta, filtrado y actualización de estados tanto para 
 * pedidos programados como para pedidos exprés, incluyendo la generación de 
 * identificadores únicos y la validación de seguridad (PIN) para entregas.</p>
 *
 * @author jesus
 * @author isaac
 * @author 262722
 * @author 262742
 */
public interface iPedidoBO {

    /**
     * Genera un número de pedido secuencial o aleatorio garantizando que no exista previamente en la base de datos.
     *
     * @return El número de pedido generado y validado.
     * @throws NegocioException Si ocurre un error de concurrencia o de conexión con la persistencia.
     */
    public int generarNumeroDePedido() throws NegocioException;

    /**
     * Registra un nuevo pedido programado junto con los productos que lo conforman.
     *
     * @param pedidoProgramado Objeto con los datos del pedido programado a registrar.
     * @param detalles Lista de los detalles (productos y cantidades) asociados al pedido.
     * @return El pedido programado ya registrado con su ID generado.
     * @throws NegocioException Si los datos son inválidos o ocurre un error al persistir.
     */
    public PedidoProgramado agregarPedidoProgramado(PedidoProgramado pedidoProgramado, List<DetallePedido> detalles) throws NegocioException;

    /**
     * Recupera la lista de todos los pedidos registrados en el sistema, independientemente de su tipo.
     *
     * @return Una lista de objetos {@link Pedido}.
     * @throws NegocioException Si ocurre un error de persistencia al realizar la consulta.
     */
    public List<Pedido> listarPedidos() throws NegocioException;

    
    /**
     * Actualiza el estado actual de un pedido estándar (ej. de 'Pendiente' a 'En Preparación').
     *
     * @param idPedido Identificador único del pedido a actualizar.
     * @param estado El nuevo {@link EstadoPedido} que se asignará.
     * @throws NegocioException Si el pedido no existe o la transición de estado no es permitida.
     */
    public void actualizarEstadoPedido(int idPedido, EstadoPedido estado) throws NegocioException;

    /**
     * Obtiene el historial completo de pedidos realizados por un cliente en específico.
     *
     * @param idCliente Identificador único del cliente.
     * @return Una lista de pedidos asociados al cliente.
     * @throws NegocioException Si ocurre un error al consultar la base de datos.
     */
    public List<Pedido> listarPedidosPorCliente(int idCliente) throws NegocioException;

    /**
     * Recupera los pedidos de un cliente aplicando filtros de búsqueda por folio y rango de fechas.
     *
     * @param idCliente Identificador del cliente.
     * @param folio Folio o número de orden parcial o exacto para filtrar.
     * @param fechaInicio Fecha inicial del rango de búsqueda.
     * @param fechaFin Fecha final del rango de búsqueda.
     * @return Una lista de pedidos que coinciden con los criterios de búsqueda.
     * @throws NegocioException Si las fechas son inconsistentes o falla la consulta.
     */
    public List<Pedido> listarPedidosPorClienteFiltro(int idCliente, String folio, LocalDate fechaInicio, LocalDate fechaFin) throws NegocioException;

    /**
     * Genera un folio único y válido en formato alfanumérico para identificar pedidos exprés.
     *
     * @return Una cadena de texto que representa el folio generado.
     * @throws NegocioException Si ocurre un problema al verificar la unicidad en la base de datos.
     */
    public String generarFolio() throws NegocioException;

    /**
     * Genera un PIN de seguridad de 8 dígitos y lo aplica a una función hash para su almacenamiento.
     *
     * @return El PIN generado ya procesado mediante un algoritmo de hashing (ej. BCrypt).
     * @throws NegocioException Si ocurre un error durante el proceso de encriptación.
     */
    public String generarPIN() throws NegocioException;

    /**
     * Registra un nuevo pedido exprés junto con sus detalles y aplica el PIN de seguridad autogenerado.
     *
     * @param pedidoExpress Objeto con los datos del pedido exprés a insertar.
     * @param detalles Lista de productos que conforman el pedido.
     * @return El pedido exprés registrado.
     * @throws NegocioException Si las validaciones fallan o hay un error de persistencia.
     */
    public PedidoExpress agregarPedidoExpress(PedidoExpress pedidoExpress, List<DetallePedido> detalles) throws NegocioException;

    /**
     * Actualiza el estado de un pedido exprés, exigiendo la validación previa del PIN de seguridad.
     *
     * @param idPedido Identificador del pedido exprés.
     * @param pinIngresado PIN capturado por el empleado en texto plano.
     * @param nuevoEstado El estado al que se desea avanzar.
     * @throws NegocioException Si el PIN no coincide con el hash almacenado o falla la operación.
     */
    public void actualizarEstadoPedidoExpressConPIN(int idPedido, String pinIngresado, EstadoPedido nuevoEstado) throws NegocioException;

    /**
     * Lista pedidos a nivel global aplicando múltiples filtros opcionales. 
     * Se utiliza para pantallas de gestión general de órdenes.
     *
     * @param folio Folio parcial o exacto del pedido (puede ser null).
     * @param telefono Teléfono asociado al cliente o al pedido (puede ser null).
     * @param fechaInicio Fecha inicial del rango (puede ser null).
     * @param fechaFin Fecha final del rango (puede ser null).
     * @return Lista de pedidos que cumplen con los filtros aplicados.
     * @throws NegocioException Si hay errores en los parámetros o en la persistencia.
     */
    public List<Pedido> listarPedidosFiltro(String folio, String telefono, LocalDate fechaInicio, LocalDate fechaFin) throws NegocioException;

    
    /**
     * Valida el PIN proporcionado y, de ser correcto, transiciona un pedido exprés específicamente al estado 'Entregado'.
     *
     * @param idPedido Identificador del pedido exprés a entregar.
     * @param pinPlano PIN proporcionado por el cliente/repartidor en texto plano.
     * @throws NegocioException Si el PIN es incorrecto, el pedido no existe o ya fue entregado.
     */
    public void entregarPedidoExpressConPin(int idPedido, String pinPlano) throws NegocioException;
}