package unsw.dungeon.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Window;
import org.json.JSONArray;
import org.json.JSONObject;
import unsw.dungeon.model.goal.GoalComponent;
import unsw.dungeon.model.goal.GoalComposite;
import unsw.dungeon.model.goal.GoalLeaf;
import unsw.dungeon.model.goal.GoalType;
import unsw.dungeon.prober.DungeonProbe;

import java.util.Iterator;

/**
 * Control, manipulate and listen on goals/ SUGGESTION 1: attach this as an
 * observer to dungeon. If something changes in dungeon, then call a method to
 * check whether given goals may have been satisfied
 */
public class GoalController {
    // composite tree of goals
    private GoalComponent goalTree;
    private DungeonProbe dungeonProbe;
    private BooleanProperty goalsComplete;
    private Window window;

    public GoalController(JSONObject goals, DungeonProbe dungeonProbe) {
        goalsComplete = new SimpleBooleanProperty();
        // set goals complete to false
        goalsComplete.set(false);

        // add all the goals
        goalTree = addAllGoals(goals);

        this.dungeonProbe = dungeonProbe;

        dungeonProbe.currentTreasureCollected().addListener((observable, oldValue, newValue) -> checkTreasureSatisfied());
        dungeonProbe.enemiesLeft().addListener((observable, oldValue, newValue) -> checkEnemiesSatisfied());
        dungeonProbe.floorSwitchesTriggered().addListener(((observable, oldValue, newValue) -> checkBouldersSatisfied()));
        dungeonProbe.exitInteracted().addListener(((observable, oldValue, newValue) -> attemptExit()));

        System.out.println(goalStatus());
    }

    private void attemptExit() {
        Iterator<GoalComponent> gIterator = goalTree.getIterator();

        while(gIterator.hasNext()){
            GoalComponent goalComponent = gIterator.next();
            if( !goalComponent.getGoal().equals(GoalType.EXIT) && !goalComponent.isSatisfied().get()) {
                dungeonProbe.setExitInteractedFalse();
                ToastGenerator toastGenerator = new ToastGenerator();
                toastGenerator.showToast(window, "You can't exit until you've completed the remaining goals!");
                return;
            }
        }
        goalsComplete.set(true);
    }

    private void checkTreasureSatisfied() {
        if (dungeonProbe.currentTreasureCollected().get() >= dungeonProbe.totalTreasureCount()) {
            satisfyGoal(GoalType.TREASURE);
        }
    }

    private void checkEnemiesSatisfied() {
        if (dungeonProbe.enemiesLeft().get() <= 0) {
            satisfyGoal(GoalType.ENEMIES);
        }
    }

    private void checkBouldersSatisfied() {
        if (dungeonProbe.floorSwitchesTriggered().get()) {
            satisfyGoal(GoalType.BOULDERS);
        }
    }
    /**
     * Recursively parse a JSONObject and return itself and its subcomponents
     * 
     * @param jsonObject A Mapping or an Object
     */
    public GoalComponent addAllGoals(JSONObject jsonObject) {
        // store a goal component
        GoalComponent currComponent;

        JSONArray jsonArray;

        // create a composite for and/or
        if (jsonObject.get("goal").equals("AND")) {
            currComponent = new GoalComposite(GoalType.AND);

            jsonArray = (JSONArray) jsonObject.get("subgoals");
            for (Object o : jsonArray) {
                // add each subgoal (tree) as a component
                currComponent.add(addAllGoals((JSONObject) o));
            }

        } else if (jsonObject.get("goal").equals("OR")) {
            currComponent = new GoalComposite(GoalType.OR);

            jsonArray = (JSONArray) jsonObject.get("subgoals");
            for (Object o : jsonArray) {
                currComponent.add(addAllGoals((JSONObject) o));
            }
        } else {
            // else just one single object -> String
            String goalX = (String) jsonObject.get("goal");
            GoalType goalT = GoalType.valueOf(goalX.toUpperCase());

            currComponent = new GoalLeaf(goalT);
        }

        return currComponent;

    }

    /**
     * Satisfy a given goal type.
     */
    public void satisfyGoal(GoalType goalToSatisfy) {
        goalTree.satisfyGoal(goalToSatisfy);
        goalsComplete();
    }

    /**
     * Checks whether goals have been satisfied.
     * <p>
     * NOTE: Probably a better/ more streamlined way to do this. But still quite flexible for now.
     * 
     * @return boolean on satisfication
     */
    public BooleanProperty goalsComplete() {
        goalsComplete.set(goalTree.isSatisfied().get());
        return goalsComplete;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public String goalStatus() {
        return goalTree.goalStatus();
    }

}