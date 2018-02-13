package com.acerosocotlan.entregasacerosocotlan.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.acerosocotlan.entregasacerosocotlan.R;

/**
 * Created by Saul on 13/02/2018.
 */

public class Spinner_Adaptador extends BaseAdapter {
    Context context;
    String[] content;
    LayoutInflater inflter;

    public Spinner_Adaptador(Context applicationContext, String[] content) {
        this.context = applicationContext;
        this.content = content;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return content.length;
    }

    @Override
    public Object getItem(int i) {
        return content[i].toString();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        TextView names = (TextView) view.findViewById(R.id.spinner_nombre_sucursal);
        names.setText(content[i]);
        return view;
    }
}