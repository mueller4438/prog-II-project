package ch.fhnw.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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
    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    final NumberAxis x1Axis=new NumberAxis();
    final NumberAxis y1Axis=new NumberAxis();
    private final ScatterChart<Number, Number> scatterchart = new ScatterChart<>(xAxis, yAxis);
    private final LineChart<Number, Number> lineChart = new LineChart<>(x1Axis, y1Axis);
    private XYChart.Series<Number,Number> series1 = new XYChart.Series<>();
    private LineChart.Series<Number,Number> series2 = new LineChart.Series<>();


    @Override
    public void start(Stage primarystage) {

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
                Data mydata = new TabReader().parseContents(selectedFile);
                getVariable(mydata);
                getData(mydata);
                XYChart(mydata);
                lineChart();

            } else if (selectedFile.getName().endsWith(".lin")) {
                Data mydata = new LineReader().parseContents(selectedFile);
                getVariable(mydata);
                getData(mydata);
                XYChart(mydata);
                lineChart();

            }

        } catch (DataReaderException e) {
            showErrorMessage(e.getMessage());

        }
        // Layout
        VBox pane = new VBox();
        pane.getChildren().addAll(scatterchart);
        pane.getChildren().addAll(lineChart);

        StackPane stackpane = new StackPane();
        stackpane.getChildren().add(pane);
        //stackpane.getChildren().add();

        primarystage.setTitle("Datenvisualisierung");
        Scene scene = new Scene(stackpane);
        primarystage.setScene(scene);
        primarystage.show();
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
    public void XYChart(Data mydata) {

        //put data in XY-scatterchart
        for (int i = 0; i < dataFirstvariable.length; i++) {
            series1.getData().add(new XYChart.Data<>(dataFirstvariable[i], dataSecondvariable[i]));


        }
        //hinzufügen des Scattercharts, eventuell noch verschieben
        scatterchart.getData().add(series1);


    }

    public void lineChart() {
        for (int i = 0; i < dataFirstvariable.length; i++) {
            series2.getData().add(new XYChart.Data<>(dataFirstvariable[i], dataSecondvariable[i]));}
            lineChart.getData().add(series2);


    }
}




















