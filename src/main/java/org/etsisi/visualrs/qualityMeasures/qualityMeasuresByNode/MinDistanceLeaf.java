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
import org.etsisi.visualrs.models.MaximumSpanningTreeMatrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class MinDistanceLeaf extends QualityMeasureByNode {

    double[] nOuts;

    /**
     * Calculate distance to the nearest leaf by node
     *
     * @param MRM MaximumSpanningTreeMatrix The maximum spanning tree matrix to use
     * @param nouts NumberOuts It is the numbers of branchs by node
     * @throws Exception Launch the exception for different size between matrix and vector.
     */
    public MinDistanceLeaf(MaximumSpanningTreeMatrix MRM, NumberOuts nouts) throws Exception {
        super(MRM.getMaximumSpanningTreeMatrix());
        this.nOuts = nouts.calculate();
        if (nOuts.length != MRM.getMaximumSpanningTreeMatrix().columns) {
            throw new Exception("El numero de columnas de la matriz no coincide con el tamaño del vector. (metrica: MinDistanceLeaf)");
        }
        name = "MinDistanceLeaf";
    }

    @Override
    public double[] calculate() throws Exception {
        if (resultCalculate == null) {
            double[] distanciaHoja = new double[matriz.columns];

            double udistanciaHoja = 0;

            for (int item = 0; item < matriz.columns; item++) {

                ArrayList<Integer> nodosX = new ArrayList<>();
                ArrayList<Integer> nodosXAux = new ArrayList<>();
                nodosX.add(item);
                boolean noHoja = (nOuts[item] != 1 && nOuts[item] != 0);

                while (noHoja) {
                    udistanciaHoja++;
                    for (Integer i : nodosX) {

                        double[] ColumItem = matriz.getColumn(i).toArray();

                        for (int item1 = 0; item1 < matriz.rows; item1++) {
                            if (ColumItem[item1] >=-1) {
                                if (i != item1) {
                                    nodosXAux.add(item1);
                                }
                                if (nOuts[item1] == 1) {
                                    noHoja = false;
                                    break;
                                }
                                if (item1 <= item) {
                                    udistanciaHoja += distanciaHoja[item1];
                                    noHoja = false;
                                    break;
                                }

                            }
                        }
                        if (!noHoja) {
                            break;
                        }

                    }
                    nodosX = new ArrayList(nodosXAux);
                    nodosXAux.clear();
                }
                distanciaHoja[item] = udistanciaHoja;
                udistanciaHoja = 0;

            }
            resultCalculate = distanciaHoja;
        }
        return resultCalculate;
    }

}
