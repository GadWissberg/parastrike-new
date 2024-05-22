package com.gadarts.parashoot.player;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.enemies.aircrafts.Ballistic;
import com.gadarts.parashoot.model.CollideableGameObject;
import com.gadarts.parashoot.model.GameCharacter;
import com.gadarts.parashoot.model.interfaces.Targeting;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.screens.WarScreen;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;

import static com.gadarts.parashoot.assets.Assets.Configs.ParticleEffects.SMALL_BLAST_RING;
import static com.gadarts.parashoot.model.object_factories.MiscFactory.MiscType.FLYING_PART;

/**
 * Created by Gad on 25/07/2015.
 */
public class PlayerCharacter extends GameCharacter implements Targeting<Enemy> {

    protected float shootingRate;
    protected String idleSpriteName;
    protected boolean gunLoaded;
    protected Enemy target;
    protected float attackStrength = 1;
    protected SFX shootingSoundFileName;
    protected float armorUpgrade;
    protected float rateUpgrade;
    protected float strengthUpgrade;
    private WarScreen warScreen;

    public PlayerCharacter() {
        super();
        this.idleSpriteName = Assets.GFX.Sheets.ImagesNames.IDLE;
        gibReach = Rules.Player.GIB_REACH;
    }

    public void init(String spriteName, float x, float y, float direction, float speed, float hp, WarScreen warScreen) {
        super.init(spriteName, x, y, direction, speed, hp, warScreen.getElements());
        this.warScreen = warScreen;
        gunLoaded = true;
    }

    protected void shoot() {
        if (gunLoaded && !dead && target != null) {
            if (!target.hasLanded()) {
                setTarget(null);
                return;
            }
            if (target.isOnLeftSide()) {
                setRegion(Assets.GFX.Sheets.ImagesNames.SHOOT_LEFT, Rules.Player.SideKicks.InfantryTower.SHOOT_FRAME_DURATION);
            } else {
                setRegion(Assets.GFX.Sheets.ImagesNames.SHOOT_RIGHT, Rules.Player.SideKicks.InfantryTower.SHOOT_FRAME_DURATION);
            }
            shootingSoundId = Parastrike.getSoundPlayer().playSound(shootingSoundFileName);
            gunLoaded = false;
            scheduleTask(TASK_STOP_SHOOTING, Rules.Player.Bunker.SHOOTING_TIME);
            warScreen.getFactories().getMiscFactory().createExplosion(MiscFactory.ExplosionType.BULLET_IMPACT, target.getX() - (target.getWidth() / 2) + (randomizer.nextFloat() * target.getWidth()), target.getY() + Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.BULLET_IMPACT_RELATIVE_Y);
            float attack = calculateAttack();
            target.changeHealth(-attack);
        }
    }

    public WarScreen getWarScreen() {
        return warScreen;
    }

    public float calculateAttack() {
        float randomFloat = randomizer.nextFloat();
        if (randomFloat < Rules.Cannons.MINIMAL_ATTACK_FLOAT) {
            randomFloat = Rules.Cannons.MINIMAL_ATTACK_FLOAT;
        }
        return attackStrength * (randomFloat);
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        setRegion(Assets.GFX.Sheets.ImagesNames.DEAD);
    }

    @Override
    protected void onGib() {
        onDeath();
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (target != null && !dead) {
            if (warScreen.getPlayerHandler().getBunker().isDead() || target.isDead() || target.isDestroyed()) {
                setTarget(null);
            } else {
                shoot();
            }
        }
    }

    @Override
    public boolean isPlayerCharacter() {
        return true;
    }

    public boolean hasTarget() {
        return target != null && !target.isDead();
    }

    @Override
    public boolean setTarget(Enemy otherObject) {
        this.target = otherObject;
        return true;
    }

    public float getAttackStrength() {
        return attackStrength;
    }

    public void setAttackStrength(float attackStrength) {
        this.attackStrength = attackStrength;
    }

    public void setShootingSoundFileName(SFX shootingSoundFileName) {
        this.shootingSoundFileName = shootingSoundFileName;
    }

    public void setShootingRate(float shootingRate) {
        this.shootingRate = shootingRate;
    }

    public boolean isGunLoaded() {
        return gunLoaded;
    }

    public void setGunState(boolean state) {
        this.gunLoaded = state;
    }

    public SFX getShootingSoundFileName() {
        return shootingSoundFileName;
    }

    public float getShootingRate() {
        return shootingRate;
    }

    public Enemy getTarget() {
        return target;
    }

    public void setArmorLevel(float armorLevel) {
        float added = armorUpgrade * armorLevel;
        health += added;
    }

    public void setRateLevel(float rateLevel) {
        float less = rateUpgrade * rateLevel;
        shootingRate -= less;
    }

    public void setArmorUpgrade(float armorUpgrade) {
        this.armorUpgrade = armorUpgrade;
    }

    public void setStrengthUpgrade(float strengthUpgrade) {
        this.strengthUpgrade = strengthUpgrade;
    }

    public void setRateUpgrade(float rateUpgrade) {
        this.rateUpgrade = rateUpgrade;
    }

    public void setStrengthLevel(float strengthLevel) {
        float added = strengthUpgrade * strengthLevel;
        attackStrength += added;
    }

    protected final Timer.Task TASK_STOP_SHOOTING = new Timer.Task() {
        @Override
        public void run() {
            if (!dead) {
                setRegion(idleSpriteName);
                scheduleTask(TASK_RELOAD, shootingRate);
                Parastrike.getSoundPlayer().stopSound(shootingSoundFileName, shootingSoundId);
            }
        }
    };

    public final Timer.Task TASK_RELOAD = new Timer.Task() {
        @Override
        public void run() {
            gunLoaded = true;
        }
    };

    @Override
    public void collisionInteraction(CollideableGameObject otherObject) {
        otherObject.onCollision(this);
    }

    @Override
    public void onCollision(Bullet bullet) {
        boolean shieldActive = isShieldActive();
        if (dead || bullet.isPlayerBullet() || shieldActive) {
            if (shieldActive)
                getElements().getEffectsManager().createEffect(SMALL_BLAST_RING, bullet);
            return;
        }
        changeHealth(-bullet.getDamageValue());
        int numberOfTimes = randomizer.nextInt(3) + 1;
        warScreen.getFactories().getMiscFactory().createFlyingParts(FLYING_PART, bullet.getX(), bullet.getY(), numberOfTimes);
    }

    @Override
    public void onCollision(Enemy otherObject) {
        if (otherObject instanceof Ballistic) {
            Ballistic ballistic = (Ballistic) otherObject;
            changeHealth(-ballistic.getAttackDamage());
        }
    }

    @Override
    public void onCollision(PlayerCharacter other) {

    }

    @Override
    public void onCollision(Bonus other) {

    }
}
