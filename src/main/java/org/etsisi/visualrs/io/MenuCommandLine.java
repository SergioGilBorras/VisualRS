/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.etsisi.visualrs.io;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.etsisi.visualrs.io.LoadData.DatasetToRead;
import org.etsisi.visualrs.similarityMeasureFinal.SimilarityMeasureFinal;

public class MenuCommandLine {

    private BufferedReader br;
    private ArrayList<SimilarityMeasureFinal> listSM;
    private int sD = -1;
    private int sM = -1;
    private int sSM = -1;
    private String filePath = "";
    private boolean loadCorrect = false;

    public MenuCommandLine() {
        br = new BufferedReader(new InputStreamReader(System.in));
        printHeader();
        sD = selectDataset();
        if (sD == 99) {
            filePath = loadFilePathMenu();
        }
        if (sD != -1) {
            loadCorrect = true;
        }
    }

    public boolean isLoadCorrect() {
        return loadCorrect;
    }

    public boolean hasFilePath() {
        return (!filePath.equalsIgnoreCase(""));
    }

    public String getFilePath() {
        return filePath;
    }

    public int getMode(ArrayList<SimilarityMeasureFinal> listSM) {
        this.listSM = listSM;
        loadCorrect = false;
        if (sD != -1) {
            sM = selectExecution();
            if (sM == 0) {
                sSM = selectSimilarityMeasure();
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
        System.out.println("\n");
        System.out.println(" ********************************************************");
        System.out.println(" ***                                                  ***");
        System.out.println(" ***        VisualRS - For recomender system          ***");
        System.out.println(" ***           V1.0.1 - By Sergio Gil Borras          ***");
        System.out.println(" ***                                                  ***");
        System.out.println(" ***               UPM - ETSISI - 2017                ***");
        System.out.println(" ***                                                  ***");
        System.out.println(" ********************************************************\n");
    }

    static public int selectColors(String text) {
        int re = -1;
        System.out.println("\nHex Value - Color " + text + ": [0-255]");

        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            int i = Integer.parseInt(br1.readLine());
            if (i > 255 || i < 0) {
                System.err.println("Insert a value between 0-255.");
            } else {
                re = i;
            }
        } catch (NumberFormatException ex) {
            System.err.println("Invalid Format!");
        } catch (IOException ex) {
            System.err.println("Invalid Format!");
        }
        return re;
    }

    static public Color selectColorsRGB() {
        int reR = -1;
        int reG = -1;
        int reB = -1;
        System.out.println("\nColors RGB: ");

        while (reR == -1) {
            reR = selectColors("RED");
        }
        while (reG == -1) {
            reG = selectColors("GREEN");
        }
        while (reB == -1) {
            reB = selectColors("BLUE");
        }

        return new Color(reR, reG, reB);
    }

    static public int selectSizeNodes() {
        int re = -1;
        System.out.println("\nSize of the Nodes: [1-3]");

        System.out.println("\t1) - Small.");
        System.out.println("\t2) - Medium.");
        System.out.println("\t3) - Big.");

        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            int i = Integer.parseInt(br1.readLine());
            if (i > 3 || i < 1) {
                System.err.println("Insert a value between 1-3.");
            } else {
                re = i;
            }
        } catch (NumberFormatException ex) {
            System.err.println("Invalid Format!");
        } catch (IOException ex) {
            System.err.println("Invalid Format!");
        }
        return re - 1;
    }

