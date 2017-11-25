/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.etsisi.visualrs.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.etsisi.visualrs.io.LoadData.DatasetToRead;
import org.etsisi.visualrs.similarityMeasureFinal.SimilarityMeasureFinal;

/**
 *
 * @author rasta
 */
public class MenuCommandLine {

    private BufferedReader br;
    private ArrayList<SimilarityMeasureFinal> listSM;
    private int sD = -1;
    private int sM = -1;
    private int sSM = -1;
    private boolean loadCorrect = false;

    public MenuCommandLine() {
        br = new BufferedReader(new InputStreamReader(System.in));
        printHeader();
        sD = selectDataset();
        //System.out.println("RASTA " + sD);
        if (sD != -1) {
            loadCorrect = true;
        }
    }

    public boolean isLoadCorrect() {
        return loadCorrect;
    }

    public int getMode(ArrayList<SimilarityMeasureFinal> listSM) {
        this.listSM = listSM;
        loadCorrect = false;
        if (sD != -1) {
            sM = selectExecution();
            //System.out.println("RASTA " + sM);
            if (sM == 0) {
                sSM = selectSimilarityMeasure();
                //System.out.println("RASTA " + sSM);
                if (sSM != -1) {
                    loadCorrect = true;
                }
            } else if (sM != -1) {
                loadCorrect = true;
            }
        }
        return sM;
    }

    public DatasetToRead getDataset() {
        if (loadCorrect) {
            return DatasetToRead.values()[sD];
        } else {
            return null;
        }
    }

    public SimilarityMeasureFinal getSimilarityMeasure() {
        if (loadCorrect) {
            return listSM.get(sSM);
        } else {
            return null;
        }
    }

    private void printHeader() {
        System.out.println(" ********************************************************");
        System.out.println(" ***                                                  ***");
        System.out.println(" ***        VisualRS - For recomender system          ***");
        System.out.println(" ***           V1.0.1 - By Sergio Gil Borras          ***");
        System.out.println(" ***                                                  ***");
        System.out.println(" ***                 UPM - ETSISI - 2017              ***");
        System.out.println(" ***                                                  ***");
        System.out.println(" ********************************************************\n");
    }

    private int selectExecution() {
        int re = -1;
        System.out.println("Type the model of the execution: [0-2]");

        System.out.println("\t 0) - One similarity measure execution");
        System.out.println("\t 1) - All similarities measure execution");
        System.out.println("\t 2) - All similarities measure execution (Only Graphic)");

        try {
            int i = Integer.parseInt(br.readLine());
            if (i > 2 || i < 0) {
                System.err.println("Insert a value between 0-2.");
            } else {
                re = i;
            }
        } catch (NumberFormatException ex) {
            System.err.println("Invalid Format!1");
        } catch (IOException ex) {
            System.err.println("Invalid Format!2");
        }
        return re;
    }

    private int selectDataset() {
        int re = -1;
        System.out.println("Choise a dataset: [0-4]");

        for (DatasetToRead dtr : DatasetToRead.values()) {
            System.out.println("\t " + dtr.ordinal() + ") - " + dtr.name());
        }

        try {
            int i = Integer.parseInt(br.readLine());
            if (i > 4 || i < 0) {
                System.err.println("Insert a value between 0-4.");
            } else {
                re = i;
            }
        } catch (NumberFormatException ex) {
            System.err.println("Invalid Format!1");
        } catch (IOException ex) {
            System.err.println("Invalid Format!2");
        }
        return re;
    }

    private int selectSimilarityMeasure() {
        int re = -1;
        int position = 0;
        System.out.println("Choise a similarity measure: [0-16]");

        for (SimilarityMeasureFinal sMF : listSM) {
            System.out.println("\t " + position + ") - " + sMF.getName());
            position++;
        }

        try {
            int i = Integer.parseInt(br.readLine());
            if (i > 16 || i < 0) {
                System.err.println("Insert a value between 0-16.");
            } else {
                re = i;
            }
        } catch (NumberFormatException ex) {
            System.err.println("Invalid Format!1");
        } catch (IOException ex) {
            System.err.println("Invalid Format!2");
        }
        return re;
    }

}
