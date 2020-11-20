package unsw.dungeon.model.worldobject;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import unsw.dungeon.exceptions.NoInteractionException;
import unsw.dungeon.exceptions.PushException;
import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Player;

import java.util.List;

public class FloorSwitch extends Entity {
    private Boulder boulderOnTop;
    private BooleanProperty isActivated;

    public FloorSwitch(int x, int y){
        super(x, y);
        setPassable(true);
        isActivated = new SimpleBooleanProperty();
        isActivated.set(false);
    }

    public void updateTriggeredState() {
        List<Entity> entOnTop = dungeonProbe.getListEntityAtCoords(getX(), getY());
        if (entOnTop.size() == 1) {
            setPassable(true);
            boulderOnTop = null;
        } else {
            setPassable(false);
            for (Entity ent : entOnTop) {
                if (ent.getClass().equals(Boulder.class)) {
                    boulderOnTop = (Boulder) ent;
                    break;
                }
            }
        }
        updateActivationStatus();
    }

    public void setBoulderOnTop(Boulder boulder) {
        boulderOnTop = boulder;
        updateActivationStatus();
    }

    @Override
    public void interact(Entity entity) throws NoInteractionException, PushException {
        // floor switches should only be interacted with by 'heavy' objects, e.g. boulders or the player. 
        // if a boulder interacts with it, the logic in Boulder's interact() must call this method (with itself?)
        if (entity.getClass().equals(Boulder.class)) {
            boulderOnTop = (Boulder) entity;
        }
        if (entity.getClass().equals(Player.class)) {
            if (boulderOnTop == null) throw new NoInteractionException();
            Player player = (Player) entity;

            boulderOnTop.interact(player); //will raise exception pushexception if not possible and stop here
            boulderOnTop = null;
        }
        updateActivationStatus();
    }

    public BooleanProperty isActivated() {
        return isActivated;
    }

    private void updateActivationStatus() {
        this.isActivated.set(boulderOnTop != null);
        setPassable(boulderOnTop == null);
    }


}