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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.comidita3.Interfaz;
import com.example.comidita3.Objetos.Receta;
import com.example.comidita3.Objetos.RecetaVisitada;
import com.example.comidita3.R;
import com.example.comidita3.adaptadores.UploadsAdapter;
import com.example.comidita3.adaptadores.adaptadorAjustes;
import com.example.comidita3.adaptadores.adaptadorFavoritos;
import com.example.comidita3.adaptadores.favAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFavoritos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFavoritos extends Fragment implements SearchView.OnQueryTextListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listViewItems;
    Interfaz contexto;
    //adaptadorFavoritos adaptadorFavoritos;
    NavController navController;

    //recyclerView

    RecyclerView favoritosR;
    List<Receta> listRecetasFavoritos;
    favAdapter adaptadorFavoritos;

    SearchView searchView;

    //firebase
    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public FragmentFavoritos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFavoritos.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFavoritos newInstance(String param1, String param2) {
        FragmentFavoritos fragment = new FragmentFavoritos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        contexto = (Interfaz)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favoritos, container, false);

        //listViewItems = (ListView) v.findViewById(R.id.listViewFavoritos);
        //adaptadorFavoritos ad = contexto.getAdaptadorFavoritos();
        //listViewItems.setAdapter(ad);
        //listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           // @Override
            //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               //Toast.makeText(getContext(),ad.getItem(position).getNombre(),Toast.LENGTH_SHORT).show();

              //  Bundle bundle = new Bundle();

                //bundle.putString("id",ad.getItem(position).getId());
                //bundle.putString("nombre", ad.getItem(position).getNombre());
                //bundle.putString("ingredientes", ad.getItem(position).getIngredientes());
                //bundle.putString("dificultad",ad.getItem(position).getDificultad());
                //bundle.putString("descripcion", ad.getItem(position).getDescripcion());
                //bundle.putString("urlYoutube", ad.getItem(position).getUrlYoutube());
                //bundle.putString("userPath", ad.getItem(position).getUserPath());
                //bundle.putString("imagePath", ad.getItem(position).getImagePath());
                //bundle.putString("valoracion", ad.getItem(position).getValoracion());
                //bundle.putString("visitas", ad.getItem(position).getVisitas());

                //navController.navigate(R.id.fragment_recetaDetalle,bundle);

           // }
       // });

        mAuth = FirebaseAuth.getInstance();

        searchView = v.findViewById(R.id.searchViewFavoritos);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });
        searchView.setOnQueryTextListener(this);

        obtenerFavoritas();

        favoritosR = v.findViewById(R.id.recyclerViewFavoritos);
        favoritosR.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        listRecetasFavoritos = new ArrayList<>();
        adaptadorFavoritos = new favAdapter(getActivity(),listRecetasFavoritos,navController);
        favoritosR.setAdapter(adaptadorFavoritos);


        return v;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adaptadorFavoritos.getFilter().filter(newText);
        return true;
    }

    public void obtenerFavoritas(){

        db = FirebaseFirestore.getInstance();

        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals(mAuth.getUid())) {

                                    ArrayList<String>  favs = (ArrayList)document.get("favoritas");



                                    for(int i = 0;i < favs.size();i++){


                                        int finalI = i;

                                        db.collection("recetas")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (DocumentSnapshot document : task.getResult()) {

                                                                if(document.getId().equals(favs.get(finalI))) {

                                                                    //creamos el obj receta

                                                                    Receta receta = document.toObject(Receta.class);
                                                                    listRecetasFavoritos.add(receta);
                                                                    adaptadorFavoritos.notifyDataSetChanged();


                                                                }


                                                            }
                                                        } else {

                                                            Toast.makeText(getContext(),"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });


                                    }


                                }


                            }
                        } else {

                            //Toast.makeText(getContext(),"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}