package app.gui;

import app.analysis.AbstractAnalyzerModule;
import app.fetch.Fetcher;
import app.structures.GUIDetails;
import com.google.inject.Injector;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.joda.time.DateTime;

public class ModuleController extends AbstractController {
    private AbstractAnalyzerModule module;
    private DateTime from;
    private DateTime to;
    private String committerName;
    

    public ModuleController() {}

    public void setGUIDetails(GUIDetails guiDetails){
        this.from = guiDetails.getFrom();
        this.to = guiDetails.getTo();
        this.committerName = guiDetails.getCommitterName();
    }

    public void setModule(AbstractAnalyzerModule module){
        this.module = module;
    }

    @Override
    public void show() {
        changeScene( createScene());
    }

    @Override
    Scene createScene() {
        Injector injector = AbstractController.injector;
        Fetcher fetcher = injector.getInstance(Fetcher.class);

        GridPane moduleGrid = getAbstractGrid();

        VBox moduleBox = new VBox(50);
        moduleBox.setMinHeight(700);
        moduleBox.setAlignment(Pos.CENTER);
        moduleBox.setStyle("-fx-font: 40 Tahoma");
        moduleGrid.add(moduleBox, 0,0);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(600);
        moduleBox.getChildren().addAll(
                getText(module.toString(), 70),
                imageView,
                getButton("Back", 450, 55,
                        () -> injector.getInstance(ModulesMenuController.class).show())
        );

        try {
            imageView.setImage(new Image(module.generateFile(fetcher.getCommitsFromDateRange(from, to), new GUIDetails(from, to, committerName)).toURI().toURL().toString()));
        } catch (Exception e) {
            DialogController exController = injector.getInstance(DialogController.class);
            exController.createExceptionDialog(e);
        }

        return new Scene(moduleGrid, primaryStage.getWidth(), primaryStage.getHeight());
    }
}