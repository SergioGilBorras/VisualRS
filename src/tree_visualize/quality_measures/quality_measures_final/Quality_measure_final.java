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

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public abstract class Quality_measure_final {

    /**
     * Result of calculate the quality measure
     */
    protected Double result_calculate = null;

    /**
     * Name of the quality measure
     */
    protected String name;

    /**
     * Getter of the quality measure name
     *
     * @return String with the name
     */
    public String getName() {
        return name;
    }

    /**
     * It calculates the quality measure.
     *
     * @return Double with the result
     * @throws Exception Different exceptions can be throws here with the
     * calculate of the quality measure
     */
    public abstract double calculate() throws Exception;

}
