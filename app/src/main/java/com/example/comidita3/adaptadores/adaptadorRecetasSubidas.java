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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class adaptadorRecetasSubidas extends ArrayAdapter<Receta> {

    Context context;
    int direccionLayout;
    List<Receta> lista;
    TextView nombre,facilidad;
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

        nombre = (TextView) v.findViewById(R.id.textViewNombreAdaptadorSubidas);
        nombre.setText(r.getNombre());

        facilidad = (TextView) v.findViewById(R.id.textViewFacilidadAdaptadorSubidas);
        facilidad.setText(r.getDificultad());

        foto = v.findViewById(R.id.imageViewFotoAdaptadorSubidas);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        String ruta = r.getImagePath();

        storageReference.child(ruta).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(getContext()).load(uri).into(foto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(),"error al cargar image",Toast.LENGTH_SHORT).show();
            }
        });


        return v;



    }



}
