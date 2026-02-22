/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.Cliente;
import dominio.Telefono;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author 
 */
public interface iClienteDAO {
    
    /**
     * metodo para consultar a un cliente en la BD
     * @return regresa el cliente cnsultado
     * @throws PersistenciaException 
     */
    public Cliente consultarCliente(String usuario) throws PersistenciaException;
    
    /**
     * metodo que inserta un cliente a la abla clientes y su demas info como direccion y telefono
     * @param cliente cliente a insertar
     * @param telefono telefono a insertar
     * @return cliente insertado
     * @throws PersistenciaException excepcion por si sql falla
     */
    public Cliente insertarCliente(Cliente cliente, Telefono telefono) throws PersistenciaException;
    
    /**
     * metodo que actualiza los datos de un cliente
     * @param cliente cliente con informacion a actualizar
     * @return cliente actualizado
     * @throws PersistenciaException excepcion por si el sql falla
     */
    public Cliente actualizarCliente(Cliente cliente) throws PersistenciaException;
}
