package app.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;

/**
 * Created by Karol on 2017-12-10.
 */

public class ExitController extends AbstractController {
    private final Popup popup;
    private final int width = 600;
    private final int height = 400;
    public ExitController(){
        this.popup = createPopup();
    }

    @Override
    public void show() {
        popup.show(primaryStage);
    }

    @Override
    Scene createScene(){
        return null;
    }

    private void hidePopup(){
        popup.hide();
    }

    private Popup createPopup() {
        final Popup popup = new Popup();

        GridPane exitGrid = getAbstractGrid(Color.WHEAT);
        exitGrid.setPadding(new Insets(50, 50, 50, 50));
        popup.getContent().add(exitGrid);

        Text exitText = getText("Are you sure to exit?", 50);
        exitGrid.add(exitText, 0,0);

        HBox exitHBox = new HBox(50);
        exitHBox.setStyle("-fx-font: 40 Tahoma");
        exitHBox.setAlignment(Pos.CENTER);
        exitGrid.add(exitHBox, 0,1);

        exitHBox.getChildren().addAll(
                getButton("Yes", 150, 35, () -> {
                    Platform.exit();
                    System.exit(0);
                }),
                getButton("No", 150, 35, this::hidePopup)
        );

        return popup;
    }
}
