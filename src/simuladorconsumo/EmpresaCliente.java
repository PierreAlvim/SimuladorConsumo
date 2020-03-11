/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorconsumo;

import databasejson.SimpleDataConsumo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import static simuladorconsumo.CalculoCreditos.trunc2;
import simuladorconsumo.ParametrosConsumo.TipoRedeEletrica;

/**
 *
 * @author Pierre
 */
public class EmpresaCliente extends Empresa {

    private TipoRedeEletrica tipoRede;
    private Double cota;
    private Double tarifaMedia;
    private Double descontoAplicado;
    private List<InfoMensal> dadosMensais;

    public EmpresaCliente(TipoRedeEletrica tipoRede, Double cota, String nome, String endereco, String tipo, Double tarifa, Double desconto) {
        super(nome, endereco, tipo);
        this.tipoRede = tipoRede;
        this.cota = cota;
        this.tarifaMedia = tarifa;
        this.descontoAplicado = desconto;
        dadosMensais = new ArrayList();
    }

    public Double getTarifaMedia() {
        return tarifaMedia;
    }

    public void setTarifaMedia(Double tarifaMedia) {
        this.tarifaMedia = tarifaMedia;
    }

    public Double getDescontoAplicado() {
        return descontoAplicado;
    }

    public void setDescontoAplicado(Double descontoAplicado) {
        this.descontoAplicado = descontoAplicado;
    }

    public TipoRedeEletrica getTipoRede() {
        return tipoRede;
    }

    public void setTipoRede(TipoRedeEletrica tipoRede) {
        this.tipoRede = tipoRede;
    }

    public Double getCota() {
        return cota;
    }

    public void setCota(Double cota) {
        this.cota = cota;
    }

    public List<InfoMensal> getDadosMensais() {
        return dadosMensais;
    }

    public boolean addDadoMensal(InfoMensal inf) {
        inf.setConsumoMinimo(getConsumoMinimo());
        boolean ret = dadosMensais.add(inf);
        orderaDadosMensais();
        return ret;
    }

    public void setDadosMensais(List<InfoMensal> dadosMensais) {
        this.dadosMensais = dadosMensais;
        orderaDadosMensais();
    }

    public void clearDadosMensais() {
        dadosMensais.clear();
    }

    public Double getConsumoMinimo() {
        return tipoRede.getCustoDisponibilidade();
    }

    public Double calculoCustosCredit(EmpresaGeradora empG) {

        //Se a cota não foi otimizada
        if (cota <= 0) {
            cota = calculoCreditoMedio() / empG.geracaoMediaMensal();
        }
        Double credEx = 0.;
        for (InfoMensal im : dadosMensais) {
            Double credBasico = empG.getGeracaoMes(im.getMes()).getGeracao() * cota;
            im.setCreditoGerado(credBasico);
            im.setConsumoMinimo(getConsumoMinimo());
            //double resto = 0;
            if (credBasico > 0) {
                double creditoTotal = im.getCreditoGerado() + credEx;

                //Calculo custo de consumo da distribuidora
                im.setCustoAntigo(im.getConsumo() * tarifaMedia);

                //caso o credito seja maior que o cosumo calcula o exedente
                if (creditoTotal > im.getConsumo()) {
                    credEx = creditoTotal - im.getConsumo();
                    im.setCreditoGasto(im.getConsumo());
                } else {
                    im.setCreditoGasto(creditoTotal); //todo o credito é gasto
                    credEx = 0.;
                }

                //Calculo custo consórsio
                if (im.getConsumo() >= (im.getCreditoGasto() + getConsumoMinimo())) {
                    if (ParametrosConsumo.COBRANCA_IMEDIATA) {
                        im.setCustoCons(tarifaMedia * (1 - descontoAplicado) * im.getCreditoGerado());
                    } else {
                        im.setCustoCons(tarifaMedia * (1 - descontoAplicado) * im.getCreditoGasto());
                    }
                    im.setCustoDist(tarifaMedia * (im.getConsumo() - im.getCreditoGasto()));
                } else {
                    if (ParametrosConsumo.COBRANCA_IMEDIATA) {
                        im.setCustoCons(tarifaMedia * (1 - descontoAplicado) * im.getCreditoGerado());
                    } else {
                        im.setCustoCons(tarifaMedia * (1 - descontoAplicado) * im.getCreditoGasto());
                    }
                    im.setCustoDist(tarifaMedia * getConsumoMinimo());
                }
                //creditoGerado =  resto;

                im.setEconomia(im.getCustoAntigo() - (im.getCustoCons() + im.getCustoDist()));
                im.formatTrunc();
            }
            //credEx = im.calculaCustos(credEx);
        }
        return credEx;
    }

