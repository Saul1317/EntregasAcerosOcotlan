package com.acerosocotlan.entregasacerosocotlan.modelo;

import android.content.SharedPreferences;
import android.widget.Toast;

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
    public static int getNumCamionPref(SharedPreferences prs){
        return prs.getInt("num_camion",0);
    }

    public static String ObtenerPlacasPref(SharedPreferences prs){
        return prs.getString("placas_camion",null);
    }
    public static String ObtenerNombrePref(SharedPreferences prs){
        return prs.getString("nombre_camion",null);
    }
    public static String ObtenerApellidoPref(SharedPreferences prs){
        return prs.getString("apellido_camion",null);
    }
    public static String ObtenerFotoPref(SharedPreferences prs){
        return prs.getString("foto_camion",null);
    }
    public static String ObtenerClaveChoferPref(SharedPreferences prs){
        return prs.getString("clave_chofer_camion",null);
    }
    public static String ObtenerPesoCamionChoferPref(SharedPreferences prs){
        return prs.getString("peso_camion",null);
    }
    public static String ObtenerPesoMaximoCamionPref(SharedPreferences prs){
        return prs.getString("peso_maximo_camion",null);
    }
    public static String ObtenerFolioRutaPref(SharedPreferences prs){
        return prs.getString("folio_ruta",null);
    }

    public static String ObtenerFolioEntregaPref(SharedPreferences prs){
        return prs.getString("folio_entrega",null);
    }

    public static String ObtenerFechaLlegadaPref(SharedPreferences prs){
        return prs.getString("fecha_entrega_llegada",null);
    }

    public static void GuardarUsuarioCamion(SharedPreferences prs,String placas, String nombre,
                                           String apellido, String foto, String clave_chofer, String peso, String peso_maximo){
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("placas_camion", placas);
        editor.putString("nombre_camion", nombre);
        editor.putString("apellido_camion", apellido);
        editor.putString("foto_camion", foto);
        editor.putString("clave_chofer_camion", clave_chofer);
        editor.putString("peso_camion", peso);
        editor.putString("peso_maximo_camion", peso_maximo);
        editor.apply();
    }

    public static void GuardarRuta(SharedPreferences prs,String folio){
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("folio_ruta", folio);
        editor.apply();
    }

    public static void GuardarFolioEntrega(SharedPreferences prs,String folio){
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("folio_entrega", folio);
        editor.apply();
    }

    public static void GuardarFechasEntrega(SharedPreferences prs,String llegada){
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("fecha_entrega_llegada", llegada);
        editor.apply();
    }
}
