/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Empleado;
import negocio.Excepciones.NegocioException;

/**
 *
 * @author Isaac
 */
public interface iEmpleadoBO {

    /**
     * Metodo que registra un empleado en el sistema.
     * 
     * @param empleado empleado a registrar
     * @return empleado registrado (con id)
     * @throws NegocioException excepcion por reglas de negocio
     */
    public Empleado registrarEmpleado(Empleado empleado) throws NegocioException;

    /**
     * Metodo que consulta y valida credenciales de un empleado.
     * 
     * @param usuario usuario del empleado
     * @param contrasenia contrasenia en texto plano
     * @return empleado autenticado
     * @throws NegocioException excepcion por reglas de negocio
     */
    public Empleado consultarEmpleado(String usuario, String contrasenia) throws NegocioException;
}