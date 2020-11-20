package unsw.dungeon.view;

import javafx.scene.media.AudioClip;

import java.io.File;
import java.util.Properties;

public class AudioPlayer {
    public AudioPlayer(){
        
    }

    public static void playSound(String event) {
        Properties properties = PropertiesHandler.readProperties("userprefs/config.properties");
        try {
            AudioClip file = new AudioClip((new File("sounds/" + event + ".wav")).toURI().toString());
            if (event.startsWith("UI_")) {
                file.setVolume(Double.parseDouble(properties.getProperty("uiVol")) / 100);
            } else {
                file.setVolume(Double.parseDouble(properties.getProperty("gameVol")) / 100);
            }
            file.play();
        }
        catch (Throwable e) {
            //EMPTY CATCH BLOCK FOR THE PURPOSES OF CSE RUNNING TESTS
            //SINCE CSE WILL NOT HAVE IMPORTED JAVAFX.MEDIA
        }
    }
}
