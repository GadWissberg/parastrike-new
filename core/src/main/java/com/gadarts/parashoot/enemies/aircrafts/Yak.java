package com.gadarts.parashoot.enemies.aircrafts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.AirCraft;
import com.gadarts.parashoot.enemies.Order;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.object_factories.BonusFactory;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;
import com.gadarts.parashoot.weapons.BulletType;

/**
 * The Yak, Super Yak and Medic Yak enemies.
 */

public class Yak extends AirCraft {

    /**
     * Types of yaks.
     */
    public enum Type {
        REGULAR, SUPER, MEDIC
    }

    private static final Color SUPER_COLOR = new Color(0.5f, 0, 0, 1);// The super yak's color.
    private static final Color MEDIC_COLOR = new Color(1, 0, 1, 1);// The medic yak's color.
    private boolean gunLoaded;// A flag to indicate whether the yak's gun is loaded.
    private boolean attacking;// A flag to indicate whether the yak's gun is loaded.
    private String currentSpriteName;// The name of the current shown sprite (the yak has 8 angles).
    private String spriteNameSuffix = Assets.GFX.Sheets.ImagesNames.RIGHT;// The sprite name suffix according to the direction.
    private long myFlyBySound; // The id of the yaks fly by sound.
    private Type type = Type.REGULAR; // The yak's type.

