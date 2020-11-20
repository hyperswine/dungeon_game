package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.dungeon.model.position.PosVec;

/**
 * This module tests mainly for general position related routines.
 * <p>
 * This does not cover trajectory & specific interactions between components!
 * <p>
 * Does not require the dungeon.
 * <p>
 * Meant as a sanity check & unit test for position to ensure no obvious bugs.
 */
public class PositionTest {

    /**
     * Randomly generate n positions.
     * TODO: make this actually return random & non-obstructing positions
     * 
     * NOTE: tests using this method should be run multiple times.
     * @param number - How many positions to be returned (int >= 1)
     * @return List of position vectors
     */
    public List<PosVec> setUpPositions(int number) {
        List<PosVec> positions = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        // initialize values
        for (int i = 0; i < 2 * number; i++)
            values.add(i);

        // randomize the array
        Collections.shuffle(values);

        // initialize the posvec's
        for (int i = 0; i < number - 1; i++)
            positions.add(new PosVec(i, i + 1));

        return positions;
    }

    /**
     * Ensure 2 position vectors are exactly one unit of distance apart
     */
    public boolean oneUnitApart(PosVec p1, PosVec p2){
        int d1 = Math.abs(p1.getXInt() - p2.getXInt());
        int d2 = Math.abs(p1.getYInt() - p2.getYInt());

        return (Math.max(d1, d2)==1);
    }

    public boolean neighborsApart(PosVec currPos, List<PosVec> neighbors){

        // ensure correctness
        for(PosVec n: neighbors){
            // ensure pVec's context is correct
            if (currPos.diagonalDistance(n)!=1) return false;
            // ensure n's context is correct
            if (n.diagonalDistance(currPos)!=1) return false;;
            // ensure test context is correct
            if (!oneUnitApart(n, currPos)) return false;;
        }

        return true;
    }

    /**
     * Test basic functionalities and states for the position vector
     */
    @Test
    public void testBasicPosVec0() {
        PosVec pVec = new PosVec(0, 0);

        // bind listeners to PosVec
        IntegerProperty listenerX = new SimpleIntegerProperty();
        IntegerProperty listenerY = new SimpleIntegerProperty();
        listenerX.bind(pVec.X());
        listenerY.bind(pVec.Y());

        assertTrue(listenerX.get() == 0);
        assertTrue(listenerY.get() == 0);

        // listeners must be working
        pVec.setX(1);
        pVec.setY(1);
        assertTrue(listenerX.get() == 1);
        assertTrue(listenerY.get() == 1);

        // create new position vector
        PosVec pVec2 = new PosVec(0, 0);
        assertTrue(!pVec.equals(pVec2));

        pVec2.setX(1);
        pVec2.setY(1);
        assertTrue(pVec.equals(pVec2));

        // change value again
        pVec.setX(2);
        assertTrue(listenerX.get() == 2);
        assertTrue(!pVec.equals(pVec2));
    }

    @Test
    public void testBasicPosVec1() {
        PosVec pVec = new PosVec(1, 1);

        pVec.setX(0);
        assertTrue(pVec.getXInt()==0);

        PosVec pVec2 = new PosVec(0, 1);
        assertTrue(pVec.diagonalDistance(pVec2)==0);
        assertTrue(pVec.euclideanDistance(pVec2)==0);

        PosVec pVec3 = new PosVec(3,3);
        assertTrue(pVec.diagonalDistance(pVec3)==3);
        assertTrue(pVec2.diagonalDistance(pVec3)==3);

        pVec3.setX(100);
        assertTrue(pVec.diagonalDistance(pVec3)==100);
        assertTrue(pVec.euclideanDistance(pVec3)>100);
    }

    /**
     * Basic behavior of neighbor
     */
    @Test
    public void testBasicNeighbors(){
        ///////////////
        // 'CORNER' position
        ////////////

        PosVec pVec = new PosVec(0, 0);
        // get the neighbors of pVec in a 10X10 context
        List<PosVec> neighbors1 = pVec.getNeighbors(10, 10);
        // ensure correctness
        assertTrue(neighborsApart(pVec, neighbors1));
        
        /////////
        // 'NORMAL' position
        /////////

        pVec = new PosVec(2, 2);
        neighbors1 = pVec.getNeighbors(10, 10);
        assertTrue(neighborsApart(pVec, neighbors1));

        //////////
        // 'EDGE' position
        //////////////

        pVec = new PosVec(0, 2);
        neighbors1 = pVec.getNeighbors(10, 10);
        assertTrue(neighborsApart(pVec, neighbors1));
    }

    /**
     * Ensure the neighbors are correctly identitifed by the position vectors.
     */
    @Test
    public void testNeighbors() {
        List<PosVec> positions = setUpPositions(20);

        for(PosVec p: positions)
            assertTrue(neighborsApart(p, p.getNeighbors(30, 30)));
    
    }
}
