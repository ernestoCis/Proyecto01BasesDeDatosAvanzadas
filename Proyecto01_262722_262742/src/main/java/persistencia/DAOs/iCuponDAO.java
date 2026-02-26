/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.Cupon;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Interfaz para el Data Access Object (DAO) de Cupones.</b>
 * <p>Define el contrato para las operaciones de persistencia relacionadas con 
 * los cupones de descuento, permitiendo su consulta y la actualización de 
 * sus estadísticas de uso.</p>
 *
 * @author 262722
 * @author 262742
 */
public interface iCuponDAO {
    
    /**
     * Consulta un cupón en la base de datos a partir de su nombre o código identificador.
     * * @param nombreCupon El nombre o código exacto del cupón a consultar.
     * @return El objeto {@link Cupon} poblado con los datos encontrados, o null si no existe.
     * @throws PersistenciaException Si ocurre un error al ejecutar la consulta SQL.
     */
    public Cupon consultarCupon(String nombreCupon) throws PersistenciaException;
    
    /**
     * Incrementa en 1 el contador de usos de un cupón específico en la base de datos.
     * <p>Este método debe llamarse una vez que un cupón ha sido aplicado exitosamente 
     * a un pedido para mantener el control del límite de usos.</p>
     * * @param idCupon El ID único del cupón cuyo número de usos se va a incrementar.
     * @throws PersistenciaException Si ocurre un error de SQL durante la actualización.
     */
    public void incrementarUsoCupon(int idCupon) throws PersistenciaException;
}