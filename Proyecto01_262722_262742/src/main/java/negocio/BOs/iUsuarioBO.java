/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

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
     * @return true si la autenticacion es correcta, false en caso contrario
     * @throws NegocioException si ocurre un error en la capa de negocio
     */
    boolean autenticarEmpleado(String usuario, String contrasenia) throws NegocioException;

    /**
     * Metodo para autenticar a un cliente del sistema.
     *
     * @param usuario nombre de usuario ingresado
     * @param contrasenia contraseña en texto plano ingresada
     * @return true si la autenticacion es correcta, false en caso contrario
     * @throws NegocioException si ocurre un error en la capa de negocio
     */
    boolean autenticarCliente(String usuario, String contrasenia) throws NegocioException;
}