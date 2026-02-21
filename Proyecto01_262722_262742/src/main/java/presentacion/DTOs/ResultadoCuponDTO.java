/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion.DTOs;

/**
 *
 * @author jesus y isaac
 */
public class ResultadoCuponDTO {
    private final boolean valido;
    private final String mensaje;
    private final float descuento;
    
    public ResultadoCuponDTO(boolean valido, String mensaje, float descuento){
        this.valido = valido;
        this.mensaje = mensaje;
        this.descuento = descuento;
    }

    public boolean esValido() {
        return valido;
    }

    public String getMensaje() {
        return mensaje;
    }

    public float getDescuento() {
        return descuento;
    }
    
    
}
