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
package tree_visualize.quality_measures.quality_measures_by_node;

import tree_visualize.quality_measures.fmodification.Funtion_modification;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class Quality_measure_modificate extends Quality_measure_by_node {

    Funtion_modification FM;
    Quality_measure_by_node MS;

    /**
     * Apply a modification funtion to a quality measure by node
     *
     * @param FM Funtion_modification It is the modification funtion to apply.
     * @param MS Quality_measure_by_node. It is the transformed quality measure
     * by node
     */
    public Quality_measure_modificate(Funtion_modification FM, Quality_measure_by_node MS) {
        super(null);
        this.FM = FM;
        this.MS = MS;
        name = FM.getName() + "_" + MS.getName();
    }

    @Override
    public double[] calculate() throws Exception {
        if (result_calculate == null) {
            result_calculate = FM.calculate(MS.calculate());
        }
        return result_calculate;
    }

}
