/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorconsumo;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author Pierre
 */
public class InfoMensal {

    private Meses mes;
    private IntegerProperty ano;
    private DoubleProperty consumo;
    private DoubleProperty creditoGasto;
    private DoubleProperty creditoGerado;
    private DoubleProperty custoDist;
    private DoubleProperty custoCons;
    private DoubleProperty consumoMinimo;

    public InfoMensal() {
    }

    public InfoMensal(Meses mes, double consumo, double credito, int ano, double custoDist, double cutoCons, double consumoMinimo) {

        this.ano = new SimpleIntegerProperty();
        this.consumo = new SimpleDoubleProperty();
        this.creditoGasto = new SimpleDoubleProperty();
        this.creditoGerado = new SimpleDoubleProperty();
        this.custoDist = new SimpleDoubleProperty();
        this.custoCons = new SimpleDoubleProperty();
        this.consumoMinimo = new SimpleDoubleProperty();
        
        this.mes = mes;
        this.consumo.set(consumo);
        this.creditoGasto.set(credito);
        this.ano.set(ano);
        this.custoDist.set(custoDist);
        this.creditoGerado.set(cutoCons);
        this.consumoMinimo.set(consumoMinimo);
//        this.creditoGerado = cutoCons;
//        this.consumoMinimo = consumoMinimo;
    }

    public InfoMensal(Meses mes, double consumo, int ano) {

        this.ano = new SimpleIntegerProperty();
        this.consumo = new SimpleDoubleProperty();
        this.creditoGasto = new SimpleDoubleProperty();
        this.creditoGerado = new SimpleDoubleProperty();
        this.custoDist = new SimpleDoubleProperty();
        this.custoCons = new SimpleDoubleProperty();
        this.consumoMinimo = new SimpleDoubleProperty();
        this.mes = mes;
        this.consumo.set(consumo);
        this.ano.set(ano);

    }

    public double getConsumoMinimo() {
        return consumoMinimo.get();
    }

    public void setConsumoMinimo(double consumoMinimo) {
        this.consumoMinimo.set(consumoMinimo);
    }

    public Meses getMes() {
        return mes;
    }

    public void setMes(Meses mes) {
        this.mes = mes;
    }

    public double getConsumo() {
        return consumo.get();
    }

    public void setConsumo(double consumo) {
        this.consumo.set(consumo);
    }

    public double getCreditoGasto() {
        return creditoGasto.get();
    }

    public void setCreditoGasto(double creditoGasto) {
        this.creditoGasto.set(creditoGasto);
    }

    public int getAno() {
        return ano.get();
    }

    public void setAno(int ano) {
        this.ano.set(ano);
    }

    public double getCustoDist() {
        return custoDist.get();
    }

    public void setCustoDist(double custoDist) {
        this.custoDist.set(custoDist);
    }

    public double getCustoCons() {
        return custoCons.get();
    }

    public void setCustoCons(double custoCons) {
        this.custoCons.set(custoCons);
    }

    public double getCreditoGerado() {
        return creditoGerado.get();
    }

    public void setCreditoGerado(double creditoGerado) {
        this.creditoGerado.set(creditoGerado);
    }

    public IntegerProperty getAnoProp() {
        return ano;
    }

    public void setAnoProp(IntegerProperty anoProp) {
        this.ano = anoProp;
    }

    public DoubleProperty getConsumoProp() {
        return consumo;
    }

    public void setConsumoProp(DoubleProperty consumoProp) {
        this.consumo = consumoProp;
    }

    public DoubleProperty getCreditoGastoProp() {
        return creditoGasto;
    }

    public void setCreditoGastoProp(DoubleProperty creditoGastoProp) {
        this.creditoGasto = creditoGastoProp;
    }

    public DoubleProperty getCreditoGeradoProp() {
        return creditoGerado;
    }

    public void setCreditoGeradoProp(DoubleProperty creditoGeradoProp) {
        this.creditoGerado = creditoGeradoProp;
    }

    public DoubleProperty getCustoDistProp() {
        return custoDist;
    }

    public void setCustoDistProp(DoubleProperty custoDistProp) {
        this.custoDist = custoDistProp;
    }

    public DoubleProperty getCustoConsProp() {
        return custoCons;
    }

    public void setCustoConsProp(DoubleProperty custoConsProp) {
        this.custoCons = custoConsProp;
    }

    public DoubleProperty getConsumoMinimoProp() {
        return consumoMinimo;
    }

    public void setConsumoMinimoProp(DoubleProperty roconsumoMinimoProp) {
        this.consumoMinimo = roconsumoMinimoProp;
    }

    /**
     * Calcula custos do consumo para distribuidora e para o consorcio. Caso os
     * creditos exedam o consumo o valor restante é retornado.
     *
     * @param credExtra Crédito acumulado do mês anterior caso exista.
     *
     * @return Valor exedido em creditos caso exista, ou -1 caso não seja
     * possivel calcular.
     */
    public double calculaCustos(double credExtra) {

        return -1;
    }
    
    public void invalidadeAll(){
        creditoGasto.set(-1);
        creditoGerado.set(-1);
        custoCons.set(-1);
        custoDist.set(-1);
        consumo.set(-1);
        consumoMinimo.set(-1);
    }

    private void formatTrunc() {
        creditoGasto.set(CalculoCreditos.trunc2(creditoGasto.get()));
        creditoGerado.set(CalculoCreditos.trunc2(creditoGerado.get()));
        custoCons.set(CalculoCreditos.trunc2(custoCons.get()));
        custoDist.set(CalculoCreditos.trunc2(custoDist.get()));
    }

    @Override
    public InfoMensal clone() {
        return new InfoMensal(mes, consumo.get(), creditoGasto.get(), ano.get(), custoDist.get(), custoCons.get(), consumoMinimo.get());
    }

    @Override
    public String toString() {
        return "ConsumoMensal{" + ", mes=" + mes
                + ", consumo=" + consumo + ", credito=" + creditoGasto + ", ano="
                + ano + ", custoDist=" + custoDist + ", custoCons=" + custoCons + '}';
    }

}
