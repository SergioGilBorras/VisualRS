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
package org.etsisi.visualrs.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import org.etsisi.visualrs.matrices.DoubleMatrix;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public final class LoadData {

    private DoubleMatrix rankMatrix = null;
    private File file;

    /**
     * Getter of the rank matrix.
     *
     * @return DoubleMatrix the rank matrix
     */
    public DoubleMatrix getRankMatrix() {
        return rankMatrix;
    }

    /**
     * Setter of the rank matrix
     *
     * @param rankMatrix DoubleMatrix with the rank matrix
     */
    public void setRankMatrix(DoubleMatrix rankMatrix) {
        this.rankMatrix = rankMatrix;
    }

    /**
     * Getter of the File name to save the rank matrix
     *
     * @return String File name
     */
    public String getFileName() {
        return file.getName();
    }

    /**
     * Create the rank matrix from a file.
     *
     * @param file The file with the rank matrix.
     * @param DtR Enum with the dataset to read
     * @throws Exception Different exceptions can be throws here with the
     * generation of the rank matrix.
     */
    public LoadData(File file, DatasetToRead DtR) throws Exception {
        this.file = file;
        if (hasRankMatrix()) {
            loadRankMatrix();
            System.out.println("Matrix size: " + this.rankMatrix.rows + " rows * " + this.rankMatrix.columns + " colums");
        } else {
            switch (DtR) {
                case Netflix:
                    loadFileNf();
                    break;
                case MovieLens:
                    loadFileMl1m();
                    break;
                case FilmTrust:
                    loadFileFilmTrust();
                    break;
                case Jester:
                    loadFileJester();
                    break;
                case BookCrossing:
                    loadFileBooks();
                    break;
            }
            LoadData.this.saveRankMatrix();
        }
    }

    /**
     * Load dates from file movilens 1Mb
     *
     * @throws Exception Different exceptions can be throws here with the load
     * of the movilens 1Mb file.
     */
    private void loadFileMl1m() throws Exception {
        System.out.print("\nLoading MovieLens 1M dataset...");
        int user = 0;
        int item = 0;
        double rank = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            int maxUser = 0;
            int minUser = Integer.MAX_VALUE;
            int maxItem = 0;
            int minItem = Integer.MAX_VALUE;
            double maxValue = 0;
            double minValue = 999;

            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[ |\t]");
                user = Integer.parseInt(aline[0]);
                item = Integer.parseInt(aline[1]);
                rank = Double.parseDouble(aline[2]);

                maxUser = (maxUser > user) ? maxUser : user;
                minUser = (minUser < user) ? minUser : user;
                maxItem = (maxItem > item) ? maxItem : item;
                minItem = (minItem < item) ? minItem : item;
                maxValue = (maxValue > rank) ? maxValue : rank;
                minValue = (minValue < rank) ? minValue : rank;
            }
            //System.out.println("Matrix:: " + maxUser + " X " + maxItem);
            //System.out.println("Matrix:: " + minUser + " X " + minItem);
            //System.out.println("MatrixVAL:: " + minValue + " -- " + maxValue);
            //Runtime runtime = Runtime.getRuntime();
            //System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());
            rankMatrix = new DoubleMatrix(maxUser, maxItem);

            rankMatrix.fill(-1);

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[ |\t]");
                user = Integer.parseInt(aline[0]) - 1;
                item = Integer.parseInt(aline[1]) - 1;
                rank = Double.parseDouble(aline[2]);
                rankMatrix.put(user, item, rank);

            }

            System.out.println(" Done.");
            //System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());

        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Exception:: " + e.getMessage());
            //System.out.println("R: " + user + " -- " + item + " -- " + rank);
        }
    }

    /**
     * Load dates from file filmtrust
     *
     * @throws Exception Different exceptions can be throws here with the load
     * of the filmtrust file.
     */
    private void loadFileFilmTrust() throws Exception {
        System.out.print("\nLoading FilmTrust dataset...");
        int user = 0;
        int item = 0;
        double rank = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            int maxUser = 0;
            int minUser = Integer.MAX_VALUE;
            int maxItem = 0;
            int minItem = Integer.MAX_VALUE;
            double maxValue = 0;
            double minValue = 999;

            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[ |\t]");
                user = Integer.parseInt(aline[0]);
                item = Integer.parseInt(aline[1]);
                rank = Double.parseDouble(aline[2]);

                maxUser = (maxUser > user) ? maxUser : user;
                minUser = (minUser < user) ? minUser : user;
                maxItem = (maxItem > item) ? maxItem : item;
                minItem = (minItem < item) ? minItem : item;
                maxValue = (maxValue > rank) ? maxValue : rank;
                minValue = (minValue < rank) ? minValue : rank;
            }
            //System.out.println("Matrix:: " + maxUser + " X " + maxItem);
            //System.out.println("Matrix:: " + minUser + " X " + minItem);
            //System.out.println("MatrixVAL:: " + minValue + " -- " + maxValue);
            //Runtime runtime = Runtime.getRuntime();
            //System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());
            rankMatrix = new DoubleMatrix(maxUser, maxItem);

            rankMatrix.fill(-1);

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[ |\t]");
                user = Integer.parseInt(aline[0]) - 1;
                item = Integer.parseInt(aline[1]) - 1;
                rank = Double.parseDouble(aline[2]) + 1;
                rankMatrix.put(user, item, rank);

            }

            System.out.println(" Done.");
            //System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());

        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Exception: " + e.getMessage());
            //System.out.println("R: " + user + " -- " + item + " -- " + rank);
        }
    }

    /**
     * Load dates from file netflix
     *
     * @throws Exception Different exceptions can be throws here with the load
     * of the netflix file.
     */
    private void loadFileNf() throws Exception {
        System.out.print("\nLoading Netflix dataset...");
        int user = 0;
        int item = 0;
        double rank = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            int maxUser = 0;
            int minUser = Integer.MAX_VALUE;
            int maxItem = 0;
            int minItem = Integer.MAX_VALUE;
            double maxValue = 0;
            double minValue = 999;

            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[ |\t]");
                user = Integer.parseInt(aline[0]);
                item = Integer.parseInt(aline[1]);
                rank = Double.parseDouble(aline[2]);

                maxUser = (maxUser > user) ? maxUser : user;
                minUser = (minUser < user) ? minUser : user;
                maxItem = (maxItem > item) ? maxItem : item;
                minItem = (minItem < item) ? minItem : item;
                maxValue = (maxValue > rank) ? maxValue : rank;
                minValue = (minValue < rank) ? minValue : rank;
            }
            maxUser++;
            maxItem++;
