package com.gadarts.parashoot.model;

import com.gadarts.parashoot.Parastrike;

/**
 * Created by Gad on 16/05/2016.
 */
public class ScoresHandler {
    private final WarScreenElements mechanics;
    private int enemiesDestroyedCounter;
    private int sessionScore;
    private int sessionCoins;

    public ScoresHandler(WarScreenElements mechanics) {
        this.mechanics = mechanics;

    }

    public void addScore(int scoreDelta) {
        if (!mechanics.getCurrentLevel().isGainAllowed()) {
            return;
        }
        sessionScore += scoreDelta;
        mechanics.getEffectsManager().createScoreEffect(scoreDelta);
        Parastrike.getPlayerStatsHandler().setScore(Parastrike.getPlayerStatsHandler().getScore() + scoreDelta);
    }

    public void addCoins(int coinsDelta) {
        addCoins(coinsDelta, -1, -1);
    }

    public void addCoins(int coinsDelta, float x, float y) {
        if (!mechanics.getCurrentLevel().isGainAllowed() || coinsDelta <= 0) {
            return;
        }
        mechanics.getEffectsManager().createCoinEffect(coinsDelta, x, y);
        sessionCoins += coinsDelta;
        Parastrike.getPlayerStatsHandler().setCoins(Parastrike.getPlayerStatsHandler().getCoins() + coinsDelta);
    }

    public int getSessionCoins() {
        return sessionCoins;
    }

    public int getSessionScore() {
        return sessionScore;
    }

    public void enemyDestroyed() {
        enemiesDestroyedCounter++;
    }

    public int getEnemiesDestroyed() {
        return enemiesDestroyedCounter;
    }

    public void reset() {
        enemiesDestroyedCounter = 0;
    }
}
