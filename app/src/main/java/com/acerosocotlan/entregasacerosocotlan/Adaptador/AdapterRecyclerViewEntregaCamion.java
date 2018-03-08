package com.acerosocotlan.entregasacerosocotlan.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acerosocotlan.entregasacerosocotlan.R;
import com.acerosocotlan.entregasacerosocotlan.modelo.RutaCamion_retrofit;

import java.util.List;

/**
 * Created by Saul on 28/02/2018.
 */

public class AdapterRecyclerViewEntregaCamion extends RecyclerView.Adapter<AdapterRecyclerViewEntregaCamion.EntregasAdapterRecyclerHolder>{

    private int resource;
    private List<RutaCamion_retrofit> entregasArrayList;
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private Context context;

    @Override
    public EntregasAdapterRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(EntregasAdapterRecyclerHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class EntregasAdapterRecyclerHolder extends RecyclerView.ViewHolder{
        private TextView numero_entrega, fecha_salida;
        private CardView cardViewRutas;

        public EntregasAdapterRecyclerHolder(View itemView) {
            super(itemView);
            numero_entrega = (TextView) itemView.findViewById(R.id.txt_numero_entrega);
            cardViewRutas = (CardView) itemView.findViewById(R.id.cardview_rutas);
        }
    }
}
