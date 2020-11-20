package unsw.dungeon.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class ToastController {
    @FXML
    private Label txtMessage;

    @FXML
    private ImageView imgBg;

    private String message;

    public ToastController(String message) {
        this.message = message;
    }

    @FXML
    public void initialize() {
        txtMessage.setText(message);
        imgBg.setImage(new Image(new File("images/UI/toast_bg.png").toURI().toString()));
    }

}
