package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.acerosocotlan.entregasacerosocotlan.R;

public class SinEntregasActivity extends AppCompatActivity {

    private Button btn_terminar_ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sin_entregas);
        InicializadorView();
        btn_terminar_ruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SinEntregasActivity.this, FinalizarRutaActivity.class);
                startActivity(i);
            }
        });
    }

    private void InicializadorView() {
        btn_terminar_ruta = (Button) findViewById(R.id.btn_terminar_ruta);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SinEntregasActivity.this, ScrollingRutasActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
