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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import org.jblas.DoubleMatrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public final class Maximum_spanning_tree_matrix {

    private DoubleMatrix Maximum_spanning_tree_matrix = null;
    private Similarity_matrix MC;

    /**
     * Getter of the Maximum spanning tree matrix
     *
     * @return DoubleMatrix Maximum spanning tree matrix
     */
    public DoubleMatrix getMaximum_spanning_tree_matrix() {
        return Maximum_spanning_tree_matrix;
    }

    /**
     * Getter of the File name to save the Maximum spanning tree matrix
     *
     * @return String File name
     */
    public String getFile_name() {
        return MC.getFile_name();
    }

    /**
     * Getter of the similarity measure name use to generate this matrix
     *
     * @return String similarity measure name use to generate this matrix
     */
    public String getSimilarity_measure_name() {
        return MC.getSimilarity_measure_name();
    }

    /**
     * Constructor of the class. It generates maximum spanning tree matrix from the similarity matrix.
     *
     * @param MC Similarity_matrix The similarity matrix to use.
     * @throws Exception Different exceptions can be throws here with the
     * generation of the maximum spanning tree matrix.
     */
    public Maximum_spanning_tree_matrix(Similarity_matrix MC) throws Exception {
        this.MC = MC;
        if (has_maximum_spanning_tree_matrix()) {
            load_maximum_spanning_tree_matrix();
        } else {
            prim();
            save_maximum_spanning_tree_matrix();
        }
    }

    private int index_max_votos() {
        tree_visualize.matrices.DoubleMatrix votos_matrix = MC.getRank_matrix();
        int ind_max_votos = Integer.MIN_VALUE;
        int max_votos = Integer.MIN_VALUE;
        double[] ColumItem = votos_matrix.columnSums().toArray();
        for (int item = 0; item < votos_matrix.columns; item++) {
            if (ColumItem[item] > max_votos) {
                ColumItem[item] = max_votos;
                ind_max_votos = item;
            }
        }
        return ind_max_votos;
    }

    private double prim() throws Exception {
        DoubleMatrix correlation_matrix = MC.getSimilarity_matrix();
        Maximum_spanning_tree_matrix = new DoubleMatrix(correlation_matrix.columns, correlation_matrix.columns);
        Maximum_spanning_tree_matrix.fill(-Double.MAX_VALUE);

        double MST = 0;

        double new_MAX;
        int ColMIN = -1;
        int RowMIN = -1;

        double MAX = -Double.MAX_VALUE;
        ArrayList<Integer> visitados = new ArrayList();
        ArrayList<Integer> novisitados = new ArrayList();

        int n_mediaC_index = 0;//index_max_votos();
        for (int a = 0; a < correlation_matrix.columns; a++) {
            novisitados.add(a);
        }

        novisitados.remove(n_mediaC_index);
        System.out.println("NODO Ini Prim: " + n_mediaC_index);

        boolean[] avisitados = new boolean[correlation_matrix.columns];
        Arrays.fill(avisitados, false);
        avisitados[n_mediaC_index] = true;
        int nvisitados = 1;
        visitados.add(n_mediaC_index);

        while (nvisitados < correlation_matrix.columns) {

            //Collections.shuffle(visitados);
            for (Integer v : visitados) {
                //Collections.shuffle(novisitados);
                for (Integer a : novisitados) {
                    if (!Objects.equals(a, v) && !avisitados[a]) {
                        new_MAX = correlation_matrix.get(a, v);

                        if (Double.isNaN(new_MAX)) {
                            System.err.println("isNaN::" + a + "::" + v + " - " + MAX + "::" + new_MAX);
                        }

                        if (MAX < new_MAX) {
                            RowMIN = a;
                            ColMIN = v;
                            MAX = new_MAX;
                        }

                    }
                }
            }
            MST += MAX;
            if (nvisitados % 500 == 0) {
                System.err.println("visited.size::" + nvisitados + " _ " + novisitados.size());
            }
            if (avisitados[RowMIN]) {
                System.err.println("visited.size::" + nvisitados + " _ " + novisitados.size());
                System.err.println("DUPLICATE::" + RowMIN + "::" + ColMIN + " - " + MAX + "::" + (-Double.MAX_VALUE));
                System.err.println("NO VISITED::" + novisitados);
            }

            visitados.add(RowMIN);
            int as = novisitados.remove(novisitados.indexOf(RowMIN));
            if (as != RowMIN) {
                System.err.println("ERROR :: novisitados.remove");
            }
            avisitados[RowMIN] = true;
            Maximum_spanning_tree_matrix.put(RowMIN, ColMIN, MAX);
            Maximum_spanning_tree_matrix.put(ColMIN, RowMIN, MAX);
            MAX = -Double.MAX_VALUE;
            nvisitados++;

        }
        System.err.println("NO VISITED::" + novisitados);
        System.out.println("1-SpanningTree.. Total_Weight::" + MST);
        System.out.println("1-SpanningTree.. AVG_Weight::" + (MST / Maximum_spanning_tree_matrix.rows));
        return MST;
    }

    private void save_maximum_spanning_tree_matrix() {
        System.out.println("save the Maximum spanning tree matrix..");
        File f = new File("./data/" + MC.getFile_name() + "/" + MC.getSimilarity_measure_name());
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            Maximum_spanning_tree_matrix.save("./data/" + MC.getFile_name() + "/" + MC.getSimilarity_measure_name() + "/MRM_matrix.dat");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void load_maximum_spanning_tree_matrix() {
        System.out.println("load the Maximum spanning tree matrix..");
        try {
            if (Maximum_spanning_tree_matrix == null) {
                Maximum_spanning_tree_matrix = new DoubleMatrix();
                Maximum_spanning_tree_matrix.load("./data/" + MC.getFile_name() + "/" + MC.getSimilarity_measure_name() + "/MRM_matrix.dat");
            }
        } catch (Exception e) {
            System.out.println("Excepcion (load_matrizes_MRM_calculadas): " + e.getMessage());
        }
    }

    private boolean has_maximum_spanning_tree_matrix() {
        File f = new File("./data/" + MC.getFile_name() + "/" + MC.getSimilarity_measure_name() + "/MRM_matrix.dat");
        return f.exists();
    }

}
