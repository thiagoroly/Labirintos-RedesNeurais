package com.pucrs;

import java.util.Random;
import java.util.ArrayList;

/*
Implementação do ciclo do Algoritmo Genético, incluindo codificação, operadores de seleção, cruzamento e mutação.
Estabelcer os critérios de parada e definir uma função de aptidão adequada. O algoritmo genético não pode usar a
posição S (saída), deve procurará-la. A definição dos parâmetros(tamanho da população, escolha dos operadores,
taxas de mutação e cruzamento) do algoritmo é uma parte importante do trabalho, assim como a definição da função
de aptidão. A função de aptidão é fundamental para o sucesso desse algoritmo.
 */
public class AlgoritmoGeneticoCustom {

    private int numGenes; // SIZE total de cargas
    private int numCromossomos;   // TAM tamanho da populaçao: quantidade de soluçoes
    private int maxGeracoes;  // MAX numero maximo de geraçoes (iteraçoes)
    private String[][] labirintoMatriz;
    private int[][] populacao;                       //populaçao atual: contem os cromossomos (soluçoes candidatas)
    private int[][] populacaoIntermediaria;          //populaçao intermediaria: corresponde a populaçao em construçao
    private Coordenada entrada;
    private ArrayList<Coordenada> visitados = new ArrayList<>(); //para validação de se o agente já visitou um espaço anteriormente
    //private int scoreSaida;
    //private boolean achouSaida;

    public AlgoritmoGeneticoCustom(String[][] labirintoMatriz, int numGenes, int numCromossomos, int maxGeracoes, Coordenada entrada) {
        this.numGenes = numGenes;
        this.numCromossomos = numCromossomos;
        this.maxGeracoes = maxGeracoes;
        this.labirintoMatriz = labirintoMatriz;
        this.entrada = entrada;
        populacao = new int [numCromossomos][numGenes+1];
        populacaoIntermediaria = new int [numCromossomos][numGenes+1];
        inicializaPopulacao(populacao);   //cria soluçoes aleatoriamente
    }

    public ArrayList<Coordenada> encontraSaida(){
        for(int geracao = 1; geracao <= maxGeracoes; geracao++) {
            System.out.println("Geraçao:" + geracao);

            calculaFuncoesDeAptidao(populacao);
            int melhor = selecionaElitismo(populacao, populacaoIntermediaria); //highlander, Vulgo elitismo

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
    private static void inicializaPopulacao(int[][] populacao) {
        Random r = new Random();
        for(int i = 0; i < populacao.length; i++) {
            for(int j = 0; j < populacao[i].length -1; j++) {
                populacao[i][j] = r.nextInt(8);
            }
        }
    }

    /**
     * Calcula a funçao de aptidao para a populaçao atual
     */
    private void calculaFuncoesDeAptidao(int[][] populacao){
        for(int i = 0; i < populacao.length; i++) {
            funcaoDeAptidao(populacao[i]);
        }
    }

    /**
     * Funçao de aptidao: heuristica que estima a qualidade de uma soluçao
     */
    private void funcaoDeAptidao(int[] linha) {
        int totalPontos = 0;
        //System.out.println("Entrada: " + entrada.getX() + "," + entrada.getY());
        Coordenada agente = new Coordenada(entrada.getX(), entrada.getY());
        visitados = new ArrayList<>();
        visitados.add(new Coordenada(entrada.getX(), entrada.getY()));

        int i = 0;
        for(; i<linha.length-1; i++) {
            switch (linha[i]){
                case 0:
                    totalPontos += computaPontuacao(agente, -1, -1);
                    break;
                case 1:
                    totalPontos += computaPontuacao(agente, -1, 0);
                    break;
                case 2:
                    totalPontos += computaPontuacao(agente, -1, 1);
                    break;
                case 3:
                    totalPontos += computaPontuacao(agente, 0, -1);
                    break;
                case 4:
                    totalPontos += computaPontuacao(agente, 0, 1);
                    break;
                case 5:
                    totalPontos += computaPontuacao(agente, 1, -1);
                    break;
                case 6:
                    totalPontos += computaPontuacao(agente, 1, 0);
                    break;
                case 7:
                    totalPontos += computaPontuacao(agente, 1, 1);
                    break;
            }
        }
        System.out.println("total pontos: " + totalPontos);
        linha[i] = totalPontos;
    }

    private int computaPontuacao(Coordenada agente, int X, int Y){

        if (dentroDoLimite(agente.getX() + X,agente.getY() + Y)){

            //int test1 = agente.getX() + X;
            //int test2 = agente.getY() + Y;

            //System.out.println("x: " + test1 + ", y: " + test2);

            if(!labirintoMatriz[agente.getX() + X][agente.getY() + Y].contains("1")){
                agente.setX(agente.getX()+ X);
                agente.setY(agente.getY()+ Y);

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

    private int selecionaElitismo(int[][] populacao, int[][] populacaoIntermediaria) {
        int elite = 0;
        int menorCoord = populacao[0][numGenes];

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

    private void crossOver(int[][] intermediaria, int[][] populacao) {
        int[] pai;
        int[] pai2;
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

    private  int[] torneio(int[][] populacao) {
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

    private void mutacao(int[][] intermediaria){
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

    private void mostraGeracao(int[][] populacao) {
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
        for(int i = 0; i<populacaoIntermediaria[0].length-1; i++) {
            switch (populacaoIntermediaria[0][i]){
                case 0:
                    if(localizaSaida(agente, -1, -1, caminho)){
                    return caminho;
                }
                    break;
                case 1:
                    if(localizaSaida(agente, -1, 0, caminho)){
                    return caminho;
                }
                    break;
                case 2:
                    if(localizaSaida(agente, -1, 1, caminho)){
                    return caminho;
                }
                    break;
                case 3:
                    if(localizaSaida(agente, 0, -1, caminho)){
                    return caminho;
                }
                    break;
                case 4:
                    if(localizaSaida(agente, 0, 1, caminho)){
                    return caminho;
                }
                    break;
                case 5:
                    if(localizaSaida(agente, 1, -1, caminho)){
                    return caminho;
                }
                    break;
                case 6:
                    if(localizaSaida(agente, 1, 0, caminho)){
                    return caminho;
                }
                    break;
                case 7:
                    if(localizaSaida(agente, 1, 1, caminho)){
                        return caminho;
                    }
                    break;
            }
        }
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
