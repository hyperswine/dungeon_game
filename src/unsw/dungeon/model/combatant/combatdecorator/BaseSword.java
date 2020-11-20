package unsw.dungeon.model.combatant.combatdecorator;

import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Combatant;
import unsw.dungeon.model.position.DirectionFacing;
import unsw.dungeon.model.position.PosVec;
import unsw.dungeon.model.worldobject.item.Item;

/**
 * @deprecated
 */
public class BaseSword extends CombatDecorator {

    public BaseSword(Combatant combatant){
        this.combatant = combatant;
    }

    @Override
    public boolean move(PosVec newPos, DirectionFacing dFacing) {
        return false;
    }

    /**
     * Allow the player to deal damage more to an enemy.
     * Base sword does 10 extra dmg.
     */
    @Override
    public void attack(Object obj, int dmg) throws Exception {
        combatant.attack(obj, 10);
    }

    @Override
    public void takeDamage(int dmg) {
        combatant.takeDamage(dmg);
    }


    @Override
    public boolean useItem(Item item) {
        return combatant.useItem(item);
    }

    @Override
    public void interact(Entity entity) throws Exception {
        combatant.interact(entity);
    }

    @Override
    public boolean isInvincible() {
        return false;
    }

    @Override
    public boolean hasWeapon() {
        return true;
    }
}