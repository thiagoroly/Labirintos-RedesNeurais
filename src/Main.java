import com.pucrs.*;

import java.util.ArrayList;

public class Main {

    public final static int NUMGENES = 21; //total de cargas
    public final static int NUMCROMOSSOMOS = 11;   //tamanho da populaçao: quantidade de soluçoes
    public final static int MAXGERACOES = 50;  //numero maximo de geraçoes (iteraçoes)

    public static void main(String[] args) {
        /*
        Simulação deve permitir o acompanhamento visual da execução dos algoritmos e seus resultados. Entregue um executável e informações de como
        executá-lo. Permita que seu programa receba o nome do arquivo (labirinto)
        como entrada (args). Os arquivos de saída para os casos de labirinto dados
        devem ser entregues juntamente com o código e o executável.
        */

        Labirinto labirinto = new Labirinto(Arquivo.learquivo(args[0]));

        AlgoritmoGeneticoCustom aG = new AlgoritmoGeneticoCustom(labirinto.getMatriz(), Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]), labirinto.getEntrada());
        ArrayList<Coordenada> saida = aG.encontraSaida();
        System.out.println("Saída achada na coordenada: (" + saida.get(saida.size()-1).getX() + "," +saida.get(saida.size()-1).getY() + ")");

        String texto = "";
        for(Coordenada coor :  saida){
            texto += "(" + coor.getX() + "," + coor.getY() + ") ";
        }
        System.out.println(texto);

        AlgoritmoAStar aEstrela = new AlgoritmoAStar();
        aEstrela.carregaDados(labirinto.getMatriz(), labirinto.getEntrada(), saida.get(saida.size()-1));

        System.out.println(aEstrela.encontraCaminho());

        aEstrela.desenhaCaminho();
        for(int i = 0; i< labirinto.getMatriz().length; i++) {
            String linha = "";
            for (int j = 0; j < labirinto.getMatriz()[i].length; j++) {
                linha += " " + labirinto.getMatriz()[i][j];
            }
            System.out.println(linha);
        }
    }
}
