package com.example.comidita3.Objetos;

public class RecetaValorizada {

    String id;
    Float valor;
    Integer numVotaciones;

    public RecetaValorizada(String id, Float valor, Integer numVotaciones) {
        this.id = id;
        this.valor = valor;
        this.numVotaciones = numVotaciones;
    }

    public RecetaValorizada(String id, Float valor) {
        this.id = id;
        this.valor = valor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Integer getNumVotaciones() {
        return numVotaciones;
    }

    public void setNumVotaciones(Integer numVisitas) {
        this.numVotaciones = numVotaciones;
    }


}
