/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simuladorconsumo;

import databasejson.JsonIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import static simuladorconsumo.CalculoCreditos.trunc2;
import static simuladorconsumo.CalculoCreditos.trunc4;
import static simuladorconsumo.CalculoCreditos.trunc6;

/**
 *
 * @author Pierre
 */
public class FXMLDocumentController implements Initializable {

    public static Stage mainWindow;

    private EmpresaCliente empClient;
    private EmpresaGeradora empGera;
    private InfoMensal ultimoConsumoAdd;//ultimo consumo adicionado

    //Curvas para Gráfico Consumo
    private XYChart.Series<StringExpression, DoubleProperty> seriesConsumo;
    private XYChart.Series<StringExpression, DoubleProperty> seriesMinimo;
    private XYChart.Series<StringExpression, DoubleProperty> seriesCreditoGast;
    private XYChart.Series<StringExpression, DoubleProperty> seriesCreditoGera;

    //Curva para Gráfico Consumo
    private XYChart.Series seriesCustDis;
    private XYChart.Series seriesCustCons;

    //Curva para Gráfico Consumo
    private XYChart.Series<Number, Number> seriesOtimiza;

    @FXML
    private ComboBox<Meses> cbMes;
    @FXML
    private TextField tfConsumo;
    @FXML
    private Button btAdiciona;
    @FXML
    private TableView<InfoMensal> tbListaConsumo;
    @FXML
    private TextField tfAno;
    @FXML
    private Button btCalculaCustos;
    @FXML
    private TableColumn<InfoMensal, Meses> tcAno;
    @FXML
    private TableColumn<InfoMensal, Meses> tcMes;
    @FXML
    private TableColumn<InfoMensal, Double> tcConsumo;
    @FXML
    private TableColumn<InfoMensal, Double> tcCustoAnt;
    @FXML
    private TableColumn<InfoMensal, Double> tcCustoDis;
    @FXML
    private TableColumn<InfoMensal, Double> tcCustoCons;
    @FXML
    private TableColumn<InfoMensal, Double> tcCustoEco;
    @FXML
    private Label lblCreditoExOut;
    @FXML
    private Label lblCustoConsOut;
    @FXML
    private Label lblCustoAntigoOut;
    @FXML
    private BarChart<Meses, Number> lcCustos;
    @FXML
    private AreaChart<StringExpression, DoubleProperty> areaChartConsumo;
    @FXML
    private Label lblCotaOut;
    @FXML
    private Label lblEconomiaOut;
    @FXML
    private Label lblEconomiaRSOut;
    @FXML
    private LineChart<Number, Number> lcOtimiza;
    @FXML
    private TabPane tabPaneAll;
    @FXML
    private ComboBox<ParametrosConsumo.TipoRedeEletrica> cbParamSistema;
    @FXML
    private TextField tfTarifaParam;
    @FXML
    private TextField tfDescontoParam;
    @FXML
    private TextField tfMesAux;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /**
         * Dados iniciais da empresa cliente
         */
        empClient = new EmpresaCliente(ParametrosConsumo.TipoRedeEletrica.TRIFASICA, 0., "", "", "", 0.8855289, .15);

        //dados inicias Geradora
        empGera = new EmpresaGeradora(500000, "Zecon Geração", "Santos Dummond", "Hidrelétrica");
        List<Double> l = Arrays.asList(0.95, 0.95, 0.95, 0.84, 0.8, 0.7, 0.7, 0.7, 0.8, 0.9, 0.95, 0.95);
        System.out.println("Lista TAM: " + l.size());//debug
        empGera.criarEstimativaGeracao(l, Meses.Jan);

        //grafico de Consumo
        seriesMinimo = new XYChart.Series();
        seriesMinimo.setName("Minimo");
        areaChartConsumo.getData().add(seriesMinimo);

