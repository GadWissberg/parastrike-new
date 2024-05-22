package com.gadarts.parashoot.model.interfaces;

import com.gadarts.parashoot.screens.WarScreen;

/**
 * Represents a life cycle of a game object.
 */
public interface LifeCycle {
    /**
     * Called once in every frame.
     * @param delta
     */
    void onUpdate(float delta);

    /**
     * Called when this object should be marked to onTerminate.
     *
     * @param doDestroySequence Whether to perform it's destroy actions sequence (like explosions, scores, etc...).
     */
    void onDestroy(boolean doDestroySequence);

    /**
     * Removes this object from the game.
     * @param warScreen The war screen object that will remove the object.
     */
    void onTerminate(WarScreen warScreen);
}
