package app.gui;

import app.analysis.Analyzer;
import app.structures.ModuleNames;
import com.google.inject.Injector;
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
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Karol on 2017-12-10.
 */

public class ModulesMenuController extends IController {
    private IController openRepositoryController;
    private ModuleController moduleController;
    private ModuleNames moduleName;
    private DatePicker fromDatePicker;
    private DatePicker toDatePicker;
    private Button moduleGenerateButton;
    private Button moduleChangeRepositoryButton;

    public ModulesMenuController(Stage primaryStage, IController openRepositoryController, Injector f){
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
        moduleName = Arrays.stream(ModuleNames.values())
                .filter(m -> Objects.equals(m.toString(), comboBox.getSelectionModel().getSelectedItem().toString()))
                .findFirst().get();
        comboBox.setOnAction(x -> {
            moduleName = Arrays.stream(ModuleNames.values())
                    .filter(m -> Objects.equals(m.toString(), comboBox.getSelectionModel().getSelectedItem().toString()))
                    .findFirst().get();
            showAccurateFields(moduleName, moduleMenuBox);
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
        showAccurateFields(moduleName, moduleMenuBox);

        return new Scene(moduleMenuGrid, width, heigth);
    }

    private void showAccurateFields(ModuleNames moduleName, VBox moduleBox){
        ObservableList<Node> children = moduleBox.getChildren();
        children.removeAll(fromDatePicker, toDatePicker, moduleGenerateButton, moduleChangeRepositoryButton);

        switch (moduleName){
            case MODULE1:
                children.add(fromDatePicker);
                children.add(toDatePicker);
                children.add(moduleGenerateButton);
                children.add(moduleChangeRepositoryButton);
                break;
            case MODULE2:
                children.add(moduleGenerateButton);
                children.add(moduleChangeRepositoryButton);
                break;
        }
    }
}