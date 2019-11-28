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

        int i = 0;
        while (i*5 < weightValues.length -6){
            NeuronioMod neuronio = neuronios.get(i);
            neuronio.setW0(weightValues[i]);
            neuronio.setW1(weightValues[i+1]);
            neuronio.setW2(weightValues[i+2]);
            neuronio.setW3(weightValues[i+3]);
            neuronio.setW4(weightValues[i+4]);
            i++;
        }
    }
    
    public int generalizacao(double x1, double x2, double x3, double x4){  //uso da rede
        //Generalizacao - Teste da rede

        //System.out.println("\n--- GENERALIZACAO");
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

        String coordebug = "baixo";

        if (yC > maxValue) {
            maxValue = yC;
            coord = 2;
            coordebug = "cima";
        }

        if(yD > maxValue) {
            maxValue = yD;
            coord = 3;
            coordebug = "direita";
        }

        if(yE > maxValue) {
            maxValue = yE;
            coord = 4;
            coordebug = "esquerda";
        }

        //System.out.println("((" + coordebug + ")) | Saída: "+ maxValue);
        return coord;
    }
}
