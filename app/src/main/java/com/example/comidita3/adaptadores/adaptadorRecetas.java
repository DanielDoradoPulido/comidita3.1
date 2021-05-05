package com.example.comidita3.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.comidita3.R;
import com.example.comidita3.clasesPOJO.Receta;

import java.util.List;

public class adaptadorRecetas extends ArrayAdapter<Receta> {

    Context context;
    int direccionLayout;
    List<Receta> lista;
    TextView nombre,facilidad;
    ImageView foto;


    public adaptadorRecetas(@NonNull Context context, int resource, @NonNull List<Receta> objects) {
        super(context, resource, objects);

        this.context = context;
        this.direccionLayout = resource;
        this.lista = objects;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = LayoutInflater.from(context).inflate(direccionLayout, parent, false);

        Receta r = lista.get(position);

        nombre = (TextView) v.findViewById(R.id.textViewNombreAdaptadorSubidas);
        facilidad = (TextView) v.findViewById(R.id.textViewFacilidadAdaptadorSubidas);
        foto = (ImageView) v.findViewById(R.id.imageViewFotoAdaptadorSubidas);







        return v;



    }
}
