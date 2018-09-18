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
    public static String ObtenerEstatusEntregaPref(SharedPreferences prs){
        return prs.getString("estatus_entrega",null);
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

    public static void GuardarClienteEntrega(SharedPreferences prs,String folio){
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("cliente_entrega", folio);
        editor.apply();
    }

    public static String ObtenerClienteEntregaPref(SharedPreferences prs){
        return prs.getString("cliente_entrega",null);
    }

    public static void GuardarDireccionEntrega(SharedPreferences prs,String folio){
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("direccion_entrega", folio);
        editor.apply();
    }

    public static void GuardarComentarioEntrega(SharedPreferences prs,String folio){
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("comentario_entrega", folio);
        editor.apply();
    }

    public static String ObtenerComentarioEntregaPref(SharedPreferences prs){
        return prs.getString("comentario_entrega",null);
    }

    public static String ObtenerDireccionEntregaPref(SharedPreferences prs){
        return prs.getString("direccion_entrega",null);
    }

    public static void GuardarEntregaPedido(SharedPreferences prs,String folio){
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("entrega_pedido", folio);
        editor.apply();
    }

    public static String ObtenerEntregaPedido(SharedPreferences prs){
        return prs.getString("entrega_pedido",null);
    }

    public static void GuardarFechasEntrega(SharedPreferences prs,String llegada){
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("fecha_entrega_llegada", llegada);
        editor.remove("KeyName").commit();
        editor.apply();
    }

    public static void GuardarEstatusEntrega(SharedPreferences prs,String estatus){
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("estatus_entrega", estatus);
        editor.apply();
    }

    public static void BorrarFolioEntrega(SharedPreferences prs){
        SharedPreferences.Editor editor = prs.edit();
        editor.remove("folio_entrega").commit();
        editor.apply();
    }
    public static void GuardarPruebaEntrega(SharedPreferences prs,String estado){
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("estado_entrega", estado);
        editor.apply();
    }

    public static String ObtenerPruebaEntregaPref(SharedPreferences prs){
        return prs.getString("estado_entrega",null);
    }

    public static void GuardarFechaProgramada(SharedPreferences prs,String llegada){
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("fecha_programada_ruta", llegada);
        editor.apply();
    }
    public static String ObtenerFechaProgramada(SharedPreferences prs) {
        return prs.getString("fecha_programada_ruta",null);
    }

    public static void GuardarNumEntregas(SharedPreferences prs, String numEntregas) {
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("num_entregas", numEntregas);
        editor.apply();
    }
    public static String ObtenerNumEntregas(SharedPreferences prs) {
        return prs.getString("num_entregas",null);
    }

    public static void GuardarFechaInicioRuta(SharedPreferences prs, String fechaInicio) {
        SharedPreferences.Editor editor = prs.edit();
        editor.putString("fecha_inicio_ruta", fechaInicio);
        editor.apply();
    }

    public static String ObtenerFechaInicioRuta(SharedPreferences prs) {
        return prs.getString("fecha_inicio_ruta",null);
    }
}
