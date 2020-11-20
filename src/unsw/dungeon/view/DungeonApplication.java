package unsw.dungeon.view;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DungeonApplication extends Application {

    /**
     * Note this method may throw an io exception for some of its function calls
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        MenuScreen menuScreen = new MenuScreen(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(false);
        menuScreen.getMenuController().setPrimaryStage(primaryStage);
        menuScreen.start();
        if (!Platform.isSupported(ConditionalFeature.TRANSPARENT_WINDOW)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Compatibility warning");
            alert.setHeaderText(null);
            alert.setContentText("Your system does not support transparency effects. Some menus may look degraded.");

            alert.showAndWait();
        }
    }

    // Main Function, run this
    public static void main(String[] args) {
        launch(args);
    }
}
