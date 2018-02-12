package com.acerosocotlan.entregasacerosocotlan.modelo;

/**
 * Created by Saul on 12/02/2018.
 */

public class Choferes {

    private String foto_perfil;
    private String fondo_perfil;
    private String nombre;

    public Choferes(String foto_perfil, String fondo_perfil, String nombre) {
        this.foto_perfil = foto_perfil;
        this.fondo_perfil = fondo_perfil;
        this.nombre = nombre;
    }

    public String getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public String getFondo_perfil() {
        return fondo_perfil;
    }

    public void setFondo_perfil(String fondo_perfil) {
        this.fondo_perfil = fondo_perfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
