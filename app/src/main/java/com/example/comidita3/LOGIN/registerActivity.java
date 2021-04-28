package com.example.comidita3.LOGIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comidita3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registerActivity extends AppCompatActivity {

    TextView volver;
    EditText correo,contraseña,confirmacion;
    String email,password,confir;
    Button registrarse;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        registrarse = findViewById(R.id.buttonRegistrarse);

        volver = findViewById(R.id.textViewVolverLoginDesdeRegistro);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLogin();

            }
        });

        correo = findViewById(R.id.editTextCorreoRecuperar);
        contraseña = findViewById(R.id.editTextContraseñaRegistrarse);
        confirmacion = findViewById(R.id.editTextContraseñaRegistrarse2);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();



            }
        });






    }


    public void register(){

        email = correo.getText().toString();
        password = contraseña.getText().toString();
        confir = confirmacion.getText().toString();

        if(!email.isEmpty() && !password.isEmpty() && !confir.isEmpty()){

            if(!(password.length()<6) && !(confir.length()<6)){
                if(password.contentEquals(confir)){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();
                                user.sendEmailVerification();
                                showAlert("¡Enhorabuena!","registro satisfactorio,verifique su cuenta");

                            }

                            else
                                showAlert("Error","Esta cuenta ya existe/valida el correo");

                        }
                    });
                }
                else{
                    Toast.makeText(this,"las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                }
            }

            else
                Toast.makeText(this,"la contraseña debe de tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(this,"no estan rellenos los campos",Toast.LENGTH_SHORT).show();
        }




    }

    public void showAlert(String title,String mensaje){

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

        Intent intent = new Intent(getApplicationContext(), loginActivity.class);

        startActivity(intent);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(),loginActivity.class);
        startActivity(intent);
        finish();
    }
}