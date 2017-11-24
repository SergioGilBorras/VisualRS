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

import org.etsisi.visualrs.models.MaximumSpanningTreeMatrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class NumberOuts extends QualityMeasureByNode {

    /**
     * Calculate the number of branch by node
     *
     * @param MRM MaximumSpanningTreeMatrix The maximum spanning tree matrix to use
     */
    public NumberOuts(MaximumSpanningTreeMatrix MRM) {
        super(MRM.getMaximumSpanningTreeMatrix());
        name = "NumberOuts";
    }

    @Override
    public double[] calculate() throws Exception {
        if (resultCalculate == null) {
            double[] out = new double[matriz.columns];

            double uout = 0;

            for (int item = 0; item < matriz.columns; item++) {

                double[] ColumItem = matriz.getColumn(item).toArray();

                for (int item1 = 0; item1 < matriz.rows; item1++) {
                    if (ColumItem[item1] >=-1) {
                        uout++;
                    }
                }

                out[item] = uout;
                uout = 0;

            }
            resultCalculate = out;
        }
        return resultCalculate;
    }

}
