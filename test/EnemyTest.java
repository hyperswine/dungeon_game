package test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import unsw.dungeon.model.Dungeon;
import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.Enemy;
import unsw.dungeon.model.combatant.Player;
import unsw.dungeon.model.worldobject.item.Item;
import unsw.dungeon.model.worldobject.item.Key;
import unsw.dungeon.model.position.PosVec;
import unsw.dungeon.prober.DungeonProbe;

/**
 * Tests various units in Enemy class & interactions between player/enemy.
 *
 * <p>
 * Aim to find flaws/bugs in:
 * <p>
 * - trajectory
 * <p>
 * - turn-based movement & combat
 * <p>
 * - basic things, that may have been overlooked
 * </p>
 */
public class EnemyTest {
    private Dungeon dungeon;
    private Enemy enemy;
    DungeonProbe dP;

    /**
     * Set up a dungeon-based framework for general interactions.
     * <p>
     * NOTE: should not be treated as a fixture.
     */
    private DungeonMockLoader getController() {
        JSONInstances jsonInstances = new JSONInstances();
        DungeonMockLoader dMC = jsonInstances.createBasicController(150, 150);

        dungeon = dMC.load();
        Stream<Entity> entsStream = dungeon.getEntities().stream().filter((e -> (e instanceof Enemy)));
        List<Entity> ents = entsStream.collect(Collectors.toList());
        enemy = (Enemy) ents.get(0);

        return dMC;
    }

    /**
     * Set up a basic trajectory in a diagonal line.
     *
     * @param travelDist - How many diagonal tiles you want to travel (>0).
     * @return Iterator containing position vectors to follow.
     */
    private Iterator<PosVec> diagTrajectory(int travelDist) {
        List<PosVec> trj = new ArrayList<>();

        for (int i = 0; i < travelDist; i++)
            trj.add(new PosVec(i, i));

        return trj.iterator();
    }

    /**
     * @param travelDist -> A positive value. A randomly generated trajectory. NOTE:
     *                   will not included repeated positions.
     */
    private Iterator<PosVec> randTrajectory(int travelDist) {
        List<PosVec> trj = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        // initialize values
        for (int i = 0; i < 2 * travelDist; i++)
            values.add(i);

        // randomize the array
        Collections.shuffle(values);

        // initialize the posvec's
        for (int i = 0; i < travelDist - 1; i++)
            trj.add(new PosVec(i, i + 1));

        return trj.iterator();
    }

    /**
     * Test some basic functionalities
     */
    @Test
    public void testBasic0() {
        getController();
        assertTrue(enemy.getHP() == 2);

        enemy.moveNext();
        assertTrue(enemy.getX() == 2 && enemy.getY() == 2);

        PosVec newPos = new PosVec(6, 8);
        enemy.movePos(newPos);

        assertTrue(enemy.getPosition().equals(newPos));
        assertTrue(enemy.getDungeonProbe().getEnemies().hasNext());
    }

    /**
     * More basic functionalities
     */
    @Test
    public void testBasic1() {
        // resets the variables
        getController();

        enemy.setInvincibiity();

        // enemy shouldnt be invincible in MS2
        assertTrue(!enemy.isInvincible());

        Item item = new Key(10, 11, 0);

        assertTrue(!enemy.useItem(item));

        // ensure item still exists
        assertTrue(item != null);
    }

    /**
     * Test if enemy can follow trajectory
     */
    @Test
    public void testFollowTrajectory() {
        DungeonMockLoader dMC = getController();
        dungeon = dMC.load();

        Iterator<PosVec> trj = diagTrajectory(10);

        // manually travel the trj.
        while (trj.hasNext()) {
            PosVec curr = trj.next();
            if (curr.getXInt() == 1 && curr.getYInt() == 1)
                continue;
            assertTrue(enemy.movePos(curr));
            assertTrue(enemy.getPosition().equals(curr));
        }

    }

    /**
     * Ensure enemy can automatically follow a trajectory.
     */
    @Test
    public void testAutoTrajectory() {
        DungeonMockLoader dMC = getController();
        dungeon = dMC.load();

        // follow a random trajectory
        Iterator<PosVec> trj = randTrajectory(20);

        // auto-travel
        enemy.setTrajectory(trj);

        // NOTE: import reflections to inspect for any flaws/ boolean movePos()
        for (int i = 0; i < 20; i++) {
            enemy.moveNext();
        }

        assert (!trj.hasNext());
    }

    /**
     * Ensure that most /position/trajectory related methods work before trying to
     * test this!
     */
    @Test
    public void testUpdate0() {
        DungeonMockLoader dMC = getController();
        dungeon = dMC.load();

        Player player = dungeon.getPlayer();

        // create emtpy trajectory
        List<PosVec> trj = new ArrayList<>();
        enemy.setTrajectory(trj.iterator());
        assertTrue(!enemy.getTrajectory().hasNext());

        // update enemy once
        enemy.update(player);

        // confirm calculation for trajectory towards (1,1)
        Iterator<PosVec> updatedTrj = enemy.getTrajectory();
        // enemy will have attempted to move but player right next to him.
        assertTrue(updatedTrj.hasNext());
        assertTrue(updatedTrj.next().equals(new PosVec(1, 1)));

        // confirm damage dealt to player
        assertTrue(player.getHP() == 3);

        // update again
        enemy.update(player);

        // confirm staying still
        assertTrue(enemy.getPosition().equals(new PosVec(2, 2)));

        // confirm damage
        assertTrue(player.getHP() == 1);
    }

    /**
     * Test update on enemy to follow longer trajectory.
     */
    @Test
    public void testUpdate1() {
        DungeonMockLoader dMC = getController();
        dungeon = dMC.load();

        Player player = dungeon.getPlayer();

        // move player to some arbitary tile
        player.setPos(69, 96);

        // ensure enemy & players in their right positoin
        assertTrue(player.getX()==69&&player.getY()==96);
        assertTrue(enemy.getX()==2&&enemy.getY()==2);

        int max = Math.max((player.getX()-enemy.getX()), (player.getY()-enemy.getY()));

        // update the enemy
        enemy.update(player);

        // ensure the enemy has a good trajectory towards the player
        // i.e. a path length of max(dx, dy)
        Iterator<PosVec> trj = enemy.getTrajectory();

        int count = 0;
        while(trj.hasNext()){
            // ensure the next position isnt somewhere far off, e.g. if one were to use BFS.
            PosVec nextPos = trj.next();
            assertTrue(nextPos.getXInt()<70 && nextPos.getYInt()<97);
            count++;
        }

        assertTrue(count <= max);
        // ensure player hasn't taken any damage
        assertTrue(player.getHP()==5);

    }

}
