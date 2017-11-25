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

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.EdgeDirection;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.openord.OpenOrdLayout;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.DependantColor;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import static org.etsisi.visualrs.io.Exports.TypeFileExport.*;
import org.etsisi.visualrs.models.MaximumSpanningTreeMatrix;
//import org.etsisi.visualrs.matrices.DoubleMatrix;
import org.jblas.DoubleMatrix;

/**
 * This class generates the graphics about a maximum spanning tree matrix
 *
 * @author Sergio Gil Borras
 * @version 1.0 - August 2017
 * @see "Related to article 'Tree graph visualization of recommender systems
 * related information'"
 */
public class Exports {

    DoubleMatrix primMatrix = null;
    GraphModel graphModel = null;
    PreviewModel model = null;
    UndirectedGraph undirectedGraph = null;

    String correlationName;
    String fileName;

    TypeFileExport TFE;

    /**
     * Constructor of the class
     *
     * @param MRM the maximum spanning tree matrix
     * @throws Exception When the param MRM (Maximum spanning tree matrix) is
     * null
     */
    public Exports(MaximumSpanningTreeMatrix MRM) throws Exception {
        this.correlationName = MRM.getSimilarityMeasureName();
        this.fileName = MRM.getFileName();
        primMatrix = MRM.getMaximumSpanningTreeMatrix();

    }

    /**
     * Generate a graphics of the type file selected
     *
     * @param TFE Enum with type file to export
     * @throws Exception Different exceptions can be throws here with the
     * generation of the graphics
     */
    public void execute(TypeFileExport TFE) throws Exception {
        this.TFE = TFE;
        System.out.println("--:: Grafico " + correlationName + " to " + TFE.name() + "::--");

        if (TFE == CSV) {
            saveGephiCSV();
        } else if (TFE == PDF || TFE == PNG || TFE == SVG) {
            saveGephiGrafico();
        }

    }

