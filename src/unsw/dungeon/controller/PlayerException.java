package unsw.dungeon.controller;

import javafx.stage.Window;
import unsw.dungeon.exceptions.*;
import unsw.dungeon.view.AudioPlayer;

public class PlayerException {
    private Window window;
    public PlayerException(Window window) {
        this.window = window;
    }

    void handleException(Exception e) {

        ToastGenerator toastGenerator = new ToastGenerator();
        if (e.getClass().equals(NoInteractionException.class)) {
            AudioPlayer.playSound("NO_INTERACTION_ERR");
            toastGenerator.showToast(window, "You can't interact with that object.");
        }
        else if (e.getClass().equals(NoAttackTargetException.class)) {
            AudioPlayer.playSound("SWING_MISS");
        }
        else if (e.getClass().equals(NoWeaponException.class)) {
            AudioPlayer.playSound("NO_INTERACTION_ERR");
            toastGenerator.showToast(window, "You don't have a weapon to attack with.");
        }
        else if (e.getClass().equals(NoSpaceException.class)){
            AudioPlayer.playSound("WALL_BUMP");
        }
        else if (e.getClass().equals(WrongOrNoKeyException.class)){
            AudioPlayer.playSound("NO_INTERACTION_ERR");
            toastGenerator.showToast(window, "You don't have the right key.");
        } else if (e.getClass().equals(EmptySlotException.class)){
            AudioPlayer.playSound("NO_INTERACTION_ERR");
            toastGenerator.showToast(window, "Nothing in that item slot.");
        } else if (e.getClass().equals(NoDungeonPassException.class)){
            AudioPlayer.playSound("NO_INTERACTION_ERR");
            toastGenerator.showToast(window, "You need a Dungeon Pass to toggle Hyper Aura.");
        } else if (e.getClass().equals(InventoryFullException.class)) {
            AudioPlayer.playSound("NO_INTERACTION_ERR");
            toastGenerator.showToast(window, "Your inventory is full. [Q] to discard last slot.");
        }
        else {
            System.out.println("Unexpected exception.");
            //e.printStackTrace();
        }
        // or put an else statement that triggers error -> shouldnt be called in the first place.

    }


    
}