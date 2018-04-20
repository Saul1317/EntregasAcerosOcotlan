package com.acerosocotlan.entregasacerosocotlan.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.controlador.ActivityEntregas;
import com.acerosocotlan.entregasacerosocotlan.controlador.FormularioActivity;
import com.acerosocotlan.entregasacerosocotlan.controlador.ScrollingRutasActivity;
import com.acerosocotlan.entregasacerosocotlan.modelo.ConvertidorFecha;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.RutaCamion_retrofit;

import java.util.List;

/**
 * Created by Saul on 27/02/2018.
 */

public class AdapterRecyclerViewRutaCamion extends RecyclerView.Adapter<AdapterRecyclerViewRutaCamion.RutasAdapterRecyclerHolder> {

    private int resource;
    private List<RutaCamion_retrofit> rutasArrayList;
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private Context context;

    public AdapterRecyclerViewRutaCamion(List<RutaCamion_retrofit> rutasArrayList, int resource, Activity activity, Context context) {
        this.rutasArrayList = rutasArrayList;
        this.resource = resource;
        this.activity = activity;
        this.context = context;
        sharedPreferences = activity.getSharedPreferences("Login", Context.MODE_PRIVATE);
    }
    @Override
    public RutasAdapterRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent,false);
        return new RutasAdapterRecyclerHolder(view);
    }
    @Override
    public void onBindViewHolder(RutasAdapterRecyclerHolder holder, int position) {
        final RutaCamion_retrofit rutascamionInstancia = rutasArrayList.get(position);
        String fechahora = rutascamionInstancia.getProgramadaPara();
        Log.i("FECHA",fechahora);
        ConvertidorFecha confecha= new ConvertidorFecha();
        String fecha = confecha.ConvertirFecha(fechahora);
        holder.fecha_programada.setText(fecha);
        holder.numero_folio.setText(rutascamionInstancia.getIdRuta());
        holder.numero_entrega.setText(rutascamionInstancia.getNumEntregas());
        holder.cardViewRutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rutascamionInstancia.getFechaInicio().toString().isEmpty()){
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setTitle("Aviso de confirmación");
                    alert.setMessage("Esta a punto de iniciar una ruta, ¿Desea continuar?");
                    alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int whichButton) {
                            MetodosSharedPreference.GuardarRuta(sharedPreferences, rutascamionInstancia.getIdRuta().toString());
                            Intent intent = new Intent(activity, FormularioActivity.class);
                            activity.startActivity(intent);
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.show();
                }
                else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setTitle("Aviso de confirmación");
                    alert.setMessage("Esta ruta ya fue inicializada, ¿Desea continuar?");
                    alert.setPositiveButton("Entendido", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int whichButton) {
                            MetodosSharedPreference.GuardarRuta(sharedPreferences, rutascamionInstancia.getIdRuta().toString());
                            Intent intent = new Intent(activity, ActivityEntregas.class);
                            activity.startActivity(intent);
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.show();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return rutasArrayList.size();
    }
    public class RutasAdapterRecyclerHolder extends RecyclerView.ViewHolder{
        private TextView numero_entrega, fecha_programada, numero_folio;
        private CardView cardViewRutas;

        public RutasAdapterRecyclerHolder(View itemView) {
            super(itemView);
            fecha_programada = (TextView) itemView.findViewById(R.id.txt_fecha_programada);
            numero_folio = (TextView) itemView.findViewById(R.id.txt_numero_folio_ruta);
            numero_entrega = (TextView) itemView.findViewById(R.id.txt_numero_entrega);
            cardViewRutas = (CardView) itemView.findViewById(R.id.cardview_rutas);
        }
    }
}
