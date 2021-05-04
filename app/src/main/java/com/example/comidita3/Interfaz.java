package com.example.comidita3;

import com.example.comidita3.adaptadores.adaptadorAjustes;
import com.example.comidita3.adaptadores.adaptadorFavoritos;

public interface Interfaz {

    public adaptadorAjustes getAdaptadorAjustes();
    public adaptadorFavoritos getAdaptadorFavoritos();

    public void irLogin();

    public String email();


}
