/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.Direccion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.Conexion.iConexionBD;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Data Access Object (DAO) para las Direcciones.</b>
 * <p>Gestiona las operaciones de persistencia relacionadas con las direcciones 
 * de los clientes, permitiendo su inserción en la base de datos y su 
 * posterior consulta vinculada al identificador del cliente.</p>
 *
 * @author 262722
 * @author 262742
 */
public class DireccionDAO implements iDireccionDAO {
    
    private final iConexionBD conexionBD;
    
    /**
     * Logger para registrar información relevante y errores durante operaciones de persistencia.
     */
    private static final Logger LOG = Logger.getLogger(DireccionDAO.class.getName());
    
    /**
     * Constructor que inyecta la dependencia de la conexión a la base de datos.
     * @param conexionBD Implementación de la interfaz de conexión.
     */
    public DireccionDAO(iConexionBD conexionBD){
        this.conexionBD = conexionBD;
    }

    /**
     * Inserta una nueva dirección asociada a un cliente en la base de datos.
     * <p>Utiliza <code>Statement.RETURN_GENERATED_KEYS</code> para recuperar 
     * inmediatamente el ID generado por el motor de base de datos y asignarlo 
     * a la entidad devuelta.</p>
     *
     * @param direccion Objeto {@link Direccion} con los datos a persistir (debe contener el cliente asociado).
     * @return El objeto <code>Direccion</code> registrado, actualizado con su nuevo ID.
     * @throws PersistenciaException Si la inserción falla o ocurre un error de SQL.
     */
    @Override
    public Direccion insertarDireccion(Direccion direccion) throws PersistenciaException {
        String comandoSQL = """
                            INSERT INTO Direcciones(id_cliente, calle, colonia, cp, numero)
                            VALUES (?, ?, ?, ?, ?)
                            """;

        try (Connection conn = conexionBD.crearConexion(); 
             PreparedStatement ps = conn.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, direccion.getCliente().getId());
            ps.setString(2, direccion.getCalle());
            ps.setString(3, direccion.getColonia());
            ps.setInt(4, direccion.getCp());
            ps.setInt(5, direccion.getNumero());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se pudo insertar la dirección.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    direccion.setId(rs.getInt(1));
                }
            }

            return direccion;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al insertar dirección", ex);
            throw new PersistenciaException("Error al insertar dirección", ex);
        }
    }

    /**
     * Consulta y recupera la dirección principal de un cliente registrado.
     * <p>Utiliza la cláusula <code>LIMIT 1</code> para asegurar que solo se recupere 
     * un único registro en caso de que existiera más de uno asociado al mismo cliente.</p>
     *
     * @param idCliente Identificador único del cliente.
     * @return Un objeto {@link Direccion} con los datos encontrados, o <code>null</code> si no existe.
     * @throws PersistenciaException Si ocurre un error de SQL durante la consulta.
     */
    @Override
    public Direccion consultarDireccionPorCliente(int idCliente) throws PersistenciaException {
        String comandoSQL = """
                            SELECT id, calle, colonia, cp, numero
                            FROM direcciones
                            WHERE id_cliente = ?
                            LIMIT 1
                            """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                Direccion direccion = new Direccion();
                direccion.setId(rs.getInt("id"));
                direccion.setCalle(rs.getString("calle"));
                direccion.setColonia(rs.getString("colonia"));
                direccion.setCp(rs.getInt("cp"));
                direccion.setNumero(rs.getInt("numero"));

                return direccion;
            }

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al consultar dirección", ex);
            throw new PersistenciaException("Error al consultar dirección", ex);
        }
    }
}
