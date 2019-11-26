package com.pucrs;

import java.util.ArrayList;
import java.util.Random;

/*
Implementação do ciclo do Algoritmo Genético, incluindo codificação, operadores de seleção, cruzamento e mutação.
Estabelcer os critérios de parada e definir uma função de aptidão adequada. O algoritmo genético não pode usar a
posição S (saída), deve procurará-la. A definição dos parâmetros(tamanho da população, escolha dos operadores,
taxas de mutação e cruzamento) do algoritmo é uma parte importante do trabalho, assim como a definição da função
de aptidão. A função de aptidão é fundamental para o sucesso desse algoritmo.
 */
public class AlgoritmoGeneticoMod {

    private int numGenes; // SIZE total de cargas
    private int numCromossomos;   // TAM tamanho da populaçao: quantidade de soluçoes
    private int maxGeracoes;  // MAX numero maximo de geraçoes (iteraçoes)
    private String[][] labirintoMatriz;
    private double[][] populacao;                       //populaçao atual: contem os cromossomos (soluçoes candidatas)
    private double[][] populacaoIntermediaria;          //populaçao intermediaria: corresponde a populaçao em construçao
    private Coordenada entrada;
    private ArrayList<Coordenada> visitados = new ArrayList<>(); //para validação de se o agente já visitou um espaço anteriormente
    //private int scoreSaida;
    //private boolean achouSaida;

    public AlgoritmoGeneticoMod(String[][] labirintoMatriz, int numGenes, int numCromossomos, int maxGeracoes, Coordenada entrada) {
        this.numGenes = numGenes;
        this.numCromossomos = numCromossomos;
        this.maxGeracoes = maxGeracoes;
        this.labirintoMatriz = labirintoMatriz;
        this.entrada = entrada;
        populacao = new double [numCromossomos][numGenes+1];
        populacaoIntermediaria = new double [numCromossomos][numGenes+1];
        inicializaPopulacao(populacao);   //cria soluçoes aleatoriamente
    }

    public ArrayList<Coordenada> encontraSaida(){
        for(int geracao = 1; geracao <= maxGeracoes; geracao++) {
            System.out.println("Geraçao:" + geracao);

            calculaFuncoesDeAptidao(populacao);
            double melhor = selecionaElitismo(populacao, populacaoIntermediaria); //highlander, Vulgo elitismo

            if(populacaoIntermediaria[0][numGenes] > 200) {
                mostraGeracao(populacao);
                System.out.println(">>>> Achou a saída: ");

                //saida.setX(visitados.get(visitados.size()-1).getX());
                //saida.setY(visitados.get(visitados.size()-1).getY());
                //
                return solucaoOtima();
            }

            crossOver(populacaoIntermediaria, populacao);
            if(geracao%2==0) {
                mutacao(populacaoIntermediaria);
            }
            populacao = populacaoIntermediaria;
        }
        ArrayList<Coordenada> saida = new ArrayList();
        saida.add(new Coordenada(0,0));
        return saida;
    }

    /**
     * Gera populaçao inicial: conjunto de soluçoes candidatas
     */
    private static void inicializaPopulacao(double[][] populacao) {
        Random r = new Random();
        for(int i = 0; i < populacao.length; i++) {
            for(int j = 0; j < populacao[i].length -1; j++) {
                populacao[i][j] = r.nextDouble();
            }
        }
    }

    /**
     * Calcula a funçao de aptidao para a populaçao atual
     */
    private void calculaFuncoesDeAptidao(double[][] populacao){
        for(int i = 0; i < populacao.length; i++) {
            funcaoDeAptidao(populacao[i]);
        }
    }

