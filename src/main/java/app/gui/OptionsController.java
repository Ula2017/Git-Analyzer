package app.gui;

import com.google.inject.Inject;
import com.google.inject.Provider;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Created by Karol on 2017-12-10.
 */

public class OptionsController extends AbstractController {
    private Provider<MainMenuController> mainMenuController;

    @Inject
    public OptionsController(Provider<MainMenuController> mainMenuController){
       this.scene = createScene();
       this.mainMenuController = mainMenuController;
    }

    @Override
    public void show() {
        changeScene(this.scene);
    }

    @Override
    Scene createScene() {
        GridPane optionsGrid = getAbstractGrid();

        VBox optionsBox = new VBox(50);
        optionsBox.setMinHeight(700);
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setStyle("-fx-font: 40 Tahoma");
        optionsGrid.add(optionsBox, 0,0);

        optionsBox.getChildren().addAll(
                getText("Options", 70),
                getButton("Back", 350, 55,
                        () -> this.mainMenuController.get().show())
        );
        return new Scene(optionsGrid, primaryStage.getWidth(), primaryStage.getHeight());
    }
}