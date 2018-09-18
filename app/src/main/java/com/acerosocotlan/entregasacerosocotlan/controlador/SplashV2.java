package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;

public class SplashV2 extends AppCompatActivity {
    private SharedPreferences prs;
    private Animation nubesAnimacion, carroAnimacion, textoAnimacion;
    private LinearLayout textoLayout;
    private FrameLayout nubesLayout, carroLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_v2);
        Inicializador();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CargarActivity();
            }
        },4000);
    }
    public void CargarActivity(){
        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        Intent intentSelector = new Intent(SplashV2.this,SelectorActivity.class);
        Intent intentRutas = new Intent(SplashV2.this, ScrollingRutasActivity.class);
        Intent intentPrueba = new Intent(SplashV2.this, DetallesRutas.class);
        if (TextUtils.isEmpty(MetodosSharedPreference.getSucursalPref(prs)) || TextUtils.isEmpty(MetodosSharedPreference.ObtenerPlacasPref(prs))){
            startActivity(intentSelector);
        }else{
            startActivity(intentRutas);
        }
        finish();
    }
    public void Inicializador(){
        nubesLayout= (FrameLayout) findViewById(R.id.nubes_splash);
        carroLayout= (FrameLayout) findViewById(R.id.carro_splash);
        textoLayout= (LinearLayout) findViewById(R.id.texto_splash);
        nubesAnimacion = AnimationUtils.loadAnimation(this,R.anim.nubes_splash);
        nubesLayout.setAnimation(nubesAnimacion);
        carroAnimacion = AnimationUtils.loadAnimation(this,R.anim.carro_splash);
        carroLayout.setAnimation(carroAnimacion);
        textoAnimacion = AnimationUtils.loadAnimation(this,R.anim.texto_splash);
        textoLayout.setAnimation(textoAnimacion);
    }
}
