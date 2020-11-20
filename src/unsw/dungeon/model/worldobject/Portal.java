package unsw.dungeon.model.worldobject;

import unsw.dungeon.exceptions.TeleportException;
import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.view.AudioPlayer;

public class Portal extends Entity {
    
    // a portal must map to another portal
    private Portal portalExit;
    private int id;

    public Portal(int x, int y, int id){
        super(x, y);
        this.id = id;
        setPassable(false);
    }

    public void updateLink() {
        portalExit = findCorrespondingPortal();
    }

    private Portal findCorrespondingPortal() {
        for (int x = 0; x < dungeonProbe.getWidth(); x++) {
            for (int y = 0; y < dungeonProbe.getHeight(); y++) {
                Entity entity = dungeonProbe.getEntityAtCoords(x,y);
                if (entity != null && !entity.equals(this) && entity.getClass().equals(Portal.class) && ((Portal) entity).getID() == id) {
                    return (Portal) entity;
                }
            }
        }
        return null;
    }

    public Portal getPortalExit() {
        return portalExit;
    }


    public int getID() {
        return this.id;
    }

    @Override
    public void interact(Entity entity) throws TeleportException{
        // teleport the player to the same tile as portalExit
        // throw an exception that tells controller to do it
        if (!(entity.getClass().equals(Player.class)) || portalExit == null) {
            throw new TeleportException(null);
        }
        AudioPlayer.playSound("TELEPORT");
        Player player = (Player) entity;

        player.setPos(portalExit.getX(), portalExit.getY());

    }

}