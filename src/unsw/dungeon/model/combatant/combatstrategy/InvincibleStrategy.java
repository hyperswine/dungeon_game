package unsw.dungeon.model.combatant.combatstrategy;

import unsw.dungeon.exceptions.NoAttackTargetException;
import unsw.dungeon.model.combatant.Combatant;

public class InvincibleStrategy implements CombatStrategy {
    @Override
    public void attack(Object obj, int dmg) throws Exception {
        if (!(obj instanceof Combatant)) throw new NoAttackTargetException();
        Combatant combatant = (Combatant) obj;
        combatant.takeDamage(dmg);
    }

    @Override
    public int takeDamage(int currHP, int dmg) {
        return currHP;
    }

    @Override
    public boolean isInvincible() {
        return true;
    }

    @Override
    public boolean hasWeapon() {
        return false;
    }

    @Override
    public boolean damagesWeapon() {
        return false;
    }
}
