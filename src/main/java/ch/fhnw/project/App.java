package ch.fhnw.project;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;


import java.io.File;
import java.util.*;

public final class App extends Application {
    public App() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    private String firstvariable, secondvariable;
    private Double[] dataFirstvariable, dataSecondvariable;
    private Data mydata;
    private boolean isOpeningFile;
    private boolean isVisible = true;
    ColorPicker colorPicker = new ColorPicker();
    Slider slider = new Slider(0, 25, 10);


    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    final NumberAxis x1Axis=new NumberAxis();
    final NumberAxis y1Axis=new NumberAxis();


    private final ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
    private final LineChart<Number, Number> lineChart = new LineChart<>(x1Axis, y1Axis);
    private final BarChart<String,Number> barChart1= new BarChart<>(new CategoryAxis(), new NumberAxis()),barChart2=new BarChart<String, Number>(new CategoryAxis(),new NumberAxis());

    private XYChart.Series<Number,Number> series1 = new XYChart.Series<>();
    private LineChart.Series<Number,Number> series2 = new LineChart.Series<>();
    private BarChart.Series<Number,Number> series3=new BarChart.Series<>();


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
        Label plotLabel = new Label ("Plot: ");
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
                firstvariable = newValue;
                getData(mydata);
                fillLineChart();
                fillXYChart(mydata);
                fillHistogram(barChart1,firstvariable);
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
                secondvariable = newValue;
                getData(mydata);
                fillLineChart();
                fillXYChart(mydata);
                fillHistogram(barChart2,secondvariable);
            }
        });
        // Z Combobox
        ComboBox<String> zComboBox = new ComboBox<String>();
        //zComboBox.setPrefSize(150, 10);
        zComboBox.getItems().addAll(
                "", "Variable X", "Variable Y", "Variable Z"
        );
        zComboBox.setValue("");

        StackPane scatterPane = new StackPane(lineChart, scatterChart);
        scatterChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent");

        // File Path Button
        Button filePathButton = new Button(" ... ");
        filePathButton.setOnAction(actionEvent -> {
            isOpeningFile = true;
            File file = openFile(primarystage);
            filePathTextField.setText(file.getAbsolutePath());
            displayFile(primarystage, file);
            fillVariableComboBoxes(xComboBox, yComboBox);
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
        secondLine.getChildren().addAll(xLabel, xComboBox, yLabel, yComboBox, sizeLabel, zComboBox, lineChartLabel, visibleButton, plotLabel, colorPicker, slider);
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
        //primarystage.setMinHeight(1000);
        //primarystage.setMinWidth(1000);
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


    private void fillVariableComboBoxes(ComboBox<String> xComboBox, ComboBox<String> yComboBox){
        xComboBox.getItems().clear();
        xComboBox.getItems().addAll(mydata.getVariableNames());
        xComboBox.setValue(firstvariable);
        yComboBox.getItems().clear();
        yComboBox.getItems().addAll(mydata.getVariableNames());
        yComboBox.setValue(secondvariable);
    }

    private File openFile(Stage primarystage) {
        //select a line or tab oriented file
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Resource File");
        fc.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("(*.lin), (*.txt)", "*.txt", "*.lin"));
        //read and parse Data
        return fc.showOpenDialog(primarystage);

    }

    private void displayFile(Stage primaryStage, File selectedFile){
        try {
            if (selectedFile.getName().endsWith(".txt")) {
                mydata = new TabReader().parseContents(selectedFile);


            } else if (selectedFile.getName().endsWith(".lin")) {
                mydata = new LineReader().parseContents(selectedFile);


            }

        } catch (DataReaderException e) {
            showErrorMessage(e.getMessage());

        }
        getVariable(mydata);
        getData(mydata);
        fillXYChart(mydata);
        fillLineChart();
        fillHistogram(barChart1, firstvariable);
        fillHistogram(barChart2, secondvariable);
    }

    private void getData(Data mydata) {
        dataFirstvariable = mydata.getDataForVariable(firstvariable);
        dataSecondvariable = mydata.getDataForVariable(secondvariable);
    }

    private void getVariable(Data mydata) {

        //get variable names
        Set<String> vars = mydata.getVariableNames();
        Iterator<String> iter = vars.iterator();
        firstvariable = iter.next();
        if (iter.hasNext()) {
            secondvariable = iter.next();
        } else firstvariable = secondvariable;

        System.out.print(mydata);
    }

    //Alert Window
    private void showErrorMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An Error occurred: ");
        alert.setContentText(msg);

        alert.showAndWait();
    }



    //Scatterchart
    private  void fillXYChart(Data mydata) {
        //put data in XY-scatterchart
        series1.getData().clear();
        for (int i = 0; i < dataFirstvariable.length; i++) {
            Double x =dataFirstvariable[i];
            Double y =dataSecondvariable[i];
            XYChart.Data<Number,Number> dataPoint=new XYChart.Data<>(x,y);
            Circle circle=new Circle();
            slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                        circle.setRadius(slider.getValue());
                    });
            colorPicker.valueProperty().addListener(observable -> {circle.setFill(colorPicker.getValue());});

            dataPoint.setNode(circle);
            series1.getData().add(dataPoint);

           //scatterChart.getData().add(series1);




        }
        //hinzufügen des Scattercharts, eventuell noch verschiebe


    }

    private void fillLineChart() {
        series2.getData().clear();
        for (int i = 0; i < dataFirstvariable.length; i++) {
            series2.getData().add(new LineChart.Data<>(dataFirstvariable[i], dataSecondvariable[i]));}

    }
    //fillHistogram
    BarChart<String, Number> fillHistogram(BarChart<String,Number>chrt, String varName){
        Double[] d = mydata.getDataForVariable(varName);
        int numBins= (int) Math.sqrt(d.length);
        int ret[] = new int [numBins];
        Double min= Collections.min(Arrays.asList(d));
        Double max=Collections.max(Arrays.asList(d));
        Double range=max-min;
        for(int i=0;i<numBins;i++){
            ret[i]=0;
        }
        for (Double v : d) {
            int z= (int)((v-min)/(range)*(numBins-1));
            ret[z]++;
        }
        String[] xAxis=new String[numBins];
        for(int i= 0;i<numBins;i++){
            xAxis[i]= String.format("%.2f to %.2f",(min+i*range/numBins),(min+(i+1)*range/numBins));
        }
        XYChart.Series s=new XYChart.Series<>();
        for(int i=0;i<numBins;i++){
            s.getData().add(new XYChart.Data(xAxis[i], ret[i]));
        }
        chrt.getData().clear();
        chrt.getData().addAll(s);
        chrt.setTitle(varName);
        chrt.setLegendVisible(false);
        chrt.setCategoryGap(0);
        chrt.setBarGap(0);
        chrt.setStyle("-fx-background-color: transparent;");


        return chrt;
    }

}




















