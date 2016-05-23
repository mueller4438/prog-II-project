package ch.fhnw.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
        // Layout
        VBox pane = new VBox();
        pane.getChildren().addAll(scatterchart, lineChart, barChart1, barChart2);

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
    void XYChart(Data mydata) {

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
        chrt.setTitle("Histogram for"+varName);
        chrt.setLegendVisible(false);


        return chrt;
    }

}




















