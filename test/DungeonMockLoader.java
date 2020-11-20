package test;

import org.json.JSONObject;

import unsw.dungeon.view.DungeonLoader;
import unsw.dungeon.model.combatant.Enemy;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.worldobject.item.InvincibilityPotion;
import unsw.dungeon.model.worldobject.item.Key;
import unsw.dungeon.model.worldobject.item.Sword;
import unsw.dungeon.model.worldobject.item.Treasure;
import unsw.dungeon.model.worldobject.Boulder;
import unsw.dungeon.model.worldobject.Door;
import unsw.dungeon.model.worldobject.Exit;
import unsw.dungeon.model.worldobject.FloorSwitch;
import unsw.dungeon.model.worldobject.Portal;
import unsw.dungeon.model.worldobject.Wall;

public class DungeonMockLoader extends DungeonLoader {

    public DungeonMockLoader(JSONObject json) {
        super(json);
    }


    @Override
    public void onLoad(Player player) {

    }

    @Override
    public void onLoad(Wall wall) {

    }

    @Override
    public void onLoad(Exit exit) {

    }

    @Override
    public void onLoad(Sword sword) {

    }

    @Override
    public void onLoad(InvincibilityPotion invincibilityPotion) {

    }

    @Override
    public void onLoad(Treasure treasure) {

    }

    @Override
    public void onLoad(Enemy enemy) {

    }

    @Override
    public void onLoad(Boulder boulder) {

    }

    @Override
    public void onLoad(FloorSwitch floorSwitch) {

    }

    @Override
    public void onLoad(Portal portal) {

    }

    @Override
    public void onLoad(Door door) {

    }

    @Override
    public void onLoad(Key key) {

    }

}