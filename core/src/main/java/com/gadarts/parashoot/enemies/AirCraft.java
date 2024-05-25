package com.gadarts.parashoot.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.*;
import com.gadarts.parashoot.model.object_factories.BonusFactory;
import com.gadarts.parashoot.model.object_factories.EnemyFactory;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;
import com.gadarts.parashoot.weapons.BulletType;
import com.gadarts.parashoot.weapons.ShockWave;

import static com.gadarts.parashoot.assets.Assets.Configs.ParticleEffects.SMALL_BLAST_RING;

/**
 * Represents air-crafts enemies.
 */
public abstract class AirCraft extends Enemy {

    private static final Color auxColor = new Color();
    /**
     * The interval between paratrooper release.
     */
    protected float paratrooperReleaseDelay;
    /**
     * This AC's image direction delta in crash (degrees). CRASH_DEGREES_CHANGE on default.
     */
    protected float crashDegreesChange = Rules.Enemies.AirCrafts.CRASH_DEGREES_CHANGE;
    /**
     * This AC's id of it's crash sound. -1 if it doesn't have a crash sound.
     */
    private long crashSoundId;
    /**
     * This AC's file name of it's crash sound. PLANE_CRASH on default.
     */
    private SFX crashSoundFileName = SFX.Enemies.AirCrafts.PLANE_CRASH;
    /**
     * A flag to indicate whether this AC has already generated a bonus.
     */
    private boolean generatedBonus;

    /**
     * Poolable initialization method. Initializing fields, sets origin point to it's center and sets it's animation frame duration to PROPELLER_FRAME_DURATION.
     *
     * @param spriteName    The name of the beginning region.
     * @param x             Starting X.
     * @param y             Starting Y.
     * @param direction     Starting direction.
     * @param speed         Starting speed.
     * @param hp            Starting health points.
     * @param mechanics     The war screen's mechanics.
     * @param playerHandler The war screen's player handler.
     */
    protected void init(String spriteName, float x, float y, float direction, float speed, float hp, WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        super.init(spriteName, x, y, direction, speed, hp, mechanics, playerHandler, factories);
        initializeFields();
    }

    /**
     * Initializes fields when obtained from pool or created.
     */
    private void initializeFields() {
        setOrigin(width / 2, height / 2);
        crashSoundId = -1;
        setBeenInside(false);
        fitSpriteToDirection = false;
        animation.setFrameDuration(Rules.Enemies.AirCrafts.PROPELLER_FRAME_DURATION);
        startingOutside = true;
        generatedBonus = false;
        landed = false;
    }

    /**
     * Whatever this AC should do while this order fulfillment is in process.
     * The order is fulfilled at the moment this AC reaches a distance lower than the destinationReachRadius field value.
     * On default it updates the speed, position, aims to the order's destination.
     *
     * @param optimalSpeed New speed value.
     */
    @Override
    protected void onFulfillingOrder(float optimalSpeed) {
        this.speed = optimalSpeed;
        aimToDestination();
        checkWhetherOrderFulfilled();
    }

    /**
     * Calls {@link Enemy#onOrderFulfilled()} if destination reached.
     */
    private void checkWhetherOrderFulfilled() {
        float destinationX = order.destinationX;
        float destinationY = order.destinationY;
        if (x > destinationX - destinationReachRadius && x < destinationX + destinationReachRadius) {
            if (y > destinationY - destinationReachRadius && y < destinationY + destinationReachRadius)
                onOrderFulfilled();
        }
    }

    /**
     * Collision with bullet definition.
     * If the bullet was not created by a player character, than nothing would happen.
     * Else, this AC would take damage by this bullet.
     *
     * @param bullet The colliding bullet.
     */
    @Override
    public void onCollision(Bullet bullet) {
        boolean shieldActive = isShieldActive();
        if (!bullet.isPlayerBullet() || shieldActive) {
            if (shieldActive)
                getElements().getEffectsManager().createEffect(SMALL_BLAST_RING, bullet);
            return;
        }
        handleBulletHitDamage(bullet);
    }

