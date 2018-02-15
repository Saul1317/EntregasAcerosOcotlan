package com.acerosocotlan.entregasacerosocotlan.modelo;

/**
 * Created by Saul on 15/02/2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Camion_retrofit {

    @SerializedName("clave")
    @Expose
    private String clave;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("estatus")
    @Expose
    private String estatus;
    @SerializedName("tipo")
    @Expose
    private String tipo;
    @SerializedName("modelo")
    @Expose
    private String modelo;
    @SerializedName("serie")
    @Expose
    private String serie;
    @SerializedName("placas")
    @Expose
    private String placas;
    @SerializedName("pesoUnidad")
    @Expose
    private String pesoUnidad;
    @SerializedName("PesoMaximo")
    @Expose
    private String pesoMaximo;
    @SerializedName("pesoToleranciaMaxima")
    @Expose
    private String pesoToleranciaMaxima;
    @SerializedName("claveChofer")
    @Expose
    private String claveChofer;
    @SerializedName("foto")
    @Expose
    private String foto;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("apellidoPaterno")
    @Expose
    private String apellidoPaterno;
    @SerializedName("apellidoMaterno")
    @Expose
    private String apellidoMaterno;

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getPlacas() {
        return placas;
    }

    public void setPlacas(String placas) {
        this.placas = placas;
    }

    public String getPesoUnidad() {
        return pesoUnidad;
    }

    public void setPesoUnidad(String pesoUnidad) {
        this.pesoUnidad = pesoUnidad;
    }

    public String getPesoMaximo() {
        return pesoMaximo;
    }

    public void setPesoMaximo(String pesoMaximo) {
        this.pesoMaximo = pesoMaximo;
    }

    public String getPesoToleranciaMaxima() {
        return pesoToleranciaMaxima;
    }

    public void setPesoToleranciaMaxima(String pesoToleranciaMaxima) {
        this.pesoToleranciaMaxima = pesoToleranciaMaxima;
    }

    public String getClaveChofer() {
        return claveChofer;
    }

    public void setClaveChofer(String claveChofer) {
        this.claveChofer = claveChofer;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }
}