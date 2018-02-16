package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerView;
import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.Camion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView choferesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        choferesRecycler = (RecyclerView) findViewById(R.id.choferes_recycler);
        ObtenerCamiones();
    }
    public void ObtenerCamiones(){
        Call<List<Camion_retrofit>> call =NetworkAdapter.getApiService().CatalogoCamiones();
        call.enqueue(new Callback<List<Camion_retrofit>>() {
            @Override
            public void onResponse(Call<List<Camion_retrofit>> call, Response<List<Camion_retrofit>> response) {
                List<Camion_retrofit> camion_retrofit = response.body();
                LinearLayoutManager l = new LinearLayoutManager(getApplicationContext());
                l.setOrientation(LinearLayoutManager.VERTICAL);
                choferesRecycler.setLayoutManager(l);
                AdapterRecyclerView arv = new AdapterRecyclerView(camion_retrofit,R.layout.cardview_choferes, MainActivity.this);
                choferesRecycler.setAdapter(arv);
            }

            @Override
            public void onFailure(Call<List<Camion_retrofit>> call, Throwable t) {

            }
        });
    }
}
