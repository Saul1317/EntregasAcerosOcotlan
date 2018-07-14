package com.acerosocotlan.entregasacerosocotlan.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.controlador.DescargaEntregaActivity;
import com.acerosocotlan.entregasacerosocotlan.controlador.ScrollingRutasActivity;
import com.acerosocotlan.entregasacerosocotlan.modelo.Camion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.MetodosSharedPreference;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Saul on 12/02/2018.
 */

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.AdapterRecyclerHolder>{

    private List<Camion_retrofit> camionArrayList;
    private SharedPreferences sharedPreferences;
    private int resource;
    private Activity activity;
    private Context context;

    public AdapterRecyclerView(List<Camion_retrofit> camionArrayList, int resource, Activity activity, Context context) {
        this.camionArrayList = camionArrayList;
        this.resource = resource;
        this.activity = activity;
        this.context = context;
        sharedPreferences = activity.getSharedPreferences("Login", Context.MODE_PRIVATE);
    }

    @Override
    public AdapterRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent,false);
        return new AdapterRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterRecyclerHolder holder, final int position) {
        final Camion_retrofit camionInstancia = camionArrayList.get(position);
        holder.nombre_chofer_Cardview.setText(camionInstancia.getNombre()+" "+camionInstancia.getApellidoPaterno());
        holder.modelo_camion_carview.setText(camionInstancia.getDescripcion());
        holder.placas_camion_carview.setText("Placas "+camionInstancia.getPlacas());
        Picasso.with(context).load(camionInstancia.getFotoCamion()).fit().into(holder.foto_fondo_Cardview);
        Picasso.with(context).load(camionInstancia.getFotoChofer()).fit().placeholder(R.drawable.obrero).into(holder.foto_perfil_Cardview);
        holder.foto_fondo_Cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodosSharedPreference.GuardarUsuarioCamion(sharedPreferences,
                        camionInstancia.getPlacas().toString(),
                        camionInstancia.getNombre().toString(),
                        camionInstancia.getApellidoPaterno().toString(),
                        camionInstancia.getFotoChofer().toString(),
                        camionInstancia.getClaveChofer().toString(),
                        camionInstancia.getPesoUnidad().toString(),
                        camionInstancia.getPesoMaximo().toString());
                Intent intent = new Intent(activity, ScrollingRutasActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return camionArrayList.size();
    }
    public class AdapterRecyclerHolder extends RecyclerView.ViewHolder{

        private ImageView foto_fondo_Cardview, foto_perfil_Cardview;
        private TextView nombre_chofer_Cardview, modelo_camion_carview, placas_camion_carview ;

        public AdapterRecyclerHolder(View itemView) {
            super(itemView);
            foto_fondo_Cardview =(ImageView) itemView.findViewById(R.id.foto_fondo_chofer);
            foto_perfil_Cardview =(ImageView) itemView.findViewById(R.id.foto_perfil_chofer);
            nombre_chofer_Cardview = (TextView) itemView.findViewById(R.id.txt_nombre_chofer);
            modelo_camion_carview = (TextView) itemView.findViewById(R.id.txt_modelo_camion);
            placas_camion_carview = (TextView) itemView.findViewById(R.id.txt_placas_camion);
        }
    }
}
