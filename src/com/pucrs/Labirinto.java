package com.pucrs;

import java.util.ArrayList;

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

    public String desenhaMatriz(ArrayList<Coordenada> caminho){
        boolean aux;
        String saida = "";
        for(int i = 0; i < this.matriz.length; i++){
            for(int j = 0; j < this.matriz[0].length; j++){
                aux = false;
                for(Coordenada coor : caminho){
                    if((coor.getX() == i) && (coor.getY() == j)){
                        saida += "$ ";
                        aux = true;
                    }
                }
                if(!aux){
                    saida += this.matriz[i][j] + " ";
                }
            }
            saida += "\n";
        }
        return saida;
    }
}
