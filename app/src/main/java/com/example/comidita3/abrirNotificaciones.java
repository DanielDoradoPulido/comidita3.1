package com.example.comidita3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.comidita3.LOGIN.loginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class abrirNotificaciones extends AppCompatActivity {


    TextView  text;
    String ruta;
    public FirebaseFirestore db;
    BottomNavigationView bottomNavigationView;
    NavHostFragment navHostFragment;
    NavController navController;
    String correoElec,contra;
    boolean prelogueado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_notificaciones);

        Intent intent = getIntent();

        prelogueado = false;

        if(intent!=null){

            prelogueado = intent.getBooleanExtra("log",false);

            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferencias_PMDM_correo_file), Context.MODE_PRIVATE);
            correoElec = sharedPref.getString(getString(R.string.preferencias_email),"nofunciono");
            contra = sharedPref.getString(getString(R.string.preferencias_password),"nofunciono");
            boolean isLogin = sharedPref.getBoolean(getString(R.string.preferencias_islogin),false);

            if(isLogin) {

                mostrarNoti();




            }

            else if(!isLogin && !prelogueado){

                mostrarNoti();

            }
            else{

                Intent intent2 = new Intent(getApplicationContext(), loginActivity.class);
                intent2.putExtra("notif", true);
                startActivity(intent2);
                finish();


            }

        }








        //Toast.makeText(this,x,Toast.LENGTH_SHORT).show();



    }

    public void mostrarNoti(){

        text = findViewById(R.id.textViewNotificacion);

        //obtenemos la info de la notificacion

        SharedPreferences sharedPref2 = getSharedPreferences(getString(R.string.preferencias_Recetas), Context.MODE_PRIVATE);
        ruta = sharedPref2.getString(getString(R.string.rutaReceta), "nofunciono");
        text.setText(ruta);

        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
        intent2.putExtra("notif", ruta);
        intent2.putExtra("abrir", true);
        startActivity(intent2);
        finish();

    }
}