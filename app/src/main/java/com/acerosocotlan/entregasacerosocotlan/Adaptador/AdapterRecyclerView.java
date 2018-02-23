package com.acerosocotlan.entregasacerosocotlan.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.controlador.ScrollingRutasActivity;
import com.acerosocotlan.entregasacerosocotlan.modelo.Camion_retrofit;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Saul on 12/02/2018.
 */

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.AdapterRecyclerHolder>{

    private List<Camion_retrofit> camionArrayList;
    private int resource;
    private Activity activity;
    private Context context;

    public AdapterRecyclerView(List<Camion_retrofit> camionArrayList, int resource, Activity activity, Context context) {
        this.camionArrayList = camionArrayList;
        this.resource = resource;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public AdapterRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent,false);
        return new AdapterRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterRecyclerHolder holder, int position) {
        final Camion_retrofit camionInstancia = camionArrayList.get(position);
        holder.nombre_chofer_Cardview.setText(camionInstancia.getNombre());
        Picasso.with(context).load("https://api.learn2crack.com/android/images/donut.png").fit().into(holder.foto_fondo_Cardview);
        //Picasso.with(context).load("https://api.learn2crack.com/android/images/froyo.png").fit().into(holder.foto_perfil_Cardview);
        holder.foto_fondo_Cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,camionInstancia.getNombre(),Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(activity, ScrollingRutasActivity.class);
                //activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return camionArrayList.size();
    }

    public class AdapterRecyclerHolder extends RecyclerView.ViewHolder{

        private ImageView foto_fondo_Cardview, foto_perfil_Cardview;
        private TextView nombre_chofer_Cardview ;

        public AdapterRecyclerHolder(View itemView) {
            super(itemView);
            foto_fondo_Cardview =(ImageView) itemView.findViewById(R.id.foto_fondo_chofer);
            foto_perfil_Cardview =(ImageView) itemView.findViewById(R.id.foto_perfil_chofer);
            nombre_chofer_Cardview = (TextView) itemView.findViewById(R.id.txt_nombre_chofer);
        }
    }
}
