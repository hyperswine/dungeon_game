package unsw.dungeon.model.worldobject.item;

import unsw.dungeon.exceptions.NoDungeonPassException;
import unsw.dungeon.model.combatant.Combatant;

/**
 * Part of the Hyper(tm) package that new players get access to via in-game micro-transactions(tm)(c)(r)
 * <p>
 * Want an aura that never fades and immediately extinguishes anything next to you (including walls, items)?
 */
public class HyperAura extends Item {

    public HyperAura(int x, int y) {
        super(x, y);
        itemId = 6;
    }

    @Override
    public boolean useItem(Object obj) throws NoDungeonPassException {
        if(!(obj instanceof Combatant)) return false;
        
        Combatant combatant = (Combatant) obj;

        if(!combatant.hasDungeonPass()) throw new NoDungeonPassException();
        if(!combatant.isHyperd()) combatant.setHyperAura();
        else combatant.endInvincibility();

        return true;
    }

    @Override
    public String spriteFileName() {
        return "hyper_aura.png";
    }
}
