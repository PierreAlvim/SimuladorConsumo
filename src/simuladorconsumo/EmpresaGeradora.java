/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorconsumo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Pierre
 */
public class EmpresaGeradora extends Empresa {

    private double geracaoMaxima;
    private ArrayList<GeracaoMensal> geracoesMensaisEstimadas;

    public EmpresaGeradora(double geracaoMaxima, String nome, String endereco, String tipo) {
        super(nome, endereco, tipo);
        this.geracaoMaxima = geracaoMaxima;
        geracoesMensaisEstimadas = new ArrayList<>();
    }

    public double getGeracaoMaxima() {
        return geracaoMaxima;
    }

    public void setGeracaoMaxima(double geracaoMaxima) {
        this.geracaoMaxima = geracaoMaxima;
    }

    public GeracaoMensal getGeracaoMes(Meses mes) {
        for(GeracaoMensal g : geracoesMensaisEstimadas)
            if(g.getMes() == mes)
                return g;
        
        System.out.println("ERRO Mes nao encontrado");
        return null;
    }
    
    public double geracaoMediaMensal(){
        double soma=0;
        for(GeracaoMensal g : geracoesMensaisEstimadas)
        {
            soma+=g.getGeracao();
        }
        return soma/12;
    }

    public boolean criarEstimativaGeracao(List<Double> c, Meses inicial) {
        if (c.size() == 12) {
            for (Number n : c) {
                geracoesMensaisEstimadas.add(new GeracaoMensal(this, inicial, geracaoMaxima, (double) n));
                inicial = Meses.getValue(inicial.getNum() + 1);
            }
            listarGeracaoMensal();
            return true;
        }
        System.out.println("ERRO 12 não encontrado");
        return false;
    }
    
    public void listarGeracaoMensal()
    {
        System.out.println("Lista Geração mensal:");
        geracoesMensaisEstimadas.forEach((g)->System.out.println(g));
    }

}
