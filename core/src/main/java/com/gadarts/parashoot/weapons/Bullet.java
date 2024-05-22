package com.gadarts.parashoot.weapons;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.model.CollideableGameObject;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.interfaces.Targeting;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.player.Bonus;
import com.gadarts.parashoot.player.PlayerCharacter;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 21/02/2015.
 */
public class Bullet extends CollideableGameObject implements Targeting<Enemy> {

    private boolean homing;
    private float damageValue;
    public Enemy target;
    protected MiscFactory.ExplosionType myExplosion;
    private float homingRotation;
    protected SFX explosionSound;
    private boolean playerBullet;
    private Factories factories;

    public Bullet() {
        super(true);
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.PLAYER_BULLETS_DATA_FILE);
    }

    public void init(String spriteName, float x, float y, float direction, float speed, WarScreenElements mechanics, Factories factories) {
        init(spriteName, x, y, direction, speed, 0, mechanics, factories);
    }

    public void init(String spriteName, float x, float y, float direction, float speed, float damageValue, WarScreenElements mechanics, Factories factories) {
        init(spriteName, x, y, direction, speed, damageValue, true, mechanics, factories, true);
    }

    public void init(String spriteName, float x, float y, float direction, float speed, float damageValue, WarScreenElements mechanics, Factories factories, boolean randomize) {
        init(spriteName, x, y, direction, speed, damageValue, true, mechanics, factories, randomize);
    }

    public void init(String spriteName, float x, float y, float direction, float speed, float damageValue, boolean allowLight, WarScreenElements mechanics, Factories factories) {
        init(spriteName, x, y, direction, speed, damageValue, allowLight, mechanics, factories, true);
    }

    public void init(String spriteName, float x, float y, float direction, float speed, float damageValue, boolean allowLight, WarScreenElements mechanics, Factories factories, boolean randomizeSpeed) {
        super.init(spriteName, x, y, direction, speed, mechanics);
        this.factories = factories;
        this.damageValue = damageValue;
        this.homingRotation = Rules.Cannons.IronDome.HOMING_ROTATION;
        this.myExplosion = MiscFactory.ExplosionType.SMALL_EXPLOSION;
        this.explosionSound = SFX.Misc.HIT;
        this.target = null;
        homing = false;
        setOrigin(width / 2, height / 2);
        setSpriteDirection(direction);
        if (allowLight) {
            createLight();
        }
        if (randomizeSpeed) {
            randomizeSpeed();
        }
    }

    private void randomizeSpeed() {
        double random = Math.random();
        speed += ((random > 0.5) ? 1 : -1) * ((speed * 0.3) * random);
    }

    public Factories getFactories() {
        return factories;
    }

    @Override
    public void collisionInteraction(CollideableGameObject other) {
        other.onCollision(this);
    }

    @Override
    public void onCollision(Bullet other) {
        if ((isPlayerBullet() && !other.isPlayerBullet() && !(other instanceof EnemyMiniGunBullet)) || (!isPlayerBullet() && other.isPlayerBullet())) {
            onDestroy(true);
        }
    }

    @Override
    public void onCollision(Enemy other) {
        if (isPlayerBullet()) {
            onDestroy(true);
        }
    }

    @Override
    public void onCollision(PlayerCharacter other) {
        if (!isPlayerBullet() && !other.isDead()) {
            onDestroy(true);
        }
    }

    @Override
    public void onCollision(Bonus other) {
        if (isPlayerBullet() && !other.hasBeenObtained() && other.isObtainAble()) {
            onDestroy(true);
        }
    }

    public float getDamageValue() {
        return damageValue;
    }

    protected void createMyExplosion() {
        if (myExplosion == null) {
            return;
        }
        factories.getMiscFactory().createExplosion(myExplosion, x, y);
        if (explosionSound != null) {
            Parastrike.getSoundPlayer().playSound(explosionSound);
        }
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (target != null && !target.isDestroyed()) {
            if (homing) {
                float directionToObject = GameUtils.getDirectionToPoint(x, y, target.getCenterX(), target.getCenterY());
                if (direction < directionToObject) {
                    if (Math.abs(direction - directionToObject) < 180) {
                        direction += homingRotation;
                    } else {
                        direction -= homingRotation;
                    }
                } else {
                    if (Math.abs(direction - directionToObject) < 180) {
                        direction -= homingRotation;
                    } else {
                        direction += homingRotation;
                    }
                }
            }
        }
    }

    @Override
    protected void onGroundCollision() {
        onDestroy(true);
    }

    @Override
    public void onDestroy(boolean doDestroySequence) {
        onDestroy(doDestroySequence, true);
    }

    @Override
    protected void onDestroy(boolean doDestroySequence, boolean clearScheduler) {
        super.onDestroy(doDestroySequence, clearScheduler);
        if (doDestroySequence) {
            createMyExplosion();
        }
    }

    public void setHomingRotation(float homingRotation) {
        this.homingRotation = homingRotation;
    }

    public void emitSmoke(String smoke) {
        getElements().getEffectsManager().createEffect(smoke, this);
    }

    public void setExplosion(MiscFactory.ExplosionType explosion) {
        setExplosion(explosion, null);
    }

    @Override
    public boolean isPlayerCharacter() {
        return true;
    }

    public boolean isPlayerBullet() {
        return playerBullet;
    }

    @Override
    public boolean hasTarget() {
        return target != null && !target.isDead();
    }

    public boolean setTarget(Enemy target) {
        if (target == null) {
            return false;
        }
        this.target = target;
        scheduleTask(TASK_START_HOMING, Rules.Cannons.IronDome.START_HOMING_DELAY);
        return true;
    }

    public void setExplosion(MiscFactory.ExplosionType explosion, SFX explosionSound) {
        this.explosionSound = explosionSound;
        this.myExplosion = explosion;
    }

    public void setDamageValue(float damageValue) {
        this.damageValue = damageValue;
    }

    private final Timer.Task TASK_START_HOMING = new Timer.Task() {
        @Override
        public void run() {
            homing = true;
        }
    };

    public void setAsPlayerBullet(boolean asPlayerBullet) {
        this.playerBullet = asPlayerBullet;
    }
}
