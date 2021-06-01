package com.example.comidita3.clasesPOJO;

import java.util.Comparator;

public class RecetaValorizada implements Comparable<RecetaValorizada> {

    String id;
    Float valor;
    Integer numVisitas;

    public RecetaValorizada(String id, Float valor, Integer numVisitas) {
        this.id = id;
        this.valor = valor;
        this.numVisitas = numVisitas;
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

    public Integer getNumVisitas() {
        return numVisitas;
    }

    public void setNumVisitas(Integer numVisitas) {
        this.numVisitas = numVisitas;
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
