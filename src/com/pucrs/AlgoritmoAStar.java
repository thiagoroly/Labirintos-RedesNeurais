package com.pucrs;

import java.util.ArrayList;

/*
Implementação do Algoritmo A* : o algoritmo genético encontra o S e passa
para o A*: o labirinto, a célula de entrada E e a de saída S descoberta.
Implementar a versão em grafo que é uma extensão do Dijkstra.
*/

public class AlgoritmoAStar {

    private ArrayList<Nodo> conexoes;
    private ArrayList<Nodo> visitados;
    private ArrayList<Nodo> desconhecidos;
    private int matrizDistancias[][];
    private int matrizCoordenadas[][];
    private String matrizCaminho[][];
    private int matrizIDs[][];
    private Celulas arqControl;

    private int quantidadeNodos;
    private Nodo nodoInicial;
    private Nodo nodoFinal;
    private Nodo nodoAtual;

    public AlgoritmoAStar(){
        conexoes = new ArrayList<>();
        visitados = new ArrayList<>();
        desconhecidos = new ArrayList<>();
        arqControl = new Celulas();
    }
    
    public void carregaDados(String[][] matriz, Coordenada entrada, Coordenada saida){
        arqControl.converteMatrizParaGrafo(matriz, entrada, saida);
        quantidadeNodos = arqControl.getQuantidade();
        matrizDistancias = arqControl.getDistancias();
        matrizCoordenadas = arqControl.getCoordenadas();
        matrizCaminho = matriz;
        matrizIDs = arqControl.getMatrizDeIDs();
        
        for(int i = 0; i< quantidadeNodos; i++) desconhecidos.add(new Nodo(i));
        
        nodoInicial = containsNo(desconhecidos, arqControl.getIdNoInicial());
        nodoFinal = containsNo(desconhecidos, arqControl.getIdNoFinal());
        desconhecidos.remove(arqControl.getIdNoInicial());
        nodoAtual = nodoInicial;
    }

    public String encontraCaminho(){
        Nodo aux = null;
        String caminho = "";
        int distancia = 0;
        while(nodoFinal != nodoAtual){
            visitados.add(nodoAtual);
            conexoes.remove(nodoAtual);
            insereconexoes(nodoAtual.getId());
            nodoAtual = proximoNo();
            if(nodoAtual == null) return "Nao Existe caminho.";
        }
        
        aux = nodoFinal;
        distancia = nodoFinal.getPeso();
        while(aux != null){

            caminho = "(" + (matrizCoordenadas[aux.getId()][0]) + "," + (matrizCoordenadas[aux.getId()][1]) + ") " + caminho;
            //caminho = (aux.getId() + 1) + " " + caminho;
            aux = aux.getAnt();
        }
        return "Caminho: "+ caminho + "  Distancia: "+ distancia;
    }

    private void insereconexoes(int id){
        Nodo aux;
        
        for(int i = 0; i< 8; i++){
            if(matrizDistancias[id][i] > 0){
                int idVizinho = encontraIdVizinho(id, i);

                aux = containsNo(desconhecidos, idVizinho);
                int novoPeso = matrizDistancias[id][i] + nodoAtual.getPeso();
                if(aux != null){
                    if(!visitados.contains(aux)){
                        aux.setPeso(novoPeso);
                        aux.setAnt(nodoAtual);
                        conexoes.add(aux);
                        desconhecidos.remove(aux);
                    }
                }
                else{
                    aux = containsNo(conexoes,idVizinho);
                    if(aux != null){
                        if(aux.getPeso() > novoPeso){ 
                            aux.setPeso(novoPeso);
                            aux.setAnt(nodoAtual);
                        }
                    }
                }
            }
        }
    }

    private Nodo proximoNo(){
        Nodo prox;
        int menor;
        int aux = 0;
        if(conexoes.isEmpty())return null;
        
        prox = conexoes.get(0);
        int[] coord1 = matrizCoordenadas[prox.getId()]; 
        int[] coord2 = matrizCoordenadas[nodoFinal.getId()];
        menor = prox.getPeso() + heuristica(coord1[0],coord1[1],coord2[0],coord2[1]);
        
        for(Nodo cur: conexoes){
            coord1 = matrizCoordenadas[cur.getId()]; 
            aux = heuristica(coord1[0], coord1[1], coord2[0], coord2[1]);
            if((cur.getPeso() + aux) < menor){//se atual melhor que menor
                menor = cur.getPeso() + aux;
                prox = cur;
            }
        }
        return prox;
    }
    
    private int heuristica(int latitudeCelula1,int longitudeCelula1, int latitudeCelula2, int longitudeCelula2){
        return Math.abs(latitudeCelula1 - latitudeCelula2) + Math.abs(longitudeCelula1 - longitudeCelula2);
    }

    private Nodo containsNo(ArrayList<Nodo> list, int id){
        for(Nodo cur: list){
            if(cur.getId() == id){
                return cur;
            }
        }
        return null;
    }

    private int encontraIdVizinho(int celula, int direcao){
        int idVizinho = -1;
        int coorX = matrizCoordenadas[celula][0];
        int coorY = matrizCoordenadas[celula][1];

        switch (direcao){
            case 0:
                idVizinho = matrizIDs[coorX-1][coorY-1];
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
        return idVizinho;
    }

    public void desenhaCaminho(){
        Nodo aux;
        aux = nodoFinal;
        while(aux != null){
            if(matrizCaminho[matrizCoordenadas[aux.getId()][0]][matrizCoordenadas[aux.getId()][1]].equals("0")){
                matrizCaminho[matrizCoordenadas[aux.getId()][0]][matrizCoordenadas[aux.getId()][1]] = "$";
            }
            aux = aux.getAnt();
        }
        //return matrizCaminho;
    }
}