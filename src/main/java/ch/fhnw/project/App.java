package ch.fhnw.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import java.util.function.Supplier;

public final class App extends Application {
    public App() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    private String  firstvariable,secondvariable;
    private Double[] dataFirstvariable,dataSecondvariable;
    final NumberAxis xAxis = new NumberAxis();
    final NumberAxis yAxis = new NumberAxis();
    private final ScatterChart<Number, Number> scatterchart=new ScatterChart<Number, Number>(xAxis,yAxis);
    private HBox scatter;
    private XYChart.Series series1 = new XYChart.Series<>();





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

            }
            else if (selectedFile.getName().endsWith(".lin")) {
                Data mydata = new LineReader().parseContents(selectedFile);
                getVariable(mydata);
                getData(mydata);
                XYChart(mydata);

            }

        } catch (DataReaderException e) {
            showErrorMessage(e.getMessage());

        }
        // Layout
        VBox pane = new VBox();
        pane.getChildren().addAll(scatterchart);

        StackPane stackpane=new StackPane();
        stackpane.getChildren().add(pane);

        primarystage.setTitle("Datenvisualisierung");
        Scene scene = new Scene(stackpane);
        primarystage.setScene(scene);
        primarystage.show();
    }

    private void getData(Data mydata) {
        dataFirstvariable=mydata.getDataForVariable(firstvariable);
        dataSecondvariable=mydata.getDataForVariable(secondvariable);
    }

    private void getVariable(Data mydata) {

        //get variable names
        Set<String> vars = mydata.getVariableNames();
        Iterator<String> iter = vars.iterator();
        firstvariable = iter.next();
        if(iter.hasNext()){
            secondvariable=iter.next();
            System.out.print(iter);
        }
        else firstvariable=secondvariable;

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

    //Scatterchart, put data in scatterchart
    public void XYChart(Data mydata) {

        //put Data in scatterchart
        for(int i=0;i<dataFirstvariable.length;i++){
            series1.getData().add(new XYChart.Data(dataFirstvariable[i],dataSecondvariable[i]));

        }
        //
        scatterchart.getData().addAll(series1);
        scatter=new HBox();
        scatter.getChildren().addAll(scatterchart);






        }
    public void scaleaxis(){

    }



    //catch values for specific variables
   /* private void dataForVariable(Data data) {
        dataFirstvariable= data.getDataForVariable(firstvariable);
        dataSecondvariable=data.getDataForVariable(secondvariable);
    }*/

}










