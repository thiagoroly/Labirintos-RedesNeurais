package com.pucrs;

import java.util.Random;

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

    public Coordenada encontraSaida(){
        Coordenada saida = new Coordenada(0,0);
        for(int geracao = 0; geracao<maxGeracoes; geracao++) {
            System.out.println("Geraçao:" + geracao);

            calculaFuncoesDeAptidao(populacao);


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
    private static void calculaFuncoesDeAptidao(int[][] populacao){
        for(int i = 0; i < populacao.length; i++) {
            funcaoDeAptidao(populacao[i]);
        }
    }

    /**
     * Funçao de aptidao: heuristica que estima a qualidade de uma soluçao
     */
    private void funcaoDeAptidao(int[] linha) {
        int totalPontos =0;
        Coordenada agente = entrada;
        ArrayList<Coordenada> vizitados = new

        int i = 0;
        for(; i<linha.length-1; i++) {
            switch (i){
                case 0:

                    if (dentroDoLimite(agente.getX()-1,agente.getY()-1)){
                        agente.setX(agente.getX()-1);
                        agente.setY(agente.getY()-1);

                    }
                    else{
                        totalPontos-= 5;
                    }

                    break;
                case 1:
                    idVizinho = matrizIDs[coorX-1][coorY];
                    break;
                case 2:
                    idVizinho = matrizIDs[coorX-1][coorY+1];
                    break;
                case 3:
                    idVizinho = matrizIDs[coorX][coorY-1];
                    break;
                case 4:
                    idVizinho = matrizIDs[coorX][coorY+1];
                    break;
                case 5:
                    idVizinho = matrizIDs[coorX+1][coorY-1];
                    break;
                case 6:
                    idVizinho = matrizIDs[coorX+1][coorY];
                    break;
                case 7:
                    idVizinho = matrizIDs[coorX+1][coorY+1];
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


}
