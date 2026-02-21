/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyecto01_262722_262742;

import java.sql.Connection;
import negocio.BOs.ClienteBO;
import negocio.BOs.CuponBO;
import negocio.BOs.PedidoBO;
import negocio.BOs.ProductoBO;
import negocio.BOs.iClienteBO;
import negocio.BOs.iCuponBO;
import negocio.BOs.iPedidoBO;
import negocio.BOs.iProductoBO;
import negocio.BOs.iUsuarioBO;
import persistencia.Conexion.ConexionBD;
import persistencia.Conexion.iConexionBD;
import persistencia.DAOs.ClienteDAO;
import persistencia.DAOs.CuponDAO;
import persistencia.DAOs.PedidoDAO;
import persistencia.DAOs.ProductoDAO;
import persistencia.DAOs.iClienteDAO;
import persistencia.DAOs.iCuponDAO;
import persistencia.DAOs.iPedidoDAO;
import persistencia.DAOs.iProductoDAO;
import persistencia.DAOs.iUsuarioDAO;
import presentacion.Menu;

/**
 *
 * @author jesus
 */
public class Proyecto01_262722_262742 {

    public static void main(String[] args){
        
        //conexion
        iConexionBD conexion = new ConexionBD();
        
        //usuario
        iUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
        iUsuarioBO usuarioBO = new UsuarioBO(usuarioDAO);
        
        //producto
        iProductoDAO productoDAO = new ProductoDAO(conexion);
        iProductoBO productoBO = new ProductoBO(productoDAO);
        
        //cupon
        iCuponDAO cuponDAO = new CuponDAO(conexion);
        iCuponBO cuponBO = new CuponBO(cuponDAO);
        
        //pedido
        iPedidoDAO pedidoDAO = new PedidoDAO(conexion);
        iPedidoBO pedidoBO = new PedidoBO(pedidoDAO);
        
        //cliente
        iClienteDAO clienteDAO = new ClienteDAO(conexion);
        iClienteBO clienteBO = new ClienteBO(clienteDAO);
        
        //ejecutar menu
        Menu menu = new Menu(productoBO, cuponBO, pedidoBO, clienteBO);
        menu.setVisible(true);
        
    }
}
