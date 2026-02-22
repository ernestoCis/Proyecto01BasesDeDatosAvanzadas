/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Cliente;
import dominio.Empleado;
import negocio.Excepciones.NegocioException;

/**
 *
 * @author Isaac
 */
public interface iUsuarioBO {

    /**
     * Metodo para autenticar a un empleado del sistema.
     *
     * @param usuario nombre de usuario ingresado
     * @param contrasenia contrasenia en texto plano ingresada
     * @return Empleado empleado con toda su info
     * @throws NegocioException si ocurre un error en la capa de negocio
     */
    Empleado iniciarSesionEmpleado(String usuario, String contrasenia) throws NegocioException;

    /**
     * Metodo para autenticar a un cliente del sistema.
     *
     * @param usuario nombre de usuario ingresado
     * @param contrasenia contraseña en texto plano ingresada
     * @return Cliente cliente con toda su info
     * @throws NegocioException si ocurre un error en la capa de negocio
     */
    Cliente iniciarSesionCliente(String usuario, String contrasenia) throws NegocioException;
}
