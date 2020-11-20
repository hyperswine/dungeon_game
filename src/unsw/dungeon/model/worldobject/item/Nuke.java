package unsw.dungeon.model.worldobject.item;

import unsw.dungeon.model.combatant.Combatant;
import unsw.dungeon.view.AudioPlayer;

/**
 * Destroy every enemy in a 1x1 radius
 */
public class Nuke extends Item{

    public Nuke(int x, int y) {
        super(x, y);
        itemId = 2;
    }

    @Override
    public boolean useItem(Object obj) {
        if (!(obj instanceof Combatant))
            return false;
        AudioPlayer.playSound("BOMB");
        Combatant combatant = (Combatant) obj;

        //if(!combatant.hasDungeonPass()) return false;
        combatant.removeFromInventory(this);
        dungeonProbe.nukeSurroundings(combatant.getX(), combatant.getY(), 1);

        return true;
    }

    @Override
    public String spriteFileName() {
        return "nuke.png";
    }
    
}