package Controller;

import DataBase.*;
import Model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class VendaIngressosController implements Initializable {
    @FXML
    public Pane PnFundo;
    @FXML
    public Button BtnConfirma;
    @FXML
    public Button BtnVoltar;
    @FXML
    public Button BtnProximo;
    @FXML
    public Pane PnBotoes;
    @FXML
    private AnchorPane PnPrincipal;
    @FXML
    public Spinner SpnInt;
    @FXML
    public Spinner SpnMeia;
    @FXML
    public Pane PnIngressos;
    @FXML
    public Pane PnSelecionar;
    @FXML
    public Pane PnBilheteria;
    @FXML
    public ImageView ImgCapa;
    @FXML
    public ComboBox CbFilme;
    @FXML
    public Label LbValorTotal;
    @FXML
    public ComboBox CbSessao;
    @FXML
    public Label LbSala;
    @FXML
    public Label LbPrecoInteira;
    @FXML
    public Label LbPrecoMeia;
    @FXML
    public Button BtnCancelar;
    //DAO de sessões
    private SessaoDAO SDAO = new SessaoDAO();
    //DAO de lugares
    private LugarDAO LDAO = new LugarDAO();
    //Quantidade de lugares que estão ocupados na sessão
    private int qtddLugaresOcupados=0;
    //Funcionario que esta vendadendo
    private Funcionario f;
    //Sessão atual
    private Sessao s;
    //Construtor
    private EscolhaLugarController controller;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SetSpinner(100);
        Shadow();
        CbFilme.setItems(GetFilmes());
        ChangeCapa();
        ChangeTotal();
        ChangeSala();
        BtnConfirma.setVisible(false);
        BtnVoltar.setVisible(false);
    }
    public void setFuncionario(Funcionario f)
    {
        this.f=f;
    }
    //Configura os Spinners de Ingresso
    @FXML
    private void SetSpinner(double disponiveis)
    {
        SpinnerValueFactory<Double> valueFactoryInt = new SpinnerValueFactory.DoubleSpinnerValueFactory(0,disponiveis);
        SpinnerValueFactory<Double> valueFactoryMeia = new SpinnerValueFactory.DoubleSpinnerValueFactory(0,disponiveis);
        SpnInt.setValueFactory(valueFactoryInt);
        SpnMeia.setValueFactory(valueFactoryMeia);
    }
//Atualiza a capa do filme selecionado.
    @FXML
    private void ChangeCapa()
    {
            CbFilme.valueProperty().addListener(new ChangeListener<Filme>() {
                @Override public void changed(ObservableValue ov, Filme f, Filme f1) {
                    f1.setSessoes(SDAO.getSessao(f1));
                    ImgCapa.setImage(f1.getImage());
                    ObservableList<Sessao> Sessões = FXCollections.observableArrayList(f1.getSessoes());
                    CbSessao.setItems(Sessões);
                    LbValorTotal.setText("0.0");
                    SpnInt.getValueFactory().setValue(0.0);
                    SpnMeia.getValueFactory().setValue(0.0);
                    LbPrecoInteira.setText("0.0");
                    LbPrecoMeia.setText("0.0");
                }
            });
    }
//Atualzia a sala da sessão escolhida.
    @FXML
    private void ChangeSala(){
        CbSessao.valueProperty().addListener(new ChangeListener<Sessao>() {
            @Override public void changed(ObservableValue ov, Sessao s, Sessao s1) {
                if(s1!=null)
                {
                    LbSala.setText(Integer.toString(s1.getSala().getId()));
                    LbPrecoInteira.setText(Double.toString(s1.getPrecoEntradaInteira()));
                    LbPrecoMeia.setText(Double.toString(s1.getPrecoEntradaMeia()));
                    LbValorTotal.setText("0.0");
                    SpnInt.getValueFactory().setValue(0.0);
                    SpnMeia.getValueFactory().setValue(0.0);
                    VerificaSessão(s1);
                }
                else
                {
                    LbSala.setText("-");
                }
             }
        });
    }
