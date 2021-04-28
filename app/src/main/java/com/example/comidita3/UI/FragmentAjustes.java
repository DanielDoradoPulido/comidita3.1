package com.example.comidita3.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.comidita3.Interfaz;
import com.example.comidita3.LOGIN.recuperarContrasena;
import com.example.comidita3.MainActivity;
import com.example.comidita3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class FragmentAjustes extends Fragment {

   ListView listView;
   ArrayAdapter adaptador;
   Interfaz contexto;

    private FirebaseAuth mAuth;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentAjustes() {
        // Required empty public constructor
    }


    public static FragmentAjustes newInstance(String param1, String param2) {
        FragmentAjustes fragment = new FragmentAjustes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contexto = (Interfaz)context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ajustes, container, false);

        listView = v.findViewById(R.id.listViewAjustes);

        mAuth = FirebaseAuth.getInstance();

        ArrayAdapter ad = contexto.getAdaptadorAjustes();

        listView.setAdapter(ad);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(ad.getItem(position).toString().equals("Cerrar Sesión")){

                    AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
                    builder.setTitle("¿Cerrar Sesión?")
                            .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                            .setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //borrar cookies
                                    SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preferencias_PMDM_correo_file), Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString(getString(R.string.preferencias_email), "");
                                    editor.putString(getString(R.string.preferencias_password), "");
                                    editor.putBoolean(getString(R.string.preferencias_islogin), false);
                                    editor.commit();

                                    //desloguear firebase account

                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if(user != null){
                                        mAuth.signOut();
                                    }

                                    Toast.makeText(getContext(),"Se ha cerrado sesion",Toast.LENGTH_SHORT).show();

                                    contexto.irLogin();

                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alerta = builder.create();
                    alerta.show();

                }

                if(ad.getItem(position).toString().equals("Cambiar Correo Electrónico"))
                    Toast.makeText(getContext(),"cambiar correo elec",Toast.LENGTH_SHORT).show();
                if(ad.getItem(position).toString().equals("Cambiar Contraseña"))
                    Toast.makeText(getContext(),"cambiar contra",Toast.LENGTH_SHORT).show();


            }
        });




        return v;

    }


}