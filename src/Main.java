import com.pucrs.*;

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

        Labirinto labirinto = new Labirinto(Arquivo.learquivo("./src/labirinto1_10.txt"));

        //for(int i = 0; i< matriz.length; i++) {
        //    String linha = "";
        //    for (int j = 0; j < matriz[i].length; j++) {
        //        linha += " " + matriz[i][j];
        //    }
        //    System.out.println(linha);
        //}

        //int idEntrada = 0;
        //int idSaida = 55;

        //AlgoritmoAStar aEstrela = new AlgoritmoAStar();
        //aEstrela.carregaDados(matriz, idEntrada, idSaida);
        //System.out.println(aEstrela.encontraCaminho());

        //aEstrela.desenhaCaminho();
        //for(int i = 0; i< matriz.length; i++) {
        //    String linha = "";
        //    for (int j = 0; j < matriz[i].length; j++) {
        //        linha += " " + matriz[i][j];
        //    }
        //    System.out.println(linha);
        //}


        AlgoritmoGeneticoCustom aG = new AlgoritmoGeneticoCustom(labirinto.getMatriz(), 30, 11, 50, labirinto.getEntrada());
        Coordenada saida = aG.encontraSaida();
        System.out.println("Saída achada na coordenada: " + saida.getX() + ";" +saida.getY());
    }
}
