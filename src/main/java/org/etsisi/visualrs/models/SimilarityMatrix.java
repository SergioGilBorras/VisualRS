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
import org.etsisi.visualrs.io.LoadData;
import org.etsisi.visualrs.similarityMeasureFinal.SimilarityMeasureFinal;
import org.jblas.DoubleMatrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public final class SimilarityMatrix {

    private DoubleMatrix similarityMatrix = null;
    private LoadData MV;
    private SimilarityMeasureFinal FC;

    /**
     * Getter of the rank matrix.
     *
     * @return sparse DoubleMatrix with the votes.
     */
    public org.etsisi.visualrs.matrices.DoubleMatrix getRankMatrix() {
        return MV.getRankMatrix();
    }

    /**
     * Getter of the similarity matrix.
     *
     * @return DoubleMatrix with the similarity values.
     */
    public DoubleMatrix getSimilarityMatrix() {
        return similarityMatrix;
    }

    /**
     * Getter of the File name to save the similarity matrix
     *
     * @return String File name
     */
    public String getFileName() {
        return MV.getFileName();
    }

    /**
     * Getter of the similarity measure name.
     *
     * @return String the similarity measure name.
     */
    public String getSimilarityMeasureName() {
        return FC.getName();
    }

    /**
     * Constructor of the class. It generates similarity matrix from the rank
     * matrix and a similarity measure.
     *
     * @param FC SimilarityMeasureFinal The similarity measure final to
 generate the similarity matrix.
     * @param MV LoadData The rank matrix to generate the similarity matrix.
     * @throws Exception Different exceptions can be throws here with the
     * generation of the similarity matrix.
     */
    public SimilarityMatrix(SimilarityMeasureFinal FC, LoadData MV) throws Exception {
        this.FC = FC;
        this.MV = MV;
        if (hasMatrizesCalculadas()) {
            loadSimilarityMatrix();
        } else {
            generateSimilarityMatrix();
            deleteOrphanNodes();
            saveSimilarityMatrix();
        }
    }

    private void deleteOrphanNodes() throws Exception {
        org.etsisi.visualrs.matrices.DoubleMatrix votosMatrix = MV.getRankMatrix();
        int itemOk = 0;
        ArrayList<Integer> columnsToRemove = new ArrayList();
        for (int i = 0; i < similarityMatrix.columns; i++) {
            double[] ColumItem = similarityMatrix.getColumn(i).toArray();
            int cont = 0;
            for (int j = 0; j < ColumItem.length; j++) {
                if (ColumItem[j] != -99) {
                    cont++;
                }
            }
            if (cont > 0) {
                itemOk++;
            } else {
                columnsToRemove.add(i);
            }
        }
        if (columnsToRemove.size() > 0) {
            System.out.println("New matrix:: " + itemOk + " x " + itemOk);
            DoubleMatrix correlationMatrixAux = new DoubleMatrix(itemOk, itemOk);
            org.etsisi.visualrs.matrices.DoubleMatrix votosMatrixAux = new org.etsisi.visualrs.matrices.DoubleMatrix(votosMatrix.rows, itemOk);
            votosMatrixAux.fill(votosMatrix.getFill());
            double[] VColum = new double[itemOk];
            int indexColum = 0;
            for (int i = 0; i < similarityMatrix.columns; i++) {
                double[] ColumItem = similarityMatrix.getColumn(i).toArray();;
                if (!columnsToRemove.contains(i)) {
                    int indexRow = 0;
                    for (int j = 0; j < ColumItem.length; j++) {
                        if (!columnsToRemove.contains((Integer) j)) {
                            VColum[indexRow] = ColumItem[j];
                            indexRow++;
                        }
                    }

                    correlationMatrixAux.putColumn(indexColum, new DoubleMatrix(VColum));
                    votosMatrixAux.putColumn(indexColum, votosMatrix.getColumn(i).toArray());
                    indexColum++;
                }
            }

            MV.setRankMatrix(votosMatrixAux);
            MV.saveRankMatrix(FC.getName());
            similarityMatrix = correlationMatrixAux;
        }
    }

    private void generateSimilarityMatrix() throws Exception {
        org.etsisi.visualrs.matrices.DoubleMatrix votosMatrix = MV.getRankMatrix();
        similarityMatrix = new DoubleMatrix(votosMatrix.columns, votosMatrix.columns);
        similarityMatrix.fill(-99);

        long tIni = System.currentTimeMillis();

        for (int item = 0; item < votosMatrix.columns; item++) {

            if (item % 100 == 0) {
                System.out.println("SIM load:: " + item + " :: " + (System.currentTimeMillis() - tIni));
                tIni = System.currentTimeMillis();
            }

            double[] ColumItem = votosMatrix.getColumn(item).toArray();

            for (int item1 = item + 1; item1 < votosMatrix.columns; item1++) {

                double[] ColumItem1 = votosMatrix.getColumn(item1).toArray();

                double val = FC.calculate(ColumItem, ColumItem1);

                similarityMatrix.put(item, item1, val);
                similarityMatrix.put(item1, item, val);
            }

        }
    }

    private void saveSimilarityMatrix() {
        System.out.println("save similarity matrix..");
        File f = new File("./data/" + MV.getFileName() + "/" + FC.getName());
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            similarityMatrix.save("./data/" + MV.getFileName() + "/" + FC.getName() + "/correlationMatrix.dat");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void loadSimilarityMatrix() {
        System.out.println("load similarity matrix..");
        try {
            if (similarityMatrix == null) {
                similarityMatrix = new DoubleMatrix();
                similarityMatrix.load("./data/" + MV.getFileName() + "/" + FC.getName() + "/correlationMatrix.dat");
            }
        } catch (Exception e) {
            System.out.println("Excepcion (loadSimilarityMatrix): " + e.getMessage());
        }
    }

    private boolean hasMatrizesCalculadas() {
        File f = new File("./data/" + MV.getFileName() + "/" + FC.getName() + "/correlationMatrix.dat");
        return f.exists();
    }

}
