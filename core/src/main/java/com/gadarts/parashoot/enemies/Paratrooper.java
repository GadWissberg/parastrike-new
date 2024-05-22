package com.gadarts.parashoot.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.paratroopers.Parachute;
import com.gadarts.parashoot.level_model.Level;
import com.gadarts.parashoot.model.*;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.player.PlayerCharacter;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;
import com.gadarts.parashoot.weapons.BulletType;
import com.gadarts.parashoot.weapons.Flame;
import com.gadarts.parashoot.weapons.ShockWave;

public abstract class Paratrooper extends GroundUnit {

    private boolean swingsAnimation;
    private double spriteDirectionSpeed;
    private Parachute parachute;
    private long myFallingSoundId;
    private SFX myFallingSoundName;

    public Paratrooper() {
        super();
        gravityAcceleration = Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.GRAVITY_ACCELERATION;
        maxFallingSpeed = Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.Speed.MAX_FALLING_SPEED;
        customMaskWidth = Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.MASK_WIDTH;
    }

    public void init(String spriteName, float x, float y, float hp, float gibReach, WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        super.init(spriteName, x, y, direction, Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.Speed.START_FALLING_SPEED, hp, mechanics, playerHandler, factories);
        scheduleTask(TASK_OPEN_PARACHUTE, Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.TIME_TO_OPEN_PARACHUTE);
        setOrigin(getWidth() / 2, getHeight());
        this.gibReach = gibReach;
        animating = false;
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        gravityStatus = true;
        fallingSpeed = Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.Speed.START_FALLING_SPEED;
        fitSpriteToDirection = false;
        spriteDirectionSpeed = Rules.Enemies.GroundUnits.Paratroopers.Parachute.SWING_ANIMATION_SPEED;
        landed = false;
        parachute = (Parachute) getFactories().getMiscFactory().createMisc(MiscFactory.MiscType.PARACHUTE, x, y);
        myFallingSoundId = -1;
        myFallingSoundName = SFX.Enemies.GroundUnits.Paratroopers.FALLING_1;
        setParachute(parachute);
        getElements().getCurrentLevel().AddToTotalEnemies();
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        super.onDraw(batch);
        if (parachute != null && parachute.isVisible()) {
            parachute.onDraw(batch);
        }
    }

    @Override
    protected void fall(float delta) {
        if ((y - height) <= Rules.Level.GROUND_Y) {
            setY(Rules.Level.GROUND_Y + height);
            speed = 0;
        } else {
            if (gravityStatus) {
                setY(y - fallingSpeed);
                if (fallingSpeed < maxFallingSpeed) {
                    fallingSpeed += gravityAcceleration;
                }
            }
        }
    }

    @Override
    public void onDeath() {
        super.onDeath();
        if (!hasLanded() && lastDamagedBy == Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.DamageEffect.REGULAR) {
            onGib();
            return;
        }
        rewardPlayer();
        handleDieSprite();
    }

    @Override
    public void onOutside(Level currentLevel) {
        setDeadFlag(true);
        setDestroyMeFlag(true);
    }

    private void playRandomDyingSound() {
        int chances = randomizer.nextInt(6);
        SFX selectedSound = null;
        switch (chances) {
            case 0:
                selectedSound = SFX.Enemies.GroundUnits.Paratroopers.DEATH_1;
                break;
            case 1:
                selectedSound = SFX.Enemies.GroundUnits.Paratroopers.DEATH_2;
                break;
            case 2:
                selectedSound = SFX.Enemies.GroundUnits.Paratroopers.DEATH_3;
                break;
            case 3:
                selectedSound = SFX.Enemies.GroundUnits.Paratroopers.DEATH_4;
                break;
            case 4:
                selectedSound = SFX.Enemies.GroundUnits.Paratroopers.DEATH_5;
                break;
            case 5:
                selectedSound = SFX.Enemies.GroundUnits.Paratroopers.DEATH_6;
                break;
        }
        Parastrike.getSoundPlayer().playSound(selectedSound);
    }

