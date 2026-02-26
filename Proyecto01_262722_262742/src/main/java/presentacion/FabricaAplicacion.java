/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

import negocio.BOs.*;
import persistencia.Conexion.ConexionBD;
import persistencia.Conexion.iConexionBD;
import persistencia.DAOs.*;

/**
 * <p>
 * Clase responsable de la creación y ensamblaje de todas las dependencias
 * principales del sistema.
 * </p>
 *
 * <p>
 * Implementa el patrón <b>Factory</b>, permitiendo centralizar la instanciación
 * de:
 * </p>
 *
 * <ul>
 * <li>La conexión a la base de datos.</li>
 * <li>Los objetos DAO (capa de persistencia).</li>
 * <li>Los objetos BO (capa de negocio).</li>
 * <li>El contexto global de la aplicación (AppContext).</li>
 * </ul>
 *
 * <p>
 * Esta clase garantiza que el <b>AppContext</b> se cree correctamente con todas
 * sus dependencias ya configuradas, promoviendo una arquitectura en capas
 * limpia y desacoplada.
 * </p>
 *
 * <h2>Arquitectura aplicada</h2>
 * <ul>
 * <li>Capa de Persistencia → DAOs</li>
 * <li>Capa de Negocio → BOs</li>
 * <li>Contexto compartido → AppContext</li>
 * </ul>
 *
 * <p>
 * De esta manera, las pantallas únicamente reciben el AppContext sin
 * preocuparse por cómo se construyen internamente sus dependencias.
 * </p>
 *
 * @author 262722, 2627242
 */
public class FabricaAplicacion {

    /**
     * <p>
     * Método estático encargado de crear y configurar el contexto completo de
     * la aplicación.
     * </p>
     *
     * <p>
     * El proceso que realiza es el siguiente:
     * </p>
     *
     * <ol>
     * <li>Instancia la conexión a la base de datos.</li>
     * <li>Crea los DAOs necesarios.</li>
     * <li>Crea los BO utilizando los DAOs correspondientes.</li>
     * <li>Construye y retorna un objeto AppContext con todos los BO.</li>
     * </ol>
     *
     * <p>
     * Este método asegura que todas las dependencias estén correctamente
     * enlazadas antes de iniciar la aplicación.
     * </p>
     *
     * @return instancia completamente configurada de AppContext
     */
    public static AppContext crearContexto() {

        /**
         * Instancia de la conexión a la base de datos.
         */
        iConexionBD conexion = new ConexionBD();

        // =======================
        // DAOs (Persistencia)
        // =======================
        /**
         * DAO para operaciones relacionadas con usuarios.
         */
        iUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);

        /**
         * DAO para operaciones relacionadas con productos.
         */
        iProductoDAO productoDAO = new ProductoDAO(conexion);

        /**
         * DAO para operaciones relacionadas con cupones.
         */
        iCuponDAO cuponDAO = new CuponDAO(conexion);

        /**
         * DAO para operaciones relacionadas con pedidos.
         */
        iPedidoDAO pedidoDAO = new PedidoDAO(conexion);

        /**
         * DAO para operaciones relacionadas con clientes.
         */
        iClienteDAO clienteDAO = new ClienteDAO(conexion);

        /**
         * DAO para operaciones relacionadas con detalles de pedido.
         */
        iDetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO(conexion);

        /**
         * DAO para operaciones relacionadas con empleados.
         */
        iEmpleadoDAO empleadoDAO = new EmpleadoDAO(conexion);

        // =======================
        // BOs (Negocio)
        // =======================
        /**
         * BO encargado de la lógica de usuarios.
         */
        iUsuarioBO usuarioBO = new UsuarioBO(usuarioDAO);

        /**
         * BO encargado de la lógica de productos.
         */
        iProductoBO productoBO = new ProductoBO(productoDAO);

        /**
         * BO encargado de la lógica de cupones.
         */
        iCuponBO cuponBO = new CuponBO(cuponDAO);

        /**
         * BO encargado de la lógica de pedidos. Requiere acceso a pedidoDAO,
         * detallePedidoDAO y cuponDAO.
         */
        iPedidoBO pedidoBO = new PedidoBO(pedidoDAO, detallePedidoDAO, cuponDAO);

        /**
         * BO encargado de la lógica de clientes.
         */
        iClienteBO clienteBO = new ClienteBO(clienteDAO);

        /**
         * BO encargado de la lógica de empleados.
         */
        iEmpleadoBO empleadoBO = new EmpleadoBO(empleadoDAO);

        /**
         * BO encargado de la lógica de detalles de pedido.
         */
        iDetallePedidoBO detallePedidoBO = new DetallePedidoBO(detallePedidoDAO);

        /**
         * Retorna el contexto completo de la aplicación con todas sus
         * dependencias correctamente configuradas.
         */
        return new AppContext(
                usuarioBO,
                productoBO,
                cuponBO,
                pedidoBO,
                clienteBO,
                empleadoBO,
                detallePedidoBO
        );
    }
}
