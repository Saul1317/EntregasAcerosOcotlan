package com.acerosocotlan.entregasacerosocotlan.modelo;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Saul on 15/02/2018.
 */

public interface NetworkService {
    //http://10.1.15.12:8080/web/entregas/catcamiones/gao_pruebas

    @POST("catcamiones/gao_pruebas")
    Call<Camion_retrofit> CatalogoCamiones();
}
