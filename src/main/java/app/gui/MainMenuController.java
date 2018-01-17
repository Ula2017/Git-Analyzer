package app.gui;

import com.google.inject.Inject;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainMenuController extends AbstractController {
    private OpenRepositoryController openRepositoryController;
    private ContactController contactController;
    private OptionsController optionsController;
    private DialogController dialogController;

    @Inject
    public MainMenuController(OpenRepositoryController op, ContactController cont,
                              OptionsController opct, DialogController d){

        this.contactController = cont;
        this.optionsController = opct;
        this.dialogController = d;
        this.openRepositoryController = op;
        this.scene = createScene();
    }

    @Override
    public void show() {
        changeScene(this.scene);
    }

    @Override
    Scene createScene() {

        GridPane mainMenuGrid = getAbstractGrid();
        mainMenuGrid.add(getText("Welcome to Git Analyzer", 70), 0,0);

        VBox mainMenuBox = new VBox(50);
        mainMenuBox.setAlignment(Pos.CENTER);
        mainMenuBox.setStyle("-fx-font: 40 Tahoma");
        mainMenuGrid.add(mainMenuBox, 0,1);

        mainMenuBox.getChildren().addAll(
            getButton("Open repository", 350, 55,
                    () -> this.openRepositoryController.show()),
            getButton("Options", 350, 55,
                    () -> this.optionsController.show()),
            getButton("Contact", 350, 55,
                    () -> this.contactController.show()),
            getButton("Exit", 350, 55,
                    () -> this.dialogController.createExitDialog())
        );
        return new Scene(mainMenuGrid, primaryStage.getWidth(), primaryStage.getHeight());
    }
}