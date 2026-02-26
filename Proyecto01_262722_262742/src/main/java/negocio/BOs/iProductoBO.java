/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import dominio.Producto;
import java.util.List;
import negocio.Excepciones.NegocioException;

/**
 * <b>Interfaz para el Objeto de Negocio (BO) de Productos.</b>
 * <p>Define el contrato para la gestión del catálogo de productos en el sistema. 
 * Garantiza que las operaciones de consulta, inserción y actualización apliquen 
 * las reglas de negocio correspondientes antes de interactuar con la capa de datos.</p>
 *
 * @author jesus
 * @author 262722
 * @author 262742
 */
public interface iProductoBO {
    
    /**
     * Consulta la información de un producto específico en la base de datos.
     * * @param producto Objeto Producto que contiene el criterio de búsqueda (ej. ID).
     * @return El objeto <code>Producto</code> con toda su información recuperada.
     * @throws NegocioException Si el producto no existe, los datos de búsqueda son inválidos o hay error de conexión.
     */
    public Producto consultarProducto(Producto producto) throws NegocioException;
    
    /**
     * Valida y registra un nuevo producto en el catálogo del sistema.
     * * @param producto Objeto de dominio con los datos del producto a ingresar.
     * @return El producto insertado de manera exitosa, incluyendo su ID generado.
     * @throws NegocioException Si el producto no cumple con las reglas de negocio (ej. campos vacíos) o falla la base de datos.
     */
    public Producto insertarProducto(Producto producto) throws NegocioException;
    
    /**
     * Valida y actualiza la información de un producto existente en el catálogo.
     * * @param producto Objeto con los datos actualizados a guardar.
     * @return El producto tras ser modificado exitosamente.
     * @throws NegocioException Si los datos son inválidos, el ID no existe o ocurre un error de persistencia.
     */
    public Producto actualizarProducto(Producto producto) throws NegocioException;
    
    
    /**
     * Recupera el catálogo completo de productos disponibles en el sistema.
     * * @return Una lista que contiene todos los objetos <code>Producto</code> registrados.
     * @throws NegocioException Si ocurre un error al procesar la solicitud en la capa de datos.
     */
    public List<Producto> listarProductos() throws NegocioException;
}