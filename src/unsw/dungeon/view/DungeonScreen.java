package unsw.dungeon.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import unsw.dungeon.controller.DungeonController;
import unsw.dungeon.controller.DungeonControllerLoader;
import unsw.dungeon.controller.InventoryController;

import java.io.IOException;

public class DungeonScreen {
    private Stage stage;
    private Stage inventoryStage;
    private String title;
    private DungeonController controller;
    private DungeonControllerLoader controllerLoader;

    private Scene scene;
    private Scene inventoryScene;
    public DungeonScreen(Stage primaryStage, String fileName) throws IOException {
        // set the stage as a dungeon
        primaryStage.setTitle("Dungeon");

        // create a dungeon 'loader' and its controller
        controllerLoader = new DungeonControllerLoader(fileName, this);
        controller = controllerLoader.loadController();

        // load the fxml of the dungeon's GUI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DungeonView.fxml"));
        FXMLLoader loaderInv = new FXMLLoader(getClass().getResource("inventoryPane.fxml"));

        // set the controller for the GUI
        loader.setController(controller);
        loaderInv.setController(new InventoryController(controller.getDungeon()));

        // load the loader into the 'parent', which e.g. stores images and animations
        Parent root = loader.load();
        Parent rootInv = loaderInv.load();

        // create new scene
        scene = new Scene(root);
        inventoryScene = new Scene(rootInv);

        // request the 'focus'
        root.requestFocus();
        stage = primaryStage;

        inventoryStage = new Stage();
        inventoryStage.setScene(scene);
        inventoryStage.initStyle(StageStyle.UNDECORATED);
        inventoryStage.setAlwaysOnTop(true);
    }

    public void start() {
        // set the scene and display the view
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

        inventoryStage.setScene(inventoryScene);
        inventoryStage.show();
        inventoryStage.centerOnScreen();
        inventoryStage.setY(stage.getY() + stage.getHeight() - 1);
        stage.requestFocus();

    }

    public DungeonController getController() {
        return controller;
    }

    public void closeAllDungeonWindows() {
        stage.close();
        inventoryStage.close();
    }

    public Stage getStage() {
        return stage;
    }
}
