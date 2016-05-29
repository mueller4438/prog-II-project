package ch.fhnw.project;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.util.*;

public final class App extends Application {
    public App() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    private String firstVariable, secondVariable, thirdVariable;
    private Double[] dataFirstVariable, dataSecondVariable,dataThirdVariable;
    private Data myData;
    private boolean isOpeningFile;
    private boolean isVisible = true;
    ColorPicker colorPicker = new ColorPicker();
    Slider slider = new Slider(0, 25, 3);


    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    final NumberAxis x1Axis = new NumberAxis();
    final NumberAxis y1Axis = new NumberAxis();


    private final ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
    private final LineChart<Number, Number> lineChart = new LineChart<>(x1Axis, y1Axis);
    private final BarChart<String, Number> barChart1= new BarChart<>(new CategoryAxis(), new NumberAxis()), barChart2 = new BarChart<String, Number>(new CategoryAxis(), new NumberAxis());

    private XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
    private LineChart.Series<Number, Number> series2 = new LineChart.Series<>();
    //private BarChart.Series<Number,Number> series3=new BarChart.Series<>();


    @Override
    public void start(Stage primarystage) {

        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        scatterChart.getData().add(series1);
        scatterChart.setLegendVisible(false);
        lineChart.getData().add(series2);
        lineChart.setLegendVisible(false);

        // File Path Text Field
        TextField filePathTextField = new TextField();
        filePathTextField.setDisable(true);
        filePathTextField.setPrefSize(1000, 5);

        //Slider Point Size
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);




        // Disable Line Chart Button
        Button visibleButton = new Button();
        visibleButton.setOnMouseClicked(actionEvent -> isLineChartVisible(visibleButton));
        visibleButton.setText("invisible");
        visibleButton.setPrefSize(100, 10);

        // Labels
        // Label Plot
        Label plotLabel = new Label ("Plot Color: ");
        // Label Line Chart
        Label lineChartLabel = new Label ("Line Chart: ");
        // Labels Combobox
        Label xLabel = new Label("X-Axis: ");
        Label yLabel = new Label("Y-Axis: ");
        Label sizeLabel = new Label("Bubble Size: ");

