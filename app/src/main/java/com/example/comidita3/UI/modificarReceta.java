package com.example.comidita3.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.comidita3.Interfaz;
import com.example.comidita3.R;
import com.example.comidita3.clasesPOJO.Receta;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link modificarReceta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class modificarReceta extends Fragment {

    Interfaz contexto;
    NavController navController;
    EditText nombre,ingredientes,descripcion,URL;
    RadioButton rapida,intermedia,lenta;
    ImageView imagen,back;
    Button guardar;
    public Uri imageUri;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    String imagePath,userPath,name,ingredients,description,link;
    boolean fotoSubida = false,hecho =false;
    String dificultad;

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
    private String mParam1;
    private String mParam2;

    private String idReceta,nombreReceta,ingredientesReceta,dificultadReceta,descripcionReceta,urlYoutubeReceta,userpathReceta,imagepathReceta,valoracionReceta,visitasReceta;

    public modificarReceta() {
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
        if (getArguments() != null) {
            idReceta = getArguments().getString(ARG_ID);
            nombreReceta = getArguments().getString(ARG_NOMBRE);
            ingredientesReceta = getArguments().getString(ARG_INGREDIENTES);
            dificultadReceta = getArguments().getString(ARG_DIFICULTAD);
            descripcionReceta = getArguments().getString(ARG_DESCRIPCION);
            urlYoutubeReceta = getArguments().getString(ARG_URLYOUTUBE);
            userpathReceta = getArguments().getString(ARG_USERPATH);
            imagepathReceta= getArguments().getString(ARG_IMAGEPATH);
            valoracionReceta = getArguments().getString(ARG_VALORACION);
            visitasReceta = getArguments().getString(ARG_VISITAS);
        }


        //storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_modificar_receta, container, false);

        //edit text
        nombre = v.findViewById(R.id.editTextNombreRecetaModificar);
        nombre.setText(nombreReceta);

        ingredientes = v.findViewById(R.id.editTextIngredientesRecetaModificar);
        ingredientes.setText(ingredientesReceta);

        descripcion = v.findViewById(R.id.editTextPasosRecetaModificar);
        descripcion.setText(descripcionReceta);

        URL = v.findViewById(R.id.editTextURLRecetaModificar);
        URL.setText(urlYoutubeReceta);

        rapida = v.findViewById(R.id.radioButtonRapidoModificar);
        intermedia = v.findViewById(R.id.radioButtonButtonIntermedioModificar);
        lenta = v.findViewById(R.id.radioButtonLargoModificar);

        Toast.makeText(getContext(),dificultadReceta,Toast.LENGTH_SHORT).show();

        if(dificultadReceta.equals("Rápida de hacer"))
            rapida.setChecked(true);
        else  if(dificultadReceta.equals("Tiempo intermedio"))
            intermedia.setChecked(true);
        else if(dificultadReceta.equals("Larga de hacer"))
            lenta.setChecked(true);
        else{

        }

        imagen = v.findViewById(R.id.imageViewRecetaModificar);
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        storageReference.child(imagepathReceta).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

        imagePath ="";

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {


            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    comprobarSalida();

                    return true;
                }
                return false;
            }
        });

        //instancia firebase user
        mAuth = FirebaseAuth.getInstance();

        //boton back
        back = v.findViewById(R.id.imageViewSalirRecetaModificar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarSalida();

            }
        });

        //boton subir
        guardar = v.findViewById(R.id.buttonRecetaModificarGuardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userPath = mAuth.getCurrentUser().getUid();

                if(rapida.isChecked()){

                    dificultad = "Rápida de hacer";
                    guardar(userPath);

                }
                else if(intermedia.isChecked()) {
                    dificultad = "Tiempo intermedio";
                    guardar(userPath);
                }
                else if(lenta.isChecked()){
                    dificultad = "Larga de hacer";
                    guardar(userPath);
                }
                else{
                    Toast.makeText(getContext(),"debes marcar arriba la estimación de su preparación...",Toast.LENGTH_SHORT).show();
                }


            }
        });






        return v;
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
            imagen.setImageURI(imageUri);
            fotoSubida  = true;
            uploadPicture();

        }
    }

    public  void uploadPicture(){

        if(!imagePath.equals("")){

            StorageReference storageRef = storage.getReference();
            StorageReference desertRef = storageRef.child(imagePath);

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

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Subiendo imagen...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + randomKey);

        riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                imagePath = riversRef.getPath();
                //subimos a la nube la imagen



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
                pd.setMessage("Espere un momento por favor...");

            }
        });



    }

    public void guardar(String UID){

        AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
        builder.setTitle("Modificar receta")
                .setMessage("¿Está seguro de que quieres actualizar la receta?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //obtenemos los parametros
                        name = nombre.getText().toString();
                        ingredients = ingredientes.getText().toString();
                        description = descripcion.getText().toString();
                        if(URL.getText().toString().isEmpty())
                            link = "";
                        else
                            link = URL.getText().toString();

                        //comprobamos que no estan vacios

                        if(!name.isEmpty() && !ingredients.isEmpty() && !description.isEmpty()){

                                //construimos el objeto de tipo Receta
                                Receta receta = new Receta(idReceta,name,ingredients,description,link,imagePath,UID);
                                receta.setDificultad(dificultad);


                                //comprobamos que ha subido una nueva imagen

                                if(imagePath.equals(""))
                                    imagePath = imagepathReceta;

                                //si la ha subido borramos la vieja

                                if(fotoSubida){

                                    //buscamos la imagen vieja
                                    StorageReference storageRef = storage.getReference();

                                    // Create a reference to the file to delete
                                    StorageReference desertRef = storageRef.child(imagepathReceta);

                                    // Delete the file
                                    desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            hecho = true;
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            hecho = false;
                                        }
                                    });

                                    if(hecho)
                                        Toast.makeText(getContext(),"hecho",Toast.LENGTH_SHORT).show();

                                }


                                //llamamos a la bbdd
                                //actualizamos a la coleccion de recetas la receta
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                db.collection("recetas").document(receta.getId()).update("nombre",name);
                                db.collection("recetas").document(receta.getId()).update("ingredientes",ingredients);
                                db.collection("recetas").document(receta.getId()).update("descripcion",description);
                                db.collection("recetas").document(receta.getId()).update("urlYoutube",link);
                                db.collection("recetas").document(receta.getId()).update("imagePath",imagePath);
                                db.collection("recetas").document(receta.getId()).update("dificultad",dificultad);

                                Toast.makeText(getContext(),"Receta actualizada con éxito. ¡Gracias!",Toast.LENGTH_SHORT).show();
                                navController.navigate(R.id.fragmentHome);



                        }
                        else
                            Toast.makeText(getContext(),"Rellena todos los campos",Toast.LENGTH_SHORT).show();



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

    public void comprobarSalida(){

        if(fotoSubida){

            StorageReference storageRef = storage.getReference();

            StorageReference desertRef = storageRef.child(imagePath);

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

            navController.navigate(R.id.fragmentSubidas);

        }
        else{
            navController.navigate(R.id.fragmentSubidas);
        }



    }
}