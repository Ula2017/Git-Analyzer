package app.gui;

import app.fetch.Fetcher;
import app.fetch.URLReader;
import com.google.inject.Inject;
import com.google.inject.Provider;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Created by Karol on 2017-12-10.
 */

public class OpenRepositoryController extends AbstractController {
    Fetcher fetcher;
    private ModulesMenuController modulesMenuController;
    private DialogController dialogController;
    private Provider<MainMenuController> mainMenuController;

    @Inject
    public OpenRepositoryController(Fetcher f, ModulesMenuController m,
                                    DialogController d, Provider<MainMenuController> ma){

        this.fetcher = f;
        this.modulesMenuController = m;
        this.dialogController = d;
        this.mainMenuController = ma;
        this.scene = createScene();
    }

    @Override
    public void show() {
        changeScene(this.scene);
    }

    @Override
    Scene createScene() {
        GridPane openRepositoryGrid = getAbstractGrid();

        VBox openRepositoryBox = new VBox(50);
        openRepositoryBox.setMinHeight(700);
        openRepositoryBox.setAlignment(Pos.CENTER);
        openRepositoryBox.setStyle("-fx-font: 40 Tahoma");
        openRepositoryGrid.add(openRepositoryBox, 0,0);

        TextField repoPathTextField = new TextField();
        repoPathTextField.setPrefHeight(40);
        Button openRepositoryButton = getButton("Open repository", 350, 55, () -> {
            try {
                if (URLReader.checkIfExistsRemote(repoPathTextField.getText())) {
                    fetcher.prepareDownloader(repoPathTextField.getText());
                    modulesMenuController.show();
                    repoPathTextField.clear();
                } else {
                    repoPathTextField.setStyle("-fx-border-color: red");
                    dialogController.createWarningDialog("Incorrect repository url or connection problem. ");
                    repoPathTextField.setStyle("-fx-border-color: black");
                }
            } catch (Exception e) {
                dialogController.createExceptionDialog(e);

            }
        });
        openRepositoryBox.getChildren().addAll(
                getText("Open repository", 70),
                getText("Path to repository (git file):", 70),
                repoPathTextField,
                openRepositoryButton,
                getButton("Back", 350, 55,
                        () -> { repoPathTextField.clear(); this.mainMenuController.get().show();})
        );

        return new Scene(openRepositoryGrid, primaryStage.getWidth(), primaryStage.getHeight());
    }
}