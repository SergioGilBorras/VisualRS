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
package org.etsisi.visualrs.similarityMeasureBase;

import java.util.Arrays;

/**
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems related information'"
 */
public class MMD extends SimilarityMeasureBase {

    @Override
    public double calculate(double[] a1, double[] a2) {
        if (a1 == null || a2 == null || a1.length != a2.length) {
            throw new IllegalArgumentException("A parameter is null, or the pameters don't have the same lenght.");
        }
        int[] nVotosA1 = new int[7];
        Arrays.fill(nVotosA1, 0);
        int[] nVotosA2 = new int[7];
        Arrays.fill(nVotosA2, 0);
        for (int i = 0; i < a1.length; i++) {
            if (a1[i] == -1) {
                a1[i] = 0;
            }
            if (a2[i] == -1) {
                a2[i] = 0;
            }
            nVotosA1[(int) Math.round(a1[i])]++;
            nVotosA2[(int) Math.round(a2[i])]++;
        }
        double dv1 = (a1.length - nVotosA1[0]);
        double carA1 = 0;
        if (dv1 != 0) {
            carA1 = 1 / dv1;
        }
        double dv2 = (a2.length - nVotosA2[0]);
        double carA2 = 0;
        if (dv2 != 0) {
            carA2 = 1 / dv2;
        }
        double votos = 0;
        for (int i = 1; i < 6; i++) {
            votos += Math.pow(nVotosA1[i] - nVotosA2[i], 2) - carA1 - carA2;
        }

        votos /= 5;
        if (votos == -1) {
            return 0;
        }
        double res = 1 / (1 + votos);

        return res;
    }

}
