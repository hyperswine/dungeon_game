package test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import unsw.dungeon.exceptions.CombatSlotFullException;
import unsw.dungeon.exceptions.NoAttackTargetException;
import unsw.dungeon.exceptions.NoSpaceException;
import unsw.dungeon.exceptions.NoWeaponException;
import unsw.dungeon.model.Dungeon;
import unsw.dungeon.model.combatant.Enemy;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.worldobject.item.InvincibilityPotion;
import unsw.dungeon.model.worldobject.item.Sword;
import unsw.dungeon.prober.DungeonProbe;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class CombatTest {

    private Dungeon dungeon;
    private Player player;
    private Enemy enemy;
    private Sword sword;
    private InvincibilityPotion invincibilityPotion;
    private DungeonProbe dP;
    private int maxHP = 5;

    public void setUp() throws FileNotFoundException {
        JSONObject json = new JSONObject();

        json.put("width", Integer.toString(5));
        json.put("height", Integer.toString(5));

        JSONArray jsonArr = new JSONArray();
        JSONObject entPlayer = new JSONObject();
        entPlayer.put("x", 1);
        entPlayer.put("y", 1);
        entPlayer.put("type", "player");

        org.json.JSONObject entEnemy = new JSONObject();
        entEnemy.put("type", "enemy");
        entEnemy.put("x", 2);
        entEnemy.put("y", 1);

        JSONObject entSword = new JSONObject();
        entSword.put("type", "sword");
        entSword.put("x", 0);
        entSword.put("y", 1);

        JSONObject entPotion = new JSONObject();
        entPotion.put("type", "invincibility");
        entPotion.put("x", 1);
        entPotion.put("y", 0);


        jsonArr.put(entPlayer);
        jsonArr.put(entEnemy);
        jsonArr.put(entPotion);
        jsonArr.put(entSword);

        JSONObject goal = new JSONObject();
        json.put("goal-condition", goal);

        json.put("entities", jsonArr);


        DungeonMockLoader dMC = new DungeonMockLoader(json);
        dungeon = dMC.load();
        player = (Player) dungeon.getEntityAtCoords(1,1);
        enemy = (Enemy) dungeon.getEntityAtCoords(2,1);
        invincibilityPotion = (InvincibilityPotion) dungeon.getEntityAtCoords(1,0);
        sword = (Sword) dungeon.getEntityAtCoords(0,1);
//        dungeon = new Dungeon(5,5);
//        player = new Player(1,1);
//        enemy = new Enemy(2,2,1);
//        sword = new Sword(0,1);
//        invincibilityPotion = new InvincibilityPotion(1,0);
//        dP = new DungeonProbe(dungeon);
//        dungeon.addEntity(player, dP);
//        dungeon.addEntity(enemy, dP);
//        dungeon.addEntity(sword, dP);
//        dungeon.addEntity(invincibilityPotion, dP);
//        dungeon.finaliseEntities();
    }

    /**
     * Test general combat functionality for the players and enemies
     */
    @Test
    public void testBasicCombat() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
        assertThrows(NoWeaponException.class, () -> {
            player.attack(null);
        });
        assertThrows(NoSpaceException.class, () -> {
            player.moveLeft();
        });
        player.interact();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
        player.attack(null);
        assertTrue(enemy.getHP()==0);
        player.takeDamage(9999);
        assertTrue(player.getHP() <= 0);
    }

    @Test
    public void testNoWeapon() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
        assertThrows(NoWeaponException.class, () -> {
            player.attack(null);
        });
        player.takeDamage(9999);
        assertTrue(player.getHP() <= 0);
    }

    @Test
    public void testNoDurability() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveLeft();
        });
        player.interact();
        assertThrows(NoAttackTargetException.class, () -> {
            player.attack(null);
        });
        assertThrows(NoAttackTargetException.class, () -> {
            player.attack(null);
        });
        assertThrows(NoAttackTargetException.class, () -> {
            player.attack(null);
        });
        assertThrows(NoAttackTargetException.class, () -> {
            player.attack(null);
        });
        assertThrows(NoAttackTargetException.class, () -> {
            player.attack(null);
        });
        assertThrows(NoSpaceException.class, () -> {
            player.moveRight();
        });
        assertThrows(NoWeaponException.class, () -> {
            player.attack(null);
        });
    }

    @Test
    public void testInvincibleRight() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
        player.interact();
        player.moveRight();
        assert enemy.getHP() <= 0;
        player.takeDamage(9999);
        assertFalse(player.getHP() <= 0);

    }

    @Test
    public void testInvincibleDown() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
        player.interact();
        player.moveUp();
        player.moveRight();
        player.moveDown();
        assert enemy.getHP() <= 0;
    }

    @Test
    public void testInvincibleUp() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
        player.interact();
        player.moveDown();
        player.moveRight();
        player.moveUp();
        assert enemy.getHP() <= 0;
    }

    @Test
    public void testInvincibleLeft() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
        player.interact();
        player.moveDown();
        player.moveRight();
        player.moveRight();
        player.moveUp();
        player.moveLeft();
        assert enemy.getHP() <= 0;
    }

    @Test
    public void testInvincibleTimeout() throws Exception {
        setUp();
        assertThrows(NoSpaceException.class, () -> {
            player.moveUp();
        });
        player.interact();
        Thread.sleep(6000);
        assertFalse(player.isInvincible());
    }

    @Test
    public void testAlreadyHasWeapon() throws Exception {
        setUp();
        Sword s2 = new Sword(1,2);
        dungeon.addEntity(s2,dP);
        // Pick up first weapon
        assertThrows(NoSpaceException.class, () -> {
            player.moveLeft();
        });
        player.interact();
        //Try to pick up other one
        assertThrows(NoSpaceException.class, () -> {
            player.moveDown();
        });
        assertThrows(CombatSlotFullException.class, () -> {
            player.interact();
        });
    }

    @Test
    public void testSeek(){
        
    }

    /**
     * Will not be useful for now.
     */
    @Test
    public void testCombatBehavior(){
        
    }

    @Test
    public void testInventoryManagement(){

    }

    @Test
    public void testInventoryProperties(){

    }


}