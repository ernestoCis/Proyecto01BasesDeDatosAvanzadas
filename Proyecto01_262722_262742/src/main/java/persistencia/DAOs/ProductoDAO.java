/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOs;

import dominio.EstadoProducto;
import dominio.Producto;
import dominio.TipoProducto;
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
 * <b>Data Access Object (DAO) para la gestión de Productos.</b>
 * <p>Esta clase maneja la persistencia de la entidad Producto en la base de datos,
 * permitiendo realizar operaciones CRUD (Crear, Leer, Actualizar, Listar).</p>
 *
 * @author 262722, 262742
 */
public class ProductoDAO implements iProductoDAO {

    /**
     * Componente encargado de crear conexiones con la base de datos. Se inyecta
     * por constructor para reducir acoplamiento y facilitar pruebas.
     */
    private final iConexionBD conexionBD;

    /**
     * Logger para registrar información relevante y errores durante operaciones de
     * persistencia.
     */
    private static final Logger LOG = Logger.getLogger(ProductoDAO.class.getName());

    /**
     * Constructor que inicializa la dependencia de conexión.
     *
     * @param conexionBD Objeto que gestiona la creación de conexiones a la base de datos.
     */
    public ProductoDAO(iConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Consulta un producto específico en la base de datos mediante su ID.
     *
     * @param producto Objeto {@link Producto} que contiene el ID a buscar.
     * @return El {@link Producto} recuperado con todos sus datos poblados.
     * @throws PersistenciaException Si el producto no existe o si ocurre un error de SQL.
     */
    @Override
    public Producto consultarProducto(Producto producto) throws PersistenciaException {
        String comandoSQL = """
                            SELECT id, nombre, tipo, precio, estado, descripcion FROM Productos
                            WHERE id = ?
                            """;

        try (Connection conn = this.conexionBD.crearConexion();) {

            Producto p;
            try (PreparedStatement ps = conn.prepareStatement(comandoSQL)) {
                ps.setInt(1, producto.getId());

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        LOG.log(Level.WARNING, "No se encontró el producto con id {0}", producto.getId());
                        throw new PersistenciaException("No existe el producto con el ID proporcionado.");
                    }

                    p = new Producto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            TipoProducto.valueOf(rs.getString("tipo")),
                            rs.getFloat("precio"),
                            EstadoProducto.valueOf(rs.getString("estado").replace(" ", "_")),
                            rs.getString("descripcion")
                    );
                }
            }

            return p;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error de SQL al consultar el producto", ex);
            throw new PersistenciaException(ex.getMessage());
        }
    }

    /**
     * Inserta un nuevo producto en la base de datos.
     *
     * @param producto Objeto {@link Producto} a persistir.
     * @return El mismo objeto producto, pero con su ID autogenerado asignado.
     * @throws PersistenciaException Si la inserción falla o no se puede obtener el ID generado.
     */
    @Override
    public Producto insertarProducto(Producto producto) throws PersistenciaException {
        String comandoSQL = """
                            INSERT INTO Productos(nombre, tipo, precio, estado, descripcion) 
                            VALUES(?,?,?,?,?)
                            """;

        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, String.valueOf(producto.getTipo()));
            ps.setFloat(3, producto.getPrecio());
            ps.setString(4, producto.getEstado().name().replace("_", " "));
            ps.setString(5, producto.getDescripcion());

            int filasInsertadas = ps.executeUpdate();

            if (filasInsertadas == 0) {
                LOG.log(Level.WARNING, "No se pudo insertar el producto: {0}", producto);
                throw new PersistenciaException("No se pudo insertar el producto");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    producto.setId(rs.getInt(1));
                } else {
                    throw new PersistenciaException("Error al obtener el ID generado del nuevo producto.");
                }
            }

            LOG.log(Level.INFO, "Producto insertado con éxito. ID: {0}", producto.getId());
            return producto;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error de SQL al insertar el producto", ex);
            throw new PersistenciaException(ex.getMessage());
        }

    }

    /**
     * Actualiza la información de un producto existente en la base de datos.
     *
     * @param producto Objeto {@link Producto} con los datos actualizados y el ID original.
     * @return El mismo objeto producto tras ser actualizado.
     * @throws PersistenciaException Si el ID no existe en la BD o falla la actualización.
     */
    @Override
    public Producto actualizarProducto(Producto producto) throws PersistenciaException {

        String comandoSQL = """
                            UPDATE Productos
                            SET nombre = ?, tipo = ?, precio = ?, estado = ?, descripcion = ?
                            WHERE id = ?
                            """;

        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getTipo().name());
            ps.setFloat(3, producto.getPrecio());
            ps.setString(4, producto.getEstado().name().replace("_", " "));
            ps.setString(5, producto.getDescripcion());
            ps.setInt(6, producto.getId());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se pudo actualizar: el ID proporcionado no existe.");
            }

            return producto;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error de SQL al actualizar producto", ex);
            throw new PersistenciaException(ex.getMessage());
        }
    }

    /**
     * Lista todos los productos que tienen el estado "Disponible".
     *
     * @return Una lista de objetos {@link Producto} disponibles en el sistema.
     * @throws PersistenciaException Si ocurre un error al procesar la consulta.
     */
    @Override
    public List<Producto> listarProductos() throws PersistenciaException {

        String comandoSQL = """
                            SELECT id, nombre, tipo, precio, estado, descripcion
                            FROM Productos WHERE estado = "Disponible"
                            """;

        List<Producto> productos = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Producto p = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        TipoProducto.valueOf(rs.getString("tipo")),
                        rs.getFloat("precio"),
                        EstadoProducto.valueOf(rs.getString("estado").replace(" ", "_")),
                        rs.getString("descripcion")
                );

                productos.add(p);
            }

            return productos;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error SQL al listar productos", ex);
            throw new PersistenciaException(ex.getMessage());
        }
    }

}