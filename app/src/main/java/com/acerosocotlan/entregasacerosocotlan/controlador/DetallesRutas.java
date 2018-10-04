package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerViewDetallesEntrega;
import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerViewDetallesRutas;
import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.Detalles_Ruta_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.Detalles_entregas_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.EntregasCamion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallesRutas extends AppCompatActivity {

    private FloatingActionButton fab_detalle_ruta_regresar;
    private Button boton_iniciar_ruta;
    private TextView txt_folio_ruta, txt_detalles_fecha_programada, txt_cantidad_entregas, txt_detalle_ruta_estatus;
    private RecyclerView entregas_disponibles;
    private SharedPreferences prs;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_rutas);
        InicializadorView();
        ObtenerEntregasDisponibles();
        fab_detalle_ruta_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbrirRutas();
                vibrator.vibrate(50);
            }
        });
        boton_iniciar_ruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoInicioRuta();
                vibrator.vibrate(50);
            }
        });
    }
    private void DialogoInicioRuta() {
        AlertDialog.Builder alert = new AlertDialog.Builder(DetallesRutas.this);
        alert.setMessage("Esta a punto de inicializar esta ruta, ¿Desea continuar?");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent(getApplicationContext(), FormularioActivity.class);
                startActivity(intent);
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
    @Override
    public void onBackPressed()
    {
        AbrirRutas();
    }
    private void AbrirRutas() {
        Intent i = new Intent(DetallesRutas.this, ScrollingRutasActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    private void InicializadorView() {
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        fab_detalle_ruta_regresar = (FloatingActionButton) findViewById(R.id.fab_detalle_ruta_regresar);
        boton_iniciar_ruta = (Button) findViewById(R.id.boton_iniciar_ruta);
        txt_folio_ruta = (TextView) findViewById(R.id.txt_folio_ruta);
        txt_detalles_fecha_programada = (TextView) findViewById(R.id.txt_detalles_fecha_programada);
        txt_cantidad_entregas = (TextView) findViewById(R.id.txt_cantidad_entregas);
        txt_detalle_ruta_estatus = (TextView) findViewById(R.id.txt_detalle_ruta_estatus);
        entregas_disponibles = (RecyclerView) findViewById(R.id.entregas_disponibles);

        txt_folio_ruta.setText("Folio "+MetodosSharedPreference.ObtenerFolioRutaPref(prs));
        txt_detalles_fecha_programada.setText("Programado para el "+MetodosSharedPreference.ObtenerFechaProgramada(prs));
        txt_cantidad_entregas.setText("Esta ruta tiene "+MetodosSharedPreference.ObtenerNumEntregas(prs)+" entregas");
        if(MetodosSharedPreference.ObtenerFechaInicioRuta(prs).isEmpty()){
            txt_detalle_ruta_estatus.setText("Estatus: sin iniciar");
        }else{
            txt_detalle_ruta_estatus.setText("Estatus: iniciada");
        }
    }
    public void ObtenerEntregasDisponibles(){
        Call<List<Detalles_Ruta_retrofit>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).EntregasDisponibles(
                "entregasmovil_"+MetodosSharedPreference.ObtenerFolioRutaPref(prs)+"/"+MetodosSharedPreference.getSociedadPref(prs));
        call.enqueue(new Callback<List<Detalles_Ruta_retrofit>>() {
            @Override
            public void onResponse(Call<List<Detalles_Ruta_retrofit>> call, Response<List<Detalles_Ruta_retrofit>> response) {
                if(response.isSuccessful()){
                    List<Detalles_Ruta_retrofit> listaEntregas  = response.body();
                    LlenarRecyclerView(listaEntregas);
                }
            }

            @Override
            public void onFailure(Call<List<Detalles_Ruta_retrofit>> call, Throwable t) {

            }
        });
    }
    public void LlenarRecyclerView(List<Detalles_Ruta_retrofit> ListaEntregas){
        LinearLayoutManager l = new LinearLayoutManager(getApplicationContext());
        l.setOrientation(LinearLayoutManager.VERTICAL);
        entregas_disponibles.setLayoutManager(l);
        AdapterRecyclerViewDetallesRutas arv = new AdapterRecyclerViewDetallesRutas(R.layout.cardview_detalles_rutas, ListaEntregas,DetallesRutas.this, getApplicationContext());
        entregas_disponibles.setAdapter(arv);
    }
}
