package unsw.dungeon.model.combatant;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import unsw.dungeon.exceptions.NoInteractionException;
import unsw.dungeon.exceptions.NoSpaceException;
import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.combatstrategy.HyperAuraStrategy;
import unsw.dungeon.model.combatant.combatstrategy.InvincibleStrategy;
import unsw.dungeon.model.combatant.combatstrategy.NoWeaponStrategy;
import unsw.dungeon.model.position.DirectionFacing;
import unsw.dungeon.model.position.PosVec;
import unsw.dungeon.model.worldobject.item.Item;
import unsw.dungeon.model.worldobject.item.Key;
import unsw.dungeon.model.worldobject.item.Sword;

import java.util.Iterator;
import java.util.List;

/**
 * The player, manipulated mostly by the PlayerController & other entities.
 * <p>
 * Contains:
 * - default HP values
 * - a 'combat state' of weapon/weaponless and invincibility on/off
 * - a 'direction' UP/DOWN/LEFT/RIGHT
 * - methods to move the player cardinally by 1 tile
 * - method to interact with 'world objects'
 * - methods to pick up & use 'items'
 * - methods to attack & take damage to/from a combatant
 * - methods to access, manipulate, and an inventory object.
 */
public class Player extends Combatant {

    private ObjectProperty<DirectionFacing> directionFacing;

    /**
     * Construct a player object. Note that a combatant's hp can be set directly.
     */
    public Player(int x, int y) {
        super(x, y);


        this.directionFacing = new SimpleObjectProperty<>();
        this.combatStrategy = new NoWeaponStrategy();
        // default HP values for Player
        setHP(5);
    }


    ///////
    //  GENERAL METHODS
    ///////

    /**
     * Player interacts with an entity, passing (this) into it.
     *
     * @throws Exception - If the entity is non-interactable, they may throw an exception.
     */

    public void interactWithEntityFacing() throws Exception {
        // interacts with the object in front of user.
        Entity target = dungeonProbe.entityFacing(directionFacing.get(), getX(), getY());
        if (target == null) throw new NoInteractionException();
        target.interact(this);
    }

    public void interact(Entity target) throws NoInteractionException {
        throw new NoInteractionException();
    }

    public void updateDirection(DirectionFacing directionFacing) {
        this.directionFacing.set(directionFacing);
    }

    public ObjectProperty<DirectionFacing> directionFacing() {
        return directionFacing;
    }


    /////////
    //  COMBATANT METHODS
    /////////

    /**
     * Increment the movement of the player by dx, dy.
     *
     * @param dx,dy -> amount of movement from the current tile.
     */
    public boolean move(int dx, int dy, DirectionFacing dFacing) throws Exception {
        Entity dest = dungeonProbe.getEntityAtCoords(getX() + dx, getY() + dy);
        updateDirection(dFacing);
        if (dest == null || dest.isPassable()) {
            position.incrementPos(dx, dy);
        }
        else if (combatStrategy.getClass().equals(InvincibleStrategy.class) && dest instanceof Combatant) {
            attack(null, 10);
            position.incrementPos(dx, dy);
        }
        // destroy all entities around the combatant
        // SUGGESTION: have some nice burning sound effects
        else if (combatStrategy.getClass().equals(HyperAuraStrategy.class)){
            List<Entity> entities = getNeighboringEntities();
            for(Entity e: entities)
                if(e instanceof Enemy) e.setIsInWorld(false);
            
            position.incrementPos(dx, dy);
        }
        else throw new NoSpaceException();

        // if position wasn't passable, player would have not passed
        //return (dest.isPassable()); //may raise nullpointerexpcetion where dest is empty
        return true;
    }

    /**
     * Make the player attempt to move into a new position.
     * NOTE: The player should only move to neighboring positions
     *
     * Only used in testing purposes for now.
     */
    @Override
    public boolean move(PosVec newPos, DirectionFacing dFacing) throws Exception {
        return false;
    }

    /**
     * <p>
     * Player will only 'attack' a combatant. If the object was not a combatant on the frontend,
     * the controller handles it.
     * </p><p>
     * Player does base (2) damage with no weapon, i.e. using fists.
     * </p>
     * NOTE: Method no longer throws exceptions.
     */
    @Override
    public void attack(Object obj, int dmg) throws Exception {
        Entity target = dungeonProbe.entityFacing(directionFacing.get(), getX(), getY());
        if (combatStrategy.damagesWeapon()) {
            combatDurabilityDecrement();
        }
        combatStrategy.attack(target, dmg);

    }

    public IntegerProperty weaponDurability() {
        if (hasCombatItem())
            return ((Sword) inventory.getCombatItem()).hitsRemaining();
        else return new SimpleIntegerProperty(9999);
    }
    /////////
    //  EXTRA METHODS
    /////////

    /**
     * @param id - The id of the key to be used
     * @return true if key in inventory, false otherwise.
     */
    public boolean useKey(int id){
        Iterator<Item> itemIterator = inventory.getInvIterator();
        while (itemIterator.hasNext()) {
            Item item = itemIterator.next();
            if (item.getClass().equals(Key.class)) {
                Key key = (Key) item;
                boolean result = key.useItem(id);
                if (result) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Make the player change his sprite to something, or temporary sound
     */
    public void emote(String emote){
        if(emote == "taunt"){
            
        }
    }

    public void sortItems(){
        inventory.sortInventory();
    }

}
