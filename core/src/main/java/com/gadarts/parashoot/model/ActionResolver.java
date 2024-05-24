package com.gadarts.parashoot.model;

import com.badlogic.gdx.ApplicationListener;

import java.util.HashMap;

/**
 * An action resolver - Used to send actions from the game itself to the Android part.
 */
public interface ActionResolver extends ApplicationListener {

    /**
     * Starts the analytics session.
     */
    void StartSession();

    /**
     * Ends the analytics session.
     */
    void EndSession();

    /**
     * Starts an activity with an email intent.
     */
    void mail();

    /**
     * Show toast.
     *
     * @param message The message to show.
     */
    void toast(@SuppressWarnings("SameParameterValue") String message);

    /**
     * Returns the game's version specified in the manifest.
     */
    String getAppVersion() throws Exception;

    /**
     * Calls the android audio object's sound pool autoPause.
     */
    void autoPauseSoundPool();


}
