# Visual-RS 2017

1. [Installation](#installation)
2. [Getting Started](#getting-started)
3. [Package Structure](#package-structure)
4. [Main Classes](#main-classes)
5. [Extending VisualRS](#extending-visualrs)
6. [Test Example](#test-example)
7. [Datasets](#datasets)

## Summary

VisualRS is an object oriented Java framework that allows:

1. To relate items or users from recommender systems.
2. To provide tree graph structures containing the most relevant relationships between items or between users.
3. To create visualizations, using Gephi, of the resulting tree graphs.
4. To select the tree graph structures that allow a more balanced visualization and an easier navigation.
5. To choose between an assortment of collaborative filtering similarity measures.
6. To measure the quality of the results making use of centrality measures in graphs.
7. To easily embed new similarity metrics and quality measures.

VisualRS can be used:

1. As recommender systems analysis tool.
2. To provide users and technical managers with a visualization tool of items and users.
3. To facilitate research both into the collaborative filtering graphic representation and into the information navigation fields. It provides a broad basis for personalization and experimentation.

## Installation

### Download

This project is hosted in a Git repository. We can download it from the web:

```
https://github.com/SergioGilBorras/VisualRS
```

We can also clone it from Git using the command:

```
git clone https://github.com/SergioGilBorras/VisualRS.git
```

**Warning**: [`git-lfs`](https://git-lfs.github.com/) must be installed in order to download testing datasets.

### Maven

This Project has been developed using Maven. In order to compile and execute the Project, through maven, we will follow the following commands:

1. First, ensure JAVA_HOME environment variable is set and points to your JDK installation.
2. To install the Maven Project, downloading all its dependencies:

  ```
  mvn package
  ```
3.	To generate a Maven Web site where dependencies, plugins, summary, etc. are included.

  ```
  mvn site
  ```

4.	To create the Project’s API:

  ```
  mvn javadoc:javadoc
  ```

5.	To execute the Project, using Maven:

  ```
  mvn exec:java -Dexec.mainClass="org.etsisi.visualrs.test.Test"
  ```

### Java JAR

To use the library in other projects or to run our test application without Maven: we can [download a ZIP file](https://github.com/SergioGilBorras/VisualRS/releases), containing: a) The JAR of our already generated application, and b) The necessary libraries to use it.

The following command runs the test application:

```
java -jar mVisualRS-1.0.1.jar
```

## Getting Started

To use the library, we first load the selected dataset into the File class. We are using the provided datasets:

```Java
File myFile = new File (this.getClass().getResource("/datasets/FilmTrust.txt").toURI());
```

The dataset is then loaded into the data structure:

```Java
LoadData MV = new LoadData(myFile, DatasetToRead.FilmTrust);
```

Then, we will create the similarity matrix from: a) The class where the dataset (`LoadData`) is loaded, and b) A final similarity measure. In this case we have chosen Pearson correlation. We can choose other final similarity measures from the `org.etsisi.visualrs.similarityMeasureFinal` package or we can create our own similarity measures as we will explain later. If we use the `FSM` similarity measure, first, we need to generate the similarity vector (`GenerateSimilarityVectorsSimple` class). You can learn more about this on the [javadoc](https://sergiogilborras.github.io/VisualRS/org/etsisi/visualrs/models/GenerateSimilarityVectorsSimple).

```Java
SimilarityMatrix MC = new SimilarityMatrix(new FPearson(), MV);
```

Then we will make the maximum spanning tree, by using the `MaximumSpanningTreeMatrix` class:

```Java
MaximumSpanningTreeMatrix MRM = new MaximumSpanningTreeMatrix(MC);
```

In order to test the quality of the resulting tree graph, we select any metrics based on distance. In this example we have decided to use a matrix distance with `weight = 1`, but we have more distance matrices implementations in the
[`org.etsisi.visualrs.models`](https://sergiogilborras.github.io/VisualRS/org/etsisi/visualrs/models/package-summary.html) package.

```Java
NumberOuts nouts = new NumberOuts(MRM);
DistanceMatrixW1 MDW1 = new DistanceMatrixW1(MRM);
```

Now we can execute several quality measures. In this example we use the closeness centrality quality measure. We are looking for a positive correlation result (Pearson correlation) between the closeness centrality and the #OUT of each graph node.

```Java
Pearson pearsonCorrelation = new Pearson();
ClosenessCentralityWqW1 pcW1 = new ClosenessCentralityWqW1(MDW1);
QualityMeasureCorrelation QMC = new QualityMeasureCorrelation(pearsonCorrelation, nouts, pcW1)
```

We can print the result:

```Java
System.out.println(QMC.getName() + " .. " + QMC.calculate());
```

We can export the generated tree graph using different graphic formats (PDF, SVG, PNG). The CSV format as an adjacency list is also supported. In this example we choose the PNG format. We can also customize the exported graphs. We can define the color of nodes, edges and labels. We can also customize the size of nodes and tags. Edges' and nodes' colors can also be defined based on their similarity and relevance respectively. Following example shows how to customize the exported graph.

```Java
Exports graphicsGephi = new Exports(MRM);

GraphicsGephi.setColorNodes(Color.ORANGE);
GraphicsGephi.setSizeNodes(20);
GraphicsGephi.setColorEdgeBySimilarity();
GraphicsGephi.setColorTags(Color.BLACK);
GraphicsGephi.setSizeTags(0);

GraphicsGephi.execute(TypeFileExport.PNG);
```

The exported graph will looks like this:

![Getting started exported graph](http://rs.etsisi.upm.es/visualrs/getting-started.jpg "Getting started exported graph")

This example has been added to the `org.etsisi.visualrs.examples.GettingStarted` class. You can run it using the following command:

```
mvn exec:java -Dexec.mainClass="org.etsisi.visualrs.examples.GettingStarted"
```

Following figure summarizes the process sequence to generate VisualRS results. We can load different current open RS datasets, using LoadData class, from the `io` package. Then we choose the similarity measure that will relate users or items. To convert the rating matrix to a graph, we use the `SimilarityMatrix` class, into the models package; the same package provides us with the class `MaximumSpanningTreeMatrix` to turn the graph into a tree. We can test each information tree quality using node-based and histogram-based quality measures or an aggregation, composition and modification mix; package `qualityMeasures` contains the necessary classes. Finally, results can be graphically shown using the Export class from the `io` package. Additionally, tabular and text formats are supported.

![Process sequence to generate VisualRS results](http://rs.etsisi.upm.es/visualrs/image010.png "Process sequence to generate VisualRS results")

## Package Structure

The following figure shows the packages structure

![Package structure](http://rs.etsisi.upm.es/visualrs/image001.jpg "Package structure")

Main Packages are:
* `IO`: Input/Output functionality. It contains the following classes:
  *	`Export`
  *	`LoadData`
  *	`MenuCommandLine`

* `Models`: Operating logic of the framework. It contains the following classes:
  *	`DistanceMatrixW1`
  *	`DistanceMatrixWOuts`
  *	`DistanceMatrixWq`
  *	`GenerateSimilarityVectorsSimple`
  *	`MaximumSpanningTreeMatrix`
  *	`SimilarityMatrix`

* `SimilarityMeasureFinal`: It contains the final similarity measures classes. In the following sections we will explain this package.

* `QualityMeasures`: It contains the quality measures classes. In the following sections we will explain this package.
 
## Main Classes

This section summarizes the framework’s main classes. Any additional information can be found in [javadoc documentation](https://sergiogilborras.github.io/VisualRS/).

### Main classes structure

![Main classes](http://rs.etsisi.upm.es/visualrs/image002.jpg "Main classes")

This figure shows the framework’s main classes. It focuses on: 1) Classes to generate the similarities matrices and the tree graph, and 2) Classes to test each tree graph quality. First, the ratings matrix can be loaded from a data file. We have several reading functions adapted to each of the existing datasets. We can choose from a complete set of similarity measures (Similarity_measure_final), in order to obtain different graphs and tree graphs.

The `Similarity_matrix` instances will contain all the items or users similarity values. This are symmetric matrices. From each similarities matrix we apply the Prim’s algorithm in order to obtain its maximum spanning tree.
This frameworks contains the necessary functionalities to test each tree graph quality. We can choose from a selection of distance quality measures. These qualities measures are applied to each tree graph. Finally, we can export graph results using PNG, PDF, CSV or SVG formats.

### Quality measures

![Quality measures classes](http://rs.etsisi.upm.es/visualrs/image003.jpg "Quality measures classes")

This figure shows the classes involved in the quality measures. There are four types of quality measures that we will use to test results:

* The average error of the histogram.
* The correlation of the histogram.
* The aggregation of a simple quality measure.
* The correlation of two simple quality measures.

The aggregation quality measure is formed by joining an aggregation function and a simple quality measure. The aggregation function we have implemented is AVG, which is the weighted average of the nodes values. The correlation quality measure is formed by joining a correlation function and two simple quality measures. The implemented simple quality measures are:

*	Avg_distance_leaves_outs_wq_w1
*	Avg_distance_leaves_wq_w1
*	Betweenness_centrality
*	Closeness_centrality_wq_w1
*	Min_distance_leaf
*	Number_outs
*	Number_votes
*	Sum_votes

Any of the defined similarity measures can be used to implement a correlation function. The “average error of the histogram” and the “correlation of the histogram” processes are based on their comparison with a histogram that follows the geometric serie ½ (a * 1/2K), where a is the number of the tree leaves. We have empirically shown that when the histogram approaches this type of series, the best results are produced in the rest of the quality measures, and vice versa.

Quality measures based on the modification or composition of the simple quality measures have also been created. We have created: a) The modification function LN, the neperian logarithm for each node, and b) The composition function based on the division of two simple quality measures.

You can add new aggregation, modification or composition functions by extending them from their parent classes. In the same way, you can add new simple quality measures. Just extending functions we can expand the number of available quality measures.

### Similarity measures

![Similarity metrics classes](http://rs.etsisi.upm.es/visualrs/image004.jpg "Similarity metrics classes")

In this figure we see two differentiated measures groups: a) Base similarities, and b) Final similarities. Both extend their corresponding abstract classes. The base similarity measures are implemented as defined in the research papers.

The final similarity measures are customized to the recommender systems particularities: due to the sparsity nature of the datasets, a filtering is done so that: a) Uncommon votes are eliminated, b) Vectors without a minimum of common votes return -99, and c) When vectors are identical, we return 1 (máximum similarity).
Different popular combinations of well known similarity measures are also made: such as JMSD and CJacMD. We provide different similarity measures combining Jaccard with other measures, because they report good quality results. The framework’s similarity measures are:

* CJacMD
* Cosine
* Covariance
* JMMD
* JMSD
* Jaccard
* JaccardCosine
* JaccardCovariance
* JaccardKendalls
* JaccardPearson
* JaccardSpearmans
* Kendalls
* MMD
* MSD
* Pearson
* SM
* Spearman

## Extending VisualRS

### Creating new similarity measures

To add a new similarity measure, we must create two classes: 1) To extend the abstract class `SimilarityMeasureBase`, and 2) To extend the abstract class `SimilarityMeasureFinal`.

The following classes show the process, using Pearson correlation as an example:

```Java
public class Pearson extends SimilarityMeasureBase {

    PearsonsCorrelation pc;
    public Pearson() {
        pc = new PearsonsCorrelation();
    }

    @Override
    public double calculate(double[] a1, double[] a2) {
        return pc.correlation(a1, a2);
    }
}
```

```Java
public class FPearson extends SimilarityMeasureFinal {

    Pearson pc;
    public FPearson() {
        this.name = "Pearsons Correlation";
        pc = new Pearson();
    }

    @Override
    public double calculate(double[] a1, double[] a2) {
        double val = -99;
        double[][] Vcomum = this.filterCommonVotes(a1, a2);
        if (Vcomum[0].length > minComunUsers) {
            val = pc.calculate(Vcomum[0], Vcomum[1]);
            if (Double.isNaN(val)) {
                val = 1;//
            }
        }
        return val;
    }
}
```

This similarity metric has been added to the classes `org.etsisi.visualrs.examples.SMBPearsonExample` and `org.etsisi.visualrs.examples.SMPearsonExample`.

### Creating new quality measures

To add a new quality measure, we have to extend the abstract class `QualityMeasureByNode`. The following code shows, as an example, the outs based quality measure:

```Java
public class NumberOuts extends QualityMeasureByNode {

    public NumberOuts(MaximumSpanningTreeMatrix MRM) {
        super(MRM.getMaximumSpanningTreeMatrix());
        name = "NumberOuts";
    }

    @Override
    public double[] calculate() throws Exception {
        if (resultCalculate == null) {
            double[] out = new double[matriz.columns];
            double uout = 0;
            for (int item = 0; item < matriz.columns; item++) {
                double[] ColumItem = matriz.getColumn(item).toArray();
                for (int item1 = 0; item1 < matriz.rows; item1++) {
                    if (ColumItem[item1] >=-1) {
                        uout++;
                    }
                }
                out[item] = uout;
                uout = 0;
            }
            resultCalculate = out;
        }
        return resultCalculate;
    }
}
```

This quality measure has been added to the class `org.etsisi.visualrs.examples.QMOutsNumber`.
 
## Test example

A Test class has been implemented to act as “Hello word”. The `Test` class contains three examples that are executed using the console:

* `exec_once (index list, similarity measure)`: It executes a similarity measure on the chosen dataset. The index of the similarity measure will be passed to the function; then, the obtained graph will be tested through the different quality measures. Results will be displayed by console. Results from the graph will be exported in all the supported file types (PDF, PNG, CSV, SVG).

* `exec_batch ()`: It executes all the provided similarity measures. The obtained graphs will be tested through the different quality measures. Results will be saved in a spreadsheet. Results from each graph will be exported in all the supported file types (PDF, PNG, CSV, SVG).

* `executionBattery_graph ()`: It executes all the provided similarity measures. Results from each graph will be exported in all the supported file types (PDF, PNG, CSV, SVG). In this case, the quality measures on each graph will not be executed.

To execute the console Test code, we use the command:  

```
java -jar mVisualRS-1.0.1.jar
```

The Test program allows us to select the dataset:

![Database selection](http://rs.etsisi.upm.es/visualrs/image005.png "Database selection")

Once the selected dataset has been loaded, the program will let us to choose one of the three supported options:

![Execution mode](http://rs.etsisi.upm.es/visualrs/image006.png "Execution mode")

If we have chosen “One similarity measure execution”, another menu will be shown to select the similarity measure:

![Similarity metric selection](http://rs.etsisi.upm.es/visualrs/image007.png "Similarity metrics selection")

The intermediate matrices results and the different export files will be saved into the `./Data` folder. Inside this folder we will find subfolders with the names of the used data files, and within each of these we will find: a) The spreadsheet with the results of all similarity measures, b) A copy of the rating matrix, and c) Several folders containing the intermediate matrices of each similarity measure.

## Results

Next we show the resulting table from the option 1 execution: "All similarities measures execution" corresponding to the Test class `exec_batch ()` method.

The table shows a complete set of quality measures results when all the implemented similarity measures are applied to the MovieLens 1M dataset. Red values represent inadequate results, while blue shadowed cells show the best results. This experiment shows the superiority of the `JMSD` and `Jaccard * Cosine` metrics to represent MovieLens RS information. The set of implemented quality measures is:

* OUT ≈ CC: Correlation between OUT and Closeness central
* OUT ≈ BC: Correlation between OUT and Between centrality
* SUM ≈ CC: Correlation between SUM and Closeness central
* SUM ≈ BC: Correlation between SUM and Between centrality
* OUT ≈ SUM. Correlation between OUT and SUM
* SUM ≈ LD. Correlation between SUM and nearest leaf
* OUT ≈ LD. Correlation between OUT and nearest leaf
* AVG_LD
* AVG_LD_OUT
* SUM ≈ AVG_LD_OUT. Correlation between SUM and AVG_LD_OUT

![Results example](http://rs.etsisi.upm.es/visualrs/image008.png "Results example")

As an example, we show the tree graph obtained by selecting the data set MovieLens 1M and the similarity measure JMSD:

![Chart example](http://rs.etsisi.upm.es/visualrs/image009.png "Chart example")
 
## Datasets

The following datasets are included into the VisualRS framework (datasets directory):

* `BookCrossing.csv` [(link)](http://www2.informatik.uni-freiburg.de/~cziegler/BX/): Contains the book rating information. Ratings (`Book-Rating`) are either explicit, expressed on a scale from 1-10 (higher values denoting higher appreciation), or implicit, expressed by 0.

* `Jester.csv` [(link)](http://www.ieor.berkeley.edu/~goldberg/jester-data/): Anonymous Ratings Data from the Jester Online Joke Recommender System.

* `MovieLens1M.txt` [(link)](https://MovieLens.org/): Offers collaborative filtering (CF) datasets for movies. MovieLens datasets come in different sizes. Also links to the older EachMovie dataset that can be obtained upon request from Compaq.

* `FilmTrust.txt` [(link)](https://www.librec.net/datasets.html): FilmTrust is a small dataset crawled from the entire FilmTrust website in June, 2011.

* `Netflix.txt` [(prize link)](http://netflixprize.com) & [(dataset link)](https://www.kaggle.com/netflix-inc/netflix-prize-data): Netflix held the Netflix Prize open competition for the best algorithm to predict user ratings for films. The grand prize was $1,000,000 and was won by BellKor's Pragmatic Chaos team. This is the dataset that was used in that competition.
