package unsw.dungeon.observer;

/**
 * Custom observer. E.g. may be observing on PosVec, enemy, goals, extensions.
 *
 */
public interface Observer {
    // NOTE: obj should not be an aggregate

    // push observation, obj updated by subject.
    void update(Object obj);
}
