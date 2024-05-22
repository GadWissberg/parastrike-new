package com.gadarts.parashoot.enemies;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.ground_crafts.Tank;
import com.gadarts.parashoot.level_model.Level;
import com.gadarts.parashoot.model.*;
import com.gadarts.parashoot.model.interfaces.SkillAble;
import com.gadarts.parashoot.model.interfaces.Targeting;
import com.gadarts.parashoot.player.Bonus;
import com.gadarts.parashoot.player.PlayerCharacter;
import com.gadarts.parashoot.screens.WarScreen;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;

/**
 * Represents enemies.
 */
public abstract class Enemy extends GameCharacter implements Targeting<PlayerCharacter>, SkillAble {
    /**
     * The given coins when defeated.
     */
    protected int coinsWorth;

    /**
     * The current player target to attack.
     */
    protected PlayerCharacter target;

    /**
     * The minimum interval between every attack in seconds.
     */
    protected float reloadTime;

    /**
     * The amount of damage of every attack.
     */
    protected float attackDamage;

    /**
     * The current order to perform.
     */
    protected Order order;

    /**
     * The current direction to the order's destination.
     */
    protected float directionToDestination;

    /**
     * The maximum radius to reach the order's destination.
     */
    protected float destinationReachRadius = 6;

    /**
     * The amount of degrees for changing direction.
     */
    protected float rotationChange;

    /**
     * The ID of the motor sound.
     */
    private long motorSoundId;

    /**
     * The name of the motor sound.
     */
    protected SFX motorSoundFileName;

    /**
     * The current volume of the motor sound.
     */
    private float motorSoundVolume;

    /**
     * Whether landed or not.
     */
    boolean landed;

    private PlayerHandler playerHandler;

    private Factories factories;

    public Enemy() {
        super();
        coinsWorth = getCoinsBySkill();
    }

    /**
     * Poolable initialization method. Initializing fields and plays the motor sound (if it has any).
     *
     * @param spriteName    The name of the beginning region.
     * @param x             Starting X.
     * @param y             Starting Y.
     * @param direction     Starting direction.
     * @param speed         Starting speed.
     * @param hp            Starting health points.
     * @param mechanics     The war screen's mechanics.
     * @param playerHandler The war screen's player handler.
     * @param factories     The war screen's factories.
     */
    protected void init(String spriteName, float x, float y, float direction, float speed, float hp, WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        super.init(spriteName, x, y, direction, speed, hp, mechanics);
        this.playerHandler = playerHandler;
        this.factories = factories;
        initializeFields();
        playMotorSound();
    }

    /**
     * @return Whether landed or not.
     */
    public boolean hasLanded() {
        return landed;
    }

    /**
     * Adds one to score and {@link Enemy#coinsWorth} to coins.
     */
    protected void rewardPlayer() {
        ScoresHandler scoresHandler = getElements().getScoresHandler();
        scoresHandler.addScore(1);
        scoresHandler.addCoins(coinsWorth, getX(), getY());
    }

    /**
     * Calls {@link com.gadarts.parashoot.model.GameObject#onOutside(Level)} and fails the Rampage feat if it has reached outside alive.
     *
     * @param currentLevel The current war screen's level.
     */
    @Override
    public void onOutside(Level currentLevel) {
        super.onOutside(currentLevel);
        if ((isStartingOutside() && hasBeenInside()) && !isDead()) {
            Level.Feat rampage = Level.Feat.RAMPAGE;
            if (currentLevel.getFeat(rampage)) currentLevel.failFeat(rampage);
        }
    }

    protected PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    protected Factories getFactories() {
        return factories;
    }

    /**
     * Nothing is supposed to happen when enemy collides a bonus.
     */
    @Override
    public void onCollision(Bonus other) {

    }

    /**
     * Initializes fields when obtained from pool or created.
     */
    private void initializeFields() {
        target = null;
        motorSoundVolume = 0;
    }

    /**
     * Plays the motor sound (if there is any).
     */
    void playMotorSound() {
        if (motorSoundFileName != null) {
            motorSoundId = Parastrike.getSoundPlayer().playSound(motorSoundFileName, true, 0);
        }
    }

    /**
     * Calls {@link GameCharacter#onDeath()} and stops the motor sound (if there is any).
     */
    @Override
    protected void onDeath() {
        super.onDeath();
        stopMotorSound();
    }

    /**
     * Calls {@link com.gadarts.parashoot.model.PoolableGameObject#onTerminate(WarScreen)} and notifies the war screen about the termination of this enemy.
     *
     * @param warScreen
     */
    @Override
    public void onTerminate(WarScreen warScreen) {
        super.onTerminate(warScreen);
        warScreen.onEnemyTermination(this);
    }

