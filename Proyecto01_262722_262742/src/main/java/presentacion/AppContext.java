/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dominio.Cliente;
import dominio.Empleado;
import negocio.BOs.iClienteBO;
import negocio.BOs.iCuponBO;
import negocio.BOs.iDetallePedidoBO;
import negocio.BOs.iEmpleadoBO;
import negocio.BOs.iPedidoBO;
import negocio.BOs.iProductoBO;
import negocio.BOs.iUsuarioBO;

/** 
 * <p>
 * Clase que representa el <b>contexto general de la aplicación</b>. Su función
 * principal es centralizar:
 * </p>
 *
 * <ul>
 * <li>Las dependencias de la capa de negocio (BO).</li>
 * <li>El estado actual de la sesión (cliente o empleado autenticado).</li>
 * </ul>
 *
 * <p>
 * Esta clase actúa como un contenedor compartido que permite a las pantallas y
 * controladores acceder a los distintos servicios del sistema sin necesidad de
 * instanciarlos nuevamente.
 * </p>
 *
 * <p>
 * Además, administra el usuario actualmente autenticado, permitiendo cambiar o
 * cerrar la sesión activa.
 * </p>
 *
 * <h2>Responsabilidades</h2>
 * <ul>
 * <li>Proveer acceso a los BO del sistema.</li>
 * <li>Mantener el estado del cliente autenticado.</li>
 * <li>Mantener el estado del empleado autenticado.</li>
 * <li>Permitir cerrar la sesión activa.</li>
 * </ul>
 *
 * @author 262722, 2627242
 */
public class AppContext {

    /**
     * BO encargado de la lógica relacionada con usuarios.
     */
    private final iUsuarioBO usuarioBO;

    /**
     * BO encargado de la lógica relacionada con productos.
     */
    private final iProductoBO productoBO;

    /**
     * BO encargado de la lógica relacionada con cupones.
     */
    private final iCuponBO cuponBO;

    /**
     * BO encargado de la lógica relacionada con pedidos.
     */
    private final iPedidoBO pedidoBO;

    /**
     * BO encargado de la lógica relacionada con clientes.
     */
    private final iClienteBO clienteBO;

    /**
     * BO encargado de la lógica relacionada con empleados.
     */
    private final iEmpleadoBO empleadoBO;

    /**
     * BO encargado de la lógica relacionada con los detalles de pedido.
     */
    private final iDetallePedidoBO detallePedidoBO;

    /**
     * Cliente actualmente autenticado en el sistema. Puede ser null si no hay
     * sesión activa de cliente.
     */
    private Cliente clienteActual;

    /**
     * Empleado actualmente autenticado en el sistema. Puede ser null si no hay
     * sesión activa de empleado.
     */
    private Empleado empleadoActual;

    /**
     * <p>
     * Constructor que inicializa el contexto de la aplicación con todas las
     * dependencias necesarias de la capa de negocio.
     * </p>
     *
     * <p>
     * Las dependencias se inyectan desde el exterior, permitiendo una mejor
     * separación de responsabilidades y facilitando pruebas.
     * </p>
     *
     * @param usuarioBO BO de usuarios
     * @param productoBO BO de productos
     * @param cuponBO BO de cupones
     * @param pedidoBO BO de pedidos
     * @param clienteBO BO de clientes
     * @param empleadoBO BO de empleados
     * @param detallePedidoBO BO de detalles de pedido
     */
    public AppContext(iUsuarioBO usuarioBO,
            iProductoBO productoBO,
            iCuponBO cuponBO,
            iPedidoBO pedidoBO,
            iClienteBO clienteBO,
            iEmpleadoBO empleadoBO,
            iDetallePedidoBO detallePedidoBO) {
        this.usuarioBO = usuarioBO;
        this.productoBO = productoBO;
        this.cuponBO = cuponBO;
        this.pedidoBO = pedidoBO;
        this.clienteBO = clienteBO;
        this.empleadoBO = empleadoBO;
        this.detallePedidoBO = detallePedidoBO;
    }

    /**
     * Obtiene el BO de usuarios.
     *
     * @return instancia de iUsuarioBO
     */
    public iUsuarioBO getUsuarioBO() {
        return usuarioBO;
    }

    /**
     * Obtiene el BO de productos.
     *
     * @return instancia de iProductoBO
     */
    public iProductoBO getProductoBO() {
        return productoBO;
    }

    /**
     * Obtiene el BO de cupones.
     *
     * @return instancia de iCuponBO
     */
    public iCuponBO getCuponBO() {
        return cuponBO;
    }

    /**
     * Obtiene el BO de pedidos.
     *
     * @return instancia de iPedidoBO
     */
    public iPedidoBO getPedidoBO() {
        return pedidoBO;
    }

    /**
     * Obtiene el BO de clientes.
     *
     * @return instancia de iClienteBO
     */
    public iClienteBO getClienteBO() {
        return clienteBO;
    }

    /**
     * Obtiene el BO de empleados.
     *
     * @return instancia de iEmpleadoBO
     */
    public iEmpleadoBO getEmpleadoBO() {
        return empleadoBO;
    }

    /**
     * Obtiene el BO de detalles de pedido.
     *
     * @return instancia de iDetallePedidoBO
     */
    public iDetallePedidoBO getDetallePedidoBO() {
        return detallePedidoBO;
    }

    /**
     * Obtiene el cliente actualmente autenticado.
     *
     * @return cliente en sesión o null si no hay sesión activa
     */
    public Cliente getClienteActual() {
        return clienteActual;
    }

    /**
     * Establece el cliente actualmente autenticado.
     *
     * @param clienteActual cliente que iniciará sesión
     */
    public void setClienteActual(Cliente clienteActual) {
        this.clienteActual = clienteActual;
    }

    /**
     * Obtiene el empleado actualmente autenticado.
     *
     * @return empleado en sesión o null si no hay sesión activa
     */
    public Empleado getEmpleadoActual() {
        return empleadoActual;
    }

    /**
     * Establece el empleado actualmente autenticado.
     *
     * @param empleadoActual empleado que iniciará sesión
     */
    public void setEmpleadoActual(Empleado empleadoActual) {
        this.empleadoActual = empleadoActual;
    }

    /**
     * <p>
     * Cierra la sesión activa en el sistema.
     * </p>
     *
     * <p>
     * Este método limpia tanto el cliente como el empleado actual, dejando el
     * contexto sin ningún usuario autenticado.
     * </p>
     */
    public void cerrarSesion() {
        this.clienteActual = null;
        this.empleadoActual = null;
    }
}
