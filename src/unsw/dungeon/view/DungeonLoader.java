package unsw.dungeon.view;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import unsw.dungeon.controller.GoalController;
import unsw.dungeon.model.Dungeon;
import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Enemy;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.worldobject.item.*;
import unsw.dungeon.model.worldobject.*;
import unsw.dungeon.prober.DungeonProbe;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Loads a 'dungeon' model by inspecting a .json file.
 * Loads all the entities into the dungeon backend. Nothing to do with the frontend.
 * 
 * <p>
 * "By extending this class, a subclass can <b>hook</b> into entity creation. This is
 * useful for creating UI elements with corresponding entities."
 * </p>
 */
public abstract class DungeonLoader {

    private JSONObject json;
    // accessed by dungeon loader controller
    protected DungeonProbe dungeonProbe;

    public DungeonLoader(String filename) throws FileNotFoundException {
        json = new JSONObject(new JSONTokener(new FileReader(filename)));
    }

    public DungeonLoader(JSONObject json) {
        this.json = json;
    }

    /**
     * Parses the JSON to create a dungeon.
     * 
     * @return The corresponding Dungeon Object
     */
    public Dungeon load() {
        // get dungeon dimensions
        int width = json.getInt("width");
        int height = json.getInt("height");

        Dungeon dungeon = new Dungeon(width, height);
        dungeonProbe = new DungeonProbe(dungeon);

        // get entities
        JSONArray jsonEntities = json.getJSONArray("entities");

        for (int i = 0; i < jsonEntities.length(); i++) {
            loadEntity(dungeon, jsonEntities.getJSONObject(i));
        }
        dungeon.finaliseEntities();
        return dungeon;
    }

    public GoalController loadGoals(){
        // get goals
        JSONObject goals = json.getJSONObject("goal-condition");
        return new GoalController(goals, dungeonProbe);
    }

    private void loadEntity(Dungeon dungeon, JSONObject json) {
        String type = json.getString("type");
        int x = json.getInt("x");
        int y = json.getInt("y");

        Entity entity = null;

        switch (type) {
            case "player":
                Player player = new Player(x, y);
                dungeon.setPlayer(player);
                onLoad(player);
                entity = player;
                break;
            case "wall":
                Wall wall = new Wall(x, y);
                onLoad(wall);
                entity = wall;
                break;
            case "exit":
                Exit exit = new Exit(x, y);
                onLoad(exit);
                entity = exit;
                break;
            case "sword":
                Sword sword = new Sword(x, y);
                onLoad(sword);
                entity = sword;
                break;
            case "invincibility":
                InvincibilityPotion invincibilityPotion = new InvincibilityPotion(x, y);
                onLoad(invincibilityPotion);
                entity = invincibilityPotion;
                break;
            case "enemy":
                Enemy enemy = new Enemy(2, x, y);
                onLoad(enemy);
                entity = enemy;
                break;
            case "treasure":
                Treasure treasure = new Treasure(x,y);
                onLoad(treasure);
                entity = treasure;
                break;
            case "boulder":
                Boulder boulder = new Boulder(x, y);
                onLoad(boulder);
                entity = boulder;
                break;
            case "switch":
                FloorSwitch floorSwitch = new FloorSwitch(x, y);
                onLoad(floorSwitch);
                entity = floorSwitch;
                break;
            case "portal":
                int id = json.getInt("id");
                Portal portal = new Portal(x, y, id);
                onLoad(portal);
                entity = portal;
                break;
            case "door":
                id = json.getInt("id");
                Door door = new Door(x, y, id);
                onLoad(door);
                entity = door;
                break;
            case "key":
                id = json.getInt("id");
                Key key = new Key(x, y, id);
                onLoad(key);
                entity = key;
                break;
            case "hyper_scroll":
                HyperScroll hyperScroll = new HyperScroll(x,y);
                onLoad(hyperScroll);
                entity = hyperScroll;
                break;
            case "taunt":
                Taunt taunt = new Taunt(x,y);
                onLoad(taunt);
                entity = taunt;
                break;
            case "nuke":
                Nuke nuke = new Nuke(x,y);
                onLoad(nuke);
                entity = nuke;
                break;
            case "hyper_aura":
                HyperAura hyperAura = new HyperAura(x,y);
                onLoad(hyperAura);
                entity = hyperAura;
                break;
            case "dungeon_pass":
                DungeonPass dungeonPass = new DungeonPass(x,y);
                onLoad(dungeonPass);
                entity = dungeonPass;
                break;
            default:
                entity = new Sword(x,y); //NOTE: TEMPORARY for MS2
        }
        dungeon.addEntity(entity, dungeonProbe);
    }

    public abstract void onLoad(Player player);

    public abstract void onLoad(Wall wall);

    public abstract void onLoad(Exit exit);

    public abstract void onLoad(Sword sword);

    public abstract void onLoad(InvincibilityPotion invincibilityPotion);

    public abstract void onLoad(Treasure treasure);

    public abstract void onLoad(Enemy enemy);

    public abstract void onLoad(Boulder boulder);

    public abstract void onLoad(FloorSwitch floorSwitch);

    public abstract void onLoad(Portal portal);

    public abstract void onLoad(Door door);

    public abstract void onLoad(Key key);

    public abstract void onLoad(HyperScroll hyperScroll);

    public abstract void onLoad(Taunt taunt);

    public abstract void onLoad(Nuke nuke);

    public abstract void onLoad(HyperAura hyperAura);

    public abstract void onLoad(DungeonPass dungeonPass);

    // Create additional abstract methods for any additional/new entities

}
