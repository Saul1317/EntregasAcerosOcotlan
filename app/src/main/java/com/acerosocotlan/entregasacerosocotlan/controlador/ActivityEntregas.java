package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerViewEntregaCamion;
import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerViewRutaCamion;
import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.AvisoPersonal_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.EntregasCamion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.InformacionAvisos_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;
import com.acerosocotlan.entregasacerosocotlan.modelo.RutaCamion_retrofit;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ActivityEntregas extends AppCompatActivity {
    //VIEWS
    private RecyclerView entregaRecycler;
    private Button btn_finalizar_ruta;
    //DATOS EXTERNOS
    private Location location;
    private LocationManager locationManager;
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Calendar calendar;
    private double latitude;
    private double longitud;
    //SHARED PREFERENCE
    private SharedPreferences prs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregas);
        Inicializador();
        ObtenerRuta();
        btn_finalizar_ruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NuevaActividad();

            }
        });
    }

    //ACTIVITY
    public void Inicializador(){
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        entregaRecycler = (RecyclerView) findViewById(R.id.entregas_recycler);
        btn_finalizar_ruta = (Button) findViewById(R.id.btn_finalizar_ruta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    public void NuevaActividad(){
        Intent i = new Intent(ActivityEntregas.this, FinalizarRutaActivity.class);
        startActivity(i);
    }
    //OBTENER DATOS
    public String ObtenerFecha(){
        calendar = Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime()).toString();
    }
    //RETROFIT2
    public void ObtenerRuta(){
        Call<List<EntregasCamion_retrofit>> call = NetworkAdapter.getApiService().EntregasCamiones(
                "http://entregas.dyndns.org/web/entregas/entregasmovil_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs)+"/gao");
        call.enqueue(new Callback<List<EntregasCamion_retrofit>>() {
            @Override
            public void onResponse(Call<List<EntregasCamion_retrofit>> call, Response<List<EntregasCamion_retrofit>> response) {
                if (response.isSuccessful()){
                    List<EntregasCamion_retrofit> entrega_retrofit = response.body();
                    LlenarRecyclerView(entrega_retrofit);
                }
            }

            @Override
            public void onFailure(Call<List<EntregasCamion_retrofit>> call, Throwable t) {
                Log.i("ESTADO", "NO ENTRA");
            }
        });
    }
    public void LlenarRecyclerView(List<EntregasCamion_retrofit> camion){
        LinearLayoutManager l = new LinearLayoutManager(getApplicationContext());
        l.setOrientation(LinearLayoutManager.VERTICAL);
        entregaRecycler.setLayoutManager(l);
        AdapterRecyclerViewEntregaCamion arv = new AdapterRecyclerViewEntregaCamion(camion,R.layout.cardview_entregas, ActivityEntregas.this, getApplicationContext());
        entregaRecycler.setAdapter(arv);
    }
    public void InsertarFormulario(String folio, String latitud, String longitud){
        Call<List<String>> call = NetworkAdapter.getApiService().IniciaEntrega(
                "iniciarentrega_"+folio+"_inicio/gao",
                 ObtenerFecha(),
                latitud,
                longitud);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    List<String> respuesta = response.body();
                    String valor = respuesta.get(0).toString();
                    Log.i("Respuesta", valor);
                    if (valor.equals("correcto")){
                        ObtenerAvisoPersonal();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No manches", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("ERROR SERVIDOR", "onFailure: ERROR"+t.getLocalizedMessage());
            }
        });
    }
    public void ObtenerAvisoPersonal() {
        Call<List<AvisoPersonal_retrofit>> call = NetworkAdapter.getApiService().ObtenerAvisoPersonal("avisopersonal_5/gao");
        call.enqueue(new Callback<List<AvisoPersonal_retrofit>>() {
            @Override
            public void onResponse(Call<List<AvisoPersonal_retrofit>> call, Response<List<AvisoPersonal_retrofit>> response) {
                    if (response.isSuccessful()){
                        List<AvisoPersonal_retrofit> respuesta = response.body();
                        Log.i("AQUI", "Se mando");
                    }
            }
            @Override
            public void onFailure(Call<List<AvisoPersonal_retrofit>> call, Throwable t) {
                Log.i("ERROR SERVIDOR", "onFailure: ERROR"+t.getLocalizedMessage());
            }
        });

    }
    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(ActivityEntregas.this, ScrollingRutasActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
