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
import android.widget.TextView;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.controlador.DescargaEntregaActivity;
import com.acerosocotlan.entregasacerosocotlan.controlador.FormularioActivity;
import com.acerosocotlan.entregasacerosocotlan.modelo.EntregasCamion_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.RutaCamion_retrofit;

import java.util.List;

/**
 * Created by Saul on 28/02/2018.
 */

public class AdapterRecyclerViewEntregaCamion extends RecyclerView.Adapter<AdapterRecyclerViewEntregaCamion.EntregasAdapterRecyclerHolder>{

    private int resource;
    private List<EntregasCamion_retrofit> entregaArrayList;
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private Context context;

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
    public void onBindViewHolder(EntregasAdapterRecyclerHolder holder, int position) {
        EntregasCamion_retrofit entregascamionInstancia = entregaArrayList.get(position);
        holder.folio_entregas.setText(entregascamionInstancia.getFolioEntrega().toString());
        holder.sucursal.setText(entregascamionInstancia.getSucursal().toString());
        holder.entrega.setText(entregascamionInstancia.getEntrega().toString());
        holder.folio_ruta.setText(entregascamionInstancia.getFolioRuta().toString());
        holder.fecha_inicio.setText(entregascamionInstancia.getFechaInicio().toString());
        holder.fecha_llegada.setText(entregascamionInstancia.getFechaLlegada().toString());
        holder.fecha_salida.setText(entregascamionInstancia.getFechaSalida().toString());
        holder.comentario.setText(entregascamionInstancia.getComentarios().toString());
        holder.estatus.setText(entregascamionInstancia.getEstatus().toString());
        holder.kg_total.setText(entregascamionInstancia.getKgTotal().toString());
        holder.pedido.setText(entregascamionInstancia.getPedido().toString());
        holder.cardViewEntregas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DescargaEntregaActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return entregaArrayList.size();
    }

    public class EntregasAdapterRecyclerHolder extends RecyclerView.ViewHolder{
        private TextView folio_entregas, sucursal, folio_ruta, entrega, fecha_inicio, fecha_llegada, fecha_salida, comentario, estatus, kg_total, pedido;
        private CardView cardViewEntregas;

        public EntregasAdapterRecyclerHolder(View itemView) {
            super(itemView);
            folio_entregas = (TextView) itemView.findViewById(R.id.txt_entregas_folio_entrega);
            sucursal = (TextView) itemView.findViewById(R.id.txt_entregas_sucursal);
            folio_ruta = (TextView) itemView.findViewById(R.id.txt_entregas_FolioRuta);
            entrega = (TextView) itemView.findViewById(R.id.txt_entregas_Entrega);
            fecha_inicio = (TextView) itemView.findViewById(R.id.txt_entregas_FechaInicio);
            fecha_llegada = (TextView) itemView.findViewById(R.id.txt_entregas_FechaLlegada);
            fecha_salida = (TextView) itemView.findViewById(R.id.txt_entregas_FechaSalida);
            comentario = (TextView) itemView.findViewById(R.id.txt_entregas_comentarios);
            estatus = (TextView) itemView.findViewById(R.id.txt_entregas_Estatus);
            kg_total = (TextView) itemView.findViewById(R.id.txt_entregas_KgTotal);
            pedido = (TextView) itemView.findViewById(R.id.txt_entregas_Pedido);
            cardViewEntregas = (CardView) itemView.findViewById(R.id.cardview_entregas);
        }
    }
}
