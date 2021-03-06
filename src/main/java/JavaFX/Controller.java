package JavaFX;


import algorithm.*;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Scheduler;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private static final Random RND = new Random();;
    private GanttChart<Number,String> chart;
    private Timeline timerHandler;
    private Timeline scheduleHandler;
    private Timeline poller;
    private double startTime;
    private double currentTime;
    private SolutionTree _solutionTree;
    private SolutionNode _currentBestSolution;
    private boolean pollingRanOnce = false;

    @FXML
    private VBox memBox;

    @FXML
    private VBox searchSpaceBox;

    @FXML
    private VBox ganttChartBox;

    @FXML
    private Label currentBestTime;

    @FXML
    private Label timeElapsed;

    @FXML
    private Label numOfProcessors;

    @FXML
    private Label numOfTasks;

    @FXML
    private Label checkedSchedule;

    @FXML
    private Label statusText;

    @FXML
    private Label inputFile;

    @FXML
    private Label outputFile;

    @FXML
    private Label numOfCores;

    @FXML
    private ImageView statusImage;



    private Tile memoryTile;
    private Tile circularPercentageTile;
    private Tile imageTile;
    private int TILE_WIDTH = 200;
    private int TILE_HEIGHT = 200;

    private ChartData chartData1 = new ChartData("Item 1", 100); // waiquan

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setUpConfigInfo();
        setUpCheckedSchedule();
        setUpMemoryTile();
        setUpGanttBox();
        setUpCircularPercentageTile();
        autoUpdate();
        memoryTile.setValue(0);
        circularPercentageTile.setValue(0); // inner cycle
        startTimer();

    }


    /**
     * Set up the memory tile to monitor the memory usage during the running program
     *
     */
    private void setUpMemoryTile() {
        this.memoryTile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .prefSize(TILE_WIDTH, TILE_HEIGHT)
                .unit("MB")

                .maxValue(Runtime.getRuntime().maxMemory() / (1024 * 1024))
                .threshold(Runtime.getRuntime().maxMemory() * 0.8 / (1024 * 1024))
                .build();
        memBox.getChildren().addAll(this.memoryTile);
    }


    private void setUpCircularPercentageTile(){
        circularPercentageTile = TileBuilder.create()

                .skinType(Tile.SkinType.RADIAL_PERCENTAGE)
                .prefSize(TILE_WIDTH, TILE_HEIGHT)

                .maxValue(100)

                .valueColor(Color.WHITE)
                .unitColor(Color.WHITE)
                .descriptionColor(Color.LIGHTGREY)
                .barColor(Tile.BLUE)

                .description("Searched")
                .textVisible(false)
                .chartData(chartData1)    // outer cycle
                .animated(true)
                .referenceValue(2)
                .value(chartData1.getValue())


                .decimals(0)
                .build();
        searchSpaceBox.getChildren().addAll(this.circularPercentageTile);
    }

    /**
     * Set up the gantt Chart box and display the gantt chart in it
     *
     */
    private void setUpGanttBox(){

        // Setting up number of processors and array of their names
        int numberPro = Scheduler.getNumOfProcessor();
        String[] processors = new String[numberPro];
        for (int i = 0;i<numberPro;i++){
            processors[i]="Processor "+i;
        }

        // Setting up time (x) axis
        final NumberAxis timeAxis = new NumberAxis();
        timeAxis.setLabel("");
        timeAxis.setTickLabelFill(Color.rgb(42,177,255));
        timeAxis.setMinorTickCount(4);

        // Setting up processor (y) axis
        final CategoryAxis processorAxis = new CategoryAxis();
        processorAxis.setLabel("");
        processorAxis.setTickLabelGap(1);
        processorAxis.setTickLabelRotation(270);
        processorAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(processors)));

        // Setting up chart
        chart = new GanttChart<>(timeAxis, processorAxis);
        chart.setLegendVisible(false);
        chart.setBlockHeight(180/numberPro);
        chart.getStylesheets().add(getClass().getResource("/GanttChart.css").toExternalForm());

        // Setting up gantt chart box
        ganttChartBox.getChildren().add(chart);
        ganttChartBox.setStyle("-fx-background-color: WHITE");

        // Rotate the chart inside the Gantt Box
        chart.setRotate(90);

    }

    /**
     * Keep calculating the memory usage value and display it on the screen
     * Check the current best solution every 50 ms to see if it is changed.
     * If it is modified, call the updateGanttChart method to change the gantt chart as well.
     *
     */
    private void autoUpdate() {
        poller = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            // Updating memory tile
            double memoryUsage = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1000000d);
            memoryTile.setValue(memoryUsage);

            // check if the current best solution is changed
            if(_solutionTree.getCurrentBestSolution() != null && _solutionTree.getCurrentBestSolution() != _currentBestSolution){
                _currentBestSolution = _solutionTree.getCurrentBestSolution();
                updateGanttChart(_currentBestSolution);
            }

            // check if the program finishes working
            if(_solutionTree.getIsCompleted()){
                if(pollingRanOnce) {
                    stopTimer();
                    poller.stop();
                    Image finishImg = new Image("/images/finish.png");
                    statusImage.setImage(finishImg);
                    statusText.setText("Done");
                    statusText.setTextFill(Color.GREEN);
                    circularPercentageTile.setValue(100);
                    poller.stop();
                    return;
                }
            }
            else {
                double percentage = (double) _solutionTree.getVisitedSecondLevelSolutionNodes()/_solutionTree.getSecondLevelSolutionNodes() * 100;
                if ( percentage > 99) {
                    percentage = 99;
                }
                circularPercentageTile.setValue(percentage);
            }
            pollingRanOnce = true;
        }));
        poller.setCycleCount(Animation.INDEFINITE);
        poller.play();
    }

    /**
     * Update the gantt chart according to the current best solution
     * @param bestSolution
     *
     */
    private void updateGanttChart(SolutionNode bestSolution){
        int numProcessers = Scheduler.getNumOfProcessor();

        XYChart.Series[] allSeries = new XYChart.Series[numProcessers];

        for (int i=0;i<numProcessers;i++){
            allSeries[i] = new XYChart.Series();
        }

        for (Processor processor: bestSolution.getProcessors()){
            Map<TaskNode, Integer> tasks = processor.getTasks();
            for (TaskNode task : tasks.keySet()) {
                XYChart.Data newData = new XYChart.Data(tasks.get(task), "Processor " + (processor.getID()-1),
                        new GanttChart.ExtraData(task, "task-style"));
                allSeries[processor.getID()-1].getData().add(newData);
            }
        }

        //clear and rewrite series onto the chart
        chart.getData().clear();
        for (XYChart.Series series: allSeries){
            chart.getData().add(series);
        }

        addToolTips();

        currentBestTime.setText(_solutionTree.getCurrentBestSolution().getEndTime() + "s");
    }

    /**
     * Get the desired information from Scheduler and put it into corresponding location
     * The information includes: number of processors, number of tasks, input file name, output file name
     *
     */
    public void setUpConfigInfo(){
        numOfProcessors.setText(String.valueOf(Scheduler.getNumOfProcessor()));
        numOfTasks.setText(String.valueOf(Scheduler.getNumOfTasks()));
        inputFile.setText(Scheduler.getInputFileName() + ".dot");
        outputFile.setText(Scheduler.getOutputName() + ".dot");
        if(!Scheduler.getOpenParallelization()){
            numOfCores.setText("1");
        }else{
            numOfCores.setText(String.valueOf(Scheduler.getNumCores()));
        }
    }

    /**
     * Record how many schedules we completed.
     * Update the data every 100 milliseconds
     *
     */
    public void setUpCheckedSchedule(){

        scheduleHandler = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            if(!Scheduler.getOpenParallelization()){
                checkedSchedule.setText(String.valueOf(_solutionTree.getCheckedSchedule()));
            }else{
                checkedSchedule.setText(String.valueOf(SolutionRecursiveAction.getCheckedSchedule()));
            }
        }));
        scheduleHandler.setCycleCount(Animation.INDEFINITE);
        scheduleHandler.play();

    }


    public void setSolutionTree(SolutionTree solutionTree){
        this._solutionTree = solutionTree;
    }

    /**
     * Record the time the program used to complete scheduling tasks.
     *
     */
    private void startTimer(){

        startTime=System.currentTimeMillis();
        timerHandler = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            currentTime=System.currentTimeMillis();
            timeElapsed.setText((currentTime - startTime) / 1000 + "s");
        }));

        timerHandler.setCycleCount(Animation.INDEFINITE);
        timerHandler.play();
    }

    public void stopTimer(){
        timerHandler.stop();
    }


    /**
     * Set up listeners that listen to the stage
     * 1. Make the rotated Gantt Chart height=currentWidth+grownWidth width=currentHeight+grownHeight
     *
     * @param stage The stage of the current Visualization
     */
    public void setStageAndSetupListeners(Stage stage){
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {

            chart.setMinHeight(ganttChartBox.getWidth());
            chart.setPrefHeight(ganttChartBox.getWidth());

        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {

        });
    }

    private void addToolTips() {
        /**
         * Browsing through the Data and applying ToolTip
         * as well as the class on hover
         */
        for (XYChart.Series<Number,String> s : chart.getData()) {
            for (XYChart.Data<Number,String> d : s.getData()) {
                Tooltip tooltip = new Tooltip("Task: " + GanttChart.getID(d.getExtraValue()) + "   Weight: " + GanttChart.getLength(d.getExtraValue()));
                tooltip.setFont(Font.font(16));
                Tooltip.install(d.getNode(), tooltip);

                //Adding class on hover
                d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));

                //Removing class on exit
                d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
            }
        }
    }
}
