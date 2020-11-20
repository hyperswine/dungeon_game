package unsw.dungeon.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;

public class TutorialController {

    private final Stage stage;
    @FXML
    private ImageView imgDisplay;

    @FXML
    private Button btnNext;

    @FXML
    private Button btnPrev;

    @FXML
    private Button btnClose;

    private int currPage = 1;

    public TutorialController(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private void initialize() {
        updateImage();
        btnPrev.setDisable(true);
    }

    @FXML
    private void next(ActionEvent event) {
        if (currPage == 12) stage.close();

        btnPrev.setDisable(false);
        currPage++;
        if (currPage == 12) btnNext.setText("Done");
        updateImage();
    }

    @FXML
    private void prev(ActionEvent event) {
        currPage--;
        btnNext.setText("Next");
        if (currPage == 1) btnPrev.setDisable(true);
        updateImage();
    }

    @FXML
    private void handleClose() {
        stage.close();
    }
    private void updateImage() {
        imgDisplay.setImage(new Image(new File("images/UI/tutorials/tut" + currPage +".png").toURI().toString()));
    }

}
