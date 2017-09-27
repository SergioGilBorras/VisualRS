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
package tree_visualize.similarity_measure_base;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class SM extends Similarity_measure_base {

    int border = 0;
    double[] SP = null;
    double[] SN = null;

    /**
     * Create SM a similarity measure base
     *
     * @param border limit between positive and negative.
     * @param SP_u Array of double with the positive similarity vectors.
     * @param SN_u Array of double with the negative similarity vectors.
     */
    public SM(int border, double[] SP_u, double[] SN_u) {
        this.border = border;
        this.SP = SP_u;
        this.SN = SN_u;
    }

    @Override
    public double calculate(double[] a1, double[] a2) {
        if (a1 == null || a2 == null || a1.length != a2.length) {
            throw new IllegalArgumentException("Uno de los parametros es null. O no tienen la misma longitud");
        }
        double n_index_A = 0;
        double n_index_B = 0;
        double n_index_C = 0;
        double sum_index_A = 0;
        double sum_index_B = 0;
        double sum_index_C = 0;

        for (int i = 0; i < a1.length; i++) {
            //System.err.println(a1[i] + " -- " + a2[i]);
            if (a1[i] >= border && a2[i] >= border) {
                n_index_A++;
                sum_index_A += (1 - Math.pow(a1[i] - a2[i], 2)) * (SP[i] * SP[i]);
            } else if (a1[i] > 0 && a1[i] < border && a2[i] > 0 && a2[i] < border) {
                n_index_B++;
                sum_index_B += (1 - Math.pow(a1[i] - a2[i], 2)) * (SN[i] * SN[i]);
            } else if (a1[i] > 0 && a1[i] < border && a2[i] >= border) {
                n_index_C++;
                sum_index_C += (1 - Math.pow(a1[i] - a2[i], 2)) * (SP[i] * SN[i]);
            } else if (a1[i] >= border && a2[i] > 0 && a2[i] < border) {
                n_index_C++;
                sum_index_C += (1 - Math.pow(a1[i] - a2[i], 2)) * (SP[i] * SN[i]);
            }

        }
        double div = 0;
        double A = 0;
        if (n_index_A != 0) {
            A = sum_index_A / n_index_A;
            div++;
        }
        double B = 0;
        if (n_index_B != 0) {
            B = sum_index_B / n_index_B;
            div++;
        }
        double C = 0;
        if (n_index_C != 0) {
            C = sum_index_C / n_index_C;
            div++;
        }
        
        return (1 / div) * (A + B + C);

    }

}
