package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerView;
import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.Camion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView choferesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializador();
        ObtenerCamiones();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObtenerCamiones();
            }
        });
    }
    public void ObtenerCamiones(){
        Call<List<Camion_retrofit>> call =NetworkAdapter.getApiService().CatalogoCamiones("catcamiones/gao");
        call.enqueue(new Callback<List<Camion_retrofit>>() {
            @Override
            public void onResponse(Call<List<Camion_retrofit>> call, Response<List<Camion_retrofit>> response) {
                List<Camion_retrofit> camion_retrofit = response.body();
                LlenarRecyclerView(camion_retrofit);
            }

            @Override
            public void onFailure(Call<List<Camion_retrofit>> call, Throwable t) {
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
    public void inicializador(){
        choferesRecycler = (RecyclerView) findViewById(R.id.choferes_recycler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
