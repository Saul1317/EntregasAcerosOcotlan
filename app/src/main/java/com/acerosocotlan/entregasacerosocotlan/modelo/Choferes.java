package com.acerosocotlan.entregasacerosocotlan.modelo;

/**
 * Created by Saul on 12/02/2018.
 */

public class Choferes {

    private String foto_perfil_chofer;
    private String nombre_chofer;


    public Choferes(String foto_perfil_chofer, String nombre_chofer) {
        this.foto_perfil_chofer = foto_perfil_chofer;
        this.nombre_chofer = nombre_chofer;
    }

    public String getFoto_perfil_chofer() {
        return foto_perfil_chofer;
    }

    public void setFoto_perfil_chofer(String foto_perfil_chofer) {
        this.foto_perfil_chofer = foto_perfil_chofer;
    }

    public String getNombre_chofer() {
        return nombre_chofer;
    }

    public void setNombre_chofer(String nombre_chofer) {
        this.nombre_chofer = nombre_chofer;
    }
}
