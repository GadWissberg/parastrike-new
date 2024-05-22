package com.gadarts.parashoot.enemies.aircrafts;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.AirCraft;
import com.gadarts.parashoot.enemies.Order;
import com.gadarts.parashoot.misc.effects.ParticleWrapper;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.player.PlayerCharacter;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;

/**
 * The Ballistic Rocket enemy.
 */
public class Ballistic extends AirCraft {
    /**
     * The smoke which is emitted.
     */
    private ParticleWrapper mySmoke;

    /**
     * The WOOSH sound id when it falls.
     */
    private long myFallingSoundId;

    public Ballistic() {
        super();
        fieldsInitializationOnConstruction();
    }

    /**
     * Initializing the Ballistic fields when the object is created.
     */
    private void fieldsInitializationOnConstruction() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.AirCrafts.YAK_DATA_FILE);
        destinationReachRadius = Rules.Enemies.AirCrafts.Ballistic.DISTANCE_FROM_DESTINATION_REACH;
        gravityAcceleration = Rules.Enemies.AirCrafts.Ballistic.GRAVITY_ACCELERATION;
        rotationChange = Rules.Enemies.AirCrafts.Ballistic.ROTATION_CHANGE;
        attackDamage = getDamageBySkill(0);
        motorSoundFileName = SFX.Enemies.AirCrafts.BALLISTIC_FLY;
    }

    /**
     * Poolable initialization method.
     *
     * @param x         Starting X.
     * @param y         Starting Y.
     * @param direction Starting direction (degrees).
     * @param mechanics The war screen's mechanics.
     */
    public void init(float x, float y, float direction, WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.BALLISTIC, x, y, direction, getSpeedBySkill(), getHealthBySkill(), mechanics, playerHandler, factories);
        float randomHeight = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - (randomizer.nextFloat() * Rules.Enemies.AirCrafts.Ballistic.MAXIMUM_DELTA_HEIGHT);
        fitSpriteToDirection = true;
        order = new Order(Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, randomHeight);
        directionToDestination = GameUtils.getDirectionToPoint(x, y, order.destinationX, order.destinationY);
        mySmoke = mechanics.getEffectsManager().createEffect(Assets.Configs.ParticleEffects.BALLISTIC_SMOKE, this);
    }

    /**
     * Completely overridden method. The Ballistic would never generate a bonus.
     */
    @Override
    protected void generateBonus() {
        //Do not generate.
    }

    /**
     * Completely overridden method. The Ballistic would never emit damage smoke.
     */
    @Override
    protected void createDamageSmoke() {
        //Ballistic shouldn't emit smoke when damaged.
    }

    /**
     * The Ballistic would call super.onUpdate() and set the emitted smoke position.
     * @param delta
     */
    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (mySmoke != null) { // Position the smoke to it's ass as long as it is emitted.
            mySmoke.setOrigin((float) (Math.cos(Math.toRadians(spriteDirection)) * -width / 2), (float) (Math.sin(Math.toRadians(spriteDirection)) * -width / 2));
        }
    }

    /**
     * Calls super.onOrderFulfilled, plays the falling sound and stops the motor sound.
     */
    @Override
    protected void onOrderFulfilled() {
        super.onOrderFulfilled();
        myFallingSoundId = Parastrike.getSoundPlayer().playSound(SFX.Enemies.AirCrafts.FALLING_BALLISTIC);
        stopMotorSound();
    }

    /**
     * Completely overridden method. The Ballistic would gib when dead.
     */
    @Override
    protected void onDeath() {
        onGib();
    }

    /**
     * Completely overridden method. The Ballistic would play big explosion sound and create a huge explosion.
     */
    @Override
    protected void createExplosions() {
        Parastrike.getSoundPlayer().playSound(SFX.Misc.BIG_EXPLOSION);
        if (getCenterY() <= Rules.Level.GROUND_Y) {
            getFactories().getMiscFactory().createExplosion(MiscFactory.ExplosionType.HUGE_EXPLOSION_UP, getX(), Rules.Level.GROUND_Y);
        } else {
            getFactories().getMiscFactory().createExplosion(MiscFactory.ExplosionType.HUGE_EXPLOSION, getX(), getY());
        }
    }

    /**
     * Calls super.onGib() and destroys the Ballistic.
     */
    @Override
    protected void onGib() {
        super.onGib();
        destroySmoke();
        onDestroy();
        Parastrike.getSoundPlayer().stopSound(SFX.Enemies.AirCrafts.FALLING_BALLISTIC, myFallingSoundId);
    }

    /**
     * Calls super.onDestroy() and stops the falling ballistic sound is destroy sequence on.
     */
    @Override
    public void onDestroy(boolean doDestroySequence) {
        super.onDestroy(doDestroySequence);
        if (doDestroySequence) {
            Parastrike.getSoundPlayer().stopSound(SFX.Enemies.AirCrafts.FALLING_BALLISTIC, myFallingSoundId);
        }
    }

    /**
     * Completely overridden method. The Ballistic would slow down and fall.
     * @param delta
     */
    @Override
    protected void onWaitingForNewOrder(float delta) {
        slowDownSpeed(GameUtils.realSpeed(Rules.Enemies.AirCrafts.Ballistic.DECELERATION, delta));
        gravityStatus = true;
        setSpriteDirectionToMovement();
        destroySmoke();
    }

    /**
     * Destroys the emitted smoke.
     * Render type: once.
     */
    private void destroySmoke() {
        if (mySmoke != null) {
            if (mySmoke.isComplete()) {
                mySmoke.destroy();
                mySmoke = null;
            } else {
                mySmoke.allowCompletion();
            }
        }
    }

    @Override
    public float getSpeedBySkill() {
        return Rules.Enemies.AirCrafts.Ballistic.SPEED;
    }

    @Override
    public void onCollision(PlayerCharacter other) {
        setHealth(0);
    }

    @Override
    public float getHealthBySkill() {
        int ordinal = Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal();
        if (ordinal == 0) {
            return Rules.Enemies.AirCrafts.Ballistic.Easy.HP;
        } else if (ordinal == 1) {
            return Rules.Enemies.AirCrafts.Ballistic.Medium.HP;
        } else {
            return Rules.Enemies.AirCrafts.Ballistic.Hard.HP;
        }
    }

    @Override
    public int getCoinsBySkill() {
        int ordinal = Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal();
        if (ordinal == 0) {
            return Rules.Enemies.AirCrafts.Ballistic.Easy.COINS;
        } else if (ordinal == 1) {
            return Rules.Enemies.AirCrafts.Ballistic.Medium.COINS;
        } else {
            return Rules.Enemies.AirCrafts.Ballistic.Hard.COINS;
        }
    }

    @Override
    public float getDamageBySkill(int weapon) {
        int ordinal = Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal();
        if (ordinal == 0) {
            return Rules.Enemies.AirCrafts.Ballistic.Easy.ATTACK_DAMAGE;
        } else if (ordinal == 1) {
            return Rules.Enemies.AirCrafts.Ballistic.Medium.ATTACK_DAMAGE;
        } else {
            return Rules.Enemies.AirCrafts.Ballistic.Hard.ATTACK_DAMAGE;
        }
    }

    @Override
    public float getSpecificBySkill() {
        return 0;
    }
}
