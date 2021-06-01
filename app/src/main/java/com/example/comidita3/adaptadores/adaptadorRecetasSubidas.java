package com.example.comidita3.adaptadores;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.comidita3.R;
import com.example.comidita3.clasesPOJO.Receta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class adaptadorRecetasSubidas extends ArrayAdapter<Receta> {

    Context context;
    int direccionLayout;
    List<Receta> lista;
    String id;
    TextView nombre,facilidad,valoracionTotal;
    CircleImageView foto;
    private FirebaseStorage storage;
    private StorageReference storageReference;




    public adaptadorRecetasSubidas(@NonNull Context context, int resource, @NonNull List<Receta> objects) {
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

        id = r.getId();

        nombre = (TextView) v.findViewById(R.id.textViewNombreAdaptadorSubidas);
        nombre.setText(r.getNombre());

        facilidad = (TextView) v.findViewById(R.id.textViewFacilidadAdaptadorSubidas);
        facilidad.setText(r.getDificultad());

        valoracionTotal = v.findViewById(R.id.textViewValoracionGloballAdaptador);
        calculoValor();






        String ruta = r.getImagePath();




        return v;



    }

    public void calculoValor(){

        String valor = "0";

        //buscamos su map de valoraciones

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("recetas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals(id)) {

                                    //Guardamos el valor de su map

                                    Map<String,String> users = (HashMap)document.get("votaciones");

                                    //iteramos el map para ir sumando sus puntos

                                    float puntos  = 0;

                                    for (String value : users.values()) {
                                        //System.out.println("Value = " + value);

                                        Float valorPos = Float.parseFloat(value);
                                        puntos = puntos + valorPos;
                                    }

                                    float division = puntos / users.size();

                                    String finali = String.valueOf(division);

                                    valoracionTotal.setText(finali);


                                    // Toast.makeText(getContext(),"Puntos: " + puntos +" numero " + users.size() +" puntuacion " + finali,Toast.LENGTH_SHORT).show();



                                }


                            }
                        } else {

                            Toast.makeText(getContext(),"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });






    }



}
