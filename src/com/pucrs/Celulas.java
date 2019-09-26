package com.pucrs;

public class Celulas {
    private int quantidade;
    private int distancias[][];
    private int coordenadas[][];
    private int matrizDeIDs[][];

    public int getQuantidade(){
        return quantidade;
    }

    public int[][] getDistancias(){
        return distancias;
    }

    public int[][] getCoordenadas(){
        return coordenadas;
    }

    public int[][] getMatrizDeIDs() {
        return matrizDeIDs;
    }

    public void converteMatrizParaGrafo(String[][] matriz){
        matrizDeIDs = new int[matriz.length][matriz[0].length];
        quantidade = contaCelulas(matriz);
        coordenadas = new int[quantidade][2];
        coletaCoordenadas(matriz);
        distancias = new int[quantidade][8];
        for(int i=0;i<quantidade;i++){
            calculaDistanciaDosVizinhos(i, matriz);
        }
    }

    private int contaCelulas(String[][] matriz){
        int total = 0;
        for (int i = 0; i < matriz.length; i++){
            for (int j = 0; j < matriz[0].length; j++){
                if(!matriz[i][j].contains("1")){
                    total++;
                }
            }
        }
        return total;
    }

    private void coletaCoordenadas(String[][] matriz){
        int celula = 0;
        for (int i = 0; i < matriz.length; i++){
            for (int j = 0; j < matriz[0].length; j++){
                if(!matriz[i][j].contains("1")){
                    coordenadas[celula][0] = i;
                    coordenadas[celula][1] = j;
                    matrizDeIDs[i][j] = celula;
                    celula++;
                }
                else{
                    matrizDeIDs[i][j] = -1;
                }
            }
        }
    }

    private void calculaDistanciaDosVizinhos(int celula, String[][] matriz){
        int celulaVizinha = 0;
        for(int x = -1; x < 2; x++){
            for(int y = -1; y < 2; y++){
                if(!(x == 0 && y == 0)){
                    int vizinhoX = coordenadas[celula][0] + x;
                    int vizinhoY = coordenadas[celula][1] + y;

                    if((vizinhoX > -1) && (vizinhoX < matriz.length) && (vizinhoY > -1) && (vizinhoY < matriz[0].length)){
                        if(!matriz[vizinhoX][vizinhoY].contains("1")){
                            distancias[celula][celulaVizinha] = 1;
                        }
                        else{
                            distancias[celula][celulaVizinha] = 0;
                        }
                    }
                    else{
                        distancias[celula][celulaVizinha] = 0;
                    }
                    celulaVizinha++;
                }
            }
        }
    }

}