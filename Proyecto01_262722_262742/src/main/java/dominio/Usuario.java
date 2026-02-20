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
    private String usuario;
    private String contrasenia;
    private RolUsuario rol;

    public Usuario() {
    }

    public Usuario(int id, String usario, String contrasenia, RolUsuario rol) {
        this.id = id;
        this.usuario = usario;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
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

    public void setUsuario(String usario) {
        this.usuario = usario;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

}
