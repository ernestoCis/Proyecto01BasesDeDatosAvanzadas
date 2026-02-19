/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author Isaac
 */
public class DetallePedido {

    private int id;
    private String nota;      
    private int cantidad;
    private float precio;
    private float subtotal;
    private Pedido pedido;
    private Producto producto;

    public DetallePedido() {
    }

    public DetallePedido(int id, String nota, int cantidad, float precio, float subtotal, Pedido pedido, Producto producto) {
        this.id = id;
        this.nota = nota;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = subtotal;
        this.pedido = pedido;
        this.producto = producto;
    }

    public int getId() {
        return id;
    }

    public String getNota() {
        return nota;
    }

    public int getCantidad() {
        return cantidad;
    }

    public float getPrecio() {
        return precio;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
