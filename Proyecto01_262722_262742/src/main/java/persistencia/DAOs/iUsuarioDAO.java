/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.Cliente;
import dominio.Empleado;
import persistencia.Excepciones.PersistenciaException;

public interface iUsuarioDAO {

    Empleado consultarEmpleadoPorUsuario(String usuario) throws PersistenciaException;

    Cliente consultarClientePorUsuario(String usuario) throws PersistenciaException;
}