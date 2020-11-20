package unsw.dungeon.model.worldobject;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import unsw.dungeon.model.Entity;

public class Exit extends Entity {
    private BooleanProperty interacted;

    public Exit(int x, int y) {
        super(x, y);
        interacted = new SimpleBooleanProperty();
        interacted.set(false);
    }

    /**
     * Goal Controller listens in. Player should exit dungeon when exit is interacted with,
     * regardless of whether goals are complete.
     */
    @Override
    public void interact(Entity entity) {
        interacted.set(true);
    }

    public BooleanProperty interacted() {
        return interacted;
    }

}