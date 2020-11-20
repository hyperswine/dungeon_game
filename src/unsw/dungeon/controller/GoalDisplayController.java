package unsw.dungeon.controller;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;

public class GoalDisplayController {

    @FXML
    private ImageView imgTitle;

    @FXML
    private Label lblGoals;

    @FXML
    private Pane pane;

    private GoalController goalController;

    public GoalDisplayController(GoalController goalController) {
        this.goalController = goalController;
    }

    @FXML
    public void initialize() {
        imgTitle.setImage(new Image((new File("images/UI/txtGoals.png")).toURI().toString()));
        lblGoals.setText(goalController.goalStatus());
        if (!Platform.isSupported(ConditionalFeature.TRANSPARENT_WINDOW)) {
            pane.setStyle("-fx-background-color: #8b560c;");
        }
    }

}
