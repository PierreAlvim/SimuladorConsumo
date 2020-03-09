/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorconsumo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Pierre
 */
public enum Meses {

    Jan(1), Fev(2), Mar(3), Abr(4), Mai(5), Jun(6), Jul(7), Ago(8), Set(9), Out(10), Nov(11), Dez(12);

    private final int num;

    Meses(int num) {
        this.num = num;
    }

    int getNum() {
        return num;
    }
    

    public static List<Meses> getValues(){
        return Arrays.asList(values());
        
    }
 
    /**
     * Transforma valor inteiro em Mês
     *
     * @param i Número equivalente ao mês, caso seja maior que 12 então resto
     * com 12 sera usado.
     * @return Mês equivalente
     */
    public static Meses getValue(int i) {
        for (Meses mes : Meses.values()) {
            if (mes.getNum() == ((i > 12) ? i % 12 : i)) {
                return mes;
            }
        }
        throw new IllegalArgumentException("numero do mes invalido");
    }
    
    /**
     * Transforma valor decimal (representando binário) em lista de meses.<br>
     * Ex:  3       (000000000011) ->  [jan, fev]<br>
     *      45      (000000101101) ->  [jan, mar, abr, jun]<br>
     *      21      (000000010101) ->  [jan, mar, mai]<br>
     *      4095    (111111111111) ->  [jan, fev ... nov, dez]
     * @param value
     * @return 
     */
    public static List<Meses> getList(int value){
        ArrayList<Meses> list =new ArrayList();
        if(value > 4095)
            value = 4095;
        for(int i=0;i<12;i++ )
        {
            if(((value >> i) & 1) == 1)
                list.add(getValue(i+1));
        }
        return list;
    }


}
