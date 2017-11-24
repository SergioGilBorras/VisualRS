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

import org.etsisi.visualrs.qualityMeasures.fModification.FuntionModification;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class QualityMeasureModificate extends QualityMeasureByNode {

    FuntionModification FM;
    QualityMeasureByNode MS;

    /**
     * Apply a modification funtion to a quality measure by node
     *
     * @param FM FuntionModification It is the modification funtion to apply.
     * @param MS QualityMeasureByNode. It is the transformed quality measure
 by node
     */
    public QualityMeasureModificate(FuntionModification FM, QualityMeasureByNode MS) {
        super(null);
        this.FM = FM;
        this.MS = MS;
        name = FM.getName() + "_" + MS.getName();
    }

    @Override
    public double[] calculate() throws Exception {
        if (resultCalculate == null) {
            resultCalculate = FM.calculate(MS.calculate());
        }
        return resultCalculate;
    }

}
