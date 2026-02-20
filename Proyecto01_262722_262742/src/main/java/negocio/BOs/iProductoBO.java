/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import dominio.Producto;
import java.util.List;
import negocio.Excepciones.NegocioException;

/**
 *
 * @author jesus
 */
public interface iProductoBO {
    /**
     * Metodo para consultar un producto a la BD
     * @param producto producto a buscar
     * @return regresa el producto consultado
     * @throws NegocioException excepcion por regla de negocio
     */
    public Producto consultarProducto(Producto producto) throws NegocioException;
    
    /**
     * metodo para agregar un preoducto a la BD mediante DTO
     * @param producto producto a ingresar
     * @return producto insertado
     * @throws NegocioException NegocioException excepcion por regla de negocio
     */
    public Producto insertarProducto(Producto producto) throws NegocioException;
    
    /**
     * metodo para actualizar a un produtco en la BD
     * @param producto producto a actualizazar
     * @return producto actualizado
     * @throws NegocioException NegocioException excepcion por regla de negocio
     */
    public Producto actualizarProducto(Producto producto) throws NegocioException;
    
    /**
     * metodo que lista todos los productos del DAO
     * @return lista de productos
     * @throws NegocioException excepcion por regla de negocio
     */
    public List<Producto> listarProductos() throws NegocioException;
}