    private void handleBulletHitDamage(Bullet bullet) {
        if (getPlayerHandler().getTurret().getCurrentWeaponIndex() == BulletType.SHOCK_WAVE && bullet instanceof ShockWave) {
            ShockWave shockWave = (ShockWave) bullet;
            if (!shockWave.hasHit())
                collisionWithShockWave(bullet, shockWave);
        } else changeHealth(-bullet.getDamageValue());
    }

    /**
     * Creates flying parts and gets damage.
     *
     * @param bullet    The shockwave bullet.
     * @param shockWave The shockwave itself.
     */
    private void collisionWithShockWave(Bullet bullet, ShockWave shockWave) {
        if (isShieldActive()) {
            getElements().getEffectsManager().createEffect(SMALL_BLAST_RING, bullet);
            return;
        }
        getFactories().getMiscFactory().createFlyingParts(MiscFactory.MiscType.FLYING_PART, x, y, 3);
        shockWave.setHit(true);
        changeHealth(-bullet.getDamageValue());
    }

    @Override
    public void collisionInteraction(CollideableGameObject gameObject) {
        gameObject.onCollision(this);
    }

    /**
     * Orders this AC to fly away to outside the screen(to sky).
     */
    protected void flyAway() {
        setOrder(new Order(Rules.System.Resolution.WIDTH_TARGET_RESOLUTION * randomizer.nextFloat(), Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION + Rules.Enemies.AirCrafts.FLY_AWAY_DISTANCE_ABOVE_SKY));
    }

    /**
     * When this AC dies, it would crash. Score and coins are given according to it's worth.
     */
    @Override
    protected void onDeath() {
        super.onDeath();
        manageFallingWhenDead();
        getFactories().getMiscFactory().createExplosion(MiscFactory.ExplosionType.EXPLOSION, getCenterX(), getCenterY());
        getElements().getEffectsManager().createEffect(Assets.Configs.ParticleEffects.SMOKE, this);
        manageGiftsWhenDead();
    }

    /**
     * Creates bonus, score and coins.
     */
    private void manageGiftsWhenDead() {
        if (MathUtils.randomBoolean(Rules.Enemies.AirCrafts.BONUS_CHANCES)) {
            generateBonus();
        }
        rewardPlayer();
    }

    /**
     * The air-craft would turn into a falling craft.
     */
    private void manageFallingWhenDead() {
        crashSoundId = Parastrike.getSoundPlayer().playSound(crashSoundFileName);
        setRegion(Assets.GFX.Sheets.ImagesNames.FALLING);
        Parastrike.getSoundPlayer().playSound(SFX.Misc.HIT);
        gravityStatus = true;
        speed += Rules.Enemies.AirCrafts.SINK_SPEED_ACCELERATION;
        faceRegion();
    }

    /**
     * Would generate a random bonus (only allowed bonuses according to current level) if it didn't generate any bonus yet. This won't generate a bonus if the selected bonus is a side-kick upgrade and no side-kicks are selected and if the selected bonus is  ammo and cannon-ball is selected.
     */
    protected void generateBonus() {
        if (generatedBonus) return;
        generatedBonus = true;
        BonusFactory bonusFactory = getFactories().getBonusFactory();
        if (GameSettings.ENEMY_TESTING) {
            bonusFactory.createBonus(BonusFactory.BonusType.values()[MathUtils.random(BonusFactory.BonusType.values().length - 1)], x, y);
            return;
        }
        Array<BonusFactory.BonusType> allowedBonuses = getElements().getCurrentLevel().getAllowedBonuses();
        if (allowedBonuses.size == 0) {
            return;
        }
        BonusFactory.BonusType bonus = allowedBonuses.get(MathUtils.random(allowedBonuses.size - 1));
        if (!(bonus == BonusFactory.BonusType.SIDEKICK_UPGRADE && Parastrike.getPlayerStatsHandler().getSelectedSideKick() == null) && !(bonus == BonusFactory.BonusType.AMMO && Parastrike.getPlayerStatsHandler().getSelectedCannon() == BulletType.CANNON_BALL)) {
            bonusFactory.createBonus(bonus, x, y);
        }
    }

