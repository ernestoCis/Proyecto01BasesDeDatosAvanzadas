/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Producto;
import java.util.List;
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

    /**
     * metodo que obtiene del DAO los valores de un producto
     * @param producto 
     * @return
     * @throws NegocioException 
     */
    @Override
    public Producto consultarProducto(Producto producto) throws NegocioException {
        try{
            //llamamos al DAO y consultamos el producto
            Producto productoConsultado = this.productoDAO.consultarProducto(producto);
            if(productoConsultado == null){
                LOG.warning("No se pudó obtener el producto con id: " + producto.getId());
                throw new NegocioException("No se obtuvo ningun producto con id: " + producto.getId());
            }
            
            //existe el producto
            return productoConsultado;
            
        }catch(PersistenciaException ex){
            LOG.warning("No se pudo obtener el producto con id: " + producto.getId() + ". " + ex);
            throw new NegocioException("Problemas al intentar consultar el producto con id: " + producto.getId() + ". " + ex.getMessage());
        }
    }

    @Override
    public Producto insertarProducto(Producto producto) throws NegocioException {
        try {
            return productoDAO.insertarProducto(producto);
        } catch (PersistenciaException ex) {
            LOG.warning("Error al agregar el produtco" + ex);
            throw new NegocioException("Error al agregar el producto: " + ex.getMessage());
        }
    }

    @Override
    public Producto actualizarProducto(Producto producto) throws NegocioException {
        try {
            return productoDAO.actualizarProducto(producto);
            
        } catch (PersistenciaException ex) {
            LOG.warning("Error al actualizar el producto " + ex);
            throw new NegocioException("Error al aztualizar el producto. " + ex.getMessage());
        }
    }

    @Override
    public List<Producto> listarProductos() throws NegocioException {
        try {
            return productoDAO.listarProductos();
        } catch (PersistenciaException ex) {
            throw new NegocioException("No se pudieron listar los productos.", ex);
        }
    }
    
}
