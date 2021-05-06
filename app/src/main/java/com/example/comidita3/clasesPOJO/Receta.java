package com.example.comidita3.clasesPOJO;

public class Receta {

    private String id;
    private String nombre;
    private String ingredientes;
    private String descripcion;
    private String urlYoutube;
    private String imagePath;
    private String userPath;
    private String visitas;
    private String valoracion;

    public Receta(String id, String nombre, String ingredientes, String descripcion, String urlYoutube, String imagePath, String userPath) {
        this.id = id;
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.descripcion = descripcion;
        this.urlYoutube = urlYoutube;
        this.imagePath = imagePath;
        this.userPath = userPath;
        this.visitas = "0";
        this.valoracion = "0";
    }

    public Receta(String id, String nombre, String ingredientes, String descripcion, String urlYoutube, String imagePath, String userPath, String visitas, String valoracion) {
        this.id = id;
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.descripcion = descripcion;
        this.urlYoutube = urlYoutube;
        this.imagePath = imagePath;
        this.userPath = userPath;
        this.visitas = visitas;
        this.valoracion = valoracion;
    }

    public Receta(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlYoutube() {
        return urlYoutube;
    }

    public void setUrlYoutube(String urlYoutube) {
        this.urlYoutube = urlYoutube;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUserPath() {
        return userPath;
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }

    public String getVisitas() {
        return visitas;
    }

    public void setVisitas(String visitas) {
        this.visitas = visitas;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }
}
