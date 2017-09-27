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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public final class Rank_matrix {

    private DoubleMatrix rank_matrix = null;
    private File file;

    /**
     * Getter of the rank matrix.
     *
     * @return DoubleMatrix the rank matrix
     */
    public DoubleMatrix getRank_matrix() {
        return rank_matrix;
    }

    /**
     * Setter of the rank matrix
     *
     * @param rank_matrix DoubleMatrix with the rank matrix
     */
    public void setRank_matrix(DoubleMatrix rank_matrix) {
        this.rank_matrix = rank_matrix;
    }

    /**
     * Getter of the File name to save the rank matrix
     *
     * @return String File name
     */
    public String getFile_name() {
        return file.getName();
    }

    /**
     * Create the rank matrix from a file.
     *
     * @param file The file with the rank matrix.
     * @throws Exception Different exceptions can be throws here with the
     * generation of the rank matrix.
     */
    public Rank_matrix(File file, Dataset_To_Read DtR) throws Exception {
        this.file = file;
        if (Rank_matrix.this.has_rank_matrix()) {
            Rank_matrix.this.load_rank_matrix();
            System.out.println("Matrix:: " + this.rank_matrix.rows + " X " + this.rank_matrix.columns);
        } else {
            switch (DtR) {
                case NetFlix:
                    load_file_nf();
                    break;
                case Movielens:
                    load_file_ml1m();
                    break;
                case Filmtrus:
                    load_file_filmtrust();
                    break;
                case Jester:
                    load_file_jester();
                    break;
                case BookCrossing:
                    load_file_books();
                    break;
            }
            Rank_matrix.this.save_rank_matrix();
        }
    }

    /**
     * Load dates from file movilens_1Mb
     *
     * @throws Exception Different exceptions can be throws here with the load
     * of the movilens_1Mb file.
     */
    private void load_file_ml1m() throws Exception {
        System.out.println("load from file movilens_1Mb");
        int user = 0;
        int item = 0;
        double rank = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            int max_user = 0;
            int min_user = Integer.MAX_VALUE;
            int max_item = 0;
            int min_item = Integer.MAX_VALUE;
            double max_value = 0;
            double min_value = 999;

            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[ |\t]");
                user = Integer.parseInt(aline[0]);
                item = Integer.parseInt(aline[1]);
                rank = Double.parseDouble(aline[2]);

                max_user = (max_user > user) ? max_user : user;
                min_user = (min_user < user) ? min_user : user;
                max_item = (max_item > item) ? max_item : item;
                min_item = (min_item < item) ? min_item : item;
                max_value = (max_value > rank) ? max_value : rank;
                min_value = (min_value < rank) ? min_value : rank;
            }
            System.out.println("Matrix:: " + max_user + " X " + max_item);
            System.out.println("Matrix:: " + min_user + " X " + min_item);
            System.out.println("MatrixVAL:: " + min_value + " -- " + max_value);
            Runtime runtime = Runtime.getRuntime();
            System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());
            rank_matrix = new DoubleMatrix(max_user, max_item);

            rank_matrix.fill(-1);

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[ |\t]");
                user = Integer.parseInt(aline[0]) - 1;
                item = Integer.parseInt(aline[1]) - 1;
                rank = Double.parseDouble(aline[2]);
                rank_matrix.put(user, item, rank);

            }
            System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception:: " + e.getMessage());
            System.out.println("R: " + user + " -- " + item + " -- " + rank);
        }
    }

    /**
     * Load dates from file filmtrust
     *
     * @throws Exception Different exceptions can be throws here with the load
     * of the filmtrust file.
     */
    private void load_file_filmtrust() throws Exception {
        System.out.println("load from file filmtrust");
        int user = 0;
        int item = 0;
        double rank = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            int max_user = 0;
            int min_user = Integer.MAX_VALUE;
            int max_item = 0;
            int min_item = Integer.MAX_VALUE;
            double max_value = 0;
            double min_value = 999;

            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[ |\t]");
                user = Integer.parseInt(aline[0]);
                item = Integer.parseInt(aline[1]);
                rank = Double.parseDouble(aline[2]);

                max_user = (max_user > user) ? max_user : user;
                min_user = (min_user < user) ? min_user : user;
                max_item = (max_item > item) ? max_item : item;
                min_item = (min_item < item) ? min_item : item;
                max_value = (max_value > rank) ? max_value : rank;
                min_value = (min_value < rank) ? min_value : rank;
            }
            System.out.println("Matrix:: " + max_user + " X " + max_item);
            System.out.println("Matrix:: " + min_user + " X " + min_item);
            System.out.println("MatrixVAL:: " + min_value + " -- " + max_value);
            Runtime runtime = Runtime.getRuntime();
            System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());
            rank_matrix = new DoubleMatrix(max_user, max_item);

            rank_matrix.fill(-1);

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[ |\t]");
                user = Integer.parseInt(aline[0]) - 1;
                item = Integer.parseInt(aline[1]) - 1;
                rank = Double.parseDouble(aline[2]) + 1;
                rank_matrix.put(user, item, rank);

            }
            System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception:: " + e.getMessage());
            System.out.println("R: " + user + " -- " + item + " -- " + rank);
        }
    }

    /**
     * Load dates from file netflix
     *
     * @throws Exception Different exceptions can be throws here with the load
     * of the netflix file.
     */
    private void load_file_nf() throws Exception {
        System.out.println("load from file netflix");
        int user = 0;
        int item = 0;
        double rank = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            int max_user = 0;
            int min_user = Integer.MAX_VALUE;
            int max_item = 0;
            int min_item = Integer.MAX_VALUE;
            double max_value = 0;
            double min_value = 999;

            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[ |\t]");
                user = Integer.parseInt(aline[0]);
                item = Integer.parseInt(aline[1]);
                rank = Double.parseDouble(aline[2]);

                max_user = (max_user > user) ? max_user : user;
                min_user = (min_user < user) ? min_user : user;
                max_item = (max_item > item) ? max_item : item;
                min_item = (min_item < item) ? min_item : item;
                max_value = (max_value > rank) ? max_value : rank;
                min_value = (min_value < rank) ? min_value : rank;
            }
            max_user++;
            max_item++;
            if (max_user > 50000) {
                max_user = 50000;
            }
            if (max_item > 5000) {
                max_item = 5000;
            }
            System.out.println("Matrix:: " + max_user + " X " + max_item);
            System.out.println("Matrix:: " + min_user + " X " + min_item);
            System.out.println("MatrixVAL:: " + min_value + " -- " + max_value);
            Runtime runtime = Runtime.getRuntime();
            System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());
            rank_matrix = new DoubleMatrix(max_user, max_item);

            rank_matrix.fill(-1);

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[ |\t]");
                user = Integer.parseInt(aline[0]);
                item = Integer.parseInt(aline[1]);
                rank = Double.parseDouble(aline[2]);
                if (item < max_item && user < max_user) {
                    rank_matrix.put(user, item, rank);
                }
            }
            System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception:: " + e.getMessage());
            System.out.println("R: " + user + " -- " + item + " -- " + rank);
        }
    }

    /**
     * Load dates from file bookcrossing
     *
     * @throws Exception Different exceptions can be throws here with the load
     * of the bookcrossing file.
     */
    private void load_file_books() throws Exception {
        System.out.println("load from file bookcrossing");
        int user = 0;
        int item = 0;
        ArrayList<String> conversorISBN = new ArrayList<>();
        ArrayList<String> conversorUser = new ArrayList<>();
        double rank = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            int max_user = 0;
            int min_user = Integer.MAX_VALUE;
            int max_item = 0;
            int min_item = Integer.MAX_VALUE;
            double max_value = 0;
            double min_value = 999;

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

                max_user = (max_user > user) ? max_user : user;
                min_user = (min_user < user) ? min_user : user;
                max_item = (max_item > item) ? max_item : item;
                min_item = (min_item < item) ? min_item : item;
                max_value = (max_value > rank) ? max_value : rank;
                min_value = (min_value < rank) ? min_value : rank;
            }
            max_user++;
            max_item++;
            if (max_user > 50000) {
                max_user = 50000;
            }
            if (max_item > 5000) {
                max_item = 5000;
            }
            System.out.println("Matrix MAX:: " + max_user + " X " + max_item);
            System.out.println("Matrix MIN:: " + min_user + " X " + min_item);
            System.out.println("Matrix VAL:: " + min_value + " -- " + max_value);
            Runtime runtime = Runtime.getRuntime();
            System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());
            rank_matrix = new DoubleMatrix(max_user, max_item);

            rank_matrix.fill(-1);

            br = new BufferedReader(new FileReader(file));

            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[;]");

                String User = aline[0].replace('"', ' ').trim();
                user = conversorUser.indexOf(User);

                String ISBN = aline[1].replace('"', ' ').trim();
                item = conversorISBN.indexOf(ISBN);

                rank = (Double.parseDouble(aline[2].replace('"', ' ').trim()) + 2) / 2; //pongo los valores entre 0 y 6
                if (item < max_item && user < max_user) {
                    rank_matrix.put(user, item, rank);
                }

            }
            System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception:: " + e.getMessage());
            System.out.println("R: " + user + " -- " + item + " -- " + rank);
        }
    }

    /**
     * Load dates from file jester
     *
     * @throws Exception Different exceptions can be throws here with the load
     * of the jester file.
     */
    private void load_file_jester() throws Exception {
        System.out.println("load from file jester");
        int user = 0;
        int item = 0;
        double rank = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            int max_user = 0;
            int max_item = 0;

            int nlinea = 0;
            while ((line = br.readLine()) != null) {
                String[] aline = line.trim().split("[;]");

                nlinea++;
                max_item = aline.length - 1;
            }
            max_user = nlinea;

            System.out.println("Matrix MAX:: " + max_user + " X " + max_item);
            Runtime runtime = Runtime.getRuntime();
            System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());
            rank_matrix = new DoubleMatrix(max_user, max_item);

            rank_matrix.fill(-1);

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
                    rank_matrix.put(user, item, rank);
                }
                nlinea++;
            }
            System.out.println("MEMORY:: " + runtime.maxMemory() + " - " + runtime.totalMemory() + " - " + runtime.freeMemory());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception:: " + e.getMessage());
            System.out.println("R: " + user + " -- " + item + " -- " + rank);
        }
    }

    private void save_rank_matrix() {
        System.out.println("save rank matrix..");
        File f = new File("./data/" + file.getName());
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            rank_matrix.save("./data/" + file.getName() + "/votos_matrix.dat");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Save to disk the rank matrix in the subfolder with name FC_name
     *
     * @param FC_name String The name of the similarity function to use.
     */
    public void save_rank_matrix(String FC_name) {
        System.out.println("save rank_matrix..");
        File f = new File("./data/" + file.getName() + "/" + FC_name);
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            rank_matrix.save("./data/" + file.getName() + "/" + FC_name + "/votos_matrix.dat");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void load_rank_matrix() {
        System.out.println("load rank matrix..");
        try {
            if (rank_matrix == null) {
                rank_matrix = new DoubleMatrix();
                rank_matrix.load("./data/" + file.getName() + "/votos_matrix.dat");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Excepcion (load rank matrix): " + e.getMessage());
        }
    }

    /**
     * Load from disk the rank matrix of the subfolder with name FC_name
     *
     * @param FC_name String The name of the similarity function to use.
     */
    public void load_rank_matrix(String FC_name) {
        System.out.println("load rank matrix..");
        try {
            rank_matrix = new DoubleMatrix();
            rank_matrix.load("./data/" + file.getName() + "/" + FC_name + "/votos_matrix.dat");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Excepcion (load_matrices_votos_calculadas): " + e.getMessage());
        }
    }

    private boolean has_rank_matrix() {
        File f = new File("./data/" + file.getName() + "/votos_matrix.dat");
        return f.exists();
    }

    /**
     * Ask if the file with the rank matrix is saved in the subfolder with name
     * FC_name
     *
     * @param FC_name String The name of the similarity function to use.
     * @return boolean Return true if the file with the rank matrix is saved in
     * the subfolder with name FC_name
     */
    public boolean has_rank_matrix(String FC_name) {
        File f = new File("./data/" + file.getName() + "/" + FC_name + "/votos_matrix.dat");
        return f.exists();
    }

    /**
     * Enumerate with the type of file to read
     */
    public enum Dataset_To_Read {

        /**
         * The datasert is from NetFlix
         */
        NetFlix,
        /**
         * The datasert is from Movielens
         */
        Movielens,
        /**
         * The datasert is from BookCrossing
         */
        BookCrossing,
        /**
         * The dataserte is from filmtrus
         */
        Filmtrus,
        /**
         * The datasert is from jester
         */
        Jester
    }

}