    /**
     * Generate CSV file with the adyacent list of the tree
     *
     * @param TFE Enum with type file to export
     * @throws Exception
     */
    private void saveGephiCSV() {

        File sFile = new File("./data/" + fileName + "/" + correlationName + "/gephi_" + correlationName + ".csv");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sFile));
            boolean entra = false;
            for (int item = 0; item < primMatrix.columns; item++) {
                entra = false;
                double[] ColumItem = primMatrix.getColumn(item).toArray();
                for (int item1 = 0; item1 < primMatrix.rows; item1++) {
                    if (ColumItem[item1] >= -1) {
                        if (!entra) {
                            bw.write(item + ";");
                            entra = true;
                        }
                        bw.write(item1 + ";");
                    }
                }
                bw.write("\r\n");
            }
            bw.close();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Generate a graphics of the type file selected
     *
     * @throws Exception
     */
    private void saveGephiGrafico() throws Exception {
        if (undirectedGraph == null) {
            ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
            pc.newProject();
            Workspace workspace = pc.getCurrentWorkspace();

            //Get a graph model - it exists because we have a workspace
            graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);
            model = Lookup.getDefault().lookup(PreviewController.class).getModel();
            undirectedGraph = graphModel.getUndirectedGraph();

            for (int i = 0; i < primMatrix.columns; i++) {
                //Create  nodes
                Node n0 = graphModel.factory().newNode("n" + i);
                n0.setLabel("No" + i);
                n0.setSize(1f);
                n0.setAlpha(0.5f);
                n0.setColor(Color.BLACK);
                undirectedGraph.addNode(n0);
            }
            for (int i = 0; i < primMatrix.columns; i++) {
                for (int ii = i + 1; ii < primMatrix.rows; ii++) {
                    double value = primMatrix.get(i, ii);
                    if (value >= -1) {
                        value = 1;
                        Edge e0 = graphModel.factory().newEdge(undirectedGraph.getNode("n" + i), undirectedGraph.getNode("n" + ii), EdgeDirection.UNDIRECTED.ordinal(), value, false);
                        undirectedGraph.addEdge(e0);
                    }
                }
            }

            for (Node n : undirectedGraph.getNodes().toArray()) {
                if (undirectedGraph.getDegree(n) == 0) {
                    undirectedGraph.removeNode(n);
                }
            }

            //Count nodes and edges
            System.out.println("Nodes: " + undirectedGraph.getNodeCount() + " Edges: " + undirectedGraph.getEdgeCount());

            //Iterate over nodes
//        for (Node n : undirectedGraph.getNodes()) {
//            Node[] neighbors = undirectedGraph.getNeighbors(n).toArray();
//            System.out.println(n.getLabel() + " has " + neighbors.length + " neighbors");
//        }
            //Iterate over edges
//        for (Edge e : undirectedGraph.getEdges()) {
//            System.out.println(e.getSource().getId() + " -> " + e.getTarget().getId());
//        }
            OpenOrdLayout firstLayout = new OpenOrdLayout(null);
            firstLayout.setGraphModel(graphModel);
            firstLayout.resetPropertiesValues();
            firstLayout.setNumIterations(750);
            firstLayout.setNumThreads(3);
            firstLayout.setEdgeCut(0.8f);
            firstLayout.setRealTime(0.2f);
            firstLayout.setRandSeed(-9139240299543417142l);

            firstLayout.setCooldownStage(15);
            firstLayout.setCrunchStage(25);
            firstLayout.setExpansionStage(25);
            firstLayout.setLiquidStage(25);
            firstLayout.setSimmerStage(10);

            firstLayout.initAlgo();

            for (int i = 0; i < 300 && firstLayout.canAlgo(); i++) {
                firstLayout.goAlgo();
            }
            firstLayout.endAlgo();

            ForceAtlas2 secondLayout = new ForceAtlas2(null);
            secondLayout.setGraphModel(graphModel);
            secondLayout.resetPropertiesValues();
            secondLayout.setGravity(1.0);
            secondLayout.setJitterTolerance(1.0);
            secondLayout.setBarnesHutTheta(1.2);
            secondLayout.setScalingRatio(2.0);
            secondLayout.setThreadsCount(3);

            secondLayout.initAlgo();

            for (int i = 0; i < 300 && secondLayout.canAlgo(); i++) {
                secondLayout.goAlgo();
            }
            secondLayout.endAlgo();

            YifanHuLayout thirdLayout = new YifanHuLayout(null, new StepDisplacement(1f));
            thirdLayout.setGraphModel(graphModel);
            thirdLayout.resetPropertiesValues();
            thirdLayout.setBarnesHutTheta(1.2f);
            thirdLayout.setInitialStep(20f);
            thirdLayout.setOptimalDistance(100f);
            thirdLayout.setQuadTreeMaxLevel(10);
            thirdLayout.setRelativeStrength(0.2f);
            thirdLayout.setStepRatio(0.95f);
            thirdLayout.setConvergenceThreshold(0.0001f);
            thirdLayout.setAdaptiveCooling(true);

            thirdLayout.initAlgo();

            for (int i = 0; i < 200 && thirdLayout.canAlgo(); i++) {
                thirdLayout.goAlgo();
            }
            thirdLayout.endAlgo();

            model.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
            model.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS, Boolean.TRUE);
            model.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(Color.BLACK));
            model.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, 0.3f);
            model.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, model.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(12f));
            model.getProperties().putValue(PreviewProperty.NODE_LABEL_PROPORTIONAL_SIZE, Boolean.TRUE);
            model.getProperties().putValue(PreviewProperty.NODE_BORDER_WIDTH, 1);
            model.getProperties().putValue(PreviewProperty.NODE_BORDER_COLOR, new DependantColor(Color.BLACK));
        }
//Export
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        try {
            if (TFE == PDF) {
                ec.exportFile(new File("./data/" + fileName + "/" + correlationName + "/gephi_" + correlationName + ".pdf"));
            } else if (TFE == PNG) {
                ec.exportFile(new File("./data/" + fileName + "/" + correlationName + "/gephi_" + correlationName + ".png"));
            } else if (TFE == SVG) {
                ec.exportFile(new File("./data/" + fileName + "/" + correlationName + "/gephi_" + correlationName + ".svg"));
            }
        } catch (IOException ex) {
            //ex.printStackTrace();
            return;
        }
    }

    /**
     * Enumerate with the type of file to export
     */
    public enum TypeFileExport {

        /**
         * Type of file like a Adobe PDF
         */
        PDF,
        /**
         * Type of file like a image PNG
         */
        PNG,
        /**
         * Type of file like a adyacent list in CSV format
         */
        CSV,
        /**
         * Type of file like a image SVG
         */
        SVG
    }

}
