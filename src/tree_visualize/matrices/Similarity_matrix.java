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

import java.io.File;
import java.util.ArrayList;
import tree_visualize.similarity_measure_final.Similarity_measure_final;
import org.jblas.DoubleMatrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public final class Similarity_matrix {

    private DoubleMatrix similarity_matrix = null;
    private Rank_matrix MV;
    private Similarity_measure_final FC;

    /**
     * Getter of the rank matrix.
     *
     * @return sparse DoubleMatrix with the votes.
     */
    public tree_visualize.matrices.DoubleMatrix getRank_matrix() {
        return MV.getRank_matrix();
    }

    /**
     * Getter of the similarity matrix.
     *
     * @return DoubleMatrix with the similarity values.
     */
    public DoubleMatrix getSimilarity_matrix() {
        return similarity_matrix;
    }

    /**
     * Getter of the File name to save the similarity matrix
     *
     * @return String File name
     */
    public String getFile_name() {
        return MV.getFile_name();
    }

    /**
     * Getter of the similarity measure name.
     *
     * @return String the similarity measure name.
     */
    public String getSimilarity_measure_name() {
        return FC.getName();
    }

    /**
     * Constructor of the class. It generates similarity matrix from the rank
     * matrix and a similarity measure.
     *
     * @param FC Similarity_measure_final The similarity measure final to
     * generate the similarity matrix.
     * @param MV Rank_matrix The rank matrix to generate the similarity matrix.
     * @throws Exception Different exceptions can be throws here with the
     * generation of the similarity matrix.
     */
    public Similarity_matrix(Similarity_measure_final FC, Rank_matrix MV) throws Exception {
        this.FC = FC;
        this.MV = MV;
        if (has_matrizes_calculadas()) {
            load_similarity_matrix();
        } else {
            generate_similarity_matrix();
            delete_orphan_nodes();
            save_similarity_matrix();
        }
    }

    private void delete_orphan_nodes() throws Exception {
        tree_visualize.matrices.DoubleMatrix votos_matrix = MV.getRank_matrix();
        int item_ok = 0;
        ArrayList<Integer> columnsToRemove = new ArrayList();
        for (int i = 0; i < similarity_matrix.columns; i++) {
            double[] ColumItem = similarity_matrix.getColumn(i).toArray();
            int cont = 0;
            for (int j = 0; j < ColumItem.length; j++) {
                if (ColumItem[j] != -99) {
                    cont++;
                }
            }
            if (cont > 0) {
                item_ok++;
            } else {
                columnsToRemove.add(i);
            }
        }
        if (columnsToRemove.size() > 0) {
            System.out.println("New matrix:: " + item_ok + " x " + item_ok);
            DoubleMatrix correlation_matrix_aux = new DoubleMatrix(item_ok, item_ok);
            tree_visualize.matrices.DoubleMatrix votos_matrix_aux = new tree_visualize.matrices.DoubleMatrix(votos_matrix.rows, item_ok);
            votos_matrix_aux.fill(votos_matrix.fill);
            double[] VColum = new double[item_ok];
            int index_colum = 0;
            for (int i = 0; i < similarity_matrix.columns; i++) {
                double[] ColumItem = similarity_matrix.getColumn(i).toArray();;
                if (!columnsToRemove.contains(i)) {
                    int index_row = 0;
                    for (int j = 0; j < ColumItem.length; j++) {
                        if (!columnsToRemove.contains((Integer) j)) {
                            VColum[index_row] = ColumItem[j];
                            index_row++;
                        }
                    }

                    correlation_matrix_aux.putColumn(index_colum, new DoubleMatrix(VColum));
                    votos_matrix_aux.putColumn(index_colum, votos_matrix.getColumn(i).toArray());
                    index_colum++;
                }
            }

            MV.setRank_matrix(votos_matrix_aux);
            MV.save_rank_matrix(FC.getName());
            similarity_matrix = correlation_matrix_aux;
        }
    }

    private void generate_similarity_matrix() throws Exception {
        tree_visualize.matrices.DoubleMatrix votos_matrix = MV.getRank_matrix();
        similarity_matrix = new DoubleMatrix(votos_matrix.columns, votos_matrix.columns);
        similarity_matrix.fill(-99);

        long t_ini = System.currentTimeMillis();

        for (int item = 0; item < votos_matrix.columns; item++) {

            if (item % 100 == 0) {
                System.out.println("SIM_load:: " + item + " :: " + (System.currentTimeMillis() - t_ini));
                t_ini = System.currentTimeMillis();
            }

            double[] ColumItem = votos_matrix.getColumn(item).toArray();

            for (int item1 = item + 1; item1 < votos_matrix.columns; item1++) {

                double[] ColumItem1 = votos_matrix.getColumn(item1).toArray();

                double val = FC.calculate(ColumItem, ColumItem1);

                similarity_matrix.put(item, item1, val);
                similarity_matrix.put(item1, item, val);
            }

        }
    }

    private void save_similarity_matrix() {
        System.out.println("save similarity matrix..");
        File f = new File("./data/" + MV.getFile_name() + "/" + FC.getName());
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            similarity_matrix.save("./data/" + MV.getFile_name() + "/" + FC.getName() + "/correlation_matrix.dat");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void load_similarity_matrix() {
        System.out.println("load similarity matrix..");
        try {
            if (similarity_matrix == null) {
                similarity_matrix = new DoubleMatrix();
                similarity_matrix.load("./data/" + MV.getFile_name() + "/" + FC.getName() + "/correlation_matrix.dat");
            }
        } catch (Exception e) {
            System.out.println("Excepcion (load similarity matrix): " + e.getMessage());
        }
    }

    private boolean has_matrizes_calculadas() {
        File f = new File("./data/" + MV.getFile_name() + "/" + FC.getName() + "/correlation_matrix.dat");
        return f.exists();
    }

}
