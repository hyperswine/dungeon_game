package unsw.dungeon.model.worldobject.item;

/**
 * Tired of losing the game so much? With the DungeonPass(tm) you now have access to very powerful equipment, known as 'Hyper Items'. Sourced directly from the centre of the Big Bang itself 13 billion years ago, the hyper item contains HyperSpace Fusion(tm)(c) energy available only to celestial beings. Having this pass will make you, the player, a celestial being.
 * Get yourself a dungeon pass. Available on select maps only.
 */
public class DungeonPass extends Item{

    public DungeonPass(int x, int y) {
        super(x, y);
        itemId = 5;
    }

    /**
     * Does nothing.
     */
    @Override
    public boolean useItem(Object obj) {
        return false;
    }

    @Override
    public String spriteFileName() {
        return "dungeon_pass.png";
    }
    
}