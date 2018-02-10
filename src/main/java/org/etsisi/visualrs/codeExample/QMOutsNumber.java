/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.etsisi.visualrs.codeExample;

import org.etsisi.visualrs.models.MaximumSpanningTreeMatrix;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.QualityMeasureByNode;

/**
 *
 * @author rasta
 */
public class QMOutsNumber extends QualityMeasureByNode {

    public QMOutsNumber(MaximumSpanningTreeMatrix MRM) {
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
                    if (ColumItem[item1] >= -1) {
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