    /**
     * Calls {@link CollideableGameObject#onUpdate(float)}, handles the current order and cresendos upwards the motor sound (if there is any).
     *
     * @param delta Between current frame and prev.
     */
    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        handleOrders(getSpeedBySkill(), delta);
        if (motorSoundFileName != null) {
            if (motorSoundVolume < 1) {
                motorSoundVolume += 0.01f;
                Parastrike.getSoundPlayer().setVolume(motorSoundFileName, motorSoundId, motorSoundVolume);
            }
        }
    }

    /**
     * Sets the {@link Enemy#dead} flag. If the enemy is not dead and he's set to dead, scores handler is notified for statistics.
     *
     * @param state
     */
    @Override
    protected void setDeadFlag(boolean state) {
        if (!dead && state) {
            getElements().getScoresHandler().enemyDestroyed();
        }
        super.setDeadFlag(state);
    }

    /**
     * Calls {@link GameObject#onDestroy()} and stops the motor sound (if there is any).
     *
     * @param doDestroySequence
     */
    @Override
    public void onDestroy(boolean doDestroySequence) {
        super.onDestroy(doDestroySequence);
        stopMotorSound();
    }

    /**
     * Stops the motor sound, if there is any.
     */
    protected void stopMotorSound() {
        if (motorSoundFileName != null) {
            Parastrike.getSoundPlayer().stopSound(motorSoundFileName, motorSoundId);
        }
    }

    public void setReloadTime(float reloadTime) {
        this.reloadTime = reloadTime;
    }

    protected void setAttackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Nothing is supposed to happen when enemy collides a bullet in the abstract level. The actual implementation is in the specific enemy class.
     */
    @Override
    public void onCollision(Bullet other) {
    }

    /**
     * Nothing is supposed to happen when enemy collides another enemy in the abstract level. The actual implementation is in the specific enemy class.
     */
    @Override
    public void onCollision(Enemy other) {

    }

    /**
     * Nothing is supposed to happen when enemy collides a player character.
     */
    @Override
    public void onCollision(PlayerCharacter other) {

    }

    @Override
    public void collisionInteraction(CollideableGameObject other) {
        other.onCollision(this);
    }

    /**
     * @return false, of course.
     */
    @Override
    public boolean isPlayerCharacter() {
        return false;
    }

    /**
     * @return true only if this enemy has a target and it's not dead.
     */
    public boolean hasTarget() {
        return target != null && !target.isDead();
    }

    /**
     * Sets the given target.
     *
     * @param target
     * @return true.
     */
    public boolean setTarget(PlayerCharacter target) {
        this.target = target;
        return true;
    }

    /**
     * Rotates the enemy ones step towards the order's destination.
     */
    protected void aimToDestination() {
        if (directionToDestination > direction + 2 || directionToDestination < direction - 2) {
            directionToDestination = GameUtils.getDirectionToPoint(x, y, order.destinationX, order.destinationY);
            if (direction < directionToDestination) {
                if (Math.abs(direction - directionToDestination) < 180) {
                    setDirection(direction + rotationChange);
                } else {
                    setDirection(direction - rotationChange);
                }
            } else {
                if (Math.abs(direction - directionToDestination) < 180) {
                    setDirection(direction - rotationChange);
                } else {
                    setDirection(direction + rotationChange);
                }
            }
        }
    }

    /**
     * Updates the fulfillment process of the current order. If it has no order, will wait for a new order.
     *
     * @param optimalSpeed
     * @param delta
     */
    private void handleOrders(float optimalSpeed, float delta) {
        if (!dead) {
            if (order != null) {
                onFulfillingOrder(optimalSpeed);
            } else {
                onWaitingForNewOrder(delta);
            }
        }
    }

    /**
     * Whatever this enemy should do while this order fulfillment is in process.
     *
     * @param speed The new speed value.
     */
    protected abstract void onFulfillingOrder(float speed);

    /**
     * Whether this enemy can be tracked by homing weapons.
     */
    public boolean isHomeable() {
        return true;
    }

    public PlayerCharacter getTarget() {
        return target;
    }

    /**
     * Speeds up the current speed, 'till reached the optimal.
     *
     * @param optimalSpeed
     * @param speedDelta
     */
    protected void speedUp(float optimalSpeed, float speedDelta) {
        if (speed < optimalSpeed) {
            this.speed += speedDelta;
        } else {
            this.speed = optimalSpeed;
        }
    }

    /**
     * Whatever this enemy should do while waiting for a new order.
     *
     * @param aux Optional aux value.
     */
    protected abstract void onWaitingForNewOrder(float aux);

    /**
     * Slows down the current speed, 'till reaches 0. When reaches 0, frame duration is also 0.
     *
     * @param delta The pace of slow down.
     */
    protected void slowDownSpeed(float delta) {
        if (speed >= 0) {
            speed -= delta;
        } else {
            speed = 0;
            setFrameDuration(0);
        }
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    /**
     * Schedules the order's task and set the next order.
     */
    protected void onOrderFulfilled() {
        // For some reason, calling stopMotorSound() in Tank (where it's supposed to be) isn't working! Might be a bug in LibGDX. Might be related: http://stackoverflow.com/questions/20905364/no-sound-in-android-application-with-libgdx.
        if (this instanceof Tank) {
            stopMotorSound();
        }
        if (order.task != null) {
            scheduleTask(order.task, order.taskDelay);
        }
        setOrder(order.nextOrder);
    }

    /**
     * Sets the given order and calculates the direction to it's destination and reach radius
     *
     * @param order
     */
    public void setOrder(Order order) {
        this.order = order;
        if (order != null) {
            directionToDestination = GameUtils.getDirectionToPoint(x, y, order.destinationX, order.destinationY);
            if (order.destinationReach > -1) {
                destinationReachRadius = order.destinationReach;
            }
        }
    }

    /**
     * Sets the current frames to idle frames.
     */
    protected final Timer.Task TASK_CHANGE_TO_IDLE = new Timer.Task() {
        @Override
        public void run() {
            if (!dead) {
                setRegion(Assets.GFX.Sheets.ImagesNames.IDLE, Rules.Enemies.AirCrafts.PROPELLER_FRAME_DURATION);
            }
        }
    };

}
