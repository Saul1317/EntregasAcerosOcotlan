package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.acerosocotlan.entregasacerosocotlan.Adaptador.AdapterRecyclerView;
import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.Choferes;

import java.security.AccessController;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public ArrayList<Choferes> cargarArrayList(){
        ArrayList<Choferes> listachoferes = new ArrayList<>();
        listachoferes.add(new Choferes(
                "C://Users/Saul/Desktop/usuario_ejemplo.jpg",
                "C://Users/Saul/Desktop/camion_prueba.jpg",
                "Saúl Alejandro Delgado Mayorga"));
        return listachoferes;
    }
}
