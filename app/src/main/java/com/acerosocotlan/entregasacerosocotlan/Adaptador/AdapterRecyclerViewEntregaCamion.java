package com.acerosocotlan.entregasacerosocotlan.Adaptador;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.controlador.ActivityEntregas;
import com.acerosocotlan.entregasacerosocotlan.controlador.DescargaEntregaActivity;
import com.acerosocotlan.entregasacerosocotlan.controlador.ErrorConexion;
import com.acerosocotlan.entregasacerosocotlan.controlador.EvidenciasActivity;
import com.acerosocotlan.entregasacerosocotlan.controlador.FinalizarRutaActivity;
import com.acerosocotlan.entregasacerosocotlan.controlador.FormularioActivity;
import com.acerosocotlan.entregasacerosocotlan.modelo.EntregasCamion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.Localizacion;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;
import com.acerosocotlan.entregasacerosocotlan.modelo.RutaCamion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.ValidacionConexion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by Saul on 28/02/2018.
 */

public class AdapterRecyclerViewEntregaCamion extends RecyclerView.Adapter<AdapterRecyclerViewEntregaCamion.EntregasAdapterRecyclerHolder>{

    private int resource;
    private List<EntregasCamion_retrofit> entregaArrayList;
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private Context context;
    private EntregasCamion_retrofit entregascamionInstancia;
    private int posicion;
    boolean validacion = false;
    private LocationManager locationManager;
    private Localizacion localizacion;
    private ProgressDialog progressDoalog;
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Calendar calendar;

