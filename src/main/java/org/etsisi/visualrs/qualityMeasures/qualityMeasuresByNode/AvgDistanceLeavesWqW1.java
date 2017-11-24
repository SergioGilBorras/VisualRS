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

import java.util.ArrayList;
import org.etsisi.visualrs.models.DistanceMatrixW1;
import org.etsisi.visualrs.models.DistanceMatrixWq;
//import org.etsisi.visualrs.matrices.DoubleMatrix;
import org.jblas.DoubleMatrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class AvgDistanceLeavesWqW1 extends QualityMeasureByNode {

    double[] nOuts;

    /**
     * Calculate the distance average to the leaves from the distance matrix
     * with weight=1
     *
     * @param MDW1 DistanceMatrixW1 The distance matrix with weight=1
     * @param nOuts NumberOuts It is the numbers of branchs by node
     * @throws Exception Different exceptions can be throws here with the
     * calculate of the AvgDistanceLeavesWqW1
     */
    public AvgDistanceLeavesWqW1(DistanceMatrixW1 MDW1, NumberOuts nOuts) throws Exception {
        super(MDW1.getMatrixW1());
        DoubleMatrix dm = MDW1.getMatrixW1();
        this.nOuts = nOuts.calculate();
        if (this.nOuts.length != dm.columns) {
            throw new Exception("El numero de columnas de la matriz no coincide con el tamaño del vector. (metrica: AvgDistanceLeavesWqW1)");
        }
        name = "MediaDistaHojaW1";
    }

    /**
     * Calculate the distance average to the leaves from the distance matrix
     * with weight=q
     *
     * @param MDWq DistanceMatrixWq The distance matrix with weight=q
     * @param nOuts NumberOuts It is the numbers of branchs by node
     * @throws Exception Different exceptions can be throws here with the
     * calculate of the AvgDistanceLeavesWqW1
     */
    public AvgDistanceLeavesWqW1(DistanceMatrixWq MDWq, NumberOuts nOuts) throws Exception {
        super(MDWq.getMatrixWq());
        DoubleMatrix dm = MDWq.getMatrixWq();
        this.nOuts = nOuts.calculate();
        if (this.nOuts.length != dm.columns) {
            throw new Exception("El numero de columnas de la matriz no coincide con el tamaño del vector. (metrica: AvgDistanceLeavesWqW1)");
        }
        name = "MediaDistaHojaWq";
    }

    @Override
    public double[] calculate() throws Exception {
        if (resultCalculate == null) {
            double[] distanciaHoja = new double[matriz.columns];
            ArrayList<Integer> hojas = new ArrayList<>();
            int nHojas = 0;

            for (int item = 0; item < matriz.columns; item++) {

                if (nOuts[item] == 1) {
                    nHojas++;
                    hojas.add(item);
                }

            }
            double mediaHojas = 0;

            for (int item1 = 0; item1 < matriz.rows; item1++) {
                if (nOuts[item1] > 0) {
                    double[] ColumItem = matriz.getColumn(item1).toArray();

                    for (Integer i : hojas) {

                        if (ColumItem[i] == Double.MAX_VALUE) {
                            //System.out.println("RAS:: " + item1+ " -- " + i + " __ " + ColumItem[i]);
                            mediaHojas += 1;
                        } else {
                            mediaHojas += ColumItem[i];
                        }

                    }
                    distanciaHoja[item1] = (mediaHojas / nHojas);
                    //System.out.println("RAS:: " + distanciaHoja[item1] + " - " + mediaHojas + " - " + nHojas);
                    mediaHojas = 0;
                }
            }

            resultCalculate = distanciaHoja;
        }
        return resultCalculate;
    }

}
