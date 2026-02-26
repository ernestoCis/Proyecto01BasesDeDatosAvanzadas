/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Empleado;
import negocio.Excepciones.NegocioException;

/**
 * <b>Interfaz para el Objeto de Negocio (BO) de Empleados.</b>
 * <p>Define el contrato operativo para la gestión del personal en el sistema.
 * Garantiza que cualquier implementación respete las reglas de negocio críticas, 
 * como la unicidad de usuarios y el manejo seguro de credenciales.</p>
 *
 * @author Isaac
 * @author 262722
 * @author 262742
 */
public interface iEmpleadoBO {

    /**
     * Registra un nuevo empleado en el sistema.
     * <p>La implementación de este método debe garantizar que el nombre de usuario 
     * no esté duplicado, asignar el rol por defecto correspondiente y aplicar una 
     * función hash a la contraseña antes de enviarla a la capa de persistencia.</p>
     * * @param empleado Objeto con los datos del empleado a registrar.
     * @return El empleado registrado, incluyendo el identificador generado por la base de datos.
     * @throws NegocioException Si faltan datos obligatorios, el usuario ya existe o hay problemas de conexión.
     */
    public Empleado registrarEmpleado(Empleado empleado) throws NegocioException;

    /**
     * Consulta y valida las credenciales de un empleado para el control de acceso.
     * <p>Debe comparar la contraseña proporcionada en texto plano contra el hash 
     * almacenado en la base de datos.</p>
     * * @param usuario Nombre de usuario ingresado en el formulario de inicio de sesión.
     * @param contrasenia Contraseña ingresada en texto plano.
     * @return El objeto {@link Empleado} autenticado si las credenciales son correctas.
     * @throws NegocioException Si el usuario no existe, la contraseña es incorrecta o falla la base de datos.
     */
    public Empleado consultarEmpleado(String usuario, String contrasenia) throws NegocioException;
}