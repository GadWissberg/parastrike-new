package com.gadarts.parashoot.enemies.aircrafts;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.AirCraft;
import com.gadarts.parashoot.enemies.Order;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;
import com.gadarts.parashoot.weapons.BulletType;

/**
 * The Zeppelin enemy.
 */
public class Zeppelin extends AirCraft {

    private boolean attacking; // A flag to indicate whether the zeppelin is in attack mode.

    public Zeppelin() {
        super();
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.AirCrafts.ZEPPELIN_DATA_FILE);
        gibReach = Rules.Enemies.AirCrafts.Zeppelin.GIB_REACH;
        gravityAcceleration = Rules.Enemies.AirCrafts.Zeppelin.BOMB_GRAVITY_ACCELERATION;
        maxFallingSpeed = Rules.Enemies.AirCrafts.Zeppelin.BOMB_MAX_FALLING_SPEED;
        crashDegreesChange = 0;
        motorSoundFileName = SFX.Enemies.AirCrafts.BALLISTIC_FLY;
    }

    /**
     * Poolable initialization method. Orders the zeppelin to fly above player.
     *
     * @param x             Starting X.
     * @param y             Starting Y.
     * @param direction     the zeppelin's direction.
     * @param mechanics     The war screen's mechanics.
     * @param playerHandler The war screen's player handler.
     * @param factories     The war screen's factories.
     */
    public void init(float x, float y, float direction, WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.IDLE, x, y, direction, Rules.Enemies.AirCrafts.Zeppelin.SPEED, getHealthBySkill(), mechanics, playerHandler, factories);
        faceRegion();
        this.speed = 0;
        setOrder(ORDER_FLY_TO_ABOVE_PLAYER);
    }


    @Override
    public int getCoinsBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.AirCrafts.Zeppelin.Easy.COINS;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.AirCrafts.Zeppelin.Medium.COINS;
        }
        return Rules.Enemies.AirCrafts.Zeppelin.Hard.COINS;
    }

    /**
     * Completely overridden method. The zeppelin fulfills his order by moving to the destination X. On arrival the order is fulfilled.
     */
    @Override
    protected void onFulfillingOrder(float optimalSpeed) {
        speedUp(optimalSpeed, Rules.Enemies.GroundUnits.GroundCrafts.SPEED_DELTA);
        float destination = order.destinationX;
        if (x > destination - 1 && x < destination + 1) {
            onOrderFulfilled();
        }
    }

    /**
     * Completely overridden method because the damage smoke has to positioned on the zeppelin's center.
     */
    @Override
    protected void createDamageSmoke() {
        if (myDamageSmoke == null) {
            myDamageSmoke = getElements().getEffectsManager().createEffect(Assets.Configs.ParticleEffects.DAMAGE_SMOKE, this, 0, 0);
        }
    }

    /**
     * Completely overridden method. The zeppelin's dead and falls down, creating random explosions.
     */
    @Override
    protected void onDeath() {
        setDeadFlag(true);
        stopMotorSound();
        setRegion(Assets.GFX.Sheets.ImagesNames.FALLING);
        Parastrike.getSoundPlayer().playSound(SFX.Misc.HIT);
        gravityStatus = true;
        faceRegion();
        TASK_CREATE_RANDOM_EXPLOSION.run();
        WarScreenElements mechanics = getElements();
        mechanics.getEffectsManager().createEffect(Assets.Configs.ParticleEffects.SMOKE, this);
        if (MathUtils.randomBoolean(Rules.Enemies.AirCrafts.BONUS_CHANCES)) {
            generateBonus();
        }
        mechanics.getScoresHandler().addScore(1);
        mechanics.getScoresHandler().addCoins(coinsWorth, getX(), getY());
    }

    /**
     * Completely overridden method. When the zeppelin has no order, it'll slow down 'till it has completely stopped.
     * @param delta
     */
    @Override
    protected void onWaitingForNewOrder(float delta) {
        slowDownSpeed(Rules.Enemies.GroundUnits.GroundCrafts.SPEED_DELTA);
    }

    /**
     * When the zeppelin is in the center of the screen, it'll start attacking.
     * @param delta
     */
    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        int centerOfScreen = Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2;
        if ((x > centerOfScreen - Rules.Enemies.AirCrafts.Zeppelin.ATTACK_REGION && x < centerOfScreen + Rules.Enemies.AirCrafts.Zeppelin.ATTACK_REGION) && !dead && !attacking) {
            attacking = true;
            scheduleTask(TASK_ATTACK, 0);
        } else {
            attacking = false;
        }
    }

    /**
     * Start's a chain of random explosions.
     */
    private final Timer.Task TASK_CREATE_RANDOM_EXPLOSION = new Timer.Task() {
        @Override
        public void run() {
            if (!destroyMeFlag) {
                getFactories().getMiscFactory().createExplosion(MiscFactory.ExplosionType.EXPLOSION, x + randomizer.nextFloat() * width - width / 2, y + randomizer.nextFloat() * height - height / 2);
                scheduleTask(TASK_CREATE_RANDOM_EXPLOSION, randomizer.nextFloat() * Rules.Enemies.AirCrafts.Zeppelin.RANDOM_EXPLOSION_MAX_TIME);
                Parastrike.getSoundPlayer().playSound(SFX.Misc.EXPLOSION);
            }
        }
    };

    @Override
    public float getSpeedBySkill() {
        return Rules.Enemies.AirCrafts.Zeppelin.SPEED;
    }

    @Override
    public float getHealthBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.AirCrafts.Zeppelin.Easy.HP;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.AirCrafts.Zeppelin.Medium.HP;
        } else {
            return Rules.Enemies.AirCrafts.Zeppelin.Hard.HP;
        }
    }

    @Override
    public float getDamageBySkill(int weapon) {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.AirCrafts.Zeppelin.Easy.DAMAGE_VALUE;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.AirCrafts.Zeppelin.Medium.DAMAGE_VALUE;
        } else {
            return Rules.Enemies.AirCrafts.Zeppelin.Hard.DAMAGE_VALUE;
        }
    }

    @Override
    public float getSpecificBySkill() {
        return 0;
    }

    /**
     * Will fly away, out of the screen.
     */
    private final Timer.Task TASK_FLY_AWAY = new Timer.Task() {
        @Override
        public void run() {
            if (isFacingLeft()) {
                setOrder(ORDER_FLY_AWAY_LEFT);
            } else {
                setOrder(ORDER_FLY_AWAY_RIGHT);
            }
        }
    };

    private final Order ORDER_FLY_TO_ABOVE_PLAYER = new Order(Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, TASK_FLY_AWAY, Rules.Enemies.AirCrafts.Zeppelin.WAIT_ABOVE_PLAYER_DURATION);
    private static final Order ORDER_FLY_AWAY_LEFT = new Order(-Rules.Enemies.AirCrafts.Zeppelin.OUTSIDE_DISTANCE_DESTINATION, 0, 0);
    private static final Order ORDER_FLY_AWAY_RIGHT = new Order(Rules.System.Resolution.WIDTH_TARGET_RESOLUTION + Rules.Enemies.AirCrafts.Zeppelin.OUTSIDE_DISTANCE_DESTINATION, 0, 0);

    /**
     * Will start a chain of attack, dropping bombs.
     */
    private final Timer.Task TASK_ATTACK = new Timer.Task() {
        @Override
        public void run() {
            if (TASK_ATTACK.isScheduled()) {
                return;
            }
            if (attacking && !dead && !getTarget().isDead()) {
                scheduleTask(TASK_ATTACK, Rules.Enemies.AirCrafts.Zeppelin.ATTACK_RELOAD);
                scheduleTask(TASK_CHANGE_TO_IDLE, Rules.Enemies.AirCrafts.Zeppelin.ATTACK_IMAGE_DURATION);
                float x;
                if (isFacingLeft()) {
                    x = getX() + 20;
                } else {
                    x = getX() - 20;
                }
                Bullet bullet = getFactories().getBulletFactory().createBullet(BulletType.ZEPPELIN_BOMB, x, y - 30, 0);
                bullet.setDirection(direction);
                bullet.setDamageValue(getDamageBySkill(0));
                setRegion(Assets.GFX.Sheets.ImagesNames.SHOOT, Rules.Enemies.AirCrafts.PROPELLER_FRAME_DURATION);
            }
        }
    };

}

