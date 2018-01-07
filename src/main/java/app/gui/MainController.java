package app.gui;

import app.fetch.RepositoryModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainController extends IController {

    public MainController(){
        this.scene = createScene();
    }

    @Override
    public void show() {
        changeScene(this.scene);
    }

    @Override
    Scene createScene() {
        GridPane menuGrid = getAbstractGrid(Color.WHITE);
        Text menuTitle = getText("Welcome to Git Analyzer", 70);
        menuGrid.add(menuTitle, 0,0);

        VBox menuVBox = new VBox(50);
        menuGrid.add(menuVBox, 0,1);
        menuVBox.setAlignment(Pos.CENTER);
        menuVBox.setStyle("-fx-font: 40 Tahoma");

        ObservableList<Node> menuVBoxChildren = menuVBox.getChildren();
        Injector injector = IController.injector;

        Button signInButton = getButton("Open repository", 350, 55,
                () -> injector.getInstance(OpenRepositoryController.class).show());
        menuVBoxChildren.add(signInButton);

        Button optionsButton = getButton("Options", 350, 55,
                () -> injector.getInstance(OptionsController.class).show());
        menuVBoxChildren.add(optionsButton);

        Button contactButton = getButton("Contact", 350, 55,
                () -> injector.getInstance(ContactController.class).show());
        menuVBoxChildren.add(contactButton);

        Button exitButton = getButton("Exit", 350, 55,
                () -> injector.getInstance(ExitController.class).show());
        menuVBoxChildren.add(exitButton);

        return new Scene(menuGrid, primaryStage.getWidth(), primaryStage.getHeight());
    }
}
