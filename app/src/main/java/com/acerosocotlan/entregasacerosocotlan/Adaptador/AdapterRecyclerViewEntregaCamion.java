package com.acerosocotlan.entregasacerosocotlan.Adaptador;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private ActivityEntregas a;
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
        final EntregasCamion_retrofit entregascamionInstancia = entregaArrayList.get(position);
        holder.folio_entregas.setText(entregascamionInstancia.getFolioEntrega().toString());
        holder.entrega.setText(entregascamionInstancia.getEntrega().toString());
        holder.fecha_llegada.setText(entregascamionInstancia.getFechaLlegada().toString());
        holder.fecha_salida.setText(entregascamionInstancia.getFechaSalida().toString());
        holder.cardViewEntregas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetodosSharedPreference.GuardarFolioEntrega(sharedPreferences, entregascamionInstancia.getFolioEntrega().toString());
                ActivityEntregas activityEntregasInstancia = new ActivityEntregas();
                Localizacion localizacionInstancia = new Localizacion();
                activityEntregasInstancia.InsertarFormulario(
                        MetodosSharedPreference.ObtenerFolioEntregaPref(sharedPreferences),
                        localizacionInstancia.ObtenerLatitud(activity, context),
                        localizacionInstancia.ObtenerLongitud(activity, context));
                Intent i = new Intent(context, DescargaEntregaActivity.class);
                activity.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return entregaArrayList.size();
    }

    public class EntregasAdapterRecyclerHolder extends RecyclerView.ViewHolder{
        private TextView folio_entregas, entrega, fecha_llegada, fecha_salida;
        private CardView cardViewEntregas;

        public EntregasAdapterRecyclerHolder(View itemView) {
            super(itemView);
            folio_entregas = (TextView) itemView.findViewById(R.id.txt_entregas_folio_entrega);
            entrega = (TextView) itemView.findViewById(R.id.txt_entregas_Entrega);
            fecha_llegada = (TextView) itemView.findViewById(R.id.txt_entregas_FechaLlegada);
            fecha_salida = (TextView) itemView.findViewById(R.id.txt_entregas_FechaSalida);
            cardViewEntregas = (CardView) itemView.findViewById(R.id.cardview_entregas);
        }
    }
}