    /**
     * Calcula um valor de credito inicial baseado no consumo descartando o
     * mínimo.
     *
     * @return O credito calculado.
     */
    public Double calculoCreditoMedio() {
        Double soma = 0.;
        for (InfoMensal d : dadosMensais) {
            soma += d.getConsumo() >= getConsumoMinimo()
                    ? d.getConsumo() - getConsumoMinimo()
                    : 0;
        }
        //System.out.println(soma);
        return soma / dadosMensais.size();
    }

    /**
     * Calcula a total de custo Antigo da distribuidora para os dados meses.
     *
     * @return valor total do custo antigo da distribuidora.
     */
    public Double custoTotalAntigo() {
        Double soma = 0.;
        for (InfoMensal c : dadosMensais) {
            soma += c.getCustoAntigo();
        }

        return trunc2(soma);
    }

    /**
     * Calcula a total de custo da distribuidora para os dados meses.
     *
     * @return valor total do custo da distribuidora.
     */
    public Double custoTotalDist() {
        Double soma = 0.;
        for (InfoMensal c : dadosMensais) {
            soma += c.getCustoDist();
        }
        return trunc2(soma);
    }

    /**
     * Calcula o total de custo com consórcio para os dados meses.
     *
     * @return valor total do custo com consórcio.
     */
    public Double custoTotalCons() {
        Double soma = 0.;
        for (InfoMensal c : dadosMensais) {
            soma += c.getCustoCons();
        }

        return trunc2(soma);
    }

    public Double getEconomiaPercent() {
        return getEconomia() / custoTotalAntigo() * 100;
    }

    public Double getEconomia() {
        return (custoTotalAntigo() - custoTotalDist() - custoTotalCons());
    }

    /**
     * Ordena os dados mensais por mês e ano.
     */
    private void orderaDadosMensais() {
        dadosMensais.sort((InfoMensal t, InfoMensal t1) -> {
            return ((t.getMes().getNum() + t.getAno() * 13) - (t1.getMes().getNum() + t1.getAno() * 13));
        });
    }

    private void orderaDadosMensais(Meses inicial) {
        dadosMensais.sort((InfoMensal t, InfoMensal t1) -> {
            return ((t.getMes().getNum() + 13 * t.getAno()) - (t1.getMes().getNum() + 13 * t1.getAno()));
        });
    }

    /**
     * Checa a integridade dos meses adicionados. Caso os 12 meses estejam
     * completos e sem repetição retorna 0, e outro numero caso não estejam.
     *
     * @return Retorna 0 somente se existir consumo em todos os 12 meses, e sem
     * repetição. Valores negativos caso exista mais de um ano adicionado ex: -5
     * caso exita um ano e 5 meses adicionados. Valores positivos caso exista um
     * mes faltando ou repetido, retorna o mês onde o primeiro problema foi
     * encontrado.
     */
    public int checkMeses() {
        orderaDadosMensais(); //Ordena 
        int a = 0;//inicia a comparacao
        short f = 4095;
        for (InfoMensal im : dadosMensais) {
            if (a != im.getMes().getNum()) {
                a = im.getMes().getNum();
                f -= Math.pow(2, a - 1);
            }
        }
        return f;
    }

    public SimpleDataConsumo toSimpleData() {
        Meses[] meses = new Meses[12];
        double[] consumo = new double[12];
        int i = 0;
        for (InfoMensal im : dadosMensais) {
            meses[i] = im.getMes();
            consumo[i] = im.getConsumo();
            i++;
        }
        return new SimpleDataConsumo(tipoRede, cota, tarifaMedia, descontoAplicado,
                nome, endereco, tipo, dadosMensais.get(0).getAno(), consumo, meses);
    }

    @Override
    public EmpresaCliente clone() {
        EmpresaCliente e = new EmpresaCliente(tipoRede, cota, nome, endereco, tipo, tarifaMedia, descontoAplicado);
        dadosMensais.forEach((d) -> e.addDadoMensal(d.clone()));
        return e;
    }

    @Override
    public String toString() {
        return nome + ", " + endereco;
    }

}
