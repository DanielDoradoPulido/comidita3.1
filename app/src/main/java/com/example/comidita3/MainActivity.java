package com.example.comidita3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.comidita3.LOGIN.loginActivity;
import com.example.comidita3.adaptadores.adaptadorAjustes;
import com.example.comidita3.adaptadores.adaptadorFavoritos;
import com.example.comidita3.adaptadores.adaptadorRecetasSubidas;
import com.example.comidita3.clasesPOJO.Receta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Interfaz{

    BottomNavigationView bottomNavigationView;
    NavHostFragment navHostFragment;
    NavController navController;
    String email;

    //listAdaptadores
    ArrayList<Receta> subidas;


    //cargar ajustes
    adaptadorAjustes arrayAdapterAjustes;
    adaptadorFavoritos arrayAdapterFavoritos;
    adaptadorRecetasSubidas arrayAdapterSubidas;
    public FirebaseFirestore db;

    //authentication
    private FirebaseAuth mAuth;


    public static ArrayList<String> favoritos = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //extraer info login



        Intent intent = getIntent();




        email = intent.getStringExtra("enviar");

        mAuth = FirebaseAuth.getInstance();







        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView,navController);



    }


    @Override
    public adaptadorFavoritos getAdaptadorFavoritos() {
        arrayAdapterFavoritos = new adaptadorFavoritos(this, R.layout.adaptador_layout,favoritos);

        return arrayAdapterFavoritos;
    }

    public void loadData(){

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
                                String descripcion = document.getString("descripcion").toString();
                                String imagePath = document.getString("imagePath").toString();
                                String urlYoutube = document.getString("urlYoutube").toString();
                                String valoracion = document.getString("valoracion").toString();
                                String visitas = document.getString("visitas").toString();
                                String userpath = document.getString("userPath").toString();

                                Receta r = new Receta(id,nombre,ingredientes,descripcion,urlYoutube,imagePath,userpath,visitas,valoracion);

                                //lo añadimos a lista de subidas
                                subidas.add(r);
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
    public void irLogin() {
        Intent intent = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(intent);
    }

    @Override
    public String email() {

        String direccion = email;

        return direccion;
    }

    public void mensaje(String x ){
        Toast.makeText(this,x,Toast.LENGTH_SHORT).show();
    }



}