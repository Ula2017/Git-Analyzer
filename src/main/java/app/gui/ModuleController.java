package app.gui;

import app.analysis.AbstractAnalyzerModule;
import app.analysis.AnalysisFactory;
import app.structures.GUIDetails;
import com.google.inject.Inject;
import com.google.inject.Provider;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.joda.time.DateTime;

public class ModuleController extends AbstractController {
    private AbstractAnalyzerModule module;
    private DateTime from;
    private DateTime to;
    private String committerName;
    private AnalysisFactory analysisFactory;
    private DialogController dialogController;
    private Provider<ModulesMenuController> modulesMenuController;

    @Inject
    public ModuleController(AnalysisFactory analysisFactory, DialogController d,
                            Provider<ModulesMenuController> m) {

        this.analysisFactory = analysisFactory;
        this.dialogController = d;
        this.modulesMenuController = m;
    }

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
        GridPane moduleGrid = getAbstractGrid();

        VBox moduleBox = new VBox(50);
        moduleBox.setMinHeight(700);
        moduleBox.setAlignment(Pos.CENTER);
        moduleBox.setStyle("-fx-font: 40 Tahoma");
        moduleGrid.add(moduleBox, 0,0);

        Node node = null;
        try { node = analysisFactory.generateNode(module, new GUIDetails(from, to, committerName));}
        catch (Exception e) { dialogController.createExceptionDialog(e);}

        moduleBox.getChildren().addAll(
                getText(module.toString(), 70),
                node,
                getButton("Back", 450, 55,
                        () -> modulesMenuController.get().show())
        );

        return new Scene(moduleGrid, primaryStage.getWidth(), primaryStage.getHeight());
    }
}