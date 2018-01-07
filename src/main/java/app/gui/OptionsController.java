package app.gui;

import com.google.inject.Injector;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Created by Karol on 2017-12-10.
 */

public class OptionsController extends AbstractController {
    public OptionsController(){
        this.scene = createScene();
    }

    @Override
    public void show() {
        changeScene(this.scene);
    }

    @Override
    Scene createScene() {
        Injector injector = AbstractController.injector;

        GridPane optionsGrid = getAbstractGrid();

        VBox optionsBox = new VBox(50);
        optionsBox.setMinHeight(700);
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setStyle("-fx-font: 40 Tahoma");
        optionsGrid.add(optionsBox, 0,0);

        optionsBox.getChildren().addAll(
                getText("Options", 70),
                getButton("Back", 350, 55,
                        () -> injector.getInstance(MainMenuController.class).show())
        );

        return new Scene(optionsGrid, primaryStage.getWidth(), primaryStage.getHeight());
    }
}