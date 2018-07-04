package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.ValidacionConexion;

public class ErrorConexion extends AppCompatActivity {

    private TextView txt_error_conexion;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_conexion);
        Inicializador();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecargarApp();
            }
        });
    }
    private void RecargarApp() {
        if(ValidacionConexion.isConnectedWifi(getApplicationContext())||ValidacionConexion.isConnectedMobile(getApplicationContext())){
            if(ValidacionConexion.isOnline(getApplicationContext())){
                Intent i = new Intent(ErrorConexion.this, ScrollingRutasActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }else{
                Toast.makeText(ErrorConexion.this, "No tienes acceso a internet", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(ErrorConexion.this, "Esta apagado tu WIFI", Toast.LENGTH_SHORT).show();
        }
    }
    private void Inicializador() {
        txt_error_conexion= (TextView) findViewById(R.id.txt_error_conexion);
        fab = (FloatingActionButton) findViewById(R.id.fab_recargar_app);
    }
}
