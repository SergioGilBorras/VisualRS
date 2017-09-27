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
package tree_visualize.quality_measures.quality_measures_by_node;

import tree_visualize.matrices.DoubleMatrix;
import tree_visualize.matrices.Rank_matrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class Sum_votes extends Quality_measure_by_node {

    DoubleMatrix matriz_dispersa;

    /**
     * Calculate the sum of votes by node
     *
     * @param MV Rank_matrix Matrix of votes to use.
     */
    public Sum_votes(Rank_matrix MV) {
        super(null);//MV.getRank_matrix());
        matriz_dispersa = MV.getRank_matrix();
        name = "Suma_votos";
    }

    @Override
    public double[] calculate() throws Exception {
        if (result_calculate == null) {

            double[] sumVotos = new double[matriz_dispersa.columns];

            double usumVotos = 0;

            for (int item = 0; item < matriz_dispersa.columns; item++) {

                double[] ColumItem_rank = matriz_dispersa.getColumn(item).toArray();

                for (int item1 = 0; item1 < matriz_dispersa.rows; item1++) {
                    if (ColumItem_rank[item1] != -1) {
                        usumVotos += ColumItem_rank[item1];
                    }
                }

                sumVotos[item] = usumVotos;
                usumVotos = 0;

            }
            result_calculate = sumVotos;
        }
        return result_calculate;
    }

}