//Verifica quantos lugares ocupados tem na sessão e se ela ja esta lotada.
    private void VerificaSessão(Sessao s) {
        qtddLugaresOcupados=0;
        s.setLugares(LDAO.getLugares(s));
        for (int i = 0; i < s.getLugares().size(); i++) {
            if (s.getLugares().get(i).isOcupado()) {
                qtddLugaresOcupados++;
            }
        }
        if (qtddLugaresOcupados == s.getLugares().size()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sala cheia");
            alert.setHeaderText(null);
            alert.setContentText("Sem lugares disponiveis");
            alert.showAndWait();
            SpnInt.setDisable(true);
            SpnMeia.setDisable(true);
        }
        else
        {
            SetSpinner(s.getLugares().size()-qtddLugaresOcupados);
            SpnInt.setDisable(false);
            SpnMeia.setDisable(false);

        }
    }
//Atualiza o valor total do preço.
    @FXML
    private void ChangeTotal()
    {
        SpnInt.valueProperty().addListener(new ChangeListener<Double>() {
            @Override
            public void changed(ObservableValue observable, Double oldValue, Double newValue) {
                LbValorTotal.setText(Double.toString(newValue * Double.parseDouble(LbPrecoInteira.getText())+ (double)SpnMeia.getValue()*Double.parseDouble(LbPrecoMeia.getText())));
            }
        });
        SpnMeia.valueProperty().addListener(new ChangeListener<Double>() {
            @Override
            public void changed(ObservableValue observable, Double oldValue, Double newValue) {
                LbValorTotal.setText(Double.toString(newValue * Double.parseDouble(LbPrecoMeia.getText())+ (double)SpnInt.getValue()*Double.parseDouble(LbPrecoInteira.getText())));
            }
        });
    }
//Pega as mediadas do painel principal e configura a tela.
    public void SetMedidas(Double h,double w)
    {
        PnPrincipal.setPrefWidth(w);
        PnPrincipal.setPrefHeight(h);
    }
//Adiciona Sombra aos paineis
    private void Shadow()
    {
        DropShadow Shad = new DropShadow();
        Shad.setOffsetX(4);
        Shad.setOffsetY(6);
        Shad.setColor(Color.rgb(0, 0, 0));
        PnIngressos.setEffect(Shad);
        PnBilheteria.setEffect(Shad);
        PnSelecionar.setEffect(Shad);
        PnBotoes.setEffect(Shad);
    }
//Retorna uma ObservableList de filmes para popular a combobox.
    private ObservableList<Filme> GetFilmes() {
        FilmeDAO fDAO = new FilmeDAO();
        ObservableList<Filme> Filmes = FXCollections.observableArrayList(fDAO.getFilmes());
        return Filmes;
    }
//Configura o botão de cancelar
    @FXML
    public void Cancelar(ActionEvent evente)
    {
        SpnInt.getValueFactory().setValue(0.0);
        SpnMeia.getValueFactory().setValue(0.0);
    }
//Configurar o botão de Proximo.
    @FXML
    public void Proximo(ActionEvent event) throws IOException {
        //Verifica se algum ingresso foi comprado e abre a escolha de lugar
        if (Double.parseDouble(LbValorTotal.getText()) != 0.0) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/EscolhaLugar.fxml"));
            AnchorPane pane = loader.load();
            controller = loader.getController();
            s = (Sessao) CbSessao.getValue();
            controller.SetLugares(s);
            controller.SetIngressos((double) SpnInt.getValue() + (double) SpnMeia.getValue(), Double.parseDouble(LbValorTotal.getText()));
            controller.SetMedidas(PnFundo.getHeight(), PnFundo.getWidth());
            PnFundo.getChildren().setAll(pane);
            BtnConfirma.setVisible(true);
            BtnVoltar.setVisible(true);
            BtnProximo.setVisible(false);
            BtnCancelar.setVisible(false);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Valor total invalido");
            alert.setHeaderText(null);
            alert.setContentText("Verifique se todos os passos para compra de um ingresso foram executados!");
            alert.showAndWait();
        }
    }
