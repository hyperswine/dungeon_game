package unsw.dungeon.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;

public class ToastGenerator {
    public void showToast(Window window, String message) {
        Popup toast = new Popup();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/toast.fxml"));
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(l -> toast.hide());
        toast.hide();
        ToastController c = new ToastController(message);
        loader.setController(c);
        try {
            toast.getContent().add(loader.load());
        } catch (IOException w) {
            w.printStackTrace();
        }
        toast.show(window);
        toast.centerOnScreen();
        toast.setY(window.getY() + window.getHeight() - toast.getHeight());
        delay.play();
    }

}
