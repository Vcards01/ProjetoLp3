package Controller;

import DataBase.CaixaDAO;
import DataBase.FuncionarioDAO;
import DataBase.ProdutoDAO;
import DataBase.VendaDAO;
import Model.Caixa;
import Model.Funcionario;
import Model.Produto;
import Model.Venda;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class VendaAlimentosController implements Initializable {
    @FXML
    public AnchorPane PnPrincipal;
    @FXML
    public TableView TabelaProduto;
    @FXML
    public TableColumn ColunaProdutoTP;
    @FXML
    public TableColumn ColunaTipoTP;
    @FXML
    public TableColumn ColunaPrecoTP;
    @FXML
    public TableColumn ColunaEstoqueTP;
    @FXML
    public TableView TabelaCarrinho;
    @FXML
    public TableColumn CProdutoCarrinho;
    @FXML
    public TableColumn CProdutoQuantidade;
    @FXML
    public TextField TxtTotal;
    @FXML
    public Button btnAdd;
    @FXML
    public Button BtnRmv;
    //Valor total da compra
    private double total=0;
    //Funcionario que esta vendadendo
    private Funcionario f;
    //Lista de Produtos comprados
    ArrayList<Produto> list = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SetTabelaVenda();
        SetTabelaCarrinho();
        Shadow();
    }
    public void setFuncionario(Funcionario f)
    {
        this.f=f;
    }
    //Define as cores quando a venda vem dos ingressos.
    public void SetCollors()
    {
        PnPrincipal.setStyle("-fx-background-color:  #80CBC4");
        BtnRmv.setStyle("-fx-background-color:  #009688" );
        btnAdd.setStyle("-fx-background-color:  #009688");
        ColunaPrecoTP.setStyle("-fx-background-color:  #80CBC4;"+"-fx-font-size: 14px;"+"-fx-border-color:  #009688;"+"-fx-font-weight:bold;");
        ColunaTipoTP.setStyle("-fx-background-color:  #80CBC4;"+"-fx-font-size: 14px;"+"-fx-border-color:  #009688;"+"-fx-font-weight:bold;");
        ColunaProdutoTP.setStyle("-fx-background-color:  #80CBC4;"+"-fx-font-size: 14px;"+"-fx-border-color:  #009688;"+"-fx-font-weight:bold;");
        ColunaEstoqueTP.setStyle("-fx-background-color:  #80CBC4;"+"-fx-font-size: 14px;"+"-fx-border-color:  #009688;"+"-fx-font-weight:bold;");
        CProdutoQuantidade.setStyle("-fx-background-color:  #80CBC4;"+"-fx-font-size: 14px;"+"-fx-border-color:  #009688;"+"-fx-font-weight:bold;");
        CProdutoCarrinho.setStyle("-fx-background-color:  #80CBC4;"+"-fx-font-size: 14px;"+"-fx-border-color:  #009688;"+"-fx-font-weight:bold;");
    }
//Configura a tabela venda.
    public void SetTabelaVenda()
    {

        ColunaProdutoTP.setCellValueFactory(new PropertyValueFactory<>("nome"));
        ColunaTipoTP.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        ColunaPrecoTP.setCellValueFactory(new PropertyValueFactory<>("preco"));
        ColunaEstoqueTP.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        TabelaProduto.setItems(GetProdutos());

    }
//Retorna uma ObservableList de prdoutos para popular a tabela.
    public ObservableList<Produto> GetProdutos()
    {
        ProdutoDAO PDAO= new ProdutoDAO();
        ObservableList<Produto> Produtos = FXCollections.observableArrayList(PDAO.getProdutos());
        return Produtos;
    }
    public void GetMedidas(double h,double w)
    {
        PnPrincipal.setPrefWidth(w);
        PnPrincipal.setPrefHeight(h);
    }
//Adiciona produtos ao carrinho
    @FXML
    public void AddProduto(ActionEvent event)
    {
        Produto p = (Produto) TabelaProduto.getSelectionModel().getSelectedItem();
        if(list.isEmpty())
        {
           p.setQuantidadeDeVenda(1);
           list.add(p);
        }
        else
        {
            if(list.contains(p))
            {
                list.get(list.indexOf(p)).setQuantidadeDeVenda(p.getQuantidadeDeVenda()+1);

            }
            else
            {
                p.setQuantidadeDeVenda(1);
                list.add(p);

            }
        }
        SetTotal(p.getPreco(),true);
        Fill(list);
    }
//Remove produtos do carrinho
    @FXML
    public void RmvProduto(ActionEvent evente)
    {
        Produto p = (Produto) TabelaCarrinho.getSelectionModel().getSelectedItem();
        if(list.isEmpty())
        {
            System.out.println("Sem produtos, colocar aviso dps");
        }
        else if(p==null)
        {
            System.out.println("Sem produtos selecionado, colocar aviso dps");
        }
       else
        {
            if(p.getQuantidadeDeVenda()>1)
            {
                list.get(list.indexOf(p)).setQuantidadeDeVenda(p.getQuantidadeDeVenda()-1);
            }
            else if(p.getQuantidadeDeVenda()==1)
            {
                list.remove(p);
            }
        }
        SetTotal(p.getPreco(),false);
        Fill(list);
    }
