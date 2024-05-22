package com.gadarts.parashoot.player.sidekick;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.PoolableGameObject;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.interfaces.Targeting;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;
import com.gadarts.parashoot.weapons.BulletType;

/**
 * Created by Gad on 11/10/2015.
 */
public class SideKickHead extends PoolableGameObject implements Targeting<Enemy> {
    protected float stepsAhead = 50;
    protected com.gadarts.parashoot.player.HeadedSideKick body;
    protected Bullet myBullet;
    private BulletType bulletType;
    protected float shootingFrameDuration;
    protected Enemy target;

    private Factories factories;

    public void init(float x, float y, WarScreenElements mechanics, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.HEAD, x, y, 0, 0, mechanics);
        this.factories = factories;
        setOrigin(width / 2, height / 2);
    }

    public void setBody(com.gadarts.parashoot.player.HeadedSideKick body) {
        this.body = body;
    }

    public Factories getFactories() {
        return factories;
    }

    public void aimToTargetAndShoot() {
        float stepX = (float) (Math.cos(Math.toRadians(target.getDirection())) * target.getSpeed());
        float stepY = (float) (Math.sin(Math.toRadians(target.getDirection())) * target.getSpeed());
        float directionToObject = GameUtils.getDirectionToPoint(x, y, target.getCenterX() + stepsAhead * stepX, target.getCenterY() + stepsAhead * stepY);
        if (directionToObject <= direction + 2 && directionToObject >= direction - 2) {
            if (body.isGunLoaded()) {
                shoot();
            }
        } else {
            if (direction < directionToObject) {
                if (Math.abs(direction - directionToObject) < 180) {
                    setDirection(direction + 1);
                } else {
                    setDirection(direction - 1);
                }
            } else {
                if (Math.abs(direction - directionToObject) < 180) {
                    setDirection(direction - 1);
                } else {
                    setDirection(direction + 1);
                }
            }
        }
    }

    protected void shoot() {
        createBullet();
        Parastrike.getSoundPlayer().playSound(body.getShootingSoundFileName());
        body.setGunState(false);
        scheduleTask(body.TASK_RELOAD, body.getShootingRate());
        setRegion(Assets.GFX.Sheets.ImagesNames.SHOOT, shootingFrameDuration);
        scheduleTask(TASK_SET_IDLE_IMAGE, Rules.Player.SideKicks.Flamer.SHOOTING_IMAGE_SHOWN_DURATION);
    }

    protected void createBullet() {
        float cosPos = (float) (Math.cos(Math.toRadians(direction)) * 25);
        float sinPos = (float) (Math.sin(Math.toRadians(direction)) * 25);
        myBullet = getFactories().getBulletFactory().createBullet(bulletType, x + cosPos, y + sinPos, direction);
        myBullet.setDamageValue(body.getAttackStrength());
    }


    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (hasTarget()) {
            aimToTargetAndShoot();
        }
    }

    @Override
    public boolean isPlayerCharacter() {
        return true;
    }

    public boolean hasTarget() {
        return target != null && !target.isDead() && !target.isDestroyed();
    }

    private final Timer.Task TASK_SET_IDLE_IMAGE = new Timer.Task() {
        @Override
        public void run() {
            setRegion(Assets.GFX.Sheets.ImagesNames.HEAD);
        }
    };

    public void setBulletType(BulletType bullet) {
        this.bulletType = bullet;
    }

    public void setShootingFrameDuration(float frameDuration) {
        this.shootingFrameDuration = frameDuration;
    }

    public boolean setTarget(Enemy target) {
        this.target = target;
        return true;
    }

}
