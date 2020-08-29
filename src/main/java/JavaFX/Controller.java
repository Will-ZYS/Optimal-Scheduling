package JavaFX;


import algorithm.Processor;
import algorithm.SolutionNode;
import algorithm.SolutionTree;
import algorithm.TaskNode;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.colors.Bright;
import eu.hansolo.tilesfx.colors.Dark;
import eu.hansolo.tilesfx.tools.Helper;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Scheduler;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.scene.image.Image ;
import static javafx.scene.paint.Color.rgb;

public class Controller implements Initializable {

    private static final Random RND = new Random();;
    private GanttChart<Number,String> chart;
    private Timeline timerHandler;
    private double startTime;
    private double currentTime;
    private SolutionTree _solutionTree;
    private boolean pollingRanOnce = false;

    @FXML
    private VBox memBox;
    @FXML
    private VBox searchSpaceBox;
    @FXML
    private VBox ganttChartBox;

    private Tile memoryTile;
    private Tile circularPercentageTile;
    private Tile imageTile;
    private int TILE_WIDTH = 200;
    private int TILE_HEIGHT = 200;

    private ChartData chartData1 = new ChartData("Item 1", 100); // waiquan

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setUpMemoryTile();
//        setUpImageTile();
        setUpGanttBox();
        setUpCircularPercentageTile();
        autoUpdate();


        memoryTile.setValue(0);

        circularPercentageTile.setValue(25);
    }

    public void initialize() {

    }

    private void setUpMemoryTile() {
        this.memoryTile = TileBuilder.create().skinType(Tile.SkinType.GAUGE_SPARK_LINE)

                .skinType(Tile.SkinType.GAUGE)
                .prefSize(TILE_WIDTH, TILE_HEIGHT)
//                .title("Gauge Tile")
                .unit("MB")
                // Customized Colours
                .backgroundColor(Color.WHITE)
                .valueColor(Color.BLACK)
                .unitColor(Color.BLACK)
                // ====
                .maxValue(Runtime.getRuntime().maxMemory() / (1024 * 1024))
                .threshold(Runtime.getRuntime().maxMemory() * 0.8 / (1024 * 1024))
                .build();
        memBox.getChildren().addAll(this.memoryTile);
    }

    private void setUpImageTile() {
        this.imageTile = TileBuilder.create().skinType(Tile.SkinType.IMAGE)
                .prefSize(TILE_WIDTH,TILE_HEIGHT)
                .title("ImageCounter Tile")
                .text("Whatever text")
                .description("Whatever\nnumbers")
                .image(new Image("https://static.thenounproject.com/png/688062-200.png"))
                .imageMask(Tile.ImageMask.ROUND)
                .text("Whatever text")
                .textAlignment(TextAlignment.CENTER)
                .build();
        memBox.getChildren().addAll(this.imageTile);
//        Image image = new Image("/background.jpg");
//        memBox.getChildren().add(new ImageView(image));
    }

    private void setUpCircularPercentageTile(){
        circularPercentageTile = TileBuilder.create()
//                .skinType(Tile.SkinType.CIRCULAR_PROGRESS)
//                .prefSize(TILE_WIDTH, TILE_HEIGHT)
//                // Customized Colours
//                .backgroundColor(Color.WHITE)
//                // ====
//                .unit("searched")
//                .build();


                .skinType(Tile.SkinType.RADIAL_PERCENTAGE)
                .prefSize(TILE_WIDTH, TILE_HEIGHT)
                //.backgroundColor(Color.web("#26262D"))
                .maxValue(100)
//                .title("RadialPercentage Tile")
//                // Customized Colours
                .backgroundColor(Color.WHITE)
                .valueColor(Color.BLACK)
                .unitColor(Color.BLACK)
//                // ====
                .description("Searched")
                .textVisible(false)
                .chartData(chartData1)
                .animated(true)
                .referenceValue(2)
                .value(chartData1.getValue())
                .descriptionColor(Tile.GRAY)
                .barColor(Tile.BLUE)

                .decimals(0)
                .build();
        searchSpaceBox.getChildren().addAll(this.circularPercentageTile);
    }

    private void setUpGanttBox(){

        // Setting up number of processors and array of their names
        int numberPro = Scheduler.get_numOfProcessor();
        String[] processors = new String[numberPro];
        for (int i = 0;i<numberPro;i++){
            processors[i]="Processor "+i;
        }

        // Setting up time (x) axis
        final NumberAxis timeAxis = new NumberAxis();
        timeAxis.setLabel("");
        timeAxis.setTickLabelFill(Color.rgb(254,89,21));
        timeAxis.setMinorTickCount(4);

        // Setting up processor (y) axis
        final CategoryAxis processorAxis = new CategoryAxis();
        processorAxis.setLabel("");
        timeAxis.setTickLabelFill(Color.rgb(254,89,21));
        processorAxis.setTickLabelGap(1);
        processorAxis.setTickLabelRotation(270);
        processorAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(processors)));

        // Setting up chart
        chart = new GanttChart<>(timeAxis, processorAxis);
        chart.setLegendVisible(false);
        chart.setBlockHeight(280/numberPro);

        chart.getStylesheets().add(getClass().getResource("/GanttChart.css").toExternalForm());
        chart.setMaxHeight(ganttChartBox.getPrefHeight());
        ganttChartBox.getChildren().add(chart);
        ganttChartBox.setStyle("-fx-background-color: WHITE");
        ganttChartBox.setRotate(90);

    }

    private void autoUpdate() {
        Timeline poller = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            // Start polling memory tile
            double memoryUsage = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1000000d);
            memoryTile.setValue(memoryUsage);

            if(_solutionTree.getCurrentBestSolution() != null){
                updateGanttChart(_solutionTree.getCurrentBestSolution());
            }
            if(_solutionTree.isCompleted()){
                if(pollingRanOnce) {
                    return;
                }
            }
            pollingRanOnce = true;
        }));
        poller.setCycleCount(Animation.INDEFINITE);
        poller.play();
    }

    private void updateGanttChart(SolutionNode bestSolution){
        int numProcessers = Scheduler.get_numOfProcessor();

        // new array of series to write onto
        XYChart.Series[] seriesArray = new XYChart.Series[numProcessers];

        // initializing series obj
        for (int i=0;i<numProcessers;i++){
            seriesArray[i]=new XYChart.Series();
        }


        // for every task in schedule, write its data onto the specific series
        for (Processor processor: bestSolution.getProcessors()){

            Map<TaskNode, Integer> tasks = processor.getTasks();
            for (TaskNode task : tasks.keySet()) {
                XYChart.Data newData = new XYChart.Data(tasks.get(task), "Processor " + (processor.getID()-1),
                        new GanttChart.ExtraData(task, "task-style"));

                System.out.println(tasks.get(task));
                System.out.println(task.getName());
                System.out.println(processor.getID());

                seriesArray[processor.getID()-1].getData().add(newData);
            }
        }

        //clear and rewrite series onto the chart
        chart.getData().clear();
        for (XYChart.Series series: seriesArray){
            chart.getData().add(series);
        }
    }


    public void setSolutionTree(SolutionTree solutionTree){
        this._solutionTree = solutionTree;
    }

    /**
     * Set up listeners that listen to the stage
     * 1. Make the rotated Gantt Chart height=currentWidth+grownWidth width=currentHeight+grownHeight
     *
     * @param stage The stage of the current Visualization
     */
    public void setStageAndSetupListeners(Stage stage){
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
        });
    }

}
