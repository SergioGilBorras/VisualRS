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
package tree_visualize.similarity_measure_final;

import tree_visualize.similarity_measure_base.Jaccard;
import tree_visualize.similarity_measure_base.MSD;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class FJMSD extends Similarity_measure_final {

    MSD pc;
    Jaccard jc;

    /**
     * Create the JMSD correlation, combination of Jaccard correlation and MSD
     * (Mean Squared Difference). It is a similarity measure final
     */
    public FJMSD() {
        this.name = "JMSD Correlation";
        pc = new MSD();
        jc = new Jaccard();
    }

    @Override
    public double calculate(double[] a1, double[] a2) {
        double val = -99, jac = 0;
        double[][] Vcomum = this.filter_comun_vote(a1, a2);
        if (Vcomum[0].length > min_comun_users) {
            jac = jc.calculate(a1, a2);
            val = jac * pc.calculate(Vcomum[0], Vcomum[1]);
            if (Double.isNaN(val)) {
                val = jac;
            }
        }
        return val;
    }

}
