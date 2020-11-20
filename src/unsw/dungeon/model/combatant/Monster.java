package unsw.dungeon.model.combatant;

import unsw.dungeon.model.Entity;
import unsw.dungeon.model.position.DirectionFacing;
import unsw.dungeon.model.position.PosVec;

public class Monster extends Enemy {

    public Monster(int hp, int x, int y) {
        super(hp, x, y);
    }

    /**
     * Move twice
     */
    @Override
    public void moveNext() {
        for(int i=0; i<2; i++) super.moveNext();
    }

}