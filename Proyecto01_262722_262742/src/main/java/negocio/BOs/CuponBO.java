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
 * <b>Objeto de Negocio (BO) para la gestión de Cupones.</b>
 * <p>Esta clase actúa como intermediaria entre la presentación y la persistencia, 
 * encargándose de validar la existencia de códigos promocionales y calcular 
 * los montos de descuento aplicables a los pedidos.</p>
 * * @author 262722
 * @author 262742
 */
public class CuponBO implements iCuponBO{
    
    //DAO comun
    private iCuponDAO cuponDAO;
    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());
    
    /**
     * Constructor que inicializa el BO con su respectivo DAO.
     * @param cupon Implementación de la interfaz del DAO de cupones.
     */
    public CuponBO(iCuponDAO cupon){
        this.cuponDAO = cupon; // asignamos valor al DAO
    }

    /**
     * Consulta la información completa de un cupón por su nombre.
     * @param nombreCupon El código o nombre identificador del cupón.
     * @return El objeto <code>Cupon</code> recuperado de la base de datos.
     * @throws NegocioException Si el cupón no existe o ocurre un error en la capa de persistencia.
     */
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

    /**
     * Valida si un cupón es aplicable y calcula el descuento en moneda nacional.
     * <p>Este método verifica:</p>
     * <ul>
     * <li>Que el nombre del cupón no sea nulo o vacío.</li>
     * <li>Que el subtotal del pedido sea mayor a cero.</li>
     * <li>La existencia del cupón en el sistema.</li>
     * </ul>
     * <p>El cálculo del descuento se realiza mediante la fórmula: $descuento = subtotal \times (porcentaje / 100)$</p>
     * * @param nombreCupon Código del cupón a validar.
     * @param subtotal Monto actual de la venta antes del descuento.
     * @return Un objeto <code>ResultadoCuponDTO</code> indicando si es válido, un mensaje descriptivo y el monto a descontar.
     * @throws NegocioException Si los parámetros de entrada son inválidos o falla la conexión.
     */
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
        
        float descuento;
        descuento = subtotal * (cupon.getDescuento()/100f);
        
        //para el mejor de los casos
        return new ResultadoCuponDTO(true, "Cupon valido", descuento);
    }

    
}