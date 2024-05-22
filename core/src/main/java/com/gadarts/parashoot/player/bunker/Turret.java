package com.gadarts.parashoot.player.bunker;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.TimeUtils;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.GameObject;
import com.gadarts.parashoot.model.object_factories.BulletFactory;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.screens.WarScreen;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;
import com.gadarts.parashoot.weapons.BulletType;
import com.gadarts.parashoot.weapons.ShockWave;

import static com.gadarts.parashoot.utils.Rules.Menu.Mentors.Names.UPGRADE_GENERATOR;

/**
 * Created by Gad on 06/02/2015.
 */
public class Turret extends GameObject {

    public static final float OVER_HEAT = 100;
    private final int GENERATOR_LEVEL;

    private boolean gunLoaded = true;
    private long lastShotTime;
    private boolean gunReloading;
    private float rotation;
    private boolean isPressingFire;
    private BulletType currentWeaponIndex;
    private float shootingRate = Rules.Cannons.CannonBall.SHOOTING_RATE;
    private String currentWeaponName;
    private SFX currentWeaponSound;
    private boolean shootingIsAnimation;
    private float currentShootingFrameDuration = Rules.Misc.GENERAL_ANIMATION_FRAME_DURATION;
    private float shootingRateUpgrade = 1;
    private float strengthUpgrade = 1;
    private float strength = 1;
    private float heatPoints;
    private boolean overHeat;
    private boolean rotatable = true;
    private float targetX;
    private float targetY;
    private float currentHeatUse;
    private WarScreen warScreen;

