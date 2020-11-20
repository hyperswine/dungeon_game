package unsw.dungeon.model.goal;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Single Goal. The leaves of the composite tree.
 * <p>
 * Goal is marked satisified upon satisfaction.
 * <p>
 * If there are multiple goals of the same goal leaf type, then mark them all
 * off as satisfied.
 */
public class GoalLeaf implements GoalComponent {

    // ENEMY, SWITCH, EXIT, TREASURE only
    private GoalType goal;

    BooleanProperty goalSatisfied;

    public GoalLeaf(GoalType goal) {
        this.goal = goal;
        this.goalSatisfied = new SimpleBooleanProperty();
        // NOTE: unless the goal was suppposed to be satisfied upon
        // game opening. Then the controller would have checked the
        // dungeon property (upon loading the game as well) to check
        // whether goal is satisfied. E.g. all switches have a boulder spawned on them.
        goalSatisfied.set(false);
    }

    @Override
    public void add(GoalComponent goal) {
    }

    /**
     *
     * @return Type of goal -> BOULDER, ENEMIES, TREASURE, EXIT
     */
    public GoalType getGoal() {
        return goal;
    }

    @Override
    public void satisfyGoal(GoalType goalType) {
        if (goalType.equals(this.goal))
            goalSatisfied.set(true);
    }

    @Override
    public GoalComponent getChild(int index) {
        return null;
    }

    @Override
    public boolean remove(GoalComponent goal) {
        return false;
    }

    @Override
    public BooleanProperty isSatisfied() {
        return goalSatisfied;
    }

    /**
     * Change the goal-complete status. Should be called by a listener.
     */
    public void updateGoalStatus(boolean satisfy) {
        goalSatisfied.set(satisfy);
    }

    @Override
    public String goalStatus() {
        String result = "";
        switch (goal) {
            case EXIT:
                result = "Get to the exit";
                break;
            case ENEMIES:
                result = "Kill all enemies";
                break;
            case BOULDERS:
                result = "Get all boulders on all switches";
                break;
            case TREASURE:
                result =  "Collect all treasure";
                break;
        }
        return (isSatisfied().get() ? "✓ " : "✕ ") + result ;
    }

    /**
     * Returns an iterator with 1 element - itself.
     */
    @Override
    public Iterator<GoalComponent> getIterator() {
        List<GoalComponent> goal = new ArrayList<>();
        goal.add(this);
        return goal.iterator();
    }

    // NOTE: adding an 'equals' method can be done here, and also in GoalComposite.
}