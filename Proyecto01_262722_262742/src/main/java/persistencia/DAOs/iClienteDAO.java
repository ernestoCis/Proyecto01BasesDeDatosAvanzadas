/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.Cliente;
import dominio.Telefono;
import java.util.List;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Interfaz para el Data Access Object (DAO) de Clientes.</b>
 * <p>Define el contrato para las operaciones de persistencia relacionadas con la entidad Cliente, 
 * incluyendo la gestión de su información personal y sus teléfonos asociados.</p>
 *
 * @author 262722
 * @author 262742
 */
public interface iClienteDAO {
    
    /**
     * Consulta un cliente en la base de datos utilizando su nombre de usuario.
     * * @param usuario El nombre de usuario (credencial) del cliente a buscar.
     * @return El objeto {@link Cliente} poblado con los datos encontrados.
     * @throws PersistenciaException Si ocurre un error durante la consulta SQL o si no se encuentra el cliente.
     */
    public Cliente consultarCliente(String usuario) throws PersistenciaException;
    
    /**
     * Inserta un nuevo cliente en la tabla de clientes, junto con su información 
     * de contacto inicial (teléfono y dirección).
     * * @param cliente Objeto {@link Cliente} que contiene la información personal a insertar.
     * @param telefono Objeto {@link Telefono} principal asociado al cliente.
     * @return El {@link Cliente} insertado, con su ID autogenerado.
     * @throws PersistenciaException Si ocurre un error al ejecutar la transacción SQL.
     */
    public Cliente insertarCliente(Cliente cliente, Telefono telefono) throws PersistenciaException;
    
    /**
     * Actualiza los datos personales de un cliente existente en la base de datos.
     * * @param cliente Objeto {@link Cliente} con la información actualizada y su ID original.
     * @return El objeto {@link Cliente} tras ser actualizado.
     * @throws PersistenciaException Si ocurre un error de SQL o el cliente no existe.
     */
    public Cliente actualizarCliente(Cliente cliente) throws PersistenciaException;
    
    /**
     * Inserta una lista de teléfonos asociados a un cliente específico en la tabla de teléfonos.
     * * @param idCliente ID del cliente al que se le agregarán los teléfonos.
     * @param telefonos Lista de objetos {@link Telefono} a insertar.
     * @return La lista de teléfonos insertados con sus respectivos IDs autogenerados.
     * @throws PersistenciaException Si falla la inserción en la base de datos.
     */
    public List<Telefono> insertarTelefonos(int idCliente, List<Telefono> telefonos) throws PersistenciaException;
}