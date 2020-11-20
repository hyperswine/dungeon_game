package test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import unsw.dungeon.model.Dungeon;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Basic test to create a mock controller & test some basic functionality.
 */
public class JSONTest {

    public JSONObject createJSON0(){
        JSONObject json = new JSONObject();

        // TODO: add a dungeon here.

        return json;
    }

    /**
     * 
     * @param w > 3
     * @param h > 3
     * @return A mock controller
     */
    public DungeonMockLoader createController(int w, int h){
        JSONObject json = new JSONObject();

        json.put("width", Integer.toString(w));
        json.put("height", Integer.toString(h));

        // TODO: put logic for creating entities in separate method
        JSONArray jsonArr = new JSONArray();
        JSONObject entityX = new JSONObject();
        entityX.put("x", 1);
        entityX.put("y", 1);
        entityX.put("type", "player");

        JSONObject entityY = new JSONObject();
        entityY.put("type", "enemy");
        entityY.put("x", 2);
        entityY.put("y", 2);

        JSONObject entityZ = new JSONObject();
        entityZ.put("type", "exit");
        entityZ.put("x", Integer.toString(w));
        entityZ.put("y", Integer.toString(h));

        jsonArr.put(entityX);
        jsonArr.put(entityY);
        jsonArr.put(entityZ);

        JSONObject goal = new JSONObject();
        goal.put("goal", "enemies");

        json.put("entities", jsonArr);
        json.put("goal-condition", goal);

        DungeonMockLoader dMC = new DungeonMockLoader(json);

        return dMC;
    }

    @Test
    public void testBasic(){
        DungeonMockLoader dMC = createController(10, 10);
        Dungeon dungeon = dMC.load();

        assertTrue(dungeon.getHeight()==10); 
        assertTrue(dungeon.getWidth()==10);
    }
}