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

import org.etsisi.visualrs.matrices.DoubleMatrix;
import org.etsisi.visualrs.io.LoadData;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'"
 */
public class SumVotes extends QualityMeasureByNode {

    DoubleMatrix matrizDispersa;

    /**
     * Calculate the sum of votes by node
     *
     * @param MV LoadData Matrix of votes to use.
     */
    public SumVotes(LoadData MV) {
        super(null);
        matrizDispersa = MV.getRankMatrix();
        name = "SumVotes";
    }

    @Override
    public double[] calculate() throws Exception {
        if (resultCalculate == null) {

            double[] sumVotos = new double[matrizDispersa.columns];

            double usumVotos = 0;

            for (int item = 0; item < matrizDispersa.columns; item++) {

                double[] ColumItemRank = matrizDispersa.getColumn(item).toArray();

                for (int item1 = 0; item1 < matrizDispersa.rows; item1++) {
                    if (ColumItemRank[item1] != -1) {
                        usumVotos += ColumItemRank[item1];
                    }
                }

                sumVotos[item] = usumVotos;
                usumVotos = 0;

            }
            resultCalculate = sumVotos;
        }
        return resultCalculate;
    }

}
