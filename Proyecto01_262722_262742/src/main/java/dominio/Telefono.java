/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author Isaac
 */
public class Telefono {
    private int id;
    private String telefono;
    private String etiqueta;
    private Cliente cliente;

    public Telefono() {
    }

    public Telefono(int id, String telefono, String etiqueta, Cliente cliente) {
        this.id = id;
        this.telefono = telefono;
        this.etiqueta = etiqueta;
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
}
