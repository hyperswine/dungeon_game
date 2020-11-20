package unsw.dungeon.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.json.JSONObject;
import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Enemy;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.position.PosVec;
import unsw.dungeon.model.worldobject.*;
import unsw.dungeon.model.worldobject.item.*;
import unsw.dungeon.view.DungeonLoader;
import unsw.dungeon.view.DungeonScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * A DungeonLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 *
 */
public class DungeonControllerLoader extends DungeonLoader {

    private List<ImageView> entities;

    //Images
    private Image playerImage;
    private Image wallImage;
    private Image exitImage;
    private Image swordImage;
    private Image invincibilityPotionImage;
    private Image enemyImage;
    private Image treasureImage;
    private Image boulderImage;
    private Image floorSwitchImage;
    private Image portalImage;
    private Image keyImage;
    private Image doorImage;
    private Image openDoorImage;
    private Image hyperScrollImage;
    private Image hyperTauntImage;
    private Image nukeImage;
    private Image hyperAuraImage;
    private Image dungeonPassImage;
    private Image hyperTreasureImage;

    private PlayerController playerController;

    private DungeonScreen dungeonScreen;
    /**
     * Bind images (view) to position (state) of entities.
     */
    public DungeonControllerLoader(String filename, DungeonScreen dungeonScreen)
            throws FileNotFoundException {
        super(filename);
        setImages();
        this.dungeonScreen = dungeonScreen;
    }

    public DungeonControllerLoader(JSONObject json) {
        super(json);
        //setImages();
    }

    private void setImages() {
        entities = new ArrayList<>();
        playerImage = new Image((new File("images/player/human_right.png")).toURI().toString());
        wallImage = new Image((new File("images/worldobject/brick_brown_0.png")).toURI().toString());
        exitImage = new Image((new File("images/worldobject/exit.png")).toURI().toString());
        swordImage = new Image((new File("images/item/greatsword_1_new.png")).toURI().toString());
        invincibilityPotionImage = new Image((new File("images/item/brilliant_blue_new.png")).toURI().toString());

        enemyImage = new Image((new File("images/enemy/deep_elf_master_archer_1.png")).toURI().toString());
        treasureImage = new Image((new File(("images/item/gold_pile.png")).toURI().toString()));
        boulderImage = new Image((new File(("images/worldobject/boulder.png")).toURI().toString()));
        floorSwitchImage = new Image((new File(("images/worldobject/pressure_plate.png")).toURI().toString()));
        portalImage = new Image((new File(("images/worldobject/portal.png")).toURI().toString()));

        keyImage = new Image((new File(("images/item/key.png")).toURI().toString()));
        doorImage = new Image((new File(("images/worldobject/closed_door.png")).toURI().toString()));

        openDoorImage = new Image((new File(("images/worldobject/open_door.png")).toURI().toString()));
        hyperScrollImage = new Image(new File("images/item/hyper_scroll.png").toURI().toString());
        hyperTauntImage = new Image(new File("images/item/taunt.png").toURI().toString());
        nukeImage = new Image(new File("images/item/nuke.png").toURI().toString());
        hyperAuraImage = new Image(new File("images/item/hyper_aura.png").toURI().toString());
        dungeonPassImage = new Image(new File("images/item/dungeon_pass.png").toURI().toString());
        hyperTreasureImage = new Image(new File("images/item/hyper_treasure.png").toURI().toString());
    }

    @Override
    public void onLoad(Player player) {
        playerController = new PlayerController(player);
        // playerController.setPlayer(player);
        ImageView view = new ImageView(playerImage);

        //Bindings.bindBidirectional(view.imageProperty(), playerController.model());
        playerController.model().bindBidirectional(view.imageProperty());

        addEntity(player, view);
    }

    @Override
    public void onLoad(Wall wall) {
        ImageView view = new ImageView(wallImage);
        addEntity(wall, view);
    }

    @Override
    public void onLoad(Exit exit) {
        ImageView view = new ImageView(exitImage);
        addEntity(exit, view);
    }

    @Override
    public void onLoad(Sword sword) {
        ImageView view = new ImageView(swordImage);
        addEntity(sword, view);
    }

    @Override
    public void onLoad(InvincibilityPotion invincibilityPotion) {
        ImageView view = new ImageView(invincibilityPotionImage);
        addEntity(invincibilityPotion, view);
    }

    @Override
    public void onLoad(Enemy enemy) {
        ImageView view = new ImageView(enemyImage);
        addEntity(enemy, view);
    }

    @Override
    public void onLoad(Treasure treasure) {
        ImageView view = new ImageView(treasureImage);
        addEntity(treasure, view);
    }

    @Override
    public void onLoad(Boulder boulder) {
        ImageView view = new ImageView(boulderImage);
        addEntity(boulder, view);
    }

    @Override
    public void onLoad(FloorSwitch floorSwitch) {
        ImageView view = new ImageView(floorSwitchImage);
        addEntity(floorSwitch, view);
    }

    @Override
    public void onLoad(Portal portal) {
        ImageView view = new ImageView(portalImage);
        addEntity(portal, view);
    }

    @Override
    public void onLoad(Door door) {
        ImageView view = new ImageView(doorImage);
        door.unlockedOpen().addListener((observable, oldValue, newValue) -> view.setImage(openDoorImage));
        addEntity(door, view);
    }

    @Override
    public void onLoad(Key key) {
        ImageView view = new ImageView(keyImage);
        addEntity(key, view);
    }

    @Override
    public void onLoad(HyperScroll hyperScroll) {
        ImageView view = new ImageView(hyperScrollImage);
        addEntity(hyperScroll, view);
    }

    @Override
    public void onLoad(Taunt taunt) {
        ImageView view = new ImageView(hyperTauntImage);
        addEntity(taunt, view);
    }

    @Override
    public void onLoad(Nuke nuke) {
        ImageView view = new ImageView(nukeImage);
        addEntity(nuke, view);
    }

    @Override
    public void onLoad(HyperAura hyperAura){
        ImageView view = new ImageView(hyperAuraImage);
        addEntity(hyperAura, view);
    }

    @Override
    public void onLoad(DungeonPass dungeonPass) {
        ImageView view = new ImageView(dungeonPassImage);
        addEntity(dungeonPass, view);
    }

    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entities.add(view);
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the dungeon.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * 
     */
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());

        //entity.getPosition().addListener(this);
        ObjectProperty<PosVec> pVecProp = new SimpleObjectProperty<>();
        entity.getPosition();

        entity.x().addListener((observable, oldValue, newValue) -> GridPane.setColumnIndex(node, newValue.intValue()));
        entity.y().addListener((observable, oldValue, newValue) -> GridPane.setRowIndex(node, newValue.intValue()));
        entity.isInWorld().addListener((observable, oldValue, newValue) -> node.setVisible(false));
    }


    /**
     * Create a controller that can be attached to the DungeonView with all the
     * loaded entities.
     * @return The main controller for the game. Contains dungeon, player and goals.
     * @throws FileNotFoundException
     */
    public DungeonController loadController() throws FileNotFoundException {
        return new DungeonController(load(), entities, this, loadGoals(), playerController, dungeonScreen);
    }

}