    static public int selectSizesTags() {
        int re = -1;
        System.out.println("\nSize of the Tags: [1-3]");

        
        System.out.println("\t1) - Small.");
        System.out.println("\t2) - Medium.");
        System.out.println("\t3) - Big.");
        System.out.println("\t4) - Not show.");

        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            int i = Integer.parseInt(br1.readLine());
            if (i > 4 || i < 1) {
                System.err.println("Insert a value between 1-4.");
            } else {
                re = i;
            }
        } catch (NumberFormatException ex) {
            System.err.println("Invalid Format!");
        } catch (IOException ex) {
            System.err.println("Invalid Format!");
        }
        return re - 1;
    }

    static public int selectColorsNodes() {
        int re = -1;
        System.out.println("\nColors of the Nodes: [1-3]");

        System.out.println("\t1) - Default.");
        System.out.println("\t2) - Custom Defined.");
        System.out.println("\t3) - Based on the item relevance.");

        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            int i = Integer.parseInt(br1.readLine());
            if (i > 3 || i < 1) {
                System.err.println("Insert a value between 1-3.");
            } else {
                re = i;
            }
        } catch (NumberFormatException ex) {
            System.err.println("Invalid Format!");
        } catch (IOException ex) {
            System.err.println("Invalid Format!");
        }
        return re - 1;
    }
    
    static public int selectColorsEdges() {
        int re = -1;
        System.out.println("\nColors of the Edges: [1-3]");

        System.out.println("\t1) - Default.");
        System.out.println("\t2) - Custom Defined.");
        System.out.println("\t3) - Based on the similarity.");

        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            int i = Integer.parseInt(br1.readLine());
            if (i > 3 || i < 1) {
                System.err.println("Insert a value between 1-3.");
            } else {
                re = i;
            }
        } catch (NumberFormatException ex) {
            System.err.println("Invalid Format!");
        } catch (IOException ex) {
            System.err.println("Invalid Format!");
        }
        return re - 1;
    }

    static public int selectColorsTags() {
        int re = -1;
        System.out.println("\nColors of the Tags: [1-2]");

        System.out.println("\t1) - Default.");
        System.out.println("\t2) - Custom Defined.");

        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            int i = Integer.parseInt(br1.readLine());
            if (i > 2 || i < 1) {
                System.err.println("Insert a value between 1-2.");
            } else {
                re = i;
            }
        } catch (NumberFormatException ex) {
            System.err.println("Invalid Format!");
        } catch (IOException ex) {
            System.err.println("Invalid Format!");
        }
        return re - 1;
    }

    private int selectExecution() {
        int re = -1;
        System.out.println("\nType the model of the execution: [1-3]");

        System.out.println("\t1) - One similarity measure execution");
        System.out.println("\t2) - All similarities measure execution");
        System.out.println("\t3) - All similarities measure execution (Only Graphic)");

        try {
            int i = Integer.parseInt(br.readLine());
            if (i > 3 || i < 1) {
                System.err.println("Insert a value between 1-3.");
            } else {
                re = i;
            }
        } catch (NumberFormatException ex) {
            System.err.println("Invalid Format!");
        } catch (IOException ex) {
            System.err.println("Invalid Format!");
        }
        return re - 1;
    }

    private int selectDataset() {
        int re = -1;
        System.out.println("\nChoose a dataset: [1-5]");

        for (DatasetToRead dtr : DatasetToRead.values()) {
            System.out.println("\t" + (dtr.ordinal() + 1) + ") - " + dtr.name());
        }
        System.out.println("\t6) - External dataset file");

        try {
            int i = Integer.parseInt(br.readLine());
            if (i == 6) {
                re = 100;
            } else if (i > 5 || i < 1) {
                System.err.println("Insert a value between 1-5.");
            } else {
                re = i;
            }
        } catch (NumberFormatException ex) {
            System.err.println("Invalid Format!");
        } catch (IOException ex) {
            System.err.println("Invalid Format!");
        }
        return re - 1;
    }

    private int selectSimilarityMeasure() {
        int re = -1;
        int position = 0;
        System.out.println("\nChoose a similarity measure: [1-17]");

        for (SimilarityMeasureFinal sMF : listSM) {
            System.out.println("\t" + (position + 1) + ") - " + sMF.getName());
            position++;
        }

        try {
            int i = Integer.parseInt(br.readLine());
            if (i > 17 || i < 1) {
                System.err.println("Insert a value between 1-17.");
            } else {
                re = i;
            }
        } catch (NumberFormatException ex) {
            System.err.println("Invalid Format!");
        } catch (IOException ex) {
            System.err.println("Invalid Format!");
        }
        return re - 1;
    }

    private String loadFilePathMenu() {
        System.out.println("Write the full path to the dataset (fields must be sperated with blanks): ");
        try {
            String DsFilePath = br.readLine();
            File f = new File(DsFilePath);
            if (!f.exists()) {
                System.out.println("The path to the dataset is not correct. ");
                return loadFilePathMenu();
            }
            return DsFilePath;
        } catch (IOException ex) {
            System.err.println("Invalid Format!");
        }
        return loadFilePathMenu();
    }

}
