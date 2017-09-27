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

import tree_visualize.matrices.Distance_matrix_wq;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class Betweenness_centrality extends Quality_measure_by_node {

    /**
     * Calculate the Betweenness_centrality from the distance matrix with
     * weight=q.
     *
     * @param MD_wq Distance_matrix_wq The distance matrix with weight=q.
     */
    public Betweenness_centrality(Distance_matrix_wq MD_wq) {
        super(MD_wq.getMatrix_wq());
        name = "Betweenness_centrality";
    }

    @Override
    public double[] calculate() throws Exception {
        if (result_calculate == null) {
            double[] res = new double[matriz.columns];
            for (int item = 0; item < matriz.columns; item++) {
                int caminos_pasan_item = 0;
                for (int item1 = 0; item1 < matriz.columns; item1++) {
                    for (int item2 = item1 + 1; item2 < matriz.columns; item2++) {
                        if (matriz.get(item1, item2) == matriz.get(item, item2) + matriz.get(item1, item)) {
                            caminos_pasan_item++;
                        }
                    }
                }

                res[item] = caminos_pasan_item / ((Math.pow(matriz.columns, 2) - matriz.columns) / 2);
            }
            result_calculate = res;
        }
        return result_calculate;
    }

}
