package com.acerosocotlan.entregasacerosocotlan.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saul on 27/02/2018.
 */

public class RutaCamion_retrofit {

    @SerializedName("id_entregas")
    @Expose
    private String idEntregas;
    @SerializedName("sucursal")
    @Expose
    private String sucursal;
    @SerializedName("almacen")
    @Expose
    private String almacen;
    @SerializedName("folio")
    @Expose
    private String folio;
    @SerializedName("claveVehiculo")
    @Expose
    private String claveVehiculo;
    @SerializedName("claveChofer")
    @Expose
    private String claveChofer;
    @SerializedName("fechaSalida")
    @Expose
    private String fechaSalida;
    @SerializedName("fechaLlegada")
    @Expose
    private String fechaLlegada;
    @SerializedName("kilomSalida")
    @Expose
    private String kilomSalida;
    @SerializedName("kilomLlegada")
    @Expose
    private String kilomLlegada;
    @SerializedName("placas")
    @Expose
    private String placas;
    @SerializedName("numEntregas")
    @Expose
    private String numEntregas;

    public RutaCamion_retrofit(String idEntregas, String sucursal, String almacen, String folio, String claveVehiculo, String claveChofer, String fechaSalida, String fechaLlegada, String kilomSalida, String kilomLlegada, String placas, String numEntregas) {
        this.idEntregas = idEntregas;
        this.sucursal = sucursal;
        this.almacen = almacen;
        this.folio = folio;
        this.claveVehiculo = claveVehiculo;
        this.claveChofer = claveChofer;
        this.fechaSalida = fechaSalida;
        this.fechaLlegada = fechaLlegada;
        this.kilomSalida = kilomSalida;
        this.kilomLlegada = kilomLlegada;
        this.placas = placas;
        this.numEntregas = numEntregas;
    }

    public String getIdEntregas() {
        return idEntregas;
    }

    public void setIdEntregas(String idEntregas) {
        this.idEntregas = idEntregas;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getAlmacen() {
        return almacen;
    }

    public void setAlmacen(String almacen) {
        this.almacen = almacen;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
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

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(String fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public String getKilomSalida() {
        return kilomSalida;
    }

    public void setKilomSalida(String kilomSalida) {
        this.kilomSalida = kilomSalida;
    }

    public String getKilomLlegada() {
        return kilomLlegada;
    }

    public void setKilomLlegada(String kilomLlegada) {
        this.kilomLlegada = kilomLlegada;
    }

    public String getPlacas() {
        return placas;
    }

    public void setPlacas(String placas) {
        this.placas = placas;
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
                "idEntregas='" + idEntregas + '\'' +
                ", sucursal='" + sucursal + '\'' +
                ", almacen='" + almacen + '\'' +
                ", folio='" + folio + '\'' +
                ", claveVehiculo='" + claveVehiculo + '\'' +
                ", claveChofer='" + claveChofer + '\'' +
                ", fechaSalida='" + fechaSalida + '\'' +
                ", fechaLlegada='" + fechaLlegada + '\'' +
                ", kilomSalida='" + kilomSalida + '\'' +
                ", kilomLlegada='" + kilomLlegada + '\'' +
                ", placas='" + placas + '\'' +
                ", numEntregas='" + numEntregas + '\'' +
                '}';
    }
}