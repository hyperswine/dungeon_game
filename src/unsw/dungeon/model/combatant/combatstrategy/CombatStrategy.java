package unsw.dungeon.model.combatant.combatstrategy;

public interface CombatStrategy {
    /**
     *
     * @param obj
     * @param dmg
     * @return whether the combat item's durability should be decremented
     * @throws Exception
     */
    void attack(Object obj, int dmg) throws Exception;
    int takeDamage(int currHP, int dmg);
    boolean isInvincible();
    boolean hasWeapon();

    boolean damagesWeapon();
}
