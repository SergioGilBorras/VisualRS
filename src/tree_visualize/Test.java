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
package tree_visualize;

import tree_visualize.matrices.Generate_similarity_vectors_simple;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import tree_visualize.similarity_measure_base.Pearson;
import tree_visualize.similarity_measure_final.FCJacMD;
import tree_visualize.similarity_measure_final.FCosine;
import tree_visualize.similarity_measure_final.FCovariance;
import tree_visualize.similarity_measure_final.FJMMD;
import tree_visualize.similarity_measure_final.FJMSD;
import tree_visualize.similarity_measure_final.FJaccard;
import tree_visualize.similarity_measure_final.FJaccardCosine;
import tree_visualize.similarity_measure_final.FJaccardCovariance;
import tree_visualize.similarity_measure_final.FJaccardKendalls;
import tree_visualize.similarity_measure_final.FJaccardPearson;
import tree_visualize.similarity_measure_final.FJaccardSpearmans;
import tree_visualize.similarity_measure_final.FKendalls;
import tree_visualize.similarity_measure_final.FMMD;
import tree_visualize.similarity_measure_final.FMSD;
import tree_visualize.similarity_measure_final.FPearson;
import tree_visualize.similarity_measure_final.FSM;
import tree_visualize.similarity_measure_final.FSpearmans;
import tree_visualize.similarity_measure_final.Similarity_measure_final;
import tree_visualize.graphics_gephi.Gephi;
import tree_visualize.graphics_gephi.Gephi.Type_File_Export;
import tree_visualize.matrices.Similarity_matrix;
import tree_visualize.matrices.Distance_matrix_w1;
import tree_visualize.matrices.Distance_matrix_wouts;
import tree_visualize.matrices.Distance_matrix_wq;
import tree_visualize.matrices.Maximum_spanning_tree_matrix;
import tree_visualize.matrices.Rank_matrix;
import tree_visualize.quality_measures.faggregation.AVG;
import tree_visualize.quality_measures.fcomposition.Div;
import tree_visualize.quality_measures.fmodification.LN;
import tree_visualize.quality_measures.quality_measures_final.Quality_measure_correlation_histogram;
import tree_visualize.quality_measures.quality_measures_final.Quality_measure_EM_histogram;
import tree_visualize.quality_measures.quality_measures_final.Quality_measure_aggregation;
import tree_visualize.quality_measures.quality_measures_final.Quality_measure_correlation;
import tree_visualize.quality_measures.quality_measures_final.Quality_measure_final;
import tree_visualize.quality_measures.quality_measures_by_node.Betweenness_centrality;
import tree_visualize.quality_measures.quality_measures_by_node.Min_distance_leaf;
import tree_visualize.quality_measures.quality_measures_by_node.Avg_distance_leaves_outs_wq_w1;
import tree_visualize.quality_measures.quality_measures_by_node.Avg_distance_leaves_wq_w1;
import tree_visualize.quality_measures.quality_measures_by_node.Quality_measure_composition;
import tree_visualize.quality_measures.quality_measures_by_node.Quality_measure_modificate;
import tree_visualize.quality_measures.quality_measures_by_node.Number_outs;
import tree_visualize.quality_measures.quality_measures_by_node.Number_votes;
import tree_visualize.quality_measures.quality_measures_by_node.Closeness_centrality_wq_w1;
import tree_visualize.quality_measures.quality_measures_by_node.Sum_votes;

