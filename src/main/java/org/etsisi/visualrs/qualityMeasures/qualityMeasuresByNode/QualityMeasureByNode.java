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
package org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode;

//import org.etsisi.visualrs.matrices.DoubleMatrix;
import org.jblas.DoubleMatrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public abstract class QualityMeasureByNode {

    /**
     * The matrix to calculate the quality measure
     */
    protected DoubleMatrix matriz;

    /**
     * Result of calculate the quality measure
     */
    protected double[] resultCalculate = null;

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
     * Getter of the matrix to calculate the quality measure
     *
     * @return DoubleMatrix with the matrix to calculate the quality measure
     */
    public DoubleMatrix getMatriz() {
        return matriz;
    }

    /**
     * Constructor class. Create a quality measure from a matrix by node.
     *
     * @param grafo DoubleMatrix The matrix to calculate the quality measure
     */
    public QualityMeasureByNode(DoubleMatrix grafo) {
        this.matriz = grafo;
    }

    /**
     * It calculates the quality measure by node.
     *
     * @return Array of Double with the result by node.
     * @throws Exception Different exceptions can be throws here with the
     * calculate of the quality measure by node
     */
    public abstract double[] calculate() throws Exception;

}
