package unsw.dungeon.model.combatant.combatstrategy;

import unsw.dungeon.exceptions.NoWeaponException;

public class NoWeaponStrategy implements CombatStrategy {

    @Override
    public void attack(Object obj, int dmg) throws Exception {
        throw new NoWeaponException();
    }

    @Override
    public int takeDamage(int currHP, int dmg) {
        return currHP - dmg;
    }

    @Override
    public boolean isInvincible() {
        return false;
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
