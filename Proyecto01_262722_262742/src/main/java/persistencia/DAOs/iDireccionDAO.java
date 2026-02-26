/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.Direccion;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Interfaz para el Data Access Object (DAO) de Direcciones.</b>
 * <p>Define el contrato para las operaciones de persistencia relacionadas con las 
 * direcciones físicas asociadas a los clientes dentro de la base de datos.</p>
 *
 * @author 262722
 * @author 262742
 */
public interface iDireccionDAO {
    
    /**
     * Inserta una nueva dirección en la base de datos.
     * * @param direccion Objeto {@link Direccion} que contiene la información geográfica a persistir.
     * @return El objeto {@link Direccion} insertado, con su ID autogenerado asignado.
     * @throws PersistenciaException Si ocurre un error al ejecutar la inserción SQL.
     */
    public Direccion insertarDireccion(Direccion direccion) throws PersistenciaException;
    
    /**
     * Consulta y recupera la dirección asociada a un cliente específico en la base de datos.
     * * @param idCliente El ID único del cliente del cual se desea consultar la dirección.
     * @return El objeto {@link Direccion} correspondiente al cliente, o null si no se encuentra.
     * @throws PersistenciaException Si ocurre un error de SQL durante la consulta.
     */
    public Direccion consultarDireccionPorCliente(int idCliente) throws PersistenciaException;
}