package com.example.comidita3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.example.comidita3.LOGIN.loginActivity;
import com.example.comidita3.adaptadores.adaptadorAjustes;
import com.example.comidita3.adaptadores.adaptadorFavoritos;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Interfaz {

    BottomNavigationView bottomNavigationView;
    NavHostFragment navHostFragment;
    NavController navController;
    String email;



    //cargar ajustes
    adaptadorAjustes arrayAdapterAjustes;
    adaptadorFavoritos arrayAdapterFavoritos;
    public static ArrayList<String> subidas = new ArrayList<>();
    public static ArrayList<String> favoritos = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //extraer info login

        Intent intent = getIntent();

        email = intent.getStringExtra("enviar");

        subidas.add("subida1");
        subidas.add("subida2");
        subidas.add("subida3");
        subidas.add("subida4");
        subidas.add("subida5");
        subidas.add("subida6");
        subidas.add("subida7");
        subidas.add("subida8");
        subidas.add("subida9");
        subidas.add("subida10");

        favoritos.add("favoritos1");
        favoritos.add("favoritos2");
        favoritos.add("favoritos3");
        favoritos.add("favoritos4");
        favoritos.add("favoritos5");
        favoritos.add("favoritos6");
        favoritos.add("favoritos7");
        favoritos.add("favoritos8");
        favoritos.add("favoritos9");
        favoritos.add("favoritos10");




        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView,navController);



    }

    @Override
    public adaptadorAjustes getAdaptadorAjustes() {

        arrayAdapterAjustes = new adaptadorAjustes(this, R.layout.adaptador_layout,subidas);

        return arrayAdapterAjustes;
    }

    @Override
    public adaptadorFavoritos getAdaptadorFavoritos() {
        arrayAdapterFavoritos = new adaptadorFavoritos(this, R.layout.adaptador_layout,favoritos);

        return arrayAdapterFavoritos;
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


}