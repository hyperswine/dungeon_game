package unsw.dungeon.model.combatant.combatdecorator;

import unsw.dungeon.model.combatant.Combatant;
import unsw.dungeon.model.position.DirectionFacing;
import unsw.dungeon.model.position.PosVec;
import unsw.dungeon.model.worldobject.item.Item;
/**
 * @deprecated
 */
public abstract class CombatDecorator extends Combatant{
    protected Combatant combatant;

    /**
     * To not lose track of the combatant's position.
     */
    @Override
    public PosVec getPosition() {
        return combatant.getPosition();
    }

    public abstract boolean move(PosVec newPos, DirectionFacing dFacing);
    public abstract void attack(Object obj, int dmg) throws Exception;
    public abstract void takeDamage(int dmg);
    public abstract boolean useItem(Item item);
    public abstract boolean isInvincible();
    public abstract boolean hasWeapon();
}