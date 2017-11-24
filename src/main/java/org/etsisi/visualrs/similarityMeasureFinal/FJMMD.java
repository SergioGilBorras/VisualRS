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
package org.etsisi.visualrs.similarityMeasureFinal;

import org.etsisi.visualrs.similarityMeasureBase.Jaccard;
import org.etsisi.visualrs.similarityMeasureBase.MMD;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class FJMMD extends SimilarityMeasureFinal {

    MMD pc;
    Jaccard jc;

     /**
     * Create the JMMD correlation, combination of Jaccard correlation and MMD
     * (Mean Measure of Divergence). It is a similarity measure final
     */
    public FJMMD() {
        this.name = "JMMD Correlation";
        pc = new MMD();
        jc = new Jaccard();
    }

    @Override
    public double calculate(double[] a1, double[] a2) {
        double val = -99, jac = 0;
        double[][] Vcomum = this.filterCommonVotes(a1, a2);
        if (Vcomum[0].length > minComunUsers) {
            jac = jc.calculate(a1, a2);
            val =  jac * pc.calculate(Vcomum[0], Vcomum[1]);
            if (Double.isNaN(val)) {
                val = jac;
            }
        }
        return val;
    }

}
