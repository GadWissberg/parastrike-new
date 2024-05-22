package com.gadarts.parashoot.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.player.sidekick.SideKickHead;
import com.gadarts.parashoot.screens.WarScreen;

/**
 * Created by gadw1_000 on 06-Jul-15.
 */
public abstract class HeadedSideKick extends SideKick {

    protected SideKickHead head;

    public void init(float x, int hp, WarScreen warScreen) {
        super.init(x, hp, warScreen);
    }

    public void setHead(SideKickHead head) {
        this.head = head;
        head.setBody(this);
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        getWarScreen().getFactories().getMiscFactory().createExplosion(MiscFactory.ExplosionType.EXPLOSION, head.getX(), head.getY());
        head.onDestroy();
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (!head.isDestroyed()) {
            head.onUpdate(delta);
        }
    }

    @Override
    protected void shoot() {
        if (!head.hasTarget()) {
            head.setTarget(target);
        }
    }

    @Override
    public boolean setTarget(Enemy target) {
        super.setTarget(target);
        head.setTarget(target);
        return true;
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        super.onDraw(batch);
        if (!head.isDestroyed()) {
            head.onDraw(batch);
        }
    }

    public void setHeadScaleToDirection() {
        if (isOnLeftSide()) {
            head.setScaleY(-1);
            head.setDirection(180);
        }
    }
}
