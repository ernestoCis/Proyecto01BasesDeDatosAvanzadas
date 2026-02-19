/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import java.time.LocalDateTime;

/**
 *
 * @author Isaac
 */
public class PedidoProgramado extends Pedido {

    private Cupon cupon;

    public PedidoProgramado() {
    }

    public PedidoProgramado(int id, EstadoPedido estado, LocalDateTime fechaCreacion, LocalDateTime fechaEntrega, MetodoPago metodoPago, float total, int numeroPedido, Cliente cliente, Cupon cupon) {
        super(id, estado, fechaCreacion, fechaEntrega, metodoPago, total, numeroPedido, cliente);
        this.cupon = cupon;
    }

    public Cupon getCupon() {
        return cupon;
    }

    public void setCupon(Cupon cupon) {
        this.cupon = cupon;
    }
}
