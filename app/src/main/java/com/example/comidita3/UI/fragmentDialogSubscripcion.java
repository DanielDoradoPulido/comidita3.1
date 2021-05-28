package com.example.comidita3.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.comidita3.Interfaz;
import com.example.comidita3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class fragmentDialogSubscripcion extends DialogFragment {

    //objetos
    Interfaz contexto;

    int position = 0;
    Boolean pulsado = false;
    private FirebaseAuth mAuth;
    int suscritoYa = 9;





   public fragmentDialogSubscripcion(){

   }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        mAuth = FirebaseAuth.getInstance();

        //inicializar objetos

        String [] list = new String[8];
        list[0]="Recetas de rápida preparación";
        list[1]="Receta de preparación intermedia";
        list[2]="Receta de larga preparación";
        list[3] = "Recetas rápidas y medias";
        list[4] = "Recetas rápidas y largas";
        list[5] = "Recetas largas y medias";
        list[6] = "Todas las recetas";
        list[7] = "Ninguna";


        builder.setTitle("Selecciona el tipo de suscripción")

                .setSingleChoiceItems(list, 15, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        position = which;
                        pulsado  = true;



                    }
                }).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {




                if(pulsado){

                    String opc = list[position];

                    contexto.desubscripcion(position);

                    dialog.dismiss();




                   // Toast.makeText(getContext(),"¡Genial! , ahora recibiras notificaciones sobre " + opc , Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(getContext(),"No has pulsado" , Toast.LENGTH_SHORT).show();
                }

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getContext(),"La subscripcion ha sido cancelada", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        return builder.create();
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        contexto = (Interfaz) context;
    }


}
