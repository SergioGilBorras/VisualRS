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
package org.etsisi.visualrs.test;

import org.etsisi.visualrs.models.GenerateSimilarityVectorsSimple;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import org.etsisi.visualrs.similarityMeasureBase.Pearson;
import org.etsisi.visualrs.similarityMeasureFinal.FCJacMD;
import org.etsisi.visualrs.similarityMeasureFinal.FCosine;
import org.etsisi.visualrs.similarityMeasureFinal.FCovariance;
import org.etsisi.visualrs.similarityMeasureFinal.FJMMD;
import org.etsisi.visualrs.similarityMeasureFinal.FJMSD;
import org.etsisi.visualrs.similarityMeasureFinal.FJaccard;
import org.etsisi.visualrs.similarityMeasureFinal.FJaccardCosine;
import org.etsisi.visualrs.similarityMeasureFinal.FJaccardCovariance;
import org.etsisi.visualrs.similarityMeasureFinal.FJaccardKendalls;
import org.etsisi.visualrs.similarityMeasureFinal.FJaccardPearson;
import org.etsisi.visualrs.similarityMeasureFinal.FJaccardSpearmans;
import org.etsisi.visualrs.similarityMeasureFinal.FKendalls;
import org.etsisi.visualrs.similarityMeasureFinal.FMMD;
import org.etsisi.visualrs.similarityMeasureFinal.FMSD;
import org.etsisi.visualrs.similarityMeasureFinal.FPearson;
import org.etsisi.visualrs.similarityMeasureFinal.FSM;
import org.etsisi.visualrs.similarityMeasureFinal.FSpearmans;
import org.etsisi.visualrs.similarityMeasureFinal.SimilarityMeasureFinal;
import org.etsisi.visualrs.io.Exports;
import org.etsisi.visualrs.io.Exports.TypeFileExport;
import org.etsisi.visualrs.models.SimilarityMatrix;
import org.etsisi.visualrs.models.DistanceMatrixW1;
import org.etsisi.visualrs.models.DistanceMatrixWOuts;
import org.etsisi.visualrs.models.DistanceMatrixWq;
import org.etsisi.visualrs.models.MaximumSpanningTreeMatrix;
import org.etsisi.visualrs.io.LoadData;
import org.etsisi.visualrs.io.MenuCommandLine;
import org.etsisi.visualrs.qualityMeasures.fAggregation.AVG;
import org.etsisi.visualrs.qualityMeasures.fComposition.Div;
import org.etsisi.visualrs.qualityMeasures.fModification.LN;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresFinal.QualityMeasureCorrelationHistogram;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresFinal.QualityMeasureEMHistogram;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresFinal.QualityMeasureAggregation;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresFinal.QualityMeasureCorrelation;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresFinal.QualityMeasureFinal;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.BetweennessCentrality;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.MinDistanceLeaf;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.AvgDistanceLeavesOutsWqW1;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.AvgDistanceLeavesWqW1;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.QualityMeasureComposition;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.QualityMeasureModificate;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.NumberOuts;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.NumberVotes;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.ClosenessCentralityWqW1;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.SumVotes;

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
    ArrayList<SimilarityMeasureFinal> listSM;
    /**
     * The list with the quality measures to execute
     */
    ArrayList<QualityMeasureFinal> listQM;

    LoadData MV;
    SimilarityMatrix MC;
    MaximumSpanningTreeMatrix MRM;

    DistanceMatrixW1 MDW1;
    DistanceMatrixWq MDWq;
    DistanceMatrixWOuts MDWouts;

    NumberOuts nouts;

    GenerateSimilarityVectorsSimple gvss;

    String fileName;

    /**
     * Point of entry to the application
     *
     * @param args Params of command line like Array of strings
     */
    public static void main(String[] args) {
        Test t = new Test();
        System.exit(0);
    }

    /**
     * Execute the test
     */
    public Test() {
        try {
            MenuCommandLine menu = new MenuCommandLine();
            if (menu.isLoadCorrect()) {
                selectDataset(menu.getDataset());
                gvss = new GenerateSimilarityVectorsSimple(4, MV);
                generateListSM();

                switch (menu.getMode(listSM)) {
                    case 0:
                        SimilarityMeasureFinal SMF = menu.getSimilarityMeasure();
                        if (SMF != null) {
                            execOnce(SMF);
                            System.out.println("\nYou can get the generated report and graphics in the DATA folder.");
                        } else {
                            System.out.println("Sorry, you should fill correctly the menu.");
                        }
                        break;
                    case 1:
                        execBatch();
                        System.out.println("\nYou can get the generated report and graphics in the DATA folder.");
                        break;
                    case 2:
                        execGraphicsBatch();
                        System.out.println("\nYou can get the generated graphics in the DATA folder.");
                        break;
                    default:
                        System.out.println("Sorry, you should fill correctly the menu.1");
                }
            } else {
                System.out.println("Sorry, you should fill correctly the menu.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception: " + e.getMessage());
        }

    }

    /**
     * Select the dataset to execute
     */
    private void selectDataset(LoadData.DatasetToRead dts) throws Exception {
        File file;
        switch (dts) {
            case Jester:
                file = new File(this.getClass().getResource("/datasets/jester-data-1.csv").toURI());
                break;
            case MovieLens:
                file = new File(this.getClass().getResource("/datasets/ml1m-ratings.txt").toURI());
                break;
            case BookCrossing:
                file = new File(this.getClass().getResource("/datasets/BX-Book-Ratings.csv").toURI());
                break;
            case FilmTrust:
                file = new File(this.getClass().getResource("/datasets/filmtrust-ratings.txt").toURI());
                break;
            case Netflix:
                file = new File(this.getClass().getResource("/datasets/nf-ratings.txt").toURI());
                break;
            default:
                file = new File(this.getClass().getResource("/datasets/filmtrust-ratings-s.txt").toURI());
                break;
        }
        MV = new LoadData(file, dts);

        fileName = file.getName();
    }

    /**
     * Generate the graphics for the calculate maximum spanning tree matrix
     */
    private void execGraphics() throws Exception {
        Exports graphicsGephi = new Exports(MRM);
        for (TypeFileExport p : TypeFileExport.values()) {
            graphicsGephi.execute(p);
        }
    }

    /**
     * Generate a tree using the SM select of listSM by index. And it shows its
     * results of the QM
     *
     * @param idSel Index of the listSM (list with the similarity measures)
     */
    private void execOnce(SimilarityMeasureFinal FCFActivo) throws Exception {

        if (FCFActivo != null) {
            if (MV.hasRankMatrix(FCFActivo.getName())) {
                MV.loadRankMatrix(FCFActivo.getName());
            }

            System.out.println(" -- " + FCFActivo.getName() + " -- ");

            MC = new SimilarityMatrix(FCFActivo, MV);
            MRM = new MaximumSpanningTreeMatrix(MC);

            nouts = new NumberOuts(MRM);

            MDW1 = new DistanceMatrixW1(MRM);
            MDWq = new DistanceMatrixWq(MRM);
            MDWouts = new DistanceMatrixWOuts(MRM, nouts);

            generateListQM();

            for (QualityMeasureFinal metrica : listQM) {
                System.out.println(metrica.getName() + " .. " + metrica.calculate());
            }

            execGraphics();
        }
    }

    /**
     * Generate the trees using the listSM. And it shows its results of QM
     */
    private void execBatch() throws Exception {
        boolean setHeader = false;
        String Header = ";";

        File sFile = new File("./data/" + fileName + "/resultado_" + System.currentTimeMillis() + ".csv");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sFile));
            for (SimilarityMeasureFinal FCFActivo : listSM) {

                System.out.println(" -- " + FCFActivo.getName() + " -- ");

                if (MV.hasRankMatrix(FCFActivo.getName())) {
                    MV.loadRankMatrix(FCFActivo.getName());
                }

                MC = new SimilarityMatrix(FCFActivo, MV);
                MRM = new MaximumSpanningTreeMatrix(MC);

                nouts = new NumberOuts(MRM);

                MDW1 = new DistanceMatrixW1(MRM);
                MDWq = new DistanceMatrixWq(MRM);
                MDWouts = new DistanceMatrixWOuts(MRM, nouts);

                generateListQM();

                String Results = FCFActivo.getName() + ";";
                for (QualityMeasureFinal metrica : listQM) {
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

                execGraphics();
            }
            bw.close();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Generate the trees using the listSM. And it generates the graphics for
     * them.
     */
    private void execGraphicsBatch() throws Exception {

        try {
            for (SimilarityMeasureFinal FCFActivo : listSM) {

                System.out.println(" -- " + FCFActivo.getName() + " -- ");

                MC = new SimilarityMatrix(FCFActivo, MV);
                MRM = new MaximumSpanningTreeMatrix(MC);

                execGraphics();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Load the list for similarity measures to use.
     */
    private void generateListSM() throws Exception {
        System.out.println("Generate list SM..");

        listSM = new ArrayList<>();

        listSM.add(new FPearson());
        listSM.add(new FKendalls());
        listSM.add(new FSpearmans());
        listSM.add(new FCovariance());
        listSM.add(new FCosine());
        listSM.add(new FMSD());
        listSM.add(new FMMD());
        listSM.add(new FSM(gvss));
        listSM.add(new FJaccard());
        listSM.add(new FJaccardPearson());
        listSM.add(new FJaccardKendalls());
        listSM.add(new FJaccardSpearmans());
        listSM.add(new FJaccardCovariance());
        listSM.add(new FJaccardCosine());
        listSM.add(new FJMSD());
        listSM.add(new FJMMD());
        listSM.add(new FCJacMD());

    }

    /**
     * Load the list for quality measures to use.
     */
    private void generateListQM() throws Exception {
        System.out.println("Generate list QM..");

        listQM = new ArrayList<>();

        Pearson pearsonCorrelation = new Pearson();
        ClosenessCentralityWqW1 pcW1 = new ClosenessCentralityWqW1(MDW1);
        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, nouts, pcW1));

        BetweennessCentrality ci = new BetweennessCentrality(MDWq);
        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, nouts, ci));

        SumVotes sv = new SumVotes(MV);
        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, sv, pcW1));

        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, sv, ci));

        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, nouts, sv));

        QualityMeasureModificate lnSV = new QualityMeasureModificate(new LN(), sv);
        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, nouts, lnSV));

        NumberVotes nv = new NumberVotes(MV);
        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, nouts, nv));

        QualityMeasureComposition mcAVGVotos = new QualityMeasureComposition(new Div(), sv, nv);
        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, nouts, mcAVGVotos));

        MinDistanceLeaf dj = new MinDistanceLeaf(MRM, nouts);
        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, dj, sv));

        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, dj, lnSV));

        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, dj, nv));

        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, nouts, dj));

        QualityMeasureModificate lnDJ = new QualityMeasureModificate(new LN(), dj);
        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, nouts, lnDJ));

        AvgDistanceLeavesWqW1 mDHW1 = new AvgDistanceLeavesWqW1(MDW1, nouts);
        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, nouts, mDHW1));

        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, sv, mDHW1));

        AvgDistanceLeavesOutsWqW1 mDHWouts = new AvgDistanceLeavesOutsWqW1(MDW1, MDWouts, nouts);
        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, nouts, mDHWouts));

        listQM.add(new QualityMeasureCorrelation(pearsonCorrelation, sv, mDHWouts));

        listQM.add(new QualityMeasureAggregation(new AVG(), mDHW1));
        listQM.add(new QualityMeasureAggregation(new AVG(), mDHWouts));

        listQM.add(new QualityMeasureEMHistogram(nouts));
        listQM.add(new QualityMeasureCorrelationHistogram(pearsonCorrelation, nouts));

    }

}
