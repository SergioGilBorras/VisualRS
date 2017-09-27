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

import tree_visualize.quality_measures.faggregation.Funtion_aggregation;
import tree_visualize.quality_measures.quality_measures_by_node.Quality_measure_by_node;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class Quality_measure_aggregation extends Quality_measure_final {

    Funtion_aggregation FA;
    Quality_measure_by_node MS1;

    /**
     * Apply a Aggregation funtion to a quality measure by node
     *
     * @param FA Funtion_aggregation It is the aggregation funtion to apply.
     * @param MS1 Quality_measure_by_node. It is the transformed quality measure by node
     */
    public Quality_measure_aggregation(Funtion_aggregation FA, Quality_measure_by_node MS1) {
        this.FA = FA;
        this.MS1 = MS1;
        name = FA.getName() + "_" + MS1.getName();
    }

    @Override
    public double calculate() throws Exception {
        if (result_calculate == null) {
            result_calculate = FA.calculate(MS1.calculate());
        }
        return result_calculate;
    }

}
