/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.Telefono;
import java.util.List;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author
 */
public interface iTelefonoDAO {
    
    /**
     * metodo que inserta un telefono ocn etiqueta a la BD
     * @param telefono telefono a insertar
     * @return telefono insertado
     * @throws PersistenciaException excepcion por si falla sql
     */
    public Telefono insertarTelefono(Telefono telefono) throws PersistenciaException;

    /**
     * metodo que consulta los telefonos de un cliente
     * @param idCliente id del cliente
     * @return lista de telefonos por cliente
     * @throws PersistenciaException excepcion por si falla el sql
     */
    public List<Telefono> consultarTelefonosPorCliente(int idCliente) throws PersistenciaException;
    
}