        seriesCreditoGast = new XYChart.Series();
        seriesCreditoGast.setName("Credito Gasto");
        areaChartConsumo.getData().add(seriesCreditoGast);
        
        seriesCreditoGera = new XYChart.Series();
        seriesCreditoGera.setName("Credito Gerado");
        areaChartConsumo.getData().add(seriesCreditoGera);

        seriesConsumo = new XYChart.Series();
        seriesConsumo.setName("Consumo");
        areaChartConsumo.getData().add(seriesConsumo);

        //grafico de custo
        seriesCustDis = new XYChart.Series();
        seriesCustDis.setName("Custo Distribuidora");
        lcCustos.getData().add(seriesCustDis);

        seriesCustCons = new XYChart.Series();
        seriesCustCons.setName("Custo Total Consórcio");
        lcCustos.getData().add(seriesCustCons);

        //grafico de Otimização
        seriesOtimiza = new XYChart.Series();
        seriesOtimiza.setName("Otimização da Cota");
        lcOtimiza.getData().add(seriesOtimiza);

        tcAno.setCellValueFactory(
                new PropertyValueFactory<>("ano"));
        tcMes.setCellValueFactory(
                new PropertyValueFactory<>("mes"));
        tcConsumo.setCellValueFactory(
                new PropertyValueFactory<>("consumo"));
        tcCustoAnt.setCellValueFactory(
                new PropertyValueFactory<>("custoAntigo"));
        tcCustoDis.setCellValueFactory(
                new PropertyValueFactory<>("custoDist"));
        tcCustoCons.setCellValueFactory(
                new PropertyValueFactory<>("custoCons"));
        tcCustoEco.setCellValueFactory(
                new PropertyValueFactory<>("economia"));

//        tbListaConsumo.setEditable(true);
//        tcConsumo.setEditable(true);
        cbMes.getItems().addAll(Meses.values());
        cbParamSistema.getItems().addAll(ParametrosConsumo.TipoRedeEletrica.values());

        tfDescontoParam.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
                    if (!newPropertyValue) {
                        atualizaParametros();
                    }
                });
        tfTarifaParam.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
                    if (!newPropertyValue) {
                        atualizaParametros();
                    }
                });

        /* -------------------- TABLE SETUP --------------------- */
//        TableColumn<InfoMensal, Double> custoDistrib = new TableColumn();
//        custoDistrib.setCellValueFactory((param) -> {
//            return new ObservableValueBase<Double>() {
//                @Override
//                public Double getValue() {
//                     return new Double((int)param.getValue().getCustoAntigo());
//                }
//            };
//        });
//        custoDistrib.setText("Cemig Antes");
//        tbListaConsumo.getColumns().set(3,custoDistrib);
//        
//        TableColumn<InfoMensal, Double> tcCustoDitribAgora = new TableColumn();
//        tcCustoDitribAgora.setCellValueFactory((param) -> {
//            return new ObservableValueBase<Double>() {
//                @Override
//                public Double getValue() {
//                     return new Double((int)param.getValue().getCustoDist());
//                }
//            };
//        });
//        tcCustoDitribAgora.setText("Cemig Agora");
//        tbListaConsumo.getColumns().set(4,tcCustoDitribAgora);
//        
//        TableColumn<InfoMensal, Double> tcCustoCons = new TableColumn();
//        tcCustoCons.setCellValueFactory((param) -> {
//            return new ObservableValueBase<Double>() {
//                @Override
//                public Double getValue() {
//                     return new Double((int)param.getValue().getCustoCons());
//                }
//            };
//        });
//        tcCustoCons.setText("Consorcio");
//        tbListaConsumo.getColumns().set(5,tcCustoCons);
        //--------------------------------------------------------------
//        tbListaConsumo.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            if (newSelection != null) {
//                tbListaConsumo.getSelectionModel().clearSelection();
//            }
//        });
        //empClient.setDadosMensais((ArrayList<InfoMensal>) JsonIO.loadFile());
        //ArrayList<InfoMensal> loadList = (ArrayList<InfoMensal>)
