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

import tree_visualize.matrices.Distance_matrix_w1;
import tree_visualize.matrices.Distance_matrix_wq;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class Closeness_centrality_wq_w1 extends Quality_measure_by_node {

    /**
     * Calculate the closeness centrality to weight=1
     *
     * @param MD_w1 Distance_matrix_w1 The distance matrix with weight=1
     */
    public Closeness_centrality_wq_w1(Distance_matrix_w1 MD_w1) {
        super(MD_w1.getMatrix_w1());
        name = "Closeness_centrality_w1";
    }

    /**
     * Calculate the closeness centrality to weight=q
     *
     * @param MD_wq Distance_matrix_wq The distance matrix with weight=q
     */
    public Closeness_centrality_wq_w1(Distance_matrix_wq MD_wq) {
        super(MD_wq.getMatrix_wq());
        name = "Closeness_centrality_wq";
    }

    @Override
    public double[] calculate() throws Exception {
        if (result_calculate == null) {
            double[] res = new double[matriz.columns];
            for (int item1 = 0; item1 < matriz.columns; item1++) {
                double[] col = matriz.getColumn(item1).toArray();
                res[item1] = 0;
                for (int item = 0; item < matriz.columns; item++) {
                    if (col[item] != Double.MAX_VALUE) { //Quito los nodos no conectados. Porque tiene distancia infinita
                        res[item1] += col[item];
                    }
                }

                if (res[item1] != 0) {
                    res[item1] = 1 / res[item1];
                }

                //System.err.println(" -- " + res[item1]);
            }
            result_calculate = res;
        }
        return result_calculate;
    }

}
