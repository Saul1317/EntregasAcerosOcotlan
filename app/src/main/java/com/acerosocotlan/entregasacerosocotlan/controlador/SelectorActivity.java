package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.Adaptador.Spinner_Adaptador;
import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;
import com.acerosocotlan.entregasacerosocotlan.modelo.Prueba_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.ValidacionConexion;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectorActivity extends AppCompatActivity {

    private String text_sucursal,text_sociedad;

    /******variables para spinners*********/
    private Spinner spinner_sucursal, spinner_local;

    /*******variable para boton**********/
    private Button boton_ingresar;
    private CardView cardView_bloqueo;
    private TextInputEditText textInputEditText_codigo_seguridad;

    /******* variable para preferencias*****/
    private SharedPreferences sharedPreferences;
    private Spinner_Adaptador SA;
    private Animation codigo_animacion;
    private String txtprueba1;
    private String txtprueba2;
    private ProgressDialog progressDoalog;

    String [] sociedad= {"Arandas","Autlan","Ayotlan","Bajio","DAO","GAO","GAO_resp","Ixtapa","La Cienega",
            "Laminas del Norte","Los Altos","Mucha Lamina","Pacifico","Pega","Saabsa","Tepa", "Tijuana","Zula"};

    String [] adapter_arandas= {"Arandas"};
    String [] adapter_autlan= {"Carretera Morelia","Lopez Mateos","Cocula","Ciudad Guzman","Patria","Circunvalacion","El Salto","Tesistan"};
    String [] adapter_ayotlan= {"Ayotlan","Degollado"};
    String [] adapter_bajio= {"Atotonilco","El 14","Lagos de moreno","Zapotlanejo","El mirador"};
    String [] adapter_dao= {"CAOSA","Sahuayo","Poncitlan","La barca","Madero","Morelia","La piedad","Lazaro cardenas","Economicos"};
    String [] adapter_gao= {"GAO Matriz","GAO Express","GAO Centro de Servicio"};
    String [] adapter_ixtapa= {"CEDI Ixtapa","Vallarta","Pitillal","Bucerias"};
    String [] adapter_la_cienega= {"Tateposco","Tonala","Jauja","Lopez Cienega"};
    String [] adapter_laminas= {"Comercializadora"};
    String [] adapter_los_altos= {"Los Altos","Jesus Maria","San Francisco","Altos Arandas"};
    String [] adapter_mucha= {"Mucha lamina"};
    String [] adapter_pacifico= {"Tepic","Ajijic","Acatlan","Chapala","Autlan","Ixtlan","Aeropuerto"};
    String [] adapter_pega= {"Santa Maria"};
    String [] adapter_SAABSA= {"Saabsa"};
    String [] adapter_tepa= {"Primeras","Segundas"};
    String [] adapter_tijuna= {"CEDI Tijuana","5 Y 10","Jardin Dorado","Mexicali","Ensenada"};
    String [] adapter_zula= {"Zula Matriz","Ferreteria"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /****ejecuta identificadores *****/
        Identificadores();
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        /*****adapatador para spinner sociedad*****/
        SA = new Spinner_Adaptador(getApplicationContext(),sociedad);
        spinner_local.setAdapter(SA);

        spinner_local.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinner = spinner_local.getSelectedItem().toString();
                if (spinner.equals("Arandas")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_arandas);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("Autlan")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_autlan);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("Ayotlan")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_ayotlan);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("Bajio")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_bajio);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("DAO")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_dao);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("GAO")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_gao);
                    spinner_sucursal.setAdapter(SA);
                }else if (spinner.equals("GAO_resp")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_gao);
                    spinner_sucursal.setAdapter(SA);
                }
                else if (spinner.equals("Ixtapa")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_ixtapa);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("La Cienega")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_la_cienega);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("Laminas del Norte")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_laminas);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("Los Altos")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_los_altos);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("Mucha Lamina")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_mucha);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("Pacifico")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_pacifico);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("Pega")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_pega);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("Saabsa")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_SAABSA);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("Tepa")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_tepa);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("Tijuana")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_tijuna);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("Zula")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_zula);
                    spinner_sucursal.setAdapter(SA);
                } else if (spinner.equals("Mucha Lamina")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_mucha);
                    spinner_sucursal.setAdapter(SA);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        boton_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_sucursal = spinner_sucursal.getSelectedItem().toString();
                text_sociedad = spinner_local.getSelectedItem().toString();
                String nuevaSociedad  = text_sociedad.replace(" ","_");
                GuardarPreferencias(text_sociedad, nuevaSociedad);
                prueba();
            }
        });

        textInputEditText_codigo_seguridad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager keyboard = (InputMethodManager) SelectorActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (hasFocus)
                    keyboard.showSoftInput(textInputEditText_codigo_seguridad, 0);
                else
                    keyboard.hideSoftInputFromWindow(textInputEditText_codigo_seguridad.getWindowToken(), 0);
            }
        });

        textInputEditText_codigo_seguridad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(textInputEditText_codigo_seguridad.getText().length()==4){
                    if(textInputEditText_codigo_seguridad.getText().toString().equals("0000")){
                        textInputEditText_codigo_seguridad.setFocusable(false);
                        cardView_bloqueo.startAnimation(codigo_animacion);
                        cardView_bloqueo.setVisibility(View.INVISIBLE);
                        spinner_sucursal.setEnabled(true);
                        spinner_local.setEnabled(true);
                        boton_ingresar.setEnabled(true);
                    }
                }
            }
        });
    }

    private void GuardarPreferencias(String sucursal,String sociedad){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sociedad", sociedad);
        editor.putString("sucursal", sucursal);
        editor.apply();
    }
    /******* metodo identificadores ********/
    private void Identificadores(){
        spinner_sucursal = (Spinner) findViewById(R.id.spinner_sucursal);
        spinner_local = (Spinner) findViewById(R.id.spinner_sociedad);
        spinner_sucursal.setEnabled(false);
        spinner_local.setEnabled(false);
        boton_ingresar= (Button) findViewById(R.id.boton_ingresar);
        boton_ingresar.setEnabled(false);
        cardView_bloqueo = (CardView) findViewById(R.id.cardview_bloqueo);
        textInputEditText_codigo_seguridad =  (TextInputEditText) findViewById(R.id.text_input_layout_codigo_seguridad);
        codigo_animacion = AnimationUtils.loadAnimation(SelectorActivity.this,R.anim.codigo_seguridad_animation);
        progressDoalog = new ProgressDialog(SelectorActivity.this);
        encryptar();
    }
    /******* metodo para mostrar actividad ********/
    private void NuevaActividad(){
        Intent i = new Intent(SelectorActivity.this, MainActivity.class);
        startActivity(i);
    }
    private void encryptar(){
        String text1 = "codigo";
        String text2 = "binarioxd";
        byte[] encrpt1;
        byte[] encrpt2;

        try {
            encrpt1 = text1.getBytes("UTF-8");
            encrpt2 = text2.getBytes("UTF-8");
            txtprueba1 = Base64.encodeToString(encrpt1, Base64.DEFAULT);
            txtprueba2 = Base64.encodeToString(encrpt2, Base64.DEFAULT);
            Log.i("USER", txtprueba1);
            Log.i("PASS", txtprueba2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private void prueba(){
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Validando sus datos");
        progressDoalog.setCancelable(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        Call<Prueba_retrofit> call = NetworkAdapter.getApiServiceAlternativo().Solicitarprueba("egao.php",txtprueba1,txtprueba2);
        call.enqueue(new Callback<Prueba_retrofit>() {
            @Override
            public void onResponse(Call<Prueba_retrofit> call, Response<Prueba_retrofit> response) {
                progressDoalog.dismiss();
                if(response.isSuccessful()) {
                    Prueba_retrofit  prueba_retrofit= response.body();
                    if (!prueba_retrofit.getResp().isEmpty()) {
                        MetodosSharedPreference.GuardarPruebaEntrega(sharedPreferences, prueba_retrofit.getResp());
                        Log.i("URL", MetodosSharedPreference.ObtenerPruebaEntregaPref(sharedPreferences));
                        NuevaActividad();
                    }else{
                        MostrarDialogCustomNoConfiguracion();
                    }
                }else{
                    MostrarDialogCustomNoConfiguracion();
                }
            }

            @Override
            public void onFailure(Call<Prueba_retrofit> call, Throwable t) {
                MostrarDialogCustomNoConfiguracion();
                progressDoalog.dismiss();
            }
        });
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
                        Toast.makeText(SelectorActivity.this, "No tienes acceso a internet", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SelectorActivity.this, "Esta apagado tu WIFI", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
