package app.gui;

import com.sun.glass.ui.Screen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Git Analyzer");
        primaryStage.setResizable(false);
        primaryStage.setMinWidth(Screen.getMainScreen().getVisibleWidth());
        primaryStage.setMinHeight(Screen.getMainScreen().getVisibleHeight());
        IController.width = Screen.getMainScreen().getVisibleWidth();
        IController.heigth = Screen.getMainScreen().getVisibleHeight();

        new MainController(primaryStage).show();
    }
}