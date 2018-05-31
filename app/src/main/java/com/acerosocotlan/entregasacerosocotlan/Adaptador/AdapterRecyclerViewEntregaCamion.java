package com.acerosocotlan.entregasacerosocotlan.Adaptador;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
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
import com.acerosocotlan.entregasacerosocotlan.controlador.FinalizarRutaActivity;
import com.acerosocotlan.entregasacerosocotlan.controlador.FormularioActivity;
import com.acerosocotlan.entregasacerosocotlan.modelo.EntregasCamion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.Localizacion;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.NetworkAdapter;
import com.acerosocotlan.entregasacerosocotlan.modelo.RutaCamion_retrofit;

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
    private Localizacion localizacionInstancia = new Localizacion();
    private EntregasCamion_retrofit entregascamionInstancia;
    private int posicion;
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
        if (entregascamionInstancia.getEstatus().toString().equals("Proximo")
                ||entregascamionInstancia.getEstatus().toString().equals("Descargando")
                ||entregascamionInstancia.getEstatus().toString().equals("En sitio")){
            holder.linearLayout_entregas.setBackgroundColor(Color.parseColor("#FFD600"));
            validacion= true;
            posicion= position;
        }
        holder.folio_entregas.setText(entregascamionInstancia.getFolioEntrega().toString());
        holder.entrega.setText(entregascamionInstancia.getEntrega().toString());
        holder.txt_cliente.setText(entregascamionInstancia.getNomcliente().toString());
        holder.txt_direccion.setText(entregascamionInstancia.getDireccion().toString());
        holder.cardViewEntregas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validacion==true){
                    if(entregaArrayList.get(position).getEstatus().equals("En Ruta")||entregaArrayList.get(position).getEstatus().equals("En Ruta")){

                    }else{
                        //METODOS SHARED PREFERENCE
                        //Log.i("FOLIO QUE SE ALMACENARA",entregaArrayList.get(position).getFolioEntrega());
                        //Log.i("FOLIO ALMACENADO",entregaArrayList.get(position).getFolioEntrega());
                        MetodosSharedPreference.GuardarFolioEntrega(sharedPreferences, entregaArrayList.get(position).getFolioEntrega());
                        MetodosSharedPreference.GuardarFechasEntrega(sharedPreferences, entregaArrayList.get(position).getFechaLlegada());
                        MetodosSharedPreference.GuardarEstatusEntrega(sharedPreferences, entregaArrayList.get(position).getEstatus());
                        DialogoConfirmacionContinuarEntrega();
                    }
                 }else{
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
    public void DialogodeConfirmacionInsercion(){
        //Codigo de confirmación
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},100);
            }else{
                //INSTANCIAS ACTIVIDADES
                ActivityEntregas activityEntregasInstancia = new ActivityEntregas();
                //METODO PARA INSERTAR
                activityEntregasInstancia.InsertarFormulario(MetodosSharedPreference.ObtenerFolioEntregaPref(sharedPreferences),
                        localizacionInstancia.ObtenerLatitud(context),
                        localizacionInstancia.ObtenerLongitud(context));
                //Log.i("FOLIO UTILIZADO",entregascamionInstancia.getFolioEntrega());
                //activityEntregasInstancia.ObtenerAvisoPersonal(MetodosSharedPreference.ObtenerFolioEntregaPref(sharedPreferences));
                Intent i = new Intent(context, DescargaEntregaActivity.class);
                activity.startActivity(i);
            }
        }else {
            //INSTANCIAS ACTIVIDADES
            ActivityEntregas activityEntregasInstancia = new ActivityEntregas();
            //METODO PARA INSERTAR
            activityEntregasInstancia.InsertarFormulario(MetodosSharedPreference.ObtenerFolioEntregaPref(sharedPreferences),
                    localizacionInstancia.ObtenerLatitud(context),
                    localizacionInstancia.ObtenerLongitud(context));
            //Log.i("FOLIO UTILIZADO",entregascamionInstancia.getFolioEntrega());
            activityEntregasInstancia.ObtenerAvisoPersonal(MetodosSharedPreference.ObtenerFolioEntregaPref(sharedPreferences));
            Intent i = new Intent(context, DescargaEntregaActivity.class);
            activity.startActivity(i);
        }
    }
    private void DialogoConfirmacionContinuarEntrega() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Aviso de confirmación");
        alert.setMessage("Esta entrega ya fue iniciada, ¿Desea continuar?");

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
        alert.setTitle("Aviso de confirmación");
        alert.setMessage("Esta a punto de comenzar esta entrega, ¿Desea continuar?");

        alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                DialogodeConfirmacionInsercion();
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }
}
