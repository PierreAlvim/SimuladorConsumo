/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorconsumo;

/**
 *
 * @author Pierre
 */
public class CalculoCreditos {
    
    
    
    /**
     * Metodo auxiliar para formatação de String em numero de casa descimais fixo.
     * @param value numero double
     * @return String com 2 casas decinais
     */
    public static double trunc2(double value) {
        return Math.round(value * 100) / 100d;
    }
    
    public static double trunc4(double value) {
        return Math.round(value * 10000) / 10000d;
    }
    
    public static double trunc6(double value) {
        return Math.round(value * 1000000) / 1000000d;
    }
    
           
}
