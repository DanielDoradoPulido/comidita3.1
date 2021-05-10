package com.example.comidita3.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class FragmentAjustes extends Fragment {

   ListView listView;
   ArrayAdapter adaptador;
   Interfaz contexto;
   LinearLayout lSubidas,lContra,lFoto,lCerrarsesion;
   String correoElec;
   NavController navController;
   ImageView perfil;
    TextView nombre,correo;
    private FirebaseAuth mAuth;

    //storage
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    String imagePath;
    String pathInicio;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentAjustes() {
        // Required empty public constructor
    }


    public static FragmentAjustes newInstance(String param1, String param2) {

        FragmentAjustes fragment = new FragmentAjustes();

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

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        contexto.loadDataSubidas();

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //cargar imagen de perfil

        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals(mAuth.getUid())) {

                                    nombre.setText(document.getString("nombre"));
                                    correo.setText(document.getString("correo"));

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contexto = (Interfaz)context;

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
        View v = inflater.inflate(R.layout.fragment_ajustes, container, false);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preferencias_PMDM_correo_file), Context.MODE_PRIVATE);


        //pares clave-valor

        correoElec = sharedPref.getString(getString(R.string.preferencias_email),"nofunciono");

        //asignar nombre y correo con atuhentication + firestore

        nombre = v.findViewById(R.id.textViewNombreAjustes);
        correo = v.findViewById(R.id.textViewCorreoAjustes);

        perfil = v.findViewById(R.id.circleImageViewPerfilAjustes);

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });



        lCerrarsesion = v.findViewById(R.id.linearLayoutCerrarSesion);
        lContra = v.findViewById(R.id.linearLayoutCambiarContraseña);
        lSubidas = v.findViewById(R.id.linearLayoutSubidas);

        lCerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
                builder.setTitle("Cerrar Sesión")
                        .setMessage("¿Estás seguro de que quieres cerrar la sesion?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //borrar cookies
                                SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preferencias_PMDM_correo_file), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(getString(R.string.preferencias_email), "");
                                editor.putString(getString(R.string.preferencias_password), "");
                                editor.putBoolean(getString(R.string.preferencias_islogin), false);
                                editor.commit();

                                //desloguear firebase account

                                FirebaseUser user = mAuth.getCurrentUser();

                                if(user != null){
                                    mAuth.signOut();
                                }

                                Toast.makeText(getContext(),"Se ha cerrado sesion",Toast.LENGTH_SHORT).show();

                                contexto.irLogin();

                                dialog.dismiss();

                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alerta = builder.create();
                alerta.show();


                mAuth.signOut();
            }
        });

        lContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
                builder.setTitle("Cambiar Contraseña")
                        .setMessage("Se enviará la solicitud a su correo electrónico")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseAuth auth = FirebaseAuth.getInstance();

                                String emailAddress = contexto.email();

                                auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){

                                            Toast.makeText(getContext(),"Enviado",Toast.LENGTH_SHORT).show();

                                        }
                                        else{
                                            Toast.makeText(getContext(),"no Enviado",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                                dialog.dismiss();

                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });



                AlertDialog alerta = builder.create();
                alerta.show();





            }
        });

        lSubidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSubidas();



            }
        });




        return v;

    }

    public void showSubidas(){

        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(perfil, "perfilSubidas")
                .build();

        navController.navigate(
                R.id.fragmentSubidas,
                null, // Bundle of args
                null, // NavOptions
                extras);




    }

    public void choosePicture(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            perfil.setImageURI(imageUri);
            uploadPicture();
        }
    }

    public  void uploadPicture(){

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Subiendo imagen...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("users/" + randomKey);

        riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                imagePath = riversRef.getPath();
                asignarURL(imagePath);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getContext(),"imagen no subida,error",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100 * snapshot.getBytesTransferred());
                pd.setMessage("Progress: " +(int)progressPercent+ "%");

            }
        });



    }

    public void asignarURL(String path){

        //buscamos el usuario

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        StorageReference storageRef = storage.getReference();

        // Create a reference to the file to delete


        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals(mAuth.getUid())) {

                                   pathInicio = document.getString("perfilPath");

                                   if(!pathInicio.equals("")){

                                       StorageReference desertRef = storageRef.child(pathInicio);

                                       // Delete the file
                                       desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {
                                               //correo.setText("borrado");
                                           }
                                       }).addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception exception) {
                                               // Uh-oh, an error occurred!
                                           }
                                       });


                                   }

                                    //una vez localizado actualizamos el campo que necesitamos
                                    db.collection("usuarios").document(document.getId()).update("perfilPath",path);




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