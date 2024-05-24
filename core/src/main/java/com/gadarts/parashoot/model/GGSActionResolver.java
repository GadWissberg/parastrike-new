package com.gadarts.parashoot.model;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public interface GGSActionResolver {
    boolean isSignedIn();

    void logout();


    void login();

    void submitScore(int score);


    void saveSnapshot(final Image savingAnimation);

    void loadFromSnapshot();

    boolean shouldSaveGame();

    void setShouldSaveGame(boolean should);

}
