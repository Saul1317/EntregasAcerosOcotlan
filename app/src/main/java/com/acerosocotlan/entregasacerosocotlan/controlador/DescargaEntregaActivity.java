package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.Localizacion;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DescargaEntregaActivity extends AppCompatActivity {

    ImageButton btn_descarga_camion, btn_finalizacion_camion;
    //DATOS EXTERNOS
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Calendar calendar;
    //SHARED PREFERENCE
    private SharedPreferences prs;
    //INSTANCIA
    private Localizacion localizacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga_entrega);
        Inicializador();
        if(MetodosSharedPreference.ObtenerFechaLlegadaPref(prs).isEmpty()){
            btn_finalizacion_camion.setEnabled(false);
            btn_descarga_camion.setEnabled(true);
        }else{
            btn_finalizacion_camion.setEnabled(true);
            btn_descarga_camion.setEnabled(false);
        }

        btn_descarga_camion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidarPermisosGPS()==true){
                    DialogoConfirmacion();
                }else {
                    ActivityCompat.requestPermissions(DescargaEntregaActivity.this, new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},100);
                }
            }
        });
        btn_finalizacion_camion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NuevaActividad();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entregas, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.posponer_entrega:
                DialogoConfirmacionPosponerEntrega();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void Inicializador(){
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_descarga_camion = (ImageButton) findViewById(R.id.boton_descarga_camion);
        btn_finalizacion_camion = (ImageButton) findViewById(R.id.boton_finalizacion_descarga);
        btn_finalizacion_camion.setEnabled(false);
    }
    public void NuevaActividad(){
        Intent i = new Intent(DescargaEntregaActivity.this, EvidenciasActivity.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    public void DialogoConfirmacion(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Aviso de confirmación");
        alert.setMessage("Esta a punto de informar su llegada con el cliente, desea continuar?");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                InsertarLlegadaCamion();
                btn_finalizacion_camion.setEnabled(true);
                btn_descarga_camion.setEnabled(false);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }
    public void DialogoConfirmacionPosponerEntrega(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Aviso de confirmación");
        alert.setMessage("Esta a punto de posponer la entrega, desea continuar?");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                InsertarLlegadaCamion();
                btn_finalizacion_camion.setEnabled(true);
                btn_descarga_camion.setEnabled(false);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }
    //OBTENER DATOS
    public String ObtenerFecha(){
        calendar = Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime()).toString();
    }
    public boolean ValidarPermisosGPS(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }else{
                return true;
            }
        }else {
            return true;
        }
    }
    //RETROFIT2
    public void InsertarLlegadaCamion(){
        localizacion = new Localizacion();
        Call<List<String>> call = NetworkAdapter.getApiService().LlegadaEntrega(
                "iniciarentrega_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"_llegada/gao",
                ObtenerFecha(),
                localizacion.ObtenerLatitud(DescargaEntregaActivity.this, getApplicationContext()),
                localizacion.ObtenerLongitud(DescargaEntregaActivity.this, getApplicationContext()));
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    List<String> respuesta = response.body();
                }else{
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("ERROR SERVIDOR", "onFailure: ERROR"+t.getMessage());
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(DescargaEntregaActivity.this, ActivityEntregas.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
