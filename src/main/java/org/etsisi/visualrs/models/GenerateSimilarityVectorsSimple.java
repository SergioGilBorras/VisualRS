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

import org.etsisi.visualrs.matrices.DoubleMatrix;
import org.etsisi.visualrs.io.LoadData;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class GenerateSimilarityVectorsSimple {

    DoubleMatrix voteMatrix;

    int border = 0;
    double[] SP = null;
    double[] SN = null;

    /**
     * The constructor of the class to generate the similarity vectors
     *
     * @param limitSup The limit to difence good or bad vote in the Rank matrix
     * @param MV Matrix of votes (LoadData)
     */
    public GenerateSimilarityVectorsSimple(int limitSup, LoadData MV) {
        this.voteMatrix = MV.getRankMatrix();
        this.border = limitSup;
    }

    private void calcular() throws Exception {
        SN = new double[voteMatrix.rows];
        SP = new double[voteMatrix.rows];
        for (int i = 0; i < voteMatrix.rows; i++) {
            double[] col = voteMatrix.getRow(i).toArray();
            double N = 0;
            double P = 0;
            double U = 0;
            for (int j = 0; j < voteMatrix.columns; j++) {
                if (col[j] >= border) {
                    P++;
                    U++;
                } else if (col[j] > 0 && col[j] < border) {
                    N++;
                    U++;
                }
            }

            SP[i] = P / U;
            SN[i] = N / U;

        }
    }

    /**
     * Getter int High limit between positive and negative result.
     *
     * @return int The border value
     */
    public int getBorder() {
        return border;
    }

    /**
     * Getter double vector of similarity positive
     *
     * @return double vector of similarity positive
     * @throws Exception Different exceptions can be throws here with the
     * generation of the similarity vectors
     */
    public double[] SimilarityPositive() throws Exception {
        if (SP == null) {
            calcular();
        }

        return SP;
    }

    /**
     * Getter double vector of similarity negative
     *
     * @return double vector of similarity negative
     * @throws Exception Different exceptions can be throws here with the
     * generation of the similarity vectors
     */
    public double[] SimilarityNegative() throws Exception {
        if (SN == null) {
            calcular();
        }

        return SN;
    }

}
