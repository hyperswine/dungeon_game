package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import unsw.dungeon.model.position.NodeTile;
import unsw.dungeon.model.position.PosVec;

/**
 * Test node tiles only
 */
public class NodeTileTest {
    /**
     * Initialize a basic tile.
     */
    public NodeTile initializeTile0(){
        PosVec pVec = new PosVec(0, 0);
        NodeTile nT = new NodeTile(pVec, true, null);

        return nT;
    }

    @Test
    public void testBasicNT0(){
        NodeTile nT = initializeTile0();

        // note: once an NT has been set to passable, it should not be re-set
        assertTrue(nT.isPassable());
        assertTrue(nT.getParent()==null);
        assertTrue(nT.getPosition().equals(new PosVec(0, 0)));

        nT.setDistances(1, 2);
        assertTrue(nT.getTotalDistance()==3);

        NodeTile nT2 = new NodeTile(new PosVec(1, 1) ,false, nT);
        assertTrue(!nT.equals(nT2));
        assertTrue(nT2.getParent().equals(nT));

        NodeTile nT3 = new NodeTile(new PosVec(0, 0), true, nT);
        // symmetric check
        assertTrue(nT3.equals(nT));
        assertTrue(nT.equals(nT3));

        assertTrue(nT2.getParent().equals(nT));

    }

    @Test
    public void testBasicNT1(){
        NodeTile nT = initializeTile0();
        NodeTile nT2 = initializeTile0();

        nT.setDistances(0, 100);
        nT2.setDistances(100,0);

        assertTrue(nT.getDistToEnd()==100); 
        assertTrue(nT.getDistToStart()==0); 

        assertTrue(nT2.getDistToEnd()==0); 
        assertTrue(nT2.getDistToStart()==100); 

    }
}