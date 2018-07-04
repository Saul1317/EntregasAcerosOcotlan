package com.acerosocotlan.entregasacerosocotlan.modelo;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Saul on 15/02/2018.
 */

public interface NetworkService {
    //http://10.1.15.12:8080/web/entregas/catcamiones/gao_pruebas
    //http://entregas.dyndns.org/web/entregas/rutasmovil-jr46404/gao_pruebas
    @POST
    Call<List<Camion_retrofit>> CatalogoCamiones(@Url String url);
    @POST
    Call<List<RutaCamion_retrofit>> RutasCamiones(@Url String url);
    @POST
    Call<List<EntregasCamion_retrofit>> EntregasCamiones(@Url String url);
    @POST
    Call<List<InformacionAvisos_retrofit>> ObtenerInformacionParaAviso(@Url String url);
    @FormUrlEncoded
    @POST
    Call<List<String>> MandarFormularioPost(@Url String url,
                                            @Field("fechainicio") String fecha,
                                            @Field("latinicio") String latitud,
                                            @Field("loninicio") String longitud,
                                            @Field("kminicio") String km);
    @FormUrlEncoded
    @POST
    Call<List<String>> LlegadaRuta(@Url String url,
                                            @Field("fechallegada") String fecha,
                                            @Field("latllegada") String latitud,
                                            @Field("lonllegada") String longitud,
                                            @Field("kmllegada") String km);
    @FormUrlEncoded
    @POST
    Call<List<String>> IniciaEntrega(@Url String url,
                                                     @Field("fecha") String fecha,
                                                     @Field("latitud") String latitud,
                                                     @Field("longitud") String longitud);
    @FormUrlEncoded
    @POST
    Call<List<String>> LlegadaEntrega(@Url String url,
                                     @Field("fecha") String fecha,
                                     @Field("latitud") String latitud,
                                     @Field("longitud") String longitud);
    @FormUrlEncoded
    @POST
    Call<List<String>> DescargarEntrega(@Url String url,
                                        @Field("fecha") String fecha,
                                        @Field("latitud") String latitud,
                                        @Field("longitud") String longitud);

    @FormUrlEncoded
    @POST
    Call<List<String>> SalidaEntrega(@Url String url,
                                     @Field("fecha") String fecha,
                                     @Field("latitud") String latitud,
                                     @Field("longitud") String longitud,
                                     @Field("comentarios") String comentario);

    @FormUrlEncoded
    @POST
    Call<List<String>> PosponerEntrega(@Url String url,
                                       @Field("fecha") String fecha,
                                       @Field("latitud") String latitud,
                                       @Field("longitud") String longitud);
}