//        EmpresaCliente lCli = JsonIO.loadFile();
//        if (lCli != null)
//            empClient = lCli;
//forEach((inf) -> {
//            empClient.addDadoMensal(inf);
//        });
        Platform.runLater(() -> {
            atualizaCampos();
            atualizaTabela();

        });

    }

    private void atualizaTabela() {
        tbListaConsumo.getItems().clear();
        tbListaConsumo.getItems().addAll(empClient.getDadosMensais());

    }

    private void atualizaCampos() {

        //Primeira Chamada
        if (ultimoConsumoAdd == null) {
            java.util.Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            Meses mes = Meses.getValue(cal.get(Calendar.MONTH) + 1); //ajusta para padrão jan=1

            //Cria um Dialog e pergunta o Mês de inicio e sugerindo o atual
            mes = selecionaMesInicial(mes);

            int ano = cal.get(Calendar.YEAR) - 1; //usa Ano Anterior
            System.out.println("ano num:" + ano);
            System.out.println("mes num:" + mes);

            cbMes.setValue(mes);
            tfAno.setText(String.valueOf(ano - 2000)); //mantem ano com 2 digitos
        } else {
            int mes = ultimoConsumoAdd.getMes().getNum();
            int ano = ultimoConsumoAdd.getAno();
            System.out.println("Mes: " + Meses.getValue(mes));

            cbMes.setValue(Meses.getValue(mes + 1));//Mês seguinte
            if (mes == 12) {
                ano++;
            }
            tfAno.setText(String.valueOf(ano));
        }
        atualizaParametros();
        tfConsumo.clear();
        tfConsumo.requestFocus();
    }

    /**
     * Atualiza as informações das caixas de texto, para manter o valor original
     * use valores null.
     *
     * @param credEx credito Excedido no final da simulação.
     * @param custoCons custo total com consorcio.
     * @param custoDist custo original com a distribuidora.
     * @param cota cota do consorcio calculada.
     * @param economia economia gerada com adesão ao consorcio em porcentagem
     * (%).
     * @param economiaRS economia gerada em Reias (R$).
     */
    private void atualizaCaixaTexto(Double credEx, Double custoCons, Double custoAntigo, Double cota, Double economia, Double economiaRS) {
        if (credEx != null) {
            lblCreditoExOut.setText(trunc2(credEx) + "kWh");
        }
        if (custoCons != null) {
            lblCustoConsOut.setText("R$ " + trunc2(custoCons));
        }
        if (custoAntigo != null) {
            lblCustoAntigoOut.setText("R$ " + trunc2(custoAntigo));
        }
        if (cota != null) {
            lblCotaOut.setText("" + trunc6(cota * 100) + "%");
        }
        if (economia != null) {
            lblEconomiaOut.setText("" + trunc4(economia) + "%");
        }
        if (economiaRS != null) {
            lblEconomiaRSOut.setText("R$ " + trunc4(economiaRS));
        }

    }

    private void adicionaPontoGraficoConsumo(InfoMensal c) {
        //populating the series with data and binding them for future changes
        
        int pos = empClient.getDadosMensais().indexOf(c); /*salva a posição original do item na lista de dados*/
        
        StringExpression xname = Bindings.concat(c.getAno(), "/", c.getMes());
        if (c.getConsumoMinimo() > 0) {
            XYChart.Data d0 = new XYChart.Data();
            d0.YValueProperty().bind(c.consumoMinimoProperty());
            d0.XValueProperty().bind(xname);
            seriesMinimo.getData().add(pos,d0);
            c.consumoMinimoProperty().addListener((Observable obs) -> {
                if (!empClient.getDadosMensais().contains(c)) {
                    seriesMinimo.getData().remove(d0);
                    //System.out.println("Listener CALLED");//#debug
                }
            });
        }

        XYChart.Data d1 = new XYChart.Data();
        d1.YValueProperty().bind(c.creditoGastoProperty());
        d1.XValueProperty().bind(xname);
        seriesCreditoGast.getData().add(pos,d1);
        c.creditoGastoProperty().addListener((Observable obs) -> {
            if (!empClient.getDadosMensais().contains(c)) {
                seriesCreditoGast.getData().remove(d1);
                //System.out.println("Listener CALLED");//#debug
            }
        });

        XYChart.Data d2 = new XYChart.Data();
        d2.YValueProperty().bind(c.creditoGeradoProperty());
        d2.XValueProperty().bind(xname);
        seriesCreditoGera.getData().add(pos,d2);
        c.creditoGeradoProperty().addListener((Observable obs) -> {
            if (!empClient.getDadosMensais().contains(c)) {
                seriesCreditoGera.getData().remove(d2);
                System.out.println("Listener CALLED");//#debug
            }
        });

        XYChart.Data d3 = new XYChart.Data();
        d3.YValueProperty().bind(c.consumoProperty());
        d3.XValueProperty().bind(xname);
        seriesConsumo.getData().add(pos,d3);
        c.consumoProperty().addListener((Observable obs) -> {
            if (!empClient.getDadosMensais().contains(c)) {
                seriesConsumo.getData().remove(d3);
                //System.out.println("Listener CALLED");//#debug
            }
        });

    }

    private void adicionaPontoGraficoCusto(InfoMensal c) {
        //populating the series with data
        String xname = c.getAno() + "/" + c.getMes();
        if (c.getCustoCons() != 0) {
            seriesCustCons.getData().add(new XYChart.Data(xname, (c.getCustoCons() + c.getCustoDist())));
        }
        if (c.getCustoAntigo() != 0) {
            seriesCustDis.getData().add(new XYChart.Data(xname, c.getCustoAntigo()));
        }
    }

    private void atualizaGraficoConsumo() {

        areaChartConsumo.getData().forEach((t) -> {
            t.getData().clear();
        });

        empClient.checkMeses();
        //Platform.runLater(() -> {
        for (InfoMensal c : empClient.getDadosMensais()) {
            adicionaPontoGraficoConsumo(c);
            System.out.println(c.getMes().getNum());//#debug
        }
        //});
    }

    private void atualizaGraficoCusto() {
//        lcCustos.getData().forEach((t) -> {
//            t.getData().clear();
//        });
        seriesCustCons.getData().clear();
        seriesCustDis.getData().clear();
        //System.out.println("ordem custo");
        //Platform.runLater(() -> {
        for (InfoMensal c : empClient.getDadosMensais()) {
            adicionaPontoGraficoCusto(c);
            //System.out.println(c.getMes().getNum());
        }
        //});
    }

    private void atualizaGraficoOtimi(Map<Double, Double> tree) {

        lcOtimiza.getData().forEach((t) -> {
            t.getData().clear();
        });
        //lcOtimiza.getData().clear();
        //System.out.println(tree.size());
        Platform.runLater(() -> {
            for (Map.Entry<Double, Double> e : tree.entrySet()) {
                addPontoGrafOtimi(e, e.getKey().equals(empClient.getCota()));
            }
        });
        //System.out.println(e + " Max:" + e.getKey().equals(empClient.getCota()));
        //series.getData().add(data);

    }

    private void addPontoGrafOtimi(Map.Entry<Double, Double> p, boolean destaque) {
        //populating the series with data

        if (p != null) {
            String xname = ("" + trunc6(p.getKey() * 100));
            XYChart.Data point = new XYChart.Data(xname, p.getValue());

            if (destaque) {

                final Region plotpoint = new Region();
                final Circle c = new Circle(2);
                plotpoint.setShape(c);
                final Popup popup = new Popup();
                final Pane pane = new Pane(new Label("Cota: " + xname + "%\nEconomia: " + trunc4(p.getValue()) + "%"));

                popup.getContent().addAll(pane);

                plotpoint.setOnMouseEntered((event) -> {
                    Bounds boundsInScreen = plotpoint.localToScreen(plotpoint.getBoundsInLocal());
                    popup.setX(boundsInScreen.getMinX());
                    popup.setY(boundsInScreen.getMinY() - 40);
                    popup.show(mainWindow);
                });
                plotpoint.setOnMouseExited((event) -> {
                    popup.hide();
                });
                point.setNode(plotpoint);
            }
            seriesOtimiza.getData().add(point);
        }

    }

    private void atualizaParametros() {
        tfDescontoParam.setText(empClient.getDescontoAplicado() * 100 + "%");
        tfTarifaParam.setText(empClient.getTarifaMedia() + "");
        cbParamSistema.setValue(empClient.getTipoRede());
    }

    @FXML
    private void onActionBTAdiciona(ActionEvent event) {
        try {
            if (cbMes.getValue() != null && !"".equals(tfConsumo.getText()) && !"".equals(tfAno.getText())) {
                if (empClient.getDadosMensais().size() < 12) {

                    System.out.println("-------" + tfConsumo.getText() + "-------");//debug
                    float consumo = Float.parseFloat(tfConsumo.getText());
                    int ano = Integer.parseInt(tfAno.getText());
                    ultimoConsumoAdd = new InfoMensal(cbMes.getValue(), consumo, ano);
                    empClient.addDadoMensal(ultimoConsumoAdd);

                    atualizaTabela();
                    atualizaCampos();
                    adicionaPontoGraficoConsumo(ultimoConsumoAdd);
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText("Erro ao adicionar");
                    alert.setContentText("Os dados de um ano ja estão completos!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error de Campo Vazio");
                alert.setHeaderText("Erro nos dados");
                alert.setContentText("Algum campo está vazio, não foi possível adicionar!");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error de Formato");
            alert.setHeaderText("Erro no dado de Ano ou Consumo");
            alert.setContentText("Os dados de Ano e Consumo precisa ser somente numeros");
            alert.showAndWait();
        }

    }

    @FXML
    private void onActionBtCalculaCust(ActionEvent event) {

        boolean aux = false;
        int v = empClient.checkMeses();
        if (v == 0) {
            aux = true;
        } else {
            //Alerta de dados mensais incompletos
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Dados Incompletos");
            alert.setHeaderText("Faltam dados de algum(s) meses");
            String m = Meses.getList(v).toString();
            alert.setContentText("Não foram encontrados dados para os mese(s):\n"
                    + m + ".\nO cálculo provavelmente não estará correto, "
                    + "deseja continua mesmo assim?");
            Optional<ButtonType> bt = alert.showAndWait();
            if (bt.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                aux = true;
            }
        }
        //BOTAO DE OTIMIZAÇÂO DESATIVADO
        //OTIMIZAÇÂO È FEITA AUTOMATICAMENTE
        OtimizacaoIterativa.runOtimizacao(seriesOtimiza, empClient, empGera);
        atualizaCaixaTexto(null, null, null, empClient.getCota(), null, null);
        //----------------------------------
        if (aux) {
            //simula creditos e custos usando a empresa geradora e parametros, retorna creditos excedentes caso exista
            Double credEx = empClient.calculoCustosCredit(empGera);
            atualizaCaixaTexto(credEx, empClient.custoTotalCons(), empClient.custoTotalAntigo(), empClient.getCota(), empClient.getEconomiaPercent(), empClient.getEconomia());

            Platform.runLater(() -> {
                atualizaTabela();
                //atualizaGraficoConsumo();
                atualizaGraficoCusto();
            });
        }
    }

    @FXML
    private void onActionBTOtimiza(ActionEvent event) {

        //mostra a tab com o grafico de otimização
        tabPaneAll.getSelectionModel().clearAndSelect(3);
        OtimizacaoIterativa.runOtimizacao(seriesOtimiza, empClient, empGera);
        //atualizaGraficoOtimi(OtimizacaoCota.simularCustos(empClient, empGera));
        atualizaCaixaTexto(null, null, null, empClient.getCota(), null, null);
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Otimização");
        alert.setHeaderText("Otimização Finalizada");
        alert.setContentText("A otimização foi feita, e o valor de cota"
                + " para máxima economia é de " + trunc6(empClient.getCota() * 100)
                + "%, deseja atualizar os cálculos de custos?");
        if (alert.showAndWait().get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            onActionBtCalculaCust(null);
        }

    }

    @FXML
    private void onActionBtLimpaDados(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Delete");
        alert.setHeaderText("Os dados serão apagados");
        alert.setContentText("Os dados serão apagados da tabela e dos graficos e não podem ser resgatados!");
        Optional<ButtonType> bt = alert.showAndWait();
        if (bt.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            empClient.clearDadosMensais();
            ultimoConsumoAdd = null;
            Platform.runLater(() -> {
                atualizaTabela();
                atualizaCampos();
                atualizaGraficoConsumo();
                atualizaGraficoCusto();
            });
        }
        
    }

    @FXML
    private void onActionBTSalvarDados(ActionEvent event) {

        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Salvar Dados");
        dialog.setHeaderText("Digite o nome da empresa junto com numero do de cadastro da Cemig.\n Ex.: PadariaDoisIrmaos_12345678");
        //dialog.setContentText("Digite um valor de Cota em %");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            empClient.setNome(result.get());
            try {
                JsonIO.save(empClient);
                Alert alert = new Alert(AlertType.INFORMATION, "Dados salvos com sucesso.", ButtonType.OK);
                alert.showAndWait();

            } catch (IOException ex) {
                Alert alert = new Alert(AlertType.ERROR, "Não foi possivel salvar o arquivo! \nErro: " + ex.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }

    }

    private Meses selecionaMesInicial(Meses atual) {
        ChoiceDialog<Meses> dialog = new ChoiceDialog<Meses>(atual, Meses.getValues());
        dialog.setTitle("Selecione o mês inicial");
        //dialog.setHeaderText("Look, a Choice Dialog");
        dialog.setContentText("Selecione o primeiro mês dos ultimos 12 meses informados na sua conta");

        // Traditional way to get the response value.
        Optional<Meses> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }
        return atual;

    }

    @FXML
    private void onActionBTEditCota(ActionEvent event) {

        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Editar Cota");
        dialog.setHeaderText("Editar valor de Cota manualmente");
        dialog.setContentText("Digite um valor de Cota em %");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                Double d = Double.parseDouble(result.get());
                if (d > 0 || d < 100) {
                    empClient.setCota(d / 100);
                    atualizaCaixaTexto(null, null, null, empClient.getCota(), null, null);
                } else {
                    throw new Exception();
                }

            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Valor Inválido!");
            }
        }

    }

    @FXML
    private void onActionBTDelete(ActionEvent event) {
        InfoMensal im = tbListaConsumo.getSelectionModel().getSelectedItem();
        if (im != null) {
            if (empClient.getDadosMensais().size() == 12) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmar Delete");
                alert.setHeaderText("O dado será apagado");
                alert.setContentText("Deseja realmente deletar entrada selecionada na tabela?");
                Optional<ButtonType> bt = alert.showAndWait();
                if (bt.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    cbMes.setValue(im.getMes());
                    tfAno.setText("" + im.getAno());
                    tbListaConsumo.getSelectionModel().clearSelection();
                    empClient.getDadosMensais().remove(im);
                    im.invalidadeAll();
                    atualizaTabela();
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao deletar");
                alert.setContentText("Não é possivel deletar uma entrada antes "
                        + "de completar os valores, por favor tente novamente "
                        + "quando os dados de 1 ano estiverem completos, ou "
                        + "use o botão limpar para deletar todas as entradas.");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao deletar");
            alert.setContentText("Nenhuma entrada selecionada na tabela!");
            alert.showAndWait();
        }

    }

    @FXML
    private void onKeyTypedDel(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) {
            onActionBTDelete(null);
        }
    }

    @FXML
    private void onActionBTCarregar(ActionEvent event) {

        List<File> lFile = JsonIO.findData();
        List<EmpresaCliente> lEmp = new ArrayList();
        lFile.forEach((f) -> {
            //System.out.println(f.getName());
            EmpresaCliente e = JsonIO.loadFile(f);
            //System.out.println(e);
            if (e != null) {
                lEmp.add(e);
            }
        });
        ChoiceDialog<EmpresaCliente> dialog = new ChoiceDialog<>(null, lEmp);
        dialog.setTitle("Carregar arquivo");
        dialog.setHeaderText("Escolha qual carregar.");
        dialog.setContentText("Arquivos encontrados:");

        Optional<EmpresaCliente> result = dialog.showAndWait();

        if (result.isPresent()) {
            empClient.clearDadosMensais();
            empClient = result.get();
            if (empClient.getDescontoAplicado() == null) {
                empClient.setDescontoAplicado(.0);
            }
            if (empClient.getTarifaMedia() == null) {
                empClient.setTarifaMedia(.0);
            }
            Platform.runLater(() -> {
                
                for (InfoMensal im : empClient.getDadosMensais()) {
                    adicionaPontoGraficoConsumo(im);
                }
                ultimoConsumoAdd = empClient.getDadosMensais().get(empClient.getDadosMensais().size()-1);
                atualizaCampos();
                atualizaTabela();

            });
        }

    }

    @FXML
    private void onActionTFDesconto(ActionEvent event) {
        try {
            String s = tfDescontoParam.getText();
            if (s.endsWith("%")) {
                s = s.substring(0, s.length() - 1);
            }
            empClient.setDescontoAplicado(Double.parseDouble(s) / 100);
            //ParametrosConsumo.atualizaTarifaGeracao();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Valor Alterado para: " + s + "%");
            alert.showAndWait();
        } catch (NumberFormatException e) {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Valor inválido!");
            alert.showAndWait();
        }
        tfDescontoParam.setText(empClient.getDescontoAplicado() * 100 + "%");
        //System.out.println("Desconto Alterado");
    }

    @FXML
    private void onActionCBSistema(ActionEvent event) {
        if (cbParamSistema.isFocused()) {
            empClient.setTipoRede(cbParamSistema.getSelectionModel().getSelectedItem());
            System.out.println("Sistema rede Alterada");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Valor Alterado para: " + cbParamSistema.getSelectionModel().getSelectedItem().name());
            alert.showAndWait();
        }
    }

    @FXML
    private void onActionTFTarifa(ActionEvent event) {
        try {
            String s = tfTarifaParam.getText();
            empClient.setTarifaMedia(Double.parseDouble(s.substring(0, s.length())));
            ParametrosConsumo.atualizaTarifaGeracao();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Valor Alterado para: " + s);
            alert.showAndWait();

        } catch (NumberFormatException e) {
            tfTarifaParam.setText(empClient.getTarifaMedia() + "");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Valor inválido!");
            alert.showAndWait();
        }
        System.out.println("Tarifa Alterada");
    }

    /**
     * Para impedir a mudança manual do mes, foi adicionado um Text Field
     * espelho ao ComboBox de mes, o ComboBox ficou escondido e toda alteração
     * feita nele pelo programa altera o TextField espelho mostrando ao usuario
     * o valor correto do mês.
     *
     * @param event
     */
    @FXML
    private void onAction_cbMes(ActionEvent event) {
        tfMesAux.setText(cbMes.getValue().name());
    }

}
