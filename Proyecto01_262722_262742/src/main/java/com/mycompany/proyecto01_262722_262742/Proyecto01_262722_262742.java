/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyecto01_262722_262742;

import java.sql.Connection;
import negocio.BOs.CuponBO;
import negocio.BOs.ProductoBO;
import negocio.BOs.iCuponBO;
import negocio.BOs.iProductoBO;
import persistencia.Conexion.ConexionBD;
import persistencia.Conexion.iConexionBD;
import persistencia.DAOs.CuponDAO;
import persistencia.DAOs.PedidoDAO;
import persistencia.DAOs.ProductoDAO;
import persistencia.DAOs.iCuponDAO;
import persistencia.DAOs.iPedidoDAO;
import persistencia.DAOs.iProductoDAO;
import presentacion.Menu;

/**
 *
 * @author jesus
 */
public class Proyecto01_262722_262742 {

    public static void main(String[] args){
        
        //conexion
        iConexionBD conexion = new ConexionBD();
        
        //pedido
        iPedidoDAO pedidoDAO = new PedidoDAO(conexion);
        
        //producto
        iProductoDAO productoDAO = new ProductoDAO(conexion);
        iProductoBO productoBO = new ProductoBO(productoDAO);
        
        //cupon
        iCuponDAO cuponDAO = new CuponDAO(conexion);
        iCuponBO cuponBO = new CuponBO(cuponDAO);
        
        
        
        Menu menu = new Menu(productoBO, cuponBO);
        menu.setVisible(true);
        
    }
}