        // X Combobox
        ComboBox<String> xComboBox = new ComboBox<String>();
        //xComboBox.setPrefSize(150, 10);
        xComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null || isOpeningFile){
                    return;
                }
                firstVariable = newValue;
                getData(myData);
                fillLineChart();
                fillXYChart(myData);
                fillHistogram(barChart1, firstVariable);
            }
        });
        // Y Combobox
        ComboBox<String> yComboBox = new ComboBox<String>();
        //yComboBox.setPrefSize(150, 10);
        yComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null || isOpeningFile){
                    return;
                }
                secondVariable = newValue;
                getData(myData);
                fillLineChart();
                fillXYChart(myData);
                fillHistogram(barChart2,secondVariable);

            }
        });
        // Z Combobox
        ComboBox<String> zComboBox = new ComboBox<String>();
        zComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue==null || isOpeningFile){
                    return;
                }
                thirdVariable=newValue;
                fillXYChart(myData);


            }
        });


        StackPane scatterPane = new StackPane(lineChart, scatterChart);
        scatterChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");

        // File Path Button
        Button filePathButton = new Button(" ... ");
        filePathButton.setOnAction(actionEvent -> {
            isOpeningFile = true;
            File file = openFile(primarystage);
            filePathTextField.setText(file.getAbsolutePath());
            displayFile(primarystage, file);
            fillVariableComboBoxes(xComboBox, yComboBox,zComboBox);
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
        thirdLine.getChildren().addAll(scatterPane);
        //thirdLine.setAlignment(Pos.CENTER);
        //thirdLine.setSpacing(10);
        //thirdLine.setPadding(new javafx.geometry.Insets(5, 5, 5, 5));
        thirdLine.setStyle("-fx-background-color: white;");
        thirdLine.setHgrow(scatterPane, Priority.ALWAYS);

        // Fourth Line HBox
        HBox fourthLine = new HBox();
        fourthLine.getChildren().addAll(barChart1, barChart2);
        //fourthLine.setAlignment(Pos.CENTER);
        //fourthLine.setSpacing(10);
        //fourthLine.setPadding(new javafx.geometry.Insets(5, 5, 5, 5));
        fourthLine.setStyle("-fx-background-color: white;");
        fourthLine.setHgrow(barChart1, Priority.ALWAYS);
        fourthLine.setHgrow(barChart2, Priority.ALWAYS);

        VBox pane = new VBox();
        pane.getChildren().addAll(firstLine, secondLine, thirdLine, fourthLine);
        pane.setAlignment(Pos.TOP_CENTER);
        //pane.setSpacing(10);
        //pane.setPadding(new javafx.geometry.Insets(5, 5, 5, 5));
        pane.setStyle("-fx-background-color: white;");
        pane.setVgrow(thirdLine, Priority.ALWAYS);
        pane.setVgrow(fourthLine, Priority.ALWAYS);

        StackPane stackpane = new StackPane();
        stackpane.getChildren().add(pane);

        // Scene
        Scene scene = new Scene(stackpane, 1000, 1000);

        // Stage
        primarystage.setTitle("Datenvisualisierung");
        primarystage.setMinHeight(1000);
        primarystage.setMinWidth(1000);
        //primarystage.setMaxHeight(1000);
        //primarystage.setMaxWidth(1000);
        primarystage.setScene(scene);
        primarystage.show();
    }

    private void isLineChartVisible(Button visibleButton){
        if (isVisible == true){
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
        zComboBox.getItems().addAll(myData.getVariableNames());
        zComboBox.setValue(thirdVariable);

    }

    private File openFile(Stage primarystage) {
        //select a line or tab oriented file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("(*.lin), (*.txt)", "*.txt", "*.lin"));
        //read and parse Data
        return fileChooser.showOpenDialog(primarystage);

    }

    private void displayFile(Stage primaryStage, File selectedFile){
        try {
            if (selectedFile.getName().endsWith(".txt")) {
                myData = new TabReader().parseContents(selectedFile);
            }
            else if (selectedFile.getName().endsWith(".lin")) {
                myData = new LineReader().parseContents(selectedFile);
            }

        } catch (DataReaderException e) {
            showErrorMessage(e.getMessage());
        }
        getVariable(myData);
        getData(myData);
        fillXYChart(myData);
        fillLineChart();
        fillHistogram(barChart1, firstVariable);
        fillHistogram(barChart2, secondVariable);
    }

    private void getData(Data myData) {
        dataFirstVariable = myData.getDataForVariable(firstVariable);
        dataSecondVariable = myData.getDataForVariable(secondVariable);

    }

    private void getVariable(Data myData) {
        //get variable names
        Set<String> vars = myData.getVariableNames();
        Iterator<String> iter = vars.iterator();
        firstVariable = iter.next();
        if (iter.hasNext()) {
            secondVariable = iter.next();
            if (iter.hasNext()) {
            thirdVariable=null;
            }
            else thirdVariable=null;
            }

        else firstVariable = secondVariable;
    }

    //Alert Window
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An Error occurred: ");
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Scatterchart
    private  void fillXYChart(Data myData) {
        //put data in XY-scatterchart
        series1.getData().clear();





        for (int i = 0; i < dataFirstVariable.length; i++) {
            Double x = dataFirstVariable[i];
            Double y = dataSecondVariable[i];

            XYChart.Data<Number,Number> dataPoint = new XYChart.Data<>(x, y);
            Circle circle = new Circle();

            circle.setRadius(slider.getValue());
            if(thirdVariable!=null) {
                Double z = myData.getDataForVariable(thirdVariable)[i];
                double size=bubbleSizeMaxValue(myData);

                slider.valueProperty().addListener((observable, oldValue, newValue) ->

                        circle.setRadius(slider.getValue() * z / size * 5
                        ));
            }
            else slider.valueProperty().addListener((observable, oldValue, newValue) ->

                    circle.setRadius(slider.getValue() )
            );


            circle.setFill(colorPicker.getValue());
            colorPicker.valueProperty().addListener(observable -> {circle.setFill(colorPicker.getValue());});
            dataPoint.setNode(circle);
            series1.getData().add(dataPoint);
           //scatterChart.getData().add(series1);
        }
    }


    //Bubble size scaling
    private double bubbleSizeMaxValue(Data myData) {
        if(thirdVariable!=null) {
            Double[] d = myData.getDataForVariable(thirdVariable);
            Double max = Collections.max(Arrays.asList(d));
            return max;
        }

        else {
            Double max= 1.0;
            return max;
            }
    }

    private void fillLineChart() {
        series2.getData().clear();
        for (int i = 0; i < dataFirstVariable.length; i++) {
            series2.getData().add(new LineChart.Data<>(dataFirstVariable[i], dataSecondVariable[i]));}
    }

    //fillHistogram
    BarChart<String, Number> fillHistogram(BarChart<String,Number>chart, String variableName){
        Double[] d = myData.getDataForVariable(variableName);
        int numBins = (int) Math.sqrt(d.length);
        int ret[] = new int [numBins];
        Double min = Collections.min(Arrays.asList(d));
        Double max = Collections.max(Arrays.asList(d));
        Double range = max-min;
        for(int i = 0; i < numBins; i++){
            ret[i] = 0;
        }
        for (Double v : d) {
            int z = (int)((v-min) / (range + 0.000001) * (numBins));
            ret[z]++;
        }
        String[] xAxis = new String[numBins];
        for(int i = 0; i < numBins; i++){
            xAxis[i] = String.format("%.2f to %.2f",(min + i * range / numBins), (min + (i + 1) * range / numBins));
        }
        XYChart.Series series = new XYChart.Series<>();
        for(int i = 0; i < numBins; i++){
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




















