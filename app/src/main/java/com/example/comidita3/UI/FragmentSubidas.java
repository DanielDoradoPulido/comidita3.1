package com.example.comidita3.UI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.comidita3.Interfaz;
import com.example.comidita3.R;
import com.example.comidita3.adaptadores.adaptadorAjustes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSubidas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSubidas extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listViewItems;
    Interfaz contexto;
    FloatingActionButton fab;
    NavController navController;


    public FragmentSubidas() {
        // Required empty public constructor
    }



    public static FragmentSubidas newInstance(String param1, String param2) {
        FragmentSubidas fragment = new FragmentSubidas();
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
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        contexto = (Interfaz)context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subidas, container, false);

        fab = v.findViewById(R.id.floatingActionButtonAñadirSubidas);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.fragmentRellenarReceta);

            }
        });

        listViewItems = (ListView) v.findViewById(R.id.listViewSubidas);
        adaptadorAjustes ad = contexto.getAdaptadorAjustes();
        listViewItems.setAdapter(ad);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(),ad.getItem(position),Toast.LENGTH_SHORT).show();

            }
        });


        return v;

    }


}