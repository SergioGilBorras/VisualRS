/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.etsisi.visualrs.codeExample;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.etsisi.visualrs.similarityMeasureBase.SimilarityMeasureBase;

/**
 *
 * @author rasta
 */
public class SMBPearsonExample extends SimilarityMeasureBase {

    PearsonsCorrelation pc;

    public SMBPearsonExample() {
        pc = new PearsonsCorrelation();
    }

    @Override
    public double calculate(double[] a1, double[] a2) {
        return pc.correlation(a1, a2);
    }

}
