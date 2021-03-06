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

import com.example.comidita3.Interfaz;
import com.example.comidita3.R;
import com.example.comidita3.Objetos.Receta;
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

public class rellenarReceta extends Fragment {

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
    boolean fotoSubida = false;
    String dificultad;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public rellenarReceta() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contexto = (Interfaz)context;
    }

    public static rellenarReceta newInstance(String param1, String param2) {
        rellenarReceta fragment = new rellenarReceta();
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


        //storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rellenar_receta, container, false);

        //edit text
        nombre = v.findViewById(R.id.editTextNombreRecetaRellenar);
        ingredientes = v.findViewById(R.id.editTextIngredientesRecetaRellenar);
        descripcion = v.findViewById(R.id.editTextPasosRecetaRellenar);
        URL = v.findViewById(R.id.editTextURLRecetaRellenar);

        rapida = v.findViewById(R.id.radioButton6);
        intermedia = v.findViewById(R.id.radioButton7);
        lenta = v.findViewById(R.id.radioButton8);

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
        back = v.findViewById(R.id.imageViewSalirRecetaRellenar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarSalida();

            }
        });

        //boton subir
        guardar = v.findViewById(R.id.buttonRecetaRellenarGuardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userPath = mAuth.getCurrentUser().getUid();

                if(rapida.isChecked()){

                    dificultad = "R??pida de hacer";
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
                    Toast.makeText(getContext(),"debes marcar arriba la estimaci??n de su preparaci??n...",Toast.LENGTH_SHORT).show();
                }


            }
        });



        imagen = v.findViewById(R.id.imageViewRecetaRellenar);
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
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
        builder.setTitle("Subir receta")
                .setMessage("??Est?? seguro de que quiere subir la receta?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //obtenemos los parametros
                        name = nombre.getText().toString();
                        ingredients = ingredientes.getText().toString();
                        description = descripcion.getText().toString();
                        link = URL.getText().toString();


                        //comprobamos que no estan vacios

                        if(!name.isEmpty()  && !ingredients.isEmpty() && !description.isEmpty() && !link.isEmpty()){

                            if(name.length() < 50){

                                if(fotoSubida){



                                    //generamos un id random
                                    String rId = UUID.randomUUID().toString();

                                    //construimos el objeto de tipo Receta
                                    Receta receta = new Receta(rId,name,ingredients,description,link,imagePath,UID);
                                    receta.setDificultad(dificultad);

                                    //llamamos a la bbdd

                                    //a??adimos a la coleccion de recetas la receta
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();


                                    //a??adimos a la coleccion de valoraciones un documento que corresponda al documento de la receta
                                    Map<String,Object> valoraciones = new HashMap<>();
                                    valoraciones.put(UID,"0.0");

                                    Map<String,Object> recetaN = new HashMap<>();
                                    recetaN.put("descripcion",receta.getDescripcion());
                                    recetaN.put("dificultad",receta.getDificultad());
                                    recetaN.put("id",receta.getId());
                                    recetaN.put("imagePath",receta.getImagePath());
                                    recetaN.put("ingredientes",receta.getIngredientes());
                                    recetaN.put("nombre",receta.getNombre());
                                    recetaN.put("urlYoutube",receta.getUrlYoutube());
                                    recetaN.put("userPath",UID);
                                    recetaN.put("valoracion","0");
                                    recetaN.put("visitas","0");
                                    recetaN.put("votaciones",valoraciones);

                                    db.collection("recetas").document(receta.getId()).set(recetaN);

                                    //enviar notificacion a suscriptores
                                    if(dificultad.equals("R??pida de hacer")){

                                        contexto.enviarNotificacion("RECETAS_RAPIDAS",rId);
                                        contexto.enviarNotificacion("RECETAS_RAPIDAS_MEDIAS",rId);
                                        contexto.enviarNotificacion("RECETAS_RAPIDAS_LARGAS",rId);
                                        contexto.enviarNotificacion("TODAS",rId);

                                        //creacion notificion para que el usuario creador pueda saber si ha sido subida su receta

                                        String title = "??Se ha subido tu receta! \uD83D\uDE0E";
                                        String body = "Echa un vistazo a como ha quedado \uD83C\uDF72" ;

                                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                            contexto.versionMayor(title,body,rId);
                                        else
                                            contexto.versionMenor(title,body,rId);

                                    }
                                    else if(dificultad.equals("Tiempo intermedio")){
                                        contexto.enviarNotificacion("RECETAS_MEDIAS",rId);
                                        contexto.enviarNotificacion("RECETAS_LARGAS_MEDIAS",rId);
                                        contexto.enviarNotificacion("RECETAS_RAPIDAS_MEDIAS",rId);
                                        contexto.enviarNotificacion("TODAS",rId);

                                        //creacion notificion para que el usuario creador pueda saber si ha sido subida su receta

                                        String title = "??Se ha subido tu receta! \uD83D\uDE0E";
                                        String body = "Echa un vistazo a como ha quedado \uD83C\uDF72" ;

                                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                            contexto.versionMayor(title,body,rId);
                                        else
                                            contexto.versionMenor(title,body,rId);



                                    }
                                    else if(dificultad.equals("Larga de hacer")){
                                        contexto.enviarNotificacion("RECETAS_LENTAS",rId);
                                        contexto.enviarNotificacion("RECETAS_LARGAS_MEDIAS",rId);
                                        contexto.enviarNotificacion("RECETAS_RAPIDAS_LARGAS",rId);
                                        contexto.enviarNotificacion("TODAS",rId);

                                        //creacion notificion para que el usuario creador pueda saber si ha sido subida su receta

                                        String title = "??Se ha subido tu receta! \uD83D\uDE0E";
                                        String body = "Echa un vistazo a como ha quedado \uD83C\uDF72" ;

                                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                            contexto.versionMayor(title,body,rId);
                                        else
                                            contexto.versionMenor(title,body,rId);


                                    }


                                    Toast.makeText(getContext(),"Receta subida con exito. ??Gracias!",Toast.LENGTH_SHORT).show();
                                    navController.navigate(R.id.fragmentHome);




                                }
                                else{
                                    Toast.makeText(getContext(),"falta subir la foto",Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{
                                Toast.makeText(getContext(),"El nombre de la receta no puede superar los 50 caracteres...",Toast.LENGTH_SHORT).show();
                            }



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