    /**
     * Funçao de aptidao: heuristica que estima a qualidade de uma soluçao
     */
    private void funcaoDeAptidao(double[] linha) {
        int totalPontos = 0;
        //System.out.println("Entrada: " + entrada.getX() + "," + entrada.getY());
        Coordenada agente = new Coordenada(entrada.getX(), entrada.getY());
        visitados = new ArrayList<>();
        visitados.add(new Coordenada(entrada.getX(), entrada.getY()));

        MultiLayerPerceptron mlp = new MultiLayerPerceptron(linha);
        int coord;
        int B;
        int C;
        int D;
        int E;
        int moveScore;

        //enquanto não morrer ou encontrar a saída
        while(true){
            //pega o conteúdo das celulas vizinhas
            B = observaCelulaVizinha(agente.getX(), agente.getY(), 1, 0);
            C = observaCelulaVizinha(agente.getX(), agente.getY(), -1, 0);
            D = observaCelulaVizinha(agente.getX(), agente.getY(), 0, 1);
            E = observaCelulaVizinha(agente.getX(), agente.getY(), 0, -1);

            coord = mlp.generalizacao(B, C, D, E);
            moveScore = computaPontuacao(agente, converteCoordenada(coord));
            if((moveScore < -1) || (moveScore > 49)){
                break;
            }
            //soma os pontos
            totalPontos += moveScore;
        }
        System.out.println("total pontos: " + totalPontos);
        linha[linha.length-1] = totalPontos; //guarda a aptidão na ultima posição do vetor
    }

    private int observaCelulaVizinha(int atualX, int atualY, int X, int Y){
        //pega coord atual do agente e testa B C D e F, um por vez se é válido(in bounds)

        //caso válido pega o conteúdo em string e converte para int
        // 0/E=0 1=1 M=2 S=3

        return 0;
    }

    private int computaPontuacao(Coordenada agente, Coordenada nextMove){
        // 0/E = +5
        // repetido = -1
        // 1 = -30
        // M = +20
        // S = +50

        // fazer o crossover

        if (dentroDoLimite(agente.getX() + nextMove.getX(),agente.getY() + nextMove.getY())){

            //int test1 = agente.getX() + X;
            //int test2 = agente.getY() + Y;

            //System.out.println("x: " + test1 + ", y: " + test2);

            if(!labirintoMatriz[agente.getX() + nextMove.getX()][agente.getY() + nextMove.getY()].contains("1")){
                agente.setX(agente.getX()+ nextMove.getX());
                agente.setY(agente.getY()+ nextMove.getY());

                //System.out.println(agente.getX() + "," + agente.getY());
                //System.out.println();

                if(jaVisitouCoordenada(agente.getX(), agente.getY())){
                    return -11;
                }
                else {
                    if(labirintoMatriz[agente.getX()][agente.getY()].contains("S")){
                        visitados.add(new Coordenada(agente.getX(), agente.getY()));
                        return 300;
                    }
                    visitados.add(new Coordenada(agente.getX(), agente.getY()));
                    return 10;
                }
            }
        }
        return -0;
    }

    private Coordenada converteCoordenada(int coord){
        // 1B 2C 3D 4E
        Coordenada coordenada = new Coordenada(0, 0);
        switch (coord){
            case 1:
                coordenada = new Coordenada(1, 0);
                break;
            case 2:
                coordenada = new Coordenada(-1, 0);
                break;
            case 3:
                coordenada = new Coordenada(0, 1);
                break;
            case 4:
                coordenada = new Coordenada(0, -1);
                break;
        }
        return coordenada;
    }

    private boolean dentroDoLimite(int x, int y){
        if(((x > -1) && (x < labirintoMatriz.length)) && ((y > -1) && (y < labirintoMatriz[0].length))){
            return true;
        }
        return false;
    }

    private Boolean jaVisitouCoordenada(int x, int y){
        if(visitados.isEmpty()){
            return false;
        }
        else{
            for(Coordenada coord: visitados){
                if(coord.getX() == x && coord.getY() == y){
                    return true;
                }
            }
        }

        return false;
    }

    private double selecionaElitismo(double[][] populacao, double[][] populacaoIntermediaria) {
        int elite = 0;
        double menorCoord = populacao[0][numGenes];

        for(int i=1; i < populacao.length; i++) {
            if(populacao[i][numGenes] > menorCoord) {
                menorCoord = populacao[i][numGenes];
                elite = i;
            }
        }
        System.out.println("Elitismo - selecao; melhor do cromossomo: " + elite);

        for(int i=0; i < numGenes+1; i++) {
            populacaoIntermediaria[0][i] = populacao[elite][i];
        }
        return elite;
    }

