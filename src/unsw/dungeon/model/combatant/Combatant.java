package unsw.dungeon.model.combatant;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.dungeon.exceptions.EmptySlotException;
import unsw.dungeon.exceptions.InventoryFullException;
import unsw.dungeon.exceptions.NoDungeonPassException;
import unsw.dungeon.model.Entity;
import unsw.dungeon.model.combatant.combatstrategy.CombatStrategy;
import unsw.dungeon.model.combatant.combatstrategy.HyperAuraStrategy;
import unsw.dungeon.model.combatant.combatstrategy.InvincibleStrategy;
import unsw.dungeon.model.combatant.combatstrategy.NoWeaponStrategy;
import unsw.dungeon.model.combatant.combatstrategy.SwordStrategy;
import unsw.dungeon.model.position.DirectionFacing;
import unsw.dungeon.model.position.PosVec;
import unsw.dungeon.model.worldobject.item.Item;
import unsw.dungeon.observer.Observer;
import unsw.dungeon.view.AudioPlayer;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A combatant in this new Dungeon Crawler game.
 * <p>
 * Contains:
 * <p>
 * Attack, Taking Damage, Getting HP, Using an Item.
 * </p>
 * </p>
 */
public abstract class Combatant extends Entity {

    private IntegerProperty HP;
    protected Inventory inventory;
    protected CombatStrategy combatStrategy;

    /**
     * Create a combatant positioned in square (x,y)
     */
    public Combatant(int x, int y) {
        super(x, y);
        HP = new SimpleIntegerProperty();
        inventory = new Inventory();
    }

    public Combatant(){}

    /////////
    //  HP
    /////////
    public IntegerProperty HP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP.set(HP);
    }


    ////////
    //  COMBAT
    ////////
    public abstract void attack(Object obj, int dmg) throws Exception;

    public void takeDamage(int dmg) {
        HP.set(Math.max(combatStrategy.takeDamage(HP.get(), dmg), 0));
    }

    public boolean isInvincible() {
        return combatStrategy.isInvincible();
    };

    public void setInvincible() {
        combatStrategy = new InvincibleStrategy();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(this::endInvincibility, 3, TimeUnit.SECONDS);
        AudioPlayer.playSound("INVINCIBILITY");
        AudioPlayer.playSound("INVINCIBLE_COUNTDOWN");
    }

    public void endInvincibility() {
        if (hasCombatItem()) combatStrategy = new SwordStrategy();
        else combatStrategy = new NoWeaponStrategy();
    }

    public void setHyperAura(){
        // set an aura that never fades & extinguishes all entities
        combatStrategy = new HyperAuraStrategy();
    }

    public boolean isHyperd(){
        return combatStrategy.getClass().equals(HyperAuraStrategy.class);
    }

    //////
    //  MOVEMENT
    ///////
    public abstract boolean move(PosVec newPos, DirectionFacing dFacing) throws Exception;


    //////
    //  UTILITY
    //////

    /**
     *
     * @param item -> Specific item to be added to inventory
     */
    public void addToInv(Item item) throws InventoryFullException {
        inventory.addItem(item);
    }

    /**
     * Decrement the durability of the combatant's combat weapon by 1 point.
     */
    public void combatDurabilityDecrement(){
        inventory.combatDurabilityDecrement();
        if (!hasCombatItem()) combatStrategy = new NoWeaponStrategy();
    }

    /**
     * Searches for the item in the user's inventory. If it exists, will use the first one encountered
     * <p>
     * If it exists, will use the first one encountered
     * @return true if item was found and used, false if not.
     */
    public boolean useItem(Item item){
    //    // note cons1 -> return true, cons2 -> return false.
    //    Consumer<Boolean> ret = null;
    //    inventory.iterateInventory(i -> i.equals(item), j -> j.useItem(this), ret, ret);

    //    return ret;
        return false;
    }

    /**
     * Uses item in the slot number
     * @param slotNo 1 to 5
     */
    public void useItemInSlot(int slotNo) throws EmptySlotException, NoDungeonPassException {
        System.out.println(slotNo);
        Iterator<Item> itemIterator = inventory.getInvIterator();
        int currSlot = 1;
        while (itemIterator.hasNext() && currSlot < slotNo) {
            itemIterator.next();
            currSlot++;
        }
        Item item;
        try {
            item = itemIterator.next();
        } catch (NoSuchElementException e) {
            throw new EmptySlotException();
        }
        item.useItem(this);

    }

    /**
     *
     * @param item - Class of item, e.g. Treasure, Key, Invincibility Potion
     * @param id - ID of item. E.g. keyID = 0, if item class is 'Key'. Pass in id = -1 for no id.
     * @return True if item w/ <optional> id is in inventory. False otherwise.
     */
    public Item getItemInv(Item item, int id) {


        return null;
    }

    /**
     *
     * @param item -> Class of Item, e.g. Treasure, Key, Invincibility Potion
     * @return int -> Count of how many items the combatant has in their inventory
     */
    public int getItemCount(Item item){



        return 0;
    }

    /////////
    //  METHOD FORWARDING INVENTORY
    ////////
    public void addToInventory(Item item) throws InventoryFullException {
        inventory.addItem(item);
    }

    public void removeFromInventory(Item item) {
        inventory.removeItem(item);
    }

    public void incrementTreasureCount() { inventory.incrementTreasureCount(); }

    public boolean hasCombatItem() {
        return inventory.hasCombatItem();
    }

    public IntegerProperty treasureCount() {
        return inventory.treasureCount();
    }

    public void attachInventoryListener(Observer observer) {
        inventory.addListener(observer);
    }

    public boolean hasDungeonPass(){
        return inventory.hasDungeonPass();
    }

    /**
     * @pre combat slot is NOT full - check with hasCombatItem()
     */
    public void setCombatItem(Item item) {
        inventory.setCombatItem(item);
        this.combatStrategy = new SwordStrategy();
    }

    /**
     * Replaces an item position with another item in the user's inventory
     */
    public void swapInventory(Item item1, Item item2) {
        inventory.swap(item1, item2);
    }

    /**
     * Drop the first item in the inventory.
     */
    public void dropItem(){
        inventory.dropLastItem();
    }

}
