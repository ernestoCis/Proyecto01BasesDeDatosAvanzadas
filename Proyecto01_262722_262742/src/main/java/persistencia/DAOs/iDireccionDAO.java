/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.Direccion;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author 
 */
public interface iDireccionDAO {
    /**
     * metodo que guarda una direccion en la BD
     * @param direccion direccion a insertar
     * @return direccion insertada
     * @throws PersistenciaException excpecion por si falla el sql
     */
    public Direccion insertarDireccion(Direccion direccion) throws PersistenciaException;
    
    
    /**
     * metodo que consulta una direccion por cliente
     * @param int id del usuario 
     * @return direccion consultada
     * @throws PersistenciaException 
     */
    public Direccion consultarDireccionPorCliente(int idCliente) throws PersistenciaException;
}
