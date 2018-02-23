package com.acerosocotlan.entregasacerosocotlan.modelo;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Saul on 15/02/2018.
 */

public interface NetworkService {
    //http://10.1.15.12:8080/web/entregas/catcamiones/gao_pruebas
    @POST
    Call<List<Camion_retrofit>> CatalogoCamiones(@Url String url);
}
