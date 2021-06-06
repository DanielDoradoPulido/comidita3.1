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

import android.text.method.ScrollingMovementMethod;
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
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_receta_detalle_sinPerfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_receta_detalle_sinPerfil extends Fragment {

    private static final String ARG_ID = "id";
    private static final String ARG_NOMBRE = "nombre";
    private static final String ARG_INGREDIENTES = "ingredientes";
    private static final String ARG_DIFICULTAD = "dificultad";
    private static final String ARG_DESCRIPCION = "descripcion";
    private static final String ARG_URLYOUTUBE = "urlYoutube";
    private static final String ARG_USERPATH = "userPath";
    private static final String ARG_IMAGEPATH = "imagePath";
    private static final String ARG_VALORACION = "valoracion";
    private static final String ARG_VISITAS = "visitas";

    // TODO: Rename and change types of parameters
    private String id,nombre,ingredientes,descripcion,urlYoutube,userpath,imagepath,valoracion,visitas,dificultad;

    //Objetos clase
    TextView name,descript,ingredients,facilities,valoracionTotal;
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

    public static fragment_recetaDetalle newInstance(String param1, String param2, String param3, String param4, String param5, String param6, String param7, String param8, String param9,String param10) {

        fragment_recetaDetalle fragment = new fragment_recetaDetalle();

        Bundle args = new Bundle();

        args.putString(ARG_ID, param1);
        args.putString(ARG_NOMBRE, param2);
        args.putString(ARG_INGREDIENTES, param3);
        args.putString(ARG_DIFICULTAD,param4);
        args.putString(ARG_DESCRIPCION, param5);
        args.putString(ARG_URLYOUTUBE, param6);
        args.putString(ARG_USERPATH, param7);
        args.putString(ARG_IMAGEPATH, param8);
        args.putString(ARG_VALORACION, param9);
        args.putString(ARG_VISITAS, param10);

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
            dificultad = getArguments().getString(ARG_DIFICULTAD);
            descripcion = getArguments().getString(ARG_DESCRIPCION);
            urlYoutube = getArguments().getString(ARG_URLYOUTUBE);
            userpath = getArguments().getString(ARG_USERPATH);
            imagepath = getArguments().getString(ARG_IMAGEPATH);
            valoracion = getArguments().getString(ARG_VALORACION);
            visitas = getArguments().getString(ARG_VISITAS);


        }



        //contexto.loadDataSubidasOther(userpath);


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

        comprobarVotado();
        sumarVisita();



        ratingBar = view.findViewById(R.id.ratingBarDetalleRecetaSinPerfil);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                //llamamos al metodo operarVotado el numero de la votacion
                operarVotado(rating);



                //Toast.makeText(getContext(),String.valueOf(rating),Toast.LENGTH_SHORT).show();

            }
        });

        valoracionTotal = view.findViewById(R.id.textViewValoracionGlobalSinPerfil);
        calculoValor();

        mAuth = FirebaseAuth.getInstance();

        ingredients = view.findViewById(R.id.textViewIngredientesDetalleReceta);
        ingredients.setText(ingredientes);
        ingredients.setMovementMethod(new ScrollingMovementMethod());

        descript = view.findViewById(R.id.textViewPasosDetalleCreador);
        descript.setText(descripcion);
        descript.setMovementMethod(new ScrollingMovementMethod());

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
        facilities.setText(dificultad);

        fav = view.findViewById(R.id.imageButtonFavoritosDetalleReceta);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"Has pulsado el boton fav",Toast.LENGTH_SHORT).show();
                operarFav();
            }
        });




        ratingBar = view.findViewById(R.id.ratingBarDetalleRecetaSinPerfil);

        imagen = view.findViewById(R.id.imageViewDetalleReceta);


        storageReference.child(imagepath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                try{
                    Glide.with(getContext()).load(uri).into(imagen);
                }catch (Exception e){

                }
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

    public void comprobarVotado(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("recetas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals(id)) {

                                    Map<String,String> users = (HashMap)document.get("votaciones");

                                    if(users.containsKey(mAuth.getUid())){

                                        Float votado = Float.parseFloat(users.get(mAuth.getUid()));

                                        ratingBar.setRating(votado);

                                    }

                                }


                            }
                        } else {

                            Toast.makeText(getContext(),"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void operarVotado(float rating){

        //tranformamos el float a String para añadirlo

        String value = String.valueOf(rating);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("recetas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals(id)) {

                                    //Toast.makeText(getContext(),"entro",Toast.LENGTH_SHORT).show();

                                    Map<String,String>  users = (HashMap)document.get("votaciones");

                                    if(users.isEmpty() ){


                                        users.put(mAuth.getUid(),value);

                                    }
                                    else{

                                        if(users.containsKey(mAuth.getUid())){
                                            users.remove(mAuth.getUid());
                                            users.put(mAuth.getUid(),value);
                                        }
                                        else
                                            users.put(mAuth.getUid(),value);

                                    }

                                    db.collection("recetas").document(document.getId()).update("votaciones",users);

                                    //Toast.makeText(getContext(),"votaste",Toast.LENGTH_SHORT).show();

                                    //accion de refrescar puntuacion media



                                }


                            }
                        } else {

                            Toast.makeText(getContext(),"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });



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

                                    Map<String,String>  users = (HashMap)document.get("votaciones");

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
    public void sumarVisita(){

        String s = visitas;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        int sumado = Integer.parseInt(visitas) + 1;

        //actualizamos la bbdd

        db.collection("recetas").document(id).update("visitas",String.valueOf(sumado));



    }
}