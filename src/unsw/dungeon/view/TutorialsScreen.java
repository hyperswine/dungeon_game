package unsw.dungeon.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unsw.dungeon.controller.TutorialController;

import java.io.IOException;

public class TutorialsScreen {
    private Stage stage;
    private TutorialController tutorialController;

    private Scene scene;

    public TutorialsScreen(Stage stage) throws IOException {
        this.stage = stage;
        stage.setAlwaysOnTop(true);
        tutorialController = new TutorialController(stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tutorialScreen.fxml"));
        loader.setController(tutorialController);
        Parent root = loader.load();
        scene = new Scene(root, 960, 600);
    }

    public void start() {
        stage.setTitle("Tutorials");
        stage.setScene(scene);
        stage.show();
    }
}
