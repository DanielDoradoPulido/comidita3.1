package com.example.comidita3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comidita3.LOGIN.loginActivity;
import com.example.comidita3.adaptadores.PopularAdapters;
import com.example.comidita3.adaptadores.adaptadorAjustes;
import com.example.comidita3.adaptadores.adaptadorFavoritos;
import com.example.comidita3.adaptadores.adaptadorRecetasSubidas;
import com.example.comidita3.Objetos.Receta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Interfaz {

    BottomNavigationView bottomNavigationView;
    NavHostFragment navHostFragment;
    NavController navController;
    String email;
    String imagePath;

    //listAdaptadores
    ArrayList<Receta> subidas;
    ArrayList<Receta> subidasOtherUser;
    ArrayList<Receta> favoritos;


    //cargar ajustes
    adaptadorAjustes arrayAdapterAjustes;
    adaptadorFavoritos arrayAdapterFavoritos;
    adaptadorRecetasSubidas arrayAdapterSubidas,arrayAdapterSubidasOther;
    public FirebaseFirestore db;

    //authentication
    private FirebaseAuth mAuth;

    //notificacion push
    String recipePath="Vacio";
    boolean notificacion = false;
    boolean creador = false;

    //borrar
    Boolean x = false,y=false,z=false;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    String pathRecetaBorrada;
    Boolean hecho = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //extraer info para carga notificaciones

        Intent intent = getIntent();

        if(intent !=null){
            recipePath = intent.getStringExtra("notif");
            notificacion = intent.getBooleanExtra("abrir",false);
           // creador = intent.getBooleanExtra("creador",false);

            if(notificacion)
                cargarNotificacion(recipePath);

        }

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView,navController);



    }




    public void loadDataSubidas(){

        subidas = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        db.collection("recetas")
                .whereEqualTo("userPath", mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //creamos un objeto Receta
                                String id = document.getString("id").toString();
                                String nombre = document.getString("nombre").toString();
                                String ingredientes = document.getString("ingredientes").toString();
                                String dificultad = document.getString("dificultad").toString();
                                String descripcion = document.getString("descripcion").toString();
                                String imagePath = document.getString("imagePath").toString();
                                String urlYoutube = document.getString("urlYoutube").toString();
                                String valoracion = document.getString("valoracion").toString();
                                String visitas = document.getString("visitas").toString();
                                String userpath = document.getString("userPath").toString();

                                Receta r = new Receta(id,nombre,ingredientes,descripcion,urlYoutube,imagePath,userpath,visitas,valoracion);
                                r.setDificultad(dificultad);
                                //lo añadimos a lista de subidas
                                subidas.add(r);
                            }
                        } else {

                        }
                    }
                });






    }



    @Override
    public void loadDataSubidasOther(String s) {
        subidasOtherUser = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        db.collection("recetas")
                .whereEqualTo("userPath", s)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //creamos un objeto Receta
                                String id = document.getString("id").toString();
                                String nombre = document.getString("nombre").toString();
                                String ingredientes = document.getString("ingredientes").toString();
                                String dificultad = document.getString("dificultad").toString();
                                String descripcion = document.getString("descripcion").toString();
                                String imagePath = document.getString("imagePath").toString();
                                String urlYoutube = document.getString("urlYoutube").toString();
                                String valoracion = document.getString("valoracion").toString();
                                String visitas = document.getString("visitas").toString();
                                String userpath = document.getString("userPath").toString();

                                Receta r = new Receta(id,nombre,ingredientes,descripcion,urlYoutube,imagePath,userpath,visitas,valoracion);
                                r.setDificultad(dificultad);

                                //lo añadimos a lista de subidas
                                subidasOtherUser.add(r);
                            }
                        } else {

                        }
                    }
                });
    }

    @Override
    public adaptadorRecetasSubidas getAdaptadorRecetasSubidas() {

        //obtenemos todos los objetos y los guardamos en una list de objetos de tipo Receta

        arrayAdapterSubidas = new adaptadorRecetasSubidas( this,R.layout.adaptador_recetas_subidas_layout,subidas);

        return arrayAdapterSubidas;


    }

    @Override
    public adaptadorRecetasSubidas getAdaptadorRecetasSubidasOther() {

        arrayAdapterSubidasOther = new adaptadorRecetasSubidas( this,R.layout.adaptador_recetas_subidas_layout,subidasOtherUser);

        return arrayAdapterSubidasOther;
    }

    @Override
    public adaptadorFavoritos getAdaptadorFavoritos() {

        arrayAdapterFavoritos = new adaptadorFavoritos( this,R.layout.adaptador_recetas_subidas_layout,favoritos);

        return arrayAdapterFavoritos;
    }

    @Override
    public void loadDataFavoritos() {

        favoritos = new ArrayList<>();

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

                                                                    //creamos un objeto Receta
                                                                    String id = document.getString("id").toString();
                                                                    String nombre = document.getString("nombre").toString();
                                                                    String ingredientes = document.getString("ingredientes").toString();
                                                                    String dificultad = document.getString("dificultad").toString();
                                                                    String descripcion = document.getString("descripcion").toString();
                                                                    String imagePath = document.getString("imagePath").toString();
                                                                    String urlYoutube = document.getString("urlYoutube").toString();
                                                                    String valoracion = document.getString("valoracion").toString();
                                                                    String visitas = document.getString("visitas").toString();
                                                                    String userpath = document.getString("userPath").toString();

                                                                    Receta r = new Receta(id,nombre,ingredientes,descripcion,urlYoutube,imagePath,userpath,visitas,valoracion);
                                                                    r.setDificultad(dificultad);

                                                                    //lo añadimos a lista de subidas
                                                                    favoritos.add(r);



                                                                }


                                                            }
                                                        } else {

                                                           // Toast.makeText(getContext(),"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
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

    @Override
    public void subscribir(int opcion) {
        //RECETAS RAPIDAS
        if(opcion==0){



            FirebaseMessaging.getInstance().subscribeToTopic("RECETAS_RAPIDAS")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "¡Genial! Le enviaremos notificaciones sobre recetas de rápida preparación";
                            if (!task.isSuccessful()) {
                                msg = "Ha habido un error con la suscripcion.intentelo de nuevo...";
                            }

                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

            //buscamos el usuario

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("usuarios")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {

                                    if(document.getId().equals(mAuth.getUid())) {

                                        ArrayList<String> suscripcion = new ArrayList<>();


                                        suscripcion.add("RECETAS_RAPIDAS");

                                        //una vez localizado actualizamos el campo que necesitamos
                                        db.collection("usuarios").document(document.getId()).update("suscripciones",suscripcion);




                                    }
                                    else;
                                    //Toast.makeText(getContext(),"no encontrado",Toast.LENGTH_SHORT).show();

                                }
                            } else {

                                //Toast.makeText(,"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



        }

        //RECETAS MEDIAS
        else if(opcion==1){



            FirebaseMessaging.getInstance().subscribeToTopic("RECETAS_MEDIAS")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "¡Genial! Le enviaremos notificaciones sobre recetas de preparación media";
                            if (!task.isSuccessful()) {
                                msg = "Ha habido un error con la suscripcion.intentelo de nuevo...";
                            }

                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

            //buscamos el usuario

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("usuarios")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {

                                    if(document.getId().equals(mAuth.getUid())) {

                                        ArrayList<String> suscripcion = new ArrayList<>();


                                        suscripcion.add("RECETAS_MEDIAS");

                                        //una vez localizado actualizamos el campo que necesitamos
                                        db.collection("usuarios").document(document.getId()).update("suscripciones",suscripcion);




                                    }
                                    else;
                                    //Toast.makeText(getContext(),"no encontrado",Toast.LENGTH_SHORT).show();

                                }
                            } else {

                                //Toast.makeText(,"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

        //RECETAS LARGAS
        else if(opcion==2){



            FirebaseMessaging.getInstance().subscribeToTopic("RECETAS_LENTAS")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "¡Genial! Le enviaremos notificaciones sobre recetas de larga preparación";
                            if (!task.isSuccessful()) {
                                msg = "Ha habido un error con la suscripcion.intentelo de nuevo...";
                            }

                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

            //buscamos el usuario

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("usuarios")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {

                                    if(document.getId().equals(mAuth.getUid())) {

                                        ArrayList<String> suscripcion = new ArrayList<>();

                                        suscripcion.add("RECETAS_LENTAS");

                                        //una vez localizado actualizamos el campo que necesitamos
                                        db.collection("usuarios").document(document.getId()).update("suscripciones",suscripcion);




                                    }
                                    else;
                                    //Toast.makeText(getContext(),"no encontrado",Toast.LENGTH_SHORT).show();

                                }
                            } else {

                                //Toast.makeText(,"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

        //RECETAS RAPIDAS Y MEDIAS
        else if(opcion==3){



            FirebaseMessaging.getInstance().subscribeToTopic("RECETAS_RAPIDAS_MEDIAS")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "¡Genial! Le enviaremos notificaciones sobre recetas de rápida y media preparación";
                            if (!task.isSuccessful()) {
                                msg = "Ha habido un error con la suscripcion.intentelo de nuevo...";
                            }

                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

            //buscamos el usuario

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("usuarios")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {

                                    if(document.getId().equals(mAuth.getUid())) {

                                        ArrayList<String> suscripcion = new ArrayList<>();

                                        suscripcion.add("RECETAS_RAPIDAS_MEDIAS");

                                        //una vez localizado actualizamos el campo que necesitamos
                                        db.collection("usuarios").document(document.getId()).update("suscripciones",suscripcion);




                                    }
                                    else;
                                    //Toast.makeText(getContext(),"no encontrado",Toast.LENGTH_SHORT).show();

                                }
                            } else {

                                //Toast.makeText(,"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });




        }

        //RECETAS RAPIDAS Y LARGAS
        else if(opcion==4){



            FirebaseMessaging.getInstance().subscribeToTopic("RECETAS_RAPIDAS_LARGAS")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "¡Genial! Le enviaremos notificaciones sobre recetas de rápida y larga preparación";
                            if (!task.isSuccessful()) {
                                msg = "Ha habido un error con la suscripcion.intentelo de nuevo...";
                            }

                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

            //buscamos el usuario

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("usuarios")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {

                                    if(document.getId().equals(mAuth.getUid())) {

                                        ArrayList<String> suscripcion = new ArrayList<>();

                                        suscripcion.add("RECETAS_RAPIDAS_LARGAS");

                                        //una vez localizado actualizamos el campo que necesitamos
                                        db.collection("usuarios").document(document.getId()).update("suscripciones",suscripcion);




                                    }
                                    else;
                                    //Toast.makeText(getContext(),"no encontrado",Toast.LENGTH_SHORT).show();

                                }
                            } else {

                                //Toast.makeText(,"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

        //RECETAS MEDIAS Y LARGAS
        else if(opcion==5){



            FirebaseMessaging.getInstance().subscribeToTopic("RECETAS_LARGAS_MEDIAS")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "¡Genial! Le enviaremos notificaciones sobre recetas de larga y media preparación";
                            if (!task.isSuccessful()) {
                                msg = "Ha habido un error con la suscripcion.intentelo de nuevo...";
                            }

                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

            //buscamos el usuario

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("usuarios")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {

                                    if(document.getId().equals(mAuth.getUid())) {

                                        ArrayList<String> suscripcion = new ArrayList<>();

                                        suscripcion.add("RECETAS_LARGAS_MEDIAS");

                                        //una vez localizado actualizamos el campo que necesitamos
                                        db.collection("usuarios").document(document.getId()).update("suscripciones",suscripcion);




                                    }
                                    else;
                                    //Toast.makeText(getContext(),"no encontrado",Toast.LENGTH_SHORT).show();

                                }
                            } else {

                                //Toast.makeText(,"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

        //TODAS LAS RECETAS
        else if(opcion==6){



            FirebaseMessaging.getInstance().subscribeToTopic("TODAS")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "¡Genial! Le enviaremos notificaciones sobre todo tipo de recetas";
                            if (!task.isSuccessful()) {
                                msg = "Ha habido un error con la suscripcion.intentelo de nuevo...";
                            }

                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

            //buscamos el usuario

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("usuarios")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {

                                    if(document.getId().equals(mAuth.getUid())) {

                                        ArrayList<String> suscripcion = new ArrayList<>();

                                        suscripcion.add("TODAS");

                                        //una vez localizado actualizamos el campo que necesitamos
                                        db.collection("usuarios").document(document.getId()).update("suscripciones",suscripcion);




                                    }
                                    else;
                                    //Toast.makeText(getContext(),"no encontrado",Toast.LENGTH_SHORT).show();

                                }
                            } else {

                                //Toast.makeText(,"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else if(opcion==7){

        }
        else{

        }

    }

    @Override
    public void desubscripcion(int posicion) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals(mAuth.getUid())) {

                                    ArrayList<String> suscripcionLista = (ArrayList<String>) document.get("suscripciones");
                                    ArrayList<String> suscripcionVacia = new ArrayList<>();
                                    suscripcionVacia.add("NINGUNA");

                                    if(!suscripcionLista.isEmpty()){

                                        String suscripcion = suscripcionLista.get(0);

                                        FirebaseMessaging.getInstance().unsubscribeFromTopic(suscripcion)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        String msg = "Se ha desuscrito de " + suscripcion;
                                                        if (!task.isSuccessful()) {
                                                            msg = "Ha habido un error con la suscripcion.intentelo de nuevo...";
                                                        }

                                                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                        subscribir(posicion);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {


                                                Toast.makeText(MainActivity.this, "fallo", Toast.LENGTH_SHORT).show();
                                            }
                                        });







                                        //una vez localizado actualizamos el campo que necesitamos
                                        db.collection("usuarios").document(document.getId()).update("suscripciones",suscripcionVacia);

                                    }





                                }
                                else;
                                //Toast.makeText(getContext(),"no encontrado",Toast.LENGTH_SHORT).show();

                            }
                        } else {

                            //Toast.makeText(,"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    @Override
    public void enviarNotificacion(String tipo,String url) {

        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();

        try{

            json.put("to","/topics/" + tipo);
            JSONObject notification = new JSONObject();
            notification.put("titulo","¡Hey Reciper! \uD83D\uDE0A");
            notification.put("detalle","Creemos que esta receta podría interesarte \uD83C\uDF74");
            notification.put("recipePath",url);
            notification.put("userID",mAuth.getUid());

            json.put("data",notification);

            String URL ="https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,json,null,null){

                @Override
                public Map<String, String> getHeaders()  {

                    Map<String,String> header = new HashMap<>();

                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAAqiNVVa0:APA91bEsTjpvEet4d8XzGWeVR_IzETDSX5HNozxWIWlmauawRNKupcmr1VEp4M4vO1MNeU8Nc6wbzbpo_xva195X8AJBcZsME0wK2JP33ZfjMu3k1woKe77ZKzm_BVP-yNJX9iM4gSlo");

                    return  header;

                }
            };

            myrequest.add(request);




        }catch (JSONException e1){
            e1.printStackTrace();
        }


    }

    @Override
    public void versionMayor(String title, String detail, String recipePath) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferencias_Recetas), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.rutaReceta), recipePath);
        editor.putBoolean(getString(R.string.recetaCreador),true);
        editor.commit();

        String id = "mensaje";

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nc = new NotificationChannel(id,"nuevo",NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            assert nm != null;
            nm.createNotificationChannel(nc);
        }

        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(detail)
                .setContentIntent(clickNoti(recipePath))
                .setContentInfo("nuevo");

        Random random = new Random();
        int idNotify = random.nextInt(8000);

        assert nm !=null;
        nm.notify(idNotify,builder.build());
    }

    @Override
    public void versionMenor(String title, String detail, String recipePath) {

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferencias_Recetas), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.rutaReceta), recipePath);
        editor.putBoolean(getString(R.string.recetaCreador),true);
        editor.commit();

        String id = "mensaje";

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id);

        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(detail)
                .setContentIntent(clickNoti(recipePath))
                .setContentInfo("nuevo");

        Random random = new Random();
        int idNotify = random.nextInt(8000);

        assert nm !=null;
        nm.notify(idNotify,builder.build());

    }

    @Override
    public PendingIntent clickNoti(String recipePath) {

        Intent nf = new Intent(getApplicationContext(), abrirNotificaciones.class);
        nf.putExtra("url",recipePath);


        nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this,0,nf,0);

    }

    @Override
    public PopularAdapters getPopulares() {
        return null;
    }

    @Override
    public void irLogin() {
        Intent intent = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(intent);
    }

    @Override
    public String email() {



        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals(mAuth.getUid())) {


                                    email = document.getString("correo");


                                }


                            }
                        } else {

                            //Toast.makeText(getContext(),"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return email;
    }

    @Override
    public void abrirURL(String url) {

        String aux = "";

        if(url.length() > 4){

            for (int i = 0 ; i < 4 ; i++){

                char c = url.charAt(i);

                aux = aux + c;


            }

            if(aux.equals("www.")){

                if( Patterns.WEB_URL.matcher("https://" + url).matches()){
                    Uri uri = Uri.parse("https://" + url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this,"Lo sentimos pero la URL del video no esta disponible...",Toast.LENGTH_SHORT).show();
                }



            }
            else if(aux.equals("http")){

                if( Patterns.WEB_URL.matcher(url).matches()){
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this,"Lo sentimos pero la URL del video no esta disponible...",Toast.LENGTH_SHORT).show();
                }
            }


            else{

                if( Patterns.WEB_URL.matcher("https://www." + url).matches()){
                    Uri uri = Uri.parse("https://www." + url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this,"Lo sentimos pero la URL del video no esta disponible...",Toast.LENGTH_SHORT).show();
                }

            }

        }

        else{
            Toast.makeText(this,"Lo sentimos la url no esta disponible",Toast.LENGTH_SHORT).show();
        }







    }

    public void mensaje(String x ){
        Toast.makeText(this,x,Toast.LENGTH_SHORT).show();
    }

    public void cargarNotificacion(String ruta){

        //hacemos la query en la coleccion recetas

        db = FirebaseFirestore.getInstance();

        db.collection("recetas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals(ruta)) {

                                    //creamos un objeto Receta
                                    String id = document.getString("id").toString();
                                    String nombre = document.getString("nombre").toString();
                                    String ingredientes = document.getString("ingredientes").toString();
                                    String dificultad = document.getString("dificultad").toString();
                                    String descripcion = document.getString("descripcion").toString();
                                    String imagePath = document.getString("imagePath").toString();
                                    String urlYoutube = document.getString("urlYoutube").toString();
                                    String valoracion = document.getString("valoracion").toString();
                                    String visitas = document.getString("visitas").toString();
                                    String userpath = document.getString("userPath").toString();

                                    Receta r = new Receta(id,nombre,ingredientes,descripcion,urlYoutube,imagePath,userpath,visitas,valoracion);
                                    r.setDificultad(dificultad);

                                    Bundle bundle = new Bundle();

                                    bundle.putString("id",r.getId());
                                    bundle.putString("nombre", r.getNombre());
                                    bundle.putString("ingredientes", r.getIngredientes());
                                    bundle.putString("dificultad",r.getDificultad());
                                    bundle.putString("descripcion", r.getDescripcion());
                                    bundle.putString("urlYoutube", r.getUrlYoutube());
                                    bundle.putString("userPath", r.getUserPath());
                                    bundle.putString("imagePath", r.getImagePath());
                                    bundle.putString("valoracion",r.getValoracion());
                                    bundle.putString("visitas", r.getVisitas());

                                    navController.navigate(R.id.fragment_recetaDetalle,bundle);
                                }
                                else;
                                //Toast.makeText(getContext(),"no encontrado",Toast.LENGTH_SHORT).show();

                            }
                        } else {

                            //Toast.makeText(this,"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });





    }

    public void cargarNotificacionCreador(String ruta){

        //hacemos la query en la coleccion recetas

        db = FirebaseFirestore.getInstance();

        db.collection("recetas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(document.getId().equals(ruta)) {

                                    //creamos un objeto Receta
                                    String id = document.getString("id").toString();
                                    String nombre = document.getString("nombre").toString();
                                    String ingredientes = document.getString("ingredientes").toString();
                                    String dificultad = document.getString("dificultad").toString();
                                    String descripcion = document.getString("descripcion").toString();
                                    String imagePath = document.getString("imagePath").toString();
                                    String urlYoutube = document.getString("urlYoutube").toString();
                                    String valoracion = document.getString("valoracion").toString();
                                    String visitas = document.getString("visitas").toString();
                                    String userpath = document.getString("userPath").toString();

                                    Receta r = new Receta(id,nombre,ingredientes,descripcion,urlYoutube,imagePath,userpath,visitas,valoracion);
                                    r.setDificultad(dificultad);

                                    Bundle bundle = new Bundle();

                                    bundle.putString("id",r.getId());
                                    bundle.putString("nombre", r.getNombre());
                                    bundle.putString("ingredientes", r.getIngredientes());
                                    bundle.putString("dificultad",r.getDificultad());
                                    bundle.putString("descripcion", r.getDescripcion());
                                    bundle.putString("urlYoutube", r.getUrlYoutube());
                                    bundle.putString("userPath", r.getUserPath());
                                    bundle.putString("imagePath", r.getImagePath());
                                    bundle.putString("valoracion",r.getValoracion());
                                    bundle.putString("visitas", r.getVisitas());

                                    navController.navigate(R.id.fragment_receta_detalle_sinPerfil,bundle);
                                }
                                else;
                                //Toast.makeText(getContext(),"no encontrado",Toast.LENGTH_SHORT).show();

                            }
                        } else {

                            //Toast.makeText(this,"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });





    }

    public void borrarReceta(String id){

        

        db = FirebaseFirestore.getInstance();

        db.collection("recetas").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                x = true;
            }
        });

        db = FirebaseFirestore.getInstance();

        db.collection("valoraciones").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                y = true;
            }
        });

        favoritos = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                    ArrayList<String>  favs = (ArrayList)document.get("favoritas");
                                    if(favs.contains(id)){
                                        favs.remove(id);
                                        db.collection("usuarios").document(document.getId()).update("favoritas",favs);

                                    }





                            }
                        } else {

                            //Toast.makeText(getContext(),"error obteniendo los datos...",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                z = true;
            }
        });






        if (x && y && z){
            Toast.makeText(this,"Se ha borrado satisfactoriamente la receta",Toast.LENGTH_SHORT).show();


            navController.navigate(R.id.fragmentHome);
        }
        else if(!x){
            Toast.makeText(this,"Error en x",Toast.LENGTH_SHORT).show();


            navController.navigate(R.id.fragmentHome);
        }
        else if(!y){
            Toast.makeText(this,"Error en y",Toast.LENGTH_SHORT).show();


            navController.navigate(R.id.fragmentHome);
        }
        else if(!z){
            Toast.makeText(this,"Error en z",Toast.LENGTH_SHORT).show();


            navController.navigate(R.id.fragmentHome);
        }
        else{


            navController.navigate(R.id.fragmentHome);
        }


    }

    @Override
    public void modificarReceta(String recipePath) {

    }

    @Override
    public void borrarFotoRecetaBorrada(String imagePath){

        //buscamos la receta
        StorageReference storageRef = storage.getReference();

        // Create a reference to the file to delete
        StorageReference desertRef = storageRef.child(imagePath);

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
            Toast.makeText(this,"hecho",Toast.LENGTH_SHORT).show();




    }



}