    public AdapterRecyclerViewEntregaCamion(List<EntregasCamion_retrofit> entregaArrayList , int resource, Activity activity, Context context) {
        this.resource = resource;
        this.entregaArrayList = entregaArrayList;
        this.activity = activity;
        this.context = context;
        sharedPreferences = activity.getSharedPreferences("Login", Context.MODE_PRIVATE);
    }
    @Override
    public EntregasAdapterRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent,false);
        return new EntregasAdapterRecyclerHolder(view);
    }
    @Override
    public void onBindViewHolder(EntregasAdapterRecyclerHolder holder, final int position) {
        entregascamionInstancia = entregaArrayList.get(position);
        if (entregascamionInstancia.getEstatus().toString().equals("Proximo")
                ||entregascamionInstancia.getEstatus().toString().equals("Descargando")
                ||entregascamionInstancia.getEstatus().toString().equals("En sitio")){
            holder.linearLayout_entregas.setBackgroundColor(Color.parseColor("#FBBC05"));
            validacion= true;
            posicion= position;
        }
        holder.folio_entregas.setText(entregascamionInstancia.getFolioEntrega());
        holder.entrega.setText(entregascamionInstancia.getEntrega());
        holder.txt_cliente.setText(entregascamionInstancia.getNomcliente());
        holder.txt_direccion.setText(entregascamionInstancia.getDireccion());
        holder.cardViewEntregas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validacion==true){
                    if(entregaArrayList.get(position).getEstatus().equals("En Ruta")||entregaArrayList.get(position).getEstatus().equals("En Ruta")){
                        Toast.makeText(activity, "No se puede iniciar otra entrega", Toast.LENGTH_SHORT).show();
                    }else{
                        //METODOS SHARED PREFERENCE
                        //Log.i("FOLIO ALMACENADO",entregaArrayList.get(position).getFolioEntrega());
                        Log.i("FOLIO QUE SE ALMACENARA",entregaArrayList.get(position).getFolioEntrega());
                        MetodosSharedPreference.GuardarFolioEntrega(sharedPreferences, entregaArrayList.get(position).getFolioEntrega());
                        MetodosSharedPreference.GuardarFechasEntrega(sharedPreferences, entregaArrayList.get(position).getFechaLlegada());
                        MetodosSharedPreference.GuardarEstatusEntrega(sharedPreferences, entregaArrayList.get(position).getEstatus());
                        DialogoConfirmacionContinuarEntrega();
                    }
                }else{
                    Log.i("FOLIO QUE SE ALMACENARA",entregaArrayList.get(position).getFolioEntrega());
                    MetodosSharedPreference.GuardarFolioEntrega(sharedPreferences, entregaArrayList.get(position).getFolioEntrega());
                    MetodosSharedPreference.GuardarFechasEntrega(sharedPreferences, entregaArrayList.get(position).getFechaLlegada());
                    MetodosSharedPreference.GuardarEstatusEntrega(sharedPreferences, entregaArrayList.get(position).getEstatus());
                    DialogoConfirmacionComenzarEntrega();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return entregaArrayList.size();
    }
    public class EntregasAdapterRecyclerHolder extends RecyclerView.ViewHolder{
        private TextView folio_entregas, entrega, txt_cliente, txt_direccion;
        private CardView cardViewEntregas;
        private LinearLayout linearLayout_entregas;

        public EntregasAdapterRecyclerHolder(View itemView) {
            super(itemView);
            folio_entregas = (TextView) itemView.findViewById(R.id.txt_entregas_folio_entrega);
            entrega = (TextView) itemView.findViewById(R.id.txt_entregas_Entrega);
            txt_cliente = (TextView) itemView.findViewById(R.id.txt_entregas_cliente);
            txt_direccion = (TextView) itemView.findViewById(R.id.txt_entregas_direccion);
            cardViewEntregas = (CardView) itemView.findViewById(R.id.cardview_entregas);
            linearLayout_entregas= (LinearLayout) itemView.findViewById(R.id.linear_layout_entregas_cardview);
        }
    }
    private void DialogoConfirmacionContinuarEntrega() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage("La entrega "+MetodosSharedPreference.ObtenerFolioEntregaPref(sharedPreferences)+" ya fue iniciada, ¿Desea continuar?");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent i = new Intent(context, DescargaEntregaActivity.class);
                activity.startActivity(i);

            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }
    private void DialogoConfirmacionComenzarEntrega() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage("Esta a punto de comenzar la entrega "+ MetodosSharedPreference.ObtenerFolioEntregaPref(sharedPreferences)+", ¿Desea continuar?");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                progressDoalog = new ProgressDialog(activity);
                progressDoalog.setMessage("Preparando los datos");
                progressDoalog.setCancelable(false);
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();
                localizacion = new Localizacion(context);
                localizacion.ObtenerMejorLocalizacion();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        localizacion.cancelarLocalizacion();
                        InsercionDatosEntrega();
                    }
                },4000);
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }
    public String ObtenerFecha(){
        calendar = Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime()).toString();
    }
    public void InsercionDatosEntrega(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                progressDoalog.dismiss();
                ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},100);
            }else{
                Log.i("FOLIO QUE SE ALMACENARA",MetodosSharedPreference.ObtenerFolioEntregaPref(sharedPreferences));
                InsertarFormulario(MetodosSharedPreference.ObtenerFolioEntregaPref(sharedPreferences), String.valueOf(localizacion.getLatitude()),
                        String.valueOf(localizacion.getLongitud()), progressDoalog, MetodosSharedPreference.getSociedadPref(sharedPreferences));
            }
        }else {
            InsertarFormulario(MetodosSharedPreference.ObtenerFolioEntregaPref(sharedPreferences), String.valueOf(localizacion.getLatitude()),
                    String.valueOf(localizacion.getLongitud()), progressDoalog, MetodosSharedPreference.getSociedadPref(sharedPreferences));
        }
    }
    public void InsertarFormulario(String folio2, String latitud, String longitud, final ProgressDialog progressDialog, String sociedad){
        Log.i("LOCALIZACION ALMACENADA",latitud+" "+longitud);
        Log.i("SOCIEDAD",sociedad);
        Log.i("FECHA QUE SE ALMACENARA",ObtenerFecha());
        Log.i("FOLIO",folio2);

        Call<List<String>> call = NetworkAdapter.getApiService(MetodosSharedPreference.ObtenerPruebaEntregaPref(sharedPreferences)).IniciaEntrega(
                "iniciarentrega_"+folio2+"_inicio/"+sociedad, ObtenerFecha(), latitud, longitud);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    List<String> respuesta = response.body();
                    String valor = respuesta.get(0);
                    Log.i("RESPUESTA ENTREGA",valor);
                    Toast.makeText(context,"Entrega inicida", Toast.LENGTH_LONG).show();
                    abrirDescargas(context);
                }else{
                    Log.i("RESPUESTA ENTREGA","No entiende respuesta");
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("RESPUESTA ENTREGA",t.toString());
                MostrarDialogCustomNoConfiguracion();
            }
        });
    }
    private void MostrarDialogCustomNoConfiguracion(){
        AlertDialog.Builder alert = new AlertDialog.Builder(activity, R.style.DialogErrorConexion);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.activity_error_conexion, null);
        alert.setCancelable(false);
        alert.setView(dialoglayout);
        final AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogErrorConexion;
        alertDialog.show();
        final FloatingActionButton botonEntendido = (FloatingActionButton) dialoglayout.findViewById(R.id.fab_recargar_app);
        botonEntendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidacionConexion.isConnectedWifi(context)||ValidacionConexion.isConnectedMobile(context)){
                    if(ValidacionConexion.isOnline(context)){
                        alertDialog.dismiss();
                    }else{
                        Toast.makeText(context, "No tienes acceso a internet", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Esta apagado tu WIFI", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void abrirDescargas(Context context){
        Intent i = new Intent(context, DescargaEntregaActivity.class);
        activity.startActivity(i);
    }
}
