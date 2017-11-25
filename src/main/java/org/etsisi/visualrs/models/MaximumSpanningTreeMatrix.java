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
public final class MaximumSpanningTreeMatrix {

    private DoubleMatrix maximumSpanningTreeMatrix = null;
    private SimilarityMatrix MC;

    /**
     * Getter of the Maximum spanning tree matrix
     *
     * @return DoubleMatrix Maximum spanning tree matrix
     */
    public DoubleMatrix getMaximumSpanningTreeMatrix() {
        return maximumSpanningTreeMatrix;
    }

    /**
     * Getter of the File name to save the Maximum spanning tree matrix
     *
     * @return String File name
     */
    public String getFileName() {
        return MC.getFileName();
    }

    /**
     * Getter of the similarity measure name use to generate this matrix
     *
     * @return String similarity measure name use to generate this matrix
     */
    public String getSimilarityMeasureName() {
        return MC.getSimilarityMeasureName();
    }

    /**
     * Constructor of the class. It generates maximum spanning tree matrix from the similarity matrix.
     *
     * @param MC SimilarityMatrix The similarity matrix to use.
     * @throws Exception Different exceptions can be throws here with the
     * generation of the maximum spanning tree matrix.
     */
    public MaximumSpanningTreeMatrix(SimilarityMatrix MC) throws Exception {
        this.MC = MC;
        if (hasMaximumSpanningTreeMatrix()) {
            loadMaximumSpanningTreeMatrix();
        } else {
            prim();
            saveMaximumSpanningTreeMatrix();
        }
    }

    private int indexMaxVotos() {
        org.etsisi.visualrs.matrices.DoubleMatrix votosMatrix = MC.getRankMatrix();
        int indMaxVotos = Integer.MIN_VALUE;
        int maxVotos = Integer.MIN_VALUE;
        double[] ColumItem = votosMatrix.columnSums().toArray();
        for (int item = 0; item < votosMatrix.columns; item++) {
            if (ColumItem[item] > maxVotos) {
                ColumItem[item] = maxVotos;
                indMaxVotos = item;
            }
        }
        return indMaxVotos;
    }

    private double prim() throws Exception {
        DoubleMatrix correlationMatrix = MC.getSimilarityMatrix();
        maximumSpanningTreeMatrix = new DoubleMatrix(correlationMatrix.columns, correlationMatrix.columns);
        maximumSpanningTreeMatrix.fill(-Double.MAX_VALUE);

        double MST = 0;

        double newMAX;
        int ColMIN = -1;
        int RowMIN = -1;

        double MAX = -Double.MAX_VALUE;
        ArrayList<Integer> visitados = new ArrayList();
        ArrayList<Integer> novisitados = new ArrayList();

        int nMediaCIndex = 0;//indexMaxVotos();
        for (int a = 0; a < correlationMatrix.columns; a++) {
            novisitados.add(a);
        }

        novisitados.remove(nMediaCIndex);
        System.out.println("NODO Ini Prim: " + nMediaCIndex);

        boolean[] avisitados = new boolean[correlationMatrix.columns];
        Arrays.fill(avisitados, false);
        avisitados[nMediaCIndex] = true;
        int nvisitados = 1;
        visitados.add(nMediaCIndex);

        while (nvisitados < correlationMatrix.columns) {

            //Collections.shuffle(visitados);
            for (Integer v : visitados) {
                //Collections.shuffle(novisitados);
                for (Integer a : novisitados) {
                    if (!Objects.equals(a, v) && !avisitados[a]) {
                        newMAX = correlationMatrix.get(a, v);

                        if (Double.isNaN(newMAX)) {
                            System.err.println("isNaN::" + a + "::" + v + " - " + MAX + "::" + newMAX);
                        }

                        if (MAX < newMAX) {
                            RowMIN = a;
                            ColMIN = v;
                            MAX = newMAX;
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
            maximumSpanningTreeMatrix.put(RowMIN, ColMIN, MAX);
            maximumSpanningTreeMatrix.put(ColMIN, RowMIN, MAX);
            MAX = -Double.MAX_VALUE;
            nvisitados++;

        }
        System.err.println("NO VISITED::" + novisitados);
        System.out.println("1-SpanningTree.. Total Weight::" + MST);
        System.out.println("1-SpanningTree.. AVG Weight::" + (MST / maximumSpanningTreeMatrix.rows));
        return MST;
    }

    private void saveMaximumSpanningTreeMatrix() {
        System.out.println("save the Maximum spanning tree matrix..");
        File f = new File("./data/" + MC.getFileName() + "/" + MC.getSimilarityMeasureName());
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            maximumSpanningTreeMatrix.save("./data/" + MC.getFileName() + "/" + MC.getSimilarityMeasureName() + "/MRMMatrix.dat");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void loadMaximumSpanningTreeMatrix() {
        System.out.println("load the Maximum spanning tree matrix..");
        try {
            if (maximumSpanningTreeMatrix == null) {
                maximumSpanningTreeMatrix = new DoubleMatrix();
                maximumSpanningTreeMatrix.load("./data/" + MC.getFileName() + "/" + MC.getSimilarityMeasureName() + "/MRMMatrix.dat");
            }
        } catch (Exception e) {
            System.out.println("Excepcion (loadMaximumSpanningTreeMatrix): " + e.getMessage());
        }
    }

    private boolean hasMaximumSpanningTreeMatrix() {
        File f = new File("./data/" + MC.getFileName() + "/" + MC.getSimilarityMeasureName() + "/MRMMatrix.dat");
        return f.exists();
    }

}
