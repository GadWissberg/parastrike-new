package com.gadarts.parashoot.model;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gadarts.parashoot.screens.MenuScreenImproved;

import java.util.ArrayList;

public interface GGSActionResolver {
    boolean isSignedIn();

    void logout();

    void rateGame();

    void login();

    void submitScore(int score);

    void unlockAchievement(String achievementId);

    void showLeaderBoard();

    void getAchievements();

    void showSavedGamesUI();

    void saveSnapshot(final Image savingAnimation);

    void loadFromSnapshot();

    boolean shouldSaveGame();

    void setShouldSaveGame(boolean should);

    void queryProducts(ArrayList<String> productsNames, MenuScreenImproved.CoinsButton coinsButton);

    void buy(String pack1Id);
}
