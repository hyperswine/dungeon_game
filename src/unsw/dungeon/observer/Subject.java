package unsw.dungeon.observer;

/**
 * A custom subject to be observed upon.
 *
 * Supports both 'push' & '+ pull' observations. Tends to favor push observations.
 */
public interface Subject {
    // notify observers -> pull
    default void notifyObservers() {
        return;
    }

    // notify observers -> push
    void notifyObservers(Object obj);

    // add/remove listeners. Return true if methods succeed, false otherwise.
    boolean addListener(Object obj);

    boolean removeListener(Object obj);
}
