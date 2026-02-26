/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.Producto;
import java.util.List;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Interfaz para el Data Access Object (DAO) de Productos.</b>
 * <p>Define el contrato para las operaciones de persistencia relacionadas con el 
 * catálogo de productos, permitiendo su consulta individual, registro, 
 * actualización y listado general.</p>
 *
 * @author 262722
 * @author 262742
 */
public interface iProductoDAO {
    
    /**
     * Consulta un producto específico en la base de datos.
     * * @param producto Objeto {@link Producto} que contiene el criterio de búsqueda (normalmente el ID).
     * @return El objeto {@link Producto} con la información completa recuperada de la BD.
     * @throws PersistenciaException Si ocurre un error de SQL o el producto no existe.
     */
    public Producto consultarProducto(Producto producto) throws PersistenciaException;
    
    /**
     * Inserta un nuevo producto en el catálogo de la base de datos.
     * * @param producto Objeto {@link Producto} con los datos a persistir.
     * @return El objeto {@link Producto} insertado, incluyendo su ID autogenerado.
     * @throws PersistenciaException Si ocurre un error durante la inserción en la BD.
     */
    public Producto insertarProducto(Producto producto) throws PersistenciaException;
    
    /**
     * Actualiza la información de un producto ya existente en la base de datos.
     * * @param producto Objeto {@link Producto} con los nuevos datos y el ID correspondiente.
     * @return El objeto {@link Producto} tras aplicar los cambios.
     * @throws PersistenciaException Si ocurre un error de SQL o el ID no se encuentra.
     */
    public Producto actualizarProducto(Producto producto) throws PersistenciaException;
    
    /**
     * Recupera una lista con todos los productos registrados en la base de datos.
     * <p>Generalmente se utiliza para mostrar el catálogo disponible al usuario o empleado.</p>
     * * @return Una {@link List} de objetos {@link Producto}.
     * @throws PersistenciaException Si ocurre un error al ejecutar la consulta SQL.
     */
    public List<Producto> listarProductos() throws PersistenciaException;
}