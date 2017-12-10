package app.gui;

import app.fetch.Fetcher;
import com.sun.glass.ui.Screen;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.Arrays;

public class Controller {
    private final Scene menuScene;
    private final Scene openRepositoryScene;
    private final Scene optionsScene;
    private final Scene contactScene;
    private final Popup exitPopup;
    private final Scene analysisMenuScene;
    private final Stage primaryStage;
    private final int screenWidth;
    private final int screenHeight;
    //private final Fetcher fetcher;

    public Controller(final Stage primaryStage){
        this.screenWidth = Screen.getMainScreen().getWidth();
        this.screenHeight = Screen.getMainScreen().getHeight();
        this.primaryStage = primaryStage;
        this.menuScene = createMenuScene();
        this.openRepositoryScene = createOpenRepositoryScene();
        this.optionsScene = createOptionsScene();
        this.contactScene = createContactScene();
        this.exitPopup = createExitPopUp();
        this.analysisMenuScene = createAnalysisMenuScene();
        //this.fetcher = new Fetcher();
    }

    public void start(){
        showMenuScene();
    }

    private void showMenuScene(){
        changeScene(primaryStage, this.menuScene);
    }

    private void showOpenRepositoryScene(){
        changeScene(primaryStage, this.openRepositoryScene);
    }

    private void showOptionsScene(){
        changeScene(primaryStage, this.optionsScene);
    }

    private void showContactScene(){
        changeScene(primaryStage, this.contactScene);
    }

    private void showExitPopup(){
        exitPopup.show(primaryStage);
    }

    private void hidePopup(){
        exitPopup.hide();
    }

    private void showAnalysisMenuScene(){
        changeScene(primaryStage, this.analysisMenuScene);
    }

