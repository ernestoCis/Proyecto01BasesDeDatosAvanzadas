/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.Empleado;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Interfaz para el Data Access Object (DAO) de Empleados.</b>
 * <p>Define el contrato para las operaciones de persistencia relacionadas con los 
 * empleados del sistema, permitiendo su consulta y registro seguro en la base de datos.</p>
 *
 * @author 262722
 * @author 262742
 */
public interface iEmpleadoDAO {

    /**
     * Consulta y recupera un empleado en la base de datos utilizando su nombre de usuario.
     * * @param usuario El nombre de usuario (credencial) del empleado a buscar.
     * @return El objeto {@link Empleado} poblado con los datos encontrados, o {@code null} si no existe.
     * @throws PersistenciaException Si ocurre un error de SQL durante la consulta.
     */
    Empleado consultarEmpleado(String usuario) throws PersistenciaException;

    /**
     * Inserta un nuevo empleado en la base de datos.
     * <p><b>Nota de implementación:</b> Esta operación debe manejarse de manera transaccional,
     * ya que la creación de un empleado generalmente requiere registrar primero un 
     * usuario base para mantener la integridad referencial.</p>
     * * @param empleado Objeto {@link Empleado} que contiene la información a persistir.
     * @return El objeto {@link Empleado} insertado, con su ID autogenerado asignado.
     * @throws PersistenciaException Si ocurre un error al ejecutar la inserción SQL o la transacción falla.
     */
    Empleado insertarEmpleado(Empleado empleado) throws PersistenciaException;
}