package app.gui;

import app.iOCModule;
import com.google.inject.Guice;
import com.sun.glass.ui.Screen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Git Analyzer");
        primaryStage.setResizable(false);
        primaryStage.setWidth(Screen.getMainScreen().getVisibleWidth());
        primaryStage.setHeight(Screen.getMainScreen().getVisibleHeight());
        AbstractController.primaryStage = primaryStage;

        AbstractController.injector = Guice.createInjector(new iOCModule());
        AbstractController.injector.getInstance(MainMenuController.class).show();
    }
}