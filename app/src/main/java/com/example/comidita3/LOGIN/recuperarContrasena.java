package com.example.comidita3.LOGIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comidita3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class recuperarContrasena extends AppCompatActivity {

    ImageView logo;
    TextView volver,title,subtitle;
    Button recuperar;
    EditText correo;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        //shared animations
        logo = findViewById(R.id.imageViewLogoContraseñaActivity);
        volver = findViewById(R.id.textViewVolverLoginDesdeRecuperarContraseña);
        title = findViewById(R.id.textViewTitleContraseñaActivity);
        subtitle = findViewById(R.id.textViewSubtitleContraseñaActivity);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLogin();

            }
        });

        correo  = findViewById(R.id.editTextNombreUsuarioRegister);

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

        Intent sharedIntent = new Intent(recuperarContrasena.this, loginActivity.class);

        Pair[] pairs = new Pair[4];

        pairs[0] = new Pair<View,String>(logo,"imageTransition");
        pairs[1] = new Pair<View,String>(title,"titleText");
        pairs[2] = new Pair<View,String>(subtitle,"subtitleText");
        pairs[3] = new Pair<View,String>(recuperar,"botonTransition");


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(recuperarContrasena.this,pairs);

        startActivity(sharedIntent,options.toBundle());




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