/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Cliente;
import dominio.EstadoCliente;
import dominio.Telefono;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import negocio.util.PasswordUtil;
import persistencia.DAOs.iClienteDAO;
import persistencia.Excepciones.PersistenciaException;

/**
 * <b>Objeto de Negocio (BO) para la entidad Cliente.</b>
 * <p>Esta clase centraliza las reglas de negocio, validaciones de formato y 
 * lógica de seguridad antes de permitir que los datos lleguen a la capa de persistencia.
 * Incluye validaciones de expresiones regulares (RegEx) para nombres, teléfonos 
 * y direcciones, así como el cifrado de contraseñas mediante BCrypt.</p>
 * * @author 262722
 * @author 262742
 */
public class ClienteBO implements iClienteBO{
    
    //DAO comun
    private iClienteDAO clienteDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());
    
    /**
     * Constructor que inyecta el DAO de cliente necesario para las operaciones.
     * @param clienteDAO Interfaz del DAO de persistencia.
     */
    public ClienteBO(iClienteDAO clienteDAO){
        this.clienteDAO = clienteDAO;
    }
    

    /**
     * Registra un nuevo cliente validando exhaustivamente sus datos.
     * <p>Realiza las siguientes comprobaciones:</p>
     * <ul>
     * <li>Obligatoriedad de campos (usuario, contraseña, nombres).</li>
     * <li>Formatos de RegEx para nombres y apellidos.</li>
     * <li>Rango de edad coherente (no futuro y máximo 120 años).</li>
     * <li>Verificación de usuario duplicado en el sistema.</li>
     * <li>Hasheo de la contraseña antes de guardar.</li>
     * </ul>
     * @param cliente Objeto con los datos del nuevo cliente.
     * @param telefono Teléfono inicial de contacto.
     * @return El cliente registrado con su ID asignado.
     * @throws NegocioException Si alguna validación de formato o regla de negocio falla.
     */
    @Override
    public Cliente registrarCliente(Cliente cliente, Telefono telefono) throws NegocioException {
        try {
            if (cliente == null) {
                throw new NegocioException("El cliente es obligatorio");
            }

            if (cliente.getNombres() == null || cliente.getNombres().trim().isEmpty()) {
                throw new NegocioException("El nombre es obligatorio");
            }

            if (cliente.getUsuario() == null || cliente.getUsuario().trim().isEmpty()) {
                throw new NegocioException("El usuario es obligatorio");
            }
            
            if(cliente.getContrasenia() == null || cliente.getContrasenia().trim().isEmpty()){
                throw new NegocioException("Formato de contraseña invalido");
            }
            
            String regexTelefono = "^\\+?[0-9]{10,15}$";
            if(telefono.getTelefono() == null && !telefono.getTelefono().matches(regexTelefono)){
                throw new NegocioException("Formato de telefono invalido");
            }
            
            String regexNombres = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$";
            if(!cliente.getNombres().matches(regexNombres)){
                throw new NegocioException("Formato de nombre invalido");
            }
            
            String regexApellido = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$";
            if(!cliente.getApellidoPaterno().matches(regexApellido)){
                throw new NegocioException("Formato de apellido paterno invalido");
            }
            
            String apellidoMaterno = cliente.getApellidoMaterno();

            if (apellidoMaterno != null) {
                apellidoMaterno = apellidoMaterno.trim();
                if (!apellidoMaterno.isEmpty()) {
                    if (!apellidoMaterno.matches(regexApellido)) {
                        throw new NegocioException("Formato de apellido materno invalido");
                    }
                } else {
                    cliente.setApellidoMaterno(null);
                }
            }
            
            if(cliente.getFechaNacimiento() == null){
                throw new NegocioException("La fecha de nacimiento no puede estar vacia");
            }
            
            LocalDate hoy = LocalDate.now();
            
            if (cliente.getFechaNacimiento().isAfter(hoy) || cliente.getFechaNacimiento().isEqual(hoy)) {
                throw new NegocioException("Fecha de nacimiento invalida");
            }
            
            LocalDate limiteAntiguedad = hoy.minusYears(120);
            if (cliente.getFechaNacimiento().isBefore(limiteAntiguedad)) {
                throw new NegocioException("Fecha de nacimiento invalida");
            }
            
            String regexCalleColonia = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s.,-]{2,100}$";
            if(!cliente.getDireccion().getCalle().matches(regexCalleColonia)){
                throw new NegocioException("Formato de calle invalido");
            }
            
            String regexNumero = "^[0-9]+$";
            if(!String.valueOf(cliente.getDireccion().getNumero()).matches(regexNumero)){
                throw new NegocioException("Formato del numero de casa invalido");
            }
            
            String regexCP = "^[0-9]{5}$";
            if(!String.valueOf(cliente.getDireccion().getCp()).matches(regexCP)){
                throw new NegocioException("Formato del codigo postal invalido");
            }

            // Evitar duplicados por correo
            Cliente existente = clienteDAO.consultarCliente(cliente.getUsuario().trim());
            if (existente != null) {
                throw new NegocioException("Ya existe un cliente con ese usuario.");
            }
            
            // hasheo de la contraseña
            String hash = PasswordUtil.hash(cliente.getContrasenia());
            cliente.setContrasenia(hash);

            return clienteDAO.insertarCliente(cliente, telefono);

        } catch (PersistenciaException ex) {
            LOG.warning("No se pudo registrar al cliente. " + ex);
            throw new NegocioException("Error al registrar al cliente: " + ex.getMessage(), ex);
        }
    }

    /**
     * Autentica a un cliente en el sistema.
     * <p>Busca al usuario por su identificador y verifica que la contraseña 
     * proporcionada coincida con el hash almacenado en la base de datos.</p>
     * @param usuario Nombre de usuario.
     * @param contrasenia Contraseña en texto plano.
     * @return El objeto Cliente si las credenciales son válidas.
     * @throws NegocioException Si el usuario no existe o la contraseña es incorrecta.
     */
    @Override
    public Cliente consultarCliente(String usuario, String contrasenia) throws NegocioException {
        try {
            if(usuario == null || usuario.trim().isEmpty()){
                throw new NegocioException("El usuario es obligatorio");
            }
            
            if (contrasenia == null || contrasenia.trim().isEmpty()) {
                throw new NegocioException("La contraseña es obligatoria");
            }

            Cliente cliente = clienteDAO.consultarCliente(usuario.trim());
            
            if(cliente == null){
                throw new NegocioException("Usuario o contraseña incorrectos");
            }

            if(!PasswordUtil.verificar(contrasenia, cliente.getContrasenia())){
                throw new NegocioException("Usuario o contraseña incorrectos");
            }
            
            return cliente;

        } catch (PersistenciaException ex) {
            LOG.warning("Error al consultar al cliente. " + ex);
            throw new NegocioException("Error al consultar cliente. " + ex.getMessage(), ex);
        }
    }

    /**
     * Actualiza la información de un cliente existente.
     * <p>Valida los nuevos datos y gestiona el cambio de contraseña de forma inteligente: 
     * si la contraseña no se modifica, conserva la anterior; si se recibe una nueva, 
     * la hashea (evitando re-hashear si ya viene en formato BCrypt).</p>
     * @param cliente Cliente con datos actualizados.
     * @return El cliente actualizado.
     * @throws NegocioException Si falla alguna validación de los datos modificados.
     */
    @Override
    public Cliente actualizarCliente(Cliente cliente) throws NegocioException {
        
        if (cliente == null) {
                throw new NegocioException("El cliente es obligatorio");
            }

            if (cliente.getNombres() == null || cliente.getNombres().trim().isEmpty()) {
                throw new NegocioException("El nombre es obligatorio");
            }

            if (cliente.getUsuario() == null || cliente.getUsuario().trim().isEmpty()) {
                throw new NegocioException("El usuario es obligatorio");
            }
            
            if(cliente.getContrasenia() == null || cliente.getContrasenia().trim().isEmpty()){
                throw new NegocioException("Formato de contraseña invalido");
            }
            
            String regexNombres = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$";
            if(!cliente.getNombres().matches(regexNombres)){
                throw new NegocioException("Formato de nombre invalido");
            }
            
            String regexApellido = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,50}$";
            if(!cliente.getApellidoPaterno().matches(regexApellido)){
                throw new NegocioException("Formato de apellido paterno invalido");
            }
            
            String apellidoMaterno = cliente.getApellidoMaterno();
            
            if (apellidoMaterno != null) {
                apellidoMaterno = apellidoMaterno.trim();
                if (!apellidoMaterno.isEmpty()) {
                    if (!apellidoMaterno.matches(regexApellido)) {
                        throw new NegocioException("Formato de apellido materno invalido");
                    }
                } else {
                    cliente.setApellidoMaterno(null);
                }
            }
            
            if(cliente.getFechaNacimiento() == null){
                throw new NegocioException("La fecha de nacimiento no puede estar vacia");
            }
            
            LocalDate hoy = LocalDate.now();
            
            if (cliente.getFechaNacimiento().isAfter(hoy) || cliente.getFechaNacimiento().isEqual(hoy)) {
                throw new NegocioException("Fecha de nacimiento invalida");
            }
            
            LocalDate limiteAntiguedad = hoy.minusYears(120);
            if (cliente.getFechaNacimiento().isBefore(limiteAntiguedad)) {
                throw new NegocioException("Fecha de nacimiento invalida");
            }
            
            if(cliente.getFechaNacimiento() == null){
                throw new NegocioException("La fecha de nacimiento no puede estar vacia");
            }
            
            String regexCalleColonia = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s.,-]{2,100}$";
            if(!cliente.getDireccion().getCalle().matches(regexCalleColonia)){
                throw new NegocioException("Formato de calle invalido");
            }
            
            String regexNumero = "^[0-9]+$";
            if(!String.valueOf(cliente.getDireccion().getNumero()).matches(regexNumero)){
                throw new NegocioException("Formato del numero de casa invalido");
            }
            
            String regexCP = "^[0-9]{5}$";
            if(!String.valueOf(cliente.getDireccion().getCp()).matches(regexCP)){
                throw new NegocioException("Formato del codigo postal invalido");
            }
            
            
        
        try{
            Cliente actualBD = clienteDAO.consultarCliente(cliente.getUsuario());
            
            cliente.setId(actualBD.getId());
            
            if (cliente == null) {
                throw new NegocioException("Cliente inválido.");
            }
            

            if (isBlank(cliente.getUsuario())) {
                throw new NegocioException("El usuario no puede estar vacío.");
            }
            if (isBlank(cliente.getNombres())) {
                throw new NegocioException("El nombre no puede estar vacío.");
            }
            if (isBlank(cliente.getApellidoPaterno())) {
                throw new NegocioException("El apellido paterno no puede estar vacío.");
            }
            if (cliente.getFechaNacimiento() == null) {
                throw new NegocioException("La fecha de nacimiento es obligatoria.");
            }

            if (cliente.getFechaNacimiento().isAfter(LocalDate.now())) {
                throw new NegocioException("La fecha de nacimiento no puede ser futura.");
            }

            // Si la contraseña viene null o vacía => NO actualizar
            if (cliente.getContrasenia() == null || cliente.getContrasenia().trim().isEmpty()) {
                cliente.setContrasenia(actualBD.getContrasenia());
            } else {
                // Si ya parece BCrypt (empieza con $2a$, $2b$, $2y$) => NO hashear otra vez
                String c = cliente.getContrasenia();
                if (c.startsWith("$2a$") || c.startsWith("$2b$") || c.startsWith("$2y$")) {
                    // ya es hash, no tocar
                } else {
                    cliente.setContrasenia(PasswordUtil.hash(c));
                }
            }
            
            if(cliente.getEstado() == EstadoCliente.Activo){
                cliente.setEstado(EstadoCliente.Activo);
            }else{
                cliente.setEstado(EstadoCliente.Inactivo);
            }
            
            
            try {
                return clienteDAO.actualizarCliente(cliente);
            } catch (PersistenciaException ex) {
                LOG.warning("No se pudo actualizar el cliente: " + ex.getMessage());
                throw new NegocioException("No se pudo actualizar el cliente. " + ex.getMessage(), ex);
            }
        }catch(PersistenciaException ex){
            throw new NegocioException(ex.getMessage(), ex);
        }
        
    }
    
    /**
     * Verifica si una cadena de texto es nula o solo contiene espacios en blanco.
     */
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
    
    /**
     * Permite agregar múltiples teléfonos a un cliente, validando sus formatos.
     * <p>Limpia la lista de nulos, valida que cumplan con el formato internacional 
     * de 10 a 15 dígitos y evita la duplicidad de números en el objeto local.</p>
     * @param cliente Cliente al que se le asocian los teléfonos.
     * @param nuevos Lista de nuevos objetos Telefono.
     * @return El cliente con la lista de teléfonos actualizada.
     * @throws NegocioException Si algún número de teléfono no cumple el formato.
     */
    @Override
    public Cliente agregarTelefonos(Cliente cliente, List<Telefono> nuevos) throws NegocioException {
        try {
            if (cliente == null) {
                throw new NegocioException("Cliente no puede ser null.");
            }
            if (cliente.getId() <= 0) {
                throw new NegocioException("Cliente inválido (sin id).");
            }

            if (nuevos == null || nuevos.isEmpty()) {
                return cliente; // no hay nada que agregar
            }

            // Validar y limpiar
            List<Telefono> filtrados = new ArrayList<>();
            for (Telefono t : nuevos) {
                if (t == null) {
                    continue;
                }
                
                String num = t.getTelefono() == null ? "" : t.getTelefono().trim();
                if (num.isEmpty()) {
                    continue;
                }

                // Validación básica (ajústala a tu gusto)
                if (!num.matches("^\\+?[0-9]{10,15}$")) {
                    throw new NegocioException("Teléfono inválido: " + num);
                }

                t.setTelefono(num);
                if (t.getEtiqueta() != null && t.getEtiqueta().trim().isEmpty()) {
                    t.setEtiqueta(null);
                }

                t.setCliente(cliente);
                filtrados.add(t);
            }

            if (filtrados.isEmpty()) {
                return cliente;
            }

            // Insertar en BD
            List<Telefono> insertados = clienteDAO.insertarTelefonos(cliente.getId(), filtrados);

            // Actualizar lista en memoria
            List<Telefono> lista = cliente.getTelefonos();
            if (lista == null) {
                lista = new java.util.ArrayList<>();
            }

            // (opcional) evitar duplicados por número
            for (Telefono ins : insertados) {
                boolean existe = false;
                for (Telefono ya : lista) {
                    if (ya != null && ya.getTelefono() != null
                            && ya.getTelefono().trim().equals(ins.getTelefono().trim())) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    lista.add(ins);
                }
            }

            cliente.setTelefonos(lista);
            return cliente;

        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudieron agregar los teléfonos.", e);
        }
    }
    
}