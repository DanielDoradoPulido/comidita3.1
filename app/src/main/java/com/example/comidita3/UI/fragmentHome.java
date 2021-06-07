package com.example.comidita3.UI;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.comidita3.Interfaz;
import com.example.comidita3.Objetos.RecetaVisitada;
import com.example.comidita3.R;
import com.example.comidita3.adaptadores.PopularAdapters;
import com.example.comidita3.Objetos.Receta;
import com.example.comidita3.Objetos.RecetaValorizada;
import com.example.comidita3.adaptadores.recomendadasAdapter;
import com.example.comidita3.adaptadores.vistasAdapters;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    NavController navController;

    //Recycler view mas Populares

    List<Receta> listRecetasPopulares;
    RecyclerView recetasPopulares;
    PopularAdapters adaptadoPopulares;
    ArrayList<RecetaValorizada> valorizadas;

    //recycler View mas visitadas

    List<Receta> listRecetasVisitadas;
    RecyclerView recetasVisitadas;
    vistasAdapters adaptadorVisitada;
    ArrayList<RecetaVisitada> visitadas;

    //recycler View recomendadas

    List<Receta> listRecetasRecomendadas;
    RecyclerView recetasRecomendadas;
    recomendadasAdapter adaptadorRecomendadas;





    //firebase

    FirebaseFirestore db;

    private FirebaseAuth mAuth;



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

        mAuth = FirebaseAuth.getInstance();

        contexto.loadDataFavoritos();
        valorizadas = new ArrayList<>();
        visitadas = new ArrayList<>();

        obtenerRecomendadas();

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);





        recetasPopulares = view.findViewById(R.id.recyclerViewPopulares);
        recetasPopulares.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        listRecetasPopulares = new ArrayList<>();
        obtenerPopulares();
        adaptadoPopulares = new PopularAdapters(getActivity(),listRecetasPopulares,navController);
        recetasPopulares.setAdapter(adaptadoPopulares);

        recetasVisitadas = view.findViewById(R.id.recyclerViewVisitadas);
        recetasVisitadas.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        listRecetasVisitadas = new ArrayList<>();
        obtenerVisitadas();
        adaptadorVisitada = new vistasAdapters(getActivity(),listRecetasVisitadas,navController);
        recetasVisitadas.setAdapter(adaptadorVisitada);

        recetasRecomendadas = view.findViewById(R.id.recyclerViewRecomendadas);
        recetasRecomendadas.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        listRecetasRecomendadas = new ArrayList<>();



        adaptadorRecomendadas = new recomendadasAdapter(getActivity(),listRecetasRecomendadas,navController);
        recetasRecomendadas.setAdapter(adaptadorRecomendadas);

        //recetasRecomendadas = view.findViewById(R.id.recyclerViewRecomendadas);
        //recetasRecomendadas.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        //recetasRecomendadas.setAdapter(adaptadorVisitada);







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

        Collections.sort(valorizadas, new Comparator<RecetaValorizada>() {
            @Override
            public int compare(RecetaValorizada o1, RecetaValorizada o2) {
                if(o1.getValor()>o2.getValor()){

                    if(o1.getNumVotaciones() + 10 < o2.getNumVotaciones())
                        return  -1;
                    else if(o1.getNumVotaciones() > o2.getNumVotaciones())
                        return -1;
                    else if(o1.getNumVotaciones() == o2.getNumVotaciones())
                        return -1;

                    else{
                        return 1;
                    }

                }
                else if(o1.getValor()<o2.getValor()){

                    if(o1.getNumVotaciones() > 10 + o2.getNumVotaciones())
                        return  1;
                    else if(o1.getNumVotaciones() < o2.getNumVotaciones())
                        return 1;
                    else if(o1.getNumVotaciones() == o2.getNumVotaciones())
                        return 1;

                    else{
                        return -1;
                    }

                }
                else if(o1.getValor() == o2.getValor()){
                    if(o1.getNumVotaciones() > o2.getNumVotaciones())
                        return -1;
                    else if(o1.getNumVotaciones() < o2.getNumVotaciones())
                        return 1;
                    else
                        return 0;

                }


                return 0;
            }
        });

        if(valorizadas.size()>10){

            for(int i = 0;i<10;i++) {
                //Toast.makeText(getContext(), "nombre " +valorizadas.get(i).getId() + " valor " + valorizadas.get(i).getNumVotaciones(), Toast.LENGTH_SHORT).show();

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
                //Toast.makeText(getContext(), "nombre " +valorizadas.get(i).getId() + " valor " + valorizadas.get(i).getNumVotaciones(), Toast.LENGTH_SHORT).show();

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

    public void obtenerVisitadas(){

        db = FirebaseFirestore.getInstance();

        db.collection("recetas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {



                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String visitas = document.getString("visitas");
                                int numVisitas = Integer.parseInt(visitas);

                                RecetaVisitada recetaVisitada = new RecetaVisitada(document.getId(),numVisitas);
                                visitadas.add(recetaVisitada);






                            }

                            ordenarVisitadas();

                        } else {

                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                        }
                    }
                });



    }

    public void ordenarVisitadas(){

        Collections.sort(visitadas);

        if(visitadas.size()>10){

            for(int i = 0;i<10;i++) {
                //Toast.makeText(getContext(), " visitas " + visitadas.get(i).getVisitas(), Toast.LENGTH_SHORT).show();

                int finalI = i;
                db.collection("recetas")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        if(document.getId().equals(visitadas.get(finalI).getId())) {

                                            Receta receta = document.toObject(Receta.class);
                                            listRecetasVisitadas.add(receta);
                                            adaptadorVisitada.notifyDataSetChanged();

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

            for(int i = 0;i<visitadas.size();i++) {
                //Toast.makeText(getContext(), " visitas " + visitadas.get(i).getVisitas(), Toast.LENGTH_SHORT).show();

                int finalI = i;
                db.collection("recetas")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        if(document.getId().equals(visitadas.get(finalI).getId())) {

                                            Receta receta = document.toObject(Receta.class);
                                            listRecetasVisitadas.add(receta);
                                            adaptadorVisitada.notifyDataSetChanged();

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

    public void obtenerRecomendadas(){

        db = FirebaseFirestore.getInstance();

       // Toast.makeText(getContext(),"entre 1",Toast.LENGTH_SHORT).show();



        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                           // Toast.makeText(getContext(),"entre 2",Toast.LENGTH_SHORT).show();



                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals(mAuth.getUid())){

                                    ArrayList<String> sus = (ArrayList) document.get("suscripciones");

                                    if(sus.get(0).equals("NINGUNA")){

                                        db.collection("recetas")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {



                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                Receta r = document.toObject(Receta.class);
                                                                listRecetasRecomendadas.add(r);
                                                                adaptadorRecomendadas.notifyDataSetChanged();


                                                            }


                                                        } else {

                                                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });

                                    }
                                    else if(sus.get(0).equals("RECETAS_RAPIDAS")){

                                        db.collection("recetas")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {



                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                Receta r = document.toObject(Receta.class);

                                                                if(r.getDificultad().equals("Rápida de hacer")){

                                                                    listRecetasRecomendadas.add(r);
                                                                    adaptadorRecomendadas.notifyDataSetChanged();

                                                                }




                                                            }


                                                        } else {

                                                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                    }


                                    else if(sus.get(0).equals("RECETAS_MEDIAS")){
                                        db.collection("recetas")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {



                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                Receta r = document.toObject(Receta.class);

                                                                if(r.getDificultad().equals("Tiempo intermedio")){

                                                                    listRecetasRecomendadas.add(r);
                                                                    adaptadorRecomendadas.notifyDataSetChanged();

                                                                }




                                                            }


                                                        } else {

                                                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                    }

                                    else if(sus.get(0).equals("RECETAS_LENTAS")){
                                        db.collection("recetas")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {



                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                Receta r = document.toObject(Receta.class);

                                                                if(r.getDificultad().equals("Larga de hacer")){

                                                                    listRecetasRecomendadas.add(r);
                                                                    adaptadorRecomendadas.notifyDataSetChanged();

                                                                }




                                                            }


                                                        } else {

                                                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                    }


                                    else if(sus.get(0).equals("RECETAS_RAPIDAS_MEDIAS")){
                                        db.collection("recetas")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {



                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                Receta r = document.toObject(Receta.class);

                                                                if(r.getDificultad().equals("Rápida de hacer") || r.getDificultad().equals("Tiempo intermedio")){

                                                                    listRecetasRecomendadas.add(r);
                                                                    adaptadorRecomendadas.notifyDataSetChanged();

                                                                }




                                                            }


                                                        } else {

                                                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                    }
                                    else if(sus.get(0).equals("RECETAS_RAPIDAS_LARGAS")){
                                        db.collection("recetas")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {



                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                Receta r = document.toObject(Receta.class);

                                                                if(r.getDificultad().equals("Rápida de hacer") || r.getDificultad().equals("Larga de hacer")){

                                                                    listRecetasRecomendadas.add(r);
                                                                    adaptadorRecomendadas.notifyDataSetChanged();

                                                                }




                                                            }


                                                        } else {

                                                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                    }
                                    else if(sus.get(0).equals("RECETAS_LARGAS_MEDIAS")){
                                        db.collection("recetas")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {



                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                Receta r = document.toObject(Receta.class);

                                                                if(r.getDificultad().equals("Tiempo intermedio") || r.getDificultad().equals("Larga de hacer")){

                                                                    listRecetasRecomendadas.add(r);
                                                                    adaptadorRecomendadas.notifyDataSetChanged();

                                                                }




                                                            }


                                                        } else {

                                                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                    }
                                    else{
                                        db.collection("recetas")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {



                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                Receta r = document.toObject(Receta.class);
                                                                listRecetasRecomendadas.add(r);
                                                                adaptadorRecomendadas.notifyDataSetChanged();


                                                            }


                                                        } else {

                                                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                    }
                                }







                            }


                        } else {

                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                        }
                    }
                });






    }





}