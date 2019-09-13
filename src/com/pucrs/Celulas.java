package com.pucrs;

import java.io.*;

public class Celulas {
    private int quantidade;
    private int distancias[][];
    private int coordenadas[][];

    public int getQuantidade(){
        return quantidade;
    }

    public int[][] getDistancias(){
        return distancias;
    }

    public int[][] getCoordenadas(){
        return coordenadas;
    }

    public void readArquivo(String arq){
        File arquivo = new File(arq);
        FileReader arqLeitura;
        BufferedReader leitor;
        String linha;
        String[] val;
        try {
            arqLeitura = new FileReader(arquivo);
            leitor = new BufferedReader(arqLeitura);

            try {
                linha = leitor.readLine();
                quantidade = Integer.parseInt(linha);
                distancias = new int[quantidade][quantidade];
                coordenadas = new int[quantidade][2];

                linha = leitor.readLine();

                for(int i=0;i<quantidade;i++){
                    linha = leitor.readLine();
                    val = linha.split(" ");
                    for(int j=0;j<2;j++){
                        coordenadas[i][j] = Integer.parseInt(val[j]);
                    }
                }

                linha = leitor.readLine();

                for(int i=0;i<quantidade;i++){
                    linha = leitor.readLine();
                    val = linha.split(" ");
                    for(int j=0;j<quantidade;j++){
                        distancias[i][j] = Integer.parseInt(val[j]);
                    }
                }


            } catch (IOException e) {
                System.out.println("ERRO ao ler o aquivo");
                System.exit(0);
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERRO ao ler o aquivo");
            System.exit(0);
        }
    }

    public void converteMatrizParaGrafo(String[][] matriz){
        quantidade = matriz.length * matriz[0].length;
        distancias = new int[quantidade][8];
        coordenadas = new int[quantidade][2];

        for(int i=0;i<quantidade;i++){
            for(int j=0;j<2;j++){
                coordenadas[i][j] = Integer.parseInt(matriz[i][j]);
            }
        }
        for(int i=0;i<quantidade;i++){
            for(int j=0;j<quantidade;j++){
                if((Integer.parseInt(matriz[i][j]) == 0) || (matriz[i][j]).contains("E") ||
                        (matriz[i-1][j-1]).contains("S")){
                    distancias[i][j] = 1;
                }
                if((Integer.parseInt(matriz[i][j]) == 1)){
                    distancias[i][j] = 0;
                }
            }
        }
    }
}
