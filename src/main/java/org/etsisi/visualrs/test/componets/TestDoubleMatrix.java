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
package org.etsisi.visualrs.test.componets;

import org.etsisi.visualrs.matrices.DoubleMatrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class TestDoubleMatrix {

    /**
     * This class test the class DoubleMatrix
     *
     * @throws Exception Different exceptions can be throws here with this test.
     */
    public void test() throws Exception {
        DoubleMatrix votosMatrix = new DoubleMatrix(5, 5);

        votosMatrix.fill(-1);

        votosMatrix.put(0, 0, 2);
        votosMatrix.put(0, 1, 2);
        votosMatrix.put(0, 2, 2);
        votosMatrix.put(0, 3, 2);
        votosMatrix.put(0, 4, 2);

        votosMatrix.put(1, 3, 6);
        votosMatrix.put(2, 3, 6);
        votosMatrix.put(3, 3, 6);
        votosMatrix.put(4, 3, 6);
        double[] Array = {8, 8, 8, 8, 8};

        votosMatrix.putColumn(1, Array);

        System.out.println("votosMatrix:: " + votosMatrix.toString());

        DoubleMatrix dm = votosMatrix.getRow(3);

        System.out.println("getRow:: " + dm.toString());

        double[] array = dm.toArrayT();

        for (int i = 0; i < dm.columns; i++) {
            System.out.println("a: " + i + " :: " + array[i]);
        }

        DoubleMatrix dm1 = votosMatrix.getColumn(3);

        System.out.println("getColumn:: " + dm1.toString());

        double[] array1 = dm1.toArrayT();

        for (int i = 0; i < dm1.rows; i++) {
            System.out.println("a: " + i + " :: " + array1[i]);
        }

        DoubleMatrix dm2 = votosMatrix.columnSums();

        System.out.println("columnSums:: " + dm2.toString());

        DoubleMatrix dm3 = votosMatrix.rowSums();

        System.out.println("rowSums:: " + dm3.toString());

    }

    /**
     * Point of entry to the application
     *
     * @param args Params of command line like Array of strings
     */
    public static void main(String[] args) {
        try {
            TestDoubleMatrix t = new TestDoubleMatrix();
            t.test();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }
}
