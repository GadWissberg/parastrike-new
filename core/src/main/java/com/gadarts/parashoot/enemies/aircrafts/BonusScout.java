package com.gadarts.parashoot.enemies.aircrafts;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.Rules;

/**
 * The Bonus Scout enemy.
 */
public class BonusScout extends Scout {

    public BonusScout() {
        super();
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.AirCrafts.BONUS_SCOUT_DATA_FILE);
        gibReach = Rules.Enemies.AirCrafts.BonusScout.GIB_REACH;
        motorSoundFileName = SFX.Enemies.AirCrafts.PLANE_FLY;
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
        super.init(x, y, direction, getSpeedBySkill(), getHealthBySkill(), false, mechanics, playerHandler, factories);
        TASK_CREATE_PARATROOPER.cancel();
    }

    @Override
    public int getCoinsBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.AirCrafts.BonusScout.Easy.COINS;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.AirCrafts.BonusScout.Medium.COINS;
        } else {
            return Rules.Enemies.AirCrafts.BonusScout.Hard.COINS;
        }
    }

    @Override
    public float getHealthBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.AirCrafts.BonusScout.Easy.HP;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.AirCrafts.BonusScout.Medium.HP;
        } else {
            return Rules.Enemies.AirCrafts.BonusScout.Hard.HP;
        }
    }

    @Override
    public float getSpeedBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.AirCrafts.BonusScout.Easy.SPEED;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.AirCrafts.BonusScout.Medium.SPEED;
        } else {
            return Rules.Enemies.AirCrafts.BonusScout.Hard.SPEED;
        }
    }

    /**
     * Completely overridden method. The Bonus Scout will always gib, so this is redundant.
     */
    @Override
    protected void onDeath() {
        //Do nothing.
    }

    /**
     * Calls super.onGib() and generates a bonus.
     */
    @Override
    protected void onGib() {
        super.onGib();
        generateBonus();
        rewardPlayer();
    }

    /**
     * Completely overridden method. The Bonus Scout will always gib, so this is redundant.
     */
    @Override
    protected void giveCoinsRespectivelyOnGib() {
        //Do nothing.
    }
}
