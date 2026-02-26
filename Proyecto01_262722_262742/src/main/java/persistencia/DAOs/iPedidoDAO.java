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
 * <b>Interfaz para el Data Access Object (DAO) de Pedidos.</b>
 * <p>Define el contrato principal para las operaciones de persistencia relacionadas 
 * con los pedidos del sistema. Soporta la gestión de distintos tipos de pedidos 
 * (Programados y Express), así como operaciones complejas de filtrado, validación 
 * y actualización de estados.</p>
 *
 * @author 262722
 * @author 262742
 */
public interface iPedidoDAO {

    /**
     * Inserta un pedido de tipo programado en la base de datos junto con sus detalles.
     *
     * @param pedido Objeto {@link PedidoProgramado} a insertar.
     * @param detalles Lista de objetos {@link DetallePedido} que componen el pedido.
     * @return El pedido programado insertado con sus IDs autogenerados.
     * @throws PersistenciaException Si ocurre un error durante la transacción SQL.
     */
    PedidoProgramado insertarPedidoProgramado(PedidoProgramado pedido, List<DetallePedido> detalles) throws PersistenciaException;

    /**
     * Inserta un pedido de tipo express en la base de datos junto con sus detalles.
     *
     * @param pedido Objeto {@link PedidoExpress} a insertar.
     * @param detalles Lista de objetos {@link DetallePedido} que componen el pedido.
     * @return El pedido express insertado con sus IDs autogenerados.
     * @throws PersistenciaException Si ocurre un error durante la transacción SQL.
     */
    PedidoExpress insertarPedidoExpress(PedidoExpress pedido, List<DetallePedido> detalles) throws PersistenciaException;

    /**
     * Consulta y recupera la información completa de un pedido a partir de su ID.
     *
     * @param idPedido El identificador único del pedido a buscar.
     * @return El objeto {@link Pedido} encontrado (puede ser instanciado como Programado o Express).
     * @throws PersistenciaException Si el pedido no existe o si ocurre un error de SQL.
     */
    Pedido consultarPedidoPorId(int idPedido) throws PersistenciaException;

    /**
     * Valida si un número de pedido específico ya se encuentra registrado en la base de datos.
     *
     * @param numeroPedido El número de pedido a consultar.
     * @return {@code true} si el número ya existe, {@code false} si está disponible.
     * @throws PersistenciaException Si ocurre un error de SQL durante la consulta.
     */
    public boolean existeNumeroDePedido(int numeroPedido) throws PersistenciaException;

    /**
     * Obtiene una lista con todos los pedidos registrados en el sistema.
     *
     * @return Una lista de objetos {@link Pedido}.
     * @throws PersistenciaException Si ocurre un error al ejecutar la consulta SQL.
     */
    public List<Pedido> listarPedidos() throws PersistenciaException;

    /**
     * Actualiza el estado actual de un pedido en la base de datos.
     *
     * @param idPedido El identificador único del pedido a modificar.
     * @param nuevoEstado El nuevo {@link EstadoPedido} que se le asignará.
     * @throws PersistenciaException Si ocurre un error de SQL durante la actualización.
     */
    public void actualizarEstadoPedido(int idPedido, EstadoPedido nuevoEstado) throws PersistenciaException;

    /**
     * Lista todos los pedidos asociados a un cliente específico.
     *
     * @param idCliente El identificador único del cliente para buscar sus pedidos.
     * @return Una lista de pedidos pertenecientes al cliente indicado.
     * @throws PersistenciaException Si ocurre un error de SQL durante la consulta.
     */
    public List<Pedido> listarPedidosPorCliente(int idCliente) throws PersistenciaException;

    /**
     * Aplica filtros específicos para listar los pedidos de un cliente determinado.
     *
     * @param idCliente El ID del cliente del que se buscarán los pedidos.
     * @param folio El folio del pedido a buscar (puede ser nulo o vacío para omitir).
     * @param fechaInicio La fecha de inicio para el rango de búsqueda (puede ser nulo).
     * @param fechaFin La fecha de fin para el rango de búsqueda (puede ser nulo).
     * @return Una lista de pedidos filtrados según los criterios proporcionados.
     * @throws PersistenciaException Si ocurre un error de SQL durante la consulta.
     */
    public List<Pedido> listarPedidosPorClienteFiltro(int idCliente, String folio, LocalDate fechaInicio, LocalDate fechaFin) throws PersistenciaException;

    /**
     * Valida si un folio específico ya se encuentra registrado en la base de datos.
     *
     * @param folio El folio alfanumérico a consultar.
     * @return {@code true} si el folio ya existe, {@code false} si está disponible.
     * @throws PersistenciaException Si ocurre un error de SQL durante la consulta.
     */
    public boolean existeFolio(String folio) throws PersistenciaException;

    /**
     * Calcula y devuelve cuál será el siguiente número consecutivo de pedido disponible.
     *
     * @return El siguiente número de pedido disponible como entero.
     * @throws PersistenciaException Si ocurre un error de SQL al calcular el consecutivo.
     */
    public int obtenerSiguienteNumeroDePedido() throws PersistenciaException;

    /**
     * Cuenta la cantidad de pedidos que actualmente se encuentran en un estado activo
     * para un cliente específico.
     *
     * @param idCliente El ID del cliente al cual se le contarán los pedidos activos.
     * @return El número total de pedidos activos de dicho cliente.
     * @throws PersistenciaException Si ocurre un error de SQL durante la consulta.
     */
    public int contarPedidosActivosPorCliente(int idCliente) throws PersistenciaException;

    /**
     * Lista los pedidos a nivel global aplicando filtros opcionales de búsqueda.
     *
     * @param folio El folio del pedido express a buscar (puede ser null).
     * @param telefono El teléfono asociado al cliente (puede ser null).
     * @param fechaInicio La fecha inicial del rango de búsqueda (puede ser null).
     * @param fechaFin La fecha final del rango de búsqueda (puede ser null).
     * @return Una lista de pedidos filtrados según los parámetros ingresados.
     * @throws PersistenciaException Si ocurre un error de SQL durante la consulta.
     */
    public List<Pedido> listarPedidosFiltro(String folio, String telefono, LocalDate fechaInicio, LocalDate fechaFin) throws PersistenciaException;

    /**
     * Obtiene el hash de seguridad (PIN) almacenado para un pedido express específico.
     * * @param idPedido El ID del pedido express a consultar.
     * @return El hash del PIN almacenado en la base de datos.
     * @throws PersistenciaException Si ocurre un error de SQL o si el pedido no tiene PIN.
     */
    public String obtenerPinHashPorPedido(int idPedido) throws PersistenciaException;
}