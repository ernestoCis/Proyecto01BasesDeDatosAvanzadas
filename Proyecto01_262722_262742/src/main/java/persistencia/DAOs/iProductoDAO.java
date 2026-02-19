/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.Producto;
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
     * @throws NegocioException excepcion por regla de negocio
     */
    public Producto consultarProducto(Producto producto) throws PersistenciaException;
    
    /**
     * metodo para agregar un preoducto a la BD
     * @param producto producto a ingresar
     * @return producto insertado
     * @throws NegocioException NegocioException excepcion por regla de negocio
     */
    public Producto insertarProducto(Producto producto) throws PersistenciaException;
    
    /**
     * metodo para actualizar a un produtco en la BD
     * @param producto producto a actualizazar
     * @return producto actualizado
     * @throws NegocioException NegocioException excepcion por regla de negocio
     */
    public Producto actualizarProducto(Producto producto) throws PersistenciaException;
}
