package test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import unsw.dungeon.exceptions.NoInteractionException;
import unsw.dungeon.exceptions.NoSpaceException;
import unsw.dungeon.model.Dungeon;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.worldobject.item.InvincibilityPotion;
import unsw.dungeon.prober.DungeonProbe;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class MiscInvalidInteractionsTest {
    private Dungeon dungeon;
    private DungeonProbe dP;
    private Player player;


    public void setUp() throws FileNotFoundException {
        JSONObject json = new JSONObject();

        json.put("width", Integer.toString(3));
        json.put("height", Integer.toString(3));

        // TODO: put logic for creating entities in separate method
        JSONArray jsonArr = new JSONArray();
        JSONObject entityX = new JSONObject();
        entityX.put("x", 1);
        entityX.put("y", 1);
        entityX.put("type", "player");

        jsonArr.put(entityX);


        JSONObject goal = new JSONObject();
        json.put("goal-condition", goal);

        json.put("entities", jsonArr);


        DungeonMockLoader dMC = new DungeonMockLoader(json);

        dungeon = dMC.load();
        player = (Player) dungeon.getEntityAtCoords(1,1);
//        dungeon = new Dungeon(3,3);
//        player = new Player(1,1);
//        dP = new DungeonProbe(dungeon);
//        dungeon.addEntity(player,dP);
//        dungeon.finaliseEntities();
    }

    @Test
    public void testInteractEmptySpace() throws Exception {
        setUp();
        assertThrows(NoInteractionException.class, () -> {
            player.interact();
        });
    }

    @Test
    public void testInteractOutsideMap() throws Exception{
        setUp();
        player.moveRight();
        assertThrows(NoInteractionException.class, () -> {
            player.interact();
        });
        player.moveLeft();
        player.moveLeft();
        assertThrows(NoInteractionException.class, () -> {
            player.interact();
        });
        player.moveDown();
        assertThrows(NoInteractionException.class, () -> {
            player.interact();
        });

        player.moveUp();
        player.moveUp();
        assertThrows(NoInteractionException.class, () -> {
            player.interact();
        });
    }

    @Test
    public void testMoveOutsideMap() throws Exception {
        setUp();
        player.moveRight();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
        player.moveLeft();
        player.moveLeft();
        assertThrows(NoSpaceException.class, () -> {
            player.moveLeft();
        });
        player.moveDown();
        assertThrows(NoSpaceException.class, () -> {
            player.moveDown();
        });

        player.moveUp();
        player.moveUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
    }

    @Test
    public void testUseItemNotInInventory() throws Exception {
        setUp();
        InvincibilityPotion randomItem = new InvincibilityPotion(1,2);
        assertFalse(player.useItem(randomItem));
    }

}
