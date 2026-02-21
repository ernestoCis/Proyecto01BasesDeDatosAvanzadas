/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Cliente;
import dominio.Empleado;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import persistencia.DAOs.iUsuarioDAO;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author Isaac
 */
public class UsuarioBO implements iUsuarioBO {

    private iUsuarioDAO usuarioDAO;
    private static final Logger LOG = Logger.getLogger(UsuarioBO.class.getName());

    public UsuarioBO(iUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Metodo para autenticar a un empleado.
     * @param usuario usuario ingresado
     * @param contrasenia contraseña ingresada 
     * @return true si es correcto, false si usuario o c0ontraseña no coinciden
     * @throws NegocioException si ocurre un error de persistencia o validación
     */
    @Override
    public boolean autenticarEmpleado(String usuario, String contrasenia) throws NegocioException {
        try {
            if (usuario == null || usuario.trim().isEmpty()) {
                throw new NegocioException("El usuario es obligatorio.");
            }
            if (contrasenia == null || contrasenia.isEmpty()) {
                throw new NegocioException("La contraseña es obligatoria.");
            }

            Empleado empleado = usuarioDAO.consultarEmpleadoPorUsuario(usuario.trim());

            // Si no existe, po no
            if (empleado == null) {
                return false;
            }

            // Comparación directa (texto plano)
            return empleado.getContrasenia() != null && empleado.getContrasenia().equals(contrasenia);

        } catch (PersistenciaException ex) {
            LOG.warning("Error al autenticar empleado. " + ex);
            throw new NegocioException("Error al autenticar empleado: " + ex.getMessage(), ex);
        }
    }

    /**
     * Metodo para autenticar a un cliente.
     * @param usuario usuario ingresado
     * @param contrasenia contraseña ingresada 
     * @return true si es correcto, false si usuario/contraseña no coinciden
     * @throws NegocioException si ocurre un error de persistencia o validación
     */
    @Override
    public boolean autenticarCliente(String usuario, String contrasenia) throws NegocioException {
        try {
            if (usuario == null || usuario.trim().isEmpty()) {
                throw new NegocioException("El usuario es obligatorio.");
            }
            if (contrasenia == null || contrasenia.isEmpty()) {
                throw new NegocioException("La contraseña es obligatoria.");
            }

            Cliente cliente = usuarioDAO.consultarClientePorUsuario(usuario.trim());

            // Si no existe, po no
            if (cliente == null) {
                return false;
            }

            // Comparación directa (texto plano)
            return cliente.getContrasenia() != null && cliente.getContrasenia().equals(contrasenia);

        } catch (PersistenciaException ex) {
            LOG.warning("Error al autenticar cliente. " + ex);
            throw new NegocioException("Error al autenticar cliente: " + ex.getMessage(), ex);
        }
    }
}
