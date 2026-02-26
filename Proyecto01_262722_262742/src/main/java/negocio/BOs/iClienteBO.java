/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import dominio.Cliente;
import dominio.Telefono;
import java.util.List;
import negocio.Excepciones.NegocioException;

/**
 * <b>Interfaz para el Objeto de Negocio (BO) de Clientes.</b>
 * <p>Define el contrato de operaciones permitidas para la gestión de clientes 
 * dentro de la capa de negocio. Incluye registro, consulta por credenciales, 
 * actualización y el manejo de sus números telefónicos asociados.</p>
 * * @author 262722
 * @author 262742
 */
public interface iClienteBO {
    
    /**
     * Registra un nuevo cliente en el sistema junto con su teléfono principal.
     * <p>Debe validar las reglas de negocio, como la unicidad del nombre de usuario y 
     * el cifrado de la contraseña, antes de delegar la inserción al DAO correspondiente.</p>
     * * @param cliente Objeto con los datos personales y credenciales a registrar.
     * @param telefono Teléfono principal asociado al cliente.
     * @return El cliente registrado, incluyendo su ID generado por la base de datos.
     * @throws NegocioException Si no se cumplen las reglas de negocio o falla la persistencia.
     */
    public Cliente registrarCliente(Cliente cliente, Telefono telefono) throws NegocioException;
    
    /**
     * Verifica las credenciales de un cliente para el inicio de sesión.
     * * @param usuario Nombre de usuario del cliente registrado.
     * @param contrasenia Contraseña ingresada en texto plano para su verificación.
     * @return El objeto <code>Cliente</code> autenticado si las credenciales coinciden.
     * @throws NegocioException Si el usuario no existe, la contraseña es incorrecta o hay un error de conexión.
     */
    public Cliente consultarCliente(String usuario, String contrasenia) throws NegocioException;
    
    /**
     * Actualiza los datos personales de un cliente previamente registrado.
     * * @param cliente Objeto con la información actualizada del cliente.
     * @return El cliente tras ser actualizado en la base de datos.
     * @throws NegocioException Si los datos son inválidos, el cliente no existe o falla la persistencia.
     */
    public Cliente actualizarCliente(Cliente cliente) throws NegocioException;
    
    /**
     * Agrega una lista de nuevos teléfonos a un cliente existente.
     * * @param cliente Cliente al que se le vincularán los nuevos números.
     * @param telefonos Lista de objetos <code>Telefono</code> a registrar.
     * @return El cliente con su nueva lista de teléfonos asociada.
     * @throws NegocioException Si la lista está vacía, algún teléfono es inválido o falla la inserción.
     */
    public Cliente agregarTelefonos(Cliente cliente, List<Telefono> telefonos) throws NegocioException;
}