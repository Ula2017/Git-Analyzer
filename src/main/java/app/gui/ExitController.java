package app.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Created by Karol on 2017-12-10.
 */

public class ExitController extends IController {
    private Popup popup;

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
        popup.setWidth(600);
        popup.setHeight(400);

        GridPane exitGrid = getAbstractGrid(Color.AZURE);
        popup.getContent().add(exitGrid);

        exitGrid.setPadding(new Insets(50, 50, 50, 50));

        Text exitText = getText("Are you sure to exit?", 50);
        exitGrid.add(exitText, 0,0);

        HBox exitHBox = new HBox(50);
        exitGrid.add(exitHBox, 0,1);
        exitHBox.setStyle("-fx-font: 40 Tahoma");
        exitHBox.setAlignment(Pos.CENTER);

        Button yesButton = getButton("Yes", 150, 35, () -> {
            Platform.exit();
            System.exit(0);
        });
        Button noButton = getButton("No", 150, 35, this::hidePopup);
        exitHBox.getChildren().addAll(yesButton, noButton);

        return popup;
    }
}
