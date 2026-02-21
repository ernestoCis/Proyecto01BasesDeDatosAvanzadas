/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Cliente;
import dominio.Telefono;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import negocio.util.PasswordUtil;
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
    public Cliente registrarCliente(Cliente cliente, Telefono telefono) throws NegocioException {
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
            
            // hasheo de la contraseña
            String hash = PasswordUtil.hash(cliente.getContrasenia());
            cliente.setContrasenia(hash);

            return clienteDAO.insertarCliente(cliente, telefono);

        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo registrar al cliente. " + ex);
            throw new NegocioException("Error al registrar al cliente: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Cliente consultarCliente(String usuario, String contrasenia) throws NegocioException {
        try {
            if (usuario == null || usuario.trim().isEmpty()) {
                throw new NegocioException("El usuario es obligatorio.");
            }
            
            if (contrasenia == null || contrasenia.trim().isEmpty()) {
                throw new NegocioException("La contraseña es obligatoria.");
            }

            Cliente cliente = clienteDAO.consultarCliente(usuario.trim());
            
            if(cliente == null){
                throw new NegocioException("Usuario o contraseña incorrectos.");
            }

            if(!PasswordUtil.verificar(contrasenia, cliente.getContrasenia())){
                throw new NegocioException("Usuario o contraseña incorrectos.");
            }

            return cliente;

        } catch (PersistenciaException ex) {
            LOG.warning("Error al consultar al cliente. " + ex);
            throw new NegocioException("Error al consultar cliente. " + ex.getMessage(), ex);
        }
    }
    
}
