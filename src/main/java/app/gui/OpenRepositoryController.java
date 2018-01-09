package app.gui;

import app.fetch.Fetcher;
import app.fetch.URLReader;
import app.structures.CommitDetails;
import app.structures.FileDiffs;
import com.google.inject.Injector;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Karol on 2017-12-10.
 */

public class OpenRepositoryController extends AbstractController {
    public OpenRepositoryController(){
        this.scene = createScene();
    }

    @Override
    public void show() {
        changeScene(this.scene);
    }

    @Override
    Scene createScene() {
        Injector injector = AbstractController.injector;

        GridPane openRepositoryGrid = getAbstractGrid();

        VBox openRepositoryBox = new VBox(50);
        openRepositoryBox.setMinHeight(700);
        openRepositoryBox.setAlignment(Pos.CENTER);
        openRepositoryBox.setStyle("-fx-font: 40 Tahoma");
        openRepositoryGrid.add(openRepositoryBox, 0,0);

        TextField repoPathTextField = new TextField();
        repoPathTextField.setPrefHeight(40);
        Button openRepositoryButton = getButton("Open repository", 350, 55, () -> {
                    if (URLReader.checkIfExistsRemote(repoPathTextField.getText())) {
                        Fetcher fetcher = injector.getInstance(Fetcher.class);
                        fetcher.prepareDownloader(repoPathTextField.getText());
                        injector.getInstance(ModulesMenuController.class).show();
                        repoPathTextField.clear();
                    } else {
                        repoPathTextField.setStyle("-fx-border-color: red");
                    }
                }
        );
        openRepositoryBox.getChildren().addAll(
                getText("Open repository", 70),
                getText("Path to repository (git file):", 70),
                repoPathTextField,
                openRepositoryButton,
                getButton("Back", 350, 55,
                        () -> { repoPathTextField.clear(); injector.getInstance(MainMenuController.class).show();})
        );

        return new Scene(openRepositoryGrid, primaryStage.getWidth(), primaryStage.getHeight());
    }
}