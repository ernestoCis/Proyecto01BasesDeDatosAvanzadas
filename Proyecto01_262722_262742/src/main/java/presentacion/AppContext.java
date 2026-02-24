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
 * Contexto de la aplicación: Contiene los BO (dependencias) y el estado de
 * sesión (cliente/empleado actual).
 */
public class AppContext {

    private final iUsuarioBO usuarioBO;
    private final iProductoBO productoBO;
    private final iCuponBO cuponBO;
    private final iPedidoBO pedidoBO;
    private final iClienteBO clienteBO;
    private final iEmpleadoBO empleadoBO;
    private final iDetallePedidoBO detallePedidoBO; 

    private Cliente clienteActual;
    private Empleado empleadoActual;

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

    public iUsuarioBO getUsuarioBO() { return usuarioBO; }
    public iProductoBO getProductoBO() { return productoBO; }
    public iCuponBO getCuponBO() { return cuponBO; }
    public iPedidoBO getPedidoBO() { return pedidoBO; }
    public iClienteBO getClienteBO() { return clienteBO; }
    public iEmpleadoBO getEmpleadoBO() { return empleadoBO; }

    public iDetallePedidoBO getDetallePedidoBO() {
        return detallePedidoBO;
    }

    public Cliente getClienteActual() { return clienteActual; }
    public void setClienteActual(Cliente clienteActual) { this.clienteActual = clienteActual; }

    public Empleado getEmpleadoActual() { return empleadoActual; }
    public void setEmpleadoActual(Empleado empleadoActual) { this.empleadoActual = empleadoActual; }

    public void cerrarSesion() {
        this.clienteActual = null;
        this.empleadoActual = null;
    }
}