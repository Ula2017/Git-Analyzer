package app.gui;

import app.analysis.*;
import app.fetch.Fetcher;
import app.fetch.RepositoryModule;
import app.structures.CommitDetails;
import app.structures.ModuleNames;
import com.google.inject.Guice;
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
    private ModuleNames moduleName;
    private String committerName;
    private DateTime fromDate;
    private DateTime toDate;

    public ModuleController() {
    }

    public void setDates(DateTime from, DateTime to){
        this.fromDate = from;
        this.toDate = to;
    }

    public void setCommitterName(String committerName){
        this.committerName = committerName;
    }

    @Override
    public void show() {
        changeScene( createScene());
    }

    @Override
    public void show(Object moduleName) {
        this.moduleName = (ModuleNames) moduleName;
        show();
    }

    @Override
    Scene createScene() {
        GridPane moduleGrid = getAbstractGrid(Color.WHITE);
        Injector injector = IController.injector;
        Fetcher fetcher = injector.getInstance(Fetcher.class);
        //to check
        List<CommitDetails> results= fetcher.getAllCommits();
        for (CommitDetails r : results) {
            System.out.println(r.getCommitDate() + " " + r.getAuthorName() + " " + r.getCommitMessage());
        }

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
                    MonthlyAuthorsCounter module1 = (MonthlyAuthorsCounter) module;
                    image = new Image(module1.generateFile(fetcher.getAllCommits(), fromDate, toDate));
                    imageView.setImage(image);
                    break;
                case MODULE2:
                    RepoCommits module2 = (RepoCommits) module;
                    image = new Image(module2.generateFile(fetcher.getAllCommits()));
                    imageView.setImage(image);
                    break;
                case MODULE3:
                    ProgrammingLanguagesPercentageAnalyzer module3 = (ProgrammingLanguagesPercentageAnalyzer) module;
                    image = new Image(module3.generateFile(fetcher.getDiffsFromTimeRange(committerName, fromDate, toDate)));
                    imageView.setImage(image);
                    break;
            }
        }
        catch(Exception e){
            System.err.println(e.toString());
        }

        Button moduleBackButton = getButton("Back", 450, 55,
                () -> injector.getInstance(ModulesMenuController.class).show());
        moduleBox.getChildren().add(moduleBackButton);

        return new Scene(moduleGrid, primaryStage.getWidth(), primaryStage.getHeight());
    }
}