    private void changeScene(Stage primaryStage, Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Scene createMenuScene() {
        GridPane menuGrid = getAbstractGrid(Color.WHITE);
        Text menuTitle = getText("Welcome to Git Analyzer", 70);
        menuGrid.add(menuTitle, 0,0);

        VBox menuVBox = new VBox(50);
        menuGrid.add(menuVBox, 0,1);
        menuVBox.setAlignment(Pos.CENTER);
        menuVBox.setStyle("-fx-font: 40 Tahoma");

        ObservableList<Node> menuVBoxChildren = menuVBox.getChildren();

        Button signInButton = getButton("Open repository", 350, 55, this::showOpenRepositoryScene);
        menuVBoxChildren.add(signInButton);

        Button optionsButton = getButton("Options", 350, 55, this::showOptionsScene);
        menuVBoxChildren.add(optionsButton);

        Button contactButton = getButton("Contact", 350, 55, this::showContactScene);
        menuVBoxChildren.add(contactButton);

        Button exitButton = getButton("Exit", 350, 55, this::showExitPopup);
        menuVBoxChildren.add(exitButton);

        return new Scene(menuGrid, screenWidth, screenHeight);
    }

    private Scene createOpenRepositoryScene() {
        GridPane openRepositoryGrid = getAbstractGrid(Color.WHITE);

        VBox openRepositoryBox = new VBox(70);
        openRepositoryGrid.add(openRepositoryBox, 0,0);
        openRepositoryBox.setAlignment(Pos.TOP_CENTER);
        openRepositoryBox.setStyle("-fx-font: 40 Tahoma");

        Text openRepositoryTitle = getText("Open repository", 70);
        openRepositoryBox.getChildren().add(openRepositoryTitle);

        Text repoPath = getText("Path to repository (git file):", 70);
        openRepositoryBox.getChildren().add(repoPath);

        TextField repoPathTextField = new TextField();
        repoPathTextField.setPrefHeight(40);
        openRepositoryBox.getChildren().add(repoPathTextField);

        Button openRepositoryButton = getButton("Open repository", 350, 55, () -> {
//            if(fetcher.gitRepoExists(repoPathTextField.getText())){
            if(repoPathTextField.getText().length() > 0){
                repoPathTextField.clear();
                showAnalysisMenuScene();
            }
            else{
                repoPathTextField.setStyle("-fx-border-color: red");
            }
        });
        openRepositoryBox.getChildren().add(openRepositoryButton);

        Button openRepositoryBackButton = getButton("Back", 350, 55, () -> { repoPathTextField.clear(); showMenuScene(); });
        openRepositoryBox.getChildren().add(openRepositoryBackButton);

        return new Scene(openRepositoryGrid, screenWidth, screenHeight);
    }

    private Scene createAnalysisMenuScene() {
        GridPane analysisMenuGrid = getAbstractGrid(Color.WHITE);

        VBox analysisMenuBox = new VBox(50);
        analysisMenuGrid.add(analysisMenuBox, 0,0);
        analysisMenuBox.setAlignment(Pos.CENTER);
        analysisMenuBox.setStyle("-fx-font: 40 Tahoma");

        Button analysisChangeRepositoryButton = getButton("Change Repository", 450, 55, this::showOpenRepositoryScene);
        analysisMenuBox.getChildren().add(analysisChangeRepositoryButton);

        return new Scene(analysisMenuGrid, screenWidth, screenHeight);
    }

    private Scene createOptionsScene() {
        GridPane optionsGrid = getAbstractGrid(Color.WHITE);

        VBox optionsBox = new VBox(50);
        optionsGrid.add(optionsBox, 0,0);
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setStyle("-fx-font: 40 Tahoma");

        Text optionsTitle = getText("Options", 70);
        optionsBox.getChildren().add(optionsTitle);

        Button optionsBackButton = getButton("Back", 350, 55, this::showMenuScene);
        optionsBox.getChildren().add(optionsBackButton);

        return new Scene(optionsGrid, screenWidth, screenHeight);
    }

    private Scene createContactScene() {
        GridPane contactGrid = getAbstractGrid(Color.WHITE);

        VBox contactBox = new VBox(50);
        contactGrid.add(contactBox, 0,0);
        contactBox.setAlignment(Pos.CENTER);
        contactBox.setStyle("-fx-font: 40 Tahoma");

        Text contactTitle = getText("Authors", 70);
        contactBox.getChildren().add(contactTitle);

        Arrays.asList("Karol Bartyzel", "Karolina Biela", "Agata Bogacz", "Barbara Chraścik", "Justyna Maciąg", "Urszula Soboń")
                .forEach((x) -> {
                    contactBox.getChildren().add(getText(x, 50));
                });

        Button contactBackButton = getButton("Back", 350, 55, this::showMenuScene);
        contactBox.getChildren().add(contactBackButton);

        return new Scene(contactGrid, screenWidth, screenHeight);
    }

    private Popup createExitPopUp() {
        final Popup popup = new Popup();
        popup.setWidth(600);popup.setHeight(400);

        GridPane exitGrid = getAbstractGrid(Color.AZURE);
        popup.getContent().add(exitGrid);

        exitGrid.setPadding(new Insets(50, 50, 50, 50));

        Text exitText = getText("Are you sure to exit?", 50);
        exitGrid.add(exitText, 0,0);

        HBox exitHBox = new HBox(50);
        exitGrid.add(exitHBox, 0,1);
        exitHBox.setStyle("-fx-font: 40 Tahoma");
        exitHBox.setAlignment(Pos.CENTER);

        Button yesButton = getButton("Yes", 150, 35, () -> {
            Platform.exit();
            System.exit(0);
        });
        Button noButton = getButton("No", 150, 35, this::hidePopup);
        exitHBox.getChildren().addAll(yesButton, noButton);

        return popup;
    }

    private GridPane getAbstractGrid(Color color){
        GridPane grid = new GridPane();
        grid.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(100);
        grid.setMinSize(primaryStage.getWidth(), primaryStage.getHeight());
        grid.setPadding(new Insets(25, 25, 25, 25));

        return grid;
    }

    private Button getButton(String text, int width, int height, final Runnable handler){
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.setOnAction(event -> handler.run());

        return button;
    }

    private Text getText(String text, int size) {
        Text title = new Text(text);
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, size));
        return title;
    }
}
