package app.gui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;

/**
 * Created by Karol on 2017-12-10.
 */

public class ContactController extends IController {

    public ContactController(){
        this.scene = createScene();

    }

    @Override
    public void show() {
        changeScene( this.scene);
    }

    @Override
    Scene createScene() {
        GridPane contactGrid = getAbstractGrid(Color.WHITE);

        VBox contactBox = new VBox(50);
        contactGrid.add(contactBox, 0,0);
        contactBox.setMinHeight(700);
        contactBox.setAlignment(Pos.CENTER);
        contactBox.setStyle("-fx-font: 40 Tahoma");

        Text contactTitle = getText("Authors", 70);
        contactBox.getChildren().add(contactTitle);

        Arrays.asList("Karol Bartyzel", "Karolina Biela", "Agata Bogacz", "Barbara Chraścik",
                "Justyna Maciąg", "Urszula Soboń")
                .forEach((x) -> {
                    contactBox.getChildren().add(getText(x, 50));
                });
        Injector injector = IController.injector;
        Button contactBackButton = getButton("Back", 350, 55,
                () -> injector.getInstance(MainController.class).show());
        contactBox.getChildren().add(contactBackButton);

        return new Scene(contactGrid,primaryStage.getWidth(), primaryStage.getHeight());
    }
}