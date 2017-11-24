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

import org.etsisi.visualrs.similarityMeasureBase.Kendalls;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class FKendalls extends SimilarityMeasureFinal {

    Kendalls pc;

    /**
     * Create the Kendall Rank Correlation Coefficient a similarity measure final
     */
    public FKendalls() {
        this.name = "Kendalls Correlation";
        pc = new Kendalls();
    }

    @Override
    public double calculate(double[] a1, double[] a2) {
        double val = -99;
        double[][] Vcomum = this.filterCommonVotes(a1, a2);
        if (Vcomum[0].length > minComunUsers) {
            val = pc.calculate(Vcomum[0], Vcomum[1]);
            if (Double.isNaN(val)) {
                val = 1;//
            }
        }
        return val;
    }

}
