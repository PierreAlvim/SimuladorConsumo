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
    private DoubleProperty custoAntigo;
    private DoubleProperty custoCons;
    private DoubleProperty custoDist;
    private DoubleProperty consumoMinimo;
    private DoubleProperty economia;

    public InfoMensal() {
    }

    public InfoMensal(Meses mes, double consumo, double credito, int ano, double custoAntigo, double custoCons, double custoDist, double consumoMinimo, double economia) {

        this.ano = new SimpleIntegerProperty(ano);
        this.consumo = new SimpleDoubleProperty(consumo);
        this.creditoGasto = new SimpleDoubleProperty(credito);
        this.creditoGerado = new SimpleDoubleProperty();
        this.custoAntigo = new SimpleDoubleProperty(custoAntigo);
        this.custoCons = new SimpleDoubleProperty(custoCons);
        this.custoDist = new SimpleDoubleProperty(custoDist);
        this.economia = new SimpleDoubleProperty(economia);
        this.consumoMinimo = new SimpleDoubleProperty(consumoMinimo);
        
        this.mes = mes;
//        this.creditoGerado = cutoCons;
//        this.consumoMinimo = consumoMinimo;
    }

    public InfoMensal(Meses mes, double consumo, int ano) {

        this.ano = new SimpleIntegerProperty(ano);
        this.consumo = new SimpleDoubleProperty(consumo);
        this.creditoGasto = new SimpleDoubleProperty();
        this.creditoGerado = new SimpleDoubleProperty();
        this.custoAntigo = new SimpleDoubleProperty();
        this.custoCons = new SimpleDoubleProperty();
        this.custoDist = new SimpleDoubleProperty();
        this.economia = new SimpleDoubleProperty();
        this.consumoMinimo = new SimpleDoubleProperty();
        this.mes = mes;

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

    public double getCustoAntigo() {
        return custoAntigo.get();
    }

    public void setCustoAntigo(double custoAntigo) {
        this.custoAntigo.set(custoAntigo);
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
    public double getEconomia() {
        return economia.get();
    }

    public void setEconomia(double economia) {
        this.economia.set(economia);
    }

    public IntegerProperty anoProperty() {
        return ano;
    }

//    public void setAnoProp(IntegerProperty anoProp) {
//        this.ano = anoProp;
//    }

    public DoubleProperty consumoProperty() {
        return consumo;
    }

//    public void setConsumoProp(DoubleProperty consumoProp) {
//        this.consumo = consumoProp;
//    }

    public DoubleProperty creditoGastoProperty() {
        return creditoGasto;
    }

//    public void setCreditoGastoProp(DoubleProperty creditoGastoProp) {
//        this.creditoGasto = creditoGastoProp;
//    }

    public DoubleProperty creditoGeradoProperty() {
        return creditoGerado;
    }

//    public void setCreditoGeradoProp(DoubleProperty creditoGeradoProp) {
//        this.creditoGerado = creditoGeradoProp;
//    }

    public DoubleProperty custoDistProperty() {
        return custoDist;
    }

//    public void setCustoDistProp(DoubleProperty custoDistProp) {
//        this.custoDist = custoDistProp;
//    }

    public DoubleProperty custoConsProperty() {
        return custoCons;
    }

//    public void setCustoConsProp(DoubleProperty custoConsProp) {
//        this.custoCons = custoConsProp;
//    }

    public DoubleProperty consumoMinimoProperty() {
        return consumoMinimo;
    }

//    public void setConsumoMinimoProp(DoubleProperty roconsumoMinimoProp) {
//        this.consumoMinimo = roconsumoMinimoProp;
//    }
    
    public DoubleProperty economiaProperty() {
        return economia;
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
        custoAntigo.set(-1);
        consumo.set(-1);
        consumoMinimo.set(-1);
    }

    public void formatTrunc() {
        creditoGasto.set(CalculoCreditos.trunc2(creditoGasto.get()));
        creditoGerado.set(CalculoCreditos.trunc2(creditoGerado.get()));
        custoCons.set(CalculoCreditos.trunc2(custoCons.get()));
        custoAntigo.set(CalculoCreditos.trunc2(custoAntigo.get()));
        custoDist.set(CalculoCreditos.trunc2(custoDist.get()));
        economia.set(CalculoCreditos.trunc2(economia.get()));
    }

    @Override
    public InfoMensal clone() {
        return new InfoMensal(mes, consumo.get(), creditoGasto.get(), ano.get(), custoAntigo.get(), custoCons.get(), custoDist.get(), consumoMinimo.get(), economia.get());
    }

    @Override
    public String toString() {
        return "ConsumoMensal{" + ", mes=" + mes
                + ", consumo=" + consumo.get() + ", credito=" + creditoGasto.get() + ", ano="
                + ano.get() + ", custoDist=" + custoAntigo.get() + ", custoCons=" + custoCons.get()
                + ", custoDist=" + custoDist.get() + ", economia=" + economia.get() + '}';
    }

}
