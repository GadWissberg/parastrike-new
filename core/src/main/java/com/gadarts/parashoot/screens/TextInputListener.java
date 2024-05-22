package com.gadarts.parashoot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.gadarts.parashoot.assets.Assets;

/**
 * Created by Gad on 16/12/2015.
 */
public class TextInputListener implements Input.TextInputListener {
    @Override
    public void input(String text) {
        Preferences prefs = Gdx.app.getPreferences(Assets.Configs.Preferences.Player.PREF_PLAYER);
        prefs.flush();
    }

    @Override
    public void canceled() {
        Preferences prefs = Gdx.app.getPreferences(Assets.Configs.Preferences.Player.PREF_PLAYER);
        prefs.flush();
    }
}