    public Turret(float x, float y, BulletType selectedWeapon, int generator, WarScreen warScreen) {
        super((TextureAtlas) Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.Bunker.Turret.DATA_FILE), null, x, y, warScreen.getElements());
        this.warScreen = warScreen;
        setDirection(90);
        GENERATOR_LEVEL = generator;
        initializeWeapon(selectedWeapon);
    }

    private void initializeWeapon(BulletType selectedWeapon) {
        currentWeaponIndex = selectedWeapon;
        switch (selectedWeapon) {
            case CANNON_BALL:
                shootingRate = Rules.Cannons.CannonBall.SHOOTING_RATE;
                shootingRateUpgrade = Rules.Cannons.CannonBall.SHOOTING_RATE_UPGRADE;
                strengthUpgrade = Rules.Cannons.CannonBall.DAMAGE_VALUE_UPGRADE;
                strength = Rules.Cannons.CannonBall.DAMAGE_VALUE;
                currentWeaponName = Rules.Cannons.CannonBall.NAME;
                currentWeaponSound = SFX.Weapons.CANNON_BALL;
                currentHeatUse = Rules.Cannons.CannonBall.HEAT_USE;
                break;

            case SPREAD_CANNON_BALL:
                shootingRate = Rules.Cannons.SpreadCannonBall.SHOOTING_RATE;
                currentWeaponName = Rules.Cannons.SpreadCannonBall.NAME;
                currentWeaponSound = SFX.Weapons.TRIPLE_CANNON_BALL;
                shootingRateUpgrade = Rules.Cannons.SpreadCannonBall.SHOOTING_RATE_UPGRADE;
                strengthUpgrade = Rules.Cannons.SpreadCannonBall.DAMAGE_VALUE_UPGRADE;
                strength = Rules.Cannons.SpreadCannonBall.DAMAGE_VALUE;
                currentHeatUse = Rules.Cannons.SpreadCannonBall.HEAT_USE;
                break;

            case CHAIN_GUN_BULLET:
                shootingRate = Rules.Cannons.ChainGun.SHOOTING_RATE;
                currentWeaponName = Rules.Cannons.ChainGun.NAME;
                strength = Rules.Cannons.ChainGun.DAMAGE_VALUE;
                currentWeaponSound = SFX.Weapons.CHAIN_GUN;
                shootingIsAnimation = true;
                shootingRateUpgrade = Rules.Cannons.ChainGun.SHOOTING_RATE_UPGRADE;
                strengthUpgrade = Rules.Cannons.ChainGun.DAMAGE_VALUE_UPGRADE;
                currentShootingFrameDuration = Rules.Cannons.ChainGun.TURRET_SHOOT_FRAME_DURATION;
                currentHeatUse = Rules.Cannons.ChainGun.HEAT_USE;
                break;

            case ROCKET:
                shootingRate = Rules.Cannons.RocketLauncher.SHOOTING_RATE;
                currentWeaponName = Rules.Cannons.RocketLauncher.NAME;
                currentWeaponSound = SFX.Weapons.ROCKET_LAUNCHER;
                shootingRateUpgrade = Rules.Cannons.RocketLauncher.SHOOTING_RATE_UPGRADE;
                strengthUpgrade = Rules.Cannons.RocketLauncher.DAMAGE_VALUE_UPGRADE;
                strength = Rules.Cannons.RocketLauncher.DAMAGE_VALUE;
                currentHeatUse = Rules.Cannons.RocketLauncher.HEAT_USE;
                break;

            case HOMING_MISSILE:
                shootingRate = Rules.Cannons.IronDome.SHOOTING_RATE;
                currentWeaponName = Rules.Cannons.IronDome.NAME;
                currentWeaponSound = SFX.Weapons.MISSILE_LAUNCH;
                strength = Rules.Cannons.IronDome.DAMAGE_VALUE;
                shootingIsAnimation = true;
                currentShootingFrameDuration = Rules.Cannons.IronDome.TURRET_SHOOT_FRAME_DURATION;
                shootingRateUpgrade = Rules.Cannons.IronDome.SHOOTING_RATE_UPGRADE;
                strengthUpgrade = Rules.Cannons.IronDome.DAMAGE_VALUE_UPGRADE;
                currentHeatUse = Rules.Cannons.IronDome.HEAT_USE;
                break;

            case BLASTER:
                shootingRate = Rules.Cannons.Blaster.SHOOTING_RATE;
                currentWeaponName = Rules.Cannons.Blaster.NAME;
                currentWeaponSound = SFX.Weapons.SPLIT_LASER;
                shootingRateUpgrade = Rules.Cannons.Blaster.SHOOTING_RATE_UPGRADE;
                strengthUpgrade = Rules.Cannons.Blaster.DAMAGE_VALUE_UPGRADE;
                strength = Rules.Cannons.Blaster.DAMAGE_VALUE;
                currentHeatUse = Rules.Cannons.Blaster.HEAT_USE;
                break;

            case LASER:
                shootingRate = Rules.Cannons.TwinLaser.SHOOTING_RATE;
                currentWeaponName = Rules.Cannons.TwinLaser.NAME;
                currentWeaponSound = SFX.Weapons.RAPID_LASER;
                currentShootingFrameDuration = Rules.Cannons.TwinLaser.TURRET_SHOOT_FRAME_DURATION;
                shootingIsAnimation = true;
                shootingRateUpgrade = Rules.Cannons.TwinLaser.SHOOTING_RATE_UPGRADE;
                strengthUpgrade = Rules.Cannons.TwinLaser.DAMAGE_VALUE_UPGRADE;
                strength = Rules.Cannons.TwinLaser.DAMAGE_VALUE;
                currentHeatUse = Rules.Cannons.TwinLaser.HEAT_USE;
                break;

            case SHOCK_WAVE:
                shootingRate = Rules.Cannons.ShockWave.SHOOTING_RATE;
                currentWeaponName = Rules.Cannons.ShockWave.NAME;
                rotatable = false;
                direction = 90;
                currentWeaponSound = SFX.Weapons.SHOCK_WAVE;
                shootingRateUpgrade = Rules.Cannons.ShockWave.SHOOTING_RATE_UPGRADE;
                strengthUpgrade = Rules.Cannons.ShockWave.DAMAGE_VALUE_UPGRADE;
                strength = Rules.Cannons.ShockWave.DAMAGE_VALUE;
                currentHeatUse = Rules.Cannons.ShockWave.HEAT_USE;
                break;
        }
        setRegion(Assets.GFX.Sheets.ImagesNames.IDLE);
        setOrigin(0, height / 2);
    }

    @Override
    protected boolean setRegion(String name) {
        return setRegion(name, Rules.Misc.GENERAL_ANIMATION_FRAME_DURATION);
    }

    @Override
    protected boolean setRegion(String name, float frameDuration) {
        return super.setRegion(currentWeaponName + "_" + name, frameDuration);
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        handleShootingAndReloading();
        handleRotation();
        if (heatPoints > 0) {
            if (overHeat) {
                heatPoints -= Rules.Cannons.OverHeat.OVER_HEAT_DECAY;
            } else {
                heatPoints -= Rules.Player.Bunker.Turret.HEAT_RESISTANCE_UPGRADE_UNIT * GENERATOR_LEVEL;
            }
        } else {
            if (overHeat) {
                overHeat = false;
            }
        }
    }

    private void handleRotation() {
        if (rotation != 0) {
            if (direction <= 180 && direction >= 0) {
                setDirection(direction + rotation);
            } else if (direction > 180) {
                setDirection(180);
                setRotation(0);
            } else if ((direction > 180 && direction < 360) || direction < 0) {
                setDirection(0);
                setRotation(0);
            }
        }
    }

    private void handleShootingAndReloading() {
        if (gunReloading) {
            handleShootingImage();
            reloadGun();
        } else if (isPressingFire && !warScreen.getPlayerHandler().getBunker().isDead()) {
            if (!overHeat) {
                if (gunLoaded) {
                    gunLoaded = false;
                    gunReloading = true;
                    fire();
                }
            }
        } else {
            if (shootingIsAnimation) {
                setRegion(Assets.GFX.Sheets.ImagesNames.IDLE);
            }
        }
    }

    private void reloadGun() {
        if (TimeUtils.timeSinceMillis(lastShotTime) > shootingRate) {
            gunReloading = false;
            setGunState(true);
        }
    }

    private void handleShootingImage() {
        if (!shootingIsAnimation) {
            if (TimeUtils.timeSinceMillis(lastShotTime) > Rules.Player.Bunker.Turret.ANIMATION_TIME) {
                setRegion(Assets.GFX.Sheets.ImagesNames.IDLE);
            }
        }
    }

    @Override
    public void setDirection(float degrees) {
        degrees = GameUtils.fixedDegrees(degrees);
        if (degrees >= 180 && degrees <= 270) {
            degrees = 180;
        } else if (degrees > 270) {
            degrees = 0;
        }
        super.setDirection(degrees);
    }

    private void fire() {
        lastShotTime = TimeUtils.millis();
        setRegion(Assets.GFX.Sheets.ImagesNames.SHOOT, currentShootingFrameDuration);
        createSpecificBullet();
        Parastrike.getSoundPlayer().playSound(currentWeaponSound);
    }

    private void createSpecificBullet() {
        float cosPos = (float) (Math.cos(Math.toRadians(direction)) * width);
        float sinPos = (float) (Math.sin(Math.toRadians(direction)) * width);
        BulletFactory bulletFactory = warScreen.getFactories().getBulletFactory();
        switch (currentWeaponIndex) {
            case CANNON_BALL:
                Bullet bullet = bulletFactory.createBullet(currentWeaponIndex, getX() + cosPos, getY() + sinPos, getDirection());
                bullet.setDamageValue(strength);
                break;

            case SPREAD_CANNON_BALL:
                bullet = bulletFactory.createBullet(currentWeaponIndex, getX() + cosPos, getY() + sinPos, getDirection());
                bullet.setDamageValue(strength);
                float leftBulletDirection = getDirection() + Rules.Cannons.SpreadCannonBall.OTHER_BULLET_DIRECTION_DIFFERENCE;
                bullet = bulletFactory.createBullet(currentWeaponIndex, getX() + cosPos, getY() + sinPos, leftBulletDirection);
                bullet.setDamageValue(strength);
                float rightBulletDirection = getDirection() - Rules.Cannons.SpreadCannonBall.OTHER_BULLET_DIRECTION_DIFFERENCE;
                bullet = bulletFactory.createBullet(currentWeaponIndex, getX() + cosPos, getY() + sinPos, rightBulletDirection);
                bullet.setDamageValue(strength);
                break;

            case CHAIN_GUN_BULLET:
                bullet = bulletFactory.createBullet(currentWeaponIndex, getX() + cosPos, getY() + sinPos, getDirection());
                bullet.setDamageValue(strength);
                break;

            case ROCKET:
                bullet = bulletFactory.createBullet(currentWeaponIndex, getX() + cosPos, getY() + sinPos, getDirection());
                bullet.setDamageValue(strength);
                break;

            case HOMING_MISSILE:
                bullet = bulletFactory.createBullet(currentWeaponIndex, getX() + cosPos, getY() + sinPos, getDirection());
                bullet.setDamageValue(strength);
                bullet = bulletFactory.createBullet(BulletType.NON_HOMING_MISSILE, getX() + cosPos, getY() + sinPos, getDirection());
                bullet.setDamageValue(strength);
                warScreen.getFactories().getMiscFactory().createIndependentEffect(MiscFactory.IndependentEffectType.MISSILE_LAUNCH_SMOKE, getX() + cosPos, getY() + sinPos);
                break;

            case BLASTER:
                bullet = bulletFactory.createBullet(currentWeaponIndex, getX() + cosPos, getY() + sinPos, getDirection());
                bullet.setDamageValue(strength);
                break;

            case LASER:
                float cosPos2 = (float) (Math.cos(Math.toRadians(direction + 90)) * Rules.Cannons.TwinLaser.DISTANCE_FROM_CREATION_ORIGIN);
                float sinPos2 = (float) (Math.sin(Math.toRadians(direction + 90)) * Rules.Cannons.TwinLaser.DISTANCE_FROM_CREATION_ORIGIN);
                bullet = bulletFactory.createBullet(currentWeaponIndex, getX() + cosPos + cosPos2, getY() + sinPos + sinPos2, getDirection());
                bullet.setDamageValue(strength);
                bullet = bulletFactory.createBullet(currentWeaponIndex, getX() + cosPos - cosPos2, getY() + sinPos - sinPos2, getDirection());
                bullet.setDamageValue(strength);
                break;

            case SHOCK_WAVE:
                ShockWave shockWave = (ShockWave) bulletFactory.createBullet(currentWeaponIndex, getX() + cosPos, getY() + sinPos, getDirection());
                shockWave.setDamageValue(strength);
                shockWave.activate(targetX, targetY);
                break;
        }
        heatUp();
    }


    private void heatUp() {
        if (currentHeatUse != 0) {
            if (heatPoints + currentHeatUse < OVER_HEAT) {
                heatPoints += currentHeatUse;
            } else {
                heatPoints += OVER_HEAT - heatPoints;
                overHeat = true;
                Parastrike.getMentorsManager().readyMentorIfDidntRun(UPGRADE_GENERATOR);
                warScreen.getHud().engageSmoke();
                warScreen.getElements().getMessageDisplay().add(Assets.Strings.InGameMessages.OVER_HEAT);
                Parastrike.getSoundPlayer().playSound(SFX.Weapons.OVER_HEAT);
                warScreen.getFactories().getMiscFactory().createIndependentEffect(MiscFactory.IndependentEffectType.WHITE_SMALL_SMOKE_UP, x, y);
                setRegion(Assets.GFX.Sheets.ImagesNames.IDLE);
            }
        }
    }

    public void setGunState(boolean gunState) {
        this.gunLoaded = gunState;
    }

    public void setRotation(float rotateState) {
        this.rotation = rotateState;
    }

    public void setPressingFireState(boolean state) {
        isPressingFire = state;
    }

    public void setDirectionToPosition(float targetX, float targetY) {
        setDirection((float) Math.toDegrees(Math.atan2(targetY - y, targetX - x)));
    }

    public void setShootingRateLevel(float shootingRateLevel) {
        float less = shootingRateLevel * shootingRateUpgrade;
        shootingRate -= less;
    }

    public void setStrengthLevel(float strengthLevel) {
        float added = strengthLevel * strengthUpgrade;
        strength += added;
    }

    public boolean isRotatable() {
        return rotatable;
    }

    public void setTargetPosition(float screenX, float screenY) {
        targetX = screenX;
        targetY = screenY;
    }

    public BulletType getCurrentWeaponIndex() {
        return currentWeaponIndex;
    }

    public float getHeatPoints() {
        return heatPoints;
    }

    public boolean isOverHeat() {
        return overHeat;
    }
}
