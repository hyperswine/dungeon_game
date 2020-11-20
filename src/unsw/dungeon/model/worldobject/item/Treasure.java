package unsw.dungeon.model.worldobject.item;

import javafx.beans.property.SimpleBooleanProperty;
import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Combatant;
import unsw.dungeon.view.AudioPlayer;

public class Treasure extends Item {

    public Treasure(int x, int y){
        super(x, y);
        this.isInWorld = new SimpleBooleanProperty();
        isInWorld.set(true);
        itemId = 3;
    }

    /**
     * Does nothing when used.
     */
    @Override
    public boolean useItem(java.lang.Object obj) {
        return false;
    }

    @Override
    public void interact(Entity entity) {
        if(!(entity instanceof Combatant)) return;
        AudioPlayer.playSound("TREASURE_COLLECT");
        ((Combatant) entity).incrementTreasureCount();
        isInWorld.set(false);
    }

    @Override
    public String spriteFileName() {
        return "gold_pile.png";
    }

}
