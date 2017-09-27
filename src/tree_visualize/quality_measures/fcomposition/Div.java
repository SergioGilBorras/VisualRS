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
package tree_visualize.quality_measures.fcomposition;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class Div extends Funtion_composition{

    /**
     * It calculates the division between two arrays for each element.
     */
    public Div() {
        name = "Div";
    }

    @Override
    public double[] calculate(double[] array1, double[] array2) {
        double[] res = new double[array1.length];
        for (int i = 0; i < array1.length; i++) {
            if (array2[i] > 0) {
                res[i] = array1[i] / array2[i];
            } else {
                res[i] = 0;
            }
        }
        return res;
    }

}
