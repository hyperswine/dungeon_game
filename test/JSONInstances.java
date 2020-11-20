package test;

import org.json.JSONArray;
import org.json.JSONObject;

import unsw.dungeon.model.position.PosVec;

/**
 * Contains some JSON instances for use.
 */
public class JSONInstances {
    /**
     * 
     * @param w > 100
     * @param h > 100
     * @return A mock controller
     */
    public DungeonMockLoader createBasicController(int w, int h){
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

        JSONObject entityPortal = new JSONObject();
        entityPortal.put("type", "portal");
        entityPortal.put("x", Integer.toString(5));
        entityPortal.put("y", Integer.toString(3));
        entityPortal.put("id", Integer.toString(0));

        JSONObject entityPortal2 = new JSONObject();
        entityPortal2.put("type", "portal");
        entityPortal2.put("x", Integer.toString(99));
        entityPortal2.put("y", Integer.toString(100));
        entityPortal2.put("id", Integer.toString(0));

        jsonArr.put(entityX);
        jsonArr.put(entityY);
        jsonArr.put(entityZ);
        jsonArr.put(entityPortal);
        jsonArr.put(entityPortal2);

        JSONObject goal = new JSONObject();
        goal.put("goal", "enemies");

        json.put("entities", jsonArr);
        json.put("goal-condition", goal);

        DungeonMockLoader dMC = new DungeonMockLoader(json);

        return dMC;
    }

    /**
     * Ensure 2 position vectors are exactly one unit of distance apart
     */
    public boolean oneUnitApart(PosVec p1, PosVec p2){
        int d1 = Math.abs(p1.getXInt() - p2.getXInt());
        int d2 = Math.abs(p1.getYInt() - p2.getYInt());

        return (Math.max(d1, d2)==1);
    }
}