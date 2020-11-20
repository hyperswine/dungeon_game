package test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import unsw.dungeon.exceptions.NoInteractionException;
import unsw.dungeon.exceptions.NoSpaceException;
import unsw.dungeon.exceptions.PushException;
import unsw.dungeon.model.Dungeon;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.worldobject.Boulder;
import unsw.dungeon.model.worldobject.FloorSwitch;
import unsw.dungeon.model.worldobject.Wall;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class BoulderAndSwitchTest {

    private Dungeon dungeon;
    private Boulder b1,b2,b3;
    private FloorSwitch s1,s2;
    private Player player;
    private Wall w;


    public void setUp() throws FileNotFoundException {
        JSONObject json = new JSONObject();

        json.put("width", Integer.toString(4));
        json.put("height", Integer.toString(4));

        JSONArray jsonArr = new JSONArray();
        JSONObject entPlayer = new JSONObject();
        entPlayer.put("x", 1);
        entPlayer.put("y", 1);
        entPlayer.put("type", "player");

        org.json.JSONObject entb1 = new JSONObject();
        entb1.put("type", "boulder");
        entb1.put("x", 0);
        entb1.put("y", 0);

        org.json.JSONObject entb2 = new JSONObject();
        entb2.put("type", "boulder");
        entb2.put("x", 2);
        entb2.put("y", 1);

        org.json.JSONObject entb3 = new JSONObject();
        entb3.put("type", "boulder");
        entb3.put("x", 1);
        entb3.put("y", 2);

        org.json.JSONObject ents1 = new JSONObject();
        ents1.put("type", "switch");
        ents1.put("x", 1);
        ents1.put("y", 2);

        org.json.JSONObject ents2 = new JSONObject();
        ents2.put("type", "switch");
        ents2.put("x", 3);
        ents2.put("y", 1);

        org.json.JSONObject entw = new JSONObject();
        entw.put("type", "wall");
        entw.put("x", 2);
        entw.put("y", 2);

        jsonArr.put(entPlayer);
        jsonArr.put(ents1);
        jsonArr.put(entb1);
        jsonArr.put(entb2);
        jsonArr.put(entb3);
        jsonArr.put(ents2);
        jsonArr.put(entw);

        JSONObject goal = new JSONObject();
        json.put("goal-condition", goal);

        json.put("entities", jsonArr);


        DungeonMockLoader dMC = new DungeonMockLoader(json);
        dungeon = dMC.load();
        player = (Player) dungeon.getEntityAtCoords(1,1);
        b1 = (Boulder) dungeon.getEntityAtCoords(0,0);
        b2 = (Boulder) dungeon.getEntityAtCoords(2,1);
        b3 = (Boulder) dungeon.getListEntityAtCoords(1,2).get(1);
        s1 = (FloorSwitch) (dungeon.getListEntityAtCoords(1,2).get(0));
        s2 = (FloorSwitch) dungeon.getEntityAtCoords(3,1);
        w = (Wall) dungeon.getEntityAtCoords(2,2);

//        dungeon = new Dungeon(4,4);
//        DungeonProbe dP = new DungeonProbe(dungeon);
//        player = new Player(1,1);
//        dungeon.addEntity(player, dP);
//        b1 = new Boulder(0,0);
//        dungeon.addEntity(b1,dP);
//        b2 = new Boulder(2,1);
//        dungeon.addEntity(b2, dP);
//        b3 = new Boulder(1,2);
//        dungeon.addEntity(b3, dP);
//        s1 = new FloorSwitch(1,2);
//        dungeon.addEntity(s1, dP);
//        s2 = new FloorSwitch(3,1);
//        dungeon.addEntity(s2, dP);
//        w = new Wall(2,2);
//        dungeon.addEntity(w,dP);
//        dungeon.finaliseEntities();
    }

    @Test
    public void testActivatedStatus() throws FileNotFoundException {
        setUp();
        //Already activated on load
        assertTrue(s1.isActivated().get());
        assertFalse(s2.isActivated().get());
    }

    @Test
    public void basicMoveBoulder() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
        player.interact();
        assertEquals(3,b2.getX());
        assertEquals(1,b2.getY());
    }

    @Test
    public void moveBoulderOffSwitch() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveDown();
        });
        player.interact();
        assertEquals(1,b3.getX());
        assertEquals(3,b3.getY());
        assertFalse(s1.isActivated().get());
    }

    @Test
    public void moveBoulderOnSwitch() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
        player.interact();
        assertEquals(3,b2.getX());
        assertEquals(1,b2.getY());
        assertTrue(s2.isActivated().get());
    }

    @Test
    public void moveBoulderUnable() throws Exception {
        setUp();
        player.moveUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveLeft();
        });
        assertThrows(PushException.class, () -> {
            player.interact();
        });
        player.moveDown();
        player.moveLeft();
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
        assertThrows(PushException.class, () -> {
            player.interact();
        });
        player.moveRight();
        player.moveUp();
        player.moveRight();
        assertThrows(NoSpaceException.class, () -> {
            player.moveDown();
        });
        assertThrows(PushException.class, () -> {
            player.interact();
        });
    }

    @Test
    public void moveBoulderUnableBoulderOnSwitch() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
        player.interact();
        player.moveLeft();
        player.moveDown();
        player.moveDown();
        player.moveRight();
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
        player.interact();
        player.moveUp();
        player.moveLeft();
        player.moveUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
        player.interact();
        player.moveRight();
        player.interact();
        player.moveRight();
        assertThrows(PushException.class, () -> {
            player.interact();
        });
    }

    @Test
    public void interactUnTriggeredSwitch() throws Exception {
        setUp();
        player.setPos(3,3);
        player.moveUp();
        assertThrows(NoInteractionException.class, () -> {
            player.interact();
        });
    }
}