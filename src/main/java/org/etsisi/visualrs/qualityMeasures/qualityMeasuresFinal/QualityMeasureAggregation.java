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

import org.etsisi.visualrs.qualityMeasures.fAggregation.FuntionAggregation;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.QualityMeasureByNode;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class QualityMeasureAggregation extends QualityMeasureFinal {

    FuntionAggregation FA;
    QualityMeasureByNode MS1;

    /**
     * Apply a Aggregation funtion to a quality measure by node
     *
     * @param FA FuntionAggregation It is the aggregation funtion to apply.
     * @param MS1 QualityMeasureByNode. It is the transformed quality measure by node
     */
    public QualityMeasureAggregation(FuntionAggregation FA, QualityMeasureByNode MS1) {
        this.FA = FA;
        this.MS1 = MS1;
        name = FA.getName() + "_" + MS1.getName();
    }

    @Override
    public double calculate() throws Exception {
        if (resultCalculate == null) {
            resultCalculate = FA.calculate(MS1.calculate());
        }
        return resultCalculate;
    }

}
