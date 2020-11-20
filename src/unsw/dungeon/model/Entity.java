package unsw.dungeon.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import unsw.dungeon.model.position.PosVec;
import unsw.dungeon.prober.DungeonProbe;

import java.util.ArrayList;
import java.util.List;

/**
 * An entity in the dungeon.
 *
 */
public abstract class Entity {

    // PosVec is a wrapper around IntegerProperty and allows flexible observation and extension.
    protected PosVec position;
    protected DungeonProbe dungeonProbe;

    protected BooleanProperty isInWorld;
    // NOTE: most entities are non-traversable
    protected boolean isPassable;

    /**
     * Create an entity positioned in square (x,y)
     */
    public Entity(int x, int y) {
        isInWorld = new SimpleBooleanProperty();
        isInWorld.set(true);
        this.position = new PosVec(x, y);
        this.isPassable = false;
    }

    public Entity(){}

    public boolean isPassable() {
        return isPassable;
    }

    public void setPassable(boolean passable) {
        isPassable = passable;
    }

    public BooleanProperty isInWorld() {
        return isInWorld;
    }

    public void setIsInWorld(boolean inWorld){
        isInWorld.set(inWorld);
    }

    public int getX(){
        return position.getX();
    }

    public int getY(){
        return position.getY();
    }

    /**
     * NOTE: must set a dungeon probe onLoad() for each entity.
     * @param dungeonProbe - The prober for the current dungeon object
     */
    public void setDungeonProbe(DungeonProbe dungeonProbe) {
        this.dungeonProbe = dungeonProbe;
    }

    public PosVec getPosition() {
        return position;
    }

    public void setPos(int x, int y) {
        if (x >= 0 && x < dungeonProbe.getWidth() && y >= 0 && y < dungeonProbe.getHeight())
        position.setPos(x,y);
    }

    public DungeonProbe getDungeonProbe() {
        return dungeonProbe;
    }

    /**
     * Useful for enemy calculations and flexible for extension.
     * Diagonal distance is an addon for 'Manhattan' Distance, so it
     * also calculates diagonals.
     *
     * @return Distance in 'diagonal' units from one entity to another
     */
    public double diagonalDistanceEntity(Entity e) {
        return position.diagonalDistance(e.position);
    }

    /**
     * Get the neighboring entity references packaged in a nice list.
     * NOTE 1: also need to consider 'empty' tiles that return null.
     * NOTE 2: consider double, triple stacked in trajector/ return false on impassable.
     *
     * Useful for player non-direct facing interactions/enemy attacks.
     *
     * @return List containing entities (3-8) exactly 1 tile away.
     */
    public List<Entity> getNeighboringEntities() {
        List<Entity> entities = new ArrayList<>();
        Entity entity;

        int x = position.getX();
        int y = position.getY();

        if((entity=dungeonProbe.getEntityAtCoords(x+1,y))!=null)  entities.add(entity);
        if((entity=dungeonProbe.getEntityAtCoords(x-1,y))!=null)  entities.add(entity);
        if((entity=dungeonProbe.getEntityAtCoords(x+1,y+1))!=null)  entities.add(entity);
        if((entity=dungeonProbe.getEntityAtCoords(x+1,y-1))!=null)  entities.add(entity);

        if((entity=dungeonProbe.getEntityAtCoords(x,y+1))!=null)  entities.add(entity);
        if((entity=dungeonProbe.getEntityAtCoords(x,y-1))!=null)  entities.add(entity);
        if((entity=dungeonProbe.getEntityAtCoords(x-1,y-1))!=null)  entities.add(entity);
        if((entity=dungeonProbe.getEntityAtCoords(x-1,y+1))!=null)  entities.add(entity);

        return entities;
    }

    /**
     * An entity may be interacted upon by the Player, Enemy or other Entities.
     * @param entity Object to be passed into entity; e.g. Player
     * @throws Exception - Upon an illegal or unexpected move.
     *
     * NOTE: this method may be overloaded in each entity.
     * Default behavior -> throw no-interaction exception.
     */
    public abstract void interact(Entity entity) throws Exception;

    public IntegerProperty x() {
        return position.x();
    }

    public IntegerProperty y() {
        return position.y();
    }

}
