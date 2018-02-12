/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.etsisi.visualrs.examples;

import org.etsisi.visualrs.similarityMeasureFinal.SimilarityMeasureFinal;

/**
 *
 * @author rasta
 */
 public class SMPearsonExample extends SimilarityMeasureFinal {

    SMBPearsonExample pc;
    public SMPearsonExample() {
        this.name = "Pearsons Correlation";
        pc = new SMBPearsonExample();
    }

    @Override
    public double calculate(double[] a1, double[] a2) {
        double val = -99;
        double[][] Vcomum = this.filterCommonVotes(a1, a2);
        if (Vcomum[0].length > minComunUsers) {
            val = pc.calculate(Vcomum[0], Vcomum[1]);
            if (Double.isNaN(val)) {
                val = 1;
            }
        }
        return val;
    }
}
