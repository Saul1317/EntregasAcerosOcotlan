package com.acerosocotlan.entregasacerosocotlan.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prueba_retrofit {
    @SerializedName("resp")
    @Expose
    private String resp;

    public Prueba_retrofit(String resp) {
        this.resp = resp;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    @Override
    public String toString() {
        return "Prueba_retrofit{" +
                "resp='" + resp + '\'' +
                '}';
    }
}
