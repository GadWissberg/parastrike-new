package com.gadarts.parashoot.enemies.aircrafts;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.AirCraft;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;

/**
 * The Osprey enemy.
 */
public class Osprey extends AirCraft {

    public Osprey() {
        super();
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.AirCrafts.OSPREY_DATA_FILE);
        paratrooperReleaseDelay = getSpecificBySkill();
        gibReach = Rules.Enemies.AirCrafts.Osprey.GIB_REACH;
        motorSoundFileName = SFX.Enemies.AirCrafts.HELICOPTER_FLY;
    }

    /**
     * Poolable initialization method.
     *
     * @param x             Starting X.
     * @param y             Starting Y.
     * @param direction     Starting direction (degrees).
     * @param mechanics     The war screen's mechanics.
     * @param playerHandler The war screen's player handler.
     * @param factories     The war screen's factories.
     */
    public void init(float x, float y, float direction, WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.IDLE, x, y, direction, getSpeedBySkill(), getHealthBySkill(), mechanics, playerHandler, factories);
        if (GameSettings.ALLOW_PARATROOPERS) {
            scheduleTask(TASK_CREATE_PARATROOPER, paratrooperReleaseDelay);
        }
        faceRegion();
    }

    /**
     * Completely overridden method. This isn't relevant to the Osprey.
     * @param delta
     */
    @Override
    protected void onWaitingForNewOrder(float delta) {
        //Do nothing.
    }

    @Override
    public float getHealthBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.AirCrafts.Osprey.Easy.HP;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.AirCrafts.Osprey.Medium.HP;
        } else {
            return Rules.Enemies.AirCrafts.Osprey.Hard.HP;
        }
    }

    @Override
    public float getSpeedBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.AirCrafts.Osprey.Easy.SPEED;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.AirCrafts.Osprey.Medium.SPEED;
        } else {
            return Rules.Enemies.AirCrafts.Osprey.Hard.SPEED;
        }
    }

    @Override
    public float getDamageBySkill(int weapon) {
        return 0;
    }

    @Override
    public int getCoinsBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.AirCrafts.Osprey.Easy.COINS;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.AirCrafts.Osprey.Medium.COINS;
        } else {
            return Rules.Enemies.AirCrafts.Osprey.Hard.COINS;
        }
    }

    /**
     * @return The paratroopers emit interval in seconds according to skill.
     */
    @Override
    public float getSpecificBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.AirCrafts.Osprey.Easy.SECONDS_TO_CREATE_PARATROOPER;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.AirCrafts.Osprey.Medium.SECONDS_TO_CREATE_PARATROOPER;
        } else {
            return Rules.Enemies.AirCrafts.Osprey.Hard.SECONDS_TO_CREATE_PARATROOPER;
        }
    }
}
