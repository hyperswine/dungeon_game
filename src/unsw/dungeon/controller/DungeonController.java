package unsw.dungeon.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import unsw.dungeon.model.Dungeon;
import unsw.dungeon.view.AudioPlayer;
import unsw.dungeon.view.DungeonApplication;
import unsw.dungeon.view.DungeonScreen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores a <GridPane> and a <List> of images representing all the loaded
 * entities.
 * 
 * Also contains:
 *  - reference to the dungeon
 *  - reference to the goal controller
 *  - reference to the player controller
 *  - method to initialize the images based on fxml specifications
 */
public class DungeonController {

    @FXML
    private GridPane squares;
    private List<ImageView> initialEntities;

    private Dungeon dungeon;
    private Popup pauseMenu;
    GoalController goalController;
    PlayerController playerController;

    private Window window;
    private DungeonScreen dungeonScreen;

    private boolean gameEnded;
    private Popup goalsPopup;
    /**
     * Create a dungeon controller that contains the dungeon, and a list of initial
     * entities loaded from FXML. Contains the player and a Goal Controller.
     * 
     */
    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities, DungeonControllerLoader dCL,
            GoalController goalController, PlayerController pController, DungeonScreen dungeonScreen) {
        this.dungeon = dungeon;
        this.initialEntities = new ArrayList<>(initialEntities);
        this.goalController = goalController;
        this.playerController = pController;
        this.window = null;

        pauseMenu = new Popup();
        PauseController controller = new PauseController(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/pauseMenu.fxml"));
        loader.setController(controller);
        try {
            pauseMenu.getContent().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.dungeonScreen = dungeonScreen;
        goalController.goalsComplete().addListener(((observable, oldValue, newValue) -> endGame(false)));
        pController.setDungeonController(this);
    }

    /**
     * Initialize the view with images
     */
    @FXML
    private void initialize() {
        Image ground = new Image((new File("images/worldobject/dirt_0_new.png")).toURI().toString());

        // Add the ground first so it is below all other entities
        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                squares.add(new ImageView(ground), x, y);
            }
        }

        for (ImageView entity : initialEntities) {
            squares.getChildren().add(entity);
        }
    }

    private PlayerController getPlayerController() {
        return playerController;
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (window == null) {
            window = squares.getScene().getWindow();
            goalController.setWindow(window);
        }

        PlayerException playerException = new PlayerException(window);
        if (squares.getEffect() != null && !gameEnded) {
            hidePausePopup();
            hideGoalsPopup();
        }
        else if (event.getCode() == KeyCode.ESCAPE) {
            showPausePopup();
        }
        else if (event.getCode() == KeyCode.G) {
            showGoalsPopup();
        } else if (!gameEnded ){
            playerController.handleKeyPress(event, window);
        }
    }

    private void blurGame() {
        BoxBlur blur = new BoxBlur();
        blur.setWidth(15);
        blur.setHeight(15);
        blur.setIterations(3);

        squares.setEffect(blur);
    }

    private void unblurGame() {
        squares.setEffect(null);
    }

    private void showPausePopup() {
        blurGame();
        pauseMenu.show(window);
    }

    public void hidePausePopup() {
        unblurGame();
        pauseMenu.hide();
    }

    public void showGoalsPopup() {
        blurGame();
        newGoalsPopup();
        goalsPopup.show(window);
        //pauseMenu.show(window);;
    }

    public void hideGoalsPopup() {
        unblurGame();
        goalsPopup.hide();
    }

    private void newGoalsPopup() {
        goalsPopup = new Popup();
        GoalDisplayController controller = new GoalDisplayController(goalController);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/goalsPopup.fxml"));
        loader.setController(controller);
        try {
            goalsPopup.getContent().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restart() {
        dungeonScreen.closeAllDungeonWindows();
        Platform.runLater( () -> {
            try {
                new DungeonApplication().start( new Stage() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public void endGame(boolean isFailure) {
        blurGame();
        Popup victoryScreen = new Popup();
        VictoryController controller = new VictoryController(this, isFailure);
        if (isFailure) {
            AudioPlayer.playSound("FAILURE");
        } else {
            AudioPlayer.playSound("VICTORY");
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/victoryScreen.fxml"));
        loader.setController(controller);
        try {
            victoryScreen.getContent().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        victoryScreen.show(window);
        gameEnded = true;
    }

}
