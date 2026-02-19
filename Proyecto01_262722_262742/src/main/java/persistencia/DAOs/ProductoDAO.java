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
 *
 * @author jesus
 */
public class ProductoDAO implements iProductoDAO{
    
    /**
     * Componente encargado de crear conexiones con la base de datos. Se inyecta
     * por constructor para reducir acoplamiento y facilitar pruebas.
     */
    private final iConexionBD conexionBD;

    /**
     * Logger para registrar información relevante durante operaciones de
     * persistencia.
     */
    private static final Logger LOG = Logger.getLogger(ProductoDAO.class.getName());
    
    /**
     * Constructor que inicializa la dependencia de conexión.
     *
     * @param conexionBD objeto que gestiona la creación de conexiones a la base
     * de datos
     */
    public ProductoDAO(iConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * metodo que consulta un producto en la BD
     * @param producto producto a consultar
     * @return producto consultado
     * @throws PersistenciaException excepcion por si el SQL falla
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
                            TipoProducto.valueOf(rs.getString("tipo").toUpperCase()),
                            rs.getFloat("precio"),
                            EstadoProducto.valueOf(rs.getString("estado").toUpperCase()),
                            rs.getString("descripcion")
                    );
                }
            }

            return p;
            
        }catch(SQLException ex){
            LOG.log(Level.SEVERE, "Error de SQL al consultar el producto", ex);
            throw new PersistenciaException(ex.getMessage());
        }
    }

    @Override
    public Producto insertarProducto(Producto producto) throws PersistenciaException {
        String comandoSQL = """
                            INSERT INTO Productos(nombre, tipo, precio, estado, descripcion) 
                            VALUES(?,?,?,?,?)
                            """;
        
        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS)){
            
            ps.setString(1, producto.getNombre());
            ps.setString(2, String.valueOf(producto.getTipo())) ;
            ps.setFloat(3, producto.getPrecio());
            ps.setString(4, String.valueOf(producto.getEstado()));
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
            
        }catch(SQLException ex){
            LOG.log(Level.SEVERE, "Error de SQL al insertar el producto", ex);
            throw new PersistenciaException(ex.getMessage());
        }
        
    }

    @Override
    public Producto actualizarProducto(Producto producto) throws PersistenciaException {
        String comandoSQL = """
                            UPDATE Productos
                            SET nombres = ?, tipo = ?, precio = ?, estado = ?, descripcion = ?
                            WHERE id = ?
                            """;

        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, String.valueOf(producto.getTipo()));
            ps.setFloat(3, producto.getPrecio());
            ps.setString(4, String.valueOf(producto.getEstado()));
            ps.setString(5, producto.getDescripcion());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se pudo actualizar: el ID proporcionado no existe.");
            }

            return producto;

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error de SQL al actualizar técnico", ex);
            throw new PersistenciaException(ex.getMessage());
        }
    }
    
}
