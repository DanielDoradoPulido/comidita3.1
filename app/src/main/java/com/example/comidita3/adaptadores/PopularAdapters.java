package com.example.comidita3.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.comidita3.R;
import com.example.comidita3.clasesPOJO.Receta;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopularAdapters extends RecyclerView.Adapter<PopularAdapters.ViewHolder> {

    private Context context;
    private List<Receta> listRecetas;
    private int valor=1;

    public PopularAdapters(Context context, List<Receta> listRecetas) {
        this.context = context;
        this.listRecetas = listRecetas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

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

        else if(valor==5){
            holder.animationView.setAnimation(R.raw.vegetales);
            valor++;
        }
        else{
            holder.animationView.setAnimation(R.raw.recipe2);
            valor=1;
        }
        holder.nombre.setText(listRecetas.get(position).getNombre());
        holder.dificultad.setText(listRecetas.get(position).getDificultad());


    }

    @Override
    public int getItemCount() {
        return listRecetas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LottieAnimationView animationView;
        TextView nombre,dificultad,rate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            animationView = itemView.findViewById(R.id.animationPopulares);

            nombre = itemView.findViewById(R.id.textViewNombreItem);

            dificultad = itemView.findViewById(R.id.textViewDificultadItem);



        }
    }
}
