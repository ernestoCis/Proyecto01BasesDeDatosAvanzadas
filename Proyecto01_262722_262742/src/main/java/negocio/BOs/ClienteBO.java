/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Cliente;
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
 *
 * @author
 */
public class ClienteBO implements iClienteBO{
    
    //DAO comun
    private iClienteDAO clienteDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());
    
    public ClienteBO(iClienteDAO clienteDAO){
        this.clienteDAO = clienteDAO;
    }
    

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
            
            if(cliente.getApellidoMaterno() != null || !cliente.getApellidoMaterno().trim().isEmpty()){
                if (!cliente.getApellidoMaterno().matches(regexApellido)) {
                    throw new NegocioException("Formato de apellido materno invalido");
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
            
            if(cliente.getApellidoMaterno() != null || !cliente.getApellidoMaterno().trim().isEmpty()){
                if (!cliente.getApellidoMaterno().matches(regexApellido)) {
                    throw new NegocioException("Formato de apellido materno invalido");
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
        
        try{
            Cliente actualBD = clienteDAO.consultarCliente(cliente.getUsuario());

            if (cliente == null) {
                throw new NegocioException("Cliente inválido.");
            }
            if (cliente.getId() <= 0) {
                throw new NegocioException("ID de cliente inválido.");
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
            
            try {
                return clienteDAO.actualizarCliente(cliente);
            } catch (PersistenciaException ex) {
                LOG.warning("No se pudo actualizar el cliente: " + ex.getMessage());
                throw new NegocioException("No se pudo actualizar el cliente.", ex);
            }
        }catch(PersistenciaException ex){
            throw new NegocioException(ex.getMessage(), ex);
        }
        
    }
    
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
    
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
