package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import unsw.dungeon.model.Dungeon;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.position.NodeTile;
import unsw.dungeon.model.position.PosVec;
import unsw.dungeon.model.position.Trajectory;

public class TrajectoryTest {
    JSONInstances jsonInstances;
    Player player;
    Dungeon dungeon;
    DungeonMockLoader dMC;


    /**
     * Set up a trajectory object.
     * @return A basic trajectory object.
     */
    public Trajectory setUpTrajectory(int x1, int y1, int x2, int y2){
        jsonInstances = new JSONInstances();
        dMC = jsonInstances.createBasicController(100, 150);
        dungeon = dMC.load();
        player = dungeon.getPlayer();
        
        PosVec start = new PosVec(x1,y1);
        PosVec end = new PosVec(x2,y2);

        Trajectory trj = new Trajectory(start, end, null);

        return trj;
    }

    /**
     * Basic tests for trajectory.
     */
    @Test
    public void testTrajectoryBasic(){
        
        // note trajectory currently used only as static object.
        Trajectory trj = setUpTrajectory(0, 0, 100, 100);

        trj.setDungeonProbe(player.getDungeonProbe());

        PosVec start = new PosVec(2,2);
        PosVec end = new PosVec(100,150);

        NodeTile nT = new NodeTile(start, true, null);
        NodeTile nT2 = new NodeTile(end, true, null);

        Iterator<NodeTile> nTNeighbors = trj.getPassableNeighbors(nT);
        Iterator<NodeTile> nT2Neighbors = trj.getPassableNeighbors(nT2);

        while(nTNeighbors.hasNext()){
            NodeTile neighbor = nTNeighbors.next();
            jsonInstances.oneUnitApart(neighbor.getPosition(), nT.getPosition());
        }

        while(nT2Neighbors.hasNext()){
            NodeTile neighbor = nT2Neighbors.next();
            jsonInstances.oneUnitApart(neighbor.getPosition(), nT.getPosition());
        }

    }

    /**
     * Ensure portals are recognized & mapped appropriately.
     */
    @Test
    public void testTrajectoryPortal(){
        Trajectory trj = setUpTrajectory(3, 4, 9, 9);
        trj.setDungeonProbe(player.getDungeonProbe());

        PosVec start = new PosVec(4,4);
        PosVec end = new PosVec(100,100);

        NodeTile nT = new NodeTile(start, true, null);
        NodeTile nT2 = new NodeTile(end, true, null);

        Iterator<NodeTile> nTNeighbors = trj.getPassableNeighbors(nT);
        Iterator<NodeTile> nT2Neighbors = trj.getPassableNeighbors(nT2);

        boolean flag = false;
        while(nTNeighbors.hasNext()){
            NodeTile n = nTNeighbors.next();
            // (5,3) should be mapped to (99, 100)
            assertTrue(!n.getPosition().equals(new PosVec(5, 3)));
            if(n.getPosition().equals(new PosVec(99, 100))){
                flag = true;
            }
        }

        assertTrue(flag);

        flag = false;
        while(nT2Neighbors.hasNext()){
            NodeTile n = nT2Neighbors.next();
            // (99, 100) should be mapped to (5,3)
            assertTrue(!n.getPosition().equals(new PosVec(99, 100)));
            if(n.getPosition().equals(new PosVec(5, 3))){
                flag = true;
            }
        }

        assertTrue(flag);

    }

    /**
     * Test rigorous path finding. Expecting different sorts of movements.
     * NOTE: O(|E|) worst case, but usually much bit faster.
     */
    @Test
    public void testPathFinding(){
        // set up a real trajectory from the player to the exit.
        // NOTE: players will not move diagonally in MS2.
        Trajectory trj = setUpTrajectory(1, 1, 99, 99);
        trj.setDungeonProbe(player.getDungeonProbe());

        Iterator<PosVec> trajectory = trj.calculateTrajectory();
        PosVec currP;
        PosVec prevP = null;

        while(trajectory.hasNext()){
            currP = trajectory.next();
            if(prevP == null){
                prevP = currP;
                continue;
            }
            assertTrue(currP.diagonalDistance(prevP)==1);
            prevP = currP;
        }
        // NOTE: will test for incorrect coords/ out of bounds.
    }
}