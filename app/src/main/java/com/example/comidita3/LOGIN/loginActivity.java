package com.example.comidita3.LOGIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comidita3.LOGIN.recuperarContrasena;
import com.example.comidita3.LOGIN.registerActivity;
import com.example.comidita3.MainActivity;
import com.example.comidita3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {

    Button login;
    TextView recuContra,register;
    CheckBox recordar;
    EditText correo,contraseña;
    String email,password,correoElec,contra;



    public static final String enviar = "com.example.myfirstApp.MESSAGE";

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //pares clave-valor
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferencias_PMDM_correo_file), Context.MODE_PRIVATE);
        correoElec = sharedPref.getString(getString(R.string.preferencias_email),"nofunciono");
        contra = sharedPref.getString(getString(R.string.preferencias_password),"nofunciono");

        recordar = findViewById(R.id.checkBox);



        boolean isLogin = sharedPref.getBoolean(getString(R.string.preferencias_islogin),false);
        if (isLogin) {



            FirebaseAuth.getInstance().signInWithEmailAndPassword(correoElec,contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();

                        if(user.isEmailVerified()) {

                            showMain(correoElec);

                        }

                    }





                }
            });



        }


        mAuth = FirebaseAuth.getInstance();



        correo = findViewById(R.id.editTextCorreo);
        contraseña = findViewById(R.id.editTextContraseña);

        login = findViewById(R.id.buttonLog);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();

            }
        });

        register = findViewById(R.id.textViewCrearCuenta);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegister();
            }
        });

        recuContra = findViewById(R.id.textViewRecuperarContraseña);

        recuContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperarContraseña();
            }
        });


    }

    public void login(){

        email = correo.getText().toString();
        password = contraseña.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()){

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();

                        if(user.isEmailVerified()) {

                            if(recordar.isChecked()){
                                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferencias_PMDM_correo_file), Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(getString(R.string.preferencias_email), email);
                                editor.putString(getString(R.string.preferencias_password), password);
                                editor.putBoolean(getString(R.string.preferencias_islogin), true);
                                editor.commit();

                                showMain(email);
                            }
                            else{
                                showMain(email);
                            }

                        }
                        else
                            showAlert("Error","Comprueba que has validado el correo");
                    }



                    else
                        showAlert("Error","Este correo no tiene cuenta registrada, registrese por favor");

                }
            });

        }
        else{
            Toast.makeText(this,"no estan rellenos los campos",Toast.LENGTH_SHORT).show();
        }
    }





    public void showRegister(){

        Intent intent = new Intent(getApplicationContext(), registerActivity.class);
        startActivity(intent);





    }

    public void showAlert(String title,String mensaje){

        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(mensaje)
                .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alerta = builder.create();
        alerta.show();

    }

    public void showMain(String s){

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(enviar,s);
        startActivity(intent);

        correo.setText("");
        contraseña.setText("");


    }

    public void recuperarContraseña(){

        Intent intent = new Intent(getApplicationContext(), recuperarContrasena.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();




    }
}