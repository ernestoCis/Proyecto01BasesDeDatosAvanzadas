/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author jesus
 */
public class ConexionBD implements iConexionBD{
    /**
     * Cadena de conexión utilizada para establecer comunicación con la base de
     * datos.
     */
    private final String CADENA_CONEXION = "jdbc:mysql://localhost:3306/Panaderia";

    /**
     * Usuario de la base de datos.
     */
    private final String USUARIO = "root";

    /**
     * Contraseña asociada al usuario de la base de datos.
     */
//    private final String CONTRASENIA = "Ernesto_0611";
    private final String CONTRASENIA = "Gerh@rdus2006";
    
    /**
     * Crea y retorna una conexión activa con la base de datos.
     *
     * Este método utiliza el {@link DriverManager} para establecer la conexión
     * con los parámetros definidos en esta clase.
     *
     * @return una {@link Connection} activa lista para ser utilizada por los
     * DAO
     * @throws SQLException si ocurre un error al intentar establecer la
     * conexión
     */
    @Override
    public Connection crearConexion() throws SQLException {
        return DriverManager.getConnection(CADENA_CONEXION, USUARIO, CONTRASENIA);
    }
}
