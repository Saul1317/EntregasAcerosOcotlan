package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerView;
import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerViewRutaCamion;
import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.Camion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;
import com.acerosocotlan.entregasacerosocotlan.modelo.RutaCamion_retrofit;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScrollingRutasActivity extends AppCompatActivity {
    private SharedPreferences prs;
    private RecyclerView rutasRecycler;
    private TextView nombre_chofer, clave_chofer, peso_camion,peso_maximo_camion, placa_camion;
    private ImageView foto_chofer;
    private AdapterRecyclerView instanciaRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_rutas);
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        inicializador();
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
        Call<List<RutaCamion_retrofit>> call = NetworkAdapter.getApiService().RutasCamiones(
                "rutasmovil_"+MetodosSharedPreference.ObtenerPlacasPref(prs)+"/gao");
        call.enqueue(new Callback<List<RutaCamion_retrofit>>() {
            @Override
            public void onResponse(Call<List<RutaCamion_retrofit>> call, Response<List<RutaCamion_retrofit>> response) {
                if (response.isSuccessful()){
                    List<RutaCamion_retrofit> rutas_retrofit = response.body();
                    LlenarRecyclerView(rutas_retrofit);
                }
            }

            @Override
            public void onFailure(Call<List<RutaCamion_retrofit>> call, Throwable t) {
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
    public void inicializador(){
        rutasRecycler = (RecyclerView) findViewById(R.id.rutas_recycler);
        nombre_chofer = (TextView) findViewById(R.id.txt_nombre_perfil_chofer);
        nombre_chofer.setText(MetodosSharedPreference.ObtenerNombrePref(prs)+" "+MetodosSharedPreference.ObtenerApellidoPref(prs));

        placa_camion = (TextView) findViewById(R.id.txt_placas);
        placa_camion.setText("Placas: "+MetodosSharedPreference.ObtenerPlacasPref(prs));

        clave_chofer  = (TextView) findViewById(R.id.txt_clave_chofer);
        clave_chofer.setText("Clave del chofer: "+MetodosSharedPreference.ObtenerClaveChoferPref(prs));

        peso_camion  = (TextView) findViewById(R.id.txt_peso_camion);
        peso_camion.setText("Peso del camion: "+MetodosSharedPreference.ObtenerPesoCamionChoferPref(prs));

        peso_maximo_camion  = (TextView) findViewById(R.id.txt_peso_maximo_camion);
        peso_maximo_camion.setText("Peso maximo del camion: "+MetodosSharedPreference.ObtenerPesoMaximoCamionPref(prs));

        foto_chofer = (ImageView) findViewById(R.id.foto_perfil_chofer);
        Picasso.with(getApplicationContext()).load(MetodosSharedPreference.ObtenerFotoPref(prs)).fit().into(foto_chofer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_rutas);
        setSupportActionBar(toolbar);
    }
}
