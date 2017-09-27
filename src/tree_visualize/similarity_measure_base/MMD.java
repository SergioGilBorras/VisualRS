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
package tree_visualize.similarity_measure_base;

import java.util.Arrays;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'" 
 */
public class MMD extends Similarity_measure_base {

    @Override
    public double calculate(double[] a1, double[] a2) {
        if (a1 == null || a2 == null || a1.length != a2.length) {
            throw new IllegalArgumentException("Uno de los parametros es null. O no tienen la misma longitud");
        }
        int[] n_votos_a1 = new int[7];
        Arrays.fill(n_votos_a1, 0);
        int[] n_votos_a2 = new int[7];
        Arrays.fill(n_votos_a2, 0);
        for (int i = 0; i < a1.length; i++) {
            if (a1[i] == -1) {
                a1[i] = 0;
            }
            if (a2[i] == -1) {
                a2[i] = 0;
            }
            n_votos_a1[(int) Math.round(a1[i])]++;
            n_votos_a2[(int) Math.round(a2[i])]++;
            //System.out.println("RAS:: " + (int) Math.round(a1[i]) + " -- " + (int) Math.round(a2[i]));
        }
        double dv1 = (a1.length - n_votos_a1[0]);
        double car_a1 = 0;
        if (dv1 != 0) {
            car_a1 = 1 / dv1;
        }
        double dv2 = (a2.length - n_votos_a2[0]);
        double car_a2 = 0;
        if (dv2 != 0) {
            car_a2 = 1 / dv2;
        }
        double votos = 0;
        for (int i = 1; i < 6; i++) {
            votos += Math.pow(n_votos_a1[i] - n_votos_a2[i], 2) - car_a1 - car_a2;
        }
//        if (votos > 1 || votos < -1) {
//            System.out.println("RASTA-------voto:: " + votos);
//        }
        votos /= 5;
        if (votos == -1) {
            return 0;
        }
        double res = 1 / (1 + votos);
        if (res == Double.POSITIVE_INFINITY || res == Double.NEGATIVE_INFINITY) {
            System.out.println("RAS-VOTO:: " + res + " -- " + votos);
        }
        return res;

    }

}
