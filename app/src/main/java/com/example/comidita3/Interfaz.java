package com.example.comidita3;

import com.example.comidita3.adaptadores.adaptadorAjustes;
import com.example.comidita3.adaptadores.adaptadorFavoritos;
import com.example.comidita3.adaptadores.adaptadorRecetasSubidas;

public interface Interfaz {


    //fragment de subidas
    public adaptadorRecetasSubidas getAdaptadorRecetasSubidas();
    public void loadDataSubidas();

    //fragment de subidas por otro user
    public adaptadorRecetasSubidas getAdaptadorRecetasSubidasOther();
    public void loadDataSubidasOther(String s);

    //fragment favorites
    public adaptadorFavoritos getAdaptadorFavoritos();
    public void loadDataFavoritos();

    //notificaciones push
    public void subscribir(int opcion);
    public void desubscripcion();
    public void enviarNotificacion(String tipo,String url);

    public void irLogin();

    public String email();

    public void abrirURL(String url);


}
