package com.example.comidita3.Objetos;

public class RecetaVisitada implements Comparable<RecetaVisitada>{

    String id;
    Integer visitas;

    public RecetaVisitada(String id, Integer visitas) {
        this.id = id;
        this.visitas = visitas;
    }

    public RecetaVisitada(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVisitas() {
        return visitas;
    }

    public void setVisitas(Integer visitas) {
        this.visitas = visitas;
    }


    @Override
    public int compareTo(RecetaVisitada o) {
        if(o.getVisitas() > visitas)
            return 1;

        else if(o.getVisitas() < visitas)
            return -1;

        else
            return  0;


    }
}
