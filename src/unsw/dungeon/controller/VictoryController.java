package unsw.dungeon.controller;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import unsw.dungeon.view.AudioPlayer;

import java.io.File;

public class VictoryController {


    private final boolean isFailure;
    @FXML
    private ImageView imgVictory;

    @FXML
    private ImageView btnMenu;

    @FXML
    private ImageView btnExit;

    @FXML
    private Pane pane;

    private final Image IMG_MENU_IDLE =  new Image(new File("images/UI/btnMainMenuIdle.png").toURI().toString());
    private final Image IMG_MENU_HOVER =  new Image(new File("images/UI/btnMainMenuHover.png").toURI().toString());
    private final Image IMG_EXIT_HOVER = new Image(new File("images/UI/btnExitHover.png").toURI().toString());
    private final Image IMG_EXIT_IDLE = new Image(new File("images/UI/btnExitIdle.png").toURI().toString());

    private DungeonController dungeonController;
    public VictoryController(DungeonController dC, boolean isFailure) {
        dungeonController = dC;
        this.isFailure = isFailure;
    }
    @FXML
    public void initialize() {
        btnMenu.setImage(IMG_MENU_IDLE);
        if (!isFailure) {
            imgVictory.setImage(new Image(new File("images/UI/victoryText.png").toURI().toString()));
        } else {
            imgVictory.setImage(new Image(new File("images/UI/failureText.png").toURI().toString()));
        }
        btnExit.setImage(IMG_EXIT_IDLE);
        if (!Platform.isSupported(ConditionalFeature.TRANSPARENT_WINDOW)) {
            pane.setStyle("-fx-background-color: #8b560c;");
        }
    }

    @FXML
    private void handleExit() {
        AudioPlayer.playSound("UI_CLICK");
        Platform.exit();
    }

    @FXML
    private void handleExitHover() {
        AudioPlayer.playSound("UI_MOUSE_OVER");
        btnExit.setImage(IMG_EXIT_HOVER);
    }

    @FXML
    private void handleExitExit() {
        btnExit.setImage(IMG_EXIT_IDLE);
    }


    @FXML
    private void handleMenu(MouseEvent event) {
        AudioPlayer.playSound("UI_CLICK");
        dungeonController.restart();
    }

    @FXML
    private void handleMenuExit(MouseEvent event) {
        btnMenu.setImage(IMG_MENU_IDLE);
    }

    @FXML
    private void handleMenuHover(MouseEvent event) {
        AudioPlayer.playSound("UI_MOUSE_OVER");
        btnMenu.setImage(IMG_MENU_HOVER);
    }

}
