package com.example.comidita3;

import com.example.comidita3.adaptadores.adaptadorAjustes;
import com.example.comidita3.adaptadores.adaptadorFavoritos;
import com.example.comidita3.adaptadores.adaptadorRecetasSubidas;

public interface Interfaz {


    public adaptadorFavoritos getAdaptadorFavoritos();
    public adaptadorRecetasSubidas getAdaptadorRecetasSubidas();
    public void loadDataSubidas();

    public void irLogin();

    public String email();

    public void abrirURL(String url);


}
