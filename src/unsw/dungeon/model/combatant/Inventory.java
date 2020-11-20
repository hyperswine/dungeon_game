package unsw.dungeon.model.combatant;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.dungeon.exceptions.InventoryFullException;
import unsw.dungeon.model.worldobject.item.DungeonPass;
import unsw.dungeon.model.worldobject.item.Item;
import unsw.dungeon.model.worldobject.item.Sword;
import unsw.dungeon.observer.Observer;
import unsw.dungeon.observer.Subject;
import unsw.dungeon.model.worldobject.item.ItemComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Inventory implements Subject {
    private Item combatItem;
    private List<Item> itemList;
    private IntegerProperty treasureCount;
    private List<Observer> observers;

    public Inventory() {
        combatItem = null;
        itemList = new ArrayList<>();
        treasureCount = new SimpleIntegerProperty();
        treasureCount.set(0);
        observers = new ArrayList<>();
    }

    public Iterator<Item> getInvIterator() {
        return itemList.iterator();
    }

    /**
     * NOTE: only 1 combat item may be held at a time.
     */
    public void setCombatItem(Item combatItem) {
        this.combatItem = combatItem;
        notifyObservers(this);
    }

    public boolean hasCombatItem() {
        return combatItem != null;
    }

    public boolean hasDungeonPass() {
        for (Item i : itemList)
            if (i instanceof DungeonPass)
                return true;

        return false;
    }

    public void swap(Item item1, Item item2) {
        Collections.swap(itemList, itemList.indexOf(item1), itemList.indexOf(item2));
    }

    public void addItem(Item item) throws InventoryFullException {
        if (itemList.size() >= 5) throw new InventoryFullException();
        itemList.add(item);
        System.out.println(itemList);
        notifyObservers(this);
    }

    public void removeItem(Item item) {
        itemList.remove(item);
        System.out.println(itemList);
        notifyObservers(this);
    }

    /**
     * Drop the first item present in the inventory.
     */
    public void dropLastItem(){
        Iterator<Item> itemIterator = getInvIterator();
        Item prevItem = null;
        while (itemIterator.hasNext()) {
            prevItem = itemIterator.next();
        }
        itemList.remove(prevItem);
        notifyObservers(this);
    }

    /**
     * Template for inventory iteration
     */
    public void iterateInventory(Predicate<Item> pred, Function<Item, Item> func, Consumer<Object> cons1,
            Consumer<Object> cons2) {
        Iterator<Item> invIterator = getInvIterator();
        Object x = null;
        while (invIterator.hasNext()) {
            Item currItem = invIterator.next();
            if (pred.test(currItem)) {
                x = func.apply(currItem);
                cons1.accept(x);
            }
        }
        cons2.accept(x);
    }

    public void combatDurabilityDecrement() {
        if (combatItem == null)
            return;
        Sword swordItem = (Sword) combatItem;
        swordItem.decrementHitsRemaining();
        if (swordItem.hitsRemaining().get() == 0) {
            Item old = combatItem;
            combatItem = null;
            notifyObservers(this);
            combatItem = old;
            return;
        }
        if (swordItem.hitsRemaining().get() < 0) {
            combatItem = null;
        }
        notifyObservers(this);
    }

    public void sortInventory(){
        itemList.sort(new ItemComparator());
        notifyObservers(this);
    }

    /**
     * Returns an iterator containing all sprites in the current inventory Slot 0 is
     * for combat item, if it exists. If not, slot 0 is the first item.
     *
     * @return
     */
    public Iterator<String> getSpritesIterator() {
        List<String> spriteList = new ArrayList<>();
        if (combatItem != null) {
            spriteList.add(combatItem.spriteFileName());
        }
        for (Item item : itemList) {
            spriteList.add(item.spriteFileName());
        }
        return spriteList.iterator();
    }

    public void incrementTreasureCount() {
        treasureCount.set(treasureCount.get() + 1);
    }

    public IntegerProperty treasureCount() {
        return treasureCount;
    }

    public Item getCombatItem() {
        return combatItem;
    }

    @Override
    public void notifyObservers(Object obj) {
        for (Observer observer : observers) {
            observer.update(obj);
        }
    }

    @Override
    public boolean addListener(Object obj) {
        if (!(obj instanceof Observer))
            return false;
        observers.add((Observer) obj);
        return true;
    }

    @Override
    public boolean removeListener(Object obj) {
        if (!(obj instanceof Observer))
            return false;
        observers.add((Observer) obj);
        return false;
    }

}
