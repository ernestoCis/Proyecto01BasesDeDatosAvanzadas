/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Cliente;
import dominio.Empleado;
import negocio.Excepciones.NegocioException;

/**
 * <b>Interfaz para el Objeto de Negocio (BO) de Usuarios.</b>
 * <p>Define el contrato para la autenticación y control de acceso al sistema. 
 * Separa claramente el inicio de sesión del personal interno (empleados) del 
 * acceso de los consumidores (clientes), garantizando que se apliquen las 
 * validaciones de seguridad correspondientes a cada rol.</p>
 *
 * @author Isaac
 * @author 262722
 * @author 262742
 */
public interface iUsuarioBO {

    
    
    /**
     * Autentica a un empleado en el sistema verificando sus credenciales.
     * <p>El método debe validar que los campos no estén vacíos y comparar la contraseña 
     * en texto plano contra el hash almacenado de forma segura en la base de datos.</p>
     *
     * @param usuario Nombre de usuario ingresado en el formulario.
     * @param contrasenia Contraseña ingresada en texto plano.
     * @return Objeto {@link Empleado} que contiene toda la información del usuario autenticado.
     * @throws NegocioException Si el usuario no existe, la contraseña es incorrecta o hay un error de conexión.
     */
    Empleado iniciarSesionEmpleado(String usuario, String contrasenia) throws NegocioException;

    /**
     * Autentica a un cliente en el sistema verificando sus credenciales.
     * <p>Al igual que con los empleados, valida los campos y verifica la contraseña 
     * utilizando un algoritmo de hashing seguro (como BCrypt).</p>
     *
     * @param usuario Nombre de usuario registrado por el cliente.
     * @param contrasenia Contraseña ingresada en texto plano.
     * @return Objeto {@link Cliente} que contiene la información completa del cliente autenticado.
     * @throws NegocioException Si las credenciales son inválidas o ocurre un error en la capa de persistencia.
     */
    Cliente iniciarSesionCliente(String usuario, String contrasenia) throws NegocioException;
}