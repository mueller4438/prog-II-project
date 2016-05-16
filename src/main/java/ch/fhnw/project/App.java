package ch.fhnw.project;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.function.Supplier;

public final class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primarystage) {

        //select a line or tab oriented file
        FileChooser fc=new FileChooser();
        fc.setTitle("Open Resource File");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("LineOriented", "*.lin"),
                new FileChooser.ExtensionFilter("TabOriented", "*.txt")
        );
        //read Data
        File selectedFile=fc.showOpenDialog(primarystage);
        try {
            if(selectedFile.getName().endsWith(".txt")){
                Data mydata = new TabReader().parseContents(selectedFile);
                System.out.print(mydata);
            }
            else if(selectedFile.getName().endsWith(".lin")){
                Data mydata = new LineReader().parseContents(selectedFile);
                System.out.print(mydata);
            }

        } catch (DataReaderException e) {
            showErrorMessage(e.getMessage());

        }
    }


    private void showErrorMessage(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An Error occurred: ");
        alert.setContentText(msg);

        alert.showAndWait();
    }





}
