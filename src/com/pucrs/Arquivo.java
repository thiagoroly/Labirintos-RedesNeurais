package com.pucrs;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Arquivo {

    public static String[][] learquivo(String caminho){
        String[][] matriz = new String[0][];
        try (BufferedReader br = Files.newBufferedReader(Paths.get(caminho))) {
            String linhaValor;
            int linhaNumero = 0;
            String[] colunas;
            while ((linhaValor = br.readLine()) != null) {
                if(linhaNumero == 0){
                    matriz = new String[Integer.parseInt(linhaValor)][Integer.parseInt(linhaValor)];
                }
                else {
                    colunas = linhaValor.split(" ");
                    for (int i = 0; i < colunas.length; i++) {
                        matriz[linhaNumero - 1][i] = colunas[i];
                    }
                }
                linhaNumero++;
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        return matriz;
    }
}
