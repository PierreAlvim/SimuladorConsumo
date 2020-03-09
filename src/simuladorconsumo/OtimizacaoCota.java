/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorconsumo;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Pierre
 */
public class OtimizacaoCota {

    private static int OTIM_LOOP_COUNT = 0;
    static final int nPontos = 200;
    static final int nPontosGrafico = 40;
    static final double ESPACO_AMOSTRAL = 0.001;
    static final double passo = ESPACO_AMOSTRAL / nPontos;
    static Double[] cotas = new Double[nPontos];
    static Double[] economia = new Double[nPontos];
    static TreeMap<Double, Double> out = new TreeMap<>();

    public static Map<Double, Double> simularCustos(EmpresaCliente empC, EmpresaGeradora empG) {

        if (OTIM_LOOP_COUNT == 0)
            out.clear();
        Double cota = empC.getCota() - ESPACO_AMOSTRAL / 2; //Valor inicial

        EmpresaCliente empAux = empC.clone();

        for (int i = 0; i < nPontos; i++) {
            if (cota > 0) {
                empAux.calculoCustosCredit(empG);
                empAux.setCota(cota);
                for (InfoMensal d : empAux.getDadosMensais())
                    d.setCreditoGasto(empG.getGeracaoMes(d.getMes()).getGeracao() * cota);
                empAux.calculoCustosCredit(empG);
                //System.out.println(out.get((cota*1000)));
                economia[i] = empAux.getEconomiaPercent();
            } else
                economia[i] = .0;
            cotas[i] = cota;
            out.put(cotas[i], economia[i]);
            cota += passo;
        }
        int posMax = findMax(economia, nPontos);
        Double maxC = cotas[posMax];
        empC.setCota(maxC);

        //continuar encontrando maximo caso este esteja nos limites
        if ((posMax <= (nPontos / 3) || posMax >= (nPontos - (nPontos / 3))) && OTIM_LOOP_COUNT < 15) {
            OTIM_LOOP_COUNT++;
            return simularCustos(empC, empG);
        } else {
            System.out.println("Executado: " + (OTIM_LOOP_COUNT + 1) + "x");
            OTIM_LOOP_COUNT = 0;
            double low = maxC - (nPontosGrafico / 2) * passo;
            double high = maxC + (nPontosGrafico / 2) * passo;
            return out.subMap(low, true, high, true);
        }
    }

//    public static double maximoLocal()
//    {
//        
//    }
    private static int findMax(Double[] ds, int size) {
        int m = 0;

        for (int i = 0; i < size; i++)
            if (ds[i] > ds[m])
                m = i;

        return m;

    }

}
