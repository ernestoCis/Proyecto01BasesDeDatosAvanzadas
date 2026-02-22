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
public class ProductoBO implements iProductoBO {

    // DAO común
    private iProductoDAO productoDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());

    public ProductoBO(iProductoDAO producto) {
        this.productoDAO = producto;
    }

    private void validarProducto(Producto producto) throws NegocioException {
        if (producto == null) {
            throw new NegocioException("El producto es obligatorio.");
        }
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new NegocioException("El nombre del producto es obligatorio.");
        }
        if (producto.getTipo() == null) {
            throw new NegocioException("El tipo del producto es obligatorio.");
        }
        if (producto.getEstado() == null) {
            throw new NegocioException("El estado del producto es obligatorio.");
        }
        if (producto.getPrecio() <= 0) {
            throw new NegocioException("El precio debe ser mayor a 0.");
        }
        if (producto.getDescripcion() == null || producto.getDescripcion().trim().isEmpty()) {
            throw new NegocioException("La descripción del producto es obligatoria.");
        }
    }

    @Override
    public Producto consultarProducto(Producto producto) throws NegocioException {
        try {
            if (producto == null) {
                throw new NegocioException("El producto es obligatorio.");
            }
            if (producto.getId() <= 0) {
                throw new NegocioException("El ID del producto no es válido.");
            }

            return this.productoDAO.consultarProducto(producto);

        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo obtener el producto con id: " + (producto != null ? producto.getId() : "null") + ". " + ex);
            throw new NegocioException("Problemas al intentar consultar el producto. " + ex.getMessage());
        }
    }

    @Override
    public Producto insertarProducto(Producto producto) throws NegocioException {
        try {
            validarProducto(producto);
            return productoDAO.insertarProducto(producto);

        } catch (PersistenciaException ex) {
            LOG.warning("Error al agregar el producto " + ex);
            throw new NegocioException("Error al agregar el producto: " + ex.getMessage());
        }
    }

    @Override
    public Producto actualizarProducto(Producto producto) throws NegocioException {
        try {
            if (producto == null) {
                throw new NegocioException("El producto es obligatorio.");
            }
            if (producto.getId() <= 0) {
                throw new NegocioException("El ID del producto no es válido.");
            }
            validarProducto(producto);

            return productoDAO.actualizarProducto(producto);

        } catch (PersistenciaException ex) {
            LOG.warning("Error al actualizar el producto " + ex);
            throw new NegocioException("Error al actualizar el producto. " + ex.getMessage());
        }
    }

    @Override
    public List<Producto> listarProductos() throws NegocioException {
        try {
            return productoDAO.listarProductos();
        } catch (PersistenciaException ex) {
            LOG.warning("No se pudieron listar los productos. " + ex);
            throw new NegocioException("No se pudieron listar los productos.", ex);
        }
    }
}