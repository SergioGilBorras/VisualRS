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
import tree_visualize.matrices.Distance_matrix_w1;
import tree_visualize.matrices.Distance_matrix_wouts;
import tree_visualize.matrices.Distance_matrix_wq;
//import tree_visualize.matrices.DoubleMatrix;
import org.jblas.DoubleMatrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class Avg_distance_leaves_outs_wq_w1 extends Quality_measure_by_node {

    double[] n_outs;
    DoubleMatrix path_outs_matrix;

    /**
     * Calculate the distance average to the leaves/N_outs from the distance
     * matrix with weight=1
     *
     * @param MD_w1 Distance_matrix_w1 The distance matrix with weight=1
     * @param MD_wouts Distance_matrix_wouts The distance matrix with
     * weight=N_outs
     * @param nouts Number_outs It is the numbers of branchs by node
     * @throws Exception Different exceptions can be throws here with the
     * calculate of the Avg distance leaves outs w1
     */
    public Avg_distance_leaves_outs_wq_w1(Distance_matrix_w1 MD_w1, Distance_matrix_wouts MD_wouts, Number_outs nouts) throws Exception {
        super(MD_w1.getMatrix_w1());
        this.n_outs = nouts.calculate();
        if (n_outs.length != MD_w1.getMatrix_w1().columns) {
            throw new Exception("El numero de columnas de la matriz no coincide con el tamaño del vector. (metrica: dista_hoja)");
        }

        this.path_outs_matrix = MD_wouts.getMatrix_wouts();

        name = "Media_dista_hoja_outs_w1";
    }

    /**
     * Calculate the distance average to the leaves/N_outs from the distance
     * matrix with weight=q
     *
     * @param MD_wq Distance_matrix_wq The distance matrix with weight=q
     * @param MD_wouts Distance_matrix_wouts The distance matrix with
     * weight=N_outs
     * @param nouts Number_outs It is the numbers of branchs by node
     * @throws Exception Different exceptions can be throws here with the
     * calculate of the Avg distance leaves outs wq
     */
    public Avg_distance_leaves_outs_wq_w1(Distance_matrix_wq MD_wq, Distance_matrix_wouts MD_wouts, Number_outs nouts) throws Exception {
        super(MD_wq.getMatrix_wq());
        this.n_outs = nouts.calculate();
        if (n_outs.length != MD_wq.getMatrix_wq().columns) {
            throw new Exception("El numero de columnas de la matriz no coincide con el tamaño del vector. (metrica: dista_hoja)");
        }

        this.path_outs_matrix = MD_wouts.getMatrix_wouts();

        name = "Media_dista_hoja_outs_wq";
    }

    @Override
    public double[] calculate() throws Exception {
        if (result_calculate == null) {
            double[] distanciaHoja = new double[matriz.columns];
            ArrayList<Integer> hojas = new ArrayList<>();
            int n_hojas = 0;

            for (int item = 0; item < matriz.columns; item++) {

                if (n_outs[item] == 1) {
                    n_hojas++;
                    hojas.add(item);
                }

            }
            double media_hojas = 0;

            for (int item1 = 0; item1 < matriz.rows; item1++) {
                if (n_outs[item1] > 0) {
                    double[] ColumItem = matriz.getColumn(item1).toArray();
                    double[] ColumItem_path_outs_matrix = path_outs_matrix.getColumn(item1).toArray();
                    for (Integer i : hojas) {
                        media_hojas += (ColumItem[i] / ColumItem_path_outs_matrix[i]);
                    }
                    distanciaHoja[item1] = (media_hojas / n_hojas);
                    media_hojas = 0;
                }
            }
            result_calculate = distanciaHoja;
        }
        return result_calculate;
    }

}
