package com.example.comidita3.adaptadores;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.comidita3.Objetos.Receta;
import com.example.comidita3.R;

import java.util.List;

public class recomendadasAdapter extends RecyclerView.Adapter<recomendadasAdapter.ViewHolder> {

    private Context context;
    private List<Receta> listRecetas;
    private int valor=1;
    NavController navController;

    public recomendadasAdapter(Context context, List<Receta> listRecetas,NavController navController) {
        this.context = context;
        this.listRecetas = listRecetas;
        this.navController = navController;
    }

    @NonNull
    @Override
    public recomendadasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new recomendadasAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recomendadas_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull recomendadasAdapter.ViewHolder holder, int position) {

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
        holder.nombre.setText(listRecetas.get(position).getNombre());
        holder.dificultad.setText(listRecetas.get(position).getDificultad());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                bundle.putString("id",listRecetas.get(position).getId());
                bundle.putString("nombre",listRecetas.get(position).getNombre());
                bundle.putString("ingredientes", listRecetas.get(position).getIngredientes());
                bundle.putString("dificultad",listRecetas.get(position).getDificultad());
                bundle.putString("descripcion", listRecetas.get(position).getDescripcion());
                bundle.putString("urlYoutube", listRecetas.get(position).getUrlYoutube());
                bundle.putString("userPath", listRecetas.get(position).getUserPath());
                bundle.putString("imagePath", listRecetas.get(position).getImagePath());
                bundle.putString("valoracion", listRecetas.get(position).getValoracion());
                bundle.putString("visitas", listRecetas.get(position).getVisitas());

                navController.navigate(R.id.fragment_recetaDetalle,bundle);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listRecetas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LottieAnimationView animationView;
        TextView nombre,dificultad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            animationView = itemView.findViewById(R.id.animationRecomendadas);

            nombre = itemView.findViewById(R.id.textViewNombreItemRecomendadas);

            dificultad = itemView.findViewById(R.id.textViewDificultadItemRecomendadas);



        }


    }
}