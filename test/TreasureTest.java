package test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import unsw.dungeon.exceptions.NoSpaceException;
import unsw.dungeon.model.Dungeon;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.worldobject.item.Treasure;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TreasureTest {
    private Treasure t1,t2,t3;
    private Player player;
    private Dungeon dungeon;

    public void setUp() throws FileNotFoundException {
        JSONObject json = new JSONObject();

        json.put("width", Integer.toString(3));
        json.put("height", Integer.toString(3));

        // TODO: put logic for creating entities in separate method
        JSONArray jsonArr = new JSONArray();
        JSONObject ePlayer = new JSONObject();
        ePlayer.put("x", 1);
        ePlayer.put("y", 1);
        ePlayer.put("type", "player");

        JSONObject et1 = new JSONObject();
        et1.put("type", "treasure");
        et1.put("x", 1);
        et1.put("y", 0);

        JSONObject et2 = new JSONObject();
        et2.put("type", "treasure");
        et2.put("x", 1);
        et2.put("y", 2);

        JSONObject et3 = new JSONObject();
        et3.put("type", "treasure");
        et3.put("x", 2);
        et3.put("y",2);

        jsonArr.put(ePlayer);
        jsonArr.put(et1);
        jsonArr.put(et2);
        jsonArr.put(et3);
        jsonArr.put(et3);


        JSONObject goal = new JSONObject();
        goal.put("goal", "enemies");
        json.put("goal-condition", goal);

        json.put("entities", jsonArr);


        DungeonMockLoader dMC = new DungeonMockLoader(json);

        dungeon = dMC.load();
        player = (Player) dungeon.getEntityAtCoords(1,1);
        t1 = (Treasure) dungeon.getEntityAtCoords(1,0);
        t2 = (Treasure) dungeon.getEntityAtCoords(1,2);
        t3 = (Treasure) dungeon.getEntityAtCoords(2,2);

    }

    @Test
    public void testPickupSingle1() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
        player.interact();
        assertEquals(1, player.treasureCount().get());
    }

    @Test
    public void testPickupSingle2() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveDown();
        });
        player.interact();
        assertEquals(1, player.treasureCount().get());
    }

    @Test
    public void testPickupAll() throws Exception {
        setUp();
        assertEquals(0, player.treasureCount().get());
        assertThrows(NoSpaceException.class, () -> {
            player.moveDown();
        });
        player.interact();
        assertEquals(1, player.treasureCount().get());
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
        player.interact();
        assertEquals(2, player.treasureCount().get());
        player.moveRight();
        assertThrows(NoSpaceException.class, () -> {
            player.moveDown();
        });
        player.interact();
        assertEquals(3, player.treasureCount().get());
    }
}
