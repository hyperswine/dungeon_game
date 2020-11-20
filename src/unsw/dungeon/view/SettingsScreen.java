package unsw.dungeon.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unsw.dungeon.controller.SettingsController;

import java.io.IOException;

public class SettingsScreen {
    private Stage stage;
    private SettingsController settingsController;

    private Scene scene;

    public SettingsScreen(Stage stage) throws IOException {
        this.stage = stage;
        stage.setAlwaysOnTop(true);
        settingsController = new SettingsController(stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
        loader.setController(settingsController);
        Parent root = loader.load();
        scene = new Scene(root, 352, 501);
    }

    public void start() {
        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
    }
}
