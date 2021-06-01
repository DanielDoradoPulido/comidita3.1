package com.example.comidita3.clasesPOJO;

import java.util.Comparator;

public class RecetaValorizada implements Comparable<RecetaValorizada> {

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

    @Override
    public int compareTo(RecetaValorizada o) {
        if(o.getValor() > valor){
            return 1;
        }
        else if(o.getValor()>= valor){
            return 0;
        }
        else{
            return -1;
        }
    }
}
