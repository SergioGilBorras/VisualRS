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
public class MSD extends Similarity_measure_base {

    @Override
    public double calculate(double[] a1, double[] a2) {
        if (a1 == null || a2 == null || a1.length != a2.length) {
            throw new IllegalArgumentException("Uno de los parametros es null. O no tienen la misma longitud");
        }
        double res = 0;
        double[] aux = new double[a1.length];
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < a1.length; i++) {
            if (min > a1[i]) {
                min = a1[i];
            }
            if (min > a2[i]) {
                min = a2[i];
            }
            if (max < a1[i]) {
                max = a1[i];
            }
            if (max < a2[i]) {
                max = a2[i];
            }
            aux[i] = a1[i] - a2[i];
        }
        max = max - min;
        if (max != 0) {
            for (int i = 0; i < a1.length; i++) {
                double a = (aux[i] / max);
                res += (a * a);
            }

            return 1 - res / a1.length;
        } else {
            return 1;
        }
    }

}
