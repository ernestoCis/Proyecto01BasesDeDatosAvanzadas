/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Producto;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import persistencia.DAOs.ProductoDAO;
import persistencia.DAOs.iProductoDAO;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author jesus
 */
public class ProductoBO implements iProductoBO{
    
    //DAO común
    private iProductoDAO productoDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());
    
    public ProductoBO(iProductoDAO producto){
        this.productoDAO = producto; // asignamos valor al DAO
    }

    @Override
    public Producto consultarProducto(Producto producto) throws NegocioException {
        try{
            //llamamos al DAO y guardamos el producto
            Producto productoConsultado = this.productoDAO.consultarProducto(producto);
            if(productoConsultado == null){
                LOG.warning("No se pudó obtener el producto con id: " + producto.getId());
                throw new NegocioException("No se obtuvo ningun producto con id: " + producto.getId());
            }
            
            //existe el producto
            return productoConsultado;
            
        }catch(PersistenciaException ex){
            LOG.warning("No se pudo obtener el tecnico con id: " + producto.getId());
            throw new NegocioException("Problemas al intentar consultar el producto con id: " + producto.getId(), ex);
        }
    }

    @Override
    public Producto insertarProducto(Producto producto) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Producto actualizarProducto(Producto producto) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
