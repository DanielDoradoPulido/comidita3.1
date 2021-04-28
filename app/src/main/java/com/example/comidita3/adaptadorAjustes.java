package com.example.comidita3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class adaptadorAjustes extends ArrayAdapter<String> {

    Context context;
    int direccionLayout;
    List<String> lista;

    TextView opcion,relleno;


    public adaptadorAjustes(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);

        this.context = context;
        this.direccionLayout = resource;
        this.lista = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = LayoutInflater.from(context).inflate(direccionLayout,parent,false);


        relleno = v.findViewById(R.id.textViewRellenoAjustes);
        opcion = (TextView) v.findViewById(R.id.textViewItemAjustes);
        opcion.setText(lista.get(position));






        return v;



    }


}
