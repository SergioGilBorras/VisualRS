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
package org.etsisi.visualrs.models;

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
public class DistanceMatrixWq {

    MaximumSpanningTreeMatrix MRM;
    DoubleMatrix treeDistanceMatrixWq;

    /**
     * Getter of the DoubleMatrix tree distance matrix weight = q
     *
     * @return DoubleMatrix tree distance matrix weight = q
     */
    public DoubleMatrix getMatrixWq() {
        return treeDistanceMatrixWq;
    }

    /**
     * Calculate the tree distance matrix with weight = q
     *
     * @param MRM Maximum spanning tree matrix
     * @throws Exception Different exceptions can be throws here with the
     * generation of the distance matrix.
     */
    public DistanceMatrixWq(MaximumSpanningTreeMatrix MRM) throws Exception {
        this.MRM = MRM;
        if (hasMatrizesCalculadas()) {
            loadMatrizesCalculadas();
        } else {
            generaCorrelationMatrix();
            saveMatrizesCalculadas();
        }
    }

    private void generaCorrelationMatrix() throws Exception {
        DoubleMatrix primMatrix = MRM.getMaximumSpanningTreeMatrix();
        System.out.println("\nGenerating distance matrix with weight = q...");
        treeDistanceMatrixWq = new DoubleMatrix(primMatrix.columns, primMatrix.columns);
        treeDistanceMatrixWq.fill(-1);

        long oldProgress = 100;
        for (int item = 0; item < primMatrix.columns; item++) {
            double progress = ((item * 100) / primMatrix.columns);
            if (Math.round(progress) % 25 == 0 && Math.round(progress) != oldProgress && Math.round(progress) > 0) {
                System.out.print(" " + Math.round(progress) + "% ");
                oldProgress = Math.round(progress);
            } else if (Math.round(progress) % 5 == 0 && Math.round(progress) != oldProgress) {
                System.out.print(".");
                oldProgress = Math.round(progress);
            }
            treeDistanceMatrixWq.putColumn(item, new DoubleMatrix(DIJKSTRAWq(item)));
        }
        System.out.println(" 100%");
    }

    private double[] DIJKSTRAWq(int s) throws Exception {
        DoubleMatrix primMatrix = MRM.getMaximumSpanningTreeMatrix();
        double[] distancia = new double[primMatrix.columns];
        boolean[] visto = new boolean[primMatrix.columns];
        LinkedList<Integer> cola = new LinkedList<>();
        for (int item = 0; item < primMatrix.columns; item++) {
            distancia[item] = Double.MAX_VALUE;
            visto[item] = false;
        }
        distancia[s] = 0;
        cola.addFirst(s);
        while (!cola.isEmpty()) {
            int u = cola.removeLast();
            visto[u] = true;
            double[] ColumItem = primMatrix.getColumn(u).toArray();

            for (int v = 0; v < primMatrix.rows; v++) {
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

    private void saveMatrizesCalculadas() {
        System.out.print("\nSaving calculated distance matrix with weight = q...");
        File f = new File("./data/" + MRM.getFileName() + "/" + MRM.getSimilarityMeasureName());
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            treeDistanceMatrixWq.save("./data/" + MRM.getFileName() + "/" + MRM.getSimilarityMeasureName() + "/distanciaWqMatrix.dat");
            System.out.println(" Done.");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void loadMatrizesCalculadas() {
        System.out.print("\nLoading calculated distance matrix with weight = q...");
        try {
            if (treeDistanceMatrixWq == null) {
                treeDistanceMatrixWq = new DoubleMatrix();
                treeDistanceMatrixWq.load("./data/" + MRM.getFileName() + "/" + MRM.getSimilarityMeasureName() + "/distanciaWqMatrix.dat");
            }
            System.out.println(" Done.");
        } catch (Exception e) {
            System.out.println("Exception loading matrices: " + e.getMessage());
        }
    }

    private boolean hasMatrizesCalculadas() {
        File f = new File("./data/" + MRM.getFileName() + "/" + MRM.getSimilarityMeasureName() + "/distanciaWqMatrix.dat");
        return f.exists();
    }
}