    private void crossOver(double[][] intermediaria, double[][] populacao) {
        double[] pai;
        double[] pai2;
        int corte = 10;

        for(int i=1; i<numCromossomos; i=i+2){
            do{
                pai = torneio(populacao);
                pai2 = torneio(populacao);
            }while(pai==pai2);
            //System.out.println("Gerando dois filhos...");
            for(int j=0;j<corte; j++){
                intermediaria[i][j]=pai[j];
                intermediaria[i+1][j]=pai2[j];
            }
            for(int j=corte;j<numGenes; j++){
                intermediaria[i][j]=pai2[j];
                intermediaria[i+1][j]=pai[j];
            }
        }
    }

    private  double[] torneio(double[][] populacao) {
        Random r = new Random();
        int l1 = r.nextInt(populacao.length);
        int l2 = r.nextInt(populacao.length);

        if(populacao[l1][numGenes] > populacao[l2][numGenes]) {
            //System.out.println("Cromossomo de crossover: " + l1);
            return populacao[l1];
        }else {
            //System.out.println("Cromossomo de crossover: " + l2);
            return populacao[l2];
        }
    }

    private void mutacao(double[][] intermediaria){
        Random r = new Random();

        for(int cont = 1; cont<=2; cont++){
            int linha = r.nextInt(numCromossomos);
            int coluna = r.nextInt(numGenes);
            if(intermediaria[linha][coluna]==0)
                intermediaria[linha][coluna] = 1;
            else intermediaria[linha][coluna] = 0;

            //System.out.println("Cromossomo: " + linha + " sofreu mutacao");
        }

    }

    private void mostraGeracao(double[][] populacao) {
        System.out.println("__________________________________________________________________");
        for(int i = 0; i < populacao.length; i++) {
            System.out.print("(" + i + ") ");
            for(int j = 0; j < populacao[i].length-1; j++) {
                System.out.print(populacao[i][j] + " ");
            }
            System.out.println(" Aptidao: " + populacao[i][numGenes]);
        }
        System.out.println("__________________________________________________________________");
    }

    /**
     * Decodifica melhor soluçao
     */
    private ArrayList<Coordenada> solucaoOtima(){
        ArrayList<Coordenada> caminho = new ArrayList<>();
        Coordenada agente = new Coordenada(entrada.getX(), entrada.getY());
//        for(int i = 0; i<populacaoIntermediaria[0].length-1; i++) {
//            switch (populacaoIntermediaria[0][i]){
//                case 0:
//                    if(localizaSaida(agente, -1, -1, caminho)){
//                    return caminho;
//                }
//                    break;
//                case 1:
//                    if(localizaSaida(agente, -1, 0, caminho)){
//                    return caminho;
//                }
//                    break;
//                case 2:
//                    if(localizaSaida(agente, -1, 1, caminho)){
//                    return caminho;
//                }
//                    break;
//                case 3:
//                    if(localizaSaida(agente, 0, -1, caminho)){
//                    return caminho;
//                }
//                    break;
//                case 4:
//                    if(localizaSaida(agente, 0, 1, caminho)){
//                    return caminho;
//                }
//                    break;
//                case 5:
//                    if(localizaSaida(agente, 1, -1, caminho)){
//                    return caminho;
//                }
//                    break;
//                case 6:
//                    if(localizaSaida(agente, 1, 0, caminho)){
//                    return caminho;
//                }
//                    break;
//                case 7:
//                    if(localizaSaida(agente, 1, 1, caminho)){
//                        return caminho;
//                    }
//                    break;
//            }
//        }
        return caminho;
    }

    private boolean localizaSaida(Coordenada agente, int X, int Y, ArrayList<Coordenada> caminho){
        if (dentroDoLimite(agente.getX()+ X,agente.getY() + Y)){
            if(!labirintoMatriz[agente.getX() + X][agente.getY() + Y].contains("1")){
                agente.setX(agente.getX()+ X);
                agente.setY(agente.getY()+ Y);

                if(labirintoMatriz[agente.getX()][agente.getY()].contains("S")){
                    visitados.add(agente);
                    caminho.add(new Coordenada(agente.getX(), agente.getY()));
                    return true;
                }
                caminho.add(new Coordenada(agente.getX(), agente.getY()));
            }
        }
        return false;
    }
}