//Configura o botão de Confirmar.
    public void Confirmar(ActionEvent event) throws IOException {
        int ingressos = (int)((double) SpnInt.getValue() + (double) SpnMeia.getValue());
        if(controller.GetSelecionados()<ingressos)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lugares não selecionados");
            alert.setHeaderText(null);
            alert.setContentText("Verifique se todos os lugares foram selecionados!");
            alert.showAndWait();
        }
        else
        {
            //Configura Data e Hora
            Date data = new Date(System.currentTimeMillis());
            SimpleDateFormat formatarDate = new SimpleDateFormat("dd-MM-yyy");
            SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ss");
            Date hora = Calendar.getInstance().getTime();
            //Pega o caixa aberto, para atualizar seus valores
            CaixaDAO CDAO = new CaixaDAO();
            Caixa c = CDAO.read(formatarDate.format(data));
            //DAO do funcionario para atualizar suas vendas.
            FuncionarioDAO FDAO = new FuncionarioDAO();
            //DAO do Filme para atualizar a quantidade de ingressos dele que foi vendido
            FilmeDAO FilDAO = new FilmeDAO();
            //Pegunta se deseja continuar comprando
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Continuar comprando?");
            alert.setHeaderText(null);
            alert.setContentText("Deseja obter alimentos ou acompanhamentos para saborear enquanto assiste o filme?");
            ButtonType buttonTypeNao = new ButtonType("Não,finalizar compra");
            ButtonType buttonTypeSim = new ButtonType("Sim,continuar comprando");
            alert.getButtonTypes().setAll(buttonTypeNao, buttonTypeSim);
            Optional<ButtonType> result = alert.showAndWait();
            //Verifica se o usuario quer continuar comprando ou não
            if (result.get() == buttonTypeNao) {
                //Cria um nova venda
                VendaDAO VDAO = new VendaDAO();
                Venda v = new Venda(formatarDate.format(data),formatHora.format(hora),Double.parseDouble(LbValorTotal.getText()));
                alert.close();
                //Fecha a janela de selecionar lugares e finaliza a venda
                PnFundo.getChildren().clear();
                PnFundo.getChildren().setAll(PnIngressos,PnSelecionar);
                BtnConfirma.setVisible(false);
                BtnVoltar.setVisible(false);
                BtnProximo.setVisible(true);
                BtnCancelar.setVisible(true);
                Alert aviso = new Alert(Alert.AlertType.INFORMATION);
                aviso.setTitle("Venda finalizada");
                aviso.setHeaderText(null);
                aviso.setContentText("A venda no valor de R$" + Double.parseDouble(LbValorTotal.getText()) + ",foi concluida com sucesso");
                aviso.showAndWait();
                //Atualiza o valor no caixa
                c.AddValor(Double.parseDouble(LbValorTotal.getText()));
                CDAO.update(c);
                //Cria a nova venda
                VDAO.create(v);
                //Atualiza as vendas do funcionario
                f.setQtddVendas(f.getQtddVendas()+1);
                //Atualiza a quantidade de ingressos vendidos para o filme
                FDAO.update(f);
                Filme f= s.getFilme();
                int qtdd = (int)((double)SpnInt.getValue()+(double)SpnMeia.getValue());
                f.setQtddVendida(f.getQtddVendida()+qtdd);
                FilDAO.update(f);
                //Reseta
                SpnInt.getValueFactory().setValue(0.0);
                SpnMeia.getValueFactory().setValue(0.0);
            }
            else if (result.get() == buttonTypeSim)
            {
                //Segue para a tela de alimentos porque o usuario escolheu continuar
                FXMLLoader loaderA = new FXMLLoader(getClass().getResource("../View/VendaAlimentos.fxml"));
                AnchorPane pane = loaderA.load();
                VendaAlimentosController controller = loaderA.getController();
                controller.GetMedidas(PnPrincipal.getHeight(),PnPrincipal.getWidth());
                controller.SetTotal(Double.parseDouble(LbValorTotal.getText()));
                controller.setFuncionario(f);
                controller.SetCollors();
                PnPrincipal.getChildren().setAll(pane);
            }
        }

    }
//Configura o botão voltar.
    public void Voltar(ActionEvent event)
    {
        PnFundo.getChildren().clear();
        PnFundo.getChildren().setAll(PnIngressos,PnSelecionar);
        BtnConfirma.setVisible(false);
        BtnVoltar.setVisible(false);
        BtnProximo.setVisible(true);
        BtnCancelar.setVisible(true);
    }


}
