package Model;
import javafx.scene.image.Image;
import java.util.ArrayList;

public class Filme {
    private int id;
    private int qtddVendida;
    private String nome;
    private String genero;
    private String sinopse;
    private String duracao;
    private String Img;
    private Image image ;
    private ArrayList<Sessao> sessoes= new ArrayList<Sessao>();

    public Filme(int id, String nome, String genero, String sinopse, String duracao,String Img)  {
        this.id = id;
        this.nome = nome;
        this.genero = genero;
        this.sinopse = sinopse;
        this.duracao = duracao;
        this.qtddVendida=0;
        this.Img=Img;
        image=new Image(Img);
    }
    public Filme( String nome, String genero, String sinopse, String duracao,String Img)  {
        this.nome = nome;
        this.genero = genero;
        this.sinopse = sinopse;
        this.duracao = duracao;
        this.qtddVendida=0;
        this.Img=Img;
        image=new Image(Img);
    }
    public Filme(int id, String nome, String genero, String sinopse, String duracao,int qtddVendida,String Img) {
        this.id = id;
        this.nome = nome;
        this.genero = genero;
        this.sinopse = sinopse;
        this.duracao = duracao;
        this.qtddVendida=qtddVendida;
        this.Img=Img;
        image=new Image(Img);
    }
    public Filme()
    {

    }
    @Override
    public String toString()
    {
        return nome;
    }
// Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getGenero() {
        return genero;
    }

    public String getSinopse() {
        return sinopse;
    }

    public String getDuracao() {
        return duracao;
    }

    public int getQtddVendida() {
        return qtddVendida;
    }

    public void setQtddVendida(int qtddVendida) {
        this.qtddVendida = qtddVendida;
    }

    public ArrayList<Sessao> getSessoes() {
        return sessoes;
    }

    public Image getImage() {
        return image;
    }

    public String getImg() {
        return Img;
    }
//Setters
    public void setSessoes(ArrayList<Sessao> sessoes) {
        this.sessoes = sessoes;
    }

    public void setId(int id) {
        this.id = id;
    }
}
