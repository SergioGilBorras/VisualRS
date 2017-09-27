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
package tree_visualize.quality_measures.quality_measures_final;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import tree_visualize.similarity_measure_base.Similarity_measure_base;
import tree_visualize.quality_measures.quality_measures_by_node.Number_outs;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class Quality_measure_correlation_histogram extends Quality_measure_final {

    Number_outs nouts;
    Similarity_measure_base FC;

    /**
     * Calculate the correlation of the histogram
     *
     * @param FC Similarity_measure_base It is the similarity measure to compare the histograms
     * @param nouts Number_outs It is the numbers of branchs by node
     */
    public Quality_measure_correlation_histogram(Similarity_measure_base FC, Number_outs nouts) {
        this.nouts = nouts;
        this.FC = FC;
        name = "Correlacion-Histograma";
    }

    @Override
    public double calculate() throws Exception {
        if (result_calculate == null) {
            ArrayList<Double> Lout = new ArrayList(Arrays.asList(ArrayUtils.toObject(nouts.calculate())));
            Lout.sort((o1, o2) -> o1.compareTo(o2));
            double num = 0;
            double old = -1;
            double num_old = 0;
            ArrayList<Double> val_A = new ArrayList<>();
            ArrayList<Double> val_B = new ArrayList<>();
            for (Double d : Lout) {
                if (d != old) {
                    if (old != -1) {
                        System.out.println(old + ":" + num);
                    }
                    if (old > 1) {
                        double aux = (num_old / 2);
                        val_A.add(aux);
                        val_B.add(num);
                    }
                    num_old = num;
                    old = d;
                    num = 0;
                }
                num++;
            }
            System.out.println(old + ":" + num);
            double aux = (num_old / 2);
            val_A.add(aux);
            val_B.add(num);

            result_calculate = FC.calculate(ArrayUtils.toPrimitive(val_A.toArray(new Double[val_A.size()])), ArrayUtils.toPrimitive(val_B.toArray(new Double[val_B.size()])));

        }
        return result_calculate;
    }

}
