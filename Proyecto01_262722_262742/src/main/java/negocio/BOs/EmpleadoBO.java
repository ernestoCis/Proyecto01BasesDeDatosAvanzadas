/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Empleado;
import dominio.RolUsuario;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import negocio.util.PasswordUtil;
import persistencia.DAOs.iEmpleadoDAO;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author
 */
public class EmpleadoBO implements iEmpleadoBO {

    private iEmpleadoDAO empleadoDAO;
    private static final Logger LOG = Logger.getLogger(EmpleadoBO.class.getName());

    public EmpleadoBO(iEmpleadoDAO empleadoDAO) {
        this.empleadoDAO = empleadoDAO;
    }

    @Override
    public Empleado registrarEmpleado(Empleado empleado) throws NegocioException {
        try {
            if (empleado == null) {
                throw new NegocioException("El empleado es obligatorio.");
            }

            if (empleado.getUsuario() == null || empleado.getUsuario().trim().isEmpty()) {
                throw new NegocioException("El usuario es obligatorio.");
            }

            if (empleado.getContrasenia() == null || empleado.getContrasenia().trim().isEmpty()) {
                throw new NegocioException("La contraseña es obligatoria.");
            }

            // Evitar duplicados 
            Empleado existente = empleadoDAO.consultarEmpleado(empleado.getUsuario().trim());
            if (existente != null) {
                throw new NegocioException("Ya existe un empleado con ese usuario.");
            }

            empleado.setRol(RolUsuario.Empleado);

            String hash = PasswordUtil.hash(empleado.getContrasenia());
            empleado.setContrasenia(hash);

            return empleadoDAO.insertarEmpleado(empleado);

        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo registrar al empleado. " + ex);
            throw new NegocioException("Error al registrar al empleado: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Empleado consultarEmpleado(String usuario, String contrasenia) throws NegocioException {
        try {
            if (usuario == null || usuario.trim().isEmpty()) {
                throw new NegocioException("El usuario es obligatorio.");
            }

            if (contrasenia == null || contrasenia.trim().isEmpty()) {
                throw new NegocioException("La contraseña es obligatoria.");
            }

            Empleado empleado = empleadoDAO.consultarEmpleado(usuario.trim());

            if (empleado == null) {
                throw new NegocioException("Usuario o contraseña incorrectos.");
            }

            if (!PasswordUtil.verificar(contrasenia, empleado.getContrasenia())) {
                throw new NegocioException("Usuario o contraseña incorrectos.");
            }

            return empleado;

        } catch (PersistenciaException ex) {
            LOG.warning("Error al consultar al empleado. " + ex);
            throw new NegocioException("Error al consultar empleado. " + ex.getMessage(), ex);
        }
    }
}