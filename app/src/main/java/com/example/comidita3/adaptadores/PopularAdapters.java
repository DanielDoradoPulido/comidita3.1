package com.example.comidita3.adaptadores;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
    NavController navController;

    public PopularAdapters(Context context, List<Receta> listRecetas,NavController navController) {
        this.context = context;
        this.listRecetas = listRecetas;
        this.navController = navController;
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

            animationView = itemView.findViewById(R.id.animationPopulares);

            nombre = itemView.findViewById(R.id.textViewNombreItem);

            dificultad = itemView.findViewById(R.id.textViewDificultadItem);



        }


    }
}
