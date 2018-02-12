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
package org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode;

import org.etsisi.visualrs.models.DistanceMatrixW1;
import org.etsisi.visualrs.models.DistanceMatrixWq;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class ClosenessCentralityWqW1 extends QualityMeasureByNode {

    /**
     * Calculate the closeness centrality to weight=1
     *
     * @param MDW1 DistanceMatrixW1 The distance matrix with weight=1
     */
    public ClosenessCentralityWqW1(DistanceMatrixW1 MDW1) {
        super(MDW1.getMatrixW1());
        name = "ClosenessCentralityW1";
    }

    /**
     * Calculate the closeness centrality to weight=q
     *
     * @param MDWq DistanceMatrixWq The distance matrix with weight=q
     */
    public ClosenessCentralityWqW1(DistanceMatrixWq MDWq) {
        super(MDWq.getMatrixWq());
        name = "ClosenessCentralityWq";
    }

    @Override
    public double[] calculate() throws Exception {
        if (resultCalculate == null) {
            double[] res = new double[matriz.columns];
            for (int item1 = 0; item1 < matriz.columns; item1++) {
                double[] col = matriz.getColumn(item1).toArray();
                res[item1] = 0;
                for (int item = 0; item < matriz.columns; item++) {
                    if (col[item] != Double.MAX_VALUE) { //Quito los nodos no conectados. Porque tiene distancia infinita
                        res[item1] += col[item];
                    }
                }

                if (res[item1] != 0) {
                    res[item1] = 1 / res[item1];
                }
            }
            resultCalculate = res;
        }
        return resultCalculate;
    }

}
