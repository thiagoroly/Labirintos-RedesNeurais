package com.pucrs;

public class Labirinto {

    private String[][] matriz;

    public Labirinto(String[][] matriz) {
        this.matriz = matriz;
    }

    public Coordenada getEntrada(){
        for(int i = 0; i< matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if(matriz[i][j].contains("E")){
                    return new Coordenada(i, j);
                }
            }
        }
        return new Coordenada(0, 0);
    }

    public String[][] getMatriz() {
        return matriz;
    }
}
