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
package org.etsisi.visualrs.qualityMeasures.fComposition;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public abstract class FuntionComposition {

    /**
     * Name of the composition function
     */
    protected String name;

    /**
     * Getter of the composition function name
     *
     * @return String with the name
     */
    public String getName() {
        return name;
    }

    /**
     * It calculates the composition function.
     *
     * @param array1 Array1 of double to apply the composition fuction to
     * combine
     * @param array2 Array2 of double to apply the composition fuction to
     * combine
     * @return Array of double with the result by node
     */
    public abstract double[] calculate(double[] array1, double[] array2);

}
