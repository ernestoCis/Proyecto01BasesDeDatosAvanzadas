/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import dominio.Cliente;
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
    public Cliente registrarCliente(Cliente cliente) throws NegocioException;
    
    /**
     * metodo que busca si un cliente ya esta registrado
     * @param usuario usuario del cliente registrado
     * @return cliente que esta registrado
     * @throws NegocioException excepcion por reglas de negocio
     */
    public Cliente consultarCliente(String usuario) throws NegocioException;
}
