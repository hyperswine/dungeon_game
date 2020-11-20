package unsw.dungeon.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesHandler {
    public static Properties readProperties(String fileName) {
        Properties properties = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            properties = new Properties();
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void setProperty(String fileName, String key, String value) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            Properties properties = new Properties();
            properties.load(fileInputStream);
            fileInputStream.close();

            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            properties.setProperty(key, value);
            properties.store(fileOutputStream, null);
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
