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
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.NumberOuts;
import org.jblas.DoubleMatrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class DistanceMatrixWOuts {

    private final MaximumSpanningTreeMatrix MRM;
    private DoubleMatrix treeDistanceMatrixWouts;
    private final NumberOuts nouts;

    /**
     * Getter of the DoubleMatrix tree distance matrix wouts
     *
     * @return tree distance matrix with weight = number of branch of each node.
     */
    public DoubleMatrix getMatrixWouts() {
        return treeDistanceMatrixWouts;
    }

    /**
     * Calculate the tree distance matrix with weight =  number of branch of each node.
     *
     * @param MRM Maximum spanning tree matrix
     * @param nouts number of branch of each node. Quality measure.
     * @throws Exception Different exceptions can be throws here with the
     * generation of the distance matrix
     */
    public DistanceMatrixWOuts(MaximumSpanningTreeMatrix MRM, NumberOuts nouts) throws Exception {
        this.MRM = MRM;
        this.nouts = nouts;
        if (hasCalculateMatrix()) {
            loadCalculateMatrix();
        } else {
            generaCorrelationMatrix();
            saveCalculateMatrix();
        }
    }

    private void generaCorrelationMatrix() throws Exception {
        DoubleMatrix primMatrix = MRM.getMaximumSpanningTreeMatrix();
        System.out.println("Generate distace matrix wouts");
        treeDistanceMatrixWouts = new DoubleMatrix(primMatrix.columns, primMatrix.columns);
        treeDistanceMatrixWouts.fill(-1);
        long time = System.currentTimeMillis();
        for (int item = 0; item < primMatrix.columns; item++) {
            if (item % 500 == 0) {
                System.out.println("Round Item:: " + item + " - " + (System.currentTimeMillis() - time));
                time = System.currentTimeMillis();
            }
            treeDistanceMatrixWouts.putColumn(item, new DoubleMatrix(DIJKSTRAWouts(item)));
            //treeDistanceMatrixWouts.putColumn(item, DIJKSTRAWouts(item));
        }
    }

    private double[] DIJKSTRAWouts(int s) throws Exception {
        DoubleMatrix primMatrix = MRM.getMaximumSpanningTreeMatrix();
        double[] out = nouts.calculate();

        double[] distancia = new double[primMatrix.columns];
        boolean[] visto = new boolean[primMatrix.columns];
        LinkedList<Integer> cola = new LinkedList<>();
        for (int item = 0; item < primMatrix.columns; item++) {
            distancia[item] = Double.MAX_VALUE;
            visto[item] = false;
        }
        distancia[s] = out[s];
        cola.addFirst(s);
        while (!cola.isEmpty()) {
            int u = cola.removeLast();
            visto[u] = true;
            double[] ColumItem = primMatrix.getColumn(u).toArray();

            for (int v = 0; v < primMatrix.rows; v++) {
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

    private void saveCalculateMatrix() {
        System.out.println("Save calculate matrix distance with weight=nouts ..");
        File f = new File("./data/" + MRM.getFileName() + "/" + MRM.getSimilarityMeasureName());
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            treeDistanceMatrixWouts.save("./data/" + MRM.getFileName() + "/" + MRM.getSimilarityMeasureName() + "/distanciaWoutsMatrix.dat");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void loadCalculateMatrix() {
        System.out.println("Load calculate matrix distance with weight=nout ..");
        try {
            if (treeDistanceMatrixWouts == null) {
                treeDistanceMatrixWouts = new DoubleMatrix();
                treeDistanceMatrixWouts.load("./data/" + MRM.getFileName() + "/" + MRM.getSimilarityMeasureName() + "/distanciaWoutsMatrix.dat");
            }
        } catch (Exception e) {
            System.out.println("Excepcion (Load calculate matrix distance with weight=nout): " + e.getMessage());
        }
    }

    private boolean hasCalculateMatrix() {
        File f = new File("./data/" + MRM.getFileName() + "/" + MRM.getSimilarityMeasureName() + "/distanciaWoutsMatrix.dat");
        return f.exists();
    }
}