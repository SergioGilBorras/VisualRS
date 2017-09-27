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
package tree_visualize.matrices;

import java.io.File;
import java.util.LinkedList;
import tree_visualize.quality_measures.quality_measures_by_node.Number_outs;
import org.jblas.DoubleMatrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class Distance_matrix_wouts {

    private final Maximum_spanning_tree_matrix MRM;
    private DoubleMatrix tree_distance_matrix_wouts;
    private final Number_outs nouts;

    /**
     * Getter of the DoubleMatrix tree distance matrix wouts
     *
     * @return tree distance matrix with weight = number of branch of each node.
     */
    public DoubleMatrix getMatrix_wouts() {
        return tree_distance_matrix_wouts;
    }

    /**
     * Calculate the tree distance matrix with weight =  number of branch of each node.
     *
     * @param MRM Maximum spanning tree matrix
     * @param nouts number of branch of each node. Quality measure.
     * @throws Exception Different exceptions can be throws here with the
     * generation of the distance matrix
     */
    public Distance_matrix_wouts(Maximum_spanning_tree_matrix MRM, Number_outs nouts) throws Exception {
        this.MRM = MRM;
        this.nouts = nouts;
        if (has_calculate_matrix()) {
            load_calculate_matrix();
        } else {
            genera_correlation_matrix();
            save_calculate_matrix();
        }
    }

    private void genera_correlation_matrix() throws Exception {
        DoubleMatrix prim_matrix = MRM.getMaximum_spanning_tree_matrix();
        System.out.println("generate_distace_matrix_wouts");
        tree_distance_matrix_wouts = new DoubleMatrix(prim_matrix.columns, prim_matrix.columns);
        tree_distance_matrix_wouts.fill(-1);
        long time = System.currentTimeMillis();
        for (int item = 0; item < prim_matrix.columns; item++) {
            if (item % 500 == 0) {
                System.out.println("Vuelta Item:: " + item + " - " + (System.currentTimeMillis() - time));
                time = System.currentTimeMillis();
            }
            tree_distance_matrix_wouts.putColumn(item, new DoubleMatrix(DIJKSTRA_wouts(item)));
            //tree_distance_matrix_wouts.putColumn(item, DIJKSTRA_wouts(item));
        }
    }

    private double[] DIJKSTRA_wouts(int s) throws Exception {
        DoubleMatrix prim_matrix = MRM.getMaximum_spanning_tree_matrix();
        double[] out = nouts.calculate();

        double[] distancia = new double[prim_matrix.columns];
        boolean[] visto = new boolean[prim_matrix.columns];
        LinkedList<Integer> cola = new LinkedList<>();
        for (int item = 0; item < prim_matrix.columns; item++) {
            distancia[item] = Double.MAX_VALUE;
            visto[item] = false;
        }
        distancia[s] = out[s];
        cola.addFirst(s);
        while (!cola.isEmpty()) {
            int u = cola.removeLast();
            visto[u] = true;
            double[] ColumItem = prim_matrix.getColumn(u).toArray();

            for (int v = 0; v < prim_matrix.rows; v++) {
                if (ColumItem[v] >= -1) {
                    if (!visto[v] && distancia[v] > (distancia[u] + out[v])) {
                        distancia[v] = distancia[u] + out[v];
                        cola.addFirst(v);

                    }
                }
            }
        }
        return distancia;
    }

    private void save_calculate_matrix() {
        System.out.println("Save calculate matrix distance with weight=nouts ..");
        File f = new File("./data/" + MRM.getFile_name() + "/" + MRM.getSimilarity_measure_name());
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            tree_distance_matrix_wouts.save("./data/" + MRM.getFile_name() + "/" + MRM.getSimilarity_measure_name() + "/distancia_wouts_matrix.dat");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void load_calculate_matrix() {
        System.out.println("Load calculate matrix distance with weight=nout ..");
        try {
            if (tree_distance_matrix_wouts == null) {
                tree_distance_matrix_wouts = new DoubleMatrix();
                tree_distance_matrix_wouts.load("./data/" + MRM.getFile_name() + "/" + MRM.getSimilarity_measure_name() + "/distancia_wouts_matrix.dat");
            }
        } catch (Exception e) {
            System.out.println("Excepcion (Load calculate matrix distance with weight=nout): " + e.getMessage());
        }
    }

    private boolean has_calculate_matrix() {
        File f = new File("./data/" + MRM.getFile_name() + "/" + MRM.getSimilarity_measure_name() + "/distancia_wouts_matrix.dat");
        return f.exists();
    }
}
