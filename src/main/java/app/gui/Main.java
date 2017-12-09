package app.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Git Analyzer");
        primaryStage.setResizable(false);
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());

        Controller controller = new Controller(primaryStage);
        controller.start();
    }
}