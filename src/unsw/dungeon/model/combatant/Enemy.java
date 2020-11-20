package unsw.dungeon.model.combatant;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.combatstrategy.SwordStrategy;
import unsw.dungeon.model.position.DirectionFacing;
import unsw.dungeon.model.position.PosVec;
import unsw.dungeon.model.position.Trajectory;
import unsw.dungeon.model.worldobject.item.Item;

import java.util.Iterator;

/**
 * The enemy is an entity and combatant. The Enemy object contains its HP and a
 * way to get its HP.
 * <p>
 * It also contains:
 * <p>
 * - A tracker for how many times the player moves & when to recalibrate its
 * trajectory.
 * <p>
 * - A list of vectors that tell it where to go to find a player.
 * <p>
 * - A method for taking X amount of damage by another combatant.
 * <p>
 * - A method for attacking another combatant
 */
public class Enemy extends Combatant {

    private IntegerProperty HP;
    private int playerMoveTally;
    private Iterator<PosVec> trajectory = null;
    private int dmg = 2;

    private double prevDiagDist;

    public Enemy(int hp, int x, int y) {
        super(x, y);
        HP = new SimpleIntegerProperty();
        HP.set(hp);
        this.combatStrategy = new SwordStrategy();
        prevDiagDist = 99;
    }

    ///////
    //  GENERAL METHODS
    ///////

    public int getHP() {
        return HP.get();
    }

    public void setHP(int HP) {
        this.HP.set(HP);
    }

    /**
     * Enemy's interaction with an Entity.
     * Should be able to pick up an item & use it immediately, e.g. invincibility potion right
     * next to him.
     */
    public void interact(Entity entity) throws Exception {
        assert true;
    }

    /**
     * Tally each of the enemy's tracking of the player.
     * <p>
     * NOTE: Enemy movement not completely 'optimal' in MS2 due to presence of
     * portals.
     * <p>
     * Enemy will have access to a more general 'BFS' search in MS3 to ensure 100%
     * optimal movement.
     */
    public void update(Object obj) throws Exception {
        // enemy only moves after player has moved twice; or once at the beginning.
        playerMoveTally = (playerMoveTally + 1) % 2;
        Player player = (Player) obj;

        // recalculate trajectory on player movement
        if (playerMoveTally == 1) {
            // retreive player position
            PosVec placeToGo = new PosVec(0,0);

            // check if player is invicible
            if(player.isInvincible()){
                int newX = dungeonProbe.getWidth();
                int newY = dungeonProbe.getHeight();
                placeToGo.setPos(newX-player.getX(), newY-player.getY());
            } else {
                placeToGo = player.getPosition();
            }

            Trajectory trj = new Trajectory(position, placeToGo, dungeonProbe);
            setTrajectory(trj.calculateTrajectory());

        }

        // NOTE: enemy stops if cannot move any closer. The player must then walk into the enemy themselves to die.
        // move to next position if player not already in range
        if (!getNeighboringEntities().stream().anyMatch(o -> o.getClass().equals(Player.class)))
            moveNext();

        // attempt to attack the player; player takes damage iff they're within 1 unit
        // range. Default = 2 dmg.
        attack(player, dmg);
    }

    /**
     * Set the enemy's next travel.
     *
     * @param trajectory Iteractor of position vectors to follow.
     */
    public void setTrajectory(Iterator<PosVec> trajectory) {
        this.trajectory = trajectory;
    }

    public Iterator<PosVec> getTrajectory() {
        return trajectory;
    }

    /**
     * Move to next position in the trajectory. Enemy stays still if the next
     * position is impassable; will wait for the next turn to recalc & potentially
     * move again.
     */
    public void moveNext() {
        if (trajectory != null && trajectory.hasNext()) {
            move(trajectory.next(), null);
        }
    }

    /////////
    //  COMBATANT METHODS
    /////////

    @Override
    public void attack(Object obj, int dmg) throws Exception {
        // NOTE: assume that the combatant is a player for now
        Player player = (Player) obj;


        // deal 2 damage to player once.
        if (diagonalDistanceEntity((Entity) player) <= 1.0 && prevDiagDist <= 1.0)
            player.takeDamage(999);
        prevDiagDist = diagonalDistanceEntity((Entity) player);
    }

    @Override
    public void takeDamage(int damage) {
        HP.set(Math.max(HP.get() - damage, 0));
        if (HP.get() <= 0) {
            this.isInWorld.set(false);
        }
    }

    @Override
    public boolean isInvincible() {
        return false;
    }

    /**
     *
     * @param newPos - New position to move to. If position is impassable, enemy stays still.
     * @param dFacing - Expecting null for dFacing for now. Until enemy directional sprites have been created.
     * @return
     */
    @Override
    public boolean move(PosVec newPos, DirectionFacing dFacing) {
        // check if the new position is passable
        Entity e = dungeonProbe.getEntityAtCoords(newPos.getX(), newPos.getY());
        if (e == null || e.isPassable()) {
            setPos(newPos.getX(), newPos.getY());
            return true;
        }

        return false;
    }

    @Override
    public boolean useItem(Item item) {
        return false;
    }

}
