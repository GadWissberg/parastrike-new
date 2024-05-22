package com.gadarts.parashoot.enemies.paratroopers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.misc.Misc;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.Rules;

/**
 * Paratrooper's parachute.
 */
public class Parachute extends Misc {
    /**
     * Whether the parachute is torn.
     */
    private boolean torn;

    public Parachute() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.GroundUnits.Paratroopers.PARACHUTE_DATA_FILE);
    }

    /**
     * Poolable initialization method.
     *
     * @param elements The war screen's elements.
     */
    public void init(WarScreenElements elements) {
        super.init(Assets.GFX.Sheets.ImagesNames.PARACHUTE, 0, 0, 0, 0, elements);
        gravityAcceleration = Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.GRAVITY_ACCELERATION;
        fitSpriteToDirection = false;
        setOrigin(getWidth() / 2, 0);
        visible = false;
        torn = false;
    }

    /**
     * Handles the parachute's behaviour according to it's animation.
     * @param delta
     */
    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (animation.isAnimationFinished(animationStateTime)) {
            if (currentFrame.name.equals(Assets.GFX.Sheets.ImagesNames.PARACHUTE_OPENS)) {
                setRegion(Assets.GFX.Sheets.ImagesNames.PARACHUTE);
            } else if (currentFrame.name.equals(Assets.GFX.Sheets.ImagesNames.PARACHUTE_LANDS) || currentFrame.name.equals(Assets.GFX.Sheets.ImagesNames.PARACHUTE_DEAD)) {
                onDestroy(true);
            }
        }
    }

    /**
     * Completely overridden method. If the parachute is not attached to it's paratrooper, it would fall to the ground.
     * @param delta
     */
    @Override
    protected void fall(float delta) {
        if (y <= Rules.Level.GROUND_Y) {
            setY(Rules.Level.GROUND_Y);
            speed = 0;
            fallingSpeed = 0;
            setRegion(Assets.GFX.Sheets.ImagesNames.PARACHUTE_LANDS, Rules.Enemies.GroundUnits.Paratroopers.Parachute.OPEN_ANIMATION_SPEED, Animation.PlayMode.NORMAL);
        } else {
            if (gravityStatus) {
                setY(y - fallingSpeed);
                if (fallingSpeed < maxFallingSpeed) {
                    fallingSpeed += gravityAcceleration;
                }
            }
        }
    }

    /**
     * Plays the open animation. Called few moments after the paratrooper's jump from the air-craft.
     */
    public void openUp() {
        visible = true;
        setRegion(Assets.GFX.Sheets.ImagesNames.PARACHUTE_OPENS, Rules.Enemies.GroundUnits.Paratroopers.Parachute.OPEN_ANIMATION_SPEED);
    }

    /**
     * Sets the landing animation and activates gravity.
     */
    public void land() {
        setRegion(Assets.GFX.Sheets.ImagesNames.PARACHUTE_LANDS, Rules.Enemies.GroundUnits.Paratroopers.Parachute.OPEN_ANIMATION_SPEED, Animation.PlayMode.NORMAL);
        gravityStatus = true;
        fallingSpeed = Rules.Enemies.GroundUnits.Paratroopers.Parachute.LAND_STARTING_GRAVITY_SPEED;
    }

    /**
     * Activates gravity and plays torn animation.
     */
    public void tear() {
        if (torn) {
            return;
        }
        gravityStatus = true;
        fallingSpeed = Rules.Enemies.GroundUnits.Paratroopers.Parachute.TORN_STARTING_GRAVITY_SPEED;
        setRegion(Assets.GFX.Sheets.ImagesNames.PARACHUTE_DEAD, Rules.Enemies.GroundUnits.Paratroopers.Parachute.OPEN_ANIMATION_SPEED, Animation.PlayMode.NORMAL);
        torn = true;
    }

    /**
     * @return Whether parachute is torn.
     */
    public boolean isTorn() {
        return torn;
    }
}
