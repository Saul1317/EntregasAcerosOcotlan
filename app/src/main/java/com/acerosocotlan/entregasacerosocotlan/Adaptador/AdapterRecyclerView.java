package com.acerosocotlan.entregasacerosocotlan.Adaptador;

import android.app.Activity;
import android.graphics.Picture;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.Choferes;

import java.util.ArrayList;

/**
 * Created by Saul on 12/02/2018.
 */

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.AdapterRecyclerHolder>{

    private ArrayList<Choferes> choferesArrayList;
    private int resource;
    private Activity activity;

    public AdapterRecyclerView(ArrayList<Choferes> choferesArrayList, int resource, Activity activity) {
        this.choferesArrayList = choferesArrayList;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public AdapterRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent,false);
        return new AdapterRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterRecyclerHolder holder, int position) {
        Choferes chf = choferesArrayList.get(position);
        holder.nombre_chofer_Cardview.setText(chf.getNombre());
    }

    @Override
    public int getItemCount() {
        return choferesArrayList.size();
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
