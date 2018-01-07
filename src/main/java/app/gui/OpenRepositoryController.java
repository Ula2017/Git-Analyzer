package app.gui;

import app.fetch.Fetcher;
import app.fetch.RepositoryModule;
import app.fetch.URLReader;
import app.structures.CommitDetails;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.sound.midi.SysexMessage;
import java.util.List;

/**
 * Created by Karol on 2017-12-10.
 */

public class OpenRepositoryController extends IController {

    public OpenRepositoryController(){
        this.scene = createScene();
    }

    @Override
    public void show() {
        changeScene(this.scene);
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
        Injector injector = IController.injector;

        Button openRepositoryButton = getButton("Open repository", 350, 55, () -> {

                    if (URLReader.checkIfExistsRemote(repoPathTextField.getText())) {

                        Fetcher fetcher = injector.getInstance(Fetcher.class);
                        fetcher.prepereDownloader(repoPathTextField.getText());
                        repoPathTextField.clear();
                        fetcher.getAllCommits();
//                        List<CommitDetails> results= (List<CommitDetails>)(List<?>)f.getAllCommits();
//                        for (CommitDetails r : results) {
//                            System.out.println(r.getCommitDate() + " " + r.getAuthorName() + " " + r.getCommitMessage());
//                        }
                        injector.getInstance(ModulesMenuController.class).show();

                    } else {
                        repoPathTextField.setStyle("-fx-border-color: red");
                    }

                }
        );
        openRepositoryBox.getChildren().add(openRepositoryButton);
        Button openRepositoryBackButton = getButton("Back", 350, 55,
                () -> { repoPathTextField.clear(); injector.getInstance(MainController.class).show();
        });
        openRepositoryBox.getChildren().add(openRepositoryBackButton);

        return new Scene(openRepositoryGrid, primaryStage.getWidth(), primaryStage.getHeight());
    }

}