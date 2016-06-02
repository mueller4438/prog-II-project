package ch.fhnw.project;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.util.*;

import static javafx.scene.layout.Priority.ALWAYS;

public final class App extends Application {
    public App() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    private String firstVariable, secondVariable, thirdVariable;
    private Double[] dataFirstVariable, dataSecondVariable;
    private Data myData;
    private boolean isOpeningFile;
    private boolean isVisible;
    private ColorPicker colorPicker;
    private Slider slider;

    private NumberAxis xScatterAxis;
    private NumberAxis yScatterAxis;
    private NumberAxis xLineAxis;
    private NumberAxis yLineAxis;

    private LineChart<Number, Number> lineChart;
    private BarChart<String, Number> barChart1, barChart2;

    private XYChart.Series<Number, Number> seriesOfScatterChart;
    private LineChart.Series<Number, Number> seriesOfLineChart;

    @Override
    public void start(Stage primarystage) {
        isVisible = true;

        colorPicker = new ColorPicker();
        slider = new Slider(0, 25, 3);

        xScatterAxis = new NumberAxis();
        yScatterAxis = new NumberAxis();
        xLineAxis = new NumberAxis();
        yLineAxis = new NumberAxis();

        lineChart = new LineChart<>(xLineAxis, yLineAxis);
        barChart1 = new BarChart<>(new CategoryAxis(), new NumberAxis());
        barChart2 = new BarChart<>(new CategoryAxis(), new NumberAxis());

        seriesOfScatterChart = new XYChart.Series<>();
        seriesOfLineChart = new LineChart.Series<>();

        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xScatterAxis, yScatterAxis);

        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        scatterChart.getData().add(seriesOfScatterChart);
        scatterChart.setLegendVisible(false);
        lineChart.getData().add(seriesOfLineChart);
        lineChart.setLegendVisible(false);

        // File Path Text Field
        TextField filePathTextField = new TextField();
        filePathTextField.setDisable(true);
        filePathTextField.setPrefSize(1000, 5);

        //Slider Point Size
        slider.setShowTickLabels(false);
        slider.setShowTickMarks(false);

        // Disable Line Chart Button
        Button visibleButton = new Button();
        visibleButton.setOnMouseClicked(actionEvent -> isLineChartVisible(visibleButton));
        visibleButton.setText("invisible");
        visibleButton.setPrefSize(100, 10);

        // Labels
        // Label Plot
        Label plotLabel = new Label ("Plot: ");
        // Label Line Chart
        Label lineChartLabel = new Label ("Line Chart: ");
        // Labels Combobox
        Label xLabel = new Label("X-Axis: ");
        Label yLabel = new Label("Y-Axis: ");
        Label sizeLabel = new Label("Bubble Size: ");

