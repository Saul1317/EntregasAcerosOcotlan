package com.acerosocotlan.entregasacerosocotlan.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saul on 21/03/2018.
 */

public class AvisoPersonal_retrofit {
    @SerializedName("sucursal")
    @Expose
    private String sucursal;
    @SerializedName("pedido")
    @Expose
    private String pedido;
    @SerializedName("estatus")
    @Expose
    private String estatus;
    @SerializedName("correo")
    @Expose
    private String correo;
    @SerializedName("telefono")
    @Expose
    private String telefono;

    public AvisoPersonal_retrofit(String sucursal, String pedido, String estatus, String correo, String telefono) {
        this.sucursal = sucursal;
        this.pedido = pedido;
        this.estatus = estatus;
        this.correo = correo;
        this.telefono = telefono;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "AvisoPersonal_retrofit{" +
                "sucursal='" + sucursal + '\'' +
                ", pedido='" + pedido + '\'' +
                ", estatus='" + estatus + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
