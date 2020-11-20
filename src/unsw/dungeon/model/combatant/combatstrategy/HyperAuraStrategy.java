package unsw.dungeon.model.combatant.combatstrategy;

import unsw.dungeon.model.Entity;
import unsw.dungeon.exceptions.NoAttackTargetException;
import unsw.dungeon.model.combatant.Combatant;

/**
 * The hyper aura is exactly what it sounds like.
 */
public class HyperAuraStrategy implements CombatStrategy {

    /**
     * Destroy anything and absolutely anything.
     */
    @Override
    public void attack(Object obj, int dmg) throws Exception {
        if (!(obj instanceof Combatant)){
            Entity e = (Entity) obj;
            e.setIsInWorld(false);
        }
        Combatant combatant = (Combatant) obj;
        combatant.takeDamage(dmg);
    }

    /**
     * A combatant with a hyper strategy by default, cannot take damage.
     */
    @Override
    public int takeDamage(int currHP, int dmg) {
        return currHP;
    }

    @Override
    public boolean isInvincible() {
        return false;
    }

    /**
     * The aura is the combatant's greatest weapon.
     */
    @Override
    public boolean hasWeapon() {
        return true;
    }

    @Override
    public boolean damagesWeapon() {
        return false;
    }

}