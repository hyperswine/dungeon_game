package unsw.dungeon.controller;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import unsw.dungeon.view.AudioPlayer;
import unsw.dungeon.view.TutorialsScreen;

import java.io.File;
import java.io.IOException;

public class PauseController {
    @FXML
    private ImageView imgPaused;

    @FXML
    private ImageView btnResume;

    @FXML
    private ImageView btnGoals;

    @FXML
    private ImageView btnTutorials;

    @FXML
    private ImageView btnMenu;
    @FXML
    private Pane pane;
    //IMAGES
    private final Image IMG_RESUME_IDLE =  new Image(new File("images/UI/btnResumeIdle.png").toURI().toString());
    private final Image IMG_RESUME_HOVER =  new Image(new File("images/UI/btnResumeHover.png").toURI().toString());
    private final Image IMG_TUTORIALS_HOVER =  new Image(new File("images/UI/btnTutorialsHover.png").toURI().toString());
    private final Image IMG_TUTORIALS_IDLE =  new Image(new File("images/UI/btnTutorialsIdle.png").toURI().toString());
    private final Image IMG_GOALS_IDLE =  new Image(new File("images/UI/btnGoalsIdle.png").toURI().toString());
    private final Image IMG_GOALS_HOVER =  new Image(new File("images/UI/btnGoalsHover.png").toURI().toString());
    private final Image IMG_MENU_IDLE =  new Image(new File("images/UI/btnMainMenuIdle.png").toURI().toString());
    private final Image IMG_MENU_HOVER =  new Image(new File("images/UI/btnMainMenuHover.png").toURI().toString());

    private DungeonController dungeonController;

    public PauseController(DungeonController controller) {
        dungeonController = controller;
    }

    @FXML
    private void initialize() {
        imgPaused.setImage(new Image(new File("images/UI/textGamePaused.png").toURI().toString()));
        btnResume.setImage(IMG_RESUME_IDLE);
        btnGoals.setImage(IMG_GOALS_IDLE);
        btnTutorials.setImage(IMG_TUTORIALS_IDLE);
        btnMenu.setImage(IMG_MENU_IDLE);
        if (!Platform.isSupported(ConditionalFeature.TRANSPARENT_WINDOW)) {
            pane.setStyle("-fx-background-color: #8b560c;");
        }
    }

    @FXML
    private void handleGoals(MouseEvent event) {
        AudioPlayer.playSound("UI_CLICK");
        dungeonController.hidePausePopup();
        dungeonController.showGoalsPopup();
    }

    @FXML
    private void handleGoalsExit(MouseEvent event) {
        btnGoals.setImage(IMG_GOALS_IDLE);
    }

    @FXML
    private void handleGoalsHover(MouseEvent event) {
        AudioPlayer.playSound("UI_MOUSE_OVER");
        btnGoals.setImage(IMG_GOALS_HOVER);
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

    @FXML
    private void handleResume() {
        AudioPlayer.playSound("UI_CLICK");
        dungeonController.hidePausePopup();
    }

    @FXML
    private void handleResumeExit(MouseEvent event) {
        btnResume.setImage(IMG_RESUME_IDLE);
    }

    @FXML
    private void handleResumeHover(MouseEvent event) {
        AudioPlayer.playSound("UI_MOUSE_OVER");
        btnResume.setImage(IMG_RESUME_HOVER);
    }

    @FXML
    private void handleTut(MouseEvent event) {
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
    private void handleTutExit(MouseEvent event) {
        btnTutorials.setImage(IMG_TUTORIALS_IDLE);
    }

    @FXML
    private void handleTutHover(MouseEvent event) {
        AudioPlayer.playSound("UI_MOUSE_OVER");
        btnTutorials.setImage(IMG_TUTORIALS_HOVER);
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        handleResume();
    }
}
