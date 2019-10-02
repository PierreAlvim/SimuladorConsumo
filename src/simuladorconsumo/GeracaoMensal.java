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
public class GeracaoMensal {
    private Empresa empresa;
    private Meses mes;
    private double geracaoMaxima;
    private double porcentagemGerada;

    public GeracaoMensal() {
    }

    public GeracaoMensal(Empresa empresa, Meses mes, double geracaoMaxima, double porcentagemGerada) {
        this.empresa = empresa;
        this.mes = mes;
        this.geracaoMaxima = geracaoMaxima;
        this.porcentagemGerada = porcentagemGerada;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Meses getMes() {
        return mes;
    }

    public void setMes(Meses mes) {
        this.mes = mes;
    }

    public double getgeracaoMaxima() {
        return geracaoMaxima;
    }

    public void setgeracaoMaxima(double geracaoMaxima) {
        this.geracaoMaxima = geracaoMaxima;
    }

    public double getCredito() {
        return porcentagemGerada;
    }

    public void setCredito(double porcentagemGerada) {
        this.porcentagemGerada = porcentagemGerada;
    }
    
    public double getGeracao() {
        return geracaoMaxima * porcentagemGerada;
    }

    @Override
    public String toString() {
        return "GeracaoMensal{" + "empresa=" + empresa + ", mes=" + mes + ", geracaoMaxima=" + geracaoMaxima + ", porcentagemGerada=" + porcentagemGerada + ", geraçãoMensal=" + getGeracao() + '}';
    }

    
    
    
}
