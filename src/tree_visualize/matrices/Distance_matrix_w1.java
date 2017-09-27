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
import org.jblas.DoubleMatrix;

/**
 * This class calculates the tree distance matrix with weight = 1
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class Distance_matrix_w1 {

    private Maximum_spanning_tree_matrix MRM;
    private DoubleMatrix tree_distance_matrix_w1;

    /**
     * Getter of the DoubleMatrix tree distance matrix weight = 1
     *
     * @return DoubleMatrix tree distance matrix weight = 1
     */
    public DoubleMatrix getMatrix_w1() {
        return tree_distance_matrix_w1;
    }

    /**
     * Calculate the tree distance matrix with weight = 1
     *
     * @param MRM Maximum spanning tree matrix
     * @throws Exception Different exceptions can be throws here with the
     * generation of the distance matrix.
     */
    public Distance_matrix_w1(Maximum_spanning_tree_matrix MRM) throws Exception {
        this.MRM = MRM;
        if (has_calculate_matrix()) {
            load_calculate_matrix();
        } else {
            genera_correlation_matrix();
            save_calculate_matrix();
        }
    }

    private void genera_correlation_matrix() throws Exception {
        DoubleMatrix prim_matrix = MRM.getMaximum_spanning_tree_matrix();
        System.out.println("Generate matrix distance with weight=1");
        tree_distance_matrix_w1 = new DoubleMatrix(prim_matrix.columns, prim_matrix.columns);
        tree_distance_matrix_w1.fill(-1);
        long time = System.currentTimeMillis();
        for (int item = 0; item < prim_matrix.columns; item++) {
            if (item % 500 == 0) {
                System.out.println("Round Item:: " + item + " - " + (System.currentTimeMillis() - time));
                time = System.currentTimeMillis();
            }
            tree_distance_matrix_w1.putColumn(item, new DoubleMatrix(DIJKSTRA_w1(item)));
            //tree_distance_matrix_w1.putColumn(item, DIJKSTRA_w1(item));
        }
    }

    private double[] DIJKSTRA_w1(int s) throws Exception {
        DoubleMatrix prim_matrix = MRM.getMaximum_spanning_tree_matrix();
        double[] distancia = new double[prim_matrix.columns];
        boolean[] visto = new boolean[prim_matrix.columns];
        LinkedList<Integer> cola = new LinkedList<>();
        for (int item = 0; item < prim_matrix.columns; item++) {
            distancia[item] = Double.MAX_VALUE;
            visto[item] = false;
        }
        distancia[s] = 0;
        cola.addFirst(s);
        while (!cola.isEmpty()) {
            int u = cola.removeLast();
            visto[u] = true;
            double[] ColumItem = prim_matrix.getColumn(u).toArray();

            for (int v = 0; v < prim_matrix.rows; v++) {
                if (ColumItem[v] >= -1) {
                    if (!visto[v] && distancia[v] > (distancia[u] + 1)) {
                        distancia[v] = distancia[u] + 1;
                        cola.addFirst(v);

                    }
                }
            }
        }
        return distancia;
    }

    private void save_calculate_matrix() {
        System.out.println("Save calculate matrix distance with weight=1 ..");
        File f = new File("./data/" + MRM.getFile_name() + "/" + MRM.getSimilarity_measure_name());
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            tree_distance_matrix_w1.save("./data/" + MRM.getFile_name() + "/" + MRM.getSimilarity_measure_name() + "/distancia_w1_matrix.dat");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void load_calculate_matrix() {
        System.out.println("Load calculate matrix distance with weight=1 ..");
        try {
            if (tree_distance_matrix_w1 == null) {
                tree_distance_matrix_w1 = new DoubleMatrix();
                tree_distance_matrix_w1.load("./data/" + MRM.getFile_name() + "/" + MRM.getSimilarity_measure_name() + "/distancia_w1_matrix.dat");
            }
        } catch (Exception e) {
            System.out.println("Excepcion (Load calculate matrix distance with weight=1): " + e.getMessage());
        }
    }

    private boolean has_calculate_matrix() {
        File f = new File("./data/" + MRM.getFile_name() + "/" + MRM.getSimilarity_measure_name() + "/distancia_w1_matrix.dat");
        return f.exists();
    }
}
