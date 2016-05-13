package ch.fhnw.project;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.function.Supplier;

public final class App extends Application {

    @Override
    public void start(Stage stage) {
        // Asserts correct project setup (will only compile with Java 8)
        Supplier<String> helloWorldSupplier = () -> "Hello World";
        System.out.println(helloWorldSupplier.get());
    }



}
