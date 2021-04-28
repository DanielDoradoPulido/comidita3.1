package com.example.comidita3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.comidita3.LOGIN.loginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Interfaz {

    BottomNavigationView bottomNavigationView;
    NavHostFragment navHostFragment;
    NavController navController;

    //cargar ajustes
    adaptadorAjustes arrayAdapterAjustes;
    public static ArrayList<String> opciones = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //ajustes
        opciones = new ArrayList<String>();

        opciones.add("Cambiar Correo Electrónico");
        opciones.add("Cambiar Contraseña");
        opciones.add("Cerrar Sesión");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView,navController);



    }

    @Override
    public adaptadorAjustes getAdaptadorAjustes() {

        arrayAdapterAjustes = new adaptadorAjustes(this, R.layout.adaptador_layout,opciones);

        return arrayAdapterAjustes;
    }

    @Override
    public void irLogin() {
        Intent intent = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(intent);
    }


}