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
package org.etsisi.visualrs.similarityMeasureBase;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class SM extends SimilarityMeasureBase {

    int border = 0;
    double[] SP = null;
    double[] SN = null;

    /**
     * Create SM a similarity measure base
     *
     * @param border limit between positive and negative.
     * @param SPU Array of double with the positive similarity vectors.
     * @param SNU Array of double with the negative similarity vectors.
     */
    public SM(int border, double[] SPU, double[] SNU) {
        this.border = border;
        this.SP = SPU;
        this.SN = SNU;
    }

    @Override
    public double calculate(double[] a1, double[] a2) {
        if (a1 == null || a2 == null || a1.length != a2.length) {
            throw new IllegalArgumentException("Uno de los parametros es null. O no tienen la misma longitud");
        }
        double nIndexA = 0;
        double nIndexB = 0;
        double nIndexC = 0;
        double sumIndexA = 0;
        double sumIndexB = 0;
        double sumIndexC = 0;

        for (int i = 0; i < a1.length; i++) {
            //System.err.println(a1[i] + " -- " + a2[i]);
            if (a1[i] >= border && a2[i] >= border) {
                nIndexA++;
                sumIndexA += (1 - Math.pow(a1[i] - a2[i], 2)) * (SP[i] * SP[i]);
            } else if (a1[i] > 0 && a1[i] < border && a2[i] > 0 && a2[i] < border) {
                nIndexB++;
                sumIndexB += (1 - Math.pow(a1[i] - a2[i], 2)) * (SN[i] * SN[i]);
            } else if (a1[i] > 0 && a1[i] < border && a2[i] >= border) {
                nIndexC++;
                sumIndexC += (1 - Math.pow(a1[i] - a2[i], 2)) * (SP[i] * SN[i]);
            } else if (a1[i] >= border && a2[i] > 0 && a2[i] < border) {
                nIndexC++;
                sumIndexC += (1 - Math.pow(a1[i] - a2[i], 2)) * (SP[i] * SN[i]);
            }

        }
        double div = 0;
        double A = 0;
        if (nIndexA != 0) {
            A = sumIndexA / nIndexA;
            div++;
        }
        double B = 0;
        if (nIndexB != 0) {
            B = sumIndexB / nIndexB;
            div++;
        }
        double C = 0;
        if (nIndexC != 0) {
            C = sumIndexC / nIndexC;
            div++;
        }
        
        return (1 / div) * (A + B + C);

    }

}
