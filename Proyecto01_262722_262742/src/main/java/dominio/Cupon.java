/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import java.time.LocalDate;

/**
 *
 * @author Isaac
 */
public class Cupon {
    
    private int id;
    private String nombre;
    private int descuento; 
    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;
    private int numUsos;
    private int topeUsos;

    public Cupon() {
    }

    public Cupon(int id, String nombre, int descuento, LocalDate fechaInicio, LocalDate fechaVencimiento, int numUsos, int topeUsos) {
        this.id = id;
        this.nombre = nombre;
        this.descuento = descuento;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.numUsos = numUsos;
        this.topeUsos = topeUsos;
    }

    public int getId() {
        return id;
    }

    public int getDescuento() {
        return descuento;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public int getNumUsos() {
        return numUsos;
    }

    public int getTopeUsos() {
        return topeUsos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setNumUsos(int numUsos) {
        this.numUsos = numUsos;
    }

    public void setTopeUsos(int topeUsos) {
        this.topeUsos = topeUsos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
