package com.gadarts.parashoot.model;

import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.player.Bonus;
import com.gadarts.parashoot.player.PlayerCharacter;
import com.gadarts.parashoot.weapons.Bullet;

public abstract class CollideableGameObject extends PoolableGameObject {
    private boolean singleSpatialCell;

    protected void init(String spriteName, float x, float y, float direction, float speed, WarScreenElements mechanics) {
        super.init(spriteName, x, y, direction, speed, mechanics);
    }

    /**
     * Visitor pattern - this AC would pass itself to it's collider.
     *
     * @param other It's collider.
     */
    public abstract void collisionInteraction(CollideableGameObject other);

    public abstract void onCollision(Bullet other);

    public abstract void onCollision(Enemy other);

    public abstract void onCollision(PlayerCharacter other);

    public abstract void onCollision(Bonus other);

    public CollideableGameObject() {
        this(false);
    }

    public CollideableGameObject(boolean singleSpatialCell) {
        this.singleSpatialCell = singleSpatialCell;
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (!isDestroyed()) {
            getElements().getInteractionsManager().addObjectToSpatial(this);
        }
    }

    boolean isSingleSpatialCell() {
        return singleSpatialCell;
    }
}
