package unsw.dungeon.model.worldobject;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import unsw.dungeon.exceptions.WrongOrNoKeyException;
import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.view.AudioPlayer;

public class Door extends Entity {

    // NOTE: this is automatically assigned false
    private BooleanProperty unlockedOpen;
    private int id;

    public Door(int x, int y, int id){
        super(x, y);
        this.unlockedOpen = new SimpleBooleanProperty();
        this.id = id;
    }

    /**
     * Check if player has a key of the same id. If he has a key of the same id, open the door.
     * @pre obj != null
     */
    @Override
    public void interact(Entity entity) throws WrongOrNoKeyException {
        if (entity.getClass().equals(Player.class)) {
            Player player = (Player) entity;
            if (player.useKey(id)) {
                //successfully found key -> do not have to get rid of key, player just takes the key back.
                unlockedOpen.set(true);
                isPassable = true;
                AudioPlayer.playSound("DOOR_OPEN");
            } else throw new WrongOrNoKeyException();
        }
    }

    public BooleanProperty unlockedOpen() {
        return unlockedOpen;
    }

}
