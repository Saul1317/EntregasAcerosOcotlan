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
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.acerosocotlan.entregasacerosocotlan.modelo.ValidacionConexion;
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
    //DATOS EXTERNOS
    private ProgressDialog progressDoalog;
    //SHARED PREFERENCE
    private SharedPreferences prs;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txt_id_ruta_entregas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entregas);
        Inicializador();
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
        txt_id_ruta_entregas = (TextView) findViewById(R.id.txt_id_ruta_entregas);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAzulGoogle);

        progressDoalog = new ProgressDialog(ActivityEntregas.this);
        progressDoalog.setMessage("Preparando los datos");
        progressDoalog.setCancelable(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txt_id_ruta_entregas.setText("Esta en la ruta "+MetodosSharedPreference.ObtenerFolioRutaPref(prs));
        ObtenerEntrega();
    }
    public void AbrirSinEntregas(){
        Intent i = new Intent(ActivityEntregas.this, SinEntregasActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    //RETROFIT2
    public void ObtenerEntrega(){
        progressDoalog.show();
        Call<List<EntregasCamion_retrofit>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).EntregasCamiones(
                "entregasmovil_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs)+"/"+MetodosSharedPreference.getSociedadPref(prs));
        call.enqueue(new Callback<List<EntregasCamion_retrofit>>() {
            @Override
            public void onResponse(Call<List<EntregasCamion_retrofit>> call, Response<List<EntregasCamion_retrofit>> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()){
                    swipeRefreshLayout.setRefreshing(false);
                    List<EntregasCamion_retrofit> entrega_retrofit = response.body();
                    if(entrega_retrofit.isEmpty()){
                        AbrirSinEntregas();
                    }else{
                        LlenarRecyclerView(entrega_retrofit);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<EntregasCamion_retrofit>> call, Throwable t) {
                progressDoalog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                //MostrarDialogCustomNoConfiguracion();
                Log.i("ERROR", t.toString());
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
                    }else{
                        Toast.makeText(ActivityEntregas.this, "No tienes acceso a internet", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ActivityEntregas.this, "Esta apagado tu WIFI", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ActivityEntregas.this, ScrollingRutasActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