    /**
     * Draws the current region in the given color.
     *
     * @param batch The current used batch.
     * @param color The color to colorize in.
     */
    protected void drawColoredSprite(SpriteBatch batch, Color color) {
        if (currentFrame != null && isVisible()) {
            Color originalColor = auxColor.set(batch.getColor());
            batch.setColor(color);
            batch.draw(currentFrame, x - getOriginX(), y - getOriginY(), getOriginX(), getOriginY(), currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), scaleX, scaleY, getSpriteDirection());
            batch.setColor(originalColor);
        }
    }

    /**
     * Would handle the slow image rotation when crashing.
     *
     * @param delta
     */
    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (gravityStatus) {
            if (isFacingLeft()) {
                setSpriteDirection(spriteDirection + crashDegreesChange);
            } else {
                setSpriteDirection(spriteDirection - crashDegreesChange);
            }
        }
    }

    /**
     * Would explode when colliding the ground.
     */
    @Override
    protected void onGroundCollision() {
        super.onGroundCollision();
        createExplosions();
        onDestroy();
    }

    /**
     * Would explode and give half of it's score and coins worth.
     */
    @Override
    protected void onGib() {
        giveCoinsRespectivelyOnGib();
        super.onGib();
        createExplosions();
    }

    /**
     * Gives half of the coin's worth if the air-craft is gibbed when dead and gives coin's worth plus half when gibbed while alive.
     */
    protected void giveCoinsRespectivelyOnGib() {
        int coinsToGive = coinsWorth / 2;
        if (!isDead()) coinsToGive += coinsWorth;
        getElements().getScoresHandler().addCoins(coinsToGive, getX(), getY());
    }

    /**
     * Creates few explosion and flying parts in a random positions near it.
     */
    protected void createExplosions() {
        MiscFactory miscFactory = getFactories().getMiscFactory();
        miscFactory.createFlyingParts(MiscFactory.MiscType.BIG_SCOUT_PART, x, y, 4);
        miscFactory.createFlyingParts(MiscFactory.MiscType.FLYING_PART, x, y, 4);
        Parastrike.getSoundPlayer().playSound(SFX.Misc.BIG_EXPLOSION);
        if (y <= Rules.Level.GROUND_Y) {
            miscFactory.createExplosion(MiscFactory.ExplosionType.BIG_EXPLOSION_UP, getX(), Rules.Level.GROUND_Y);
        } else {
            miscFactory.createExplosion(MiscFactory.ExplosionType.EXPLOSION, getX(), getY());
        }
        miscFactory.createExplosion(MiscFactory.ExplosionType.EXPLOSION, x - getWidth() / 2, y);
        miscFactory.createExplosion(MiscFactory.ExplosionType.EXPLOSION, x + getWidth() / 2, y);
        miscFactory.createExplosion(MiscFactory.ExplosionType.EXPLOSION, x, y, width, height);
    }

    /**
     * Stops motor and crash sound if they are played. Creates explosions in it's destroy sequence.
     *
     * @param doDestroySequence Whether to perform it's sequence of actions when destroyed.
     */
    @Override
    public void onDestroy(boolean doDestroySequence) {
        super.onDestroy(doDestroySequence);
        Parastrike.getSoundPlayer().stopSound(crashSoundFileName, crashSoundId);
        stopMotorSound();
        if (doDestroySequence) {
            createExplosions();
        }
    }

    /**
     * Releases a paratrooper and reschedules it-self.
     */
    protected final Timer.Task TASK_CREATE_PARATROOPER = new Timer.Task() {
        @Override
        public void run() {
            if (!dead && !isOutside()) {
                EnemyFactory.EnemyType randomParatrooper = getElements().getCurrentLevel().getAllowedParatroopers().random();
                getFactories().getEnemyFactory().createEnemy(randomParatrooper, getX(), getY());
                scheduleTask(TASK_CREATE_PARATROOPER, paratrooperReleaseDelay);
            }
        }
    };

}
