package unsw.dungeon.model.goal;

/**
 * Goal types include:
 * 
 * <p>
 * - Getting to specific exit(s).
 * <p>
 * - Neutralizing all enemies.
 * <p>
 * - Having a boulder on all floor switches.
 * <p>
 * - Collecting all treasure.
 * <p>
 * - AND, OR
 * 
 */
public enum GoalType {
    EXIT, BOULDERS, TREASURE, ENEMIES, AND, OR
}