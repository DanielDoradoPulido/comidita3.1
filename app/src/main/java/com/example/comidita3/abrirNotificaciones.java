package com.example.comidita3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.comidita3.clasesPOJO.Receta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class abrirNotificaciones extends AppCompatActivity {


    TextView  text;
    String ruta;
    public FirebaseFirestore db;
    BottomNavigationView bottomNavigationView;
    NavHostFragment navHostFragment;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_notificaciones);




        text = findViewById(R.id.textViewNotificacion);

        //obtenemos la info de la notificacion

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferencias_Recetas), Context.MODE_PRIVATE);
        ruta = sharedPref.getString(getString(R.string.rutaReceta),"nofunciono");
        text.setText(ruta);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("notif",ruta);
        intent.putExtra("inicia",true);
        startActivity(intent);






        //Toast.makeText(this,x,Toast.LENGTH_SHORT).show();



    }
}