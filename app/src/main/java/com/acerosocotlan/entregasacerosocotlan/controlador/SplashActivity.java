package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;

import java.nio.channels.Selector;

/**
 * Created by Saul on 08/02/2018.
 */

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences prs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        Intent intentSelector = new Intent(SplashActivity.this, SelectorActivity.class);
        Intent intentRutas = new Intent(SplashActivity.this, ScrollingRutasActivity.class);

        if (!TextUtils.isEmpty(MetodosSharedPreference.getSociedadPref(prs))
                && !TextUtils.isEmpty(MetodosSharedPreference.getSociedadPref(prs))
                && !TextUtils.isEmpty(MetodosSharedPreference.getcamionPref(prs)) ){
            startActivity(intentRutas);
        }else{
            startActivity(intentSelector);
        }
        finish();
    }
}