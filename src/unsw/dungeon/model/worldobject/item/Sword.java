package unsw.dungeon.model.worldobject.item;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.dungeon.exceptions.CombatSlotFullException;
import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Combatant;

public class Sword extends Item {

    private IntegerProperty hitsRemaining;

    public Sword(int x, int y) {
        super(x, y);
        isInWorld = new SimpleBooleanProperty();
        isInWorld.set(true);
        this.hitsRemaining = new SimpleIntegerProperty();
        this.hitsRemaining.set(5);
    }

    /**
     * 'Clicking' or attempting to 'use' this item should result in
     * nothing happening.
     * <p>
     * SUGGESTION: Perhaps make the weapon icon shimmer when player attempts to use it?
     */
    @Override
    public boolean useItem(java.lang.Object obj) {
        return false;
    }

    @Override
    public void interact(Entity entity) throws CombatSlotFullException {
        // swords lying around should only be picked up by combatants
        if (!(entity instanceof Combatant)) return;

        Combatant combatant = (Combatant) entity;

        if (combatant.hasCombatItem()) {
            throw new CombatSlotFullException();
        } else {
            combatant.setCombatItem(this);
            isInWorld.set(false);
        }
    }

    public void decrementHitsRemaining() {
        this.hitsRemaining.set(hitsRemaining.get() - 1);
    }

    public IntegerProperty hitsRemaining() {
        return hitsRemaining;
    }

    @Override
    public String spriteFileName() {
        return "greatsword_1_new.png";
    }
}
