package com.example.comidita3.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.comidita3.Interfaz;
import com.example.comidita3.R;
import com.example.comidita3.adaptadores.adaptadorRecetasSubidas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSubidas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSubidas extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listViewItems;
    Interfaz contexto;
    FloatingActionButton subir;
    NavController navController;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    TextView nombre;
    ImageView perfil;
    String pathInicio;


    public FragmentSubidas() {
        // Required empty public constructor
    }



    public static FragmentSubidas newInstance(String param1, String param2) {
        FragmentSubidas fragment = new FragmentSubidas();
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
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        contexto = (Interfaz)context;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadPerfilImage();

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    // Toast.makeText(getContext(),"hey",Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.fragmentCuenta);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subidas, container, false);

        mAuth = FirebaseAuth.getInstance();

        perfil = v.findViewById(R.id.circleImageViewPerfilUser);
        nombre = v.findViewById(R.id.textViewNombrePerfilUser);

        subir = v.findViewById(R.id.floatingActionButtonAñadirSubidas);
        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.fragmentRellenarReceta);

            }
        });

        listViewItems = (ListView) v.findViewById(R.id.listViewPerfilUser);
        adaptadorRecetasSubidas ad = contexto.getAdaptadorRecetasSubidas();

        listViewItems.setAdapter(ad);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();

                bundle.putString("id",ad.getItem(position).getId());
                bundle.putString("nombre", ad.getItem(position).getNombre());
                bundle.putString("ingredientes", ad.getItem(position).getIngredientes());
                bundle.putString("descripcion", ad.getItem(position).getDescripcion());
                bundle.putString("urlYoutube", ad.getItem(position).getUrlYoutube());
                bundle.putString("userPath", ad.getItem(position).getUserPath());
                bundle.putString("imagePath", ad.getItem(position).getImagePath());
                bundle.putString("valoracion", ad.getItem(position).getValoracion());
                bundle.putString("visitas", ad.getItem(position).getVisitas());

                navController.navigate(R.id.fragment_receta_detalle_sinPerfil,bundle);

            }
        });




        return v;

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

                                if(document.getId().equals(mAuth.getUid())) {

                                    nombre.setText(document.getString("nombre"));


                                    if(!(pathInicio = document.getString("perfilPath")).equals("")){

                                        storageReference.child(pathInicio).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                Glide.with(getContext()).load(uri).into(perfil);
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


}