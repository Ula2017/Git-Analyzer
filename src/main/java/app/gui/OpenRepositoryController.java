package app.gui;

import app.fetch.RepositoryOpener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Karol on 2017-12-10.
 */

public class OpenRepositoryController extends IController {
    private IController analysisMenuController;
    private IController mainController;
    private RepositoryOpener repositoryOpener;

    public OpenRepositoryController(Stage primaryStage, IController mainController){
        this.primaryStage = primaryStage;
        this.scene = createScene();
        this.analysisMenuController = new ModulesMenuController(primaryStage, this);
        this.mainController = mainController;
        this.repositoryOpener = RepositoryOpener.getInstance();
    }

    @Override
    public void show() {
        changeScene(primaryStage, this.scene);
    }

    @Override
    Scene createScene() {
        GridPane openRepositoryGrid = getAbstractGrid(Color.WHITE);

        VBox openRepositoryBox = new VBox(50);
        openRepositoryGrid.add(openRepositoryBox, 0,0);
        openRepositoryBox.setMinHeight(700);
        openRepositoryBox.setAlignment(Pos.CENTER);
        openRepositoryBox.setStyle("-fx-font: 40 Tahoma");

        Text openRepositoryTitle = getText("Open repository", 70);
        openRepositoryBox.getChildren().add(openRepositoryTitle);

        Text repoPath = getText("Path to repository (git file):", 70);
        openRepositoryBox.getChildren().add(repoPath);

        TextField repoPathTextField = new TextField();
        repoPathTextField.setPrefHeight(40);
        openRepositoryBox.getChildren().add(repoPathTextField);

        Button openRepositoryButton = getButton("Open repository", 350, 55, () -> {
                    repositoryOpener.setRepoUrl(repoPathTextField.getText());

                    if (repositoryOpener.checkIfExistsRemote()) {
                        repoPathTextField.clear();
                        repositoryOpener.getRepo();
                        this.analysisMenuController.show();
                    } else {
                        repositoryOpener.setRepoUrl(null);
                        repoPathTextField.setStyle("-fx-border-color: red");
                    }

                }
        );
        openRepositoryBox.getChildren().add(openRepositoryButton);
        Button openRepositoryBackButton = getButton("Back", 350, 55, () -> { repoPathTextField.clear(); mainController.show(); });
        openRepositoryBox.getChildren().add(openRepositoryBackButton);

        return new Scene(openRepositoryGrid, width, heigth);
    }

    public RepositoryOpener getRepositoryOpener() {
        return repositoryOpener;
    }
}