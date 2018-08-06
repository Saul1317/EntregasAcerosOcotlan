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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerViewRutaCamion;
import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;
import com.acerosocotlan.entregasacerosocotlan.modelo.RutaCamion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.ValidacionConexion;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ScrollingRutasActivity extends AppCompatActivity {
    private SharedPreferences prs;
    private RecyclerView rutasRecycler;
    private TextView nombre_chofer, peso_camion,peso_maximo_camion, placa_camion;
    private ImageView foto_chofer;
    private ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_rutas);
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        inicializador();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObtenerRuta();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling_rutas, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cerrar_sesion_menu:
                dialogo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void remover_variables_sharedpreference(){
        prs.edit().clear().apply();
    }
    private void dialogo(){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Alerta de cierre de sesión");
        alert.setMessage("Introduzca la contraseña");

        final EditText input = new EditText(this);
        input.setHint("Contraseña");
        input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

        LinearLayout linearLayout = new LinearLayout(ScrollingRutasActivity.this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;

        input.setLayoutParams(layoutParams);

        linearLayout.addView(input);
        linearLayout.setPadding(60, 0, 60, 0);

        alert.setView(linearLayout);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                String inputName = input.getText().toString();
                if(inputName.equals("123")){
                    remover_variables_sharedpreference();
                    salir_sesion();
                }else{

                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }
    private void salir_sesion(){
        Intent i = new Intent(ScrollingRutasActivity.this, SelectorActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    public void ObtenerRuta(){
        progressDoalog.show();
        Log.i("SOCIEDAD", MetodosSharedPreference.getSociedadPref(prs));
        Call<List<RutaCamion_retrofit>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(prs)).RutasCamiones(
                "rutasmovil_"+MetodosSharedPreference.ObtenerPlacasPref(prs)+"/" +MetodosSharedPreference.getSociedadPref(prs));
        call.enqueue(new Callback<List<RutaCamion_retrofit>>() {
            @Override
            public void onResponse(Call<List<RutaCamion_retrofit>> call, Response<List<RutaCamion_retrofit>> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()){
                    List<RutaCamion_retrofit> rutas_retrofit = response.body();
                    LlenarRecyclerView(rutas_retrofit);
                }
            }
            @Override
            public void onFailure(Call<List<RutaCamion_retrofit>> call, Throwable t) {
                progressDoalog.dismiss();
                MostrarDialogCustomNoConfiguracion();
            }
        });
    }
    public void LlenarRecyclerView(List<RutaCamion_retrofit> camion){
        LinearLayoutManager l = new LinearLayoutManager(getApplicationContext());
        l.setOrientation(LinearLayoutManager.VERTICAL);
        rutasRecycler.setLayoutManager(l);
        AdapterRecyclerViewRutaCamion arv = new AdapterRecyclerViewRutaCamion(camion,R.layout.cardview_rutas, ScrollingRutasActivity.this, getApplicationContext());
        rutasRecycler.setAdapter(arv);
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
    private void MostrarDialogCustomNoConfiguracion(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.DialogErrorConexion);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.activity_error_conexion, null);
        alert.setCancelable(false);
        alert.setView(dialoglayout);
        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogErrorConexion;
        alertDialog.show();
        FloatingActionButton botonEntendido = (FloatingActionButton) dialoglayout.findViewById(R.id.fab_recargar_app);
        botonEntendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidacionConexion.isConnectedWifi(getApplicationContext())||ValidacionConexion.isConnectedMobile(getApplicationContext())){
                    if(ValidacionConexion.isOnline(getApplicationContext())){
                        alertDialog.dismiss();
                    }else{
                        Toast.makeText(ScrollingRutasActivity.this, "No tienes acceso a internet", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ScrollingRutasActivity.this, "Esta apagado tu WIFI", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void inicializador(){
        rutasRecycler = (RecyclerView) findViewById(R.id.rutas_recycler);
        nombre_chofer = (TextView) findViewById(R.id.txt_nombre_perfil_chofer);
        nombre_chofer.setText(MetodosSharedPreference.ObtenerNombrePref(prs));

        placa_camion = (TextView) findViewById(R.id.txt_placas);
        placa_camion.setText("Placas: "+MetodosSharedPreference.ObtenerPlacasPref(prs));

        peso_camion  = (TextView) findViewById(R.id.txt_peso_camion);
        peso_camion.setText("Peso del camion: "+MetodosSharedPreference.ObtenerPesoCamionChoferPref(prs));

        peso_maximo_camion  = (TextView) findViewById(R.id.txt_peso_maximo_camion);
        peso_maximo_camion.setText("Peso maximo del camion: "+MetodosSharedPreference.ObtenerPesoMaximoCamionPref(prs));

        foto_chofer = (ImageView) findViewById(R.id.foto_perfil_chofer);
        Picasso.with(getApplicationContext()).load(MetodosSharedPreference.ObtenerFotoPref(prs)).fit().placeholder(R.drawable.obrero).into(foto_chofer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_rutas);
        setSupportActionBar(toolbar);

        if(ValidarPermisosGPS()==false){
            ActivityCompat.requestPermissions(ScrollingRutasActivity.this, new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},100);
        }
        progressDoalog = new ProgressDialog(ScrollingRutasActivity.this);
        progressDoalog.setMessage("Preparando los datos");
        progressDoalog.setCancelable(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        ObtenerRuta();
    }
}
