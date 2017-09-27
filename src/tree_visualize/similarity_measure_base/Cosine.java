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
public class Cosine extends Similarity_measure_base {

    @Override
    public double calculate(double[] a1, double[] a2) {
        if (a1 == null || a2 == null || a1.length != a2.length) {
            throw new IllegalArgumentException("Uno de los parametros es null. O no tienen la misma longitud");
        }

        double sup = 0;
        double inf1 = 0;
        double inf2 = 0;

        for (int i = 0; i < a1.length; i++) {
            sup += (a1[i] * a2[i]);
            inf1 += (a1[i] * a1[i]);
            inf2 += (a2[i] * a2[i]);
        }
        return sup / Math.sqrt(inf1 * inf2);
    }

}
