package app.gui;

import com.google.inject.Injector;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainMenuController extends AbstractController {
    public MainMenuController(){
        this.scene = createScene();
    }

    @Override
    public void show() {
        changeScene(this.scene);
    }

    @Override
    Scene createScene() {
        Injector injector = AbstractController.injector;

        GridPane mainMenuGrid = getAbstractGrid();
        mainMenuGrid.add(getText("Welcome to Git Analyzer", 70), 0,0);

        VBox mainMenuBox = new VBox(50);
        mainMenuBox.setAlignment(Pos.CENTER);
        mainMenuBox.setStyle("-fx-font: 40 Tahoma");
        mainMenuGrid.add(mainMenuBox, 0,1);

        mainMenuBox.getChildren().addAll(
            getButton("Open repository", 350, 55,
                    () -> injector.getInstance(OpenRepositoryController.class).show()),
            getButton("Options", 350, 55,
                    () -> injector.getInstance(OptionsController.class).show()),
            getButton("Contact", 350, 55,
                    () -> injector.getInstance(ContactController.class).show()),
            getButton("Exit", 350, 55,
                    () -> injector.getInstance(DialogController.class).createExitDialog())
        );

        return new Scene(mainMenuGrid, primaryStage.getWidth(), primaryStage.getHeight());
    }
}