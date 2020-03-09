/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasejson;

import simuladorconsumo.InfoMensal;
import simuladorconsumo.EmpresaCliente;
import simuladorconsumo.Meses;
import simuladorconsumo.ParametrosConsumo;

/**
 *
 * @author Pierre
 */
public class SimpleDataConsumo {
    private ParametrosConsumo.TipoRedeEletrica tipoRede;
    private Double cota;
    private Double tarifaMedia;
    private Double descontoAplicado;
    protected String nome;
    protected String endereco;
    protected String tipo;
    private int ano;
    double[] consumo;
    Meses[] meses;

    public SimpleDataConsumo(ParametrosConsumo.TipoRedeEletrica tipoRede, Double cota, Double tarifaMedia, Double descontoAplicado, String nome, String endereco, String tipo, int ano, double[] consumo, Meses[] meses) {
        this.tipoRede = tipoRede;
        this.cota = cota;
        this.tarifaMedia = tarifaMedia;
        this.descontoAplicado = descontoAplicado;
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
        this.ano = ano;
        this.consumo = consumo;
        this.meses = meses;
    }
    
    public EmpresaCliente toEmpCliente(){
       EmpresaCliente e = new EmpresaCliente(tipoRede, cota, nome, endereco, tipo, tarifaMedia, descontoAplicado);
       int auxAno = ano;
       for(int i=0;i<12;i++)
       {
           if(meses[i] !=null){
               e.addDadoMensal(new InfoMensal(meses[i], consumo[i], auxAno));
               if(meses[i] == Meses.Dez)
                   auxAno++;
           }
       }
       return e;
    }
}
