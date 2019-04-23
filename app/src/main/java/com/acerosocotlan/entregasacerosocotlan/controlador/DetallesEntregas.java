package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerViewDetallesEntrega;
import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerViewEntregaCamion;
import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.Detalles_entregas_retrofit;
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

public class DetallesEntregas extends AppCompatActivity {

    private RecyclerView material_recycler;
    private FloatingActionButton fab, fab_regresar;
    private SharedPreferences prs;
    private ProgressDialog progressDoalog;
    private Localizacion localizacion;
    private Vibrator vibrator;
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Calendar calendar;
    private TextView txt_detalle_folio_entrega, txt_detalle_entrega_pedido,
            txt_detalle_ruta, txt_detalle_estatus, txt_detalle_cliente, txt_detalle_direccion, txt_detalle_comentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_entregas);
        IncializadorView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(50);
                if (ValidarPermisosGPS()==true){
                    if(MetodosSharedPreference.ObtenerEstatusEntregaPref(prs).equals("En Ruta") || MetodosSharedPreference.ObtenerEstatusEntregaPref(prs).equals("Programado")){
                        DialogoConfirmacion();
                    }else{
                        Intent i = new Intent(DetallesEntregas.this, DescargaEntregaActivity.class);
                        startActivity(i);
                    }
                }else {
                    ActivityCompat.requestPermissions(DetallesEntregas.this, new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},100);
                }
            }
        });

        fab_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                Intent i = new Intent(DetallesEntregas.this, ActivityEntregas.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    private void IncializadorView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        material_recycler= (RecyclerView) findViewById(R.id.material_recycler);
        txt_detalle_folio_entrega= (TextView) findViewById(R.id.txt_detalle_folio_entrega);
        txt_detalle_entrega_pedido= (TextView) findViewById(R.id.txt_detalle_entrega_pedido);
        txt_detalle_ruta= (TextView) findViewById(R.id.txt_detalle_ruta);
        txt_detalle_estatus= (TextView) findViewById(R.id.txt_detalle_estatus);
        txt_detalle_cliente= (TextView) findViewById(R.id.txt_detalle_cliente);
        txt_detalle_direccion= (TextView) findViewById(R.id.txt_detalle_direccion);
        txt_detalle_comentario= (TextView) findViewById(R.id.txt_detalle_comentario);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_regresar = (FloatingActionButton) findViewById(R.id.fab_regresar);

        txt_detalle_folio_entrega.setText("Folio "+MetodosSharedPreference.ObtenerFolioEntregaPref(prs));
        Log.i("FOLIO", MetodosSharedPreference.ObtenerFolioEntregaPref(prs));

        txt_detalle_entrega_pedido.setText(MetodosSharedPreference.ObtenerEntregaPedido(prs));
        Log.i("ENTREGA", MetodosSharedPreference.ObtenerEntregaPedido(prs));

        txt_detalle_ruta.setText("Pertenece a la ruta "+MetodosSharedPreference.ObtenerFolioRutaPref(prs));
        Log.i("RUTA", MetodosSharedPreference.ObtenerFolioRutaPref(prs));

        if(MetodosSharedPreference.ObtenerEstatusEntregaPref(prs).equals("En Ruta")||MetodosSharedPreference.ObtenerEstatusEntregaPref(prs).equals("Programado")) {
            txt_detalle_estatus.setText("Estatus sin iniciar");
        }else {
            txt_detalle_estatus.setText("Estatus entrega iniciada");
        }
        Log.i("ESTATUS", MetodosSharedPreference.ObtenerEstatusEntregaPref(prs));

        txt_detalle_cliente.setText(MetodosSharedPreference.ObtenerClienteEntregaPref(prs));
        Log.i("CLIENTE", MetodosSharedPreference.ObtenerClienteEntregaPref(prs));

        txt_detalle_direccion.setText(MetodosSharedPreference.ObtenerDireccionEntregaPref(prs));
        Log.i("DIRECCIÓN", MetodosSharedPreference.ObtenerDireccionEntregaPref(prs));

        txt_detalle_comentario.setText(MetodosSharedPreference.ObtenerComentarioEntregaPref(prs));
        Log.i("COMENTARIO", MetodosSharedPreference.ObtenerComentarioEntregaPref(prs));

        ObtenerEntrega();
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
    public void DialogoConfirmacion(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Esta a punto de comenzar la entrega "+ MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+", ¿Desea continuar?");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                progressDoalog = new ProgressDialog(DetallesEntregas.this);
                progressDoalog.setMessage("Iniciando la entrega");
                progressDoalog.setCancelable(false);
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();
                localizacion = new Localizacion(getApplicationContext());
                localizacion.ObtenerMejorLocalizacion();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        localizacion.cancelarLocalizacion();
                        IniciarEntrega();
                    }
                },4000);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
    public String ObtenerFecha(){
        calendar = Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime()).toString();
    }
    public void IniciarEntrega(){
        String primeraVez="";

        if(MetodosSharedPreference.ObtenerNumEntregasTotal(prs).equals(MetodosSharedPreference.ObtenerNumEntregasActual(prs))){
            primeraVez="1";
            Log.i("AVISO PERSONAL"," ");
        }else{
            primeraVez="0";
            Log.i("AVISO PERSONAL","SI SE MANDA EL MENSAJE APARTIR DE LA SEGUNDA ENTREGA");
        }
        Call<List<String>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).IniciaEntrega(
                "iniciarentrega_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"_inicio/"+MetodosSharedPreference.getSociedadPref(prs),//llegada
                ObtenerFecha(),
                String.valueOf(localizacion.getLatitude()),
                String.valueOf(localizacion.getLongitud()),
                primeraVez);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                progressDoalog.dismiss();
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Se guardó la información", Toast.LENGTH_LONG).show();
                    Log.e("RESPUESTA ENTREGA", response.body().toString());
                    Intent i = new Intent(DetallesEntregas.this, DescargaEntregaActivity.class);
                    startActivity(i);
                }else{

                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                progressDoalog.dismiss();
                Log.e("ERROR INICIO", t.getMessage());
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(DetallesEntregas.this, ActivityEntregas.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    //AQUI LO TENIA ESTATICO
    public void ObtenerEntrega(){
        Call<List<Detalles_entregas_retrofit>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).MaterialEntrega(
                "detallechofer_"+MetodosSharedPreference.ObtenerFolioEntregaPref(prs)+"/"+MetodosSharedPreference.getSociedadPref(prs));
        call.enqueue(new Callback<List<Detalles_entregas_retrofit>>() {
            @Override
            public void onResponse(Call<List<Detalles_entregas_retrofit>> call, Response<List<Detalles_entregas_retrofit>> response) {
               List<Detalles_entregas_retrofit> listaMaterial = response.body();
                Log.i("Consulta","CONSULTA"+ response.body());
               if(response.isSuccessful()){
                   Log.i("Consulta","CONSULTA EXITOSA");
                   LlenarRecyclerView(listaMaterial);
               }
            }

            @Override
            public void onFailure(Call<List<Detalles_entregas_retrofit>> call, Throwable t) {
                Log.i("Consulta","CONSULTA NO EXITOSA");
            }
        });
    }
    public void LlenarRecyclerView(List<Detalles_entregas_retrofit> material){
        LinearLayoutManager l = new LinearLayoutManager(getApplicationContext());
        l.setOrientation(LinearLayoutManager.VERTICAL);
        material_recycler.setLayoutManager(l);
        AdapterRecyclerViewDetallesEntrega arv = new AdapterRecyclerViewDetallesEntrega(R.layout.cardview_detalles_entrega,material,DetallesEntregas.this, getApplicationContext());
        material_recycler.setAdapter(arv);
    }
}
