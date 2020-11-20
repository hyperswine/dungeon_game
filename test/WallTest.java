package test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import unsw.dungeon.exceptions.NoInteractionException;
import unsw.dungeon.exceptions.NoSpaceException;
import unsw.dungeon.model.Dungeon;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.worldobject.Wall;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertThrows;

public class WallTest {

    private Dungeon dungeon;
    private Player player;
    private Wall wall;

    public void setUp() throws FileNotFoundException {
        JSONObject json = new JSONObject();

        json.put("width", Integer.toString(3));
        json.put("height", Integer.toString(3));

        // TODO: put logic for creating entities in separate method
        JSONArray jsonArr = new JSONArray();
        JSONObject entityX = new JSONObject();
        entityX.put("x", 1);
        entityX.put("y", 2);
        entityX.put("type", "player");

        JSONObject entityY = new JSONObject();
        entityY.put("type", "wall");
        entityY.put("x", 1);
        entityY.put("y", 1);

        jsonArr.put(entityX);
        jsonArr.put(entityY);

        JSONObject goal = new JSONObject();
        goal.put("goal", "enemies");
        json.put("goal-condition", goal);

        json.put("entities", jsonArr);


        DungeonMockLoader dMC = new DungeonMockLoader(json);

        dungeon = dMC.load();
        player = (Player) dungeon.getEntityAtCoords(1,2);
        wall = (Wall) dungeon.getEntityAtCoords(1,1);
//        dungeon = new Dungeon(3,3);
//        DungeonProbe dP = new DungeonProbe(dungeon);
//        player = new Player(1,2);
//        wall = new Wall(1,1);
//        dungeon.addEntity(player, dP);
//        dungeon.addEntity(wall, dP);
    }

    @Test
    public void testUp() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
    }
    @Test
    public void testDown() throws Exception {
        setUp();
        player.moveLeft();
        player.moveUp();
        player.moveUp();
        player.moveRight();
        assertThrows(NoSpaceException.class, () -> {
            player.moveDown();
        });
    }
    @Test
    public void testLeft() throws Exception {
        setUp();
        player.moveRight();
        player.moveUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveLeft();
        });
    }
    @Test
    public void testRight() throws Exception {
        setUp();
        player.moveLeft();
        player.moveUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
    }

    @Test
    public void testInteract() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });

        assertThrows(NoInteractionException.class, () -> {
            player.interact();
        });
    }


}