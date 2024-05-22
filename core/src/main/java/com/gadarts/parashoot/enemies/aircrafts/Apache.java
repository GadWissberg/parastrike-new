package com.gadarts.parashoot.enemies.aircrafts;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.AirCraft;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.enemies.Order;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.player.PlayerCharacter;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;
import com.gadarts.parashoot.weapons.BulletType;

/**
 * The Apache enemy.
 */
public class Apache extends AirCraft {
    /**
     * Representing Apache's attacks.
     */
    private enum ApacheAttacks {
        MACHINE_GUN, MISSILE
    }

    /**
     * Whether the Apache is currently attacking.
     */
    private boolean attacking;

    public Apache() {
        super();
        constructionFieldsInitialization();
    }

    /**
     * Initializes Apache's fields on it's object creation.
     */
    private void constructionFieldsInitialization() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.AirCrafts.APACHE_DATA_FILE);
        gibReach = Rules.Enemies.AirCrafts.Apache.GIB_REACH;
        customMaskWidth = Rules.Enemies.AirCrafts.Apache.MASK_WIDTH;
        customMaskHeight = Rules.Enemies.AirCrafts.Apache.MASK_HEIGHT;
        rotationChange = Rules.Enemies.AirCrafts.Apache.ROTATION_DELTA;
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
        initializeAfterObtained();
        initializeAttack();
    }

    /**
     * Initializes the Apache when obtained from pool.
     */
    private void initializeAfterObtained() {
        attacking = false;
        destinationReachRadius = Rules.Enemies.AirCrafts.Apache.DISTANCE_FROM_DESTINATION_REACH;
        fitSpriteToDirection = false;
        faceRegion();
        animation.setFrameDuration(Rules.Enemies.AirCrafts.PROPELLER_FRAME_DURATION);
    }

    /**
     * Orders the Apache to attack the player.
     */
    private void initializeAttack() {
        setOrder(new Order(Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Rules.Level.GROUND_Y, TASK_START_ATTACK, Rules.Enemies.AirCrafts.Apache.DELAY_ATTACK));
        directionToDestination += -5 + randomizer.nextInt(10);
        scheduleTask(TASK_START_ATTACK, Rules.Enemies.AirCrafts.Apache.DELAY_ATTACK);
    }

    /**
     * Completely overridden method. The Apache speeds up and moves to it's target. If it's close enough, it would call onOrderFulfilled(). Then it will rotate it's sprite accordingly downwards a bit.
     *
     * @param optimalSpeed The speed it would speed up to.
     */
    @Override
    protected void onFulfillingOrder(float optimalSpeed) {
        speedUp(optimalSpeed, Rules.Enemies.AirCrafts.Apache.SPEED_DELTA);
        aimToDestination();
        checkIfTargetReached();
        RotatesDownwards();
    }

    /**
     * Rotates downwards a bit.
     */
    private void RotatesDownwards() {
        if (scaleX == 1) {
            if (spriteDirection > -Rules.Enemies.AirCrafts.Apache.LEFT_ROTATION_MAX) {
                setSpriteDirection(spriteDirection - rotationChange);
            }
        } else {
            if (spriteDirection < Rules.Enemies.AirCrafts.Apache.LEFT_ROTATION_MAX) {
                setSpriteDirection(spriteDirection + rotationChange);
            }
        }
    }

    /**
     * Calls {@link Apache#onOrderFulfilled()} if it had reached it's target.
     */
    private void checkIfTargetReached() {
        float destinationX = order.destinationX;
        float destinationY = order.destinationY;
        if (x > destinationX - destinationReachRadius && x < destinationX + destinationReachRadius) {
            if (y > destinationY - destinationReachRadius && y < destinationY + destinationReachRadius) {
                onOrderFulfilled();
            }
        }
    }

    /**
     * Completely overridden method. Slows down 'till reaches 0.
     *
     * @param delta The delta to decrease every call.
     */
    @Override
    protected void slowDownSpeed(float delta) {
        if (speed >= 0) { // Lower speed if above 0.
            speed -= delta;
        } else {
            speed = 0;
        }
    }

    /**
     * Completely overridden method. The Apache would slow down it's speed and rotate it's sprite upwards.
     * @param delta
     */
    @Override
    protected void onWaitingForNewOrder(float delta) {
        slowDownSpeed(Rules.Enemies.AirCrafts.Apache.SPEED_DELTA);
        rotateUpwards();
    }

    /**
     * Rotates upwards a bit.
     */
    private void rotateUpwards() {
        if (scaleX == 1) {
            if (spriteDirection < Rules.Enemies.AirCrafts.Apache.RIGHT_ROTATION_MAX) {
                setSpriteDirection(spriteDirection + rotationChange);
            }
        } else {
            if (spriteDirection > 5) {
                setSpriteDirection(spriteDirection - rotationChange);
            }
        }
    }

    /**
     * Completely overridden method. The Apache would set it's direction to directionToDestination value. The parameters here are redundant.
     */
    @Override
    protected void aimToDestination() {
        setDirection(directionToDestination);
    }

    /**
     * The Apache would call super.onDeath() and cancel the task scheduling TASK_STOP_ATTACK.
     */
    @Override
    protected void onDeath() {
        super.onDeath();
        TASK_STOP_ATTACK.cancel();
    }

    /**
     * The Apache would start the overall attack.
     */
    private final Timer.Task TASK_START_ATTACK = new Timer.Task() {
        @Override
        public void run() {
            if (getTarget().isDead()) {
                return;
            }
            destinationReachRadius = Rules.Enemies.AirCrafts.Apache.SHORT_DISTANCE_FROM_DESTINATION_REACH;
            scheduleAllAttackTasks();
            attacking = true;
        }

        private void scheduleAllAttackTasks() {
            scheduleTask(TASK_SHOOT_BULLET, Rules.Enemies.AirCrafts.Apache.RELOAD);
            scheduleTask(TASK_STRAFE, Rules.Enemies.AirCrafts.Apache.STRAFE_INTERVAL);
            scheduleTask(TASK_SHOOT_MISSILE, Rules.Enemies.AirCrafts.Apache.RELOAD_MISSILE);
            scheduleTask(TASK_STOP_ATTACK, Rules.Enemies.AirCrafts.Apache.ATTACK_DURATION);
        }
    };

    /**
     * The Apache would create a machine gun bullet.
     */
    private final Timer.Task TASK_SHOOT_BULLET = new Timer.Task() {
        @Override
        public void run() {
            if (attacking && !dead && !isDestroyed()) { // Attacks as long as it's allowed to attack and is not dead.
                scheduleTask(TASK_SHOOT_BULLET, Rules.Enemies.AirCrafts.Apache.RELOAD);
                float x, y = getY() - height / 2;
                if (scaleX == -1) { // Create the bullet on it's face.
                    x = getX() - width / 2;
                } else {
                    x = getX() + width / 2;
                }
                Bullet bullet = getFactories().getBulletFactory().createBullet(BulletType.ENEMY_MINIGUN_BULLET, x, y, GameUtils.getDirectionToPoint(x, y, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Rules.Level.GROUND_Y));
                bullet.setDamageValue(getDamageBySkill(ApacheAttacks.MACHINE_GUN.ordinal()));
                Parastrike.getSoundPlayer().playSound(SFX.Player.SideKicks.AUTO_TURRET_SHOOT);
            }
        }
    };

    /**
     * The Apache would create a missile.
     */
    private final Timer.Task TASK_SHOOT_MISSILE = new Timer.Task() {
        @Override
        public void run() {
            if (!dead && attacking) { // Attacks as long as it's allowed to attack and is not dead.
                scheduleTask(TASK_SHOOT_MISSILE, Rules.Enemies.AirCrafts.Apache.RELOAD_MISSILE);
                scheduleTask(TASK_CHANGE_TO_IDLE, Rules.Enemies.AirCrafts.Apache.MISSILE_LAUNCH_IMAGE_DURATION);
                Bullet bullet = getFactories().getBulletFactory().createBullet(BulletType.BAZOOKA_MISSILE, x, y, GameUtils.getDirectionToPoint(x, y, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Rules.Level.GROUND_Y));
                bullet.setDamageValue(getDamageBySkill(ApacheAttacks.MISSILE.ordinal()));
                Parastrike.getSoundPlayer().playSound(SFX.Weapons.MISSILE_LAUNCH);
                setRegion(Assets.GFX.Sheets.ImagesNames.SHOOT, Rules.Enemies.AirCrafts.PROPELLER_FRAME_DURATION);
            }
        }
    };

    /**
     * The Apache would move to a near random position. If it's close to the ground, it would move up.
     */
    private final Timer.Task TASK_STRAFE = new Timer.Task() {
        @Override
        public void run() {
            if (!dead && attacking) { // Strafe as long as it's allowed to attack and is not dead.
                scheduleTask(TASK_STRAFE, Rules.Enemies.AirCrafts.Apache.STRAFE_INTERVAL);
                int randomDirection;
                // Move to a random position if not close to the ground. If it is close enough, move up.
                if (y > Rules.Enemies.AirCrafts.Apache.MINIMUM_DISTANCE_FROM_GROUND) {
                    if (scaleX == 1) {
                        randomDirection = 45 + randomizer.nextInt(180);
                    } else {
                        randomDirection = 135 - randomizer.nextInt(180);
                    }
                } else {
                    randomDirection = 90;
                }
                direction = randomDirection;
                setOrder(new Order(x + (float) (Math.cos(Math.toRadians(randomDirection)) * width), y + (float) (Math.sin(Math.toRadians(randomDirection)) * width)));
            }
        }
    };

    /**
     * The Apache would stop his attack and fly away.
     */
    private final Timer.Task TASK_STOP_ATTACK = new Timer.Task() {
        @Override
        public void run() {
            attacking = false;
            flyAway();
        }
    };

    @Override
    public int getCoinsBySkill() {
        int ordinal = Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal();
        if (ordinal == 0) {
            return Rules.Enemies.AirCrafts.Apache.Easy.COINS;
        } else if (ordinal == 1) {
            return Rules.Enemies.AirCrafts.Apache.Medium.COINS;
        }
        return Rules.Enemies.AirCrafts.Apache.Hard.COINS;
    }

    @Override
    public float getSpeedBySkill() {
        return Rules.Enemies.AirCrafts.Apache.SPEED;
    }

    @Override
    public float getHealthBySkill() {
        int ordinal = Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal();
        if (ordinal == 0) {
            return Rules.Enemies.AirCrafts.Apache.Easy.HP;
        } else if (ordinal == 1) {
            return Rules.Enemies.AirCrafts.Apache.Medium.HP;
        }
        return Rules.Enemies.AirCrafts.Apache.Hard.HP;
    }

    @Override
    public float getDamageBySkill(int weapon) {
        int skill = Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal();
        if (weapon == ApacheAttacks.MACHINE_GUN.ordinal()) {
            return getMachineGunDamageAccordingToSkill(skill);
        } else {
            return getMissileDamageAccordingToSkill(skill);
        }
    }

    /**
     * @param skill Selected skill.
     * @return The damage value according to the skill.
     */
    private float getMissileDamageAccordingToSkill(int skill) {
        if (skill == 0) {
            return Rules.Enemies.AirCrafts.Apache.Easy.MISSILE_DAMAGE;
        } else if (skill == 1) {
            return Rules.Enemies.AirCrafts.Apache.Medium.MISSILE_DAMAGE;
        } else {
            return Rules.Enemies.AirCrafts.Apache.Hard.MISSILE_DAMAGE;
        }
    }

    /**
     * @param skill Selected skill.
     * @return The damage value according to the skill.
     */
    private float getMachineGunDamageAccordingToSkill(int skill) {
        if (skill == 0) {
            return Rules.Enemies.AirCrafts.Apache.Easy.ATTACK_DAMAGE;
        } else if (skill == 1) {
            return Rules.Enemies.AirCrafts.Apache.Medium.ATTACK_DAMAGE;
        } else {
            return Rules.Enemies.AirCrafts.Apache.Hard.ATTACK_DAMAGE;
        }
    }

    @Override
    public float getSpecificBySkill() {
        return 0;
    }

    @Override
    public void onCollision(Enemy other) {

    }

    @Override
    public void onCollision(PlayerCharacter other) {

    }
}
