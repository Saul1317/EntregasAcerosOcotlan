package com.acerosocotlan.entregasacerosocotlan.modelo;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saul on 15/02/2018.
 */

public class NetworkAdapter {
    private static NetworkService API_SERVICE;

    public static NetworkService getApiService() {
        String URL = "http://10.1.15.12:8080/web/entregas/";

        if(API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Camion_retrofit requestPost = new Camion_retrofit();
            API_SERVICE = retrofit.create(NetworkService.class);
        }
        return API_SERVICE;
    }
}