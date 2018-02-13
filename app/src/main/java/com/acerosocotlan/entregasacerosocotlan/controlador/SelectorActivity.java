package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.acerosocotlan.entregasacerosocotlan.Adaptador.Spinner_Adaptador;
import com.acerosocotlan.entregasacerosocotlan.R;

public class SelectorActivity extends AppCompatActivity {

    String text_sucursal,text_sociedad;

    /******variables para spinners*********/
    Spinner spinner_sucursal, spinner_local;

    /*******variable para boton**********/
    ImageButton boton_ingresar;

    /******* variable para preferencias*****/
    private SharedPreferences prs;
    Spinner_Adaptador SA;

    String [] sociedad= {"Pruebas","Arandas","Autlan","Ayotlan","Bajio","DAO","GAO","Ixtapa","La Cienega","DAO",
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
    String [] adapter_prueba= {"Pruebas"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /****ejecuta identificadores *****/
        Identificadores();
        prs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

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
                } else if (spinner.equals("Ixtapa")){
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
                else if (spinner.equals("Pruebas")){
                    SA = new Spinner_Adaptador(getApplicationContext(),adapter_prueba);
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
                //  Toast.makeText(LoginActivity.this, text_sucursal +" "+ text_sociedad, Toast.LENGTH_SHORT).show();
                NuevaActividad();
                GuardarPreferencias(text_sociedad, text_sucursal);
            }
        });
    }

    private void GuardarPreferencias(String sociedad, String sucursal){
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("sociedad", sociedad);
        editor.putString("sucursal", sucursal);
        editor.apply();
    }

    /******* metodo identificadores ********/
    private void Identificadores(){
        spinner_sucursal = (Spinner) findViewById(R.id.spinner_sucursal);
        spinner_local = (Spinner) findViewById(R.id.spinner_sociedad);
        boton_ingresar= (ImageButton) findViewById(R.id.boton_ingresar);
    }

    /******* metodo para mostrar actividad ********/
    private void NuevaActividad(){

    }

    /******* metodo para setear sucural y sociedad ********/
    private void setPreferencias(){
    }
}
