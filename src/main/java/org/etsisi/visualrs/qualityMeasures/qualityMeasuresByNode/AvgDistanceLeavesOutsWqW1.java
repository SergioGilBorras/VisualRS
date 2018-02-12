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
import org.etsisi.visualrs.models.DistanceMatrixWOuts;
import org.etsisi.visualrs.models.DistanceMatrixWq;
import org.jblas.DoubleMatrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class AvgDistanceLeavesOutsWqW1 extends QualityMeasureByNode {

    double[] nOuts;
    DoubleMatrix pathOutsMatrix;

    /**
     * Calculate the distance average to the leaves/nOuts from the distance
     * matrix with weight=1
     *
     * @param MDW1 DistanceMatrixW1 The distance matrix with weight=1
     * @param MDWouts DistanceMatrixWOuts The distance matrix with
 weight=nOuts
     * @param nouts NumberOuts It is the numbers of branchs by node
     * @throws Exception Different exceptions can be throws here with the
     * calculate of the Avg distance leaves outs w1
     */
    public AvgDistanceLeavesOutsWqW1(DistanceMatrixW1 MDW1, DistanceMatrixWOuts MDWouts, NumberOuts nouts) throws Exception {
        super(MDW1.getMatrixW1());
        this.nOuts = nouts.calculate();
        if (nOuts.length != MDW1.getMatrixW1().columns) {
            throw new Exception("El numero de columnas de la matriz no coincide con el tamaño del vector. (metrica: AvgDistanceLeavesOutsWqW1)");
        }

        this.pathOutsMatrix = MDWouts.getMatrixWouts();

        name = "MediaDistaHojaOutsW1";
    }

    /**
     * Calculate the distance average to the leaves/nOuts from the distance
     * matrix with weight=q
     *
     * @param MDWq DistanceMatrixWq The distance matrix with weight=q
     * @param MDWouts DistanceMatrixWOuts The distance matrix with
 weight=nOuts
     * @param nouts NumberOuts It is the numbers of branchs by node
     * @throws Exception Different exceptions can be throws here with the
     * calculate of the Avg distance leaves outs wq
     */
    public AvgDistanceLeavesOutsWqW1(DistanceMatrixWq MDWq, DistanceMatrixWOuts MDWouts, NumberOuts nouts) throws Exception {
        super(MDWq.getMatrixWq());
        this.nOuts = nouts.calculate();
        if (nOuts.length != MDWq.getMatrixWq().columns) {
            throw new Exception("El numero de columnas de la matriz no coincide con el tamaño del vector. (metrica: AvgDistanceLeavesOutsWqW1)");
        }

        this.pathOutsMatrix = MDWouts.getMatrixWouts();

        name = "MediaDistaHojaOutsWq";
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
                    double[] ColumItemPathOutsMatrix = pathOutsMatrix.getColumn(item1).toArray();
                    for (Integer i : hojas) {
                        mediaHojas += (ColumItem[i] / ColumItemPathOutsMatrix[i]);
                    }
                    distanciaHoja[item1] = (mediaHojas / nHojas);
                    mediaHojas = 0;
                }
            }
            resultCalculate = distanciaHoja;
        }
        return resultCalculate;
    }

}
