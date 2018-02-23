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
    @SerializedName("foto_camion")
    @Expose
    private String fotoCamion;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("apellidoPaterno")
    @Expose
    private String apellidoPaterno;
    @SerializedName("apellidoMaterno")
    @Expose
    private String apellidoMaterno;
    @SerializedName("foto_chofer")
    @Expose
    private String fotoChofer;

    public Camion_retrofit(String clave, String descripcion, String estatus, String tipo, String modelo, String serie, String placas, String pesoUnidad, String pesoMaximo, String pesoToleranciaMaxima, String claveChofer, String fotoCamion, String nombre, String apellidoPaterno, String apellidoMaterno, String fotoChofer) {
        this.clave = clave;
        this.descripcion = descripcion;
        this.estatus = estatus;
        this.tipo = tipo;
        this.modelo = modelo;
        this.serie = serie;
        this.placas = placas;
        this.pesoUnidad = pesoUnidad;
        this.pesoMaximo = pesoMaximo;
        this.pesoToleranciaMaxima = pesoToleranciaMaxima;
        this.claveChofer = claveChofer;
        this.fotoCamion = fotoCamion;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.fotoChofer = fotoChofer;
    }

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

    public String getFotoCamion() {
        return fotoCamion;
    }

    public void setFotoCamion(String fotoCamion) {
        this.fotoCamion = fotoCamion;
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

    public String getFotoChofer() {
        return fotoChofer;
    }

    public void setFotoChofer(String fotoChofer) {
        this.fotoChofer = fotoChofer;
    }

    @Override
    public String toString() {
        return "Camion_retrofit{" +
                "clave='" + clave + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estatus='" + estatus + '\'' +
                ", tipo='" + tipo + '\'' +
                ", modelo='" + modelo + '\'' +
                ", serie='" + serie + '\'' +
                ", placas='" + placas + '\'' +
                ", pesoUnidad='" + pesoUnidad + '\'' +
                ", pesoMaximo='" + pesoMaximo + '\'' +
                ", pesoToleranciaMaxima='" + pesoToleranciaMaxima + '\'' +
                ", claveChofer='" + claveChofer + '\'' +
                ", fotoCamion='" + fotoCamion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", apellidoMaterno='" + apellidoMaterno + '\'' +
                ", fotoChofer='" + fotoChofer + '\'' +
                '}';
    }
}