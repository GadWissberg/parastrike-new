package com.gadarts.parashoot.enemies.aircrafts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
 * The Scout and Basic Scout enemy.
 */
public class Scout extends AirCraft {

    private static final Color BASIC_SCOUT_COLOR = new Color(0.5f, 1f, 0.7f, 1); // The color of the basic scout.
    private boolean basicScout; // Whether this scout is basic or not.

    Scout() {
        super();
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.AirCrafts.SCOUT_DATA_FILE);
        gibReach = Rules.Enemies.AirCrafts.Scout.GIB_REACH;
        motorSoundFileName = SFX.Enemies.AirCrafts.PLANE_FLY;
    }

    /**
     * Poolable initialization method.
     *
     * @param x             Starting X.
     * @param y             Starting Y.
     * @param direction     Starting direction (degrees).
     * @param basicScout    Whether the scout is basic or not.
     * @param mechanics     The war screen's mechanics.
     * @param playerHandler The war screen's player handler.
     * @param factories     The war screen's factories.
     */
    public void init(float x, float y, float direction, boolean basicScout, WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        this.basicScout = basicScout;
        init(x, y, direction, getSpeedBySkill(), getHealthBySkill(), basicScout, mechanics, playerHandler, factories);
    }

    /**
     * Poolable initialization method.
     *
     * @param x             Starting X.
     * @param y             Starting Y.
     * @param direction     Starting direction (degrees).
     * @param speed         Starting speed.
     * @param hp            Starting hp.
     * @param mechanics     The war screen's mechanics.
     * @param playerHandler The war screen's player handler.
     * @param factories     The war screen's factories.
     */
    protected void init(float x, float y, float direction, float speed, float hp, boolean basicScout, WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.IDLE, x, y, direction, speed, hp, mechanics, playerHandler, factories);
        this.basicScout = basicScout;
        faceRegion();
        if (GameSettings.ALLOW_PARATROOPERS && !basicScout) {
            paratrooperReleaseDelay = getSpecificBySkill();
            scheduleTask(TASK_CREATE_PARATROOPER, paratrooperReleaseDelay);
        }
    }

    /**
     * Draws the scout whether it is basic or not.
     *
     * @param batch The painter's batch.
     */
    @Override
    public void onDraw(SpriteBatch batch) {
        if (basicScout) {
            drawColoredSprite(batch, BASIC_SCOUT_COLOR);
        } else {
            super.onDraw(batch);
        }
    }

    /**
     * Completely overridden method. This isn't relevant to the Scouts.
     * @param delta
     */
    @Override
    protected void onWaitingForNewOrder(float delta) {
        // Do nothing.
    }

    @Override
    public int getCoinsBySkill() {
        if (basicScout) {
            if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
                return Rules.Enemies.AirCrafts.BasicScout.Easy.COINS;
            } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
                return Rules.Enemies.AirCrafts.BasicScout.Medium.COINS;
            } else {
                return Rules.Enemies.AirCrafts.BasicScout.Hard.COINS;
            }
        } else {
            if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
                return Rules.Enemies.AirCrafts.Scout.Easy.COINS;
            } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
                return Rules.Enemies.AirCrafts.Scout.Medium.COINS;
            } else {
                return Rules.Enemies.AirCrafts.Scout.Hard.COINS;
            }
        }
    }

    @Override
    public float getSpeedBySkill() {
        if (basicScout) {
            if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
                return Rules.Enemies.AirCrafts.BasicScout.Easy.SPEED;
            } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
                return Rules.Enemies.AirCrafts.BasicScout.Medium.SPEED;
            } else {
                return Rules.Enemies.AirCrafts.BasicScout.Hard.SPEED;
            }
        } else {
            if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
                return Rules.Enemies.AirCrafts.Scout.Easy.SPEED;
            } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
                return Rules.Enemies.AirCrafts.Scout.Medium.SPEED;
            } else {
                return Rules.Enemies.AirCrafts.Scout.Hard.SPEED;
            }
        }
    }

    @Override
    public float getHealthBySkill() {
        int ordinal = Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal();
        if (basicScout) {
            if (ordinal == 0) {
                return Rules.Enemies.AirCrafts.BasicScout.Easy.HP;
            } else if (ordinal == 1) {
                return Rules.Enemies.AirCrafts.BasicScout.Medium.HP;
            } else {
                return Rules.Enemies.AirCrafts.BasicScout.Hard.HP;
            }
        } else {
            if (ordinal == 0) {
                return Rules.Enemies.AirCrafts.Scout.Easy.HP;
            } else if (ordinal == 1) {
                return Rules.Enemies.AirCrafts.Scout.Medium.HP;
            } else {
                return Rules.Enemies.AirCrafts.Scout.Hard.HP;
            }
        }
    }

    @Override
    public float getDamageBySkill(int weapon) {
        return 0;
    }

    /**
     * Returns the paratroopers emit interval in seconds according to skill.
     *
     * @return Seconds value.
     */
    @Override
    public float getSpecificBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.AirCrafts.Scout.Easy.SECONDS_TO_CREATE_PARATROOPER;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.AirCrafts.Scout.Medium.SECONDS_TO_CREATE_PARATROOPER;
        } else {
            return Rules.Enemies.AirCrafts.Scout.Hard.SECONDS_TO_CREATE_PARATROOPER;
        }
    }
}
