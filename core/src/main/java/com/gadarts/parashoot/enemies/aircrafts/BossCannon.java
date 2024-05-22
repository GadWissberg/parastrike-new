package com.gadarts.parashoot.enemies.aircrafts;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.enemies.AirCraft;
import com.gadarts.parashoot.level_model.Level;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.player.bunker.Bunker;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;

import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.InGame.Enemies.AirCrafts.BOSS_DATA_FILE;

/**
 * The boss's cannon.
 */
public class BossCannon extends AirCraft {

    private float bodyRelativeX;
    private float bodyRelativeY;

    BossCannon() {
        super();
        atlas = Parastrike.getAssetsManager().get(BOSS_DATA_FILE);
        setShieldRegion(atlas.findRegion(Assets.GFX.Sheets.ImagesNames.BOSS_SHIELD));
        TextureRegion shieldRegion = getShieldRegion();
        int shieldRelativeX = -shieldRegion.getRegionWidth() / 2;
        int shieldRelativeY = -shieldRegion.getRegionHeight() / 2;
        setShieldRelativePosition(shieldRelativeX, shieldRelativeY);
    }

    @Override
    protected void onWaitingForNewOrder(float aux) {

    }

    /**
     * Poolable initialization method.
     *
     * @param direction     Starting direction (degrees).
     * @param mechanics     The war screen's mechanics.
     * @param playerHandler The war screen's player handler.
     * @param factories     The war screen's factories.
     */
    public void init(float direction, WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.BOSS_GUN, 0, 0, direction, 0, getHealthBySkill(), mechanics, playerHandler, factories);
        customMaskWidth = Rules.Enemies.AirCrafts.Boss.Cannon.CANNON_MASK_SIZE;
        customMaskHeight = Rules.Enemies.AirCrafts.Boss.Cannon.CANNON_MASK_SIZE;
        fitSpriteToDirection = true;
        setOrigin(getWidth() / 2, 2 * getHeight() / 3);
    }

    @Override
    public void onOutside(Level currentLevel) {
        //Do nothing.
    }

    @Override
    public Rectangle setPosition(float x, float y) {
        if (x != getX() || y != getY()) {
            Bunker bunker = getPlayerHandler().getBunker();
            setDirection(GameUtils.getDirectionToPoint(this, bunker));
        }
        return super.setPosition(x, y);
    }

    @Override
    public int getCoinsBySkill() {
        int ordinal = Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal();
        if (ordinal == 0) {
            return Rules.Enemies.AirCrafts.Boss.Cannon.Easy.CANNON_COINS;
        } else if (ordinal == 1) {
            return Rules.Enemies.AirCrafts.Boss.Cannon.Medium.CANNON_COINS;
        } else {
            return Rules.Enemies.AirCrafts.Boss.Cannon.Hard.CANNON_COINS;
        }
    }

    @Override
    public float getSpeedBySkill() {
        return 0;
    }

    @Override
    public float getHealthBySkill() {
        int ordinal = Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal();
        if (ordinal == 0) {
            return Rules.Enemies.AirCrafts.Boss.Cannon.Easy.CANNON_HP;
        } else if (ordinal == 1) {
            return Rules.Enemies.AirCrafts.Boss.Cannon.Medium.CANNON_HP;
        } else {
            return Rules.Enemies.AirCrafts.Boss.Cannon.Hard.CANNON_HP;
        }
    }

    @Override
    public float getDamageBySkill(int weapon) {
        return 0;
    }

    @Override
    public float getSpecificBySkill() {
        return 0;
    }

    public void setBodyRelativeX(float bodyRelativeX) {
        this.bodyRelativeX = bodyRelativeX;
    }

    public void setBodyRelativeY(float bodyRelativeY) {
        this.bodyRelativeY = bodyRelativeY;
    }

    public float getBodyRelativeX() {
        return bodyRelativeX;
    }

    public float getBodyRelativeY() {
        return bodyRelativeY;
    }
}
