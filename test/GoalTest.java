package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import unsw.dungeon.controller.GoalController;
import unsw.dungeon.model.goal.GoalComponent;
import unsw.dungeon.model.goal.GoalComposite;
import unsw.dungeon.model.goal.GoalLeaf;
import unsw.dungeon.model.goal.GoalType;

/**
 * Test the goal components. Ensure fully functional compositions.
 * <p>
 * Will not be connecting frontend or mock controller for loosely coupled
 * testing.
 */
public class GoalTest {
    public GoalController setUpGoalController() {
        JSONObject goals1 = new JSONObject();
        goals1.put("goal", "boulders");

        JSONArray arr = new JSONArray();
        arr.put(goals1);

        // add few goals
        JSONObject goals2 = new JSONObject();
        goals2.put("goal", "AND");
        goals2.put("subgoals", arr);

        GoalController goalController = new GoalController(goals2);

        return goalController;
    }

    public GoalController setUpGoalController2() {
        JSONObject goals1 = new JSONObject();
        goals1.put("goal", "exit");
        JSONObject goals12 = new JSONObject();
        goals12.put("goal", "enemies");

        JSONArray arr = new JSONArray();
        arr.put(goals1);
        arr.put(goals12);

        // add few goals
        JSONObject goals2 = new JSONObject();
        goals2.put("goal", "OR");
        goals2.put("subgoals", arr);

        GoalController goalController = new GoalController(goals2);

        return goalController;
    }

    @Test
    public void testGoalController() {
        GoalController goalController = setUpGoalController();

        // attach a listener on goals complete
        BooleanProperty gComplete = new SimpleBooleanProperty();
        gComplete.bind(goalController.goalsComplete());

        // ensure goals havent already been completed
        assertTrue(!goalController.goalsComplete().get());
        // mark complete
        goalController.markComplete();

        // ensure listener changes value
        assertTrue(gComplete.get());

        // remark goals as incomplete
        goalController.markIncomplete();
        // ensure value has changed
        assertTrue(!gComplete.get());
        assertTrue(!goalController.goalsComplete().get());
    }

    @Test
    public void testGoalANDOR() {
        GoalController gController = setUpGoalController2();

        // no error should be thrown
        gController.update(null);

        assertTrue(!gController.goalsComplete().get());

        // satisfy given goals
        gController.satisfyGoal(GoalType.EXIT);

        assertTrue(!gController.goalsComplete().get());

        gController.satisfyGoal(GoalType.ENEMIES);

        // should be true after all goals satisfied
        assertTrue(gController.goalsComplete().get());
    }

    /**
     * Test the goal leaf for fundamental interactions and ensure expectations met.
     */
    @Test
    public void testGoalLeaf() {
        GoalLeaf gLeaf = new GoalLeaf(GoalType.BOULDERS);

        assertTrue(gLeaf.getGoal() == GoalType.BOULDERS);

        GoalLeaf gChild = new GoalLeaf(GoalType.TREASURE);

        assertTrue(gChild.getGoal() == GoalType.TREASURE);

        // adding to leaves shouldnt do anything
        gLeaf.add(gChild);
        assertTrue(gLeaf.getChild(0) == null);

        gLeaf.remove(gChild);
        assertTrue(gLeaf.getChild(0) == null);

        Iterator<GoalComponent> itGoals = gLeaf.getIterator();
        // leaf iterator should not be of any other type except itself
        // it should also be itself.
        assertTrue(itGoals.next().getClass().equals(GoalLeaf.class));
        assertTrue(!itGoals.hasNext());

        assertTrue(!gLeaf.isSatisfied().get());

        // satisify each goal
        gLeaf.updateGoalStatus(true);

        assertTrue(gLeaf.isSatisfied().get());

        // satisify each goal
        gChild.updateGoalStatus(true);

        assertTrue(gChild.isSatisfied().get());

    }

    /**
     * Various test procedures to ensure composite structure performing up to
     * expectations.
     */
    @Test
    public void testGoalComposite() {
        GoalComposite gComposite = new GoalComposite(GoalType.AND);
        GoalComposite gComposite2 = new GoalComposite(GoalType.OR);

        assertTrue(gComposite.getGoal()==GoalType.AND);
        assertTrue(gComposite2.getGoal()==GoalType.OR);

        // initialize goal leaves of the same condition
        // NOTE: this tests for abstract goals that may be specified by the .json.
        GoalLeaf gLeaf = new GoalLeaf(GoalType.EXIT);
        GoalLeaf gLeaf1 = new GoalLeaf(GoalType.EXIT);
        GoalLeaf gLeaf2 = new GoalLeaf(GoalType.EXIT);

        // add them to goal
        gComposite.add(gLeaf);
        gComposite.add(gLeaf1);

        // add the AND as a subgoal to the OR
        gComposite2.add(gComposite);
        gComposite2.add(gLeaf2);

        // these shouldn't throw an error
        GoalComponent gC1 = gComposite.getChild(0);
        GoalComponent gC2 = gComposite.getChild(1);

        GoalComponent gC3 = gComposite.getChild(2);
        assertTrue(gC1!=null && gC2!=null && gC3 == null);

        assertTrue(!gComposite.isSatisfied().get());
        assertTrue(!gComposite2.isSatisfied().get());

        gLeaf.updateGoalStatus(true);
        gLeaf1.updateGoalStatus(true);
        
        // should be satisified for AND
        assertTrue(gComposite.isSatisfied().get());

        // should be satisified for OR
        assertTrue(gComposite2.isSatisfied().get());

    }
}