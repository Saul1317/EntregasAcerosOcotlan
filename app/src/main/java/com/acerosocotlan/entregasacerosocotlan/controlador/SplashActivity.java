package com.acerosocotlan.entregasacerosocotlan.controlador;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.nio.channels.Selector;

/**
 * Created by Saul on 08/02/2018.
 */

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences prs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentLogin = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intentLogin);
    }
}