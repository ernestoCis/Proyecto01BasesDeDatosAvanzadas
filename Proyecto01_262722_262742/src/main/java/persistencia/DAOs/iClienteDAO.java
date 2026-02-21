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
    
    public Cliente insertarCliente(Cliente cliente, Telefono telefono) throws PersistenciaException;
}
