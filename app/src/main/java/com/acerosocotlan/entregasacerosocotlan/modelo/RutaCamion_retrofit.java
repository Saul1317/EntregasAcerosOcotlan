package com.acerosocotlan.entregasacerosocotlan.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saul on 27/02/2018.
 */

public class RutaCamion_retrofit {

    @SerializedName("id_ruta")
    @Expose
    private String idRuta;
    @SerializedName("sucursal")
    @Expose
    private String sucursal;
    @SerializedName("ProgramadaPara")
    @Expose
    private String programadaPara;
    @SerializedName("claveVehiculo")
    @Expose
    private String claveVehiculo;
    @SerializedName("claveChofer")
    @Expose
    private String claveChofer;
    @SerializedName("FechaInicio")
    @Expose
    private String fechaInicio;
    @SerializedName("KmInicio")
    @Expose
    private String kmInicio;
    @SerializedName("FechaLlegada")
    @Expose
    private String fechaLlegada;
    @SerializedName("KmLlegada")
    @Expose
    private String kmLlegada;
    @SerializedName("numEntregas")
    @Expose
    private String numEntregas;

    public RutaCamion_retrofit(String idRuta, String sucursal, String programadaPara, String claveVehiculo, String claveChofer, String fechaInicio, String kmInicio, String fechaLlegada, String kmLlegada, String numEntregas) {
        this.idRuta = idRuta;
        this.sucursal = sucursal;
        this.programadaPara = programadaPara;
        this.claveVehiculo = claveVehiculo;
        this.claveChofer = claveChofer;
        this.fechaInicio = fechaInicio;
        this.kmInicio = kmInicio;
        this.fechaLlegada = fechaLlegada;
        this.kmLlegada = kmLlegada;
        this.numEntregas = numEntregas;
    }

    public String getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getProgramadaPara() {
        return programadaPara;
    }

    public void setProgramadaPara(String programadaPara) {
        this.programadaPara = programadaPara;
    }

    public String getClaveVehiculo() {
        return claveVehiculo;
    }

    public void setClaveVehiculo(String claveVehiculo) {
        this.claveVehiculo = claveVehiculo;
    }

    public String getClaveChofer() {
        return claveChofer;
    }

    public void setClaveChofer(String claveChofer) {
        this.claveChofer = claveChofer;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getKmInicio() {
        return kmInicio;
    }

    public void setKmInicio(String kmInicio) {
        this.kmInicio = kmInicio;
    }

    public String getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(String fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public String getKmLlegada() {
        return kmLlegada;
    }

    public void setKmLlegada(String kmLlegada) {
        this.kmLlegada = kmLlegada;
    }

    public String getNumEntregas() {
        return numEntregas;
    }

    public void setNumEntregas(String numEntregas) {
        this.numEntregas = numEntregas;
    }

    @Override
    public String toString() {
        return "RutaCamion_retrofit{" +
                "idRuta='" + idRuta + '\'' +
                ", sucursal='" + sucursal + '\'' +
                ", programadaPara='" + programadaPara + '\'' +
                ", claveVehiculo='" + claveVehiculo + '\'' +
                ", claveChofer='" + claveChofer + '\'' +
                ", fechaInicio='" + fechaInicio + '\'' +
                ", kmInicio='" + kmInicio + '\'' +
                ", fechaLlegada='" + fechaLlegada + '\'' +
                ", kmLlegada='" + kmLlegada + '\'' +
                ", numEntregas='" + numEntregas + '\'' +
                '}';
    }
}