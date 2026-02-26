/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import dominio.Telefono;
import java.util.List;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Interfaz para el Data Access Object (DAO) de Teléfonos.</b>
 * <p>Define el contrato para la gestión de los números telefónicos de los clientes. 
 * Permite la persistencia de números individuales con etiquetas personalizadas y 
 * la recuperación de todos los contactos asociados a un cliente específico.</p>
 *
 * @author 262722
 * @author 262742
 */
public interface iTelefonoDAO {
    
    /**
     * Inserta un nuevo número de teléfono con su respectiva etiqueta (ej. Celular, Casa) 
     * en la base de datos.
     * * @param telefono Objeto {@link Telefono} que contiene el número, la etiqueta y la referencia al cliente.
     * @return El objeto {@link Telefono} persistido, incluyendo su ID generado.
     * @throws PersistenciaException Si ocurre un error durante la ejecución de la sentencia SQL.
     */
    public Telefono insertarTelefono(Telefono telefono) throws PersistenciaException;

    /**
     * Consulta y recupera la lista completa de teléfonos asociados a un cliente 
     * mediante su identificador único.
     * * @param idCliente Identificador del cliente en la base de datos.
     * @return Una {@link List} de objetos {@link Telefono} pertenecientes al cliente.
     * @throws PersistenciaException Si ocurre un error al consultar la base de datos.
     */
    public List<Telefono> consultarTelefonosPorCliente(int idCliente) throws PersistenciaException;
    
}
