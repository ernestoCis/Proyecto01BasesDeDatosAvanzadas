package dominio;

import java.time.LocalDate;

/**
 * <b>Clase que representa un Cupón de descuento en la panadería.</b>
 * <p>Esta clase se encarga de modelar los cupones promocionales que los clientes
 * pueden aplicar a sus pedidos programados para obtener descuentos. Incluye información sobre:</p>
 * <ul>
 * <li>El valor del descuento.</li>
 * <li>El periodo de validez (fechas de inicio y vencimiento).</li>
 * <li>El control de usos (cuántas veces se ha usado y el límite máximo permitido).</li>
 * </ul>
 *
 * @author 262722
 * @author 262742
 */
public class Cupon {
    
    private int id;
    private String nombre;
    private int descuento; 
    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;
    private int numUsos;
    private int topeUsos;

    /**
     * Constructor por defecto de la clase <b>Cupon</b>.
     * <p>Crea una instancia vacía sin inicializar sus atributos.</p>
     */
    public Cupon() {
    }

    /**
     * Constructor que inicializa todos los atributos de un cupón de descuento.
     *
     * @param id El identificador único del cupón en la base de datos.
     * @param nombre El nombre o código de texto del cupón (ej. "PAN10").
     * @param descuento El valor o porcentaje de descuento que otorga el cupón.
     * @param fechaInicio La fecha a partir de la cual el cupón es válido.
     * @param fechaVencimiento La fecha en la que expira el cupón.
     * @param numUsos La cantidad de veces que este cupón ya ha sido utilizado.
     * @param topeUsos El límite máximo de veces que se puede usar este cupón.
     */
    public Cupon(int id, String nombre, int descuento, LocalDate fechaInicio, LocalDate fechaVencimiento, int numUsos, int topeUsos) {
        this.id = id;
        this.nombre = nombre;
        this.descuento = descuento;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.numUsos = numUsos;
        this.topeUsos = topeUsos;
    }

    /**
     * Obtiene el identificador del cupón.
     * @return Un número entero con el <code>id</code>.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el valor del descuento del cupón.
     * @return Un número entero que representa el descuento.
     */
    public int getDescuento() {
        return descuento;
    }

    /**
     * Obtiene la fecha de inicio de validez del cupón.
     * @return Un objeto <code>LocalDate</code> con la fecha de inicio.
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Obtiene la fecha de vencimiento del cupón.
     * @return Un objeto <code>LocalDate</code> con la fecha en la que expira.
     */
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * Obtiene la cantidad de veces que el cupón ha sido utilizado.
     * @return Un número entero con el total de usos actuales.
     */
    public int getNumUsos() {
        return numUsos;
    }

    /**
     * Obtiene el límite máximo de usos permitidos para el cupón.
     * @return Un número entero con el tope de usos.
     */
    public int getTopeUsos() {
        return topeUsos;
    }

    /**
     * Establece el identificador del cupón.
     * @param id El <code>id</code> a asignar.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Establece el valor del descuento del cupón.
     * @param descuento El monto o porcentaje a descontar.
     */
    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    /**
     * Establece la fecha de inicio en la que el cupón será válido.
     * @param fechaInicio Objeto <code>LocalDate</code> de inicio.
     */
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Establece la fecha de expiración del cupón.
     * @param fechaVencimiento Objeto <code>LocalDate</code> de vencimiento.
     */
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * Establece la cantidad de usos que ha tenido el cupón.
     * @param numUsos El número de usos a registrar.
     */
    public void setNumUsos(int numUsos) {
        this.numUsos = numUsos;
    }

    /**
     * Establece el límite máximo de usos del cupón.
     * @param topeUsos El tope de usos permitidos.
     */
    public void setTopeUsos(int topeUsos) {
        this.topeUsos = topeUsos;
    }

    /**
     * Obtiene el nombre o código del cupón.
     * @return Un <code>String</code> con el nombre del cupón.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre o código del cupón.
     * @param nombre El nombre o código a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}