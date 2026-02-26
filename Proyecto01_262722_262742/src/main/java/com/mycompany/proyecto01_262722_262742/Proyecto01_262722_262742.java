package com.mycompany.proyecto01_262722_262742;

import javax.swing.SwingUtilities;
import negocio.BOs.ClienteBO;
import negocio.BOs.CuponBO;
import negocio.BOs.DetallePedidoBO;
import negocio.BOs.EmpleadoBO;
import negocio.BOs.PedidoBO;
import negocio.BOs.ProductoBO;
import negocio.BOs.UsuarioBO;
import negocio.BOs.iClienteBO;
import negocio.BOs.iCuponBO;
import negocio.BOs.iDetallePedidoBO;
import negocio.BOs.iEmpleadoBO;
import negocio.BOs.iPedidoBO;
import negocio.BOs.iProductoBO;
import negocio.BOs.iUsuarioBO;
import persistencia.Conexion.ConexionBD;
import persistencia.Conexion.iConexionBD;
import persistencia.DAOs.ClienteDAO;
import persistencia.DAOs.CuponDAO;
import persistencia.DAOs.DetallePedidoDAO;
import persistencia.DAOs.EmpleadoDAO;
import persistencia.DAOs.PedidoDAO;
import persistencia.DAOs.ProductoDAO;
import persistencia.DAOs.UsuarioDAO;
import persistencia.DAOs.iClienteDAO;
import persistencia.DAOs.iCuponDAO;
import persistencia.DAOs.iDetallePedidoDAO;
import persistencia.DAOs.iEmpleadoDAO;
import persistencia.DAOs.iPedidoDAO;
import persistencia.DAOs.iProductoDAO;
import persistencia.DAOs.iUsuarioDAO;
import presentacion.AppContext;
import presentacion.Menu;

/**
 * <b>Clase Principal del Sistema de Pedidos de la Panadería.</b>
 * <p>Esta clase sirve como el punto de entrada para la aplicación. Se encarga de
 * inicializar y conectar todas las capas del sistema de forma estructurada, incluyendo:</p>
 * <ul>
 * <li>Conexión a la base de datos.</li>
 * <li><b>DAOs</b> (Data Access Objects) para manejar la persistencia de datos.</li>
 * <li><b>BOs</b> (Business Objects) para manejar las reglas y lógica de negocio.</li>
 * </ul>
 * <p>Finalmente, encapsula estas dependencias en un contexto (<code>AppContext</code>) 
 * y arranca la interfaz gráfica del usuario.</p>
 *
 * @author 262722
 * @author 262742
 */
/**
 * calse
 * @author 262722, 262742
 */
public class Proyecto01_262722_262742 {

    /**
     * Método principal que arranca la ejecución del programa.
     * <p>Utiliza <code>SwingUtilities.invokeLater</code> para asegurar que la
     * interfaz gráfica (GUI) se construya y muestre de forma segura dentro del 
     * hilo de despacho de eventos (EDT) de Java Swing.</p>
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan en este proyecto).
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // ----- conexion -----
            iConexionBD conexion = new ConexionBD();

            // ----- DAOs -----
            iUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
            iProductoDAO productoDAO = new ProductoDAO(conexion);
            iCuponDAO cuponDAO = new CuponDAO(conexion);
            iPedidoDAO pedidoDAO = new PedidoDAO(conexion);
            iClienteDAO clienteDAO = new ClienteDAO(conexion);
            iDetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO(conexion);
            iEmpleadoDAO empleadoDAO = new EmpleadoDAO(conexion);

            // ----- BOs -----
            iUsuarioBO usuarioBO = new UsuarioBO(usuarioDAO);
            iProductoBO productoBO = new ProductoBO(productoDAO);
            iCuponBO cuponBO = new CuponBO(cuponDAO);
            iPedidoBO pedidoBO = new PedidoBO(pedidoDAO, detallePedidoDAO, cuponDAO);
            iClienteBO clienteBO = new ClienteBO(clienteDAO);
            iEmpleadoBO empleadoBO = new EmpleadoBO(empleadoDAO);
            iDetallePedidoBO detallePedidoBO = new DetallePedidoBO(detallePedidoDAO);

            // ----- AppContext -----
            AppContext ctx = new AppContext(
                    usuarioBO,
                    productoBO,
                    cuponBO,
                    pedidoBO,
                    clienteBO,
                    empleadoBO,
                    detallePedidoBO 
            );

            // ----- Ejecutar menu -----
            new Menu(ctx).setVisible(true);

        });

    }
}