    public Yak() {
        super();
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.AirCrafts.YAK_DATA_FILE);
        gibReach = Rules.Enemies.AirCrafts.Yak.GIB_REACH;
        customMaskWidth = Rules.Enemies.AirCrafts.Yak.MASK_WIDTH;
        customMaskHeight = Rules.Enemies.AirCrafts.Yak.MASK_HEIGHT;
        rotationChange = Rules.Enemies.AirCrafts.Yak.ROTATION_CHANGE;
        type = Type.REGULAR;
    }

    /**
     * Poolable initialization method.
     *
     * @param x             Starting X.
     * @param y             Starting Y.
     * @param type          The yak's type.
     * @param mechanics     The war screen's mechanics.
     * @param playerHandler The war screen's player handler.
     * @param factories     The war screen's factories.
     */
    public void init(float x, float y, Type type, WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        this.type = type;
        init(x, y, getSpeedBySkill(), getHealthBySkill(), type, mechanics, playerHandler, factories);
    }

    /**
     * Poolable initialization method.
     *
     * @param x             Starting X.
     * @param y             Starting Y.
     * @param speed         Starting speed.
     * @param hp            Stating HP.
     * @param type          The yak's type.
     * @param mechanics     The war screen's mechanics.
     * @param playerHandler The war screen's player handler.
     * @param factories     The war screen's factories.
     */
    protected void init(float x, float y, float speed, float hp, Type type, WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.IDLE, x, y, direction, speed, hp, mechanics, playerHandler, factories);
        this.type = type;
        coinsWorth = getCoinsBySkill();
        fitSpriteToDirection = true;
        attacking = false;
        boolean attackOnStart = randomizer.nextBoolean();
        if (attackOnStart && type != Type.MEDIC) {
            scheduleTask(TASK_START_ATTACK, Rules.Enemies.AirCrafts.Yak.DELAY_ATTACK);
            setOrder(ORDER_FLY_TO_PLAYER);
        } else {
            setOrder(new Order((float) (100 + (Rules.System.Resolution.WIDTH_TARGET_RESOLUTION - 200) * Math.random()), Rules.Level.GROUND_Y, ORDER_FLY_TO_SKY, null, 0, 350));
        }
        directionToDestination = GameUtils.getDirectionToPoint(x, y, order.destinationX, order.destinationY);
        direction = directionToDestination;
        currentSpriteName = Assets.GFX.Sheets.ImagesNames.IDLE;
        myFlyBySound = Parastrike.getSoundPlayer().playSound(SFX.Enemies.AirCrafts.PLANE_FLYBY);
        gunLoaded = true;
    }

    /**
     * Completely overridden method. The Yaks will always fly away when waiting for new order.
     *
     * @param delta
     */
    @Override
    protected void onWaitingForNewOrder(float delta) {
        flyAway();
    }

    /**
     * Completely overridden method. When the yak die it'll stop it's flyby sound. If it's a Medic Yak, it'll gib.
     */
    @Override
    protected void onDeath() {
        Parastrike.getSoundPlayer().stopSound(SFX.Enemies.AirCrafts.PLANE_FLYBY, myFlyBySound);
        if (type != Type.MEDIC) {
            super.onDeath();
        } else {
            onGib();
        }
    }

    /**
     * On gib it'll stop the flyby sound. If it's a Medic Yak, it'll spawn a bonus.
     */
    @Override
    protected void onGib() {
        super.onGib();
        if (type == Type.MEDIC) {
            getFactories().getBonusFactory().createBonus(BonusFactory.BonusType.FIX, x, y);
        }
        Parastrike.getSoundPlayer().stopSound(SFX.Enemies.AirCrafts.PLANE_FLYBY, myFlyBySound);
    }

    /**
     * Will draw according to it's type.
     *
     * @param batch The batch used to draw.
     */
    @Override
    public void onDraw(SpriteBatch batch) {
        switch (type) {
            case REGULAR:
                super.onDraw(batch);
                break;
            case SUPER:
                drawColoredSprite(batch, SUPER_COLOR);
                break;
            case MEDIC:
                drawColoredSprite(batch, MEDIC_COLOR);
                break;
        }
    }

    /**
     * sprite and shooting handling.
     *
     * @param delta
     */
    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        handleDirectionSprites();
        if (dead) {
            setSpriteDirectionToMovement();
        }
        if (isPermittedToShoot()) {
            shoot();
        }
    }

    /**
     * The yak can shoot if 'attacking' flag is true, the yak is facing it's destination and it's not a Medic Yak.
     */
    private boolean isPermittedToShoot() {
        return attacking && direction < directionToDestination + 3 && direction > directionToDestination - 3 && direction > 225 && direction < 315 && type != Type.MEDIC;
    }

    /**
     * Shoot (according to it's type).
     */
    private void shoot() {
        if (gunLoaded && !dead && !getTarget().isDead()) {
            gunLoaded = false;
            switch (type) {
                case REGULAR:
                    scheduleTask(TASK_RELOAD, Rules.Enemies.AirCrafts.Yak.RELOAD);
                    Bullet bullet = getFactories().getBulletFactory().createBullet(BulletType.ENEMY_MINIGUN_BULLET, (x + width / 5), y, direction);
                    bullet.setDamageValue(getDamageBySkill(0));
                    bullet = getFactories().getBulletFactory().createBullet(BulletType.ENEMY_MINIGUN_BULLET, (x - width / 5), y, direction);
                    bullet.setDamageValue(getDamageBySkill(0));
                    Parastrike.getSoundPlayer().playSound(SFX.Player.SideKicks.AUTO_TURRET_SHOOT);
                    break;
                case SUPER:
                    bullet = getFactories().getBulletFactory().createBullet(BulletType.BAZOOKA_MISSILE, (x + width / 5), y, direction);
                    bullet.setDamageValue(getDamageBySkill(0));
                    bullet.setSpeed(Rules.Enemies.AirCrafts.Yak.MISSILE_SPEED);
                    bullet = getFactories().getBulletFactory().createBullet(BulletType.BAZOOKA_MISSILE, (x - width / 5), y, direction);
                    bullet.setDamageValue(getDamageBySkill(0));
                    bullet.setSpeed(Rules.Enemies.AirCrafts.Yak.MISSILE_SPEED);
                    Parastrike.getSoundPlayer().playSound(SFX.Weapons.MISSILE_LAUNCH);
                    break;
            }
        }
    }

    @Override
    public int getCoinsBySkill() {
        if (type != null) {
            switch (type) {
                case REGULAR:
                    if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
                        return Rules.Enemies.AirCrafts.Yak.Easy.COINS;
                    } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
                        return Rules.Enemies.AirCrafts.Yak.Medium.COINS;
                    } else {
                        return Rules.Enemies.AirCrafts.Yak.Hard.COINS;
                    }
                case SUPER:
                    if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
                        return Rules.Enemies.AirCrafts.Yak.Easy.SUPER_COINS;
                    } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
                        return Rules.Enemies.AirCrafts.Yak.Medium.SUPER_COINS;
                    } else {
                        return Rules.Enemies.AirCrafts.Yak.Hard.SUPER_COINS;
                    }
                case MEDIC:
                    return 0;
            }
        }
        return 0;
    }

    /**
     * Set correct sprite according to it's direction.
     */
    private void handleDirectionSprites() {
        if (direction > 337.5 || direction <= 22.5) {
            scaleX = 1;
            scaleY = 1;
            spriteNameSuffix = Assets.GFX.Sheets.ImagesNames.RIGHT;
        } else if (direction > 22.5 && direction <= 67.5) {
            scaleX = 1;
            scaleY = 1;
            spriteNameSuffix = Assets.GFX.Sheets.ImagesNames.UP_RIGHT;
        } else if (direction > 67.5 && direction <= 112.5) {
            scaleX = 1;
            scaleY = 1;
            spriteNameSuffix = Assets.GFX.Sheets.ImagesNames.UP;
        } else if (direction > 112.5 && direction <= 157.5) {
            scaleX = 1;
            scaleY = -1;
            spriteNameSuffix = Assets.GFX.Sheets.ImagesNames.UP_RIGHT;
        } else if (direction > 157.5 && direction <= 202.5) {
            scaleX = 1;
            scaleY = -1;
            spriteNameSuffix = Assets.GFX.Sheets.ImagesNames.RIGHT;
        } else if (direction > 202.5 && direction <= 247.5) {
            scaleX = 1;
            scaleY = -1;
            spriteNameSuffix = Assets.GFX.Sheets.ImagesNames.DOWN_RIGHT;
        } else if (direction > 247.5 && direction <= 292.5) {
            scaleX = 1;
            scaleY = 1;
            spriteNameSuffix = Assets.GFX.Sheets.ImagesNames.DOWN;
        } else if (direction > 292.5 && direction <= 337.5) {
            scaleX = 1;
            scaleY = 1;
            spriteNameSuffix = Assets.GFX.Sheets.ImagesNames.DOWN_RIGHT;
        }
        if (dead) {
            currentSpriteName = Assets.GFX.Sheets.ImagesNames.FALLING;
        } else {
            if (isPermittedToShoot()) {
                currentSpriteName = Assets.GFX.Sheets.ImagesNames.SHOOT;
            } else {
                currentSpriteName = Assets.GFX.Sheets.ImagesNames.IDLE;
            }
        }
        setRegion(currentSpriteName, Rules.Enemies.GeneralAttributes.SHOOT_FRAME_DURATION);
    }

    /**
     * Completely overridden method. Will set the region with the correct sprite name suffix.
     */
    @Override
    protected boolean setRegion(String name, float frameDuration, Animation.PlayMode playMode) {
        return super.setRegion(name + "_" + spriteNameSuffix, frameDuration, playMode);
    }

    @Override
    public float getSpeedBySkill() {
        return Rules.Enemies.AirCrafts.Yak.SPEED;
    }

    @Override
    public float getHealthBySkill() {
        if (type != Type.MEDIC) {
            if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
                return Rules.Enemies.AirCrafts.Yak.Easy.HP;
            } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
                return Rules.Enemies.AirCrafts.Yak.Medium.HP;
            } else {
                return Rules.Enemies.AirCrafts.Yak.Hard.HP;
            }
        } else {
            return Rules.Enemies.AirCrafts.Yak.MedicYak.HP;
        }
    }

    @Override
    public float getDamageBySkill(int weapon) {
        switch (type) {
            case REGULAR:
                if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
                    return Rules.Enemies.AirCrafts.Yak.Easy.ATTACK_DAMAGE;
                } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
                    return Rules.Enemies.AirCrafts.Yak.Medium.ATTACK_DAMAGE;
                } else {
                    return Rules.Enemies.AirCrafts.Yak.Hard.ATTACK_DAMAGE;
                }
            case SUPER:
                if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
                    return Rules.Enemies.AirCrafts.Yak.Easy.SUPER_ATTACK_DAMAGE;
                } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
                    return Rules.Enemies.AirCrafts.Yak.Medium.SUPER_ATTACK_DAMAGE;
                } else {
                    return Rules.Enemies.AirCrafts.Yak.Hard.SUPER_ATTACK_DAMAGE;
                }
        }
        return 0;
    }

    @Override
    public float getSpecificBySkill() {
        return 0;
    }

    private final Timer.Task TASK_START_ATTACK = new Timer.Task() {
        @Override
        public void run() {
            attacking = true;
        }
    };

    private final Order ORDER_FLY_TO_PLAYER = new Order(Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Rules.Level.GROUND_Y, Rules.Enemies.AirCrafts.Yak.FLY_TO_PLAYER_REACH);

    private final Order ORDER_FLY_TO_SKY = new Order(Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION, ORDER_FLY_TO_PLAYER, TASK_START_ATTACK, 0, Rules.Enemies.AirCrafts.Yak.FLY_TO_SKY_REACH);

    /**
     * Reloads the gun.
     */
    private final Timer.Task TASK_RELOAD = new Timer.Task() {
        @Override
        public void run() {
            gunLoaded = true;
        }
    };

}
