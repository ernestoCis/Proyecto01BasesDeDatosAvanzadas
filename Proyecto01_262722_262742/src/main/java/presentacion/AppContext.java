/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import dominio.Cliente;
import dominio.Empleado;
import negocio.BOs.iClienteBO;
import negocio.BOs.iCuponBO;
import negocio.BOs.iPedidoBO;
import negocio.BOs.iProductoBO;
import negocio.BOs.iUsuarioBO;

/**
 * Contexto de la aplicación: Contiene los BO (dependencias) y el estado de
 * sesión (cliente/empleado actual).
 */
public class AppContext {

    private final iUsuarioBO usuarioBO;
    private final iProductoBO productoBO;
    private final iCuponBO cuponBO;
    private final iPedidoBO pedidoBO;
    private final iClienteBO clienteBO;

    // Sesión (pueden ser null si no hay sesión iniciada)
    private Cliente clienteActual;
    private Empleado empleadoActual;

    public AppContext(iUsuarioBO usuarioBO, iProductoBO productoBO, iCuponBO cuponBO, iPedidoBO pedidoBO, iClienteBO clienteBO) {
        this.usuarioBO = usuarioBO;
        this.productoBO = productoBO;
        this.cuponBO = cuponBO;
        this.pedidoBO = pedidoBO;
        this.clienteBO = clienteBO;
    }

    public iUsuarioBO getUsuarioBO() {
        return usuarioBO;
    }

    public iProductoBO getProductoBO() {
        return productoBO;
    }

    public iCuponBO getCuponBO() {
        return cuponBO;
    }

    public iPedidoBO getPedidoBO() {
        return pedidoBO;
    }

    public iClienteBO getClienteBO() {
        return clienteBO;
    }

    public Cliente getClienteActual() {
        return clienteActual;
    }

    public void setClienteActual(Cliente clienteActual) {
        this.clienteActual = clienteActual;
    }

    public Empleado getEmpleadoActual() {
        return empleadoActual;
    }

    public void setEmpleadoActual(Empleado empleadoActual) {
        this.empleadoActual = empleadoActual;
    }

    /**
     * Cierra sesión de cliente y empleado.
     */
    public void cerrarSesion() {
        this.clienteActual = null;
        this.empleadoActual = null;
    }
}