/**
 * This class executes the different experiments and get the results
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class Test {

    /**
     * The list with the similarity measures to use
     */
    ArrayList<Similarity_measure_final> list_SM;
    /**
     * The list with the quality measures to execute
     */
    ArrayList<Quality_measure_final> list_QM;

    Rank_matrix MV;
    Similarity_matrix MC;
    Maximum_spanning_tree_matrix MRM;

    Distance_matrix_w1 MD_w1;
    Distance_matrix_wq MD_wq;
    Distance_matrix_wouts MD_wouts;

    Number_outs nouts;

    Generate_similarity_vectors_simple gvss;

    /**
     * Point of entry to the application
     *
     * @param args Params of command line like Array of strings
     */
    public static void main(String[] args) {
        Test t = new Test();
    }

    /**
     * Execute the test
     */
    public Test() {
        try {
            //exec_once(0);
            exec_batch();
            //ejecucionBateria_graph();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }

    }

    /**
     * Generate the graphics for the calculate maximum spanning tree matrix
     */
    private void exec_graphics() throws Exception {
        Gephi graphics_gephi = new Gephi(MRM);
        for (Type_File_Export p : Type_File_Export.values()) {
            graphics_gephi.execute(p);
        }
    }

    /**
     * Generate a tree using the SM select of list_SM by index. And it shows its
     * results of the QM
     *
     * @param idSel Index of the list_SM (list with the similarity measures)
     */
    private void exec_once(int idSel) throws Exception {
        //jester-data-1.csv");//BX-Book-Ratings.csv//ml1m-ratings.txt");//filmtrust-ratings//nf-ratings//ml1m-ratings

        File file = new File(".\\datasets\\BX-Book-Ratings.csv");
        MV = new Rank_matrix(file, Rank_matrix.Dataset_To_Read.BookCrossing);
        gvss = new Generate_similarity_vectors_simple(4, MV);
        generate_list_SM();
        Similarity_measure_final FCF_activo = list_SM.get(idSel);

        if (MV.has_rank_matrix(FCF_activo.getName())) {
            MV.load_rank_matrix(FCF_activo.getName());
        }

        System.out.println(" -- " + FCF_activo.getName() + " -- ");

        MC = new Similarity_matrix(FCF_activo, MV);
        MRM = new Maximum_spanning_tree_matrix(MC);

        nouts = new Number_outs(MRM);

        MD_w1 = new Distance_matrix_w1(MRM);
        MD_wq = new Distance_matrix_wq(MRM);
        MD_wouts = new Distance_matrix_wouts(MRM, nouts);

        generate_list_QM();

        for (Quality_measure_final metrica : list_QM) {
            System.out.println(metrica.getName() + " .. " + metrica.calculate());
        }

        exec_graphics();
    }

    /**
     * Generate the trees using the list_SM. And it shows its results of QM
     */
    private void exec_batch() throws Exception {
        boolean setHeader = false;
        String Header = ";";
        //BX-Book-Ratings.csv//ml1m-ratings.txt;//filmtrust-ratings//nf-ratings//ml1m-ratings//jester-data-1.csv
        File file = new File(".\\datasets\\BX-Book-Ratings.csv");

        MV = new Rank_matrix(file, Rank_matrix.Dataset_To_Read.BookCrossing);
        gvss = new Generate_similarity_vectors_simple(4, MV);
        generate_list_SM();

        File sFile = new File("./data/" + file.getName() + "/resultado_" + System.currentTimeMillis() + ".csv");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sFile));
            for (Similarity_measure_final FCF_activo : list_SM) {

                System.out.println(" -- " + FCF_activo.getName() + " -- ");

                if (MV.has_rank_matrix(FCF_activo.getName())) {
                    MV.load_rank_matrix(FCF_activo.getName());
                }

                MC = new Similarity_matrix(FCF_activo, MV);
                MRM = new Maximum_spanning_tree_matrix(MC);

                nouts = new Number_outs(MRM);

                MD_w1 = new Distance_matrix_w1(MRM);
                MD_wq = new Distance_matrix_wq(MRM);
                MD_wouts = new Distance_matrix_wouts(MRM, nouts);

                generate_list_QM();

                String Results = FCF_activo.getName() + ";";
                for (Quality_measure_final metrica : list_QM) {
                    if (!setHeader) {
                        Header += metrica.getName() + ";";
                    }
                    Double value = metrica.calculate();
                    Results += value.toString().replace(".", ",") + ";";
                }
                if (!setHeader) {
                    setHeader = true;
                    bw.write(Header + "\r\n");
                }
                bw.write(Results);
                bw.write("\r\n");

                exec_graphics();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Generate the trees using the list_SM. And it generates the graphics for
     * them.
     */
    private void exec_graphics_batch() throws Exception {

        File file = new File(".\\datasets\\ml1m-ratings.txt");//filmtrust-ratings//nf-ratings//ml1m-ratings

        MV = new Rank_matrix(file, Rank_matrix.Dataset_To_Read.Movielens);
        gvss = new Generate_similarity_vectors_simple(4, MV);
        generate_list_SM();

        try {
            for (Similarity_measure_final FCF_activo : list_SM) {

                System.out.println(" -- " + FCF_activo.getName() + " -- ");

                MC = new Similarity_matrix(FCF_activo, MV);
                MRM = new Maximum_spanning_tree_matrix(MC);

                exec_graphics();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Load the list for similarity measures to use.
     */
    private void generate_list_SM() throws Exception {
        System.out.println("Generate list SM..");

        list_SM = new ArrayList<>();

        list_SM.add(new FPearson());
        list_SM.add(new FKendalls());
        list_SM.add(new FSpearmans());
        list_SM.add(new FCovariance());
        list_SM.add(new FCosine());
        list_SM.add(new FMSD());
        list_SM.add(new FMMD());
        list_SM.add(new FSM(gvss));
        list_SM.add(new FJaccard());
        list_SM.add(new FJaccardPearson());
        list_SM.add(new FJaccardKendalls());
        list_SM.add(new FJaccardSpearmans());
        list_SM.add(new FJaccardCovariance());
        list_SM.add(new FJaccardCosine());
        list_SM.add(new FJMSD());
        list_SM.add(new FJMMD());
        list_SM.add(new FCJacMD());

    }

    /**
     * Load the list for quality measures to use.
     */
    private void generate_list_QM() throws Exception {
        System.out.println("Generate list QM..");

        list_QM = new ArrayList<>();

        Pearson PearsonCorrelation = new Pearson();
        Closeness_centrality_wq_w1 Pc_w1 = new Closeness_centrality_wq_w1(MD_w1);
        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, nouts, Pc_w1));

        Betweenness_centrality Ci = new Betweenness_centrality(MD_wq);
        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, nouts, Ci));

        Sum_votes SV = new Sum_votes(MV);
        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, SV, Pc_w1));

        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, SV, Ci));

        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, nouts, SV));

        Quality_measure_modificate LN_SV = new Quality_measure_modificate(new LN(), SV);
        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, nouts, LN_SV));

        Number_votes NV = new Number_votes(MV);
        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, nouts, NV));

        Quality_measure_composition MC_AVGVotos = new Quality_measure_composition(new Div(), SV, NV);
        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, nouts, MC_AVGVotos));

        Min_distance_leaf DJ = new Min_distance_leaf(MRM, nouts);
        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, DJ, SV));

        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, DJ, LN_SV));

        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, DJ, NV));

        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, nouts, DJ));

        Quality_measure_modificate LN_DJ = new Quality_measure_modificate(new LN(), DJ);
        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, nouts, LN_DJ));

        Avg_distance_leaves_wq_w1 MDH_w1 = new Avg_distance_leaves_wq_w1(MD_w1, nouts);
        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, nouts, MDH_w1));

        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, SV, MDH_w1));

        Avg_distance_leaves_outs_wq_w1 MDH_wouts = new Avg_distance_leaves_outs_wq_w1(MD_w1, MD_wouts, nouts);
        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, nouts, MDH_wouts));

        list_QM.add(new Quality_measure_correlation(PearsonCorrelation, SV, MDH_wouts));

        list_QM.add(new Quality_measure_aggregation(new AVG(), MDH_w1));
        list_QM.add(new Quality_measure_aggregation(new AVG(), MDH_wouts));

        list_QM.add(new Quality_measure_EM_histogram(nouts));
        list_QM.add(new Quality_measure_correlation_histogram(PearsonCorrelation, nouts));

    }

}
