package unsw.dungeon.model.worldobject.item;
import java.util.Comparator;

public class ItemComparator implements Comparator<Item>{

    @Override
    public int compare(Item o1, Item o2) {
        if(o1.itemId < o2.itemId) return -1;
        else if(o1.itemId > o2.itemId) return 1;
        else return 0;
    }
    
}
