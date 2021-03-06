package Controller.CRUDViewControllers;

import Controller.TableGerControllers.TableGerSalaController;
import DataBase.SalaDAO;
import Model.Sala;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class SalaCRUDViewController implements Initializable {
    @FXML
    public TextField Txtid;
    @FXML
    public TextField TxtQuantidadeLugares;
    //Variavel para verificar se é editavel ou não
    private boolean editavel = false;
    //Controlador da tela anterior
    private TableGerSalaController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    //Abre em modo edição
    public void OpenEditable(Sala s)
    {
        editavel=true;
        Txtid.setText(Integer.toString(s.getId()));
        TxtQuantidadeLugares.setText(Integer.toString(s.getQtddLugares()));
    }
    @FXML
    public void Cancelar(ActionEvent event)
    {
        Stage janela = (Stage)Txtid.getScene().getWindow();
        janela.close();
    }
    @FXML
    public void Save(ActionEvent event)
    {
        if(TxtQuantidadeLugares.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Campos em branco");
            alert.setHeaderText(null);
            alert.setContentText("Não deixe nenhum campo em branco!");
            alert.showAndWait();
        }
        else
        {
            if(!CheckNumber(TxtQuantidadeLugares.getText()))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Lugares invalido");
                alert.setHeaderText(null);
                alert.setContentText("Apenas numeros são permitidos!");
                alert.showAndWait();
            }
            else
            {
                SalaDAO DAO = new SalaDAO();
                Sala s = new Sala(Integer.parseInt(TxtQuantidadeLugares.getText()));
                //Salva para uma nova Sala
                if(!editavel)
                {
                    DAO.create(s);
                }
                //Edita uma sala ja existente
                else
                {
                    s.setId(Integer.parseInt(Txtid.getText()));
                    DAO.update(s);
                }
                //Atualiza a tabela da tela anterior
                controller.SetTable();
                Stage janela = (Stage)TxtQuantidadeLugares.getScene().getWindow();
                janela.close();
            }
        }
     }
    public boolean CheckNumber( String s ) {
        // cria um array de char
        char[] c = s.toCharArray();
        boolean d = true;
        for ( int i = 0; i < c.length; i++ ){
            // verifica se o char não é um dígito
            if ( !Character.isDigit( c[ i ] ) ) {
                d = false;
                break;
            }
        }
        return d;
    }
    public void SetController(TableGerSalaController controller)
    {
        this.controller = controller;
    }
}
