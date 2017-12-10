package app.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Karol on 2017-12-10.
 */

public class OptionsController extends IController {
    private IController mainController;

    public OptionsController(Stage primaryStage, IController mainController){
        this.primaryStage = primaryStage;
        this.scene = createScene();
        this.mainController = mainController;
    }

    @Override
    public void show() {
        changeScene(primaryStage, this.scene);
    }

    @Override
    Scene createScene() {
        GridPane optionsGrid = getAbstractGrid(Color.WHITE);

        VBox optionsBox = new VBox(50);
        optionsBox.setMinHeight(700);
        optionsGrid.add(optionsBox, 0,0);
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setStyle("-fx-font: 40 Tahoma");

        Text optionsTitle = getText("Options", 70);
        optionsBox.getChildren().add(optionsTitle);

        Button optionsBackButton = getButton("Back", 350, 55, () -> mainController.show());
        optionsBox.getChildren().add(optionsBackButton);

        return new Scene(optionsGrid, width, heigth);
    }
}
