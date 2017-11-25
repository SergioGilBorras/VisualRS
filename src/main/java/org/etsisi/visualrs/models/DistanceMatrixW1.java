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
 * This class calculates the tree distance matrix with weight = 1
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class DistanceMatrixW1 {

    private MaximumSpanningTreeMatrix MRM;
    private DoubleMatrix treeDistanceMatrixW1;

    /**
     * Getter of the DoubleMatrix tree distance matrix weight = 1
     *
     * @return DoubleMatrix tree distance matrix weight = 1
     */
    public DoubleMatrix getMatrixW1() {
        return treeDistanceMatrixW1;
    }

    /**
     * Calculate the tree distance matrix with weight = 1
     *
     * @param MRM Maximum spanning tree matrix
     * @throws Exception Different exceptions can be throws here with the
     * generation of the distance matrix.
     */
    public DistanceMatrixW1(MaximumSpanningTreeMatrix MRM) throws Exception {
        this.MRM = MRM;
        if (hasCalculateMatrix()) {
            loadCalculateMatrix();
        } else {
            generaCorrelationMatrix();
            saveCalculateMatrix();
        }
    }

    private void generaCorrelationMatrix() throws Exception {
        DoubleMatrix primMatrix = MRM.getMaximumSpanningTreeMatrix();
        System.out.println("Generate matrix distance with weight=1");
        treeDistanceMatrixW1 = new DoubleMatrix(primMatrix.columns, primMatrix.columns);
        treeDistanceMatrixW1.fill(-1);
        long time = System.currentTimeMillis();
        for (int item = 0; item < primMatrix.columns; item++) {
            if (item % 500 == 0) {
                System.out.println("Round Item:: " + item + " - " + (System.currentTimeMillis() - time));
                time = System.currentTimeMillis();
            }
            treeDistanceMatrixW1.putColumn(item, new DoubleMatrix(DIJKSTRAW1(item)));
            //treeDistanceMatrixW1.putColumn(item, DIJKSTRAW1(item));
        }
    }

    private double[] DIJKSTRAW1(int s) throws Exception {
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
                    if (!visto[v] && distancia[v] > (distancia[u] + 1)) {
                        distancia[v] = distancia[u] + 1;
                        cola.addFirst(v);

                    }
                }
            }
        }
        return distancia;
    }

    private void saveCalculateMatrix() {
        System.out.println("Save calculate matrix distance with weight=1 ..");
        File f = new File("./data/" + MRM.getFileName() + "/" + MRM.getSimilarityMeasureName());
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            treeDistanceMatrixW1.save("./data/" + MRM.getFileName() + "/" + MRM.getSimilarityMeasureName() + "/distanciaW1Matrix.dat");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void loadCalculateMatrix() {
        System.out.println("Load calculate matrix distance with weight=1 ..");
        try {
            if (treeDistanceMatrixW1 == null) {
                treeDistanceMatrixW1 = new DoubleMatrix();
                treeDistanceMatrixW1.load("./data/" + MRM.getFileName() + "/" + MRM.getSimilarityMeasureName() + "/distanciaW1Matrix.dat");
            }
        } catch (Exception e) {
            System.out.println("Excepcion (Load calculate matrix distance with weight=1): " + e.getMessage());
        }
    }

    private boolean hasCalculateMatrix() {
        File f = new File("./data/" + MRM.getFileName() + "/" + MRM.getSimilarityMeasureName() + "/distanciaW1Matrix.dat");
        return f.exists();
    }
}
