package com.gadarts.parashoot.model;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.level_model.Level;
import com.gadarts.parashoot.level_model.Scene;
import com.gadarts.parashoot.screens.WarScreen;

import java.util.Map;

/**
 * Created by Gad on 29/06/2017.
 */

public class WarScreenElements {
    private final WarScreen warScreen;
    private ScoresHandler scoresHandler;
    private InteractionsManager interactionsManager;
    private EffectsManager effectsManager;
    private MessageDisplay messageDisplay;
    private com.gadarts.parashoot.level_model.Level currentLevel;

    public WarScreenElements(WarScreen warScreen) {
        this.warScreen = warScreen;
        scoresHandler = new ScoresHandler(this);
        effectsManager = new EffectsManager(this, warScreen.getFactories(), warScreen.getPlayerHandler(), warScreen.getPainter(), warScreen.getHud());
        interactionsManager = new InteractionsManager(this, warScreen.getPlayerHandler());
        messageDisplay = new MessageDisplay();
    }

    public com.gadarts.parashoot.level_model.Level getCurrentLevel() {
        return currentLevel;
    }

    public void initializeLevel() {
        currentLevel = Parastrike.getAssetsManager().getAll(com.gadarts.parashoot.level_model.Level.class, new Array<Level>()).first();
        HUD hud = warScreen.getHud();
        currentLevel.start(this, warScreen.getFactories(), hud);
        hud.getOptionsButton().setVisible(currentLevel.isOptionsButtonShown());
        hud.getBombsButton().setVisible(currentLevel.isBombsButtonShown());
    }

    public MessageDisplay getMessageDisplay() {
        return messageDisplay;
    }

    public ScoresHandler getScoresHandler() {
        return scoresHandler;
    }

    public EffectsManager getEffectsManager() {
        return effectsManager;
    }

    public InteractionsManager getInteractionsManager() {
        return interactionsManager;
    }

    public void addObjectToMap(GameObject gameObject, int list) {
        addObjectToMap(gameObject, list, false);
    }

    public void addObjectToMap(GameObject gameObject, int list, boolean addFirst) {
        warScreen.addObjectToMap(gameObject, list, addFirst);
    }

    public void destroyAllEnemies() {
        warScreen.destroyAllEnemies();
    }

    public Map<Integer, Array<? extends GameObject>> getObjectsMap() {
        return warScreen.getObjectsMap();
    }

    public Array<? extends GameObject> getAllParatroopers() {
        return warScreen.getAllParatroopers();
    }

    public OrthographicCamera getParastrikeCamera() {
        return warScreen.getParastrikeCamera();
    }

    public void setSceneUnlockIndex(int sceneCompletionIndex) {
        warScreen.setSceneUnlockIndex(sceneCompletionIndex);
    }


    public Array<Array<Enemy>> getAllEnemies() {
        return warScreen.getAllEnemies();
    }

    public void endBattle() {
        warScreen.endBattle();
    }

    public void setTouchLocked(boolean touchLocked) {
        warScreen.getCombatTouchHandler().setTouchLocked(touchLocked);
    }

    public void pauseGame(boolean enablePauseMessage) {
        warScreen.setPauseMessage(enablePauseMessage);
        warScreen.setPauseLock(Assets.InGameGuides.Focus.PAUSE_LOCK_DURATION);
        warScreen.pause();
    }

    public void sceneCompleted(Scene currentScene) {
        warScreen.sceneCompleted(currentScene);
    }
}
