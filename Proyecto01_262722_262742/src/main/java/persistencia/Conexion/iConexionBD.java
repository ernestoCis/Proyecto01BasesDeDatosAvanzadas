/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.Conexion;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <b>Interfaz para la gestión de conexiones a la base de datos.</b>
 * <p>Define el contrato que deben seguir las clases encargadas de establecer 
 * la comunicación con el motor de base de datos. Facilita la inyección de 
 * dependencias y el intercambio de proveedores de conexión (MySQL, PostgreSQL, etc.) 
 * sin afectar la lógica de los DAOs.</p>
 *
 * @author 262722
 * @author 262742
 */
public interface iConexionBD {
    
    /**
     * Crea y retorna una conexión activa con la base de datos.
     * <p>La implementación concreta de este método es responsable de definir los
     * parámetros de conexión necesarios (URL, usuario, contraseña) y establecer 
     * la comunicación con el motor de base de datos correspondiente.</p>
     *
     * @return Una instancia de {@link Connection} lista para ser utilizada.
     * @throws SQLException Si ocurre un error al intentar establecer la conexión.
     */
    Connection crearConexion() throws SQLException;
}