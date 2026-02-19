/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Isaac
 */
public abstract class Pedido {

    private int id;
    private EstadoPedido estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEntrega;
    private MetodoPago metodoPago;
    private float total;
    private int numeroPedido;
    private Cliente cliente;
    private List<DetallePedido> detalles;

    public Pedido() {
    }

    public Pedido(int id, EstadoPedido estado, LocalDateTime fechaCreacion, LocalDateTime fechaEntrega, MetodoPago metodoPago, float total, int numeroPedido, Cliente cliente) {
        this.id = id;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaEntrega = fechaEntrega;
        this.metodoPago = metodoPago;
        this.total = total;
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public float getTotal() {
        return total;
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

}
