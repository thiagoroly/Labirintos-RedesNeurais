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
    private int totalPontos;
    private int scoreSaida;
    private boolean achouSaida;


    public AlgoritmoGeneticoCustom(String[][] labirintoMatriz, int numGenes, int numCromossomos, int maxGeracoes, Coordenada entrada) {
        this.numGenes = numGenes;
        this.numCromossomos = numCromossomos;
        this.maxGeracoes = maxGeracoes;
        this.labirintoMatriz = labirintoMatriz;
        this.entrada = entrada;
        populacao = new int [numCromossomos][numGenes+1];
        populacaoIntermediaria = new int [numCromossomos][numGenes+1];
        scoreSaida = populacao.length * populacao[0].length;
        achouSaida = false;
        inicializaPopulacao(populacao);   //cria soluçoes aleatoriamente

        //encontraSaida();
    }

    public Coordenada encontraSaida(){
        Coordenada saida = new Coordenada(0,0);
        for(int geracao = 0; geracao<maxGeracoes; geracao++) {
            System.out.println("Geraçao:" + geracao);
            System.out.println("Score da saída:" + scoreSaida);

            calculaFuncoesDeAptidao(populacao);
            //int melhor = selecionaElitismo(populacao, populacaoIntermediaria);
            if(totalPontos >= scoreSaida && achouSaida) {
                System.out.println(">>>> Achou a saída: ");
                saida.setX(visitados.get(visitados.size()-1).getX());
                saida.setY(visitados.get(visitados.size()-1).getY());
                mostraGeracao(populacao);
                break;
            }

            crossOver(populacaoIntermediaria, populacao);
            if(geracao%2==0)
                mutacao(populacaoIntermediaria);
            populacao = populacaoIntermediaria;
        }
        return saida;
    }

    /**
     * Gera populaçao inicial: conjunto de soluçoes candidatas
     */
    private static void inicializaPopulacao(int[][] populacao) {
        Random r = new Random();
        for(int i = 0; i < populacao.length; i++) {
            for(int j = 0; j < populacao[i].length -1; j++) {
                populacao[i][j] = r.nextInt(8)+1;
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
        totalPontos = 55;
        Coordenada agente = entrada;
        visitados = new ArrayList<Coordenada>();
        achouSaida = false;

        int i = 0;
        for(; i<linha.length-1 && !achouSaida; i++) {
            switch (i){
                case 0:

                    if (dentroDoLimite(agente.getX()-1,agente.getY()-1) && !labirintoMatriz[agente.getX()-1][agente.getY()-1].contains("1")){
                        agente.setX(agente.getX()-1);
                        agente.setY(agente.getY()-1);
                        if(jaVisitouCoordenada(agente.getX(), agente.getY())){
                            totalPontos -= 5;
                        }
                        else {
                            if(labirintoMatriz[agente.getX()][agente.getY()].contains("S")){
                                totalPontos += scoreSaida;
                                visitados.add(agente);
                                achouSaida = true;
                            }
                            totalPontos += 10;
                            visitados.add(agente);
                        }
                    }
                    else{
                        totalPontos-= 5;
                    }
                    break;
                case 1:
                    if (dentroDoLimite(agente.getX()-1,agente.getY()) && !labirintoMatriz[agente.getX()-1][agente.getY()].contains("1")){
                        agente.setX(agente.getX()-1);
                        agente.setY(agente.getY());
                        if(jaVisitouCoordenada(agente.getX(), agente.getY())){
                            totalPontos -= 5;
                        }
                        else {
                            if(labirintoMatriz[agente.getX()][agente.getY()].contains("S")){
                                totalPontos += scoreSaida;
                                visitados.add(agente);
                                achouSaida = true;
                            }
                            totalPontos += 10;
                            visitados.add(agente);
                        }
                    }
                    else{
                        totalPontos-= 5;
                    }
                    break;
                case 2:
                    if (dentroDoLimite(agente.getX()-1,agente.getY()+1) && !labirintoMatriz[agente.getX()-1][agente.getY()+1].contains("1")){
                        agente.setX(agente.getX()-1);
                        agente.setY(agente.getY()+1);
                        if(jaVisitouCoordenada(agente.getX(), agente.getY())){
                            totalPontos -= 5;
                        }
                        else {
                            if(labirintoMatriz[agente.getX()][agente.getY()].contains("S")){
                                totalPontos += scoreSaida;
                                visitados.add(agente);
                                achouSaida = true;
                            }
                            totalPontos += 10;
                            visitados.add(agente);
                        }
                    }
                    else{
                        totalPontos-= 5;
                    }
                    break;
                case 3:
                    if (dentroDoLimite(agente.getX(),agente.getY()-1) && !labirintoMatriz[agente.getX()][agente.getY()-1].contains("1")){
                        agente.setX(agente.getX());
                        agente.setY(agente.getY()-1);
                        if(jaVisitouCoordenada(agente.getX(), agente.getY())){
                            totalPontos -= 5;
                        }
                        else {
                            if(labirintoMatriz[agente.getX()][agente.getY()].contains("S")){
                                totalPontos += scoreSaida;
                                visitados.add(agente);
                                achouSaida = true;
                            }
                            totalPontos += 10;
                            visitados.add(agente);
                        }
                    }
                    else{
                        totalPontos-= 5;
                    }
                    break;
                case 4:
                    if (dentroDoLimite(agente.getX(),agente.getY()+1) && !labirintoMatriz[agente.getX()][agente.getY()+1].contains("1")){
                        agente.setX(agente.getX());
                        agente.setY(agente.getY()+1);
                        if(jaVisitouCoordenada(agente.getX(), agente.getY())){
                            totalPontos -= 5;
                        }
                        else {
                            if(labirintoMatriz[agente.getX()][agente.getY()].contains("S")){
                                totalPontos += scoreSaida;
                                visitados.add(agente);
                                achouSaida = true;
                            }
                            totalPontos += 10;
                            visitados.add(agente);
                        }
                    }
                    else{
                        totalPontos-= 5;
                    }
                    break;
                case 5:
                    if (dentroDoLimite(agente.getX()+1,agente.getY()-1) && !labirintoMatriz[agente.getX()+1][agente.getY()-1].contains("1")){
                        agente.setX(agente.getX()+1);
                        agente.setY(agente.getY()-1);
                        if(jaVisitouCoordenada(agente.getX(), agente.getY())){
                            totalPontos -= 5;
                        }
                        else {
                            if(labirintoMatriz[agente.getX()][agente.getY()].contains("S")){
                                totalPontos += scoreSaida;
                                visitados.add(agente);
                                achouSaida = true;
                            }
                            totalPontos += 10;
                            visitados.add(agente);
                        }
                    }
                    else{
                        totalPontos-= 5;
                    }
                    break;
                case 6:
                    if (dentroDoLimite(agente.getX()+1,agente.getY()) && !labirintoMatriz[agente.getX()+1][agente.getY()].contains("1")){
                        agente.setX(agente.getX()+1);
                        agente.setY(agente.getY());
                        if(jaVisitouCoordenada(agente.getX(), agente.getY())){
                            totalPontos -= 5;
                        }
                        else {
                            if(labirintoMatriz[agente.getX()][agente.getY()].contains("S")){
                                totalPontos += scoreSaida;
                                visitados.add(agente);
                                achouSaida = true;
                            }
                            totalPontos += 10;
                            visitados.add(agente);
                        }
                    }
                    else{
                        totalPontos-= 5;
                    }
                    break;
                case 7:
                    if (dentroDoLimite(agente.getX()+1,agente.getY()+1) && !labirintoMatriz[agente.getX()+1][agente.getY()+1].contains("1")){
                        agente.setX(agente.getX()+1);
                        agente.setY(agente.getY()+1);
                        if(jaVisitouCoordenada(agente.getX(), agente.getY())){
                            totalPontos -= 5;
                        }
                        else {
                            if(labirintoMatriz[agente.getX()][agente.getY()].contains("S")){
                                totalPontos += scoreSaida;
                                visitados.add(agente);
                                achouSaida = true;
                            }
                            totalPontos += 10;
                            visitados.add(agente);
                        }
                    }
                    else{
                        totalPontos-= 5;
                    }
                    break;
            }
        }
        linha[i] = totalPontos;
    }

    private boolean dentroDoLimite(int x, int y){
        if((x > -1) && (x < labirintoMatriz.length) && (y > -1) && (x < labirintoMatriz[0].length)){
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

        for(int i=1; i<populacao.length; i++) {
            if(populacao[i][numGenes] < menorCoord) {
                menorCoord = populacao[i][numGenes];
                elite = i;
            }
        }
        System.out.println("Elitismo - selecao; melhor do cromossomo: " + elite);

        for(int i=0; i<numGenes+1; i++) {
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
            System.out.println("Gerando dois filhos...");
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

        if(populacao[l1][numGenes] < populacao[l2][numGenes]) {
            System.out.println("Cromossomo de crossover: " + l1);
            return populacao[l1];
        }else {
            System.out.println("Cromossomo de crossover: " + l2);
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

            System.out.println("Cromossomo: " + linha + " sofreu mutacao");
        }

    }

    private void mostraGeracao(int[][] populacao) {
        System.out.println("__________________________________________________________________");
        for(int i = 0; i < populacao.length; i++) {
            System.out.print("(" + i + ") ");
            for(int j = 0; j < populacao[i].length-1; j++) {
                System.out.print(populacao[i][j] + " ");
            }
            System.out.println(" Aptidao: " + totalPontos);
        }
        System.out.println("__________________________________________________________________");
    }
}
