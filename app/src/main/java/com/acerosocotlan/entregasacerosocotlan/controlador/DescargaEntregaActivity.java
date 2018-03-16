package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.R;

public class DescargaEntregaActivity extends AppCompatActivity {

    ImageButton btn_descarga_camion, btn_finalizacion_camion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga_entrega);
        Inicializador();
        btn_descarga_camion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogoConfirmacion();
            }
        });
        btn_finalizacion_camion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NuevaActividad();
            }
        });
    }

    public void Inicializador(){
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
                Toast.makeText(DescargaEntregaActivity.this, "ENVIAR DATOS DE LLEGADA", Toast.LENGTH_SHORT).show();
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

}
