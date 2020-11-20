package unsw.dungeon.model.combatant.combatdecorator;

import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Combatant;
import unsw.dungeon.model.position.DirectionFacing;
import unsw.dungeon.model.position.PosVec;
import unsw.dungeon.model.worldobject.item.InvincibilityPotion;
import unsw.dungeon.model.worldobject.item.Item;

/**
 * TODO: Ensure that all the objects passed into these methods are either a combatant or a combat decorator.
 * @deprecated
 */
public class Invincible extends CombatDecorator{

    private int maxDamage = 9999;

    public Invincible(Combatant combatant){
        this.combatant = combatant;
    }

    @Override
    public boolean move(PosVec newPos, DirectionFacing dFacing) {

        return false;
    }

    /**
     * @param obj Combatant to be attacked. The combatant takes 9999 damage upon being attacked.
     *
     * NOTE: if combatant attacks an object, it does nothing for now. Later on, the object may 'break' instead.
     */
    @Override
    public void attack(Object obj, int dmg) throws Exception {
        if(! (obj instanceof Combatant)) return;

        Combatant combatant = (Combatant) obj;
        combatant.takeDamage(dmg);
    }

    /**
     * Player does not take any damage while in invincible mode.
     * @param dmg - HP
     *
     * NOTE: upon calling this method, the combatant should lose their invincibility decorator.
     */
    @Override
    public void takeDamage(int dmg) {
        return;
    }

    /**
     * If item is not an invincibility potion, then use it.
     */
    @Override
    public boolean useItem(Item item) {
        if(item instanceof InvincibilityPotion) return false;

        return combatant.useItem(item);
    }

    @Override
    public boolean isInvincible() {
        return true;
    }

    @Override
    public boolean hasWeapon() {
        return true;
    }

    @Override
    public void interact(Entity entity) throws Exception {
        combatant.interact(entity);
    }


}
