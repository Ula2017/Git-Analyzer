package app.gui;

import app.analysis.Analyzer;
import app.fetch.Fetcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.joda.time.DateTime;

import java.time.LocalDate;

/**
 * Created by Karol on 2017-12-10.
 */

public class ModulesMenuController extends IController {
    private IController openRepositoryController;
    private ModuleController moduleController;
    private String moduleName;
    private DatePicker fromDatePicker;
    private DatePicker toDatePicker;
    private Button moduleGenerateButton;
    private Button moduleChangeRepositoryButton;

    public ModulesMenuController(Stage primaryStage, IController openRepositoryController, Fetcher f){
        this.primaryStage = primaryStage;
        this.scene = createScene();
        this.openRepositoryController = openRepositoryController;
        this.moduleController = new ModuleController(primaryStage, this, f);
    }

    @Override
    public void show() {
        changeScene(primaryStage, scene);
    }

    @Override
    Scene createScene() {
        GridPane moduleMenuGrid = getAbstractGrid(Color.WHITE);

        VBox moduleMenuBox = new VBox(120);
        moduleMenuBox.setMinHeight(700);
        moduleMenuGrid.add(moduleMenuBox, 0,0);
        moduleMenuBox.setAlignment(Pos.CENTER);
        moduleMenuBox.setStyle("-fx-font: 40 Tahoma");

        Analyzer analyzer = new Analyzer();
        ComboBox comboBox = new ComboBox(FXCollections.observableArrayList(analyzer.getModulesNames()));
        comboBox.getSelectionModel().selectFirst();
        moduleName = comboBox.getSelectionModel().getSelectedItem().toString();
        comboBox.setOnAction(x -> {
            moduleName = comboBox.getSelectionModel().getSelectedItem().toString();
            x(moduleName, moduleMenuBox);
        });
        moduleMenuBox.getChildren().add(comboBox);
        comboBox.setVisibleRowCount(5);
        toDatePicker = new DatePicker(LocalDate.now());
        fromDatePicker = new DatePicker(LocalDate.now());
        fromDatePicker.setOnAction(x -> {
            LocalDate date = fromDatePicker.getValue();
            fromDatePicker.setValue(LocalDate.of(date.getYear(), date.getMonth(), 1));
            System.out.println(fromDatePicker.getValue());
        });
        toDatePicker.setOnAction(t -> {
            LocalDate date = toDatePicker.getValue();
            toDatePicker.setValue(LocalDate.of(date.getYear(), date.getMonth(), date.lengthOfMonth()));
        });
        moduleGenerateButton = getButton("Generate", 450, 55, () -> {
            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();
            this.moduleController.setDates(new DateTime(fromDate.getYear(), fromDate.getMonthValue(), fromDate.getDayOfMonth(), 0, 0),
                    new DateTime(toDate.getYear(), toDate.getMonthValue(), toDate.getDayOfMonth(), 0, 0));
            this.moduleController.show(comboBox.getSelectionModel().getSelectedItem());
        });

        moduleChangeRepositoryButton = getButton("Change Repository", 450, 55, () -> this.openRepositoryController.show());
        x(moduleName, moduleMenuBox);

        return new Scene(moduleMenuGrid, width, heigth);
    }

    private void x(String moduleName, VBox moduleBox){
        ObservableList<Node> children = moduleBox.getChildren();
        children.removeAll(fromDatePicker, toDatePicker, moduleGenerateButton, moduleChangeRepositoryButton);

        switch (moduleName){
            case "Monthly ammount of commiters":
                children.add(fromDatePicker);
                children.add(toDatePicker);
                children.add(moduleGenerateButton);
                children.add(moduleChangeRepositoryButton);
                break;
            case "Module 2":
                children.add(moduleGenerateButton);
                children.add(moduleChangeRepositoryButton);
                break;
        }
    }
}