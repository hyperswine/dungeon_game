package unsw.dungeon.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import unsw.dungeon.view.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class MenuController {

    @FXML
    private ImageView btnPlay;
    @FXML
    private ImageView btnSettings;
    @FXML
    private ImageView btnTutorials;
    @FXML
    private ImageView btnExit;
    @FXML
    private ImageView btnSelectLevel;
    @FXML
    private ImageView imgBackground;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblLevel;

    //IMAGES
    private final Image IMG_EXIT_HOVER = new Image(new File("images/UI/btnExitHover.png").toURI().toString());
    private final Image IMG_EXIT_IDLE = new Image(new File("images/UI/btnExitIdle.png").toURI().toString());
    private final Image IMG_PLAY_HOVER = new Image(new File("images/UI/btnPlayHover.png").toURI().toString());
    private final Image IMG_PLAY_IDLE = new Image(new File("images/UI/btnPlayIdle.png").toURI().toString());
    private final Image IMG_SELECT_LEVEL_HOVER = new Image(new File("images/UI/btnSelectLevelHover.png").toURI().toString());
    private final Image IMG_SELECT_LEVEL_IDLE = new Image(new File("images/UI/btnSelectLevelIdle.png").toURI().toString());
    private final Image IMG_SETTINGS_HOVER =  new Image(new File("images/UI/btnSettingsHover.png").toURI().toString());
    private final Image IMG_SETTINGS_IDLE =  new Image(new File("images/UI/btnSettingsIdle.png").toURI().toString());
    private final Image IMG_TUTORIALS_HOVER =  new Image(new File("images/UI/btnTutorialsHover.png").toURI().toString());
    private final Image IMG_TUTORIALS_IDLE =  new Image(new File("images/UI/btnTutorialsIdle.png").toURI().toString());


    private DungeonScreen dungeonScreen;

    private Stage primaryStage;

    private File selectedLevel;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    protected void initialize() {
        lblStatus.setText("Welcome to Dungeon Game.");

        btnPlay.setImage(IMG_PLAY_IDLE);
        btnSelectLevel.setImage(IMG_SELECT_LEVEL_IDLE);
        btnSettings.setImage(IMG_SETTINGS_IDLE);
        btnTutorials.setImage(IMG_TUTORIALS_IDLE);
        btnExit.setImage(IMG_EXIT_IDLE);
        imgBackground.setImage(new Image((new File("images/UI/menu_background.png")).toURI().toString()));
        Properties properties = PropertiesHandler.readProperties("userprefs/config.properties");

        String selLevel = properties.getProperty("selectedLevel");
        if (selLevel.equals("NULL")) {
            selectedLevel = null;
            lblLevel.setText("No level selected");
        } else {
            selectedLevel = new File(selLevel);
            lblLevel.setText(selectedLevel.getName());
        }
    }
    @FXML
    void handlePlay() throws IOException {
        AudioPlayer.playSound("UI_CLICK");
        if (selectedLevel == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No level selected");
            alert.setHeaderText(null);
            alert.setContentText("No level selected - select a level first with \"Select Level\"");
            alert.showAndWait();
            return;
        }
        if (PropertiesHandler.readProperties("userprefs/config.properties").getProperty("firstLaunch").equals("1")) {
            PropertiesHandler.setProperty("userprefs/config.properties", "firstLaunch", "0");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("First launch");
            alert.setHeaderText(null);
            alert.setContentText("This is your first time playing the game. Launch the tutorials first?");
            ButtonType btnTutorial = new ButtonType("View tutorials");
            ButtonType btnContinue = new ButtonType("Ignore & start game", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btnTutorial, btnContinue);
            if (alert.showAndWait().get() == btnContinue) {
                handlePlay();
            } else {
                handleTutorials();
            }
        } else {
            System.out.println(selectedLevel.toString());
            dungeonScreen = new DungeonScreen(primaryStage, selectedLevel.toString());
            primaryStage.setAlwaysOnTop(true);
            dungeonScreen.start();
        }
    }

    @FXML
    void handlePlayHover() {
        btnPlay.setImage(IMG_PLAY_HOVER);
        lblStatus.setText("Play with the currently selected world.");
        AudioPlayer.playSound("UI_MOUSE_OVER");
    }

    @FXML
    void handlePlayExit() {
        btnPlay.setImage(IMG_PLAY_IDLE);
        lblStatus.setText("");
    }

    @FXML
    void handleSelect() {
        AudioPlayer.playSound("UI_CLICK");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Level JSON File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        String currentPath = Paths.get("./dungeons").toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File(currentPath));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            selectedLevel = selectedFile;
            lblLevel.setText(selectedLevel.getName());
            PropertiesHandler.setProperty("userprefs/config.properties", "selectedLevel", selectedLevel.toString());
        }
    }

    @FXML
    void handleSelectHover() {
        btnSelectLevel.setImage(IMG_SELECT_LEVEL_HOVER);
        lblStatus.setText("Select the level to be loaded.");
        AudioPlayer.playSound("UI_MOUSE_OVER");
    }

    @FXML
    void handleSelectExit() {
        btnSelectLevel.setImage(IMG_SELECT_LEVEL_IDLE);
        lblStatus.setText("");
    }

    @FXML
    void handleSettings() {
        AudioPlayer.playSound("UI_CLICK");
        try {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            SettingsScreen settingsScreen = new SettingsScreen(stage);
            settingsScreen.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleSettingsHover() {
        btnSettings.setImage(IMG_SETTINGS_HOVER);
        lblStatus.setText("Change settings to customise your experience.");
        AudioPlayer.playSound("UI_MOUSE_OVER");
    }

    @FXML
    void handleSettingsExit() {
        btnSettings.setImage(IMG_SETTINGS_IDLE);
        lblStatus.setText("");
    }

    @FXML
    void handleTutorials() {
        AudioPlayer.playSound("UI_CLICK");
        try {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            TutorialsScreen tutorialsScreen = new TutorialsScreen(stage);
            tutorialsScreen.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleTutHover() {
        btnTutorials.setImage(IMG_TUTORIALS_HOVER);
        AudioPlayer.playSound("UI_MOUSE_OVER");
        lblStatus.setText("View tutorials to learn how to play.");
    }

    @FXML
    void handleTutExit() {
        btnTutorials.setImage(IMG_TUTORIALS_IDLE);
        lblStatus.setText("");
    }

    @FXML
    void handleExit() {
        AudioPlayer.playSound("UI_CLICK");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Confirm exit");
        alert.setHeaderText("Are you sure you want to exit?");
        ButtonType btnExit = new ButtonType("Exit");
        ButtonType btnStay = new ButtonType("Stay", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnExit, btnStay);
        if (alert.showAndWait().get() == btnExit) {
            Platform.exit();
        }

    }

    @FXML
    void handleExitHover() {
        btnExit.setImage(IMG_EXIT_HOVER);
        AudioPlayer.playSound("UI_MOUSE_OVER");
        lblStatus.setText("Exit the game.");
    }

    @FXML
    void handleExitExit() {
        btnExit.setImage(IMG_EXIT_IDLE);
        lblStatus.setText("");
    }

}
