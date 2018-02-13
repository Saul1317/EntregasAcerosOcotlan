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
import com.acerosocotlan.entregasacerosocotlan.modelo.Camion;
import com.acerosocotlan.entregasacerosocotlan.modelo.Choferes;

import java.security.AccessController;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView choferesRecycler = (RecyclerView) findViewById(R.id.choferes_recycler);
        LinearLayoutManager l = new LinearLayoutManager(getApplicationContext());
        l.setOrientation(LinearLayoutManager.VERTICAL);
        choferesRecycler.setLayoutManager(l);
        AdapterRecyclerView arv = new AdapterRecyclerView(cargarArrayList(),R.layout.cardview_choferes, MainActivity.this);
        choferesRecycler.setAdapter(arv);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public ArrayList<Camion> cargarArrayList(){
        ArrayList<Camion> listacamion = new ArrayList<>();
        listacamion.add(new Camion(
                "C://Users/Saul/Desktop/usuario_ejemplo.jpg",
                "Saúl Alejandro",
                "C://Users/Saul/Desktop/camion_prueba.jpg",
                "",
                "",
                "",
                "",
                ""));
        listacamion.add(new Camion(
                "C://Users/Saul/Desktop/usuario_ejemplo.jpg",
                "Miguel Angel",
                "C://Users/Saul/Desktop/camion_prueba.jpg",
                "",
                "",
                "",
                "",
                ""));
        listacamion.add(new Camion(
                "C://Users/Saul/Desktop/usuario_ejemplo.jpg",
                "Pepe",
                "C://Users/Saul/Desktop/camion_prueba.jpg",
                "",
                "",
                "",
                "",
                ""));
        listacamion.add(new Camion(
                "C://Users/Saul/Desktop/usuario_ejemplo.jpg",
                "Lalo",
                "C://Users/Saul/Desktop/camion_prueba.jpg",
                "",
                "",
                "",
                "",
                ""));
        listacamion.add(new Camion(
                "C://Users/Saul/Desktop/usuario_ejemplo.jpg",
                "Daniel",
                "C://Users/Saul/Desktop/camion_prueba.jpg",
                "",
                "",
                "",
                "",
                ""));
        return listacamion;
    }
}
