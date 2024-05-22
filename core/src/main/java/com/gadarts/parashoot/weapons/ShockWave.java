package com.gadarts.parashoot.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 06/12/2015.
 */
public class ShockWave extends Bullet {
    private boolean hit;
    private float turretX;
    private float turretY;

    public ShockWave() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.SideKicks.TESLA_DATA_FILE);
    }

    public void init(float x, float y, float direction, WarScreenElements mechanics, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.ELECTRICITY, x, y, direction, 0, mechanics, factories);
        scheduleTask(TASK_DESTROY, Rules.Player.SideKicks.Tesla.ELECTRICITY_LIFE_DURATION);
        setOrigin(0, height / 2);
        turretX = x;
        turretY = y;
        hit = false;
    }

    @Override
    public float getCenterX() {
        return x;
    }

    @Override
    public float getCenterY() {
        return y;
    }

    public void activate(float targetX, float targetY) {
        x = targetX;
        y = (targetY >= Rules.Level.GROUND_Y) ? targetY : Rules.Level.GROUND_Y;
        getFactories().getMiscFactory().createExplosion(MiscFactory.ExplosionType.SHOCK_WAVE_BLAST, x, y);
        setDirection(GameUtils.getDirectionToPoint(x, y, turretX, turretY));
        setWidth(GameUtils.getDistanceBetweenTwoPoints(x, y, turretX, turretY));
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        if (currentFrame != null || !isVisible()) {
            batch.draw(currentFrame, x - getOriginX(), y - getOriginY(), getOriginX(), getOriginY(), width, height, scaleX, scaleY, getSpriteDirection());
        }
    }

    @Override
    public void onCollision(Enemy other) {
        //Do nothing.
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public boolean hasHit() {
        return hit;
    }
}
