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

import org.etsisi.visualrs.similarityMeasureBase.SimilarityMeasureBase;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.QualityMeasureByNode;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class QualityMeasureCorrelation extends QualityMeasureFinal {

    SimilarityMeasureBase FC;
    QualityMeasureByNode MS1;
    QualityMeasureByNode MS2;

    /**
     *
     * @param FC SimilarityMeasureBase It is the similarity measure to compare
 two quality measure
     * @param MS1 QualityMeasureByNode It is the quality measure to compare
 number 1.
     * @param MS2 QualityMeasureByNode It is the quality measure to compare
 number 2.
     */
    public QualityMeasureCorrelation(SimilarityMeasureBase FC, QualityMeasureByNode MS1, QualityMeasureByNode MS2) {
        this.FC = FC;
        this.MS1 = MS1;
        this.MS2 = MS2;
        name = MS1.getName() + "_VS_" + MS2.getName();
    }

    @Override
    public double calculate() throws Exception {
        if (resultCalculate == null) {
            resultCalculate = FC.calculate(MS1.calculate(), MS2.calculate());
        }
        return resultCalculate;
    }

}
