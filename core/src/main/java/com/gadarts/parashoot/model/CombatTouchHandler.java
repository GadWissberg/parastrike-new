package com.gadarts.parashoot.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.player.bunker.Turret;
import com.gadarts.parashoot.screens.WarScreen;
import com.gadarts.parashoot.utils.Rules;

public class CombatTouchHandler implements InputProcessor {
    private Vector3 lastTouch, lastSwipe;
    private Vector3 currentTouchVector = new Vector3();
    private boolean beingDragged;
    private WarScreen warScreen;
    private boolean swipeAllowed = Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS).getBoolean(Assets.Configs.Preferences.Settings.SWIPE, true);
    private boolean touchAllowed = Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS).getBoolean(Assets.Configs.Preferences.Settings.TOUCH, true);
    private boolean locked;

    public CombatTouchHandler(WarScreen warScreen) {
        lastTouch = new Vector3();
        lastSwipe = new Vector3();
        this.warScreen = warScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (locked) {
            return false;
        }
        currentTouchVector.set(screenX, screenY, 0);
        warScreen.getMainCamera().unproject(currentTouchVector);
        beingDragged = false;
        Turret turret = warScreen.getPlayerHandler().getTurret();
        if (currentTouchVector.y >= Rules.Hud.SWIPE_ZONE_Y) {
            if (turret.isRotatable()) {
                if (touchAllowed) {
                    Vector3 delta = currentTouchVector.cpy().sub(lastTouch);
                    turret.setPressingFireState(true);
                    turret.setDirectionToPosition(delta.x, delta.y);
                    return true;
                }
            }
        }
        if ((swipeAllowed && currentTouchVector.y < Rules.Hud.SWIPE_ZONE_Y) || !turret.isRotatable()) {
            turret.setPressingFireState(true);
            turret.setTargetPosition(currentTouchVector.x, currentTouchVector.y);
            lastSwipe.set(currentTouchVector);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (locked) return false;
        currentTouchVector.set(screenX, screenY, 0);
        warScreen.getMainCamera().unproject(currentTouchVector);
        warScreen.getPlayerHandler().getTurret().setPressingFireState(false);
        beingDragged = false;
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (locked) return false;
        currentTouchVector.set(screenX, screenY, 0);
        warScreen.getMainCamera().unproject(currentTouchVector);
        Turret turret = warScreen.getPlayerHandler().getTurret();
        if (currentTouchVector.y >= Rules.Level.GROUND_Y && turret.isRotatable() && !beingDragged) {
            if (touchAllowed) {
                Vector3 delta = currentTouchVector.cpy().sub(lastTouch);
                turret.setPressingFireState(true);
                turret.setDirectionToPosition(delta.x, delta.y);
                lastSwipe.set(currentTouchVector);
                return true;
            }
        } else {
            Vector3 deltaVector = currentTouchVector.cpy().sub(lastSwipe);
            if (turret.isRotatable()) {
                if (swipeAllowed) {
                    beingDragged = true;
                    if (deltaVector.x > 0) {
                        turret.setDirection(turret.getDirection() - deltaVector.len() / 3);
                    } else if (deltaVector.x < 0) {
                        turret.setDirection(turret.getDirection() + deltaVector.len() / 3);
                    }
                    lastSwipe.set(currentTouchVector);
                } else {
                    return false;
                }
            } else {
                turret.setTargetPosition(currentTouchVector.x, currentTouchVector.y);
            }
            turret.setPressingFireState(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    void setTouchLocked(boolean touchLocked) {
        if (!locked && touchLocked) {
            warScreen.getPlayerHandler().getTurret().setPressingFireState(false);
            beingDragged = false;
        }
        locked = touchLocked;
    }
}
