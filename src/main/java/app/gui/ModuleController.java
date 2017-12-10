package app.gui;

import app.analysis.IAnalyzerModule;
import app.analysis.AnalyzerProvider;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Karol on 2017-12-10.
 */
public class ModuleController extends IController{
    private IController analysisMenuController;
    private String analysisName;

    public ModuleController(Stage primaryStage, AnalysisMenuController analysisMenuController) {
        this.primaryStage = primaryStage;
        this.analysisMenuController = analysisMenuController;
    }

    @Override
    public void show() {
        changeScene(primaryStage, createScene());
    }

    @Override
    public void show(Object data) {
        analysisName = data.toString();
        show();
    }

    @Override
    Scene createScene() {
        GridPane moduleGrid = getAbstractGrid(Color.WHITE);

        VBox moduleBox = new VBox(50);
        moduleBox.setMinHeight(700);
        moduleGrid.add(moduleBox, 0,0);
        moduleBox.setAlignment(Pos.CENTER);
        moduleBox.setStyle("-fx-font: 40 Tahoma");

        Text analysisTitle = getText(analysisName, 70);
        moduleBox.getChildren().add(analysisTitle);

        IAnalyzerModule module = AnalyzerProvider.getModule(analysisName);
        Image image = new Image(module.generateFile());
        ImageView imageView = new ImageView();
        imageView.setFitWidth(600);
        moduleBox.getChildren().add(imageView);
        imageView.setImage(image);

        Button moduleBackButton = getButton("Back", 450, 55, () -> this.analysisMenuController.show());
        moduleBox.getChildren().add(moduleBackButton);

        return new Scene(moduleGrid, width, heigth);
    }
}
