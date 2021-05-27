package com.example.comidita3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.comidita3.LOGIN.loginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService  {

    private FirebaseAuth mAuth;
    String title;
    String body;




    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            title = remoteMessage.getData().get("titulo");
            body = remoteMessage.getData().get("detalle");
            String recipePath =remoteMessage.getData().get("recipePath");
            String userUID = remoteMessage.getData().get("userID");



            comprobarUsuario(userUID);


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                versionMayor(title,body,recipePath);
            else
                versionMenor(title,body,recipePath);

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {


        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
    }



    public void versionMayor(String titulo,String detalle,String recipePath){

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferencias_Recetas), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.rutaReceta), recipePath);
        editor.commit();

        String id = "mensaje";

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nc = new NotificationChannel(id,"nuevo",NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            assert nm != null;
            nm.createNotificationChannel(nc);
        }

        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(titulo)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(detalle)
                .setContentIntent(clicknoti(recipePath))
                .setContentInfo("nuevo");

        Random random = new Random();
        int idNotify = random.nextInt(8000);

        assert nm !=null;
        nm.notify(idNotify,builder.build());





    }

    public void versionMenor(String titulo,String detalle,String recipePath){

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferencias_Recetas), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.rutaReceta), recipePath);
        editor.commit();

        String id = "mensaje";

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id);

        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(titulo)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(detalle)
                .setContentIntent(clicknoti(recipePath))
                .setContentInfo("nuevo");

        Random random = new Random();
        int idNotify = random.nextInt(8000);

        assert nm !=null;
        nm.notify(idNotify,builder.build());

    }

    public PendingIntent clicknoti(String url){
        Intent nf = new Intent(getApplicationContext(), abrirNotificaciones.class);
        nf.putExtra("url",url);

        nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this,0,nf,0);


    }

    public void comprobarUsuario(String UID){

        mAuth = FirebaseAuth.getInstance();


        if(mAuth!= null & mAuth.getUid().equals(UID)){

            title = "¡Se ha subido tu receta! \uD83D\uDE0E";
            body = "Echa un vistazo a como ha quedado \uD83C\uDF72" ;


        }

    }

}
