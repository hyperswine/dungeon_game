package test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import unsw.dungeon.exceptions.NoSpaceException;
import unsw.dungeon.exceptions.WrongOrNoKeyException;
import unsw.dungeon.model.Dungeon;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.worldobject.item.Key;
import unsw.dungeon.model.worldobject.Door;
import unsw.dungeon.prober.DungeonProbe;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class KeyAndDoorTest {

    private Dungeon dungeon;
    private Player player;
    private Key k1, k2;
    private Door d1, d2;
    private DungeonProbe dP;

    public void setUp() throws FileNotFoundException {
        JSONObject json = new JSONObject();

        json.put("width", Integer.toString(4));
        json.put("height", Integer.toString(4));

        JSONArray jsonArr = new JSONArray();
        JSONObject entPlayer = new JSONObject();
        entPlayer.put("x", 1);
        entPlayer.put("y", 2);
        entPlayer.put("type", "player");

        JSONObject entk1 = new JSONObject();
        entk1.put("type", "key");
        entk1.put("x", 0);
        entk1.put("y", 2);
        entk1.put("id", 1);

        JSONObject entk2 = new JSONObject();
        entk2.put("type", "key");
        entk2.put("x", 1);
        entk2.put("y", 1);
        entk2.put("id", 2);

        JSONObject entd1 = new JSONObject();
        entd1.put("type", "door");
        entd1.put("x", 3);
        entd1.put("y", 2);
        entd1.put("id", 1);

        JSONObject entd2 = new JSONObject();
        entd2.put("type", "door");
        entd2.put("x", 1);
        entd2.put("y", 3);
        entd2.put("id",2);

        jsonArr.put(entPlayer);
        jsonArr.put(entk1);
        jsonArr.put(entk2);
        jsonArr.put(entd1);
        jsonArr.put(entd2);

        JSONObject goal = new JSONObject();
        json.put("goal-condition", goal);

        json.put("entities", jsonArr);


        DungeonMockLoader dMC = new DungeonMockLoader(json);
        dungeon = dMC.load();
        player = (Player) dungeon.getEntityAtCoords(1,2);
        k1 = (Key) dungeon.getEntityAtCoords(0,2);
        k2 = (Key) dungeon.getEntityAtCoords(1,1);
        d1 = (Door) dungeon.getEntityAtCoords(3,2);
        d2 = (Door) dungeon.getEntityAtCoords(1,3);
//        dungeon = new Dungeon(4,4);
//        dP = new DungeonProbe(dungeon);
//        player = new Player(1,2);
//        k1 = new Key(0,2,1);
//        k2 = new Key(1,1,2);
//        d1 = new Door(3,2,1);
//        d2 = new Door(1,3,2);
//        dungeon.addEntity(player,dP);
//        dungeon.addEntity(k1,dP);
//        dungeon.addEntity(k2,dP);
//        dungeon.addEntity(d1,dP);
//        dungeon.addEntity(d2,dP);
//        dungeon.finaliseEntities();
    }

    @Test
    public void testClosedDoorNotPassable() throws Exception {
        setUp();
        assertFalse(d2.isPassable());
        assertThrows(NoSpaceException.class, () -> {
            player.moveDown();
        });
        assertFalse(d1.isPassable());
        player.moveRight();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
    }

    @Test
    public void testOpenDoorOneByOne() throws Exception {
        setUp();
        assertFalse(d2.unlockedOpen().get());
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
        player.interact();
        assertThrows(NoSpaceException.class, () -> {
            player.moveDown();
        });
        player.interact();
        assertTrue(d2.unlockedOpen().get());

        assertFalse(d1.unlockedOpen().get());
        assertThrows(NoSpaceException.class, () -> {
            player.moveLeft();
        });
        player.interact();
        player.moveRight();
        player.interact();
        assertTrue(d1.unlockedOpen().get());
    }

    @Test
    public void testOpenDoorBothKeysAtOnce() throws Exception {
        setUp();
        assertFalse(d1.unlockedOpen().get());
        assertFalse(d2.unlockedOpen().get());
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
        player.interact();
        assertThrows(NoSpaceException.class, () -> {
            player.moveLeft();
        });
        player.interact();
        assertThrows(NoSpaceException.class, () -> {
            player.moveDown();
        });
        player.interact();
        assertTrue(d2.unlockedOpen().get());
        player.moveRight();
        player.interact();
        assertTrue(d1.unlockedOpen().get());
    }

    @Test
    public void testOpenWrongDoor1() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveLeft();
        });
        player.interact(); //getting key 1
        //try to use on door 2
        assertThrows(NoSpaceException.class, () -> {
            player.moveDown();
        });
        assertThrows(WrongOrNoKeyException.class, () -> {
            player.interact();
        });
    }

    @Test
    public void testOpenWrongDoor2() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
        player.interact(); //getting key 2
        //try to use on door 1
        player.moveRight();
        assertThrows(WrongOrNoKeyException.class, () -> {
            player.interact();
        });
    }

    @Test
    public void checkOpenDoorIsPassable() throws Exception {
        setUp();
        testOpenDoorOneByOne();
        assertTrue(d1.isPassable());
        assertTrue(d2.isPassable());
        player.moveRight();
        player.moveLeft();
        player.moveLeft();
        player.moveDown();
    }

}