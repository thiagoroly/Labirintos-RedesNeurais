package com.pucrs;

import java.util.ArrayList;

public class MultiLayerPerceptron
{
    //private ArrayList<NeuronioMod> neuronios;
    private NeuronioMod n1;
    private NeuronioMod n2;
    private NeuronioMod n3;
    private NeuronioMod n4;
    private NeuronioMod nB;
    private NeuronioMod nC;
    private NeuronioMod nD;
    private NeuronioMod nE;

    public MultiLayerPerceptron(double[] weightValues){
        n1 = new NeuronioMod();
        n2 = new NeuronioMod();
        n3 = new NeuronioMod();
        n4 = new NeuronioMod();
        nB = new NeuronioMod();
        nC = new NeuronioMod();
        nD = new NeuronioMod();
        nE = new NeuronioMod();

        ArrayList<NeuronioMod> neuronios = new ArrayList<>();
        neuronios.add(n1);
        neuronios.add(n2);
        neuronios.add(n3);
        neuronios.add(n4);
        neuronios.add(nB);
        neuronios.add(nC);
        neuronios.add(nD);
        neuronios.add(nE);

        int count = 0;
        while (weightValues.length > count){
            NeuronioMod neuronio = new NeuronioMod(weightValues[count], weightValues[count+1], weightValues[count+2], weightValues[count+3], weightValues[count+4]);
            neuronios.add(neuronio);
            count += 5;
        }
    }
    
    public int generalizacao(int x1, int x2, int x3, int x4){  //uso da rede
        //Generalizacao - Teste da rede

        System.out.println("\n--- GENERALIZACAO");
        //propagação

        double y1 = n1.calculaV(x1, x2, x3, x4);
        double y2 = n2.calculaV(x1, x2, x3, x4);
        double y3 = n3.calculaV(x1, x2, x3, x4);
        double y4 = n4.calculaV(x1, x2, x3, x4);

        double yB = nB.calculaV(y1, y2, y3, y4);
        double yC = nC.calculaV(y1, y2, y3, y4);
        double yD = nD.calculaV(y1, y2, y3, y4);
        double yE = nE.calculaV(y1, y2, y3, y4);


        //maior valor entre os neuronios B, C, D e E
        double maxValue = yB;
        int coord = 1;

        if (yC > maxValue) {
            maxValue = yC;
            coord = 2;
        }

        if(yD > maxValue) {
            maxValue = yD;
            coord = 3;
        }

        if(yE > maxValue) {
            maxValue = yE;
            coord = 4;
        }

        System.out.println("Saida Gerada pela rede: " + maxValue + " a coordenada é " + coord);
        return coord;
    }
}
