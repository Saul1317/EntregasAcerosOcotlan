package com.acerosocotlan.entregasacerosocotlan.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saul on 14/03/2018.
 */

public class EntregasCamion_retrofit {
    @SerializedName("FolioEntrega")
    @Expose
    private String folioEntrega;
    @SerializedName("sucursal")
    @Expose
    private String sucursal;
    @SerializedName("FolioRuta")
    @Expose
    private String folioRuta;
    @SerializedName("Entrega")
    @Expose
    private String entrega;
    @SerializedName("FechaInicio")
    @Expose
    private String fechaInicio;
    @SerializedName("FechaLlegada")
    @Expose
    private String fechaLlegada;
    @SerializedName("FechaSalida")
    @Expose
    private String fechaSalida;
    @SerializedName("comentarios")
    @Expose
    private String comentarios;
    @SerializedName("Estatus")
    @Expose
    private String estatus;
    @SerializedName("KgTotal")
    @Expose
    private String kgTotal;
    @SerializedName("Pedido")
    @Expose
    private String pedido;
    @SerializedName("nomcliente")
    @Expose
    private String nomcliente;
    @SerializedName("direccion")
    @Expose
    private String direccion;
    @SerializedName("regresado")
    @Expose
    private String regresado;

    public EntregasCamion_retrofit(String folioEntrega, String sucursal, String folioRuta, String entrega, String fechaInicio, String fechaLlegada, String fechaSalida, String comentarios, String estatus, String kgTotal, String pedido, String nomcliente, String direccion, String regresado) {
        this.folioEntrega = folioEntrega;
        this.sucursal = sucursal;
        this.folioRuta = folioRuta;
        this.entrega = entrega;
        this.fechaInicio = fechaInicio;
        this.fechaLlegada = fechaLlegada;
        this.fechaSalida = fechaSalida;
        this.comentarios = comentarios;
        this.estatus = estatus;
        this.kgTotal = kgTotal;
        this.pedido = pedido;
        this.nomcliente = nomcliente;
        this.direccion = direccion;
        this.regresado = regresado;
    }

    public String getFolioEntrega() {
        return folioEntrega;
    }

    public void setFolioEntrega(String folioEntrega) {
        this.folioEntrega = folioEntrega;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getFolioRuta() {
        return folioRuta;
    }

    public void setFolioRuta(String folioRuta) {
        this.folioRuta = folioRuta;
    }

    public String getEntrega() {
        return entrega;
    }

    public void setEntrega(String entrega) {
        this.entrega = entrega;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(String fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getKgTotal() {
        return kgTotal;
    }

    public void setKgTotal(String kgTotal) {
        this.kgTotal = kgTotal;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getNomcliente() {
        return nomcliente;
    }

    public void setNomcliente(String nomcliente) {
        this.nomcliente = nomcliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRegresado() {
        return regresado;
    }

    public void setRegresado(String regresado) {
        this.regresado = regresado;
    }

    @Override
    public String toString() {
        return "EntregasCamion_retrofit{" +
                "folioEntrega='" + folioEntrega + '\'' +
                ", sucursal='" + sucursal + '\'' +
                ", folioRuta='" + folioRuta + '\'' +
                ", entrega='" + entrega + '\'' +
                ", fechaInicio='" + fechaInicio + '\'' +
                ", fechaLlegada='" + fechaLlegada + '\'' +
                ", fechaSalida='" + fechaSalida + '\'' +
                ", comentarios='" + comentarios + '\'' +
                ", estatus='" + estatus + '\'' +
                ", kgTotal='" + kgTotal + '\'' +
                ", pedido='" + pedido + '\'' +
                ", nomcliente='" + nomcliente + '\'' +
                ", direccion='" + direccion + '\'' +
                ", regresado='" + regresado + '\'' +
                '}';
    }
}
