package com.gadarts.parashoot.enemies;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.*;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;
import com.gadarts.parashoot.weapons.BulletType;
import com.gadarts.parashoot.weapons.ShockWave;

/**
 * Represents the ground crafts (like APC and tank).
 */
public abstract class GroundCraft extends GroundUnit {
    /**
     * The x value of the beginning position.
     */
    protected float beginX;

    /**
     * Poolable initialization method. Initializing fields.
     *
     * @param x             Starting X.
     * @param y             Starting Y.
     * @param direction     Starting direction.
     * @param hp            Starting health points.
     * @param mechanics     The war screen's mechanics.
     * @param playerHandler The war screen's player handler.
     * @param factories     The war screen's factories.
     */
    protected void init(float x, float y, float direction, float hp, WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.IDLE, x, y, direction, 0, hp, mechanics, playerHandler, factories);
        faceRegion();
        setOrigin(width / 2, height / 2);
        this.y += height / 2;
        beginX = x;
        fitSpriteToDirection = false;
        setBeenInside(false);
        startingOutside = true;
    }

    @Override
    public void collisionInteraction(CollideableGameObject gameObject) {
        gameObject.onCollision(this);
    }

    /**
     * Loses health when colliding a bullet
     *
     * @param bullet
     */
    @Override
    public void onCollision(Bullet bullet) {
        if (getPlayerHandler().getTurret().getCurrentWeaponIndex() == BulletType.SHOCK_WAVE && bullet instanceof ShockWave) {
            ShockWave shockWave = (ShockWave) bullet;
            if (!shockWave.hasHit()) {
                getFactories().getMiscFactory().createFlyingParts(MiscFactory.MiscType.FLYING_PART, x, y, 3);
                changeHealth(-bullet.getDamageValue());
                shockWave.setHit(true);
            }
        } else {
            changeHealth(-bullet.getDamageValue());
        }
    }

    /**
     * Calls {@link GameCharacter#onGib()}, creates explosion and gives score.
     */
    @Override
    protected void onGib() {
        super.onGib();
        Parastrike.getSoundPlayer().playSound(SFX.Misc.BIG_EXPLOSION);
        getFactories().getMiscFactory().createExplosion(MiscFactory.ExplosionType.BIG_EXPLOSION_UP, getX(), Rules.Level.GROUND_Y);
        getElements().getScoresHandler().addCoins(coinsWorth, getX(), getY());
    }

    /**
     * Calls {@link Enemy#onDeath()}, and calls {@link GroundCraft#onGib()} because ground crafts actually only gib.
     */
    @Override
    protected void onDeath() {
        super.onDeath();
        onGib();
    }

    /**
     *
     * Calls {@link Enemy#setOrder(Order)}, and play the motor sound.
     * @param order
     */
    @Override
    public void setOrder(Order order) {
        super.setOrder(order);
        playMotorSound();
    }

    /**
     * Speeds up 'till reaches the optimal speed. Order is fulfilled once reaching it.
     * @param optimalSpeed
     */
    @Override
    protected void onFulfillingOrder(float optimalSpeed) {
        speedUp(optimalSpeed, Rules.Enemies.GroundUnits.GroundCrafts.SPEED_DELTA);
        float destination = order.destinationX;
        if (x > destination - 5 && x < destination + 5) {
            onOrderFulfilled();
        }
    }

    /**
     * Slows down when got nothing to fulfill.
     * @param delta The pace of slow down.
     */
    @Override
    protected void onWaitingForNewOrder(float delta) {
        slowDownSpeed(Rules.Enemies.GroundUnits.GroundCrafts.SPEED_DELTA);
    }

}
