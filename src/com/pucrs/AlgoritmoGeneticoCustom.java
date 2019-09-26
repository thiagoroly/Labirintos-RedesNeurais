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

    public AlgoritmoGeneticoCustom(String[][] labirintoMatriz, int numGenes, int numCromossomos, int maxGeracoes) {
        this.numGenes = numGenes;
        this.numCromossomos = numCromossomos;
        this.maxGeracoes = maxGeracoes;
        this.labirintoMatriz = labirintoMatriz;
        populacao = new int [numCromossomos][numGenes+1];
        populacaoIntermediaria = new int [numCromossomos][numGenes+1];
        inicializaPopulacao(populacao);   //cria soluçoes aleatoriamente
    }


    /**
     * Gera populaçao inicial: conjunto de soluçoes candidatas
     */
    private static void inicializaPopulacao(int[][] populacao) {
        Random r = new Random();
        for(int i = 0; i < populacao.length; i++) {
            for(int j = 0; j < populacao[i].length -1; j++) {
                populacao[i][j] = r.nextInt(2);
            }
        }
    }
}
