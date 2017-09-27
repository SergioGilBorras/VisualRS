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
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class Distance_matrix_wq {

    Maximum_spanning_tree_matrix MRM;
    DoubleMatrix tree_distance_matrix_wq;

    /**
     * Getter of the DoubleMatrix tree distance matrix weight = q
     *
     * @return DoubleMatrix tree distance matrix weight = q
     */
    public DoubleMatrix getMatrix_wq() {
        return tree_distance_matrix_wq;
    }

    /**
     * Calculate the tree distance matrix with weight = q
     *
     * @param MRM Maximum spanning tree matrix
     * @throws Exception Different exceptions can be throws here with the
     * generation of the distance matrix.
     */
    public Distance_matrix_wq(Maximum_spanning_tree_matrix MRM) throws Exception {
        this.MRM = MRM;
        if (has_matrizes_calculadas()) {
            load_matrizes_calculadas();
        } else {
            genera_correlation_matrix();
            save_matrizes_calculadas();
        }
    }

    private void genera_correlation_matrix() throws Exception {
        DoubleMatrix prim_matrix = MRM.getMaximum_spanning_tree_matrix();
        System.out.println("generate_distace_matrix_wq");
        tree_distance_matrix_wq = new DoubleMatrix(prim_matrix.columns, prim_matrix.columns);
        tree_distance_matrix_wq.fill(-1);
        long time = System.currentTimeMillis();
        for (int item = 0; item < prim_matrix.columns; item++) {
            if (item % 500 == 0) {
                System.out.println("Vuelta Item:: " + item + " - " + (System.currentTimeMillis() - time));
                time = System.currentTimeMillis();
            }
            tree_distance_matrix_wq.putColumn(item, new DoubleMatrix(DIJKSTRA_wq(item)));
            //tree_distance_matrix_wq.putColumn(item, DIJKSTRA_wq(item));
        }
    }

    private double[] DIJKSTRA_wq(int s) throws Exception {
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
                    if (!visto[v] && distancia[v] > (distancia[u] + ColumItem[v])) {
                        distancia[v] = distancia[u] + ColumItem[v];
                        cola.addFirst(v);

                    }
                }
            }
        }
        return distancia;
    }

    private void save_matrizes_calculadas() {
        System.out.println("save_matrizes_distancia_wq_calculadas..");
        File f = new File("./data/" + MRM.getFile_name() + "/" + MRM.getSimilarity_measure_name());
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            tree_distance_matrix_wq.save("./data/" + MRM.getFile_name() + "/" + MRM.getSimilarity_measure_name() + "/distancia_wq_matrix.dat");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void load_matrizes_calculadas() {
        System.out.println("load_matrizes_distancia_wq_calculadas..");
        try {
            if (tree_distance_matrix_wq == null) {
                tree_distance_matrix_wq = new DoubleMatrix();
                tree_distance_matrix_wq.load("./data/" + MRM.getFile_name() + "/" + MRM.getSimilarity_measure_name() + "/distancia_wq_matrix.dat");
            }
        } catch (Exception e) {
            System.out.println("Excepcion (load_matrizes_distancia_wq_calculadas): " + e.getMessage());
        }
    }

    private boolean has_matrizes_calculadas() {
        File f = new File("./data/" + MRM.getFile_name() + "/" + MRM.getSimilarity_measure_name() + "/distancia_wq_matrix.dat");
        return f.exists();
    }
}
