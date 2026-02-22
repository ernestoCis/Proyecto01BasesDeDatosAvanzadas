/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.Empleado;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author Isaac
 */
public interface iEmpleadoDAO {

    /**
     * Consulta un empleado por su usuario en la tabla
     *
     * @param usuario usuario a buscar
     * @return Empleado encontrado o null si no existe
     * @throws PersistenciaException si ocurre un error en BD
     */
    Empleado consultarEmpleado(String usuario) throws PersistenciaException;

    /**
     * Inserta un empleado.
     *
     * Se debe manejar en transacción.
     *
     * @param empleado empleado a insertar
     * @return empleado con id generado
     * @throws PersistenciaException si falla la inserción
     */
    Empleado insertarEmpleado(Empleado empleado) throws PersistenciaException;
}
