package unsw.dungeon.model.worldobject.item;

import unsw.dungeon.exceptions.CombatSlotFullException;
import unsw.dungeon.exceptions.InventoryFullException;
import unsw.dungeon.exceptions.NoDungeonPassException;
import unsw.dungeon.exceptions.NoInteractionException;
import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Combatant;

/**
 * General interface for an item. Viewed from the perspective of the inventory.
 *
 * Items are either 'usable' or 'key' type items. Key type items will not do
 * anything when useItem() is called. Designed for less bloat.
 *
 * More methods may be added for more complex item 'usage' or manipulation.
 */
public abstract class Item extends Entity {

    protected int itemId;

    public Item(int x, int y) {
        super(x, y);
    }

    public abstract boolean useItem(Object obj) throws NoDungeonPassException;

    /**
     * Returns the file name of this item's sprite.
     * @return
     */
    public abstract String spriteFileName();

    @Override
    public void interact(Entity entity) throws NoInteractionException, CombatSlotFullException, InventoryFullException {
        if(!(entity instanceof Combatant)) throw new NoInteractionException();

        // add item to inventory
        ((Combatant) entity).addToInventory(this);

        // remove it from the dungeon
        isInWorld.set(false);
    }

}
