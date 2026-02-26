/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import dominio.Cupon;
import negocio.Excepciones.NegocioException;
import persistencia.DAOs.CuponDAO;
import presentacion.DTOs.ResultadoCuponDTO;

/**
 * <b>Interfaz para el Objeto de Negocio (BO) de Cupones.</b>
 * <p>Define el contrato para la consulta y validación de reglas de negocio 
 * aplicables a los cupones de descuento (vigencia, topes de uso y aplicación 
 * sobre el subtotal de un pedido).</p>
 * * @author jesus
 * @author 262722
 * @author 262742
 */
public interface iCuponBO {
    
    /**
     * Consulta la información de un cupón específico en la base de datos a partir de su nombre/código.
     * * @param nombreCupon Nombre o código identificador del cupón a consultar.
     * @return El objeto <code>Cupon</code> con sus datos correspondientes.
     * @throws NegocioException Si el cupón no existe o hay problemas de comunicación con la capa de persistencia.
     */
    public Cupon consultarCupon(String nombreCupon) throws NegocioException;
    
    /**
     * Evalúa un cupón aplicando las reglas de negocio pertinentes (ej. caducidad, límite de usos).
     * <p>Calcula el impacto del cupón sobre el subtotal proporcionado y encapsula 
     * el resultado en un objeto de transferencia de datos (DTO) para la capa de presentación.</p>
     * * @param nombreCupon Nombre o código del cupón a validar.
     * @param subtotal Monto base del pedido sobre el cual se aplicará el descuento.
     * @return Un objeto <code>ResultadoCuponDTO</code> que contiene el estado de la validación, 
     * el monto de descuento calculado y/o mensajes de error.
     * @throws NegocioException Si ocurre un error durante el procesamiento de las reglas de negocio.
     */
    public ResultadoCuponDTO validarCupon(String nombreCupon, float subtotal) throws NegocioException;
}
