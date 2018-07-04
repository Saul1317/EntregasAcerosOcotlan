package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.EntregasCamion_retrofit;
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

    //VIEWS
    ImageButton btn_descarga_camion, btn_finalizacion_camion, btn_ensitio_camion;
    LinearLayout  linear_layout_filtro_llegada, linear_layout_filtro_descarga, linear_layout_filtro_salida;
    TextView txt_filtro_llegada, txt_filtro_descarga, txt_filtro_salida;
    //DATOS EXTERNOS
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Calendar calendar;
    //SHARED PREFERENCE
    private SharedPreferences prs;
    //LOCATION
    private LocationManager locationManager;
    private double longitudeBest =0, latitudeBest=0;
    private ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga_entrega);
        Inicializador();
        btn_ensitio_camion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidarPermisosGPS()==true){
                    DialogoConfirmacion();
                }else {
                    ActivityCompat.requestPermissions(DescargaEntregaActivity.this, new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},100);
                }
            }
        });
        btn_descarga_camion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogoConfirmacionDescargarEntrega();
            }
        });
        btn_finalizacion_camion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NuevaActividad();
            }
        });
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
    private void ValidacionEstadoBoton() {
        switch (MetodosSharedPreference.ObtenerEstatusEntregaPref(prs)) {
            case "En Ruta":
                btn_ensitio_camion.setEnabled(true);
                btn_descarga_camion.setEnabled(false);
                btn_finalizacion_camion.setEnabled(false);
                Log.i("ESTADO DE LA ENTREGA", "TODO HABILITADO: "+ MetodosSharedPreference.ObtenerEstatusEntregaPref(prs));
                break;
            case "Proximo":
                btn_ensitio_camion.setEnabled(true);
                btn_descarga_camion.setEnabled(false);
                btn_finalizacion_camion.setEnabled(false);
                Log.i("ESTADO DE LA ENTREGA", "TODO HABILITADO: "+ MetodosSharedPreference.ObtenerEstatusEntregaPref(prs));
                break;
            case "En sitio":
                btn_ensitio_camion.setEnabled(false);
                btn_descarga_camion.setEnabled(true);
                btn_finalizacion_camion.setEnabled(false);
                //si esta bloqueado el primer boton entonces se habilita el filtro de finalizado
                linear_layout_filtro_llegada.setVisibility(View.VISIBLE);
                txt_filtro_llegada.setVisibility(View.VISIBLE);
                btn_ensitio_camion.setEnabled(false);
                Log.i("ESTADO DE LA ENTREGA", "ESTAS EN SITIO, YA PUEDES DESCARGAR: "+ MetodosSharedPreference.ObtenerEstatusEntregaPref(prs));
                break;
            case "Descargando":
                btn_ensitio_camion.setEnabled(false);
                btn_descarga_camion.setEnabled(false);
                btn_finalizacion_camion.setEnabled(true);
                //si esta bloqueado los dos primeros boton entonces se habilita el filtro de finalizado
                //boton en sitio
                linear_layout_filtro_llegada.setVisibility(View.VISIBLE);
                txt_filtro_llegada.setVisibility(View.VISIBLE);
                btn_ensitio_camion.setEnabled(false);
                //boton descargando
                linear_layout_filtro_descarga.setVisibility(View.VISIBLE);
                txt_filtro_descarga.setVisibility(View.VISIBLE);
                btn_descarga_camion.setEnabled(false);
                Log.i("ESTADO DE LA ENTREGA", "ESTAS DESCARGANDO, YA PUEDES FINALIZAR: "+ MetodosSharedPreference.ObtenerEstatusEntregaPref(prs));
                break;
            case "Entregado":
                btn_ensitio_camion.setEnabled(false);
                btn_descarga_camion.setEnabled(false);
                btn_finalizacion_camion.setEnabled(false);
                //si la entrega fue completada se habilita el filtro en todos los botones
                //boton en sitio
                linear_layout_filtro_llegada.setVisibility(View.VISIBLE);
                txt_filtro_llegada.setVisibility(View.VISIBLE);
                btn_ensitio_camion.setEnabled(false);
                //boton descargando
                linear_layout_filtro_descarga.setVisibility(View.VISIBLE);
                txt_filtro_descarga.setVisibility(View.VISIBLE);
                btn_descarga_camion.setEnabled(false);
                //boton salida
                linear_layout_filtro_salida.setVisibility(View.VISIBLE);
                txt_filtro_salida.setVisibility(View.VISIBLE);
                btn_finalizacion_camion.setEnabled(false);
                Log.i("ESTADO DE LA ENTREGA", "YA FUE FINALIZADA: "+ MetodosSharedPreference.ObtenerEstatusEntregaPref(prs));

                break;
            default:
                break;
        }
    }
    public void NuevaActividad(){
        Intent i = new Intent(DescargaEntregaActivity.this, EvidenciasActivity.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    //Dialogos de Confirmación
    public void DialogoConfirmacion(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Esta a punto de informar su llegada con el cliente, desea continuar?");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                progressDoalog = new ProgressDialog(DescargaEntregaActivity.this);
                progressDoalog.setMessage("Preparando los datos");
                progressDoalog.setCancelable(false);
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();
                ObtenerMejorLocalizacion();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }
    public void DialogoConfirmacionDescargarEntrega(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Esta a punto de informar que esta descargando la entrega, desea continuar?");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                progressDoalog = new ProgressDialog(DescargaEntregaActivity.this);
                progressDoalog.setMessage("Preparando los datos");
                progressDoalog.setCancelable(false);
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();
                InsertarDescargaCamion();
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
        alert.setMessage("Esta a punto de posponer la entrega, desea continuar?");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                PosponerEntrega();
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
    //RETROFIT2
    public void InsertarLlegadaCamion(){
        Call<List<String>> call = NetworkAdapter.getApiService().LlegadaEntrega(
                "iniciarentrega_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"_ensitio/"+MetodosSharedPreference.getSociedadPref(prs),//llegada
                ObtenerFecha(),
                String.valueOf(latitudeBest),
                String.valueOf(longitudeBest));
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                progressDoalog.dismiss();
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Información guardada", Toast.LENGTH_LONG).show();
                    linear_layout_filtro_llegada.setVisibility(View.VISIBLE);
                    txt_filtro_llegada.setVisibility(View.VISIBLE);
                    btn_ensitio_camion.setEnabled(false);
                    btn_descarga_camion.setEnabled(true);
                }else{

                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                progressDoalog.dismiss();
                Log.i("ERROR SERVIDOR", "onFailure: ERROR"+t.getMessage());
            }
        });
    }
    public void InsertarDescargaCamion(){
        Call<List<String>> call = NetworkAdapter.getApiService().DescargarEntrega(
                "iniciarentrega_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"_llegada/"+MetodosSharedPreference.getSociedadPref(prs),
                "","","");
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                progressDoalog.dismiss();
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Descargando", Toast.LENGTH_LONG).show();
                    linear_layout_filtro_descarga.setVisibility(View.VISIBLE);
                    txt_filtro_descarga.setVisibility(View.VISIBLE);
                    btn_descarga_camion.setEnabled(false);
                    btn_finalizacion_camion.setEnabled(true);
                }else{
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                progressDoalog.dismiss();
                Log.i("ERROR SERVIDOR", "onFailure: ERROR"+t.getMessage());
            }
        });
    }
    public void PosponerEntrega(){
        Call<List<String>> call = NetworkAdapter.getApiService().PosponerEntrega(
                "iniciarentrega_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"_posponer/"+MetodosSharedPreference.getSociedadPref(prs),
                "","","");
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    Intent i = new Intent(DescargaEntregaActivity.this, ActivityEntregas.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else{
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("ERROR SERVIDOR", "onFailure: ERROR"+t.getMessage());
            }
        });
    }
    //LOCALIZACION
    private void ObtenerMejorLocalizacion(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 1000, 5, LocalizacionListener);
        }
    }
    private final LocationListener LocalizacionListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeBest = location.getLongitude();
            latitudeBest = location.getLatitude();
            Log.i("LOCALIZACION",String.valueOf(longitudeBest)+" "+String.valueOf(latitudeBest));
            locationManager.removeUpdates(LocalizacionListener);
            InsertarLlegadaCamion();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };
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
    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(DescargaEntregaActivity.this, ActivityEntregas.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    public void Inicializador(){
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_descarga_camion = (ImageButton) findViewById(R.id.boton_descarga_camion);
        btn_finalizacion_camion = (ImageButton) findViewById(R.id.boton_finalizacion_descarga);
        btn_ensitio_camion= (ImageButton) findViewById(R.id.boton_ensitio_camion);

        linear_layout_filtro_llegada= (LinearLayout) findViewById(R.id.linear_layout_filtro_llegada);
        linear_layout_filtro_descarga= (LinearLayout) findViewById(R.id.linear_layout_filtro_descarga);
        linear_layout_filtro_salida= (LinearLayout) findViewById(R.id.linear_layout_filtro_salida);

        txt_filtro_llegada= (TextView) findViewById(R.id.txt_filtro_llegada);
        txt_filtro_descarga= (TextView) findViewById(R.id.txt_filtro_descarga);
        txt_filtro_salida= (TextView) findViewById(R.id.txt_filtro_salida);

        //VISIBILIDAD FILTRO
        linear_layout_filtro_llegada.setVisibility(View.INVISIBLE);
        linear_layout_filtro_descarga.setVisibility(View.INVISIBLE);
        linear_layout_filtro_salida.setVisibility(View.INVISIBLE);
        //VISIBILIDAD TEXTO Del filtro
        txt_filtro_llegada.setVisibility(View.INVISIBLE);
        txt_filtro_descarga.setVisibility(View.INVISIBLE);
        txt_filtro_salida.setVisibility(View.INVISIBLE);
        ValidacionEstadoBoton();
    }
}
