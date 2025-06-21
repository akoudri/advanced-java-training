package com.akfc.training.concurrency;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;

public class VectorApiSimple {

    private int[] a = new int[1000];
    private int[] b = new int[1000];
    private int[] c = new int[1000];
    public void classicalApproach() {
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i] * b[i];
        }
    }

    public void vectorizedApproach() {
        VectorSpecies<Integer> species = IntVector.SPECIES_PREFERRED;
        IntVector va = IntVector.fromArray(species, a, 0);
        IntVector vb = IntVector.fromArray(species, b, 0);
        IntVector vc = va.mul(vb);
        vc.intoArray(c, 0);
    }

    public static void main(String[] args) {
        VectorApiSimple simple = new VectorApiSimple();
        simple.classicalApproach();
        simple.vectorizedApproach();
    }
}
