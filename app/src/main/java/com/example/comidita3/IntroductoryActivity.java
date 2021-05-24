package com.example.comidita3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.comidita3.LOGIN.loginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntroductoryActivity extends AppCompatActivity {

    ImageView fondo,logo,letras;
    LottieAnimationView lottieAnimationView;
    private FirebaseAuth mAuth;
    String correoElec,contra;
    public static final String enviar = "com.example.myfirstApp.MESSAGE";

    String imagePath;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);



        //login
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferencias_PMDM_correo_file), Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        //pares clave-valor

        correoElec = sharedPref.getString(getString(R.string.preferencias_email),"nofunciono");
        contra = sharedPref.getString(getString(R.string.preferencias_password),"nofunciono");
        boolean isLogin = sharedPref.getBoolean(getString(R.string.preferencias_islogin),false);

        fondo = findViewById(R.id.imageViewSplash);

        logo = findViewById(R.id.imageViewSplashLogo);

        letras = findViewById(R.id.imageViewSplashLetras);

        lottieAnimationView = findViewById(R.id.animationSplashScreen);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

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
                else{

                    Intent intent = new Intent(getApplicationContext(), loginActivity.class);

                    startActivity(intent);

                }
            }
        },6300);

        fondo.animate().translationY(-2300).setDuration(1000).setStartDelay(5000);
        logo.animate().translationY(2000).setDuration(1000).setStartDelay(5000);
        lottieAnimationView.animate().translationY(1600).setDuration(1000).setStartDelay(5000);
        letras.animate().translationY(2000).setDuration(1000).setStartDelay(5000);


    }

    public void showMain(String s){

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(enviar,s);
        startActivity(intent);
        finish();




    }


}