//Configura o carrinho
    public void SetTabelaCarrinho()
    {
        CProdutoCarrinho.setCellValueFactory(new PropertyValueFactory<>("nome"));
        CProdutoQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeDeVenda"));
    }
//Popula a tabela carrinho com o usuario adicionar
    public void Fill(ArrayList<Produto> list)
    {
        ObservableList<Produto> Produtos = FXCollections.observableArrayList(list);
        TabelaCarrinho.getItems().removeAll(TabelaCarrinho.getItems());
        TabelaCarrinho.setItems(Produtos);
    }
//Define o tatal da compra
    public void SetTotal(double valor,boolean operação)
    {
        if(operação)
        {
            total=total+valor;
            TxtTotal.setText(Double.toString(total));
        }
        else if ((!operação)&&(valor>0))
        {
            total=total-valor;
            TxtTotal.setText(Double.toString(total));
        }
        else if ((valor==0)&&(!operação))
        {
            TxtTotal.setText(Double.toString(0.0));
        }

    }
//Adiciona sombras
    public void Shadow()
    {
        DropShadow Shad = new DropShadow();
        Shad.setOffsetX(2);
        Shad.setOffsetY(4);
        Shad.setColor(Color.rgb(0, 0, 0));
        TabelaProduto.setEffect(Shad);
        TabelaCarrinho.setEffect(Shad);
        btnAdd.setEffect(Shad);
        BtnRmv.setEffect(Shad);
        TxtTotal.setEffect(Shad);
    }
    public void AttProdutos()
    {
        for(int i=0;i<list.size();i++)
        {
            ProdutoDAO PDAO = new ProdutoDAO();
            list.get(i).setQuantidade(list.get(i).getQuantidade()-list.get(i).getQuantidadeDeVenda());
            list.get(i).setQuantidadeDeVenda(list.get(i).getQuantidadeDeVenda()+PDAO.read(list.get(i).getId()).getQuantidadeDeVenda());
            PDAO.update(list.get(i));
        }
    }
    public void SetTable()
    {
        TabelaProduto.getItems().clear();
        TabelaProduto.getItems().addAll(GetProdutos());
        TabelaProduto.refresh();
    }
//Configura o botão finalizar
    @FXML
    public void Finalizar(ActionEvent event)
    {
        //Configura Data e Hora
        Date data = new Date(System.currentTimeMillis());
        SimpleDateFormat formatarDate = new SimpleDateFormat("dd-MM-yyy");
        SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ss");
        Date hora = Calendar.getInstance().getTime();
        //Busca o caixa atual,para atualizar o valor
        CaixaDAO DAO = new CaixaDAO();
        Caixa c = DAO.read(formatarDate.format(data));
        //Criar DAO do Funcionario para adicionar mais uma venda a ele
        FuncionarioDAO FDAO = new FuncionarioDAO();
        //Cria uma nova venda
        VendaDAO VDAO = new VendaDAO();
        Venda v = new Venda(formatarDate.format(data),formatHora.format(hora),Double.parseDouble(TxtTotal.getText()));
        //Aviso de venda finalizada
        Alert alert =new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Venda finalizada");
        alert.setHeaderText(null);
        alert.setContentText("A venda no valor de R$"+TxtTotal.getText()+(",foi concluida com sucesso"));
        alert.showAndWait();
        //Adiciona uma venda o funcionario e atualiza no banco
        f.setQtddVendas(f.getQtddVendas()+1);
        FDAO.update(f);
        //Adiciona o valor da venda ao caixa e atualiza ele no banco
        c.AddValor(Double.parseDouble(TxtTotal.getText()));
        DAO.update(c);
        //Cria a nova venda no banco e salva ela
        VDAO.create(v);
        //Reseta tudo
        TabelaCarrinho.getItems().removeAll(TabelaCarrinho.getItems());
        SetTotal(0,false);
        AttProdutos();
        list.clear();
        total=0;
        SetTable();
    }
//Configura o botão cancelar
    @FXML
    public void Cancelar(ActionEvent event)
    {
        Alert alert =new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Venda cancelada");
        alert.setHeaderText(null);
        alert.setContentText("A venda no valor de R$"+TxtTotal.getText()+(",foi cancelada"));
        alert.showAndWait();
        TabelaCarrinho.getItems().removeAll(TabelaCarrinho.getItems());
        SetTotal(0,false);
        list.clear();
        total=0;
    }
//Define o total com o valor vindo dos ingressos
    public void SetTotal(double valor)
    {
        total=valor;
        TxtTotal.setText(Double.toString(total));
    }


}
