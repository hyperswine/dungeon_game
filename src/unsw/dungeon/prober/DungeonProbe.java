package unsw.dungeon.prober;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import unsw.dungeon.model.Dungeon;
import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Enemy;
import unsw.dungeon.model.position.DirectionFacing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Probe on a dungeon today, with the dungeon probe(tm).
 * 
 * Features include: 
 * - asking the dungeon for whatever information it can provide you
 * - simply do nothing; the advanced AI probe(R) will come find you onLoad() for all entities.
 * 
 * note: extra features coming soon
 */
public class DungeonProbe {
    private Dungeon dungeon;

    public DungeonProbe(Dungeon dungeon){
        this.dungeon = dungeon;
    }

//    /**
//     * Did the dungeon collapse? No problem, just set another one.
//     * @param dungeon The current dungeon object
//     */
//    public void setDungeon(Dungeon dungeon) {
//        this.dungeon = dungeon;
//    }

    /**
     * Get an iterator of enemies
     */
    public Iterator<Enemy> getEnemies(){
        List<Enemy> enemies = new ArrayList<>();
        List<Entity> entities = dungeon.getEntities();

        for(Entity e: entities){
            if(e instanceof Enemy){
                enemies.add((Enemy)e);
            }
        }

        return enemies.iterator();
    }

    /**
     * Get the entity you're facing! (For the player only MS2)
     * NOTE: doesn't return all the entities, e.g. a boulder on a switch,
     * only returns the switch.
     *
     * Get the boulder as well, with (premium) getListEntityAtCoords.
     */
    public Entity entityFacing(DirectionFacing direction, int x, int y){
        return dungeon.entityFacing(direction, x, y);

    }

    /**
     * Have stacked entities? No problem.
     */
    public List<Entity> getListEntityAtCoords(int x, int y){
        return dungeon.getListEntityAtCoords(x, y);
    }

    /**
     * When you're absolutely sure whats in x,y is not stacked.
     */
    public Entity getEntityAtCoords(int x, int y){
        return dungeon.getEntityAtCoords(x, y);
    }

    public int getWidth(){
        return dungeon.getWidth();
    }

    public int getHeight(){
        return dungeon.getHeight();
    }

    /// TREASURE STUFF //
    public IntegerProperty currentTreasureCollected() {
        return dungeon.currentTreasureCollected();
    }

    public int totalTreasureCount() {
        return dungeon.totalTreasureCount();
    }

    public IntegerProperty enemiesLeft() {
        return dungeon.enemiesLeft();
    }

    public BooleanProperty floorSwitchesTriggered() { return dungeon.allFloorSwitchesTriggered(); }

    public BooleanProperty exitInteracted() {
        return dungeon.exitInteracted();
    }

    public void setExitInteractedFalse() {
        dungeon.setExitInteractedFalse();
    }

    /**
     * Spawned an Entity at (x,y). This replaces the entity at (x,y). Note cannot spawn another player.
     */
    public void spawnEntity(Entity entity){
        dungeon.spawnEntity(entity);
    }

    public void updateAllEnemies(){
        dungeon.updateAllEnemies();
    }

    public void nukeSurroundings(int x, int y, int radius){
        dungeon.destroyEnemiesAround(x, y, radius);
    }
}
