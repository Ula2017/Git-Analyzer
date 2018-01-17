package app.gui;

import app.analysis.AbstractAnalyzerModule;
import app.structures.GUIDetails;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.joda.time.DateTime;

import java.time.LocalDate;
import java.util.Set;

/**
 * Created by Karol on 2017-12-10.
 */

public class ModulesMenuController extends AbstractController {
    private String moduleName;
    private DatePicker fromDatePicker, toDatePicker;
    private TextField authorTextField;
    private Button moduleGenerateButton, moduleChangeRepositoryButton;
    private TextField  committerName;
    private ComboBox comboBox;
    private Set<AbstractAnalyzerModule> analyzerModuleSet;

    @Inject
    public ModulesMenuController(Set<AbstractAnalyzerModule> analyzerModuleSet) {
        this.analyzerModuleSet = analyzerModuleSet;
        this.scene = createScene();
    }

    @Override
    public void show() {
        changeScene(scene);
    }

    @Override
    Scene createScene() {
        Injector injector = AbstractController.injector;

        GridPane modulesMenuGrid = getAbstractGrid();

        VBox modulesMenuBox = new VBox(60);
        modulesMenuBox.setMinHeight(700);
        modulesMenuBox.setAlignment(Pos.CENTER);
        modulesMenuBox.setStyle("-fx-font: 40 Tahoma");
        modulesMenuGrid.add(modulesMenuBox, 0,0);

   //     Analyzer analyzer = new Analyzer();
   //     comboBox = new ComboBox(FXCollections.observableArrayList(analyzer.getModulesNames()));

        comboBox = new ComboBox(FXCollections.observableArrayList(analyzerModuleSet));
        comboBox.setVisibleRowCount(5);
        comboBox.getSelectionModel().selectFirst();
        comboBox.setOnAction(x -> {
        	moduleName = comboBox.getSelectionModel().getSelectedItem().toString();
        	if (moduleName.equals("Authors Commits Analyzer")){
//                	System.out.println(moduleName);
                	showAccurateFields(moduleName, modulesMenuBox);
        	}
        });
        

//        moduleName = Arrays.stream(ModuleNames.values())
//                .filter(m -> Objects.equals(m.toString(), comboBox.getSelectionModel().getSelectedItem().toString()))
//                .findFirst().get();
//        comboBox.setOnAction(x -> {
//            moduleName = Arrays.stream(ModuleNames.values())
//                    .filter(m -> Objects.equals(m.toString(), comboBox.getSelectionModel().getSelectedItem().toString()))
//                    .findFirst().get();
//            showAccurateFields(moduleName, modulesMenuBox);
//        });
//        modulesMenuBox.getChildren().add(comboBox);


        fromDatePicker = new DatePicker(LocalDate.now());
        fromDatePicker.setOnAction(x -> {
            LocalDate date = fromDatePicker.getValue();
            fromDatePicker.setValue(LocalDate.of(date.getYear(), date.getMonth(), 1));
        });
        toDatePicker = new DatePicker(LocalDate.now());
        toDatePicker.setOnAction(t -> {
            LocalDate date = toDatePicker.getValue();
            toDatePicker.setValue(LocalDate.of(date.getYear(), date.getMonth(), date.lengthOfMonth()));
        });
        
        committerName = new TextField();
        committerName.setOnAction(t -> {
        	committerName.getText();
        });
        moduleGenerateButton = getButton("Generate", 450, 55, () -> {
            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();
            ModuleController moduleController = injector.getInstance(ModuleController.class);
            moduleController.setGUIDetails(new GUIDetails(
                    new DateTime(fromDate.getYear(), fromDate.getMonthValue(), fromDate.getDayOfMonth(), 0, 0),
                    new DateTime(toDate.getYear(), toDate.getMonthValue(), toDate.getDayOfMonth(), 0, 0),
                    committerName.getText()
            ));
            moduleController.setModule((AbstractAnalyzerModule) comboBox.getSelectionModel().getSelectedItem());
            moduleController.show();
        });
        
     
        moduleChangeRepositoryButton = getButton("Change Repository", 450, 55,
                () -> injector.getInstance(OpenRepositoryController.class).show());
        authorTextField = new TextField();
        authorTextField.setPrefHeight(40);

        showAccurateFields(modulesMenuBox);

        return new Scene(modulesMenuGrid, primaryStage.getWidth(), primaryStage.getHeight());
    }

    private void showAccurateFields(VBox moduleBox){
        ObservableList<Node> children = moduleBox.getChildren();
        children.removeAll(comboBox, fromDatePicker, toDatePicker, moduleGenerateButton, moduleChangeRepositoryButton);
        children.addAll(comboBox, fromDatePicker, toDatePicker, moduleGenerateButton, moduleChangeRepositoryButton);  
    }
    
    private void showAccurateFields(String moduleName, VBox moduleBox){
        ObservableList<Node> children = moduleBox.getChildren();
        children.removeAll(comboBox, fromDatePicker, toDatePicker, committerName, moduleGenerateButton, moduleChangeRepositoryButton);
        children.addAll(fromDatePicker, toDatePicker, committerName, moduleGenerateButton, moduleChangeRepositoryButton);
       // children.add(committerName);
        
    }
}