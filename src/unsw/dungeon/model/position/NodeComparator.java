package unsw.dungeon.model.position;

import java.util.Comparator;

/**
 * Sort by the compare method.
 */
public class NodeComparator implements Comparator<NodeTile>{

    /**
     * Compares whether o1 is of shorter 'total' length than o2.
     *
     * @return Int - (-1) if shorter, 0 if equal length, 1 if longer.
     */
    @Override
    public int compare(NodeTile o1, NodeTile o2) {
        if(o1.getTotalDistance() < o2.getTotalDistance()) return -1;
        else if(o1.getTotalDistance() > o2.getTotalDistance()) return 1;

        return 0;
    }
    
}