package unsw.dungeon.exceptions;

/**
 * For combatant entities that are able to push/move objects
 * Exception if player tries to move a boulder or some object
 * with an interact() that is meant to move them
 */
public class PushException extends Exception{
    private Object obj;

    public PushException(Object obj) {
        this.obj = obj;
    }
}