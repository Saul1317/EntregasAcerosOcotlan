package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private double latitude;
    private double longitud;
    //SHARED PREFERENCE
    private SharedPreferences prs;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregas);
        Inicializador();
        ObtenerEntrega();
        btn_finalizar_ruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NuevaActividad();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ObtenerEntrega();
            }
        });
    }
    //ACTIVITY
    public void Inicializador(){
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        MetodosSharedPreference.BorrarFolioEntrega(prs);
        entregaRecycler = (RecyclerView) findViewById(R.id.entregas_recycler);
        btn_finalizar_ruta = (Button) findViewById(R.id.btn_finalizar_ruta);
        btn_finalizar_ruta.setEnabled(false);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAzulGoogle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    public void NuevaActividad(){
        Intent i = new Intent(ActivityEntregas.this, FinalizarRutaActivity.class);
        startActivity(i);
    }
    //RETROFIT2
    public void ObtenerEntrega(){
        Call<List<EntregasCamion_retrofit>> call = NetworkAdapter.getApiService().EntregasCamiones(
                "entregasmovil_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs)+"/"+MetodosSharedPreference.getSociedadPref(prs));
        call.enqueue(new Callback<List<EntregasCamion_retrofit>>() {
            @Override
            public void onResponse(Call<List<EntregasCamion_retrofit>> call, Response<List<EntregasCamion_retrofit>> response) {
                if (response.isSuccessful()){
                    swipeRefreshLayout.setRefreshing(false);
                    List<EntregasCamion_retrofit> entrega_retrofit = response.body();
                    if(entrega_retrofit.isEmpty()){
                        btn_finalizar_ruta.setEnabled(true);
                    }else{
                        LlenarRecyclerView(entrega_retrofit);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<EntregasCamion_retrofit>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Intent intentErrorConexion = new Intent(ActivityEntregas.this, ErrorConexion.class);
                intentErrorConexion.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentErrorConexion);
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

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(ActivityEntregas.this, ScrollingRutasActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
