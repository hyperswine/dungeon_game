package unsw.dungeon.model.worldobject.item;

import javafx.beans.property.SimpleBooleanProperty;
import unsw.dungeon.model.combatant.Combatant;

public class InvincibilityPotion extends Item {

    public InvincibilityPotion(int x, int y) {
        super(x, y);
        isInWorld = new SimpleBooleanProperty();
        isInWorld.set(true);
        isPassable = false;
        itemId = 4;
    }

    /**
     * Use the invincibility potion.
     */
    @Override
    public boolean useItem(Object obj) {
        if (obj instanceof Combatant) {
            ((Combatant)obj).removeFromInventory(this);
            ((Combatant)obj).setInvincible();
            return true;
        }
        else
            return false;
    }

    @Override
    public String spriteFileName() {
        return "brilliant_blue_new.png";
    }


}
