package com.acerosocotlan.entregasacerosocotlan.modelo;

import android.content.SharedPreferences;

/**
 * Created by Saul on 23/01/2018.
 */

public class MetodosSharedPreference {
    public static String getSociedadPref(SharedPreferences prs){
        return prs.getString("sociedad",null);
    }
    public static String getSucursalPref(SharedPreferences prs){
        return prs.getString("sucursal",null);
    }
    public static String getcamionPref(SharedPreferences prs){
        return prs.getString("camion",null);
    }

    public static void setcamionPref(SharedPreferences prs, int camion){
        SharedPreferences.Editor editor = prs.edit();
        editor.putInt("camion", camion);
        editor.apply();
    }
}
