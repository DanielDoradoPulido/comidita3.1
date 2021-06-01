package com.example.comidita3.clasesPOJO;

import java.util.Comparator;

public class comparatorPopulares implements Comparator<RecetaValorizada> {


    @Override
    public int compare(RecetaValorizada o1, RecetaValorizada o2) {

        if(o1.getValor()>o2.getValor()){

            if(o1.getNumVotaciones() + 10 < o2.getNumVotaciones())
                return  -1;
            else if(o1.getNumVotaciones() > o2.getNumVotaciones())
                return -1;
            else if(o1.getNumVotaciones() == o2.getNumVotaciones())
                return -1;

            else{
                return 1;
            }

        }
        else if(o1.getValor()<o2.getValor()){

            if(o1.getNumVotaciones() > 10 + o2.getNumVotaciones())
                return  1;
            else if(o1.getNumVotaciones() < o2.getNumVotaciones())
                return 1;
            else if(o1.getNumVotaciones() == o2.getNumVotaciones())
                return 1;

            else{
                return -1;
            }

        }
        else if(o1.getValor() == o2.getValor()){
            if(o1.getNumVotaciones() > o2.getNumVotaciones())
                return -1;
            else if(o1.getNumVotaciones() < o2.getNumVotaciones())
                return 1;
            else
                return 0;

        }


        return 0;
    }
}
