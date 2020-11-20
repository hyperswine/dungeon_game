package unsw.dungeon.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unsw.dungeon.controller.MenuController;

import java.awt.*;
import java.io.IOException;

public class MenuScreen {
    private Stage stage;
    private String title;
    private MenuController menuController;

    private Scene scene;

    public MenuScreen(Stage stage) throws IOException {
        this.stage = stage;
        title = "Dungeon Game";
        menuController = new MenuController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        loader.setController(menuController);
        Parent root = loader.load();
        scene = new Scene(root, 601, 431);
    }

    public void start() {
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public MenuController getMenuController() {
        return menuController;
    }
}
