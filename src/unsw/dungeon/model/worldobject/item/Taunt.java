package unsw.dungeon.model.worldobject.item;

import unsw.dungeon.model.combatant.Combatant;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.view.AudioPlayer;

public class Taunt extends Item {

    public Taunt(int x, int y) {
        super(x, y);
        itemId = 8;
    }

    /**
     * Taunt an enemy. This allows them move again. Keep spamming this item
     * and the enemy will eventually get to your position.
     */
    @Override
    public boolean useItem(Object obj) {
        //AudioPlayer.playSound("taunt");
        if(!(obj instanceof Combatant)) return false;
        AudioPlayer.playSound("TAUNT");
        Combatant combatant = (Combatant) obj;

        //if(!combatant.hasDungeonPass()) return false;

        dungeonProbe.updateAllEnemies();

        // TODO: make the player make some kind of emote, e.g. a taunt emote
        // e.g. call player.Emote(taunt) that throws an exception that makes a taunt sound "come get me if you can"
        if(obj instanceof Player){
            Player player = (Player) obj;
            player.emote("taunt");
        }

        return true;
    }

    @Override
    public String spriteFileName() {
        return "taunt.png";
    }
    
}