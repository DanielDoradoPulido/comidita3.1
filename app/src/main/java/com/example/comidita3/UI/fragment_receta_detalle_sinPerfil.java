package com.example.comidita3.UI;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.comidita3.Interfaz;
import com.example.comidita3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_receta_detalle_sinPerfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_receta_detalle_sinPerfil extends Fragment {

    private static final String ARG_ID = "id";
    private static final String ARG_NOMBRE = "nombre";
    private static final String ARG_INGREDIENTES = "ingredientes";
    private static final String ARG_DESCRIPCION = "descripcion";
    private static final String ARG_URLYOUTUBE = "urlYoutube";
    private static final String ARG_USERPATH = "userPath";
    private static final String ARG_IMAGEPATH = "imagePath";
    private static final String ARG_VALORACION = "valoracion";
    private static final String ARG_VISITAS = "visitas";

    // TODO: Rename and change types of parameters
    private String id,nombre,ingredientes,descripcion,urlYoutube,userpath,imagepath,valoracion,visitas;

    //Objetos clase
    TextView name,descript,ingredients,facilities;
    ImageView imagen;

    RatingBar ratingBar;
    ImageButton fav;
    View urlY,perfilUser;
    Interfaz contexto;
    NavController navController;
    Boolean enFav = false;


    //firestorage
    private FirebaseStorage storage;
    private StorageReference storageReference;

    //firebase authentication
    private FirebaseAuth mAuth;



    public fragment_receta_detalle_sinPerfil() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contexto = (Interfaz)context;
    }

    public static fragment_recetaDetalle newInstance(String param1, String param2, String param3, String param4, String param5, String param6, String param7, String param8, String param9) {

        fragment_recetaDetalle fragment = new fragment_recetaDetalle();

        Bundle args = new Bundle();

        args.putString(ARG_ID, param1);
        args.putString(ARG_NOMBRE, param2);
        args.putString(ARG_INGREDIENTES, param3);
        args.putString(ARG_DESCRIPCION, param4);
        args.putString(ARG_URLYOUTUBE, param5);
        args.putString(ARG_USERPATH, param6);
        args.putString(ARG_IMAGEPATH, param7);
        args.putString(ARG_VALORACION, param8);
        args.putString(ARG_VISITAS, param9);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);




        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
            nombre = getArguments().getString(ARG_NOMBRE);
            ingredientes = getArguments().getString(ARG_INGREDIENTES);
            descripcion = getArguments().getString(ARG_DESCRIPCION);
            urlYoutube = getArguments().getString(ARG_URLYOUTUBE);
            userpath = getArguments().getString(ARG_USERPATH);
            imagepath = getArguments().getString(ARG_IMAGEPATH);
            valoracion = getArguments().getString(ARG_VALORACION);
            visitas = getArguments().getString(ARG_VISITAS);


        }



        contexto.loadDataSubidasOther(userpath);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receta_detalle_sin_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.textViewNombreDetalleReceta);
        name.setText(nombre);



        mAuth = FirebaseAuth.getInstance();

        ingredients = view.findViewById(R.id.textViewIngredientesDetalleReceta);
        ingredients.setText(ingredientes);

        descript = view.findViewById(R.id.textViewPasosDetalleReceta);
        descript.setText(descripcion);

        urlY = view.findViewById(R.id.viewURLyoutube);
        urlY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"has pulsado el boton de ver video en youtube",Toast.LENGTH_SHORT).show();
                try {

                    //Toast.makeText(getContext(),urlYoutube,Toast.LENGTH_SHORT).show();
                    contexto.abrirURL(urlYoutube);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getContext(),"Lo siento la url está corrupta...",Toast.LENGTH_SHORT).show();
                }
            }
        });

        facilities = view.findViewById(R.id.textViewFacilidadDetalleReceta);
        facilities.setText("Rapido de hacer");

        fav = view.findViewById(R.id.imageButtonFavoritosDetalleReceta);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"Has pulsado el boton fav",Toast.LENGTH_SHORT).show();
                operarFav();
            }
        });




        ratingBar = view.findViewById(R.id.ratingBarDetalleReceta);

        imagen = view.findViewById(R.id.imageViewDetalleReceta);


        storageReference.child(imagepath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Glide.with(getContext()).load(uri).into(imagen);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        comprobarFav();

    }

    public void comprobarFav(){

        //comprobamos si el usuario contiene en su coleccion de favoritas el ID de la receta

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals(mAuth.getUid())) {

                                    ArrayList<String> favs = (ArrayList)document.get("favoritas");

                                    if(favs.contains(id)){
                                        //Toast.makeText(getContext(),"Lo contiene",Toast.LENGTH_SHORT).show();
                                        fav.setImageResource(R.drawable.ic_baseline_favorite_24);
                                        enFav = true;


                                    }
                                    else{
                                        //Toast.makeText(getContext(),"no lo contiene",Toast.LENGTH_SHORT).show();

                                    }




                                }


                            }
                        } else {

                            Toast.makeText(getContext(),"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });




    }

    public void operarFav(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(enFav){



            db.collection("usuarios")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {

                                    if(document.getId().equals(mAuth.getUid())) {

                                        ArrayList<String>  favs = (ArrayList)document.get("favoritas");

                                        favs.remove(id);

                                        db.collection("usuarios").document(document.getId()).update("favoritas",favs);

                                        enFav = false;

                                        fav.setImageResource(R.drawable.ic_baseline_favorite_border_24);

                                        contexto.loadDataFavoritos();



                                    }


                                }
                            } else {

                                Toast.makeText(getContext(),"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



        }
        else{

            db.collection("usuarios")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {

                                    if(document.getId().equals(mAuth.getUid())) {

                                        ArrayList<String>  favs = (ArrayList)document.get("favoritas");

                                        favs.add(id);

                                        db.collection("usuarios").document(document.getId()).update("favoritas",favs);

                                        enFav = true;

                                        fav.setImageResource(R.drawable.ic_baseline_favorite_24);

                                        contexto.loadDataFavoritos();



                                    }


                                }
                            } else {

                                Toast.makeText(getContext(),"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });













        }



        //metodo load




    }
}