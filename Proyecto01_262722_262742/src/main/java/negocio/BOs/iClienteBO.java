/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import dominio.Cliente;
import dominio.Telefono;
import java.util.List;
import negocio.Excepciones.NegocioException;

/**
 *
 * @author
 */
public interface iClienteBO {
    /**
     * metodo que agrega un cliente mediant el DAO
     * @param cliente cliente a registrar
     * @return cliente registrado
     * @throws NegocioException excepcion por reglas de negocio
     */
    public Cliente registrarCliente(Cliente cliente, Telefono telefono) throws NegocioException;
    
    /**
     * metodo que busca si un cliente ya esta registrado
     * @param usuario usuario del cliente registrado
     * @return cliente que esta registrado
     * @throws NegocioException excepcion por reglas de negocio
     */
    public Cliente consultarCliente(String usuario, String contrasenia) throws NegocioException;
    
    /**
     * metodo que actualiza un cliente mediante el DAO
     * @param cliente cliente con datos actualizados
     * @return cliente actualizado
     * @throws NegocioException excepcion por reglas de negocio
     */
    public Cliente actualizarCliente(Cliente cliente) throws NegocioException;
    
    /**
     * metodo que le agrega un telefono al cliente mediante el DAO
     * @param cliente cliente al que se le agregara el telefono
     * @param telefono telefono a agregar
     * @return cliente con el telefono agregado
     * @throws NegocioException excepcion por regla de negocio
     */
    public Cliente agregarTelefonos(Cliente cliente, List<Telefono> telefonos) throws NegocioException;
}
