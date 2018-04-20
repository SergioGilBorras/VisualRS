/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.etsisi.visualrs.examples;

import java.awt.Color;
import java.io.File;
import org.etsisi.visualrs.io.Exports;
import org.etsisi.visualrs.io.Exports.TypeFileExport;
import org.etsisi.visualrs.io.LoadData;
import org.etsisi.visualrs.io.LoadData.DatasetToRead;
import org.etsisi.visualrs.models.DistanceMatrixW1;
import org.etsisi.visualrs.models.MaximumSpanningTreeMatrix;
import org.etsisi.visualrs.models.SimilarityMatrix;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.ClosenessCentralityWqW1;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.NumberOuts;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresFinal.QualityMeasureCorrelation;
import org.etsisi.visualrs.similarityMeasureBase.Pearson;
import org.etsisi.visualrs.similarityMeasureFinal.FPearson;

public class GettingStarted {

    public static void main(String[] args) {
        GettingStarted t = new GettingStarted();
        System.exit(0);
    }

    public GettingStarted() {
        try {
            File myFile = new File(this.getClass().getResource("/datasets/FilmTrust.txt").toURI());

            LoadData MV = new LoadData(myFile, DatasetToRead.FilmTrust);

            SimilarityMatrix MC = new SimilarityMatrix(new FPearson(), MV);

            MaximumSpanningTreeMatrix MRM = new MaximumSpanningTreeMatrix(MC);

            NumberOuts nouts = new NumberOuts(MRM);

            DistanceMatrixW1 MDW1 = new DistanceMatrixW1(MRM);

            Pearson pearsonCorrelation = new Pearson();
            ClosenessCentralityWqW1 pcW1 = new ClosenessCentralityWqW1(MDW1);
            QualityMeasureCorrelation QMC = new QualityMeasureCorrelation(pearsonCorrelation, nouts, pcW1);

            System.out.println(QMC.getName() + " => " + QMC.calculate());

            Exports GraphicsGephi = new Exports(MRM);

            GraphicsGephi.setColorNodes(Color.ORANGE);
            GraphicsGephi.setSizeNodes(20);
            GraphicsGephi.setColorEdgeBySimilarity();
            GraphicsGephi.setColorTags(Color.BLACK);
            GraphicsGephi.setSizeTags(0);

            GraphicsGephi.execute(TypeFileExport.PNG);

        } catch (Exception e) {
            System.out.println("Excepcion: " + e.getMessage());
        }
    }
}
