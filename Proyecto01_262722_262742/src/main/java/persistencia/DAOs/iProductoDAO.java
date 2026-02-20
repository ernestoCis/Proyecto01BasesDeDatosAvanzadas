/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.Producto;
import java.util.List;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author jesus
 */
public interface iProductoDAO {
    /**
     * Metodo para consultar un producto a la BD
     * @param producto producto a buscar
     * @return regresa el producto consultado
     * @throws PersistenciaException PersistenciaException por falla en SQL
     */
    public Producto consultarProducto(Producto producto) throws PersistenciaException;
    
    /**
     * metodo para agregar un preoducto a la BD
     * @param producto producto a ingresar
     * @return producto insertado
     * @throws PersistenciaException PersistenciaException excepcion por falla en SQL
     */
    public Producto insertarProducto(Producto producto) throws PersistenciaException;
    
    /**
     * metodo para actualizar a un produtco en la BD
     * @param producto producto a actualizazar
     * @return producto actualizado
     * @throws PersistenciaException PersistenciaException excepcion por falla en SQL
     */
    public Producto actualizarProducto(Producto producto) throws PersistenciaException;
    
    /**
     * metodo que enlista todos los productos de la BD
     * @return regresa una lista con todos los productos
     * @throws PersistenciaException excepcion por falla en SQL
     */
    public List<Producto> listarProductos() throws PersistenciaException;
}