        // X Combobox
        ComboBox<String> xComboBox = new ComboBox<>();
        xComboBox.setPrefSize(100, 10);
        xComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null || isOpeningFile){
                return;
            }
            firstVariable = newValue;
            getData(myData);
            fillLineChart();
            fillXYChart(myData);
            fillHistogram(barChart1, firstVariable);
        });
        
        // Y Combobox
        ComboBox<String> yComboBox = new ComboBox<>();
        yComboBox.setPrefSize(100, 10);
        yComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null || isOpeningFile){
                return;
            }
            secondVariable = newValue;
            getData(myData);
            fillLineChart();
            fillXYChart(myData);
            fillHistogram(barChart2,secondVariable);
        });
        
        // Z Combobox
        ComboBox<String> zComboBox = new ComboBox<>();
        zComboBox.setPrefSize(100, 10);
        zComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null || isOpeningFile){
                return;
            }
            thirdVariable = newValue;
            seriesOfScatterChart.getData().clear();
            fillXYChart(myData);
        });
        
        // Scatter & Line Chart Stack Pane
        StackPane chartPane = new StackPane(lineChart, scatterChart);
        scatterChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");

        // File Path Button
        Button filePathButton = new Button(" ... ");
        filePathButton.setOnAction(actionEvent -> {

            isOpeningFile = true;
            File file = openFile(primarystage);
            if(file != null) {
                clearDisplayedValues();
                xComboBox.getItems().clear();
                yComboBox.getItems().clear();
                zComboBox.getItems().clear();

                if(file.length() > 0) {
                    filePathTextField.setText(file.getAbsolutePath());
                    Data data = displayFile(file);
                    if (data != null) {
                        fillVariableComboBoxes(xComboBox, yComboBox, zComboBox);
                    }
                }
            }
            isOpeningFile = false;
        });

        // Layout

        // First Line HBox
        HBox firstLine = new HBox();
        firstLine.getChildren().addAll(filePathTextField, filePathButton);
        firstLine.setAlignment(Pos.CENTER);
        firstLine.setSpacing(10);
        firstLine.setPadding(new javafx.geometry.Insets(5, 5, 5, 5));
        firstLine.setStyle("-fx-background-color: white;");

        // Second Line HBox
        HBox secondLine = new HBox();
        secondLine.getChildren().addAll(xLabel, xComboBox, yLabel, yComboBox, lineChartLabel, visibleButton, plotLabel, colorPicker, sizeLabel, zComboBox, slider);
        secondLine.setAlignment(Pos.CENTER);
        secondLine.setSpacing(10);
        secondLine.setPadding(new javafx.geometry.Insets(5, 5, 5, 5));
        secondLine.setStyle("-fx-background-color: white;");

        // Third Line HBox
        HBox thirdLine = new HBox();
        thirdLine.getChildren().addAll(chartPane);
        thirdLine.setStyle("-fx-background-color: white;");
        HBox.setHgrow(chartPane, ALWAYS);

        // Fourth Line HBox
        HBox fourthLine = new HBox();
        fourthLine.getChildren().addAll(barChart1, barChart2);
        fourthLine.setStyle("-fx-background-color: white;");
        HBox.setHgrow(barChart1, ALWAYS);
        HBox.setHgrow(barChart2, ALWAYS);

        // VBox
        VBox pane = new VBox();
        pane.getChildren().addAll(firstLine, secondLine, thirdLine, fourthLine);
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setStyle("-fx-background-color: white;");
        VBox.setVgrow(thirdLine, ALWAYS);
        VBox.setVgrow(fourthLine, ALWAYS);
        
        // VBox Stack Pane 
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(pane);

        // Scene
        Scene scene = new Scene(stackPane, 1200, 1000);

        // Stage
        primarystage.setTitle("Datenvisualisierung");
        primarystage.setScene(scene);
        primarystage.show();
    }

    //Alert Window
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An Error occurred: ");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void isLineChartVisible(Button visibleButton){
        if (isVisible){
            visibleButton.setText("visible");
            lineChart.setVisible(false);
            isVisible = false;
        }
        else {
            visibleButton.setText("invisible");
            lineChart.setVisible(true);
            isVisible = true;
        }
    }

    private void fillVariableComboBoxes(ComboBox<String> xComboBox, ComboBox<String> yComboBox,ComboBox<String> zComboBox){
        xComboBox.getItems().clear();
        xComboBox.getItems().addAll(myData.getVariableNames());
        xComboBox.setValue(firstVariable);
        yComboBox.getItems().clear();
        yComboBox.getItems().addAll(myData.getVariableNames());
        yComboBox.setValue(secondVariable);
        zComboBox.getItems().clear();
        zComboBox.getItems().add("");
        zComboBox.getItems().addAll(myData.getVariableNames());
        zComboBox.setValue("");
    }

    private File openFile(Stage primarystage) {
        //Select A Line Or Tab Oriented File
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("(*.lin), (*.txt)", "*.txt", "*.lin"));
        //Read And Parse Data
        return fileChooser.showOpenDialog(primarystage);
    }

    private Data displayFile(File selectedFile){
        try {
            if (selectedFile.getName().endsWith(".txt")) {
                myData = new TabReader().parseContents(selectedFile);
            }
            else if (selectedFile.getName().endsWith(".lin")) {
                myData = new LineReader().parseContents(selectedFile);
            }
        }
        catch (DataReaderException exception) {
            showErrorMessage(exception.getMessage());
            return null;
        }
        getVariable(myData);
        getData(myData);
        fillXYChart(myData);
        fillLineChart();
        fillHistogram(barChart1, firstVariable);
        fillHistogram(barChart2, secondVariable);
        return myData;
    }

    private void getData(Data myData) {
        dataFirstVariable = myData.getDataForVariable(firstVariable);
        dataSecondVariable = myData.getDataForVariable(secondVariable);
    }

    private void getVariable(Data myData) {
        //Get Variable Names
        Set<String> variableSet = myData.getVariableNames();
        Iterator<String> iterator = variableSet.iterator();
        firstVariable = iterator.next();
        if (iterator.hasNext()) {
            secondVariable = iterator.next();
            thirdVariable = "";
        }
        else firstVariable = secondVariable;
    }

    private void clearDisplayedValues(){
        xScatterAxis.setLabel("");
        xLineAxis.setLabel("");
        yScatterAxis.setLabel("");
        yLineAxis.setLabel("");
        barChart1.setTitle("");
        barChart2.setTitle("");
        seriesOfScatterChart.getData().clear();
        seriesOfLineChart.getData().clear();
        barChart1.getData().clear();
        barChart2.getData().clear();
    }

    //Scatterchart
    private  void fillXYChart(Data myData) {
        //Put Data In XY-Scatterchart
        seriesOfScatterChart.getData().clear();
        // Iterate Over All Datapoints & Set The Radius From The Circle
        Double[] dataForThirdVariable = myData.getDataForVariable(thirdVariable);
        Double min = 0.0;
        if(dataForThirdVariable != null && dataForThirdVariable.length > 0) {
            min = Collections.min(Arrays.asList(dataForThirdVariable));
            if (min > 0.0) {
                min = 0.0;
            }
        }
        for (int i = 0; i < dataFirstVariable.length; i++) {
            Double x = dataFirstVariable[i];
            Double y = dataSecondVariable[i];
            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(x, y);
            Circle circle = new Circle();
            circle.setRadius(slider.getValue());
            if (thirdVariable != null  && !thirdVariable.isEmpty() && dataForThirdVariable != null) {
                Double z = dataForThirdVariable[i] + Math.abs(min);
                sliderPointSize(myData, z, circle);
            }
            else {
                sliderPointSize(myData, i, circle);
            }
            circle.setFill(colorPicker.getValue());
            colorPicker.valueProperty().addListener(observable -> {
                circle.setFill(colorPicker.getValue());
            });
            dataPoint.setNode(circle);
            seriesOfScatterChart.getData().add(dataPoint);
        }
        // Label Axis Scatter & Line Chart
        xScatterAxis.setLabel(firstVariable);
        xLineAxis.setLabel(firstVariable);
        yScatterAxis.setLabel(secondVariable);
        yLineAxis.setLabel(secondVariable);
    }

    private void sliderPointSize(Data myData, double z, Circle circle) {
        if(thirdVariable != null && !thirdVariable.isEmpty()) {
             double size = bubbleSizeMaxValue(myData);
             circle.setRadius(slider.getValue() * z / size * 5);
             slider.valueProperty().addListener((observable, oldValue, newValue) ->
                     circle.setRadius(slider.getValue() * z / size * 5));
         }
         else {
            circle.setRadius(slider.getValue());
            slider.valueProperty().addListener((observable, oldValue, newValue) ->
                    circle.setRadius(slider.getValue()));
        }
    }

    //Bubble Size Scaling
    private double bubbleSizeMaxValue(Data myData) {
        if(thirdVariable != null && !thirdVariable.isEmpty()) {
            Double[] d = myData.getDataForVariable(thirdVariable);
            return Collections.max(Arrays.asList(d));
        }
        else {
            return 1.0;
        }
    }

    private void fillLineChart() {
        seriesOfLineChart.getData().clear();
        for (int i = 0; i < dataFirstVariable.length; i++) {
            seriesOfLineChart.getData().add(new LineChart.Data<>(dataFirstVariable[i], dataSecondVariable[i]));}
    }

    //Fill Histogram
    BarChart<String, Number> fillHistogram(BarChart<String,Number>chart, String variableName){
        Double[] d = myData.getDataForVariable(variableName);
        int numberOfBins = (int) Math.sqrt(d.length);
        int ret[] = new int [numberOfBins];
        Double min = Collections.min(Arrays.asList(d));
        Double max = Collections.max(Arrays.asList(d));
        Double range = max - min;
        for(int i = 0; i < numberOfBins; i++){
            ret[i] = 0;
        }
        for (Double v : d) {
            int z = (int)((v - min) / (range + 0.000001) * (numberOfBins));
            ret[z]++;
        }
        String[] xAxis = new String[numberOfBins];
        for(int i = 0; i < numberOfBins; i++){
            xAxis[i] = String.format("%.2f to %.2f",(min + i * range / numberOfBins), (min + (i + 1) * range / numberOfBins));
        }
        XYChart.Series series = new XYChart.Series<>();
        for(int i = 0; i < numberOfBins; i++){
            series.getData().add(new XYChart.Data(xAxis[i], ret[i]));
        }
        chart.getData().clear();
        chart.getData().addAll(series);
        chart.setTitle(variableName);
        chart.setLegendVisible(false);
        chart.setCategoryGap(0);
        chart.setBarGap(0);
        chart.setStyle("-fx-background-color: transparent;");
        return chart;
    }
}
