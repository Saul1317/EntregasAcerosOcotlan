package com.acerosocotlan.entregasacerosocotlan.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.controlador.EntregaActivity;
import com.acerosocotlan.entregasacerosocotlan.controlador.ScrollingRutasActivity;
import com.acerosocotlan.entregasacerosocotlan.modelo.Camion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.acerosocotlan.entregasacerosocotlan.modelo.RutaCamion_retrofit;
import com.squareup.picasso.Picasso;

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
        RutaCamion_retrofit rutascamionInstancia = rutasArrayList.get(position);
        holder.fecha_salida.setText(rutascamionInstancia.getFechaSalida());
        holder.numero_entrega.setText(rutascamionInstancia.getNumEntregas());
        holder.cardViewRutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EntregaActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rutasArrayList.size();
    }

    public class RutasAdapterRecyclerHolder extends RecyclerView.ViewHolder{
        private TextView numero_entrega, fecha_salida;
        private CardView cardViewRutas;

        public RutasAdapterRecyclerHolder(View itemView) {
            super(itemView);
            fecha_salida = (TextView) itemView.findViewById(R.id.txt_fecha_salida);
            numero_entrega = (TextView) itemView.findViewById(R.id.txt_numero_entrega);
            cardViewRutas = (CardView) itemView.findViewById(R.id.cardview_rutas);
        }
    }
}
