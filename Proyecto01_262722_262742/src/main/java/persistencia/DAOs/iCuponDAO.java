/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.Cupon;
import persistencia.Excepciones.PersistenciaException;

/**
 *
 * @author jesus y isaac
 */
public interface iCuponDAO {
    /**
     * metodo que consulta un cupon en la BD
     * @param nombreCupon nombre del cupon a consultar
     * @return cupon consultado
     * @throws PersistenciaException excepcion por si falla el SQL
     */
    public Cupon consultarCupon(String nombreCupon) throws PersistenciaException;
    
    /**
     * metodo que aumenta 1 por cada uso de un cupon
     * @param idCupon id del cupon a incrementar su numero de usos
     * @throws PersistenciaException excepcion por si el sql falla
     */
    public void incrementarUsoCupon(int idCupon) throws PersistenciaException;
}
