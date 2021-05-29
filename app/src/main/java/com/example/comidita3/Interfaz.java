package com.example.comidita3;

import android.app.PendingIntent;

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
    public void desubscripcion(int posicion);
    public void enviarNotificacion(String tipo,String url);
    public void versionMayor(String title,String detail,String recipePath);
    public void versionMenor(String title,String detail,String recipePath);
    public PendingIntent clickNoti(String recipePath);


    public void irLogin();

    public String email();

    public void abrirURL(String url);

    public void borrarReceta(String recipePath);
    public void borrarFotoRecetaBorrada(String imagePath);

    public void modificarReceta(String recipePath);


}
