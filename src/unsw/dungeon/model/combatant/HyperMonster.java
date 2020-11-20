package unsw.dungeon.model.combatant;

public class HyperMonster extends Enemy{

    public HyperMonster(int hp, int x, int y) {
        super(hp, x, y);
    }

    @Override
    public void moveNext() {
        for(int i=0; i<5; i++) super.moveNext();
    }
    
}