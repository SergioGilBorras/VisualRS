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
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.NumberOuts;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class QualityMeasureEMHistogram extends QualityMeasureFinal {

    NumberOuts nouts;

    /**
     * Calculate the medium error of the histogram
     *
     * @param nouts NumberOuts It is the numbers of branchs by node
     */
    public QualityMeasureEMHistogram(NumberOuts nouts) {
        this.nouts = nouts;
        name = "EM-Histograma";
    }

    @Override
    public double calculate() throws Exception {
        if (resultCalculate == null) {
            ArrayList<Double> Lout = new ArrayList(Arrays.asList(ArrayUtils.toObject(nouts.calculate())));

            Lout.sort((o1, o2) -> o1.compareTo(o2));
            double num = 0;
            double old = -1;
            double errorMedio = 0;
            double numOld = 0;
            double numCasos = 0;
            for (Double d : Lout) {
                if (d != old) {
                    if (old != -1) {
                        System.out.println(old + ":" + num);
                    }
                    if (old > 1) {
                        double aux = (numOld / 2);
                        errorMedio += Math.abs(aux - num);
                        numCasos++;
                    }
                    numOld = num;
                    old = d;
                    num = 0;
                }
                num++;
            }

            System.out.println(old + ":" + num);
            double aux = (numOld / 2);
            errorMedio += Math.abs(aux - num);
            numCasos++;

            resultCalculate = (numCasos / errorMedio);
        }
        return resultCalculate;
    }

}
