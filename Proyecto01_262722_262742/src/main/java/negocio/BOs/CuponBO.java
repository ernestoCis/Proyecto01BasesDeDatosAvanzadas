/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import dominio.Cupon;
import java.util.logging.Logger;
import negocio.Excepciones.NegocioException;
import persistencia.DAOs.iCuponDAO;
import persistencia.Excepciones.PersistenciaException;
import presentacion.DTOs.ResultadoCuponDTO;

/**
 *
 * @author
 */
public class CuponBO implements iCuponBO{
    
    //DAO comun
    private iCuponDAO cuponDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());
    
    public CuponBO(iCuponDAO cupon){
        this.cuponDAO = cupon; // asignamos valor al DAO
    }

    @Override
    public Cupon consultarCupon(String nombreCupon) throws NegocioException {
        try{
            //llamamos al DAO y consultamos el cupon
            Cupon cuponConsultado = this.cuponDAO.consultarCupon(nombreCupon);
            if(cuponConsultado == null){
                LOG.warning("No se pudó obtener el cupon con nombre: " + nombreCupon);
                throw new NegocioException("No se obtuvo ningun cupon con id: " + nombreCupon);
            }
            
            //existe el producto
            return cuponConsultado;
            
        }catch(PersistenciaException ex){
            LOG.warning("No se pudo obtener el cupon con id: " + nombreCupon + ". " + ex);
            throw new NegocioException("Problemas al intentar consultar el cupon con id: " + nombreCupon + ". " + ex.getMessage());
        }
    }


    @Override
    public ResultadoCuponDTO validarCupon(String nombreCupon, float subtotal) throws NegocioException {
        
        if(nombreCupon == null || nombreCupon.trim().isEmpty()){
            throw new NegocioException("El nombre del cupon es obligatorio");
        }
        
        if(subtotal <= 0.0f){
            throw new NegocioException("El subtotal no puede estar vacio o ser menor o igual a 0");
        }
        
        Cupon cupon = null;
        
        try {
            cupon = cuponDAO.consultarCupon(nombreCupon);
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
        
        if(cupon == null){
            return new ResultadoCuponDTO(false, "No existe el cupon", 0);
        }
        
        //meter reglas de negocio (fechas)
        
        float descuento;
        descuento = subtotal * (cupon.getDescuento()/100f);
        
        //para el mejor de los casos
        return new ResultadoCuponDTO(true, "Cupon valido", descuento);
    }

    
}
