import com.pucrs.*;

import java.util.ArrayList;

public class Main {

    public final static int NUMGENES = 41; //total de cargas
    public final static int NUMCROMOSSOMOS = 11;   //tamanho da populaçao: quantidade de soluçoes
    public final static int MAXGERACOES = 1000;  //numero maximo de geraçoes (iteraçoes)

    public static void main(String[] args) {
        //Versão com args
        //Labirinto labirinto = new Labirinto(Arquivo.learquivo(args[0]));
        //AlgoritmoGeneticoCustom aG = new AlgoritmoGeneticoCustom(labirinto.getMatriz(), Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), labirinto.getEntrada());

        //versão sem args e uso de constantes
        Labirinto labirinto = new Labirinto(Arquivo.learquivo("./src/labirinto0_10.txt"));
        AlgoritmoGeneticoMod aG = new AlgoritmoGeneticoMod(labirinto.getMatriz(), NUMGENES, NUMCROMOSSOMOS, MAXGERACOES, labirinto.getEntrada());

        ArrayList<Coordenada> saida = aG.encontraSaida();
        System.out.println("Saída achada na coordenada: (" + saida.get(saida.size()-1).getX() + "," +saida.get(saida.size()-1).getY() + ")");

        String texto = "";
        for(Coordenada coor :  saida){
            texto += "(" + coor.getX() + "," + coor.getY() + ") ";
        }
        System.out.println(texto);
    }
}
