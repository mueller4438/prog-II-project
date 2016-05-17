package ch.fhnw.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

public final class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private String  firstvariable,secondvariable;
    private Double[] dataFirstvariable,dataSecondvariable;

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
                System.out.print(mydata);
            } else if (selectedFile.getName().endsWith(".lin")) {
                Data mydata = new LineReader().parseContents(selectedFile);
                System.out.print(mydata);
            }

        } catch (DataReaderException e) {
            showErrorMessage(e.getMessage());

        }
        // Layout
        StackPane pane = new StackPane();

        primarystage.setTitle("Datenvisualisierung");
        Scene scene = new Scene(pane);
        primarystage.setScene(scene);
        primarystage.show();
    }


    private void showErrorMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An Error occurred: ");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    public void XYChart(Data data) {
        //get variable names
        Set<String> vars = data.getVariableNames();
        Iterator<String> iter = vars.iterator();
        firstvariable = iter.next();
        if(iter.hasNext()){
            secondvariable=iter.next();}
        else firstvariable=secondvariable;
        //get values for variable
        dataForVariable(data);
        XYChart.Series series1 = new XYChart.Series<>();
        for(int i=0;i<dataFirstvariable.length;i++)
        series1.getData().add(new XYChart.Data(dataFirstvariable[i],dataSecondvariable[i]));




        }

    private void dataForVariable(Data data) {
        dataFirstvariable= data.getDataForVariable(firstvariable);
        dataSecondvariable=data.getDataForVariable(secondvariable);
    }

}










