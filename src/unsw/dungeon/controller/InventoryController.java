package unsw.dungeon.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import unsw.dungeon.model.Dungeon;
import unsw.dungeon.model.combatant.Inventory;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.observer.Observer;
import unsw.dungeon.view.PropertiesHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class InventoryController implements Observer {

    private final Dungeon dungeon;
    @FXML
    private ImageView imgBackground;

    @FXML
    private ImageView imgCombat;

    @FXML
    private ImageView imgSlot1;

    @FXML
    private ImageView imgSlot2;

    @FXML
    private ImageView imgSlot3;

    @FXML
    private ImageView imgSlot4;

    @FXML
    private ImageView imgSlot5;

    @FXML
    private Label lblTreasureCount;

    @FXML
    private ProgressBar progressDurability;

    @FXML
    private ProgressBar progressStatus;

    private List<ImageView> imgSlots;

    public InventoryController(Dungeon dungeon) {
        this.dungeon = dungeon;
    }


    @FXML
    protected void initialize() {
        Properties properties = PropertiesHandler.readProperties("userprefs/config.properties");
        if (properties.getProperty("inventoryUIControls").equals("1")) {
            imgBackground.setImage(new Image((new File("images/UI/inventoryBack.png")).toURI().toString()));
        } else {
            imgBackground.setImage(new Image((new File("images/UI/inventoryBackNoControls.png")).toURI().toString()));
        }
        lblTreasureCount.setText("0");
        lblTreasureCount.textProperty().bind(dungeon.getPlayer().treasureCount().asString());
        Player player = dungeon.getPlayer();
        player.attachInventoryListener(this);
        imgSlots = new ArrayList<>();
        imgSlots.add(imgSlot1);
        imgSlots.add(imgSlot2);
        imgSlots.add(imgSlot3);
        imgSlots.add(imgSlot4);
        imgSlots.add(imgSlot5);
        progressDurability.setProgress(0);
        progressDurability.setVisible(false);

    }

    public void updateDurability(Player player) {
        if (player.hasCombatItem()) {
            progressDurability.setVisible(true);
            double currVal = dungeon.getPlayer().weaponDurability().get() / 5.0;
            System.out.println(currVal);
            progressDurability.setProgress(currVal);
        }
    }
    @Override
    public void update(Object obj) {
        for (ImageView imageView : imgSlots) {
            imageView.setImage(null);
        }
        if (!obj.getClass().equals(Inventory.class)) throw new IllegalArgumentException();
        Inventory inventory = (Inventory) obj;
        Iterator<String> itemIterator = inventory.getSpritesIterator();
        if (inventory.hasCombatItem()) {
            imgCombat.setImage(new Image(new File("images/item/" + itemIterator.next()).toURI().toString()));
            updateDurability(dungeon.getPlayer());
        } else {
            imgCombat.setImage(null);
            progressDurability.setProgress(0);
            progressDurability.setVisible(false);
        }

        Iterator<ImageView> slotIterator = imgSlots.iterator();
        while (itemIterator.hasNext()) {
            slotIterator.next().setImage(new Image(new File("images/item/" + itemIterator.next()).toURI().toString()));
        }
    }
}
