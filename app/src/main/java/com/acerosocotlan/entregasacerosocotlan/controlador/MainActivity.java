package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerView;
import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.Camion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;
import com.acerosocotlan.entregasacerosocotlan.modelo.ValidacionConexion;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView choferesRecycler;
    private ProgressDialog progressDoalog;
    private SharedPreferences sharedPreferences;
    private Animation botonRecargarAnimacion;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializador();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.startAnimation(botonRecargarAnimacion);
                ObtenerCamiones();
            }
        });
    }

    /*
    * Método para realizar una petición post y obtener el catálogo de los camiones disponibles en la sucursal
    */
    public void ObtenerCamiones(){
        progressDoalog.show();
        Log.i("URL CONSULTA",MetodosSharedPreference.ObtenerPruebaEntregaPref(sharedPreferences)+"catcamiones/"+MetodosSharedPreference.getSociedadPref(sharedPreferences));
        Call<List<Camion_retrofit>> call =NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(sharedPreferences)).CatalogoCamiones("catcamiones/"+MetodosSharedPreference.getSociedadPref(sharedPreferences));
        call.enqueue(new Callback<List<Camion_retrofit>>() {
            @Override
            public void onResponse(Call<List<Camion_retrofit>> call, Response<List<Camion_retrofit>> response) {
                progressDoalog.dismiss();
                if(response.isSuccessful()) {
                    //si la peticón devuelve una respuesta correcta entonces alamacenamos los camiones en una lista
                    List<Camion_retrofit> camion_retrofit = response.body();
                    // la lista es mandada por parametro
                    LlenarRecyclerView(camion_retrofit);
                    Log.i("RESPUESTA","HOLA1");
                }else{
                    MostrarDialogCustomNoConfiguracion();
                    Log.i("RESPUESTA","HOLA2");
                }
            }

            @Override
            public void onFailure(Call<List<Camion_retrofit>> call, Throwable t) {
                progressDoalog.dismiss();
                MostrarDialogCustomNoConfiguracion();
                Log.i("RESPUESTA","HOLA3");
            }
        });
    }


    public void LlenarRecyclerView(List<Camion_retrofit> camion){
        LinearLayoutManager l = new LinearLayoutManager(getApplicationContext());
        l.setOrientation(LinearLayoutManager.VERTICAL);
        choferesRecycler.setLayoutManager(l);
        AdapterRecyclerView arv = new AdapterRecyclerView(camion,R.layout.cardview_choferes, MainActivity.this, getApplicationContext());
        choferesRecycler.setAdapter(arv);
    }

    private void MostrarDialogCustomNoConfiguracion(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.DialogErrorConexion);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.activity_error_conexion, null);
        alert.setCancelable(false);
        alert.setView(dialoglayout);
        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogErrorConexion;
        alertDialog.show();
        final FloatingActionButton botonEntendido = (FloatingActionButton) dialoglayout.findViewById(R.id.fab_recargar_app);
        botonEntendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidacionConexion.isConnectedWifi(getApplicationContext())||ValidacionConexion.isConnectedMobile(getApplicationContext())){
                    if(ValidacionConexion.isOnline(getApplicationContext())){
                        alertDialog.dismiss();
                        ObtenerCamiones();
                    }else{
                        Toast.makeText(MainActivity.this, "No tienes acceso a internet", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Esta apagado tu WIFI", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void inicializador(){
        choferesRecycler = (RecyclerView) findViewById(R.id.choferes_recycler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        setSupportActionBar(toolbar);
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Preparando los datos");
        progressDoalog.setCancelable(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        botonRecargarAnimacion= AnimationUtils.loadAnimation(MainActivity.this,R.anim.flatbutton_recargar);
        ObtenerCamiones();
    }
}
