package com.acerosocotlan.entregasacerosocotlan.modelo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrador on 19/04/2018.
 */

public class NetworkAdapter {
    private static NetworkService API_SERVICE;

    public static NetworkService getApiService(String x) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(x).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        API_SERVICE = retrofit.create(NetworkService.class);
        return API_SERVICE;
    }
    public static NetworkService getApiServiceAlternativo() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.acerosocotlan.mx/gao/").addConverterFactory(GsonConverterFactory.create()).build();
        API_SERVICE = retrofit.create(NetworkService.class);
        return API_SERVICE;
    }
}

