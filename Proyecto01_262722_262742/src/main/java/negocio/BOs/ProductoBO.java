/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Producto;
import java.util.List;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import persistencia.DAOs.iProductoDAO;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Objeto de Negocio (BO) para la gestión del catálogo de Productos.</b>
 * <p>Esta clase encapsula la lógica de negocio relacionada con los productos 
 * que se ofrecen (panes, pasteles, etc.). Se asegura de que la información 
 * ingresada sea íntegra antes de interactuar con la capa de persistencia.</p>
 * * @author 262722
 * @author 262742
 */
public class ProductoBO implements iProductoBO {

    // DAO común
    private iProductoDAO productoDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());

    /**
     * Constructor que inyecta la dependencia del DAO de productos.
     * @param producto Implementación de la interfaz iProductoDAO.
     */
    public ProductoBO(iProductoDAO producto) {
        this.productoDAO = producto;
    }

    /**
     * Método auxiliar privado para centralizar la validación de un producto.
     * <p>Verifica que los campos obligatorios (nombre, tipo, estado, descripción) 
     * no sean nulos o vacíos, y que el precio sea estrictamente mayor a cero.</p>
     * * @param producto Objeto a validar.
     * @throws NegocioException Si alguno de los campos no cumple con las reglas de negocio.
     */
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

    /**
     * Consulta la información detallada de un producto específico.
     * * @param producto Objeto Producto que contiene al menos el ID a buscar.
     * @return El producto completo recuperado de la base de datos.
     * @throws NegocioException Si el ID es inválido o el producto es nulo.
     */
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

    /**
     * Registra un nuevo producto en el catálogo.
     * <p>Antes de insertar, se invocan las reglas de negocio a través de {@link #validarProducto(Producto)}.</p>
     * * @param producto El nuevo producto a registrar.
     * @return El producto insertado, incluyendo su ID generado.
     * @throws NegocioException Si los datos del producto son inválidos o ocurre un error al guardar.
     */
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

    /**
     * Actualiza la información de un producto existente en el catálogo.
     * <p>Valida la presencia de un ID válido y la integridad de los datos mediante {@link #validarProducto(Producto)}.</p>
     * * @param producto Objeto con los datos actualizados.
     * @return El producto tras ser actualizado en la base de datos.
     * @throws NegocioException Si el ID es inválido, faltan datos o hay un error de persistencia.
     */
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

    /**
     * Recupera la lista completa de todos los productos en el sistema.
     * * @return Una lista de objetos <code>Producto</code>.
     * @throws NegocioException Si ocurre un problema al comunicarse con la base de datos.
     */
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