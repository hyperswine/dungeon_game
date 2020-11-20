package unsw.dungeon.model.position;

import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.worldobject.Portal;
import unsw.dungeon.prober.DungeonProbe;

import java.util.*;

public class Trajectory {
    // passable tiles
    protected PriorityQueue<NodeTile> openTiles;
    // impassable tiles
    protected PriorityQueue<NodeTile> closedTiles;
    protected List<PosVec> trajectory;

    protected PosVec start;
    protected PosVec end;

    protected DungeonProbe dungeonProbe;

    /**
     * Create a new trajectory
     *
     * @param currPos Current position of entity
     * @param endPos  Desired position for entity
     */
    public Trajectory(PosVec currPos, PosVec endPos, DungeonProbe dProbe) {
        start = currPos;
        end = endPos;
        dungeonProbe = dProbe;
    }

    public void setDungeonProbe(DungeonProbe dungeonProbe) {
        this.dungeonProbe = dungeonProbe;
    }

    /**
     * @return List of PosVec which entity must move to
     * step-by-step to reach desired destination.
     * <p>
     * If returned value is null, then the entity
     * is already in its desired position.
     * <p>
     * CREDIT: Wikipedia for the general pseudocode on A* search.
     */
    public Iterator<PosVec> calculateTrajectory() {

        // initialize arrays
        openTiles = new PriorityQueue<>(new NodeComparator());
        closedTiles = new PriorityQueue<>(new NodeComparator());
        trajectory = new ArrayList<>();

        NodeTile startNode = new NodeTile(start, true, null);
        startNode.setDistances(0, start.diagonalDistance(end));

        openTiles.add(startNode);

        // main loop to find 'best' path
        while (openTiles.size() > 0) {

            // must be non-null since size > 0
            NodeTile currTile = openTiles.poll();
            closedTiles.add(currTile);

            // if reach the tile we're looking for, reconstruct path
            if (currTile.getPosition().equals(end)) {
                while (!currTile.equals(startNode)) {
                    trajectory.add(currTile.getPosition());
                    currTile = currTile.getParent();
                }
                // reverse list
                Collections.reverse(trajectory);
                return trajectory.iterator();
            }

            // get neighbors
            Iterator<NodeTile> neighbors = getPassableNeighbors(currTile);

            while (neighbors.hasNext()) {

                NodeTile neighbor = neighbors.next();
                if (closedTiles.contains(neighbor)) continue;

                // consider the diagonal distance as the heuristic
                PosVec neighborPos = neighbor.getPosition();
                PosVec currPos = currTile.getPosition();

                double tentativeGScore = currTile.getDistToStart() + currPos.diagonalDistance(neighborPos);
                // set the distances
                neighbor.setDistances(tentativeGScore, neighborPos.diagonalDistance(end));

                // add to list if not in already
                if(!openTiles.contains(neighbor))
                    // add neighbor to binary heap (priority queue)
                    openTiles.add(neighbor);
            }

        }

        return null;
    }


    /**
     * Given a tile, find all its neighbors. Looks at the entities at each
     * position and determins whether the tile is passable.
     *
     * @return Iterator containing passable neighbors
     */
    public Iterator<NodeTile> getPassableNeighbors(NodeTile currTile) {
        List<NodeTile> passableNeighbors = new ArrayList<>();

        int height = dungeonProbe.getHeight();
        int width = dungeonProbe.getWidth();

        PosVec position = currTile.getPosition();
        // get a position vector of all the neighbors
        List<PosVec> neighboringPosVec = position.getNeighbors(width, height);

        for(PosVec n: neighboringPosVec){
            Entity e = dungeonProbe.getEntityAtCoords(n.getX(), n.getY());
            if(e==null){
                // also adds parent tile.
                passableNeighbors.add(new NodeTile(n, true, currTile));
                continue;
            } 
    
            // if entity is a portal -> map position to other exit
            if(e instanceof Portal){
                Portal p = (Portal) e;
                Portal pExit = p.getPortalExit();
                n = pExit.getPosition();
            }

            // NOTE: update 'Player' to combatant
            if(e instanceof Portal || e instanceof Player || e.isPassable()) passableNeighbors.add(new NodeTile(n, true, currTile));
        }

        return passableNeighbors.iterator();
    }

}