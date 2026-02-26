/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.Telefono;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.Conexion.iConexionBD;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Data Access Object (DAO) para la gestión de Teléfonos.</b>
 * <p>Esta clase maneja la persistencia de los números de teléfono asociados 
 * a los clientes en la base de datos.</p>
 *
 * @author 262722
 * @author 262742
 */
public class TelefonoDAO implements iTelefonoDAO {
    
    /**
     * Componente encargado de crear conexiones con la base de datos. Se inyecta
     * por constructor para reducir acoplamiento y facilitar pruebas.
     */
    private final iConexionBD conexionBD;
    
    /**
     * Logger para registrar información relevante y errores durante operaciones de
     * persistencia.
     */
    private static final Logger LOG = Logger.getLogger(TelefonoDAO.class.getName());
    
    /**
     * Constructor que inicializa la dependencia de conexión.
     * @param conexionBD Objeto que gestiona la creación de conexiones a la base de datos.
     */
    public TelefonoDAO(iConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Inserta un nuevo número de teléfono asociado a un cliente en la base de datos.
     * * @param telefono Objeto {@link Telefono} que contiene la información a persistir.
     * Debe tener un cliente asociado con un ID válido.
     * @return El mismo objeto teléfono con su ID autogenerado asignado.
     * @throws PersistenciaException Si ocurre un error al insertar en la base de datos.
     */
    @Override
    public Telefono insertarTelefono(Telefono telefono) throws PersistenciaException {
        String comandoSQL = """
                    INSERT INTO telefonos(id_cliente, telefono, etiqueta)
                    VALUES (?, ?, ?)
                    """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, telefono.getCliente().getId());
            ps.setString(2, telefono.getTelefono());
            ps.setString(3, telefono.getEtiqueta());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se pudo insertar el teléfono.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    telefono.setId(rs.getInt(1));
                }
            }

            return telefono;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE,"Error al insertar teléfono", ex);
            throw new PersistenciaException("Error al insertar teléfono", ex);
        }
    }

    /**
     * Consulta y recupera todos los teléfonos asociados a un cliente específico.
     * * @param idCliente Identificador del cliente.
     * @return Una lista de objetos {@link Telefono} pertenecientes al cliente.
     * @throws PersistenciaException Si ocurre un error de SQL durante la consulta.
     */
    @Override
    public List<Telefono> consultarTelefonosPorCliente(int idCliente) throws PersistenciaException {
        String comandoSQL = """
                        SELECT id, telefono, etiqueta
                        FROM Telefonos
                        WHERE id_cliente = ?
                        """;

        List<Telefono> telefonos = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Telefono telefono = new Telefono();
                    telefono.setId(rs.getInt("id"));
                    telefono.setTelefono(rs.getString("telefono"));
                    telefono.setEtiqueta(rs.getString("etiqueta"));

                    telefonos.add(telefono);
                }
            }

            return telefonos;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al consultar teléfonos", ex);
            throw new PersistenciaException("Error al consultar teléfonos", ex);
        }
    }
    
}