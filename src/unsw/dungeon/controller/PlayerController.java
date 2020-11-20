package unsw.dungeon.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;
import unsw.dungeon.model.combatant.Enemy;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.position.DirectionFacing;
import unsw.dungeon.view.AudioPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * General player sprite models and view-model interactions,
 * NOTE: should probably be extended to work for enemies i.e. make a 'combatant' controller.
 * SUGGESTION: quite some of this can be done in an FXML file instead.
 */
public class PlayerController {
    private ObjectProperty<Image> model;
    private Player player;
    private PlayerException playerException;
    private DungeonController dungeonController;
    private Map<DirectionFacing, Image> playerSpritesWeapon;
    private Map<DirectionFacing, Image> playerSpritesNoWeapon;
    private Map<DirectionFacing, Image> playerSpritesInvincible;

    public void setDungeonController(DungeonController dungeonController) {
        this.dungeonController = dungeonController;
    }

    public PlayerController(Player player){

        this.playerSpritesWeapon = new HashMap<>();
        this.playerSpritesNoWeapon = new HashMap<>();
        this.playerSpritesInvincible = new HashMap<>();
        initializeHashSprites();
        this.player = player;
        model = new SimpleObjectProperty<>();
        // Player always faces right on start without weapon; Controller asks Player for their direction.
        this.model.set(getSpriteNoWeapon(DirectionFacing.RIGHT));
        player.updateDirection(DirectionFacing.RIGHT);

        player.directionFacing().addListener((observableValue, directionFacing, t1) -> updateDirectionSprite(player.directionFacing().get()));
        player.HP().addListener(((observable, oldValue, newValue) -> checkDead(player.HP().get())));
    }

    public void checkDead(int HP) {
        if (HP <= 0) {
            dungeonController.endGame(true);
        }
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    /**
     * Update the enemies when something happens to player
     */
    public void updateEnemies() {
        Iterator<Enemy> enemies = player.getDungeonProbe().getEnemies();
        while(enemies.hasNext()){
            Enemy e = enemies.next();
            try {
                e.update(player);
            }
            catch (Exception ex){
                playerException.handleException(ex);
            }
        }
    }


    private void initializeHashSprites(){
        // initialize weapon held sprites
        playerSpritesWeapon.put(DirectionFacing.UP, new Image((new File("images/player/human_up_sword.png")).toURI().toString()));
        playerSpritesWeapon.put(DirectionFacing.DOWN, new Image((new File("images/player/human_down_sword.png")).toURI().toString()));
        playerSpritesWeapon.put(DirectionFacing.LEFT, new Image((new File("images/player/human_left_sword.png")).toURI().toString()));
        playerSpritesWeapon.put(DirectionFacing.RIGHT, new Image((new File("images/player/human_right_sword.png")).toURI().toString()));

        // weapon non-held sprites
        playerSpritesNoWeapon.put(DirectionFacing.UP, new Image((new File("images/player/human_up.png")).toURI().toString()));
        playerSpritesNoWeapon.put(DirectionFacing.DOWN, new Image((new File("images/player/human_down.png")).toURI().toString()));
        playerSpritesNoWeapon.put(DirectionFacing.LEFT, new Image((new File("images/player/human_left.png")).toURI().toString()));
        playerSpritesNoWeapon.put(DirectionFacing.RIGHT, new Image((new File("images/player/human_right.png")).toURI().toString()));

        // invincible sprites
        playerSpritesInvincible.put(DirectionFacing.UP, new Image((new File("images/player/human_up_invincible.png")).toURI().toString()));
        playerSpritesInvincible.put(DirectionFacing.DOWN, new Image((new File("images/player/human_down_invincible.png")).toURI().toString()));
        playerSpritesInvincible.put(DirectionFacing.LEFT, new Image((new File("images/player/human_left_invincible.png")).toURI().toString()));
        playerSpritesInvincible.put(DirectionFacing.RIGHT, new Image((new File("images/player/human_right_invincible.png")).toURI().toString()));
    }


    private void updateDirectionSprite(DirectionFacing directionFacing) {
        if (player.isInvincible()) {
            model.set(playerSpritesInvincible.get(directionFacing));
        } else if (player.hasCombatItem()) {
            model.set(playerSpritesWeapon.get(directionFacing));
        } else {
            model.set(playerSpritesNoWeapon.get(directionFacing));
        }
    }

    /**
     * Get the sprite for the direction (or angle) the player is facing
     * @param directionFacing Enumerated type UP, DOWN, etc.
     * @return Image of the player's directional sprite
     */
    private Image getSpriteWeapon(DirectionFacing directionFacing) {
        return playerSpritesWeapon.get(directionFacing);
    }

    /**
     * Get the sprite for no weapon
     */
    private Image getSpriteNoWeapon(DirectionFacing directionFacing) {
        return playerSpritesNoWeapon.get(directionFacing);
    }

    private Image getSpriteInvincible(DirectionFacing directionFacing) {
        return playerSpritesInvincible.get(directionFacing);
    }

    public ObjectProperty<Image> model() {
        return model;
    }

    /**
     * Handler for the user's keyboard presses.
     * TODO: consider another handler for the user's mouse clicks on the
     * inventory pane, and the gridpane.
     * @param event A keyboard switch hit by the player. Unmapped switches simply do nothing for now,
     *              although having a 'chat' function maybe be of consideration.
     */
    public void handleKeyPress(KeyEvent event, Window window) {
        playerException = new PlayerException(window);
        try {
            switch (event.getCode()) {
                case UP:
                    player.move(0,-1, DirectionFacing.UP);
                    updateEnemies();
                    break;
                case DOWN:
                    player.move(0,1,DirectionFacing.DOWN);
                    updateEnemies();
                    break;
                case LEFT:
                    player.move(-1,0,DirectionFacing.LEFT);
                    updateEnemies();
                    break;
                case RIGHT:
                    player.move(1,0,DirectionFacing.RIGHT);
                    updateEnemies();
                    break;
                case SPACE:
                    player.attack(null, 10);
                    AudioPlayer.playSound("ENEMY_HIT");
                    break;
                case ENTER:
                    player.interactWithEntityFacing();
                    AudioPlayer.playSound("INTERACT");
                    break;
                case Q:
                    player.dropItem();
                    //AudioPlayer.playSound("DROP_ITEM");
                    break;
                case S:
                    player.sortItems();
                    //AudioPlayer.playSound("SWITCH");
                    break;
                case DIGIT1:
                    player.useItemInSlot(1);
                    break;
                case DIGIT2:
                    player.useItemInSlot(2);
                    break;
                case DIGIT3:
                    player.useItemInSlot(3);
                    break;
                case DIGIT4:
                    player.useItemInSlot(4);
                    break;
                case DIGIT5:
                    player.useItemInSlot(5);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            playerException.handleException(e);
        } finally {
            updateDirectionSprite(player.directionFacing().get());
        }
    }
    
}