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
 *
 * @author jesus
 */
public interface iCuponBO {
    /**
     * metodo que consulta un cupon mediante el DAO
     * @param nombreCupon nombre del cupon a consultar
     * @return cupon consultado
     * @throws NegocioException excepcion por reglas de negocio o problemas con la capa de persistencia
     */
    public Cupon consultarCupon(String nombreCupon) throws NegocioException;
    
    /**
     * metodo para validar un cupon con reglas de negocio
     * @param nombreCupon nombre del cupon a validar
     * @return regresa true si el cupon es valido, false si no es valido
     * @throws NegocioException excepcion por reglas de negocio
     */
    public ResultadoCuponDTO validarCupon(String nombreCupon, float subtotal) throws NegocioException;
}
