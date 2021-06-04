package com.example.comidita3.UI;

import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.comidita3.Objetos.Receta;
import com.example.comidita3.Objetos.RecetaValorizada;
import com.example.comidita3.Objetos.RecetaVisitada;
import com.example.comidita3.R;
import com.example.comidita3.adaptadores.PopularAdapters;
import com.example.comidita3.adaptadores.todasAdapters;
import com.example.comidita3.adaptadores.vistasAdapters;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentBuscar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentBuscar extends Fragment implements SearchView.OnQueryTextListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseFirestore db;

    SearchView searchView;
    RecyclerView recyclerViewTodas;
    NavController navController;

    List<Receta> listRecetasTodas;
    todasAdapters adaptadorTodas;
    ArrayList<String> todas;

    LottieAnimationView animationNotFound;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragmentBuscar() {
        // Required empty public constructor
    }



    public static fragmentBuscar newInstance(String param1, String param2) {
        fragmentBuscar fragment = new fragmentBuscar();
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
        return inflater.inflate(R.layout.fragment_buscar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        todas = new ArrayList<>();

        searchView = view.findViewById(R.id.searchViewBuscar);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });
        searchView.setOnQueryTextListener(this);

        animationNotFound = view.findViewById(R.id.animationNotFound);
        animationNotFound.setVisibility(View.GONE);


        obtenerTodas();

        recyclerViewTodas  = view.findViewById(R.id.recyclerViewBuscador);
        recyclerViewTodas.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        listRecetasTodas = new ArrayList<>();
        adaptadorTodas = new todasAdapters(getActivity(),listRecetasTodas,navController);
        recyclerViewTodas.setAdapter(adaptadorTodas);




        super.onViewCreated(view, savedInstanceState);
    }




    private void obtenerTodas() {

        db = FirebaseFirestore.getInstance();

        db.collection("recetas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {



                            for (QueryDocumentSnapshot document : task.getResult()) {


                                String id =  document.getId();
                                todas.add(id);






                            }

                            añadirRecetas();

                        } else {

                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                        }
                    }
                });



    }

    private void añadirRecetas() {

        for(int i = 0;i<todas.size();i++) {
            //Toast.makeText(getContext(), "nombre " +valorizadas.get(i).getId() + " valor " + valorizadas.get(i).getNumVotaciones(), Toast.LENGTH_SHORT).show();

            int finalI = i;
            db.collection("recetas")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    if (document.getId().equals(todas.get(finalI))) {

                                        Receta receta = document.toObject(Receta.class);
                                        listRecetasTodas.add(receta);
                                        adaptadorTodas.notifyDataSetChanged();

                                    }

                                }
                            } else {

                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adaptadorTodas.getFilter().filter(newText);


        return true;
    }
}