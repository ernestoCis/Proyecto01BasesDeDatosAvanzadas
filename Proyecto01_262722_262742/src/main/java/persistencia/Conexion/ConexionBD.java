/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <b>Implementación de la conexión a la base de datos.</b>
 * <p>Esta clase se encarga de gestionar los parámetros de acceso y la creación 
 * de instancias de conexión mediante JDBC para la base de datos de la Panadería.</p>
 *
 * @author 262722
 * @author 262742
 */
public class ConexionBD implements iConexionBD {
    
    /**
     * Cadena de conexión JDBC que apunta al servidor local y a la base de datos 'Panaderia'.
     */
    private final String CADENA_CONEXION = "jdbc:mysql://localhost:3306/Panaderia";

    /**
     * Identificador de usuario para la autenticación en el servidor de base de datos.
     */
    private final String USUARIO = "root";

    /**
     * Credencial de acceso asociada al usuario del sistema gestor de base de datos.
     */
    private final String CONTRASENIA = "Gerh@rdus2006";
    
    /**
     * Establece y retorna una conexión activa con el servidor MySQL.
     * <p>Utiliza el {@link DriverManager} para negociar la conexión basándose en 
     * la URL, el usuario y la contraseña configurados en la clase.</p>
     *
     * @return Una instancia de {@link Connection} lista para ejecutar sentencias SQL.
     * @throws SQLException Si ocurre un error de red, credenciales incorrectas o 
     * el servidor no está disponible.
     */
    @Override
    public Connection crearConexion() throws SQLException {
        return DriverManager.getConnection(CADENA_CONEXION, USUARIO, CONTRASENIA);
    }
}