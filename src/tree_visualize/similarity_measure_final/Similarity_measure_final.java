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

import java.util.ArrayList;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public abstract class Similarity_measure_final {

    /**
     * Name of the similarity measure
     */
    protected String name;

    /**
     * Minimum number of comun users to evaluate
     */
    public static int min_comun_users = 1;

    /**
     * Calculate the similarity measure of two arrays of double with the filters
     * for this problem. Both arrays have the same size.
     *
     * @param a1 First array to compare
     * @param a2 Second array to compare
     * @return double value of the similarity measure
     */
    public abstract double calculate(double[] a1, double[] a2);

    /**
     * Generate an array with only the comun votes
     *
     * @param ColumItem First array to filter
     * @param ColumItem1 Second array to filter
     * @return
     */
    protected double[][] filter_comun_vote(double[] ColumItem, double[] ColumItem1) {
        if (ColumItem == null || ColumItem1 == null || ColumItem.length != ColumItem1.length) {
            throw new IllegalArgumentException("Uno de los parametros es null. O no tienen la misma longitud");
        }
        ArrayList<Double> ColumItem_ok = new ArrayList();
        ArrayList<Double> ColumItem1_ok = new ArrayList();

        for (int i = 0; i < ColumItem.length; i++) {
            if (ColumItem1[i] != -1 && ColumItem[i] != -1) {
                ColumItem_ok.add(ColumItem[i]);
                ColumItem1_ok.add(ColumItem1[i]);
            }
        }

        double[][] res = new double[2][];

        res[0] = ArrayUtils.toPrimitive(ColumItem_ok.toArray(new Double[ColumItem_ok.size()]));
        res[1] = ArrayUtils.toPrimitive(ColumItem1_ok.toArray(new Double[ColumItem1_ok.size()]));
        return res;
    }

    /**
     * Getter of the name
     *
     * @return String with the name of similarity measure.
     */
    public String getName() {
        return name;
    }
}
