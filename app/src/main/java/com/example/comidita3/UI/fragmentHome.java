package com.example.comidita3.UI;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.comidita3.Interfaz;
import com.example.comidita3.R;
import com.example.comidita3.adaptadores.PopularAdapters;
import com.example.comidita3.clasesPOJO.Receta;
import com.example.comidita3.clasesPOJO.RecetaValorizada;
import com.example.comidita3.clasesPOJO.comparatorPopulares;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentHome extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    Interfaz contexto;

    //Lista recetas populares

    List<Receta> listRecetasPopulares;
    RecyclerView recetasPopulares;
    RecyclerView recetasRecomendadas;
    PopularAdapters adaptadoPopulares;
    ArrayList<RecetaValorizada> valorizadas;
    comparatorPopulares c;


    //firebase

    FirebaseFirestore db;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        contexto = (Interfaz)context;

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentHome newInstance(String param1, String param2) {
        fragmentHome fragment = new fragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //contexto.loadDataFavoritos();
        return inflater.inflate(R.layout.fragment_home, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contexto.loadDataFavoritos();
        valorizadas = new ArrayList<>();


        recetasPopulares = view.findViewById(R.id.recyclerViewPopulares);
        recetasPopulares.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        listRecetasPopulares = new ArrayList<>();
        adaptadoPopulares = new PopularAdapters(getActivity(),listRecetasPopulares);
        recetasPopulares.setAdapter(adaptadoPopulares);

        obtenerPopulares();





        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    // Toast.makeText(getContext(),"hey",Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }


    public void obtenerPopulares(){

        db = FirebaseFirestore.getInstance();

        db.collection("recetas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {



                            for (QueryDocumentSnapshot document : task.getResult()) {

                                float valor = 0;

                                Map<String,String> map = (HashMap<String, String>) document.get("votaciones");
                                Integer numVotaciones = map.size();

                                for (Map.Entry<String, String> entry : map.entrySet()) {
                                    //System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
                                    valor = valor + Float.parseFloat(entry.getValue());
                                }

                                float fin = valor/map.size();

                                RecetaValorizada recetaValorizada = new RecetaValorizada(document.getId(),fin,numVotaciones);
                                valorizadas.add(recetaValorizada);






                            }

                            ordenarPopulares();

                        } else {

                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                        }
                    }
                });



















    }

    public void ordenarPopulares() {

        Collections.sort(valorizadas,c);

        if(valorizadas.size()>10){

            for(int i = 0;i<10;i++) {
                Toast.makeText(getContext(), "nombre " +valorizadas.get(i).getId() + " valor " + valorizadas.get(i).getNumVotaciones(), Toast.LENGTH_SHORT).show();

                int finalI = i;
                db.collection("recetas")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        if(document.getId().equals(valorizadas.get(finalI).getId())) {

                                            Receta receta = document.toObject(Receta.class);
                                            listRecetasPopulares.add(receta);
                                            adaptadoPopulares.notifyDataSetChanged();

                                        }

                                    }
                                } else {

                                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });



            }

        }

        else{

            for(int i = 0;i<valorizadas.size();i++) {
                Toast.makeText(getContext(), "nombre " +valorizadas.get(i).getId() + " valor " + valorizadas.get(i).getNumVotaciones(), Toast.LENGTH_SHORT).show();

                int finalI = i;
                db.collection("recetas")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        if(document.getId().equals(valorizadas.get(finalI).getId())) {

                                            Receta receta = document.toObject(Receta.class);
                                            listRecetasPopulares.add(receta);
                                            adaptadoPopulares.notifyDataSetChanged();

                                        }

                                    }
                                } else {

                                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });



            }

        }


    }



}