//            if (maxUser > 50000) {
//                maxUser = 50000;
//            }
//            if (maxItem > 5000) {
//                maxItem = 5000;
//            }
            //System.out.println("Matrix:: " + maxUser + " X " + maxItem);
            //System.out.println("Matrix:: " + minUser + " X " + minItem);
            //System.out.println("MatrixVAL:: " + minValue + " -- " + maxValue);
            //Runtime runtime = Runtime.getRuntime();
            //System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());
            rankMatrix = new DoubleMatrix(maxUser, maxItem);

            rankMatrix.fill(-1);

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[ |\t]");
                user = Integer.parseInt(aline[0]);
                item = Integer.parseInt(aline[1]);
                rank = Double.parseDouble(aline[2]);
                if (item < maxItem && user < maxUser) {
                    rankMatrix.put(user, item, rank);
                }
            }

            System.out.println(" Done.");
            //System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());

        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Exception: " + e.getMessage());
            //System.out.println("R: " + user + " -- " + item + " -- " + rank);
        }
    }

    /**
     * Load dates from file bookcrossing
     *
     * @throws Exception Different exceptions can be throws here with the load
     * of the bookcrossing file.
     */
    private void loadFileBooks() throws Exception {
        System.out.print("\nLoading BookCrossing dataset...");
        int user = 0;
        int item = 0;
        ArrayList<String> conversorISBN = new ArrayList<>();
        ArrayList<String> conversorUser = new ArrayList<>();
        double rank = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            int maxUser = 0;
            int minUser = Integer.MAX_VALUE;
            int maxItem = 0;
            int minItem = Integer.MAX_VALUE;
            double maxValue = 0;
            double minValue = 999;

            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[;]");

                String User = aline[0].replace('"', ' ').trim();
                if (!conversorUser.contains(User)) {
                    conversorUser.add(User);
                }
                user = conversorUser.indexOf(User);

                String ISBN = aline[1].replace('"', ' ').trim();
                if (!conversorISBN.contains(ISBN)) {
                    conversorISBN.add(ISBN);
                }
                item = conversorISBN.indexOf(ISBN);

                rank = Double.parseDouble(aline[2].replace('"', ' ').trim());

                maxUser = (maxUser > user) ? maxUser : user;
                minUser = (minUser < user) ? minUser : user;
                maxItem = (maxItem > item) ? maxItem : item;
                minItem = (minItem < item) ? minItem : item;
                maxValue = (maxValue > rank) ? maxValue : rank;
                minValue = (minValue < rank) ? minValue : rank;
            }
            maxUser++;
            maxItem++;
