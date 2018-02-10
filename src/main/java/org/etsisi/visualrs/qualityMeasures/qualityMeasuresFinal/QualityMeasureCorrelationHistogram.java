/*
 * Copyright (C) 2017 Sergio Gil Borras
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.etsisi.visualrs.qualityMeasures.qualityMeasuresFinal;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.etsisi.visualrs.similarityMeasureBase.SimilarityMeasureBase;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.NumberOuts;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class QualityMeasureCorrelationHistogram extends QualityMeasureFinal {

    NumberOuts nouts;
    SimilarityMeasureBase FC;

    /**
     * Calculate the correlation of the histogram
     *
     * @param FC SimilarityMeasureBase It is the similarity measure to compare the histograms
     * @param nouts NumberOuts It is the numbers of branchs by node
     */
    public QualityMeasureCorrelationHistogram(SimilarityMeasureBase FC, NumberOuts nouts) {
        this.nouts = nouts;
        this.FC = FC;
        name = "Correlacion-Histograma";
    }

    @Override
    public double calculate() throws Exception {
        if (resultCalculate == null) {
            ArrayList<Double> Lout = new ArrayList(Arrays.asList(ArrayUtils.toObject(nouts.calculate())));
            Lout.sort((o1, o2) -> o1.compareTo(o2));
            double num = 0;
            double old = -1;
            double numOld = 0;
            ArrayList<Double> valA = new ArrayList<>();
            ArrayList<Double> valB = new ArrayList<>();
            for (Double d : Lout) {
                if (d != old) {
                    //if (old != -1) {
                    //    System.out.println(old + ":" + num);
                    //}
                    if (old > 1) {
                        double aux = (numOld / 2);
                        valA.add(aux);
                        valB.add(num);
                    }
                    numOld = num;
                    old = d;
                    num = 0;
                }
                num++;
            }
            //System.out.println(old + ":" + num);
            double aux = (numOld / 2);
            valA.add(aux);
            valB.add(num);

            resultCalculate = FC.calculate(ArrayUtils.toPrimitive(valA.toArray(new Double[valA.size()])), ArrayUtils.toPrimitive(valB.toArray(new Double[valB.size()])));

        }
        return resultCalculate;
    }

}
