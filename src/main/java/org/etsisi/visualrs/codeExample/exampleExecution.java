/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.etsisi.visualrs.codeExample;

import java.io.File;
import org.etsisi.visualrs.io.Exports;
import org.etsisi.visualrs.io.Exports.TypeFileExport;
import org.etsisi.visualrs.io.LoadData;
import org.etsisi.visualrs.io.LoadData.DatasetToRead;
import org.etsisi.visualrs.models.DistanceMatrixW1;
import org.etsisi.visualrs.models.MaximumSpanningTreeMatrix;
import org.etsisi.visualrs.models.SimilarityMatrix;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresByNode.ClosenessCentralityWqW1;
import org.etsisi.visualrs.qualityMeasures.qualityMeasuresFinal.QualityMeasureCorrelation;

public class exampleExecution {

     public static void main(String[] args) {
        exampleExecution t = new exampleExecution();
        System.exit(0);
    }

    public exampleExecution() {
        try {
            File myFile = new File(this.getClass().getResource("/datasets/FilmTrust.txt").getPath());

            LoadData MV = new LoadData(myFile, DatasetToRead.FilmTrust);

            SimilarityMatrix MC = new SimilarityMatrix(new SMPearsonExample(), MV);

            MaximumSpanningTreeMatrix MRM = new MaximumSpanningTreeMatrix(MC);

            QMOutsNumber nouts = new QMOutsNumber(MRM);

            DistanceMatrixW1 MDW1 = new DistanceMatrixW1(MRM);

            SMBPearsonExample pearsonCorrelation = new SMBPearsonExample();
            ClosenessCentralityWqW1 pcW1 = new ClosenessCentralityWqW1(MDW1);
            QualityMeasureCorrelation QMC = new QualityMeasureCorrelation(pearsonCorrelation, nouts, pcW1);

            System.out.println(QMC.getName() + " => " + QMC.calculate());

            Exports GraphicsGephi = new Exports(MRM);

            GraphicsGephi.execute(TypeFileExport.PDF);
        } catch (Exception e) {
            System.out.println("Excepcion: " + e.getMessage());
        }
    }
}
