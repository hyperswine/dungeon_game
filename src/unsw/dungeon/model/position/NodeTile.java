package unsw.dungeon.model.position;

import java.util.Objects;

/**
 * A 'node' that represents a tile in the dungeon.
 * <p>
 * A NodeTile may have 3 to 9 'neighbors'. Each neighbor is exactly 1 'unit' of distance away.
 * </p>
 * <p>
 * NOTE: Boundaries should be referenced by 'null' NodeTiles, or
 * 'impassable' NodeTiles.
 * </p>
 */
public class NodeTile{
    private NodeTile parentTile;
    private boolean passable;
    private PosVec position;

    private double distToStart;
    private double distToEnd;
    private double totalDist;

    /**
     * A NodeTile should not contain any idea of the type of entity,
     * just whether a tile can be passed or not.
     */
    public NodeTile(PosVec position, boolean passable, NodeTile parentTile){
        this.parentTile = parentTile;
        this.passable = passable;
        this.position = position;
    }
    
    /**
     * Set the total distance based on start & end distances.
     * NOTE: be careful of the heuristics used.
     * 
     * @param distToStart - N units from this node to the start Node
     * @param distToEnd - Diagonal distance from this node to end node
     */
    public void setDistances(double distToStart, double distToEnd){
        this.distToStart = distToStart;
        this.distToEnd = distToEnd;
        totalDist = distToStart + distToEnd;
    }

    public boolean isPassable(){
        return passable;
    }

    public PosVec getPosition(){
        return position;
    }

    public NodeTile getParent(){
        return parentTile;
    }
    
    /**
     * @return The distance in units from this tile to the start tile + 'optimal' distance to the end tile.
     */
    public double getTotalDistance(){
        return totalDist;
    }

    @Override
    public boolean equals(Object o) {
        // same object reference
        if (this == o) return true;
        // if object is null or not in the same class
        if (o == null || getClass() != o.getClass()) return false;
        NodeTile nodeTile = (NodeTile) o;
        return position.equals(nodeTile.position);
    }

    public double getDistToStart() {
        return distToStart;
    }

    public double getDistToEnd() {
        return distToEnd;
    }

}