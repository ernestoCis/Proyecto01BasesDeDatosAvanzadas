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
 * Fábrica para crear el AppContext una sola vez.
 */
public class FabricaAplicacion {

    public static AppContext crearContexto() {

        // Conexión
        iConexionBD conexion = new ConexionBD();

        // DAOs
        iUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
        iProductoDAO productoDAO = new ProductoDAO(conexion);
        iCuponDAO cuponDAO = new CuponDAO(conexion);
        iPedidoDAO pedidoDAO = new PedidoDAO(conexion);
        iClienteDAO clienteDAO = new ClienteDAO(conexion);

        // BOs
        iUsuarioBO usuarioBO = new UsuarioBO(usuarioDAO);
        iProductoBO productoBO = new ProductoBO(productoDAO);
        iCuponBO cuponBO = new CuponBO(cuponDAO);
        iPedidoBO pedidoBO = new PedidoBO(pedidoDAO);
        iClienteBO clienteBO = new ClienteBO(clienteDAO);

        return new AppContext(usuarioBO, productoBO, cuponBO, pedidoBO, clienteBO);
    }
}