package com.acerosocotlan.entregasacerosocotlan.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detalles_Ruta_retrofit {

    @SerializedName("Entrega")
    @Expose
    private String entrega;

    @SerializedName("Pedido")
    @Expose
    private String pedido;

    @SerializedName("nomcliente")
    @Expose
    private String nomcliente;

    @SerializedName("direccion")
    @Expose
    private String direccion;

    public Detalles_Ruta_retrofit(String entrega, String pedido, String nomcliente, String direccion) {
        this.entrega = entrega;
        this.pedido = pedido;
        this.nomcliente = nomcliente;
        this.direccion = direccion;
    }

    public String getEntrega() {
        return entrega;
    }

    public void setEntrega(String entrega) {
        this.entrega = entrega;
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

    @Override
    public String toString() {
        return "Detalles_Ruta_retrofit{" +
                "entrega='" + entrega + '\'' +
                ", pedido='" + pedido + '\'' +
                ", nomcliente='" + nomcliente + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}
