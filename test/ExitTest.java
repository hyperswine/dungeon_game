package test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import unsw.dungeon.exceptions.*;
import unsw.dungeon.model.Dungeon;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.worldobject.Exit;

import static org.junit.Assert.assertThrows;

public class ExitTest {
    private Dungeon dungeon;
    private Player player;
    private Exit exit;

    public void setUp() {
        JSONObject json = new JSONObject();

        json.put("width", Integer.toString(2));
        json.put("height", Integer.toString(2));

        JSONArray jsonArr = new JSONArray();
        JSONObject entPlayer = new JSONObject();
        entPlayer.put("x", 1);
        entPlayer.put("y", 1);
        entPlayer.put("type", "player");

        org.json.JSONObject entExit = new JSONObject();
        entExit.put("type", "exit");
        entExit.put("x", 1);
        entExit.put("y", 0);

        jsonArr.put(entPlayer);
        jsonArr.put(entExit);

        JSONObject goal = new JSONObject();
        json.put("goal-condition", goal);

        json.put("entities", jsonArr);

        DungeonMockLoader dMC = new DungeonMockLoader(json);
        dungeon = dMC.load();
        player = (Player) dungeon.getEntityAtCoords(1,1);
        exit = (Exit) dungeon.getEntityAtCoords(1,0);

    }

    @Test
    public void testInteract() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
        player.interact();
    }

}