    private void handleDieSprite() {
        if (lastDamagedBy == Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.DamageEffect.REGULAR) {
            setRegion(Assets.GFX.Sheets.ImagesNames.DYING);
            animation.setPlayMode(Animation.PlayMode.NORMAL);
            scheduleTask(TASK_DISAPPEAR, Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.BodyFadeOutTimes.TIME_TO_BODY_FADE_OUT);
            playRandomDyingSound();
        } else if (lastDamagedBy == Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.DamageEffect.ELECTRICITY) {
            setRegion(Assets.GFX.Sheets.ImagesNames.ELECTROCUTED);
            Parastrike.getSoundPlayer().playSound(SFX.Enemies.GroundUnits.Paratroopers.ELECTRIFIED);
            animation.setPlayMode(Animation.PlayMode.LOOP_RANDOM);
            animation.setFrameDuration(Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.ELECTROCUTION_FRAME_DURATION);
            scheduleTask(TASK_DISAPPEAR, Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.BodyFadeOutTimes.ELECTROCUTED_BODY_FADE_OUT);
        } else if (lastDamagedBy == Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.DamageEffect.FLAME) {
            visible = false;
            getFactories().getMiscFactory().createIndependentEffect(MiscFactory.IndependentEffectType.BURNING_THING, x, y);
            TASK_DISAPPEAR.run();
        }
    }


    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (getElements().getEffectsManager().isBioHazardSmokeExists() && hasLanded()) {
            Parastrike.getSoundPlayer().playSound(randomizer.nextBoolean() ? SFX.Enemies.GroundUnits.Paratroopers.COUGH_1 : SFX.Enemies.GroundUnits.Paratroopers.COUGH_2);
            onGib();
        }
        if (hasLanded()) {
            shoot();
        } else {
            if (isParachuteActive()) {
                swingSinkAnimation();
                placeParachute();
                if (isYpositionEqualsGround()) {
                    land();
                }
            } else {
                fallToDeath();
            }
        }
        updateParachute(delta);
    }

    private boolean isParachuteActive() {
        return parachute != null && !parachute.isTorn();
    }

    protected void shoot() {
        if (hasTarget()) {
            scheduleTask(TASK_SHOOT, reloadTime);
        }
    }

    private void updateParachute(float delta) {
        if (parachute == null) {
            return;
        }
        parachute.onUpdate(delta);
        if (parachute.isDestroyed()) {
            destroyParachute();
        }
    }

    public void land() {
        Level.Feat pure = Level.Feat.PURE;
        Level currentLevel = getElements().getCurrentLevel();
        if (currentLevel.getFeat(pure)) currentLevel.failFeat(pure);
        landed = true;
        fallingSpeed = 0;
        setSpriteDirection(0);
        if (parachute != null) {
            parachute.land();
        }
        setFacingSprite(Assets.GFX.Sheets.ImagesNames.FACING_LEFT, Assets.GFX.Sheets.ImagesNames.FACING_RIGHT, Rules.Misc.GENERAL_ANIMATION_FRAME_DURATION);
    }

    private void placeParachute() {
        parachute.setX(x);
        parachute.setY(y);
        parachute.setSpriteDirection(getSpriteDirection());
    }

    @Override
    protected void onFulfillingOrder(float speed) {
        //Do nothing.
    }

    @Override
    protected void onWaitingForNewOrder(float delta) {
        //Do nothing.
    }

    private void swingSinkAnimation() {
        if (x <= 0 || (x >= Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2 && x < Rules.Player.SideKicks.RIGHT_POSITION_X)) {
            direction = Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.Direction.LOWEST_SINK_DIRECTION_DEGREES + Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.Direction.HIGHEST_SINK_DIRECTION_DEGREES_RELATIVE;
        } else if (x >= Rules.System.Resolution.WIDTH_TARGET_RESOLUTION || (isOnLeftSide() && x > Rules.Player.SideKicks.LEFT_POSITION_X)) {
            direction = Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.Direction.LOWEST_SINK_DIRECTION_DEGREES;
        }
        if (swingsAnimation) {
            if (spriteDirection >= Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.Direction.HIGHEST_SINK_SPRITE_DIRECTION_DEGREES_RELATIVE && spriteDirection < Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.Direction.LOWEST_SINK_SPRITE_DIRECTION_DEGREES) {
                spriteDirectionSpeed *= -1;
            }
            setSpriteDirection((float) (spriteDirection + spriteDirectionSpeed));
        }
    }

    private void fallToDeath() {
        gravityStatus = true;
        setSpriteDirection((float) (spriteDirection + (spriteDirectionSpeed * 2)));
        if (isYpositionEqualsGround()) {
            Parastrike.getSoundPlayer().stopSound(myFallingSoundName, myFallingSoundId);
            if (fallingSpeed <= Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.Speed.MAXIMUM_SAFE_FALL_SPEED) {
                land();
                return;
            }
            onGib();
        }
    }

    protected void setFacingSprite(String left, String right, float frameDuration) {
        if (isOnLeftSide()) {
            setRegion(right, frameDuration);
        } else {
            setRegion(left, frameDuration);
        }
    }

    @Override
    public boolean overlapsWithOrigin(GameObject gameObject) {
        if (parachute != null && !hasLanded()) {
            //In case it is not another paratrooper with a parachute.
            if (gameObject instanceof Paratrooper) {
                Paratrooper paratrooper = (Paratrooper) gameObject;
                if (paratrooper.getParachute() != null) {
                    return super.overlapsWithOrigin(gameObject);
                }
            }
            //In case it is not a flying aircraft.
            if (gameObject instanceof AirCraft) {
                AirCraft airCraft = (AirCraft) gameObject;
                if (!airCraft.isDead()) {
                    return super.overlapsWithOrigin(gameObject);
                }
            }
            if (parachute.overlapsWithOrigin(gameObject)) {
                if (gameObject instanceof Bullet) {
                    if (!(getPlayerHandler().getTurret().getCurrentWeaponIndex() == BulletType.SHOCK_WAVE && gameObject instanceof ShockWave)) {
                        gameObject.onDestroy(true);
                    }
                    tearParachute();
                    playFallingSound();
                } else if (gameObject instanceof AirCraft) {
                    AirCraft airCraft = (AirCraft) gameObject;
                    if (parachute.isVisible() && airCraft.isDead()) {
                        tearParachute();
                        playFallingSound();
                    }
                }
            }
        }
        return super.overlapsWithOrigin(gameObject);
    }

    public void destroyParachute() {
        if (parachute != null) {
            parachute.onDestroy();
            parachute.free();
            parachute = null;
        }
    }

    private void setParachute(Parachute parachute) {
        this.parachute = parachute;
    }

    private Parachute getParachute() {
        return parachute;
    }

    private boolean isYpositionEqualsGround() {
        return y - getHeight() <= Rules.Level.GROUND_Y;
    }

    public void collisionInteraction(CollideableGameObject gameObject) {
        if (!dead) {
            gameObject.onCollision(this);
        }
    }

    @Override
    public void onCollision(Enemy gameObject) {
        if (gameObject instanceof AirCraft) {
            if ((parachute != null && parachute.isVisible() && !hasLanded()) || (hasLanded())) {
                AirCraft airCraft = (AirCraft) gameObject;
                if (airCraft.isDead()) {
                    onDestroy(false);
                    rewardPlayer();
                }
            }
        }
    }

    @Override
    public void onCollision(Bullet bullet) {
        if (!dead && bullet.isPlayerBullet()) {
            if (bullet instanceof Flame) {
                changeHealth(-bullet.getDamageValue(), Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.DamageEffect.FLAME);
            } else if (getPlayerHandler().getTurret().getCurrentWeaponIndex() == BulletType.SHOCK_WAVE && bullet instanceof ShockWave) {
                ShockWave shockWave = (ShockWave) bullet;
                if (!shockWave.hasHit()) {
                    getFactories().getMiscFactory().createFlyingParts(MiscFactory.MiscType.FLYING_PART, x, y, 3);
                    shockWave.setHit(true);
                    changeHealth(-bullet.getDamageValue(), Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.DamageEffect.ELECTRICITY);
                }
            } else {
                changeHealth(-bullet.getDamageValue());
            }
        }
    }

    @Override
    protected void checkDamageCritical() {
        //Do nothing.
    }

    public void flyAwayFrom(GameObject gameObject) {
        if (dead) {
            return;
        }
        speed = Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.Speed.FLY_AWAY_SPEED;
        if (hasLanded()) {
            y += 1;
            direction = (gameObject.getX() > x) ? Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.FLY_UP_LEFT : Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.FLY_UP_RIGHT;
            setRegion(Assets.GFX.Sheets.ImagesNames.FALLING);
            landed = false;
            setDeadFlag(true);
        } else {
            gravityStatus = true;
            if (isParachuteActive()) {
                playFallingSound();
            }
            tearParachute();
            direction = GameUtils.getDirectionToPoint(gameObject.getX(), gameObject.getY(), x, y);
        }
    }

    private void playFallingSound() {
        if (myFallingSoundId != -1) {
            return;
        }
        int random = randomizer.nextInt(4);
        SFX sound;
        if (random == 0) {
            sound = SFX.Enemies.GroundUnits.Paratroopers.FALLING_1;
        } else if (random == 1) {
            sound = SFX.Enemies.GroundUnits.Paratroopers.FALLING_2;
        } else if (random == 2) {
            sound = SFX.Enemies.GroundUnits.Paratroopers.FALLING_3;
        } else {
            sound = SFX.Enemies.GroundUnits.Paratroopers.FALLING_4;
        }
        myFallingSoundName = sound;
        myFallingSoundId = Parastrike.getSoundPlayer().playSound(sound);
    }

    private void tearParachute() {
        if (parachute != null) {
            parachute.tear();
            if (!hasLanded()) {
                setRegion(Assets.GFX.Sheets.ImagesNames.FALLING);
            }
        }
    }

    @Override
    protected void onGib() {
        super.onGib();
//        int numberOfBodyParts = randomizer.nextInt(Rules.Enemies.Paratroopers.GeneralAttributes.MAX_NUMBER_OF_BODY_PARTS) + 1;
//        createFlyingParts(Rules.GameObjectIds.BODY_PART, x, y, numberOfBodyParts);
        Parastrike.getSoundPlayer().playSound(SFX.Misc.GIB);
        getFactories().getMiscFactory().createIndependentEffect(MiscFactory.IndependentEffectType.STARS, x, y - (height / 2));
        tearParachute();
        rewardPlayer();
    }

    private final Timer.Task TASK_DISAPPEAR = new Timer.Task() {
        @Override
        public void run() {
            if (!isDead()) {
                return;
            }
            onDestroy();
        }
    };

    private final Timer.Task TASK_OPEN_PARACHUTE = new Timer.Task() {
        @Override
        public void run() {
            if (isDead()) {
                return;
            }
            animating = true;
            gravityStatus = false;
            speed = Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.Speed.SINK_SPEED;
            setDirection(randomizer.nextInt(Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.Direction.HIGHEST_SINK_DIRECTION_DEGREES_RELATIVE) + Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.Direction.LOWEST_SINK_DIRECTION_DEGREES);
            if (parachute != null) {
                parachute.openUp();
            }
            swingsAnimation = true;
        }
    };

    private final Timer.Task TASK_SHOOT = new Timer.Task() {
        @Override
        public void run() {
            if (!dead && hasLanded() && target != null && !target.isDead()) {
                setFacingSprite(Assets.GFX.Sheets.ImagesNames.SHOOT_LEFT, Assets.GFX.Sheets.ImagesNames.SHOOT_RIGHT, Rules.Enemies.GeneralAttributes.SHOOT_FRAME_DURATION);
                scheduleTask(TASK_STOP_SHOOTING, Rules.Enemies.GeneralAttributes.SHOOT_TIME);
                target.changeHealth(-attackDamage);
                int numberOfParts = randomizer.nextInt(3) + 2;
                float randomX = target.getX() + randomizer.nextInt((int) target.getWidth()) - target.getOriginX();
                float randomY = target.getY() + randomizer.nextInt((int) target.getHeight()) - target.getOriginY();
                MiscFactory miscFactory = getFactories().getMiscFactory();
                miscFactory.createFlyingParts(MiscFactory.MiscType.FLYING_PART, randomX, randomY, numberOfParts);
                miscFactory.createExplosion(MiscFactory.ExplosionType.BULLET_IMPACT, randomX, randomY);
                Parastrike.getSoundPlayer().playSound(SFX.Enemies.GroundUnits.Paratroopers.INFANTRY_SHOOT);
            }
        }
    };

    @Override
    public boolean isPlayerCharacter() {
        return false;
    }

    @Override
    public boolean setTarget(PlayerCharacter otherObject) {
        target = otherObject;
        return true;
    }

    protected final Timer.Task TASK_STOP_SHOOTING = new Timer.Task() {
        @Override
        public void run() {
            if (!dead && hasLanded()) {
                setFacingSprite(Assets.GFX.Sheets.ImagesNames.FACING_LEFT, Assets.GFX.Sheets.ImagesNames.FACING_RIGHT, Rules.Misc.GENERAL_ANIMATION_FRAME_DURATION);
            }
        }
    };

}
