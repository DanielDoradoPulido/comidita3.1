package com.example.comidita3.adaptadores;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.comidita3.Objetos.Receta;
import com.example.comidita3.R;

import java.util.ArrayList;
import java.util.List;

public class UploadsAdapter extends RecyclerView.Adapter<UploadsAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Receta> exampleList;
    private List<Receta> exampleListFull;
    private int valor=1;
    NavController navController;

    public UploadsAdapter(Context context, List<Receta> exampleList, NavController navController) {
        this.context = context;
        this.exampleList = exampleList;
        this.exampleListFull = exampleList;
        this.navController = navController;
    }

    @NonNull
    @Override
    public UploadsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UploadsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.uploads_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UploadsAdapter.ViewHolder holder, int position) {

        if(valor == 1){
            holder.animationView.setAnimation(R.raw.item);
            valor++;
        }
        else if(valor==2){
            holder.animationView.setAnimation(R.raw.cooking3);
            valor++;
        }
        else if(valor==3){
            holder.animationView.setAnimation(R.raw.recipe);
            valor++;
        }
        else if(valor==4){
            holder.animationView.setAnimation(R.raw.cuchillotenedor);
            valor++;
        }
        else if(valor==5){
            holder.animationView.setAnimation(R.raw.zumoensalada);
            valor++;
        }

        else if(valor==6){
            holder.animationView.setAnimation(R.raw.vegetales);
            valor++;
        }
        else if(valor==7){
            holder.animationView.setAnimation(R.raw.cooking);
            valor++;
        }
        else{
            holder.animationView.setAnimation(R.raw.recipe2);
            valor=1;
        }
        holder.nombre.setText(exampleList.get(position).getNombre());
        holder.dificultad.setText(exampleList.get(position).getDificultad());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                bundle.putString("id",exampleList.get(position).getId());
                bundle.putString("nombre",exampleList.get(position).getNombre());
                bundle.putString("ingredientes", exampleList.get(position).getIngredientes());
                bundle.putString("dificultad",exampleList.get(position).getDificultad());
                bundle.putString("descripcion", exampleList.get(position).getDescripcion());
                bundle.putString("urlYoutube", exampleList.get(position).getUrlYoutube());
                bundle.putString("userPath", exampleList.get(position).getUserPath());
                bundle.putString("imagePath", exampleList.get(position).getImagePath());
                bundle.putString("valoracion", exampleList.get(position).getValoracion());
                bundle.putString("visitas", exampleList.get(position).getVisitas());

                navController.navigate(R.id.fragment_recetaDetalle,bundle);

            }
        });
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Receta> filteredResults = null;
                if(constraint.length() == 0){
                    filteredResults = exampleListFull;
                }
                else{
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return  results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                exampleList = (List<Receta>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    protected  List<Receta> getFilteredResults(String constraint){
        List<Receta> results = new ArrayList<>();

        for(Receta r : exampleListFull){
            if(r.getNombre().toLowerCase().contains(constraint)){
                results.add(r);
            }
        }



        return results;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LottieAnimationView animationView;
        TextView nombre,dificultad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            animationView = itemView.findViewById(R.id.animationSubidas);

            nombre = itemView.findViewById(R.id.textViewNombreSubidas);

            dificultad = itemView.findViewById(R.id.textViewDificultadSubidas);



        }
    }
}
