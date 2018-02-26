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
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.R;

public class ScrollingRutasActivity extends AppCompatActivity {
    private SharedPreferences prs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_rutas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_rutas);
        setSupportActionBar(toolbar);
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
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
}
