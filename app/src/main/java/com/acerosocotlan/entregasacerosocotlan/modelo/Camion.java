package com.acerosocotlan.entregasacerosocotlan.modelo;

/**
 * Created by Saul on 13/02/2018.
 */

public class Camion extends Choferes {

    private String foto_camion;
    private String modelo_camion;
    private String tipo_camion;
    private String peso_camion;
    private String peso_maximo_camion;
    private String tolerancia_camion;


    public Camion(String foto_perfil_chofer, String nombre_chofer, String foto_camion, String modelo_camion, String tipo_camion, String peso_camion, String peso_maximo_camion, String tolerancia_camion) {
        super(foto_perfil_chofer, nombre_chofer);
        this.foto_camion = foto_camion;
        this.modelo_camion = modelo_camion;
        this.tipo_camion = tipo_camion;
        this.peso_camion = peso_camion;
        this.peso_maximo_camion = peso_maximo_camion;
        this.tolerancia_camion = tolerancia_camion;
    }

    public String getFoto_camion() {
        return foto_camion;
    }

    public void setFoto_camion(String foto_camion) {
        this.foto_camion = foto_camion;
    }

    public String getModelo_camion() {
        return modelo_camion;
    }

    public void setModelo_camion(String modelo_camion) {
        this.modelo_camion = modelo_camion;
    }

    public String getTipo_camion() {
        return tipo_camion;
    }

    public void setTipo_camion(String tipo_camion) {
        this.tipo_camion = tipo_camion;
    }

    public String getPeso_camion() {
        return peso_camion;
    }

    public void setPeso_camion(String peso_camion) {
        this.peso_camion = peso_camion;
    }

    public String getPeso_maximo_camion() {
        return peso_maximo_camion;
    }

    public void setPeso_maximo_camion(String peso_maximo_camion) {
        this.peso_maximo_camion = peso_maximo_camion;
    }

    public String getTolerancia_camion() {
        return tolerancia_camion;
    }

    public void setTolerancia_camion(String tolerancia_camion) {
        this.tolerancia_camion = tolerancia_camion;
    }
}
