package unsw.dungeon.model.worldobject;

import unsw.dungeon.exceptions.NoInteractionException;
import unsw.dungeon.exceptions.PushException;
import unsw.dungeon.model.Entity;
import unsw.dungeon.model.position.DirectionFacing;
import unsw.dungeon.model.combatant.Player;

import java.util.List;

public class Boulder extends Entity {
    public Boulder(int x, int y) {
        super(x, y);
    }

    /**
     * In some states (upcoming), boulders could be pushed onto combatants -> enemies should walk another
     * tile when boulder hits their tile, vice-versa for the player.
     *
     * @param entity - The object, usually a combatant that pushes the boulder
     * @throws PushException          - Boulder can't be pushed onto a certain tile
     * @throws NoInteractionException - Boulder can't be interacted with
     */
    @Override
    public void interact(Entity entity) throws PushException, NoInteractionException {
        if (!(entity instanceof Player)) throw new PushException("This shouldn't have happened...");
        Player player = (Player) entity;
        DirectionFacing facing = player.directionFacing().get();

        int destX = getX();
        int destY = getY();

        switch (facing) {
            case UP:
                destY = getY() - 1;
                break;
            case DOWN:
                destY = getY() + 1;
                break;
            case LEFT:
                destX = getX() - 1;
                break;
            case RIGHT:
                destX = getX() + 1;
                break;
        }

        List<Entity> listEntityAtDest = dungeonProbe.getListEntityAtCoords(destX, destY);
        FloorSwitch floorSwitchDest = floorSwitchAt(destX, destY);
        FloorSwitch floorSwitchHere = floorSwitchAt(getX(), getY());

        if (listEntityAtDest.size() == 0 && destY > 0 && destX < dungeonProbe.getWidth() && destY > 0 && destY < dungeonProbe.getHeight()) {
            position.setPos(destX, destY);
        } else if (floorSwitchDest != null) {
            if (listEntityAtDest.size() <= 1) { //there is a boulder
                floorSwitchDest.interact(this);
                position.setPos(destX, destY);
            } else throw new PushException(this);
        } else {
            throw new PushException(this);
        }

        if (floorSwitchHere != null) floorSwitchHere.setBoulderOnTop(null);
    }

    /**
     * Determines whether a UNTRIGGERED floorswitch (i.e. with no boulder on top) exists at these coordinates.
     *
     * @param x
     * @param y
     * @return
     */
    private FloorSwitch floorSwitchAt(int x, int y) {
        List<Entity> entityList = dungeonProbe.getListEntityAtCoords(x, y);
        for (Entity entity : entityList) {
            if (entity.getClass().equals(FloorSwitch.class)) {
                return (FloorSwitch) entity;
            }
        }
        return null;
    }

}