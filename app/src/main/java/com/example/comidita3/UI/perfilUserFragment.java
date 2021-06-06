package com.example.comidita3.UI;

import android.content.Context;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.comidita3.Interfaz;
import com.example.comidita3.Objetos.Receta;
import com.example.comidita3.R;
import com.example.comidita3.adaptadores.UploadsAdapter;
import com.example.comidita3.adaptadores.adaptadorRecetasSubidas;
import com.example.comidita3.adaptadores.perfilUserAdapter;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class perfilUserFragment extends Fragment {



    private static final String ARG_USERPATH = "userPath";


    // TODO: Rename and change types of parameters
    private String userPath;




    //elementos del layout

    CircleImageView perfil;
    TextView name;
    ListView listView;
    NavController navController;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    String pathInicio;
    Interfaz contexto;

    SearchView searchView;

    FirebaseFirestore db;

    //recyclerview

    RecyclerView subidasR;
    List<Receta> listRecetasSubidas;
    perfilUserAdapter adaptadorSubidas;





    public perfilUserFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static perfilUserFragment newInstance(String param1) {

        perfilUserFragment fragment = new perfilUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERPATH, param1);


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            userPath = getArguments().getString(ARG_USERPATH);


        }

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil_user, container, false);

        perfil = v.findViewById(R.id.circleImageViewPerfilUser);


        name = v.findViewById(R.id.textViewNombrePerfilUser);




        //listView = v.findViewById(R.id.listViewPerfilUser);
        //adaptadorRecetasSubidas ad = contexto.getAdaptadorRecetasSubidasOther();
        //listView.setAdapter(ad);
        //listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          //  @Override
            //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  Bundle bundle = new Bundle();

                //bundle.putString("id",ad.getItem(position).getId());
                //bundle.putString("nombre", ad.getItem(position).getNombre());
                //bundle.putString("ingredientes", ad.getItem(position).getIngredientes());
                //bundle.putString("descripcion", ad.getItem(position).getDescripcion());
                //bundle.putString("urlYoutube", ad.getItem(position).getUrlYoutube());
                //bundle.putString("userPath", ad.getItem(position).getUserPath());
                //bundle.putString("imagePath", ad.getItem(position).getImagePath());
                //bundle.putString("valoracion", ad.getItem(position).getValoracion());
                //bundle.putString("visitas", ad.getItem(position).getVisitas());

            //    navController.navigate(R.id.fragment_receta_detalle_sinPerfil,bundle);
            //}
        //});








        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        loadPerfilImage();

        obtenerSubidas();

        subidasR = view.findViewById(R.id.recyclerViewPerfilUser);
        subidasR.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        listRecetasSubidas = new ArrayList<>();
        adaptadorSubidas = new perfilUserAdapter(getActivity(),listRecetasSubidas,navController);
        subidasR.setAdapter(adaptadorSubidas);






    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        contexto = (Interfaz)context;
    }

    public void loadPerfilImage(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals(userPath)) {



                                    if(!(pathInicio = document.getString("perfilPath")).equals("")){

                                        name.setText(document.getString("nombre"));

                                        storageReference.child(pathInicio).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                try{
                                                    Glide.with(getContext()).load(uri).into(perfil);
                                                }catch (Exception e){

                                                }


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle any errors
                                            }
                                        });




                                    }
                                }
                                else;
                                //Toast.makeText(getContext(),"no encontrado",Toast.LENGTH_SHORT).show();

                            }
                        } else {

                            Toast.makeText(getContext(),"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void obtenerSubidas(){

        db = FirebaseFirestore.getInstance();

        db.collection("recetas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {



                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String userPath = document.getString("userPath");

                                if(userPath.equals(userPath)){

                                    Receta receta = document.toObject(Receta.class);
                                    listRecetasSubidas.add(receta);
                                    adaptadorSubidas.notifyDataSetChanged();

                                }

                            }

                        } else {

                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

                        }
                    }
                });



















    }


}