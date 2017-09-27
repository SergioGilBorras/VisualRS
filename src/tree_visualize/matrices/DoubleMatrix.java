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
package tree_visualize.matrices;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class DoubleMatrix {

    /**
     * Rows number of the matrix
     */
    public int rows;

    /**
     * Columns number of the matrix
     */
    public int columns;

    double fill = Double.POSITIVE_INFINITY;

    HashMap<Integer, HashMap<Integer, Double>> matrix;

    HashMap<Integer, HashMap<Integer, Double>> matrix_T;

    /**
     * Constructor without parameters
     */
    public DoubleMatrix() {
        matrix = new HashMap<>();
        matrix_T = new HashMap<>();
    }

    private DoubleMatrix(int rows, int columns, HashMap<Integer, HashMap<Integer, Double>> matrix, HashMap<Integer, HashMap<Integer, Double>> matrix_T, double fill) {
        this.matrix = matrix;
        this.matrix_T = matrix_T;
        this.rows = rows;
        this.columns = columns;
        this.fill = fill;
    }

    /**
     * Create a double matrix with the rows and columns specify
     *
     * @param rows int Rows number of the matrix
     * @param columns int Columns number of the matrix
     */
    public DoubleMatrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        matrix = new HashMap<>();
        matrix_T = new HashMap<>();
    }

    /**
     * It put a value in the matrix on the row and colum specify.
     *
     * @param row int The row where the value is inserted
     * @param column The column where the value is inserted
     * @param value double The value to add
     * @throws Exception Different exceptions can be throws here when you put a
     * value in the matrix
     */
    public void put(int row, int column, double value) throws Exception {
        if (this.rows <= row) {
            throw new Exception("Row fuera de los limites (Max:" + this.rows + ") Value:" + row);
        }
        if (this.columns <= column) {
            throw new Exception("Column fuera de los limites (Max:" + this.columns + ") Value:" + column);
        }

        HashMap<Integer, Double> votos = matrix.get(column);
        if (votos == null) {
            votos = new HashMap<>();
        }

        votos.put(row, value);
        matrix.put(column, votos);

        HashMap<Integer, Double> votos_user = matrix_T.get(row);
        if (votos_user == null) {
            votos_user = new HashMap<>();
        }

        votos_user.put(column, value);
        matrix_T.put(row, votos_user);
    }

    /**
     * Setter the Value to fill the matrix.
     *
     * @param fill double the Value to fill.
     */
    public void fill(double fill) {
        this.fill = fill;
    }

    /**
     * Save the matrix to disk
     *
     * @param URI String The route where the matrix is saved to disk.
     */
    public void save(String URI) {
        try {
            FileOutputStream fos = new FileOutputStream(URI);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(rows);
            oos.writeObject(columns);
            oos.writeObject(matrix);
            oos.writeObject(matrix_T);
            oos.writeObject(fill);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Load the matrix from disk
     *
     * @param URI String The route from the matrix is load to disk.
     */
    public void load(String URI) {
        try {
            FileInputStream fis = new FileInputStream(URI);
            ObjectInputStream ois = new ObjectInputStream(fis);
            rows = (int) ois.readObject();
            columns = (int) ois.readObject();
            matrix = (HashMap) ois.readObject();
            matrix_T = (HashMap) ois.readObject();
            fill = (double) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }
    }

    /**
     * Getter a Row specify for its index
     *
     * @param i int The index of the row to get.
     * @return DoubleMatrix Matrix with the row specify
     * @throws Exception Different exceptions can be throws here when you get a
     * row in the matrix
     */
    public DoubleMatrix getRow(int i) throws Exception {
        if (this.rows <= i) {
            throw new Exception("Row fuera de los limites (Max:" + this.rows + ") Value:" + i);
        }
        HashMap<Integer, HashMap<Integer, Double>> new_matrix = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Double>> new_matrix_T = new HashMap<>();
        HashMap<Integer, Double> val = matrix_T.get(i);
        new_matrix_T.put(0, val);
        if (val != null) {
            for (Map.Entry<Integer, Double> entrySet : val.entrySet()) {
                Integer key = entrySet.getKey();
                Double value = entrySet.getValue();

                HashMap<Integer, Double> Val2 = new HashMap<>();
                Val2.put(0, value);
                new_matrix.put(key, Val2);
            }
        }
        DoubleMatrix DM = new DoubleMatrix(1, this.columns, new_matrix, new_matrix_T, fill);

        return DM;
    }

    /**
     * Getter a column specify for its index
     *
     * @param i int The index of the column to get.
     * @return DoubleMatrix Matrix with the column specify
     * @throws Exception Different exceptions can be throws here when you get a
     * column in the matrix
     */
    public DoubleMatrix getColumn(int i) throws Exception {
        if (this.columns <= i) {
            throw new Exception("Column fuera de los limites (Max:" + this.columns + ") Value:" + i);
        }
        HashMap<Integer, HashMap<Integer, Double>> new_matrix = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Double>> new_matrix_T = new HashMap<>();
        HashMap<Integer, Double> val = matrix.get(i);
        new_matrix.put(0, val);
        if (val != null) {
            for (Map.Entry<Integer, Double> entrySet : val.entrySet()) {
                Integer key = entrySet.getKey();
                Double value = entrySet.getValue();

                HashMap<Integer, Double> Val2 = new HashMap<>();
                Val2.put(0, value);
                new_matrix_T.put(key, Val2);
            }
        }
        DoubleMatrix DM = new DoubleMatrix(this.rows, 1, new_matrix, new_matrix_T, fill);

        return DM;
    }

    /**
     * Getter the matrix in format double array
     *
     * @return double[] the matrix in format double array
     */
    public double[] toArray() {
        int lenght = this.columns * this.rows;
        double[] array = new double[lenght];
        Arrays.fill(array, fill);
        for (Map.Entry<Integer, HashMap<Integer, Double>> entrySet : matrix.entrySet()) {
            Integer key = entrySet.getKey();
            HashMap<Integer, Double> value = entrySet.getValue();
            if (value != null) {
                for (Map.Entry<Integer, Double> entrySet1 : value.entrySet()) {
                    Integer key1 = entrySet1.getKey();
                    Double value1 = entrySet1.getValue();
                    int posicion = (key1 * columns) + key;

                    array[posicion] = value1;
                }
            }
        }
        return array;
    }

    /**
     * Getter the transposed matrix in format double array
     *
     * @return double[] the transposed matrix in format double array
     */
    public double[] toArray_T() {
        int lenght = this.columns * this.rows;
        double[] array = new double[lenght];
        Arrays.fill(array, fill);
        for (Map.Entry<Integer, HashMap<Integer, Double>> entrySet : matrix_T.entrySet()) {
            Integer key = entrySet.getKey();
            HashMap<Integer, Double> value = entrySet.getValue();
            if (value != null) {
                for (Map.Entry<Integer, Double> entrySet1 : value.entrySet()) {
                    Integer key1 = entrySet1.getKey();
                    Double value1 = entrySet1.getValue();
                    int posicion = (key1 * rows) + key;
                    array[posicion] = value1;
                }
            }
        }
        return array;
    }

    /**
     * Getter a matrix with the sum of the columns.
     *
     * @return DoubleMatrix the matrix with the sum of the columns.
     */
    public DoubleMatrix columnSums() {
        HashMap<Integer, HashMap<Integer, Double>> new_matrix = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Double>> new_matrix_T = new HashMap<>();

        HashMap<Integer, Double> Val = new HashMap<>();

        for (Map.Entry<Integer, HashMap<Integer, Double>> entrySet : matrix.entrySet()) {
            Integer key = entrySet.getKey();
            HashMap<Integer, Double> value = entrySet.getValue();
            double total_col = 0;
            int num_ele = 0;
            for (Map.Entry<Integer, Double> entrySet1 : value.entrySet()) {
                //Integer key1 = entrySet1.getKey();
                total_col += entrySet1.getValue();
                num_ele++;

            }
            total_col = total_col + (rows * fill) - (num_ele * fill);
            Val.put(key, total_col);
        }
        new_matrix_T.put(0, Val);

        for (Map.Entry<Integer, Double> entrySet : Val.entrySet()) {
            Integer key = entrySet.getKey();
            Double value = entrySet.getValue();

            HashMap<Integer, Double> Val2 = new HashMap<>();
            Val2.put(0, value);
            new_matrix.put(key, Val2);
        }
        DoubleMatrix DM = new DoubleMatrix(1, this.columns, new_matrix, new_matrix_T, fill);

        return DM;
    }

    /**
     * Getter a matrix with the sum of the rows.
     *
     * @return DoubleMatrix the matrix with the sum of the rows.
     */
    public DoubleMatrix rowSums() {
        HashMap<Integer, HashMap<Integer, Double>> new_matrix = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Double>> new_matrix_T = new HashMap<>();

        HashMap<Integer, Double> Val = new HashMap<>();

        for (Map.Entry<Integer, HashMap<Integer, Double>> entrySet : matrix_T.entrySet()) {
            Integer key = entrySet.getKey();
            HashMap<Integer, Double> value = entrySet.getValue();
            double total_col = 0;
            int num_ele = 0;
            for (Map.Entry<Integer, Double> entrySet1 : value.entrySet()) {
                //Integer key1 = entrySet1.getKey();
                total_col += entrySet1.getValue();
                num_ele++;

            }
            total_col = total_col + (columns * fill) - (num_ele * fill);
            Val.put(key, total_col);
        }
        new_matrix.put(0, Val);

        for (Map.Entry<Integer, Double> entrySet : Val.entrySet()) {
            Integer key = entrySet.getKey();
            Double value = entrySet.getValue();

            HashMap<Integer, Double> Val2 = new HashMap<>();
            Val2.put(0, value);
            new_matrix_T.put(key, Val2);
        }
        DoubleMatrix DM = new DoubleMatrix(this.rows, 1, new_matrix, new_matrix_T, fill);

        return DM;
    }

    /**
     * It get a value in the matrix of the row and colum specify.
     *
     * @param row int The row where the value is getter
     * @param column The column where the value is getter
     * @return double the value got.
     * @throws Exception Different exceptions can be throws here when you get a
     * value in the matrix
     */
    public double get(int row, int column) throws Exception {
        if (this.rows <= row) {
            throw new Exception("Row fuera de los limites (Max:" + this.rows + ") Value:" + row);
        }
        if (this.columns <= column) {
            throw new Exception("Column fuera de los limites (Max:" + this.columns + ") Value:" + column);
        }
        double res = fill;
        HashMap hprow = matrix.get(column);
        if (hprow != null) {
            Double d = (Double) hprow.get(row);
            if (d != null) {
                res = d;
            }
        }

        return res;
    }

    /**
     * It put a column in the matrix on colum specify.
     *
     * @param column int The column where the array is getter
     * @param array souble[] the array to insert on the matrix.
     * @throws Exception Different exceptions can be throws here when you set a
     * column in the matrix
     */
    public void putColumn(int column, double[] array) throws Exception {
        if (this.columns <= column) {
            throw new Exception("Column fuera de los limites (Max:" + this.columns + ") Value:" + column);
        }
        if (this.rows < array.length) {
            throw new Exception("Array mas grande que Rows (Max:" + this.rows + ") array.length:" + array.length);
        }
        HashMap<Integer, Double> hm = new HashMap<>();
        for (int i = 0; i < array.length; i++) {
            if (array[i] != fill) {
                hm.put(i, array[i]);
                HashMap<Integer, Double> hm2 = matrix_T.get(i);
                if (hm2 == null) {
                    hm2 = new HashMap<>();
                }
                hm2.put(column, array[i]);
                matrix_T.put(i, hm2);
            }
        }
        matrix.put(column, hm);

    }

    @Override
    public String toString() {
        String a = "DoubleMatrix{" + "rows=" + rows + ", columns=" + columns + ", fill=" + fill + "}\n";
        String m = "";
        for (int i = 0; i < this.rows; i++) {
            HashMap<Integer, Double> hm2 = matrix_T.get(i);
            m += "[";
            for (int j = 0; j < this.columns; j++) {
                if (hm2 == null) {
                    m += fill + ";";
                } else {
                    Double d = hm2.get(j);
                    if (d == null) {
                        m += fill + ";";
                    } else {
                        m += d + ";";
                    }
                }
            }
            m = m.substring(0, m.length() - 1);
            m += "]\n";
        }
        return a + m;
    }

}
