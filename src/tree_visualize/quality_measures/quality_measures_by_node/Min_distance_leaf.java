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
package tree_visualize.quality_measures.quality_measures_by_node;

import java.util.ArrayList;
import tree_visualize.matrices.Maximum_spanning_tree_matrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class Min_distance_leaf extends Quality_measure_by_node {

    double[] n_outs;

    /**
     * Calculate distance to the nearest leaf by node
     *
     * @param MRM Maximum_spanning_tree_matrix The maximum spanning tree matrix to use
     * @param nouts Number_outs It is the numbers of branchs by node
     * @throws Exception
     */
    public Min_distance_leaf(Maximum_spanning_tree_matrix MRM, Number_outs nouts) throws Exception {
        super(MRM.getMaximum_spanning_tree_matrix());
        this.n_outs = nouts.calculate();
        if (n_outs.length != MRM.getMaximum_spanning_tree_matrix().columns) {
            throw new Exception("El numero de columnas de la matriz no coincide con el tamaño del vector. (metrica: dista_hoja)");
        }
        name = "Min_dista_leaf";
    }

    @Override
    public double[] calculate() throws Exception {
        if (result_calculate == null) {
            double[] distanciaHoja = new double[matriz.columns];

            double udistanciaHoja = 0;

            for (int item = 0; item < matriz.columns; item++) {

                ArrayList<Integer> nodosX = new ArrayList<>();
                ArrayList<Integer> nodosX_aux = new ArrayList<>();
                nodosX.add(item);
                boolean noHoja = (n_outs[item] != 1 && n_outs[item] != 0);

                while (noHoja) {
                    udistanciaHoja++;
                    for (Integer i : nodosX) {

                        double[] ColumItem = matriz.getColumn(i).toArray();

                        for (int item1 = 0; item1 < matriz.rows; item1++) {
                            if (ColumItem[item1] >=-1) {
                                if (i != item1) {
                                    nodosX_aux.add(item1);
                                }
                                if (n_outs[item1] == 1) {
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
                    nodosX = new ArrayList(nodosX_aux);
                    nodosX_aux.clear();
                }
                distanciaHoja[item] = udistanciaHoja;
                udistanciaHoja = 0;

            }
            result_calculate = distanciaHoja;
        }
        return result_calculate;
    }

}
