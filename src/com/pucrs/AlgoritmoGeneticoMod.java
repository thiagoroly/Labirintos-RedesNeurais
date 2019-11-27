package com.pucrs;

import java.awt.*;
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
        for(int geracao = 1; geracao < maxGeracoes+1; geracao++) {
            System.out.println("Geraçao:" + geracao);

            calculaFuncoesDeAptidao(populacao);
            selecionaElitismo(populacao, populacaoIntermediaria); //highlander, Vulgo elitismo

            if(populacaoIntermediaria[0][numGenes] > 200) {
                mostraGeracao(populacao);
                System.out.println(">>>> Achou a saída: ");
                return bestSteps();
            }

            crossOver(populacaoIntermediaria, populacao);
            if(geracao%2==0) {
                mutacao(populacaoIntermediaria);
            }
            populacao = populacaoIntermediaria;
        }

        mostraGeracao(populacao);
        System.out.println(">>>> Esgotou as gerações: ");
        return bestSteps();
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
        Coordenada coordAgente = new Coordenada(entrada.getX(), entrada.getY());
        visitados = new ArrayList<>();
        visitados.add(new Coordenada(entrada.getX(), entrada.getY()));

        MultiLayerPerceptron mlp = new MultiLayerPerceptron(linha);
        int coordToMove;
        int B;
        int C;
        int D;
        int E;
        int moveScore;

        //enquanto não morrer ou encontrar a saída
        while(true){
            //pega o conteúdo das celulas vizinhas
            B = observaCelulaVizinha(coordAgente.getX(), coordAgente.getY(), 1, 0);
            C = observaCelulaVizinha(coordAgente.getX(), coordAgente.getY(), -1, 0);
            D = observaCelulaVizinha(coordAgente.getX(), coordAgente.getY(), 0, 1);
            E = observaCelulaVizinha(coordAgente.getX(), coordAgente.getY(), 0, -1);

            coordToMove = mlp.generalizacao(B, C, D, E);
            moveScore = computaPontuacao(coordAgente, converteCoordenada(coordToMove));
            //soma os pontos
            totalPontos += moveScore;
            if((moveScore < 0) || (moveScore > 49)){
                break;
            }
        }
        System.out.println("total pontos: " + totalPontos);
        linha[linha.length-1] = totalPontos; //guarda a aptidão na ultima posição do vetor
    }

    private int observaCelulaVizinha(int atualX, int atualY, int X, int Y){
        //pega coord atual do agente e testa se é válido(in bounds)
        if (dentroDoLimite(atualX + X,atualY + Y)){
            //caso válido pega o conteúdo em string e converte para int
            String content = labirintoMatriz[atualX + X][atualY + Y];
            // 0/E=0 1=1 M=2 S=3
            if(content.equals("1")){
                return 0;
            }
            if(content.contains("0") || content.contains("E")){
                return 1;
            }
            if(content.contains("M")){
                return 2;
            }
            if(content.contains("S")){
                return 3;
            }
        }
        return 0;
    }

    private int computaPontuacao(Coordenada agente, Coordenada goTo){
        // 0/E = +5
        // repetido = -1
        // 1 = -30
        // M = +20
        // S = +50
        int nextMove = observaCelulaVizinha(agente.getX(), agente.getY(), goTo.getX(), goTo.getY());
        if (nextMove != 0){
            agente.setX(agente.getX()+ goTo.getX());
            agente.setY(agente.getY()+ goTo.getY());
            if(!jaVisitouCoordenada(agente.getX(), agente.getY())){
                visitados.add(new Coordenada(agente.getX(), agente.getY()));
                if(nextMove == 3){
                    return 50;
                }
                if(nextMove == 2){
                    return 20;
                }
                return 5;
            }
            else {
                return -1;
            }
        }
        return -30;
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

        for(int i=1; i<numCromossomos; i++){
            do{
                pai = torneio(populacao);
                pai2 = torneio(populacao);
            }while(pai==pai2);
            //System.out.println("Gerando dois filhos...");

            for(int j=0;j<numGenes; j++) {
                intermediaria[i][j] = (pai[j] + pai2[j])/2;
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
        int linha = r.nextInt(numCromossomos);
        int coluna = r.nextInt(numGenes);
        intermediaria[linha][coluna] = r.nextDouble();
        //System.out.println("Cromossomo: " + linha + " sofreu mutacao");
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
    private ArrayList<Coordenada> bestSteps(){
        ArrayList<Coordenada> caminho = new ArrayList<>();
        MultiLayerPerceptron mlp = new MultiLayerPerceptron(populacao[0]);
        Coordenada coordAgente = new Coordenada(entrada.getX(), entrada.getY());
        Coordenada coordToMove;
        int B;
        int C;
        int D;
        int E;
        int moveScore;

        //enquanto não morrer ou encontrar a saída
        while(true){
            //pega o conteúdo das celulas vizinhas
            B = observaCelulaVizinha(coordAgente.getX(), coordAgente.getY(), 1, 0);
            C = observaCelulaVizinha(coordAgente.getX(), coordAgente.getY(), -1, 0);
            D = observaCelulaVizinha(coordAgente.getX(), coordAgente.getY(), 0, 1);
            E = observaCelulaVizinha(coordAgente.getX(), coordAgente.getY(), 0, -1);

            coordToMove = converteCoordenada(mlp.generalizacao(B, C, D, E));
            caminho.add(coordToMove);
            moveScore = computaPontuacao(coordAgente, coordToMove);
            if((moveScore < -1) || (computaPontuacao(coordAgente, coordToMove) > 49)){
                break;
            }
        }
        return caminho;
    }
}
