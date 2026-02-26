/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.Cliente;
import dominio.Empleado;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Interfaz para el Data Access Object (DAO) de Usuarios.</b>
 * <p>Define el contrato para las operaciones de consulta de usuarios en el sistema.
 * Esta interfaz es fundamental para los procesos de seguridad y login, permitiendo
 * recuperar la información específica según el rol del usuario (Empleado o Cliente).</p>
 *
 * @author 262722
 * @author 262742
 */
public interface iUsuarioDAO {

    /**
     * Consulta y recupera la información de un empleado a través de su nombre de usuario.
     * <p>Se utiliza principalmente durante la autenticación de personal administrativo
     * o de ventas.</p>
     * * @param usuario El nombre de usuario (username) del empleado.
     * @return Objeto {@link Empleado} con sus datos y credenciales, o null si no se encuentra.
     * @throws PersistenciaException Si ocurre un error durante la ejecución de la consulta SQL.
     */
    Empleado consultarEmpleadoPorUsuario(String usuario) throws PersistenciaException;

    /**
     * Consulta y recupera la información de un cliente a través de su nombre de usuario.
     * <p>Se utiliza para validar el acceso de clientes y recuperar su perfil personal.</p>
     * * @param usuario El nombre de usuario (username) del cliente.
     * @return Objeto {@link Cliente} con sus datos y credenciales, o null si no se encuentra.
     * @throws PersistenciaException Si ocurre un error durante la ejecución de la consulta SQL.
     */
    Cliente consultarClientePorUsuario(String usuario) throws PersistenciaException;
}