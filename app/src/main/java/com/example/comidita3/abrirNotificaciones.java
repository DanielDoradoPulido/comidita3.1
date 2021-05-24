package com.example.comidita3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class abrirNotificaciones extends AppCompatActivity {

    String x;
    TextView  text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_notificaciones);

        Intent intent = getIntent();

        text = findViewById(R.id.textViewNotificacion);

        x = intent.getStringExtra("url");

        if(intent==null)
            text.setText("nulo");
        else{
            text.setText(x);
        }




        //Toast.makeText(this,x,Toast.LENGTH_SHORT).show();



    }
}