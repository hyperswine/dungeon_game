package unsw.dungeon.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.dungeon.model.combatant.Enemy;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.position.DirectionFacing;
import unsw.dungeon.model.worldobject.Exit;
import unsw.dungeon.model.worldobject.FloorSwitch;
import unsw.dungeon.model.worldobject.Portal;
import unsw.dungeon.model.worldobject.item.Treasure;
import unsw.dungeon.prober.DungeonProbe;

import java.util.ArrayList;
import java.util.List;

/**
 * The Dungeon object model.
 *
 * A dungeon can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 *
 */
public class Dungeon {

    private int width, height, totalTreasureCount;
    private IntegerProperty enemiesLeft;
    private List<Entity> entities;
    private List<FloorSwitch> floorSwitches;
    private BooleanProperty allFloorSwitchesTriggered, exitInteracted;
    private Player player;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.entities = new ArrayList<>();
        this.player = null;

        //GOAL STUFF//
        totalTreasureCount = 0;
        enemiesLeft = new SimpleIntegerProperty();
        floorSwitches = new ArrayList<>();
        allFloorSwitchesTriggered = new SimpleBooleanProperty();
        allFloorSwitchesTriggered.set(false);
        exitInteracted = new SimpleBooleanProperty();
    }

    public int getWidth() {
        return width;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public int getHeight() {
        return height;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void addEntity(Entity entity, DungeonProbe dungeonProbe) {
        entities.add(entity);
        entity.isInWorld().addListener((observable, oldValue, newValue) -> removeEntityFromWorld(entity));
        // set the prober
        entity.setDungeonProbe(dungeonProbe);
        if (entity.getClass().equals(Treasure.class)) totalTreasureCount++;
        if (entity.getClass().equals(Enemy.class)) enemiesLeft.set(enemiesLeft.get() + 1);
        if (entity.getClass().equals(FloorSwitch.class)) {
            ((FloorSwitch) entity).isActivated().addListener((observable, oldValue, newValue) -> refreshFloorSwitches());
            floorSwitches.add((FloorSwitch) entity);
        }
        if (entity.getClass().equals(Exit.class)) {
            Bindings.bindBidirectional(((Exit) entity).interacted(), exitInteracted);
            ((Exit) entity).interacted().bindBidirectional(exitInteracted);
        }
    }

    private void removeEntityFromWorld(Entity entity) {
        if (entity.getClass().equals(Enemy.class)) enemiesLeft.set(enemiesLeft.get() - 1);
        entities.remove(entity);
    }

    public Entity getEntityAtCoords(int x, int y) {
        for (Entity entity : entities) {
            if (entity != null && entity.getX() == x && entity.getY() == y) {
                return entity;
            }
        }
        return null;
    }

    public List<Entity> getListEntityAtCoords(int x, int y) {
        List<Entity> list = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity != null && entity.getX() == x && entity.getY() == y) {
                list.add(entity);
                System.out.println(list.size());
            }
        }
        System.out.println(list.size());
        return list;
    }

    /**
     * Used by entities, e.g. combatants that have a 'directionFacing' attr.
     * Retreive the entity 1-tile directly in front of them.
     */
    public Entity entityFacing(DirectionFacing direction, int x, int y) {
        switch (direction) {
            case LEFT:
                return (x - 1 < 0) ? null : getEntityAtCoords(x - 1, y);
            case RIGHT:
                return (x + 1 > getWidth()) ? null : getEntityAtCoords(x + 1, y);
            case UP:
                return (y - 1 < 0) ? null : getEntityAtCoords(x, y - 1);
            case DOWN:
                return ((y + 1 > getHeight())) ? null : getEntityAtCoords(x, y + 1);
        }
        return null;
    }

    /**
     * Called when the dungeon is complete.
     * Updates the links between linked entities, e.g. portals
     */
    public void finaliseEntities() {
        for (Entity e: entities) {
            if (e.getClass().equals(Portal.class)) {
                ((Portal) e).updateLink();
            }  else if (e.getClass().equals(FloorSwitch.class)) {
                ((FloorSwitch) e).updateTriggeredState();
            }
        }
    }


    public IntegerProperty currentTreasureCollected() {
        return player.treasureCount();
    }

    public int totalTreasureCount() {
        return totalTreasureCount;
    }

    public IntegerProperty enemiesLeft() {
        return enemiesLeft;
    }

    public void refreshFloorSwitches() {
        for (FloorSwitch floorSwitch : floorSwitches) {
            if (!floorSwitch.isActivated().get()) return;
        }
        allFloorSwitchesTriggered.set(true);
    }

    public BooleanProperty allFloorSwitchesTriggered() { return allFloorSwitchesTriggered; }

    public BooleanProperty exitInteracted() {
        return exitInteracted;
    }

    public void setExitInteractedFalse() {
        exitInteracted.set(false);
    }

    /**
     * Spawn an entity at (x,y)
     */
    public void spawnEntity(Entity entity){
        addEntity(entity, new DungeonProbe(this));
    }

    /**
     * Call update() on all enemies. Trigger them with the utmost passion. 
     * Broadcast your location. Dont tell, show them your eagerness to draw as 
     * much blood as possible.
     */
    public void updateAllEnemies(){
        for(Entity e: entities){
            if(e instanceof Enemy){
                Enemy enemy = (Enemy) e;
                try {
                    enemy.update(player);
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
     * Destroy all enemies with radius = radius units around.
     * Except the player of course.
     */
    public void destroyEnemiesAround(int x, int y, int radius){
        for(Entity e: entities){
            if(!(e instanceof Enemy)){
                continue;
            }
            // Tear the entity apart
            int a = e.getX();
            int b = e.getY();
            
            if(Math.abs(x-a) <= radius && Math.abs(y-b) <= radius)
                e.setIsInWorld(false);
        }
    }
}