//            if (maxUser > 50000) {
//                maxUser = 50000;
//            }
//            if (maxItem > 5000) {
//                maxItem = 5000;
//            }
            //System.out.println("Matrix MAX:: " + maxUser + " X " + maxItem);
            //System.out.println("Matrix MIN:: " + minUser + " X " + minItem);
            //System.out.println("Matrix VAL:: " + minValue + " -- " + maxValue);
            //Runtime runtime = Runtime.getRuntime();
            //System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());
            rankMatrix = new DoubleMatrix(maxUser, maxItem);

            rankMatrix.fill(-1);

            br = new BufferedReader(new FileReader(file));

            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[;]");

                String User = aline[0].replace('"', ' ').trim();
                user = conversorUser.indexOf(User);

                String ISBN = aline[1].replace('"', ' ').trim();
                item = conversorISBN.indexOf(ISBN);

                rank = (Double.parseDouble(aline[2].replace('"', ' ').trim()) + 2) / 2; //pongo los valores entre 0 y 6
                if (item < maxItem && user < maxUser) {
                    rankMatrix.put(user, item, rank);
                }

            }

            System.out.println(" Done.");
            //System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());

        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Exception: " + e.getMessage());
            //System.out.println("R: " + user + " -- " + item + " -- " + rank);
        }
    }

    /**
     * Load dates from file jester
     *
     * @throws Exception Different exceptions can be throws here with the load
     * of the jester file.
     */
    private void loadFileJester() throws Exception {
        System.out.print("\nLoading Jester dataset...");
        int user = 0;
        int item = 0;
        double rank = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            int maxUser = 0;
            int maxItem = 0;

            int nlinea = 0;
            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[;]");

                nlinea++;
                maxItem = aline.length - 1;
            }
            maxUser = nlinea;

            //System.out.println("Matrix MAX:: " + maxUser + " X " + maxItem);
            //Runtime runtime = Runtime.getRuntime();
            //System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());
            rankMatrix = new DoubleMatrix(maxUser, maxItem);

            rankMatrix.fill(-1);

            br = new BufferedReader(new FileReader(file));

            nlinea = 0;
            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[;]");

                user = nlinea;
                for (int i = 1; i < aline.length; i++) {
                    item = i - 1;
                    if (!aline[i].trim().equals("99")) {
                        rank = (Double.parseDouble(aline[i].replace(",", ".").trim()) + 14) / 4;
                    } else {
                        rank = -1;
                    }
                    rankMatrix.put(user, item, rank);
                }
                nlinea++;
            }

            System.out.println(" Done.");
            //System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());

        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Exception: " + e.getMessage());
            //System.out.println("R: " + user + " -- " + item + " -- " + rank);
        }
    }

    private void saveRankMatrix() {
        System.out.print("\nSaving rank matrix...");
        File f = new File("./data/" + file.getName());
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            rankMatrix.save("./data/" + file.getName() + "/votosMatrix.dat");
            System.out.println(" Done.");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Save to disk the rank matrix in the subfolder with name FCName
     *
     * @param FCName String The name of the similarity function to use.
     */
    public void saveRankMatrix(String FCName) {
        System.out.print("\nSaving rank matrix...");
        File f = new File("./data/" + file.getName() + "/" + FCName);
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            rankMatrix.save("./data/" + file.getName() + "/" + FCName + "/votosMatrix.dat");
            System.out.println(" Done.");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void loadRankMatrix() {
        System.out.print("Loading rank matrix...");
        try {
            if (rankMatrix == null) {
                rankMatrix = new DoubleMatrix();
                rankMatrix.load("./data/" + file.getName() + "/votosMatrix.dat");
            }
            System.out.println(" Done.");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Excepcion loading rank matrix: " + e.getMessage());
        }
    }

    /**
     * Load from disk the rank matrix of the subfolder with name FCName
     *
     * @param FCName String The name of the similarity function to use.
     */
    public void loadRankMatrix(String FCName) {
        System.out.print("Loading rank matrix...");
        try {
            rankMatrix = new DoubleMatrix();
            rankMatrix.load("./data/" + file.getName() + "/" + FCName + "/votosMatrix.dat");
            System.out.println(" Done.");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Excepcion loading rank matrix: " + e.getMessage());
        }
    }

    private boolean hasRankMatrix() {
        File f = new File("./data/" + file.getName() + "/votosMatrix.dat");
        return f.exists();
    }

    /**
     * Ask if the file with the rank matrix is saved in the subfolder with name
     * FCName
     *
     * @param FCName String The name of the similarity function to use.
     * @return boolean Return true if the file with the rank matrix is saved in
     * the subfolder with name FCName
     */
    public boolean hasRankMatrix(String FCName) {
        File f = new File("./data/" + file.getName() + "/" + FCName + "/votosMatrix.dat");
        return f.exists();
    }

    /**
     * Enumerate with the type of file to read
     */
    public enum DatasetToRead {

        /**
         * The datasert is from Netflix
         */
        Netflix,
        /**
         * The datasert is from MovieLens
         */
        MovieLens,
        /**
         * The datasert is from BookCrossing
         */
        BookCrossing,
        /**
         * The dataserte is from filmtrus
         */
        FilmTrust,
        /**
         * The datasert is from jester
         */
        Jester
    }

}
