package unsw.dungeon.model.worldobject.item;

import unsw.dungeon.model.combatant.Player;

/**
 * Feed this object into the door.
 */
public class Key extends Item {

    private int id;

    public Key(int x, int y, int id){
        super(x, y);
        isInWorld.set(true);
        this.id = id;
        itemId = 1;
    }

    /**
     * This method should be called when an entity interacts with 'Door'.
     * This method can also be called by the player if they use in the inventory.
     *
     * @param obj - Integer containing the ID of the door OR Player using the item.
     * NOTE: This item should not be directly used from the inventory.
     */
    @Override
    public boolean useItem(Object obj) {
        if(obj instanceof Integer) {
            Integer id = (Integer) obj;

            return (id.equals(this.id));
        } else if (obj.getClass().equals(Player.class)) {
            Player player = (Player) obj;
            try {
                player.interactWithEntityFacing();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        return false;

    }

    @Override
    public String spriteFileName() {
        return "key.png";
    }
}
