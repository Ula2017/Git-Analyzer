package app.gui;

import app.analysis.Analyzer;
import app.analysis.AbstractAnalyzerModule;
import app.structures.CommitDetails;
import app.fetch.Fetcher;
import app.structures.ModuleNames;
import com.google.inject.Injector;
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
import org.joda.time.DateTime;

import java.util.List;

public class ModuleController extends IController{
    private IController analysisMenuController;
    private ModuleNames moduleName;
    private DateTime fromDate;
    private DateTime toDate;
    private Injector f;

    public ModuleController(Stage primaryStage, ModulesMenuController modulesMenuController, Injector f) {
        this.primaryStage = primaryStage;
        this.analysisMenuController = modulesMenuController;
        this.f=f;
    }

    public void setDates(DateTime from, DateTime to){
        this.fromDate = from;
        this.toDate = to;
    }

    @Override
    public void show() {
        changeScene(primaryStage, createScene());
    }

    @Override
    public void show(Object moduleName) {
        this.moduleName = (ModuleNames) moduleName;
        show();
    }

    @Override
    Scene createScene() {
        GridPane moduleGrid = getAbstractGrid(Color.WHITE);

        Fetcher fetcher = f.getInstance(Fetcher.class);
        List<CommitDetails> results = fetcher.getAllCommits();

        VBox moduleBox = new VBox(50);
        moduleBox.setMinHeight(700);
        moduleGrid.add(moduleBox, 0,0);
        moduleBox.setAlignment(Pos.CENTER);
        moduleBox.setStyle("-fx-font: 40 Tahoma");

        Text analysisTitle = getText(moduleName.toString(), 70);
        moduleBox.getChildren().add(analysisTitle);

        Analyzer analyzer = new Analyzer();
        AbstractAnalyzerModule module = analyzer.getModule(moduleName);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(600);
        moduleBox.getChildren().add(imageView);
        Image image;

        try {
            switch (moduleName) {
                case MODULE1:
                    module.setToDate(toDate);
                    module.setFromDate(fromDate);
                    image = new Image(module.generateFile(results));
                    imageView.setImage(image);
                    break;
                case MODULE2:
                    image = new Image(module.generateFile(results));
                    imageView.setImage(image);
                    break;
                case MODULE3:
                    module.setToDate(toDate);
                    module.setFromDate(fromDate);
                    image = new Image(module.generateFile(results));
                    imageView.setImage(image);
                    break;
            }
        }
        catch(Exception e){
            System.err.println(e.getStackTrace());
        }

        Button moduleBackButton = getButton("Back", 450, 55, () -> this.analysisMenuController.show());
        moduleBox.getChildren().add(moduleBackButton);

        return new Scene(moduleGrid, width, heigth);
    }
}