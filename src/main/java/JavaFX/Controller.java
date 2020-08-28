package JavaFX;


import algorithm.Processor;
import algorithm.SolutionNode;
import algorithm.SolutionTree;
import algorithm.TaskNode;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.colors.Bright;
import eu.hansolo.tilesfx.colors.Dark;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.util.Duration;
import main.Scheduler;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;

import static javafx.scene.paint.Color.rgb;

public class Controller implements Initializable {

    private GanttChart<Number,String> chart;
    private Timeline timerHandler;
    private double startTime;
    private double currentTime;
    private SolutionTree _solutionTree;
    private boolean pollingRanOnce = false;

    @FXML
    private VBox memBox;

    @FXML
    private VBox ganttChartBox;

    private Tile memoryTile;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setUpMemoryTile();

        setUpGanttBox();

        autoUpdate();


        memoryTile.setValue(0);

    }

    public void initialize() {

    }

    private void setUpMemoryTile() {
        this.memoryTile = TileBuilder.create().skinType(Tile.SkinType.GAUGE_SPARK_LINE)
                .unit("MB")
                .maxValue(Runtime.getRuntime().maxMemory() / (1024 * 1024))
                .threshold(Runtime.getRuntime().maxMemory() * 0.8 / (1024 * 1024))
                .gradientStops(new Stop(0, rgb(244,160,0)),
                        new Stop(0.8, Bright.RED),
                        new Stop(1.0, Dark.RED))
                .animated(true)
                .decimals(0)
                .strokeWithGradient(true)
                .thresholdVisible(true)
                .backgroundColor(Color.WHITE)
                .valueColor(rgb(244,160,0))
                .unitColor(rgb(244,160,0))
                .barBackgroundColor(rgb(242, 242, 242))
                .thresholdColor(rgb(128, 84, 1))
                .needleColor(rgb(244,160,0))
                .build();

        memBox.getChildren().addAll(this.memoryTile);
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

}
