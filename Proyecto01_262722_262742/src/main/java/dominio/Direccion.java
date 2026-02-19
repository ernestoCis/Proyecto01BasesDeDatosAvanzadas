/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author Isaac
 */
public class Direccion {

    private int id;
    private String calle;
    private String colonia;
    private int cp;
    private int numero;
    private Cliente cliente;

    public Direccion() {
    }

    public Direccion(int id, String calle, String colonia, int cp, int numero, Cliente cliente) {
        this.id = id;
        this.calle = calle;
        this.colonia = colonia;
        this.cp = cp;
        this.numero = numero;
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public String getCalle() {
        return calle;
    }

    public String getColonia() {
        return colonia;
    }

    public int getCp() {
        return cp;
    }

    public int getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}
