/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.proyecto01_262722_262742;

import javax.swing.SwingUtilities;
import negocio.BOs.ClienteBO;
import negocio.BOs.CuponBO;
import negocio.BOs.PedidoBO;
import negocio.BOs.ProductoBO;
import negocio.BOs.UsuarioBO;
import negocio.BOs.iClienteBO;
import negocio.BOs.iCuponBO;
import negocio.BOs.iPedidoBO;
import negocio.BOs.iProductoBO;
import negocio.BOs.iUsuarioBO;
import persistencia.Conexion.ConexionBD;
import persistencia.Conexion.iConexionBD;
import persistencia.DAOs.ClienteDAO;
import persistencia.DAOs.CuponDAO;
import persistencia.DAOs.DetallePedidoDAO;
import persistencia.DAOs.PedidoDAO;
import persistencia.DAOs.ProductoDAO;
import persistencia.DAOs.UsuarioDAO;
import persistencia.DAOs.iClienteDAO;
import persistencia.DAOs.iCuponDAO;
import persistencia.DAOs.iDetallePedidoDAO;
import persistencia.DAOs.iPedidoDAO;
import persistencia.DAOs.iProductoDAO;
import persistencia.DAOs.iUsuarioDAO;
import presentacion.AppContext;
import presentacion.Menu;

/**
 *
 * @author jesus
 */
public class Proyecto01_262722_262742 {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // ===== Conexion =====
            iConexionBD conexion = new ConexionBD();

            // ===== DAOs =====
            iUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
            iProductoDAO productoDAO = new ProductoDAO(conexion);
            iCuponDAO cuponDAO = new CuponDAO(conexion);
            iPedidoDAO pedidoDAO = new PedidoDAO(conexion);
            iClienteDAO clienteDAO = new ClienteDAO(conexion);
            iDetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO(conexion);

            // ===== BOs =====
            iUsuarioBO usuarioBO = new UsuarioBO(usuarioDAO);
            iProductoBO productoBO = new ProductoBO(productoDAO);
            iCuponBO cuponBO = new CuponBO(cuponDAO);
            iPedidoBO pedidoBO = new PedidoBO(pedidoDAO, detallePedidoDAO);
            iClienteBO clienteBO = new ClienteBO(clienteDAO);

            // ===== AppContext =====
            AppContext ctx = new AppContext(
                    usuarioBO,
                    productoBO,
                    cuponBO,
                    pedidoBO,
                    clienteBO
            );

            // ===== Ejecutar Menu =====
            new Menu(ctx).setVisible(true);

        });

    }
}
