package unsw.dungeon.model.goal;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Goal that contains subgoals.
 * <p>
 * Contains a list of goal components that are either AND or OR with each other.
 * <p>
 */
public class GoalComposite implements GoalComponent {

    private List<GoalComponent> subGoals;
    private BooleanProperty goalSatisfied;

    // AND, OR only
    private GoalType subConditions;

    /**
     * Create a composite goalcomponent type object.
     * 
     * @param subConditions AND, OR only.
     */
    public GoalComposite(GoalType subConditions) {
        subGoals = new ArrayList<>();
        goalSatisfied = new SimpleBooleanProperty();
        goalSatisfied.set(false);
        this.subConditions = subConditions;
    }

    /**
     *
     * @return Type of goal -> AND/OR
     */
    public GoalType getGoal() {
        return subConditions;
    }

    @Override
    public void satisfyGoal(GoalType goalType) {
        for (GoalComponent g : subGoals) {
            g.satisfyGoal(goalType);
        }
    }

    @Override
    public void add(GoalComponent goal) {
        subGoals.add(goal);
    }

    @Override
    public GoalComponent getChild(int index) {
        if (index < subGoals.size())
            return subGoals.get(index);

        return null;
    }

    @Override
    public boolean remove(GoalComponent goal) {
        return subGoals.remove(goal);
    }

    /**
     * NOTE: this should recursively call subcomponents and ensure either 1 of them
     * is satisfied, or all of them is satisifed.
     */
    @Override
    public BooleanProperty isSatisfied() {
        boolean flag = false;
    
        switch (getGoal()) {
            case AND:
                flag = true;
                for (GoalComponent g : subGoals) {
                    if (!g.isSatisfied().get()) {
                        flag = false;
                        break;
                    }
                }

                break;

            case OR:
                for (GoalComponent g : subGoals) {
                    if (g.isSatisfied().get()) {
                        flag = true;
                        break;
                    }
                }
                break;
            default:
                break;
        }

        // if goals satisfied, set true
        if(flag) updateGoalStatus(true);

        return goalSatisfied;
    }

    /**
     * For testing purposes.
     */
    @Override
    public void updateGoalStatus(boolean satisfy) {
        goalSatisfied.set(satisfy);
    }

    @Override
    public String goalStatus() {
        StringBuilder result = new StringBuilder();
        result.append(isSatisfied().get() ? "✓ " : "✕ ");
        result.append(subConditions == GoalType.AND ? "All of the following: \n" : "One of the following: \n");
        for (GoalComponent g : subGoals) {
            result.append(g.goalStatus());
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public Iterator<GoalComponent> getIterator() {
        return subGoals.iterator();
    }

}