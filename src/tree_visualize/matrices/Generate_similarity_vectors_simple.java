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

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class Generate_similarity_vectors_simple {

    DoubleMatrix vote_matrix;

    int border = 0;
    double[] SP = null;
    double[] SN = null;

    /**
     * The constructor of the class to generate the similarity vectors
     *
     * @param limit_sup The limit to difence good or bad vote in the Rank matrix
     * @param MV Matrix of votes (Rank_matrix)
     */
    public Generate_similarity_vectors_simple(int limit_sup, Rank_matrix MV) {
        this.vote_matrix = MV.getRank_matrix();
        this.border = limit_sup;
    }

    private void calcular() throws Exception {
        SN = new double[vote_matrix.rows];
        SP = new double[vote_matrix.rows];
        for (int i = 0; i < vote_matrix.rows; i++) {
            double[] col = vote_matrix.getRow(i).toArray();
            double N = 0;
            double P = 0;
            double U = 0;
            for (int j = 0; j < vote_matrix.columns; j++) {
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
     * Getter double vector of similarity positive
     *
     * @return double vector of similarity positive
     * @throws Exception Different exceptions can be throws here with the
     * generation of the similarity vectors
     */
    public double[] Similarity_positive() throws Exception {
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
    public double[] Similarity_negative() throws Exception {
        if (SN == null) {
            calcular();
        }

        return SN;
    }

}
