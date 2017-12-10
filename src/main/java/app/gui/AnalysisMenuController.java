package app.gui;

import app.analysis.AnalyzerProvider;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by Karol on 2017-12-10.
 */

public class AnalysisMenuController extends IController {
    private IController openRepositoryController;
    private ModuleController analysisController;

    public AnalysisMenuController(Stage primaryStage, IController openRepositoryController){
        this.primaryStage = primaryStage;
        this.scene = createScene();
        this.openRepositoryController = openRepositoryController;
        this.analysisController = new ModuleController(primaryStage, this);
    }

    @Override
    public void show() {
        changeScene(primaryStage, scene);
    }

    @Override
    Scene createScene() {
        GridPane analysisMenuGrid = getAbstractGrid(Color.WHITE);

        VBox analysisMenuBox = new VBox(350);
        analysisMenuBox.setMinHeight(700);
        analysisMenuGrid.add(analysisMenuBox, 0,0);
        analysisMenuBox.setAlignment(Pos.CENTER);
        analysisMenuBox.setStyle("-fx-font: 40 Tahoma");

        ComboBox comboBox = new ComboBox(FXCollections.observableArrayList(AnalyzerProvider.getModulesNames()));
        comboBox.getSelectionModel().selectFirst();
        analysisMenuBox.getChildren().add(comboBox);
        comboBox.setVisibleRowCount(5);

        Button analysisGenerateButton = getButton("Generate", 450, 55, () -> this.analysisController.show(comboBox.getSelectionModel().getSelectedItem()));
        analysisMenuBox.getChildren().add(analysisGenerateButton);

        Button analysisChangeRepositoryButton = getButton("Change Repository", 450, 55, () -> this.openRepositoryController.show());
        analysisMenuBox.getChildren().add(analysisChangeRepositoryButton);

        return new Scene(analysisMenuGrid, width, heigth);
    }
}
