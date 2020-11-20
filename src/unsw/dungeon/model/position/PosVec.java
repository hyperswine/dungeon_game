package unsw.dungeon.model.position;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Position Vector to mark general positions in the dungeon.
 *
 */
public class PosVec {
    private IntegerProperty x;
    private IntegerProperty y;

    ObjectProperty<PosVec> pVecProp;

    public PosVec(int x, int y) {
        this.x = new SimpleIntegerProperty();
        this.x.set(x);
        this.y = new SimpleIntegerProperty();
        this.y.set(y);
        pVecProp = new SimpleObjectProperty<>();
    }

    public void setPos(int x, int y){
        this.x.set(x);
        this.y.set(y);
    }

    /**
     *
     * @param dx,dy - Amount to be incremented/decremented by
     */
    public void incrementPos(int dx, int dy){
        setPos(getX()+dx, getY()+dy);
    }

    public int getX() {
        return x.get();
    }

    public int getY() {
        return y.get();
    }

    public IntegerProperty x() { return x; }

    public IntegerProperty y() { return y; }

    /**
     * Determine whether two PosVec's refer to the same position.
     */
    public boolean equals(PosVec pVec) {
        return (pVec.getX()==getX() && pVec.getY()==getY());
    }

    /**
     * @return List of PosVec - list of neighbors (3-8) around current position
     *         vector.
     */
    public List<PosVec> getNeighbors(int dWidth, int dHeight) {
        List<PosVec> neighbors = new ArrayList<>();
        int x = getX();
        int y = getY();

        ///////////
        // UPPER SIDE
        ////////

        // x+1, y
        if (x + 1 <= dWidth)
            neighbors.add(new PosVec(x + 1, y));
        // x+1, y+1
        if (x + 1 <= dWidth && y + 1 <= dHeight)
            neighbors.add(new PosVec(x + 1, y + 1));
        // x+1, y-1
        if (x + 1 <= dWidth && y - 1 >= 0)
            neighbors.add(new PosVec(x + 1, y - 1));

        ////////
        // LEFT & RIGHT
        ////////

        // x, y-1
        if (y - 1 >= 0)
            neighbors.add(new PosVec(x, y - 1));
        // x, y+1
        if (y + 1 <= dHeight)
            neighbors.add(new PosVec(x, y + 1));

        ////////
        // LOWER SIDE
        ///////////

        // x-1, y
        if (x - 1 >= 0)
            neighbors.add(new PosVec(x - 1, y));
        // x-1, y+1
        if (x - 1 >= 0 && y + 1 <= dHeight)
            neighbors.add(new PosVec(x - 1, y + 1));
        // x-1, y-1
        if (x - 1 >= 0 && y - 1 >= 0)
            neighbors.add(new PosVec(x - 1, y - 1));

        return neighbors;
    }

    /**
     * NOTE: using c = 1; replace (1) if want higher cost for diagonal movement.F
     * <p>
     * Calculate the diagonal distance from (this) vector to another vector.
     */
    public double diagonalDistance(PosVec p) {
        int dx = Math.abs(p.getX() - getX());
        int dy = Math.abs(p.getY() - getY());

        int min = Math.min(dx, dy);
        int max = Math.max(dx, dy);

        return 1 * min + (max - min);
    }

    public double euclideanDistance(PosVec p) {
        return Math.sqrt(Math.pow(p.getX() - getX(), 2) + Math.pow(p.getY() - getY(), 2));
    }
    
}
