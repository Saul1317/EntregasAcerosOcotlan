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
import com.acerosocotlan.entregasacerosocotlan.controlador.DetallesEntregas;
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
    boolean validacion = false;

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
        //Validacion es para identificar si hay una entrega iniciada
        if (entregascamionInstancia.getEstatus().equals("Proximo") ||entregascamionInstancia.getEstatus().equals("Descargando") ||entregascamionInstancia.getEstatus().equals("En sitio")){
            holder.linearLayout_entregas.setBackgroundColor(Color.parseColor("#FBBC05"));
            validacion= true;
        }

        holder.folio_entregas.setText(entregascamionInstancia.getFolioEntrega());
        holder.entrega.setText(entregascamionInstancia.getEntrega());
        holder.txt_cliente.setText(entregascamionInstancia.getNomcliente());
        holder.txt_direccion.setText(entregascamionInstancia.getDireccion());
        holder.txt_comentario_entrega.setText(entregascamionInstancia.getCoentrega());
        holder.cardViewEntregas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Si validacion es igual a true entonces significa que si hay una entrega iniciada
                if (validacion==true){
                    //Si se selecciona una entrega que este en ruta despues de confirmar que ya hay una entrega iniciada entonces la entrega no hara nada
                    if(entregaArrayList.get(position).getEstatus().equals("En Ruta") || entregaArrayList.get(position).getEstatus().equals("Programado")){
                        Toast.makeText(activity, "No se puede iniciar otra entrega", Toast.LENGTH_SHORT).show();
                    }
                    //Si se selecciona una entrega que este iniciada entonces almacenara la informacion y podra continuar la entrega
                    else{
                        MetodosSharedPreference.GuardarFolioEntrega(sharedPreferences, entregaArrayList.get(position).getFolioEntrega());
                        MetodosSharedPreference.GuardarEntregaPedido(sharedPreferences, entregaArrayList.get(position).getEntrega());
                        MetodosSharedPreference.GuardarEstatusEntrega(sharedPreferences, entregaArrayList.get(position).getEstatus());
                        MetodosSharedPreference.GuardarClienteEntrega(sharedPreferences, entregaArrayList.get(position).getNomcliente());
                        MetodosSharedPreference.GuardarDireccionEntrega(sharedPreferences, entregaArrayList.get(position).getDireccion());
                        MetodosSharedPreference.GuardarComentarioEntrega(sharedPreferences, entregaArrayList.get(position).getCoentrega());
                        MetodosSharedPreference.GuardarFechasEntrega(sharedPreferences, entregaArrayList.get(position).getFechaLlegada());
                        DialogoConfirmacionContinuarEntrega();
                    }
                }
                //Si la validacion sale negativa entonces no hay ninguna entrega iniciada eso significa que se debera almacenar la informacion de la entrega para mostrarla en la siguiente ventana.
                else{
                        MetodosSharedPreference.GuardarFolioEntrega(sharedPreferences, entregaArrayList.get(position).getFolioEntrega());
                        MetodosSharedPreference.GuardarEntregaPedido(sharedPreferences, entregaArrayList.get(position).getEntrega());
                        MetodosSharedPreference.GuardarEstatusEntrega(sharedPreferences, entregaArrayList.get(position).getEstatus());
                        MetodosSharedPreference.GuardarClienteEntrega(sharedPreferences, entregaArrayList.get(position).getNomcliente());
                        MetodosSharedPreference.GuardarDireccionEntrega(sharedPreferences, entregaArrayList.get(position).getDireccion());
                        MetodosSharedPreference.GuardarComentarioEntrega(sharedPreferences, entregaArrayList.get(position).getCoentrega());
                        MetodosSharedPreference.GuardarFechasEntrega(sharedPreferences, entregaArrayList.get(position).getFechaLlegada());
                        Intent i = new Intent(context, DetallesEntregas.class);
                        activity.startActivity(i);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return entregaArrayList.size();
    }
    public class EntregasAdapterRecyclerHolder extends RecyclerView.ViewHolder{
        private TextView folio_entregas, entrega, txt_cliente, txt_direccion,txt_comentario_entrega;
        private CardView cardViewEntregas;
        private LinearLayout linearLayout_entregas;

        public EntregasAdapterRecyclerHolder(View itemView) {
            super(itemView);
            folio_entregas = (TextView) itemView.findViewById(R.id.txt_entregas_folio_entrega);
            txt_comentario_entrega = (TextView) itemView.findViewById(R.id.txt_entregas_comentario);
            entrega = (TextView) itemView.findViewById(R.id.txt_entregas_Entrega);
            txt_cliente = (TextView) itemView.findViewById(R.id.txt_entregas_cliente);
            txt_direccion = (TextView) itemView.findViewById(R.id.txt_entregas_direccion);
            cardViewEntregas = (CardView) itemView.findViewById(R.id.cardview_entregas);
            linearLayout_entregas= (LinearLayout) itemView.findViewById(R.id.linear_layout_entregas_cardview);
        }
    }

    private void DialogoConfirmacionContinuarEntrega() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage("La entrega "+MetodosSharedPreference.ObtenerFolioEntregaPref(sharedPreferences)+" ya fue iniciada, ¿Desea continuar?");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent i = new Intent(context, DetallesEntregas.class);
                activity.startActivity(i);

            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
    private void DialogoConfirmacionComenzarEntrega() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage("Esta a punto de comenzar la entrega "+ MetodosSharedPreference.ObtenerFolioEntregaPref(sharedPreferences)+", ¿Desea continuar?");
        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent i = new Intent(context, DetallesEntregas.class);
                activity.startActivity(i);
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
