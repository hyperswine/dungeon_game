package unsw.dungeon.model.worldobject;

import unsw.dungeon.exceptions.NoInteractionException;
import unsw.dungeon.model.Entity;

public class Wall extends Entity{

    public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    public void interact(Entity entity) throws NoInteractionException {
        //Cannot interact with wall...
        throw new NoInteractionException();
    }

}
