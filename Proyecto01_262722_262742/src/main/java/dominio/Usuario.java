/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author Isaac
 */
public abstract class Usuario {

    private int id;
    private String usario;
    private String contrasenia;
    private RolUsuario rol;

    public Usuario() {
    }

    public Usuario(int id, String usario, String contrasenia, RolUsuario rol) {
        this.id = id;
        this.usario = usario;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public String getUsario() {
        return usario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsario(String usario) {
        this.usario = usario;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

}
