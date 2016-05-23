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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.Insets;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public final class App extends Application {
    public App() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    private String firstvariable, secondvariable;
    private Double[] dataFirstvariable, dataSecondvariable;
    private Data mydata;

    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    final NumberAxis x1Axis=new NumberAxis();
    final NumberAxis y1Axis=new NumberAxis();
    final NumberAxis x2Axis=new NumberAxis();
    final NumberAxis y2Axis=new NumberAxis();

    private final ScatterChart<Number, Number> scatterchart = new ScatterChart<>(xAxis, yAxis);
    private final LineChart<Number, Number> lineChart = new LineChart<>(x1Axis, y1Axis);
    private final BarChart<String,Number> barChart1= new BarChart<>(new CategoryAxis(), new NumberAxis()),barChart2=new BarChart<String, Number>(new CategoryAxis(),new NumberAxis());

    private XYChart.Series<Number,Number> series1 = new XYChart.Series<>();
    private LineChart.Series<Number,Number> series2 = new LineChart.Series<>();
    private BarChart.Series<Number,Number> series3=new BarChart.Series<>();


    @Override
    public void start(Stage primarystage) {

        // File Path Button
        Button filePathButton = new Button(" ... ");
        filePathButton.setOnAction(actionEvent -> openFile(primarystage));

        // File Path Text Field
        TextField filePath = new TextField();
        filePath.setPrefSize(1000, 5);

        // Color Chooser Button
        Button colorButton = new Button("Color Chooser");
        colorButton.setOnAction(actionEvent -> newColor());

        // Labels Combobox
        Label xLabel = new Label("X-Axis: ");
        Label yLabel = new Label("Y-Axis: ");
        Label sizeLabel = new Label("Bubble Size: ");
        // X Combobox
        ComboBox xComboBox = new ComboBox();
        xComboBox.getItems().addAll(
                "Variable X", "Variable Y", "Variable Z"
        );
        xComboBox.setValue("Variable X");
        // Y Combobox
        ComboBox yComboBox = new ComboBox();
        yComboBox.getItems().addAll(
                "Variable X", "Variable Y", "Variable Z"
        );
        yComboBox.setValue("Variable Y");
        // Z Combobox
        ComboBox zComboBox = new ComboBox();
        zComboBox.getItems().addAll(
                "", "Variable X", "Variable Y", "Variable Z"
        );
        zComboBox.setValue("");

        StackPane scatterPane = new StackPane(lineChart, scatterchart);

        // Layout

        // First Line HBox
        HBox firstLine = new HBox();
        firstLine.getChildren().addAll(filePath, filePathButton);
        firstLine.setAlignment(Pos.CENTER_RIGHT);
        firstLine.setSpacing(10);
        firstLine.setPadding(new javafx.geometry.Insets(5, 5, 5, 5));
        firstLine.setStyle("-fx-background-color: white;");

        // Second Line HBox
        HBox secondLine = new HBox();
        secondLine.getChildren().addAll(xLabel, xComboBox, yLabel, yComboBox, sizeLabel, zComboBox, colorButton);
        secondLine.setAlignment(Pos.CENTER_RIGHT);
        secondLine.setSpacing(10);
        secondLine.setPadding(new javafx.geometry.Insets(5, 5, 5, 5));
        secondLine.setStyle("-fx-background-color: white;");

        VBox pane = new VBox();
        pane.getChildren().addAll(firstLine, secondLine, scatterchart, lineChart, barChart1, barChart2);
        //pane.getChildren().addAll(firstLine, secondLine, scatterPane, barChart1, barChart2); funktioniert nicht!
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setSpacing(10);
        pane.setPadding(new javafx.geometry.Insets(5, 5, 5, 5));
        pane.setStyle("-fx-background-color: white;");

        StackPane stackpane = new StackPane();
        stackpane.getChildren().add(pane);

        // Scene
        Scene scene = new Scene(stackpane, 750, 750);

        // Stage
        primarystage.setTitle("Datenvisualisierung");
        primarystage.setMinHeight(1000);
        primarystage.setMinWidth(1000);
        //primarystage.setMaxHeight(1000);
        //primarystage.setMaxWidth(1000);
        primarystage.setScene(scene);
        primarystage.show();
    }

    private void openFile(Stage primarystage) {
        //select a line or tab oriented file
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Resource File");
        fc.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("LineOriented", "*.lin"),
        new FileChooser.ExtensionFilter("TabOriented", "*.txt")
        );
        //read and parse Data
        File selectedFile = fc.showOpenDialog(primarystage);
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
        XYChart(mydata);
        lineChart();
        histogram(barChart1,firstvariable);
        histogram(barChart2,secondvariable);
    }

    // JColorChooser ersetzen!!!
    public static void newColor() {
        Color newColor = JColorChooser.showDialog(null, "Choose a color", null);
        //ColorPicker newColor = new ColorPicker();
        return;
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
    void XYChart(Data mydata) {
        //scatterchart.getData().clear(); funktioniert nicht
        //put data in XY-scatterchart
        for (int i = 0; i < dataFirstvariable.length; i++) {
            series1.getData().add(new XYChart.Data<>(dataFirstvariable[i], dataSecondvariable[i]));


        }
        //hinzufügen des Scattercharts, eventuell noch verschieben

        scatterchart.getData().add(series1);



    }

    void lineChart() {
        for (int i = 0; i < dataFirstvariable.length; i++) {
            series2.getData().add(new XYChart.Data<>(dataFirstvariable[i], dataSecondvariable[i]));}
        lineChart.getData().add(series2);


    }
    //histogram
    BarChart<String, Number> histogram(BarChart<String,Number>chrt, String varName){
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
            xAxis[i]=String.format(".%2f-.%2f",(min+i*range/numBins),(min+(i+1)*range/numBins));
        }
        XYChart.Series s=new XYChart.Series<>();
        for(int i=0;i<numBins;i++){
            s.getData().add(new XYChart.Data(xAxis[i], ret[i]));
        }
        chrt.getData().clear();
        chrt.getData().addAll(s);
        chrt.setTitle("Histogram for "+varName);
        chrt.setLegendVisible(false);
        chrt.setCategoryGap(0);
        chrt.setBarGap(0);
        chrt.setStyle("-fx-background-color: transparent;");


        return chrt;
    }

}




















