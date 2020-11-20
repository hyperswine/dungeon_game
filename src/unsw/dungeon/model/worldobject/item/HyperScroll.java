package unsw.dungeon.model.worldobject.item;

import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Combatant;
import unsw.dungeon.model.position.PosVec;
import unsw.dungeon.view.AudioPlayer;

import java.util.Random;

/**
 * Hyper Scrolls allow the combatant to teleport to any passable tile in the
 * dungeon, within (dx, dy) units.
 */
public class HyperScroll extends Item {

    /**
     * Create an entity positioned in square (x,y)
     */
    public HyperScroll(int x, int y) {
        super(x, y);
        itemId = 7;
    }

    @Override
    public boolean useItem(Object obj) {
        AudioPlayer.playSound("TELEPORT");
        if (!(obj instanceof Combatant))
            return false;

        Combatant combatant = (Combatant) obj;


        Random rand = new Random();
        PosVec curPos = combatant.getPosition();
        int x = curPos.getX();
        int y = curPos.getY();
        int a = x;
        int b = y;

        // do for 100 cycles; if cant find a random tile then stay in current position
        boolean flag = false;
        for (int i = 0; i < 100; i++) {
            a = rand.nextInt(dungeonProbe.getWidth());
            b = rand.nextInt(dungeonProbe.getHeight());
            Entity dest = dungeonProbe.getEntityAtCoords(a, b);
            if ((dest == null || dungeonProbe.getEntityAtCoords(a, b).isPassable()) && (a != x || b != y)) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            a = x;
            b = y;
        }


        try {
            combatant.setPos(a,b);
        } catch (Exception e) {
        }
        ((Combatant)obj).removeFromInventory(this);
        return true;
    }

    @Override
    public String spriteFileName() {
        return "hyper_scroll.png";
    }
}
