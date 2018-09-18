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
import com.acerosocotlan.entregasacerosocotlan.modelo.Detalles_Ruta_retrofit;
import com.acerosocotlan.entregasacerosocotlan.modelo.Detalles_entregas_retrofit;

import java.util.List;

public class AdapterRecyclerViewDetallesRutas extends RecyclerView.Adapter<AdapterRecyclerViewDetallesRutas.RutaDetallesAdapterRecyclerHolder> {

    private int resource;
    private List<Detalles_Ruta_retrofit> detalles_ruta_retrofitList;
    private Activity activity;
    private Context context;
    private Detalles_Ruta_retrofit detallesRutaRetrofitInstancia;

    public AdapterRecyclerViewDetallesRutas(int resource, List<Detalles_Ruta_retrofit> detalles_ruta_retrofitList, Activity activity, Context context) {
        this.resource = resource;
        this.detalles_ruta_retrofitList = detalles_ruta_retrofitList;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public RutaDetallesAdapterRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent,false);
        return new AdapterRecyclerViewDetallesRutas.RutaDetallesAdapterRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RutaDetallesAdapterRecyclerHolder holder, int position) {
        detallesRutaRetrofitInstancia = detalles_ruta_retrofitList.get(position);
        holder.txt_detalle_ruta_entrega.setText(detallesRutaRetrofitInstancia.getEntrega());
        holder.txt_detalle_ruta_cliente.setText(detallesRutaRetrofitInstancia.getNomcliente());
        holder.txt_detalle_ruta_direccion.setText(detallesRutaRetrofitInstancia.getDireccion());
    }

    @Override
    public int getItemCount() {
        return detalles_ruta_retrofitList.size();
    }

    public class RutaDetallesAdapterRecyclerHolder extends RecyclerView.ViewHolder{
        private TextView txt_detalle_ruta_entrega,txt_detalle_ruta_cliente, txt_detalle_ruta_direccion;

        public RutaDetallesAdapterRecyclerHolder(View itemView) {
            super(itemView);
            txt_detalle_ruta_entrega = (TextView) itemView.findViewById(R.id.txt_detalle_ruta_entrega);
            txt_detalle_ruta_cliente = (TextView) itemView.findViewById(R.id.txt_detalle_ruta_cliente);
            txt_detalle_ruta_direccion = (TextView) itemView.findViewById(R.id.txt_detalle_ruta_direccion);
        }
    }
}
