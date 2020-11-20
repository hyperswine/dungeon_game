package test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import unsw.dungeon.exceptions.NoSpaceException;
import unsw.dungeon.exceptions.TeleportException;
import unsw.dungeon.model.Dungeon;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.worldobject.Portal;
import unsw.dungeon.prober.DungeonProbe;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class PortalTest {

    private Dungeon dungeon;
    private Player player;
    private Portal p1A, p1B, p2A, p2B, p0;
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

        org.json.JSONObject entP1A = new JSONObject();
        entP1A.put("type", "portal");
        entP1A.put("x", 1);
        entP1A.put("y", 0);
        entP1A.put("id", 1);

        org.json.JSONObject entP1B = new JSONObject();
        entP1B.put("type", "portal");
        entP1B.put("x", 0);
        entP1B.put("y", 2);
        entP1B.put("id", 1);

        org.json.JSONObject entP2A = new JSONObject();
        entP2A.put("type", "portal");
        entP2A.put("x", 3);
        entP2A.put("y", 1);
        entP2A.put("id", 2);

        org.json.JSONObject entP2B = new JSONObject();
        entP2B.put("type", "portal");
        entP2B.put("x", 3);
        entP2B.put("y", 3);
        entP2B.put("id", 2);

        org.json.JSONObject entP0 = new JSONObject();
        entP0.put("type", "portal");
        entP0.put("x", 2);
        entP0.put("y", 2);
        entP0.put("id", 0);

        jsonArr.put(entPlayer);
        jsonArr.put(entP1A);
        jsonArr.put(entP1B);
        jsonArr.put(entP2A);
        jsonArr.put(entP2B);
        jsonArr.put(entP0);

        JSONObject goal = new JSONObject();
        json.put("goal-condition", goal);

        json.put("entities", jsonArr);


        DungeonMockLoader dMC = new DungeonMockLoader(json);
        dungeon = dMC.load();
        player = (Player) dungeon.getEntityAtCoords(1,2);
        p1A = (Portal) dungeon.getEntityAtCoords(1,0);
        p1B = (Portal) dungeon.getEntityAtCoords(0,2);
        p2A = (Portal) dungeon.getEntityAtCoords(3,1);
        p2B = (Portal) dungeon.getEntityAtCoords(3,3);
        p0 = (Portal) dungeon.getEntityAtCoords(2,2);
//        dungeon = new Dungeon(4,4);
//        dP = new DungeonProbe(dungeon);
//        player = new Player(1,2);
//        p1A = new Portal(1,0, 1);
//        dungeon.addEntity(p1A, dP);
//        p1B = new Portal(0,2,1);
//        dungeon.addEntity(p1B, dP);
//        p2A = new Portal(3,1,2);
//        dungeon.addEntity(p2A, dP);
//        p2B = new Portal(3,3,2);
//        dungeon.addEntity(p2B, dP);
//        dungeon.addEntity(player, dP);
//        dungeon.finaliseEntities();
    }

    @Test
    public void testNotNullLinks() throws FileNotFoundException {
        setUp();
        assertNotNull(p1A.getPortalExit());
        assertNotNull(p1B.getPortalExit());
        assertNotNull(p2A.getPortalExit());
        assertNotNull(p1B.getPortalExit());
    }

    @Test
    public void test1B1A() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveLeft();
        });
        player.interact();
        assertEquals(1, player.getX());
        assertEquals(0, player.getY());
    }

    @Test
    public void test1A1B() throws Exception {
        setUp();
        player.moveUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
        player.interact();
        assertEquals(0, player.getX());
        assertEquals(2, player.getY());
    }

    @Test
    public void test2B2A() throws Exception {
        setUp();
        player.moveDown();
        player.moveRight();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
        player.interact();
        assertEquals(3, player.getX());
        assertEquals(1, player.getY());
    }

    @Test
    public void test2A2B() throws Exception {
        setUp();
        player.moveUp();
        player.moveRight();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
        player.interact();
        assertEquals(3, player.getX());
        assertEquals(3, player.getY());
    }

    @Test
    public void testGoBack() throws Exception {
        setUp();
        player.moveUp();
        player.moveRight();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
        player.interact();
        assertEquals(3, player.getX());
        assertEquals(3, player.getY());
        player.moveLeft();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
        player.interact();
        assertEquals(3, player.getX());
        assertEquals(1, player.getY());
    }

    @Test
    public void testUnlikedPortal() throws FileNotFoundException, NoSpaceException {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
        assertThrows(TeleportException.class, () -> {
            player.interact();
        });
    }
}