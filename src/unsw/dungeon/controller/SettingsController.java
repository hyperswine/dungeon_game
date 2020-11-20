package unsw.dungeon.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import unsw.dungeon.view.AudioPlayer;
import unsw.dungeon.view.PropertiesHandler;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class SettingsController {

    @FXML
    private Slider sliderGameVol;

    @FXML
    private Slider sliderUIVol;

    @FXML
    private Button btnSoundLicenses;

    @FXML
    private Button btnClose;

    @FXML
    private CheckBox checkControlsOnUI;

    @FXML
    private ImageView imgTitle;

    @FXML
    private Button btnResetData;

    private Stage settingsStage;
    private Properties properties;

    public SettingsController(Stage stage) {
        this.settingsStage = stage;
    }

    @FXML
    void initialize() {
        properties = PropertiesHandler.readProperties("userprefs/config.properties");
        sliderGameVol.setValue(Double.parseDouble(properties.getProperty("gameVol")));
        sliderUIVol.setValue(Double.parseDouble(properties.getProperty("uiVol")));
        checkControlsOnUI.setSelected(Integer.parseInt(properties.getProperty("inventoryUIControls")) == 1);
        imgTitle.setImage(new Image(new File("images/UI/txtSettings.png").toURI().toString()));
    }



    @FXML
    private void handleClose(ActionEvent event) {
        settingsStage.close();
    }

    @FXML
    private void handleGameVolUpdate(MouseEvent event) {
        PropertiesHandler.setProperty("userprefs/config.properties", "gameVol", Double.toString(sliderGameVol.getValue()));
        AudioPlayer.playSound("WALL_BUMP");
    }

    @FXML
    private void handleSoundLicenses(ActionEvent event) {
        try {
            java.awt.Desktop.getDesktop().open(new File("readme.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUIVolUpdate(MouseEvent event) {
        PropertiesHandler.setProperty("userprefs/config.properties", "uiVol", Double.toString(sliderUIVol.getValue()));
        AudioPlayer.playSound("UI_CLICK");
    }

    @FXML
    private void handleControlsOnUI() {
        String newVal;
        if (checkControlsOnUI.isSelected()) newVal = "1";
        else newVal = "0";
        PropertiesHandler.setProperty("userprefs/config.properties", "inventoryUIControls", newVal);
    }

    @FXML
    private void handleResetData() {
        PropertiesHandler.setProperty("userprefs/config.properties", "inventoryUIControls", "1");
        PropertiesHandler.setProperty("userprefs/config.properties", "firstLaunch", "1");
        PropertiesHandler.setProperty("userprefs/config.properties", "controlSet", "0");
        PropertiesHandler.setProperty("userprefs/config.properties", "gameVol", "100");
        PropertiesHandler.setProperty("userprefs/config.properties", "uiVol", "100");
        PropertiesHandler.setProperty("userprefs/config.properties", "selectedLevel", "NULL");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("All Data Reset");
        alert.setHeaderText("Data reset successfully");
        settingsStage.close();
        alert.showAndWait();
        initialize();
    }
}
