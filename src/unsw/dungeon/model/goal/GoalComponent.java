package unsw.dungeon.model.goal;

import javafx.beans.property.BooleanProperty;

import java.util.Iterator;

/**
 * Stores a goal and methods associated with it.
 * <p>
 * Goals may be very abstract and associated with 'strand' type games.
 * <p>
 * Hence these may be called strand-type goals.
 */
public interface GoalComponent {
    void add(GoalComponent goal);
    GoalComponent getChild(int index);
    boolean remove(GoalComponent goal);
    Iterator<GoalComponent> getIterator();

    BooleanProperty isSatisfied();
    void updateGoalStatus(boolean satisfy);

    String goalStatus();
    GoalType getGoal();

    void satisfyGoal(GoalType goalType);
}