package unsw.dungeon.view;

import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.worldobject.Door;
import unsw.dungeon.model.worldobject.Exit;
import unsw.dungeon.model.worldobject.Portal;
import unsw.dungeon.model.worldobject.Wall;

import java.util.HashMap;

/**
 * Easy hashable interface for entities
 */
public class HashEntity {
    public HashMap<String, Class<? extends Entity>> hashEntity = new HashMap<>();

    /**
     * Initialize the hashmap
     */
    public HashEntity(){
        hashEntity.put("wall", Wall.class);
        hashEntity.put("player", Player.class);
        hashEntity.put("exit", Exit.class);
        hashEntity.put("portal", Portal.class);
        hashEntity.put("door", Door.class);
        //hashEntity.put("key", Key.class);
        
    }

}