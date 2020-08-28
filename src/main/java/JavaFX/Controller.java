package JavaFX;


import algorithm.SolutionNode;
import algorithm.SolutionTree;
import algorithm.TaskNode;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.colors.Bright;
import eu.hansolo.tilesfx.colors.Dark;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.util.Duration;
import main.Scheduler;

import java.util.Arrays;
import java.util.Objects;

import static javafx.scene.paint.Color.rgb;

public class Controller {

    private GanttChart<Number,String> chart;
    private Timeline timerHandler;
    private double startTime;
    private double currentTime;

    @FXML
    private VBox memBox;

    @FXML
    private VBox ganttChartBox;


    private Tile memoryTile;

    private SolutionTree bestSolution;


    public void initialize() {

        setUpMemoryTile();

        setUpGanttBox();


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

        memBox.getChildren().addAll(buildFlowGridPane(this.memoryTile));

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
        processorAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(processors)));

        // Setting up chart
        chart = new GanttChart<Number,String>(timeAxis,processorAxis);
        chart.setLegendVisible(false);
        chart.setBlockHeight(280/numberPro);

        //chart.getStylesheets().add(getClass().getResource("/GanttChart.css").toExternalForm());
        chart.setMaxHeight(ganttChartBox.getPrefHeight());
        ganttChartBox.getChildren().add(chart);
        ganttChartBox.setStyle("-fx-background-color: WHITE");

    }

    private void startGanttChart() {
        Timeline start = new Timeline(new KeyFrame(Duration.millis(50), event -> {
//            if(bestSolution.getCurrentBestSolution()){
//                runningText.setStyle("-fx-fill: rgb(15,157,88);");
//                runningText.setText("Done!");
//                stopTimer();
//
//                if(pollingRanOnce) {
//                    return;
//                }
//            }
//            double memoryUsage = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1000000d);
//            memoryTile.setValue(memoryUsage);

            if(bestSolution.getCurrentBestSolution() != null){
                updateGanttChart(bestSolution.getCurrentBestSolution());
            }


//            currentBestText.setText("" + monitor.getCurrentBest().getFinishTime());
//            updateNumSchedules(monitor.getCompleteSchedules());
//
//            ObservableList<ChartData> allocData = allocationTile.getChartData();
//            ObservableList<ChartData> orderData = orderTile.getChartData();
//
//            // Randomly remove data points to not overload GUI
//            if (allocData.size() >= CHART_MAX_ELEMENTS) {
//                allocData.remove((int) (Math.random() * allocData.size()));
//            }
//
//            if (orderData.size() >= CHART_MAX_ELEMENTS) {
//                orderData.remove((int) (Math.random() * orderData.size()));
//            }
//
//            allocationTile.addChartData(new ChartData(monitor.getAllocationsExpanded()));
//            orderTile.addChartData(new ChartData(monitor.getOrderingsExpanded()));
//
//            pollingRanOnce = true;
        }));
        poller.setCycleCount(Animation.INDEFINITE);
        poller.play();
    }
//
//
    private void updateGanttChart(SolutionNode bestSolution){

        int numProcessers = Scheduler.get_numOfProcessor();

        // new array of series to write onto
        XYChart.Series[] seriesArray = new XYChart.Series[numProcessers];

        // initializing series obj
        for (int i=0;i<numProcessers;i++){
            seriesArray[i]=new XYChart.Series();
        }


        // for every task in schedule, write its data onto the specific series
        for (TaskNode task: bestSolution.getTasks()){
            int idOfTask = scTask.getProcessorId();

            XYChart.Data newData = new XYChart.Data(scTask.getStartTime(), "Processor "+ String.valueOf(idOfTask),
                    new ExtraData(scTask, "task-style"));

            seriesArray[idOfTask].getData().add(newData);

        }

        //clear and rewrite series onto the chart
        chart.getData().clear();
        for (XYChart.Series series: seriesArray){
            chart.getData().add(series);
        }

        // update the best text
        currentBestText.setText(""+bestSchedule.getFinishTime());
    }




    private FlowGridPane buildFlowGridPane(Tile tile) {
        return new FlowGridPane(1, 1, tile);
    }

    public void setSolutionTree(SolutionTree solution){
        this.bestSolution = solution;
    }
}
