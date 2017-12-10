package app.gui;

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
    private IController openRepositoryController;
    private IController optionsController;
    private IController contactController;
    private IController exitController;


    public MainController(final Stage primaryStage){
        this.primaryStage = primaryStage;
        this.scene = createScene();
        this.openRepositoryController = new OpenRepositoryController(primaryStage, this);
        this.optionsController = new OptionsController(primaryStage, this);
        this.contactController = new ContactController(primaryStage, this);
        this.exitController = new ExitController(primaryStage);
    }

    @Override
    public void show() {
        changeScene(primaryStage, this.scene);
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

        Button signInButton = getButton("Open repository", 350, 55, () -> this.openRepositoryController.show());
        menuVBoxChildren.add(signInButton);

        Button optionsButton = getButton("Options", 350, 55, () -> this.optionsController.show());
        menuVBoxChildren.add(optionsButton);

        Button contactButton = getButton("Contact", 350, 55, () -> this.contactController.show());
        menuVBoxChildren.add(contactButton);

        Button exitButton = getButton("Exit", 350, 55, () -> this.exitController.show());
        menuVBoxChildren.add(exitButton);

        return new Scene(menuGrid, width, heigth);
    }
}
