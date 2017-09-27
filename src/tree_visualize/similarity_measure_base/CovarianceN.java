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

import org.apache.commons.math3.stat.correlation.Covariance;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class CovarianceN extends Similarity_measure_base {

    Covariance cc;

    /**
     * Create the Covariance correlation. It is a similarity measure base
     */
    public CovarianceN() {
        cc = new Covariance();
    }

    @Override
    public double calculate(double[] a1, double[] a2) {
        return cc.covariance(a1, a2);
    }

}
