package unsw.dungeon.exceptions;

import unsw.dungeon.model.worldobject.Portal;

public class TeleportException extends Exception {
    private Portal portal;

    public TeleportException(Portal portal){
        this.portal = portal;
    }
}