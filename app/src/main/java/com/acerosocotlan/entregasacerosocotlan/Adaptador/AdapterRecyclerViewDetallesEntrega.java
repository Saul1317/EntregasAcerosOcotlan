package com.acerosocotlan.entregasacerosocotlan.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.Detalles_entregas_retrofit;

import java.util.List;

public class AdapterRecyclerViewDetallesEntrega extends RecyclerView.Adapter<AdapterRecyclerViewDetallesEntrega.EntregasDetallesAdapterRecyclerHolder> {

    private int resource;
    private List<Detalles_entregas_retrofit> detallesEntregaArrayList;
    private Activity activity;
    private Context context;
    private Detalles_entregas_retrofit detallesEntregascamionInstancia;

    public AdapterRecyclerViewDetallesEntrega(int resource, List<Detalles_entregas_retrofit> detallesEntregaArrayList, Activity activity, Context context) {
        this.resource = resource;
        this.detallesEntregaArrayList = detallesEntregaArrayList;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public EntregasDetallesAdapterRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent,false);
        return new AdapterRecyclerViewDetallesEntrega.EntregasDetallesAdapterRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntregasDetallesAdapterRecyclerHolder holder, int position) {
        detallesEntregascamionInstancia = detallesEntregaArrayList.get(position);
        holder.txt_detalle_material.setText(detallesEntregascamionInstancia.getMaterial());
        holder.txt_cantidad_material.setText(detallesEntregascamionInstancia.getPiezas());
        holder.txt_unidad_material.setText(detallesEntregascamionInstancia.getUnidad());
    }

    @Override
    public int getItemCount() {
        return detallesEntregaArrayList.size();
    }

    public class EntregasDetallesAdapterRecyclerHolder extends RecyclerView.ViewHolder{
        private TextView txt_detalle_material,txt_cantidad_material,txt_unidad_material;

        public EntregasDetallesAdapterRecyclerHolder(View itemView) {
            super(itemView);
            txt_detalle_material = (TextView) itemView.findViewById(R.id.txt_detalle_material);
            txt_cantidad_material = (TextView) itemView.findViewById(R.id.txt_cantidad_material);
            txt_unidad_material = (TextView) itemView.findViewById(R.id.txt_unidad_material);
        }
    }
}
