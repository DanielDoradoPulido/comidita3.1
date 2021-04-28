package com.example.comidita3.LOGIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comidita3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class recuperarContrasena extends AppCompatActivity {

    TextView volver;
    Button recuperar;
    EditText correo;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        volver = findViewById(R.id.textViewVolverLoginDesdeRecuperarContraseña);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLogin();

            }
        });

        correo  = findViewById(R.id.editTextCorreoRecuperar);

        recuperar = findViewById(R.id.buttonRegistrarse);

        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate();

            }
        });

    }

    public void validate(){

        String email = correo.getText().toString().trim();

        if(email.isEmpty()  || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Introduce un correo valido",Toast.LENGTH_SHORT).show();

        }

        else{
            sendEmail(email);
        }




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(intent);
        finish();
    }

    public void showAlert(String title, String mensaje){

        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(mensaje)
                .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showLogin();
                    }
                });

        AlertDialog alerta = builder.create();
        alerta.show();

    }

    public void showLogin(){

        Intent intent = new Intent(getApplicationContext(),loginActivity.class);

        startActivity(intent);




    }

    public void sendEmail(String email){

        FirebaseAuth auth = FirebaseAuth.getInstance();

        String emailAddress = email;

        auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    showAlert("Realizado","Correo de recuperacion enviado");

                }
                else{
                    showAlert("Error","El correo no tiene cuenta");
                }

            }
        });




    }
}