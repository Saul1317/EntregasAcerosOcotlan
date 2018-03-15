package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerViewEntregaCamion;
import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerViewRutaCamion;
import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.EntregasCamion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;
import com.acerosocotlan.entregasacerosocotlan.modelo.RutaCamion_retrofit;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityEntregas extends AppCompatActivity {
    private RecyclerView entregaRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregas);
        Inicializador();
        ObtenerRuta();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void ObtenerRuta(){
        Call<List<EntregasCamion_retrofit>> call = NetworkAdapter.getApiService().EntregasCamiones(
                "http://entregas.dyndns.org/web/entregas/entregasmovil_42/gao");
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
    public void Inicializador(){
        entregaRecycler = (RecyclerView) findViewById(R.id.entregas_recycler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
