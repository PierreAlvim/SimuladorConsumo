/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorconsumo;

import java.util.ArrayList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Pierre
 */
public class OtimizacaoIterativa {

    private static final int MAX_POINTS = 301;
    private static double passo;

    static void runOtimizacao(LineChart.Series<Number, Number> list, EmpresaCliente empC, EmpresaGeradora empG) {
        list.getData().clear();
        System.out.println("TAMANHO:"+ list.getData().size());
        passo = 0.004 / (MAX_POINTS);
        EmpresaCliente auxC = empC.clone();
        //Animação inicial (envolver a cota primeira)
        double cotaInicio = empC.getCota();
        ArrayList<XYChart.Data<Double,Double>> picosEconomia = new ArrayList();
        double econoAnt = auxC.getEconomia();// ;
        XYChart.Data<Double,Double> econoMax = new XYChart.Data<>(cotaInicio, .0);
        int j = 0;
        for (int i = -MAX_POINTS / 2; i < MAX_POINTS / 2; i++) {
            double cota = cotaInicio + i * passo;
            auxC.setCota(cota);
            auxC.calculoCustosCredit(empG);
            double eco = auxC.getEconomia();
            if (econoAnt > eco)
                picosEconomia.add(new XYChart.Data<>((cota - i * passo), econoAnt));
            if (eco > econoMax.getYValue()) {
                econoMax.setXValue(cota);
                econoMax.setYValue(eco);
                j = 0;
                System.out.println("Maior:" + eco);
            }
            System.out.println(cota + ":" + eco);
            econoAnt = eco;
            list.getData().add(new XYChart.Data(cota, eco));
            j++;
        }
        
        int i = MAX_POINTS/2;
        j=0; //Distancia até o valor maximo
        while (j < MAX_POINTS/2 && i <MAX_POINTS*10) {
            double cota = cotaInicio + i * passo;
            auxC.setCota(cota);
            auxC.calculoCustosCredit(empG);
            double eco = auxC.getEconomia();
            if (econoAnt > eco)
                picosEconomia.add(new XYChart.Data<>((cota-i*passo), eco));
            if (eco > econoMax.getYValue()) {
                econoMax.setXValue(cota);
                econoMax.setYValue(eco);
                System.out.println("distancia: " + j);
                j = 0;
            }
            econoAnt = eco;
            list.getData().add(new XYChart.Data(cota, eco));
            list.getData().remove(0);
            j++;
            i++;
        }
        empC.setCota(econoMax.getXValue());
        list.getData().sort((t, t1) -> {
            int a = (double) t.getYValue() >= (double) t1.getYValue() ? 1 : -1;
            //System.out.println(a);
            return a;
        });
    }

}
