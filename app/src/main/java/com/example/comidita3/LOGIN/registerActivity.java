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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class registerActivity extends AppCompatActivity {

    TextView volver;
    EditText usuario,correo,contraseña,confirmacion;
    String user,email,password,confir,UID;
    Button registrarse;
    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;

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

        usuario = findViewById(R.id.editTextNombreUsuarioRegister);
        correo = findViewById(R.id.editTextCorreoRegistrar);
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

        user = usuario.getText().toString();
        email = correo.getText().toString();
        password = contraseña.getText().toString();
        confir = confirmacion.getText().toString();

        if(!email.isEmpty() && !password.isEmpty() && !confir.isEmpty() && !user.isEmpty()){

            if(!(password.length()<6) && !(confir.length()<6)){
                if(user.length()<15){
                    if(password.contentEquals(confir)){
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()) {

                                    //authentication
                                    FirebaseUser user2 = mAuth.getCurrentUser();
                                    user2.sendEmailVerification();

                                    //firestore

                                    UID = mAuth.getCurrentUser().getUid();


                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    ArrayList<String> favs = new ArrayList<>();
                                    ArrayList<String> subs = new ArrayList<>();
                                    subs.add("NINGUNA");

                                    Map<String,Object> userReg = new HashMap<>();
                                    userReg.put("UID",UID);
                                    userReg.put("nombre",user);
                                    userReg.put("correo",email);
                                    userReg.put("perfilPath","");
                                    userReg.put("favoritas",favs);
                                    userReg.put("suscripciones",subs);


                                    db.collection("usuarios").document(UID).set(userReg).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            showAlert("¡Enhorabuena!","¡Se ha registrado correctamente, confirme su correo y disfrute! ");

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showAlert("Error","algo salio mal...pruebe otra vez");
                                        }
                                    });



                                }

                                else
                                    showAlert("Error","Esta cuenta ya existe/valida el correo");

                            }
                        });
                    }
                    else{
                        Toast.makeText(this,"las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(this,"El nombre de usuario debe de tener menos de 15 caracteres...",Toast.LENGTH_SHORT).show();
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


        Intent intent = new Intent(getApplicationContext(),loginActivity.class);
        startActivity(intent);
        finish();
    }
}