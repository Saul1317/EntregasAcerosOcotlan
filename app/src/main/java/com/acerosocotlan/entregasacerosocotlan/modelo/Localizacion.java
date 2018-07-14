package com.acerosocotlan.entregasacerosocotlan.modelo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.controlador.ActivityEntregas;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by Saul on 21/03/2018.
 */

public class Localizacion {
    //DATOS EXTERNOS
    private Location location;
    private LocationManager locationManager;
    double longitudeBest, latitudeBest;
    private Context context;

    public Localizacion(Context context) {
        this.context = context;
    }
    public double getLatitude() {
        return latitudeBest;
    }
    public double getLongitud() {
        return longitudeBest;
    }

    public void ObtenerMejorLocalizacion(){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 0, 0, LocalizacionListener);
         }
    }
    public void  cancelarLocalizacion(){
        Log.i("LOCALIZACION","FINAL: "+"LATITUD: "+latitudeBest+"LONGITUD: "+longitudeBest);
        //Toast.makeText(context,"LATITUD: "+latitudeBest+"LONGITUD: "+longitudeBest, Toast.LENGTH_LONG).show();
        locationManager.removeUpdates(LocalizacionListener);
    }
    private final LocationListener LocalizacionListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeBest = location.getLongitude();
            latitudeBest = location.getLatitude();
            locationManager.removeUpdates(LocalizacionListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
            longitudeBest = 0;
            latitudeBest = 0;
            locationManager.removeUpdates(LocalizacionListener);
        }
    };
}
