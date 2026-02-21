/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Cliente;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import persistencia.DAOs.iClienteDAO;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author
 */
public class ClienteBO implements iClienteBO{
    
    //DAO comun
    private iClienteDAO clienteDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());
    
    public ClienteBO(iClienteDAO clienteDAO){
        this.clienteDAO = clienteDAO;
    }
    

    @Override
    public Cliente registrarCliente(Cliente cliente) throws NegocioException {
        try {
            if (cliente == null) {
                throw new NegocioException("El cliente es obligatorio.");
            }

            // Validaciones mínimas
            if (cliente.getNombres() == null || cliente.getNombres().trim().isEmpty()) {
                throw new NegocioException("El nombre es obligatorio.");
            }

            if (cliente.getUsuario() == null || cliente.getUsuario().trim().isEmpty()) {
                throw new NegocioException("El usuario es obligatorio.");
            }

            // Evitar duplicados por correo
            Cliente existente = clienteDAO.consultarCliente(cliente.getUsuario().trim());
            if (existente != null) {
                throw new NegocioException("Ya existe un cliente con ese usuario.");
            }

            return clienteDAO.insertarCliente(cliente);

        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo registrar al cliente. " + ex);
            throw new NegocioException("Error al registrar al cliente: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Cliente consultarCliente(String usuario) throws NegocioException {
        try {
            if (usuario == null || usuario.trim().isEmpty()) {
                throw new NegocioException("El usuario es obligatorio.");
            }

            Cliente cliente = clienteDAO.consultarCliente(usuario.trim());

            if (cliente == null) {
                throw new NegocioException("No existe un cliente con ese usuario.");
            }

            return cliente;

        } catch (PersistenciaException ex) {
            LOG.warning("Error al consultar al cliente. " + ex);
            throw new NegocioException("Error al consultar cliente. " + ex.getMessage(), ex);
        }
    }
    
}
